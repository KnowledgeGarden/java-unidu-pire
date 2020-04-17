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
package de.unidu.is.retrieval.hyrex;


import java.net.URL;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcClient;

/**
 * @author malik
 *
 * This client connect to HyREX rpc server that is only capable of answering simple 
 * queries with no structural constraints. It works on the data indexed by HyREX indexer.
 * The only advantage is faster retrieval in comparison to HyREX.
 * 
 * 
 */
public class HyREXRpcClient {

	
	protected static final Vector EMPTY_ARRAY = new Vector();
	protected XmlRpcClient xmlRpcClient;
	protected URL url;
	protected String password;
	protected int port;
	protected int timeout;

	public HyREXRpcClient(){
		 try{
			url=new URL("http://inex.is.informatik.uni-duisburg.de:8081/RPC2");
		   xmlRpcClient = new XmlRpcClient(url);
		 }
		 catch(Exception ex)
		 {
		 	ex.printStackTrace();
		 }
		   
	   }
	 public HyREXRpcClient(String purl){
		 try{
			url=new URL(purl);
		   xmlRpcClient = new XmlRpcClient(url);
		 }
		 catch(Exception ex)
		 {
		 	ex.printStackTrace();
		 }
		   
	   }

	   protected Object invoke(final String rpcMethodName) {
		   return invoke(rpcMethodName, EMPTY_ARRAY);
	   }

	   protected Object invoke(final String rpcMethodName, Vector parameters) {
		   try {
			   Object result = xmlRpcClient.execute(rpcMethodName, parameters);
			   if (result instanceof Hashtable) {
				   Hashtable fault = (Hashtable) result;
				   int faultCode = ((Integer) fault.get("faultCode")).intValue();
				   throw new RuntimeException(
					   "Unable to connect to device via XML-RPC. \nFault Code: "
						   + faultCode
						   + "\nFault Message: "
						   + (String) fault.get("faultString"));
			   }
			   else
			   	return result;
	   } catch (Exception e) {
			   e.printStackTrace();
			   return null;
		   }
	   }

 public String shortCommand(String query,int hits,double augmentationFactor)
 {
 	try{
 		Vector parameters=new Vector();
 		parameters.add(query);
 		parameters.add(new Integer(hits));
 		parameters.add(new Double(augmentationFactor));
 		return (String)invoke("sample.process_query",parameters);
 	}
 	catch (Exception e) {
 		e.printStackTrace();
 		return null;
	}
				
		
	}

 public Vector command(String query,int hits,double augmentationFactor)
 {
 	try{
 		Vector parameters=new Vector();
 		Vector result=new Vector();
 		String sResult=new String();
 		parameters.add(query);
 		parameters.add(new Integer(hits));
 		parameters.add(new Double(augmentationFactor));
 		sResult=(String)invoke("sample.process_query",parameters);
 		sResult=sResult.substring(sResult.indexOf('\n')+1);
 		StringTokenizer tokenizer=new StringTokenizer(sResult,"\n");
 		while(tokenizer.hasMoreElements())
 		{
 			result.add(tokenizer.nextElement());
 		}
 		return result;
 	}
 	catch (Exception e) {
 		e.printStackTrace();
 		return null;
	}
				
		
	}
	public static void main(String[] args) {
		try{
			HyREXRpcClient rpcClient=new HyREXRpcClient("http://inex.is.informatik.uni-duisburg.de:8081/RPC2");
			String result=rpcClient.shortCommand("perl features",20,0.01);
			System.out.println(result);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	   

}
