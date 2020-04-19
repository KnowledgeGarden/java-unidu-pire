/*
 Copyright 2000-2005 University Duisburg-Essen, Working group "Information Systems"

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 this file except in compliance with the License. You may obtain a copy of the
 License at

 http://www.apache.org/licenses/LICENSE-2.0 

 Unless required by applicable law or agreed to in writing, software distributed
 under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 CONDITIONS OF ANY KIND, either express or implied. See the License for the
 specific language governing permissions and limitations under the License. 
 */

// $Id: ThreadedCaller.java,v 1.8 2005/02/28 22:27:55 nottelma Exp $
package de.unidu.is.util;

import java.util.*;

/**
 * A class providing parallel execution of its method call(Object) (which has to
 * be implemented in subclasses for an array of "targets". Targets can be
 * arbitrary objects which are delivered to call(Object). The results are
 * collected in a map.
 * <p>
 * <p>
 * Currently, the threaded calling is disabled, and sequential order is enabled.
 *
 * @author Henrik Nottelmann
 * @version $Revision: 1.8 $, $Date: 2005/02/28 22:27:55 $
 * @since 2001-08-21
 */
public abstract class ThreadedCaller {

    /**
     * Map, containing all targets (as keys) and their results (as values).
     */
    protected final Map results;

    /**
     * The targets.
     */
    protected final Object[] targets;
//
//    /**
//     * Creates a new instance.
//     *
//     * @param iterator iterator, producing all targets
//     */
//    public ThreadedCaller(Iterator iterator) {
//        this(CollectionUtilities.toList(iterator));
//    }

    /**
     * Creates a new instance.
     *
     * @param list targets
     */
    public ThreadedCaller(List list) {
        results = new HashMap();
        targets = list.toArray();
    }

    /**
     * Creates a new instance.
     *
     * @param targets targets
     */
    public ThreadedCaller(Object[] targets) {
        results = new HashMap();
        this.targets = targets;
    }

    /**
     * The method performing the actual work for a target.
     * <p>
     * <p>
     * Subclasses have to overwrite this method.
     *
     * @param target target for this call
     * @return result of this call
     */
    public abstract Object call(Object target);

    /**
     * Starts the execution. This implementation uses the sequential variant.
     *
     * @return map, containing the targets and their results
     */
    public Map start() {
        return startSequential();
    }

    /**
     * Starts the parallel execution. For each target, a thread is created and
     * started. The thread calls the method call(Object) with its target and
     * puts the result in the result map. This method returns after all threads
     * terminated.
     *
     * @return map, containing the targets and their results
     */
    public Map startAll() {
        final Set completed = new HashSet();
        try {
            for (int i = 0; i < targets.length; i++) {
                final int j = i;
                new Thread(() -> {
                    Object obj = null;
                    try {
                        obj = call(targets[j]);
                    } catch (Exception ex) {
                        Log.error(ex);
                    }
                    try {
                        synchronized (results) {
                            if (obj != null)
                                results.put(targets[j], obj);
                            completed.add(targets[j]);
                            results.notify();
                        }
                    } catch (Exception ex) {
                        Log.error(ex);
                    }
                }).start();
            }
            synchronized (results) {
                while (completed.size() != targets.length)
                    results.wait();
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        }
        return results;
    }

    /**
     * Starts the sequential execution, without creating new threads. This
     * method calls the method call(Object) with its target and puts the result
     * in the result map.
     *
     * @return map, containing the targets and their results
     */
    public Map startSequential() {
        for (Object target : targets) {
            Object obj = null;
            try {
                obj = call(target);
            } catch (Exception ex) {
                Log.error(ex);
            }
            // add result to result map
            if (obj != null)
                results.put(target, obj);
        }
        return results;
    }

    /**
     * Starts the parallel execution. 10 threads are used for handling the
     * targets. The threads call the method call(Object) with its target and
     * puts the result in the result map. This method returns after all threads
     * terminated.
     *
     * @return map, containing the targets and their results
     */
    public Map start10() {
        final Set completed = new HashSet();
        // we need a new collection
        final List targetList = new ArrayList();
        Collections.addAll(targetList, targets);
        try {
            final Thread thread = Thread.currentThread();
            for (int i = 0; i < 10 && i < targets.length; i++) {
                final int j = i;
                new Thread(() -> {
                    while (true) {
                        Object target;
                        // get next target
                        synchronized (targetList) {
                            if (targetList.isEmpty())
                                break;
                            target = targetList.get(0);
                            targetList.remove(0);
                        }
                        // work on target
                        Object obj = null;
                        try {
                            obj = call(target);
                        } catch (Exception ex) {
                            Log.error(ex);
                        }
                        // add result to result map
                        synchronized (results) {
                            if (obj != null)
                                results.put(target, obj);
                            completed.add(target);
                        }
                    }
                    // nothing more to do for this thread
                    synchronized (completed) {
                        completed.notifyAll();
                    }
                }).start();
            }
            synchronized (completed) {
                while (completed.size() != targets.length)
                    completed.wait();
            }
        } catch (Exception ex) {
            de.unidu.is.util.Log.error(ex);
        }
        return results;
    }

    /**
     * Returns a map, containing all targets (as keys) and their results (as
     * values).
     *
     * @return map, containing the targets and their results
     */
    public synchronized Map getResults() {
        return results;
    }

    /**
     * Returns the result object of the specified target.
     *
     * @param target target
     * @return result object of target, or null if no result exists
     */
    public synchronized Object getResult(Object target) {
        return results.get(target);
    }

    /**
     * Returns an array, containing all targets.
     *
     * @return array with all targets
     */
    public Object[] getTargets() {
        return targets;
    }

}