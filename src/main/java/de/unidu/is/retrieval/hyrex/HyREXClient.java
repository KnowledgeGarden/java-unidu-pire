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

 
// $Id: HyREXClient.java,v 1.12 2005/02/25 14:33:29 nottelma Exp $

package de.unidu.is.retrieval.hyrex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import de.unidu.is.util.ISOReader;
import de.unidu.is.util.ISOWriter;
import de.unidu.is.util.Log;

/**
 * Low-level interface to HyREX.
 * 
 * @author Kai Grossjohann, Gudrun Fischer, Henrik Nottelmann
 * @version $Revision: 1.12 $, $Date: 2005/02/25 14:33:29 $ 
 */
public class HyREXClient {

	protected Socket hyrex;
	protected PrintWriter out;
	protected BufferedReader in;
	protected String hostname;
	protected int port;
	protected String db;
	protected String cls;

	/**
	 * Constructor.
	 * 
	 * @param hostname
	 * @param port
	 * @param db
	 * @param cls
	 */
	public HyREXClient(String hostname, int port, String db, String cls) {
		this.hostname = hostname;
		this.port = port;
		this.db = db;
		this.cls = cls;
		try {
			connect();
		} catch (IOException e) {
			Log.error(e.getMessage());
		}
	}

	/**
	 * Connects to HyREX
	 */
	private void connect() throws IOException {
		hyrex = new Socket(hostname, port);
		out = new PrintWriter(new ISOWriter(hyrex.getOutputStream()));
		in = new BufferedReader(new ISOReader(hyrex.getInputStream()));
		for (String line; (line = in.readLine()) != null && !line.equals("."););
		commandCheck("open " + db + " " + cls);
	}

	/**
	 * Sends a command to HyREX and returns the result as a single string.
	 *  
	 */
	public String shortCommand(String cmd) throws IOException {
		StringBuffer res = new StringBuffer();
		out.println(cmd.trim());
		out.flush();
		String line = in.readLine();
		if (line == null) {
			close();
			connect();
			out.println(cmd.trim());
			out.flush();
			line = in.readLine();
		}
		while (line != null) {
			if (line.equals("."))
				break;
			if (line.startsWith(".")) {
				res.append(line.substring(1) + "\n");
			} else {
				res.append(line + "\n");
			}
			line = in.readLine();
		}
		return res.toString();
	}

	/**
	 * Likewise, but can be used on an already open socket.
	 *  @deprecated
	 */
	public static String shortCommand(PrintWriter out, BufferedReader in,
			String cmd) throws IOException {
		out.println(cmd.trim());
		out.flush();
		StringBuffer res = new StringBuffer();
		String line = in.readLine();
		while (line != null) {
			if (line.equals("."))
				break;
			if (line.startsWith(".")) {
				res.append(line.substring(1) + "\n");
			} else {
				res.append(line + "\n");
			}
			line = in.readLine();
		}
		return res.toString();
	}

	/**
	 * Sends a command to HyREX and returns the result as a Vector of lines.
	 *  
	 */
	public Vector command(String cmd) throws IOException {
		Vector res = new Vector();
		out.println(cmd.trim());
		out.flush();
		String line = in.readLine();
		if (line == null) {
			close();
			connect();
			out.println(cmd.trim());
			out.flush();
			line = in.readLine();
		}
		while (line != null) {
			if (line.equals("."))
				break;
			if (line.startsWith(".")) {
				res.addElement(line.substring(1));
			} else {
				res.addElement(line);
			}
			line = in.readLine();
		}
		return res;
	}

	/**
	 * Sends a command to HyREX and ignores the result.
	 *  
	 */
	public void commandIgnoreResult(String cmd) throws IOException {
		Vector res = new Vector();
		out.println(cmd.trim());
		out.flush();
		String line = in.readLine();
		if (line == null) {
			close();
			connect();
			out.println(cmd.trim());
			out.flush();
			line = in.readLine();
		}
		while (line != null) {
			if (line.equals("."))
				break;
			line = in.readLine();
		}
	}

	/**
	 * Likewise, but can be used on an already open socket.
	 * 
	 * @param out
	 * @param in
	 * @param cmd
	 * @deprecated
	 */
	public static Vector command(PrintWriter out, BufferedReader in, String cmd)
			throws IOException {
		Vector res = new Vector();
		out.println(cmd.trim());
		out.flush();
		String line = null;
		for (int i = 0; line == null && i < 3; i++) {
			line = in.readLine();
			if (line == null) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
			}
		}
		while (line != null) {
			if (line.equals("."))
				break;
			if (line.startsWith(".")) {
				res.addElement(line.substring(1));
			} else {
				res.addElement(line);
			}
			line = in.readLine();
		}
		return res;
	}

	/**
	 * Sends a command to HyREX and checks the result code. If the status code
	 * indicates "no error", it returns the result as a Vector of lines. The
	 * first line will contain the status code, then a space, then a descriptive
	 * status message. The remaining lines are the extra data returned.
	 * 
	 * If the status code indicates an error, an IOException will be thrown and
	 * the message will contain the status code, a space, and a descriptive
	 * error message.
	 *  
	 */
	public Vector commandCheck(String cmd) throws IOException {
		Vector res = command(cmd);
		if (res.size() == 0) {
			throw new IOException("No result for command " + cmd);
		}
		if (!((String) (res.get(0))).startsWith("2")) {
			throw (new IOException((String) (res.get(0))));
		} else {
			return res;
		}
	}

	/**
	 * Likewise, but can be used on an already open socket.
	 *  @deprecated
	 */
	public static Vector commandCheck(PrintWriter out, BufferedReader in,
			String cmd) throws IOException {
		Vector res = command(out, in, cmd);
		if (res.size() == 0) {
			throw new IOException("No result for command " + cmd);
		}
		if (!((String) (res.get(0))).startsWith("2")) {
			throw (new IOException((String) (res.get(0))));
		} else {
			return res;
		}
	}

	/**
	 * Like commandCheck, but returns entire result as one string.
	 *  
	 */
	public String shortCommandCheck(String cmd) throws IOException {
		Vector lines = commandCheck(cmd);
		Iterator it = lines.iterator();
		StringBuffer res = new StringBuffer();
		it.next();
		while (it.hasNext()) {
			res.append((String) it.next());
			res.append("\n");
		}
		return res.toString();
	}

	/**
	 * Likewise, but can be used on already open socket.
	 *  @deprecated
	 */
	public static String shortCommandCheck(PrintWriter out, BufferedReader in,
			String cmd) throws IOException {
		Vector lines = commandCheck(out, in, cmd);
		Iterator it = lines.iterator();
		StringBuffer res = new StringBuffer();
		it.next();
		while (it.hasNext()) {
			res.append((String) it.next());
			res.append("\n");
		}
		return res.toString();
	}

	/**
	 * Returns a list of bases for a given HyREX server. Expects the hostname
	 * and port as arguments.
	 *  @deprecated
	 */
	public static Vector bases(String hostname, int port)
			throws UnknownHostException, IOException {
		Socket s = new Socket(hostname, port);
		PrintWriter out = new PrintWriter(s.getOutputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(s
				.getInputStream()));
		Vector res = command(out, in, "bases");
		return split((String) (res.get(1)));
	}

	/**
	 * Returns a list of classes in a given base. Expects the hostname, port
	 * number, and name of base as arguments.
	 *  @deprecated
	 */
	public static Vector classes(String hostname, int port, String base)
			throws UnknownHostException, IOException {
		Socket s = new Socket(hostname, port);
		PrintWriter out = new PrintWriter(s.getOutputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(s
				.getInputStream()));
		Vector res = commandCheck(out, in, "classes " + base);
		return split((String) (res.get(1)));
	}

	/**
	 * Returns a Vector of available data types. The "open" command must
	 * previously have been sent to the HyREX server. The socket is specified in
	 * the "out" and "in" arguments.
	 *  @deprecated
	 */
	public static Vector datatypes(PrintWriter out, BufferedReader in)
			throws IOException {
		Vector res = commandCheck(out, in, "datatypes");
		return split((String) (res.get(1)));
	}

	/**
	 * Returns a Vector of available data types. The constructor ensures that
	 * the "open" command has already been sent, so the caveat from the previous
	 * method does not apply.
	 *  
	 */
	public Vector datatypes() throws IOException {
		Vector res = commandCheck("datatypes");
		return split((String) (res.get(1)));
	}

	/**
	 * Returns a Vector of available search predicates. The "open" command must
	 * previously have been sent to the HyREX server.
	 *  @deprecated
	 */
	public static Vector predicates(PrintWriter out, BufferedReader in,
			String datatype) throws IOException {
		Vector res = commandCheck(out, in, "predicates " + datatype);
		return split((String) (res.get(1)));
	}

	/**
	 * Likewise, but the constructor ensures that "open" has been sent.
	 *  
	 */
	public Vector predicates(String datatype) throws IOException {
		Vector res = commandCheck("predicates " + datatype);
		return split((String) (res.get(1)));
	}

	/**
	 * Sends a query to the remote host and returns a Vector of results. The
	 * first element of the Vector is a String and contains the status code and
	 * status message from HyREX. Subsequent elements of the Vector are Path
	 * objects, representing one result each.
	 * 
	 * This method assumes that the "open" command has been sent to the HyREX
	 * server previously.
	 *  @deprecated
	 */
	public static Vector find(PrintWriter out, BufferedReader in, String query)
			throws IOException {
		Vector lines = commandCheck(out, in, "find " + query);
		Vector res = new Vector(lines.size());
		res.addElement(lines.remove(0));
		Iterator i = lines.iterator();
		while (i.hasNext()) {
			res.addElement(new Path((String) i.next()));
		}
		return res;
	}

	/**
	 * Likewise, but the constructor assures that "open" has been issued.
	 *  
	 */
	public Vector find(String query) throws IOException {
		Vector lines = commandCheck("find " + query);
		Vector res = new Vector(lines.size());
		res.addElement(lines.remove(0));
		Iterator i = lines.iterator();
		while (i.hasNext()) {
			res.addElement(new Path((String) i.next()));
		}
		return res;
	}

	/**
	 * Retrieves a document from HyREX, given a document id. Returns a string,
	 * which contains the document.
	 * @deprecated  
	 */
	public static String document_id(PrintWriter out, BufferedReader in,
			int docid) throws IOException {
		return shortCommandCheck(out, in, "document_id " + docid);
	}

	/**
	 * Retrieves a document from HyREX, given a document id. Returns a string,
	 * which contains the document.
	 *  @deprecated
	 */
	public static String document_id(PrintWriter out, BufferedReader in,
			String docid) throws IOException {
		return shortCommandCheck(out, in, "document_id " + docid);
	}

	/**
	 * Likewise, but constructor assures that "open" has been issued.
	 *  
	 */
	public String document_id(int docid) throws IOException {
		return shortCommandCheck("document_id " + docid);
	}

	/**
	 * Likewise, but constructor assures that "open" has been issued.
	 *  
	 */
	public String document_id(String docid) throws IOException {
		return shortCommandCheck("document_id " + docid);
	}

	/**
	 * Splits a string into lines at newline boundaries.
	 *  
	 */
	protected static Vector splitnl(String s) {
		StringTokenizer tok = new StringTokenizer(s, "\r\n");
		Vector v = new Vector();
		while (tok.hasMoreTokens()) {
			v.addElement(tok.nextToken());
		}
		return v;
	}

	/**
	 * Splits a string at whitespace.
	 *  
	 */
	protected static Vector split(String s) {
		StringTokenizer tok = new StringTokenizer(s);
		Vector v = new Vector();
		while (tok.hasMoreTokens()) {
			v.addElement(tok.nextToken());
		}
		return v;
	}

	/**
	 * Closes the connection to HyREX.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (hyrex != null) {
			in.close();
			out.close();
			hyrex.close();
			hyrex = null;
			in = null;
			out = null;
		}
	}

}
