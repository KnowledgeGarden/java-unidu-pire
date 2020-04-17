// $Id: ThreadedCallerExample.java,v 1.1 2005/02/25 14:33:29 nottelma Exp $
package de.unidu.is.util.examples;

import java.util.Map;
import java.util.Random;

import de.unidu.is.util.ThreadedCaller;

/**
 * An example for using ThreadedCaller.
 * 
 * @author nottelma
 * @since 25-Feb-2005
 * @version $Revision: 1.1 $, $Date: 2005/02/25 14:33:29 $
 */
public class ThreadedCallerExample {

	public static void main(String[] args) {
		final Random random = new Random();
		Object[] targets = new String[30];
		for (int i = 0; i < targets.length; i++)
			targets[i] = "Target #" + i;

		Map results = new ThreadedCaller(targets) {
			public Object call(Object target) {
				System.out.println(
					Thread.currentThread() + "\t" + target);
				try {
					synchronized (random) {
						Thread.sleep(random.nextInt(2000));
					}
				} catch (Exception ex) {
					de.unidu.is.util.Log.error(ex);
				}
				return null;
			}
		}
		.start();
		System.out.println("FINISH");
	}

}
