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

// $Id: StopWatch.java,v 1.7 2005/03/02 09:20:51 nottelma Exp $
package de.unidu.is.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple stop watch.
 * 
 * @author Henrik Nottelmann
 * @since 2003-10-15
 * @version $Revision: 1.7 $, $Date: 2005/03/02 09:20:51 $
 */
public class StopWatch {

	/**
	 * Name of this stop watch.
	 *  
	 */
	private String name;

	/**
	 * Time difference (in millis) betweens starts and stops.
	 */
	private long diffs;

	/**
	 * Time (in millis) of the last start event.
	 */
	private long start;

	/**
	 * Number of start event.
	 */
	private int nums;

	/**
	 * Formatter for time difference.
	 */
	private SimpleDateFormat sdf;

	/**
	 * Formatter for time difference in minutes.
	 */
	private SimpleDateFormat minuteSDF;

	private DecimalFormat form2 = new DecimalFormat("00");
	private DecimalFormat form4 = new DecimalFormat("0000");

	/**
	 * Creates a new stop watch.
	 */
	public StopWatch() {
		this("StopWatch");
	}

	/**
	 * Creates a new stop watch.
	 * 
	 * @param name
	 *                   name of the stop watch
	 */
	public StopWatch(String name) {
		this.name = name;
		sdf = new SimpleDateFormat("DD 'days', HH:mm:ss,SSS");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		minuteSDF = new SimpleDateFormat("mm:ss,SSS");
		minuteSDF.setTimeZone(TimeZone.getTimeZone("UTC"));
		reset();
	}

	/**
	 * Starts the watch.
	 */
	public void start() {
		start = System.currentTimeMillis();
		nums++;
	}

	/**
	 * Stops the watch.
	 */
	public void stop() {
		long end = System.currentTimeMillis();
		diffs += (end - start);
	}

	/**
	 * Resets the watch. The time difference, the start time and the number of
	 * start events are set to zero.
	 */
	public void reset() {
		diffs = 0;
		start = 0;
		nums = 0;
	}

	/**
	 * Returns the total (accumulated) time difference in millis.
	 * 
	 * @return total time difference in millis
	 */
	public long getTotalMillis() {
		return diffs / nums;
	}

	/**
	 * Returns the average time difference in millis.
	 * 
	 * @return average time difference in millis
	 */
	public long getAverageMillis() {
		return diffs / nums;
	}

	/**
	 * Returns a string representation of the time difference (in minutes).
	 * 
	 * @return string representation of the time difference (in minutes)
	 */
	public String toString() {
		long rest = diffs;
		int millis = (int) rest % 1000;
		rest /= 1000;
		int secs = (int) rest % 60;
		rest /= 60;
		int minutes = (int) rest % 60;
		rest /= 60;
		int hours = (int) rest % 24;
		rest /= 24;
		int days = (int) rest;
		return name + ": " + days + " days, " + hours + " hours, "
				+ form2.format(minutes) + ":" + form2.format(secs) + ","
				+ form4.format(millis) + " min";
	}

	/**
	 * Returns a string representation of the time difference.
	 * 
	 * @return string representation of the time difference
	 * @deprecated Use toString() instead
	 */
	public String asComplete() {
		return name + ": " + sdf.format(new Date(diffs)) + " min";
	}

	/**
	 * Returns a string representation of the time difference (in minutes).
	 * 
	 * @return string representation of the time difference (in minutes)
	 */
	public String asMins() {
		long rest = diffs;
		int millis = (int) rest % 1000;
		rest /= 1000;
		int secs = (int) rest % 60;
		rest /= 60;
		int minutes = (int) rest % 60;
		return name + ": " + minutes + ":" + form2.format(secs) + ","
				+ form4.format(millis) + " min";
	}

	/**
	 * Returns a string representation of the time difference (in seconds).
	 * 
	 * @return string representation of the time difference (in seconds)
	 */
	public String asSecs() {
		return name + ": " + (diffs / 1000.0) + " sec";
	}

	/**
	 * Returns a string representation of the time difference (in millis).
	 * 
	 * @return string representation of the time difference (in millis)
	 */
	public String asMillis() {
		return name + ": " + diffs + " msec";
	}

}