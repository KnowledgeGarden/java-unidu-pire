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

 
// $Id: Gnuplot.java,v 1.10 2005/03/14 17:33:15 nottelma Exp $
package de.unidu.is.gnuplot;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.unidu.is.util.Config;

/**
 * A class for creating evaluation plots with gnuplot, a common
 * Unix/Linux plotting tool with a textual interface. Thus, this class
 * might work only on Unix/Linux; anyway, it is not tested under Windows.<p>
 *
 * This class allows for 
 * <ul>
 * <li>setting the title (only used if not the default value
 * <code>null</code>),</li>
 * <li>setting the labels for the x-axis and the y-axis  (only used if
 * not the default value <code>null</code>),</li>
 * <li>setting the minimum and maximum x values (only used if different, which
 * is the default)</li>
 * <li>setting the minimum and maximum y values (only used if different, which
 * is the default)</li>
 * <li>setting the filename of the output file (in PDF format), without
 * extension .df!, and</li>
 * <li>adding one or more data sources (instances of 
 * <code>mind.evaluation.gnuplot.Source</code> or one of its subclasses).</li>
 * </ul>
 *
 * A <code>mind.evaluation.gnuplot.Source</code> describes a single plot curve
 * which might be a function, a discrete (empirical) distribution or anything
 * else.<p>
 *
 * The path to gnuplot is taken from the property <code>gnuplot.bin</code>.
 *
 * @author Henrik Nottelmann
 * @since 2002-04-09
 * @version $Revision: 1.10 $, $Date: 2005/03/14 17:33:15 $
 */
public class Gnuplot {

	/**
	 * The name (absolute or relative) of the output EPS file, without
	 * extension .eps.
	 *
	 */
	protected String outputfile;

	/**
	 * The title of the plot. If this variable is <code>null</code> (default),
	 * no title is used.
	 *
	 */
	protected String title;

	/**
	 * The label for the x-axis of the plot. If this variable is
	 * <code>null</code> (default), no label is used.
	 *
	 */
	protected String xlabel;

	/**
	 * The label for the y-axis of the plot. If this variable is
	 * <code>null</code> (default), no label is used.
	 *
	 */
	protected String ylabel;

	/**
	 * The maximum value for the x-axis (default is <code>0</code>).
	 *
	 */
	protected double maxx;

	/**
	 * The maximum value for the y-axis (default is <code>0</code>).
	 *
	 */
	protected double maxy;

	/**
	 * The minimum value for the x-axis (default is <code>0</code>).
	 *
	 */
	protected double minx;

	/**
	 * The minimum value for the y-axis (default is <code>0</code>).
	 */
	protected double miny;

	/**
	 * The registered sources. Each source describes one plot curve.
	 */
	protected List sources;

	/**
	 * Flag indicating if this is a b/w plot. Default is color.
	 */
	protected boolean bw;

	/**
	 * Flag indicating if this is a xfig plot. Default is pdf.
	 */
	protected boolean fig;

	/**
	 * Sets the name (absolute or relative) of the output EPS file,
	 * without extension .eps.
	 *
	 * @param outputfile name of the output file
	 */
	public void setOutputfile(String outputfile) {
		this.outputfile = outputfile;
	}

	/**
	 * Sets the name (absolute or relative) of the output EPS file,
	 * without extension .eps.
	 *
	 * @param outputfile name of the output file
	 */
	public void setOutputfile(File outputfile) {
		this.outputfile = outputfile.toString();
	}

	/**
	 * Sets the b/w mode.
	 */
	public void setBW() {
		this.bw = true;
	}

	/**
	 * Sets the color mode.
	 *
	 */
	public void setColor() {
		this.bw = false;
	}

	/**
	 * Sets the fig output mode.
	 *
	 */
	public void setFIG() {
		this.fig = true;
	}

	/**
	 * Sets the pdf output mode.
	 *
	 */
	public void setPDF() {
		this.fig = false;
	}

	/**
	 * Sets the title of the plot. For no title, call
	 * <code>setTitle(null)</code>. By default, no title is used.
	 *
	 * @param title title of the plot, or <code>null</code>
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the labels for the x-axis and the y-axis of the plot. For
	 * no labels, call <code>setLabels(null,null)</code>. By default, no
	 * labels used.
	 *
	 * @param xlabel label for the x-axis, or <code>null</code>
	 * @param ylabel label for the y-axis, or <code>null</code>
	 */
	public void setLabels(String xlabel, String ylabel) {
		this.xlabel = xlabel;
		this.ylabel = ylabel;
	}

	/**
	 * Sets the minimum and the maximum values for the x-axis. These
	 * values are only used if the are different. By default, both
	 * values are <code>0</code>.  labels used.
	 *
	 * @param minx minimum value for the x-axis
	 * @param maxx maximum value for the x-axis
	 */
	public void setMinMaxX(double minx, double maxx) {
		this.minx = minx;
		this.maxx = maxx;
	}

	/**
	 * Sets the minimum and the maximum values for the y-axis. These
	 * values are only used if the are different. By default, both
	 * values are <code>0</code>.  labels used.
	 *
	 * @param miny minimum value for the y-axis
	 * @param maxy maximum value for the y-axis
	 */
	public void setMinMaxY(double miny, double maxy) {
		this.miny = miny;
		this.maxy = maxy;
	}

	/**
	 * Adds a source to the plot. A source describes one plot curve.<p>
	 *
	 * If the minimum and the maximum values for the x-axis of the
	 * given source differ, they are taken into account for the
	 * minimum and the maximum values for the x-axis of this plot.
	 *
	 * @param source plot curve source to be added
	 */
	public void addSource(Source source) {
		if (sources == null)
			sources = new ArrayList();
		sources.add(source);
		double smin = source.getMin();
		double smax = source.getMax();
		if (smin != smax) {
			minx = Math.min(minx, smin);
			maxx = Math.max(maxx, smax);
		}
	}

	/**
	 * Writes the gnuplot commands into a file.
	 *
	 * @param file name of the file in which the plot commands are written into
	 */
	public void write(String file) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(file));
			writer.println(getGnuplotString());
			writer.close();
		} catch (Exception ex) {
			de.unidu.is.util.Log.error(ex);
		}
	}

	/**
	 * Returns the gnuplot commands.
	 *
	 * @return gnuplot string, based on the settings of this instance
	 */
	public String getGnuplotString() {
		StringBuffer buf = new StringBuffer();
		try {
			buf.append(
				"set terminal "
					+ (fig ? "fig" : "postscript eps")
					+ (bw ? "" : " color")
					+ "\n");
			buf.append(
				"set output '" + outputfile + (fig ? ".fig" : ".eps") + "\n");
			if (title != null)
				buf.append("set title '" + title + "'\n");
			if (xlabel != null)
				buf.append("set xlabel '" + xlabel + "\n;");
			if (ylabel != null)
				buf.append("set ylabel '" + ylabel + "'\n");
			String s = "plot ";
			if (minx != maxx)
				s += "[" + minx + ":" + maxx + "]";
			if (miny != maxy) {
				if(minx==maxx)
					s += "[]";
				s += "[" + miny + ":" + maxy + "]";
			}
			s += " ";
			Iterator it = sources.iterator();
			while (it.hasNext()) {
				Source source = (Source) it.next();
				String d = source.getCommand();
				String t = source.getTitle();
				s += d;
				if (t != null)
					s += " title '" + t + "'";
				s += ",";
			}
			s = s.substring(0, s.length() - 1);
			buf.append(s + "\n");
			buf.append("show output\n");
		} catch (Exception ex) {
			de.unidu.is.util.Log.error(ex);
		}
		return buf.toString();
	}

	/**
	 * Runs gnuplot and creates an .pdf file. The call waits until the
	 * plot is created. Errors from gnuplot are not handled in any
	 * way.
	 *
	 */
	public void run() {
		try {
			GnuplotCall.call(getGnuplotString());
			if (!fig) {
				Process p =
					Runtime.getRuntime().exec(
						Config.getString("eps2pdf.bin")
							+ " "
							+ outputfile
							+ ".eps");
				p.waitFor();
				new File(outputfile + ".eps").delete();
			}
		} catch (Exception ex) {
			de.unidu.is.util.Log.error(ex);
		}
	}

	/**
	 * Returns LaTeX commands for including the result plot.
	 *
	 * @return LaTeX commands for including the result plot
	 */
	public String getLaTeX() {
		return addLaTeX(new StringBuffer()).toString();
	}

	/**
	 * Adds LaTeX commands for including the result plot.
	 *
	 * @param latex string buffer for LaTeX commands
	 */
	public StringBuffer addLaTeX(StringBuffer latex) {
		latex.append("\\begin{center}\n");
		latex.append("\\includegraphics[scale=0.9]{" + outputfile + "}\n");
		latex.append("\\end{center}\n\n");
		return latex;
	}

}
