package com.traceray;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ThreadFactory;

import org.w3c.dom.Document;

import javax.servlet.ServletContext;
import javax.servlet.http.*;

import com.google.appengine.api.ThreadManager;

import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;
import com.google.appengine.api.taskqueue.TaskHandle;
abstract class TracerayServlet extends HttpServlet{
	protected static byte[] toBytes(String s){
		byte[] bytes=new byte[s.length()];
		for(int i=0;i<s.length();i++){
			bytes[i]=(byte)s.charAt(i);
		}
		return bytes;
	}
	protected static String queueTask(String xmlString) throws IOException{
		 Queue queue = QueueFactory.getDefaultQueue();
		 String key=Integer.toString((int)(10000*Math.random()));
	        queue.add(TaskOptions.Builder.withUrl("/worker").param("key", key).param("xmlstring", xmlString));
	        //resp.sendRedirect("/worker?key="+key);
	       
	        return key;
	}
}
@SuppressWarnings("serial")
public class Traceray2Servlet extends TracerayServlet {
//	public void doGet(HttpServletRequest req, HttpServletResponse resp)
//			throws IOException {
//		ServletContext context = getServletContext();
//		String fullPath = context.getRealPath("WEB-INF/schema/xml_schema.xsd");
//		System.out.println(fullPath);
//		Schema sch=null;
//		try {
//			sch=SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema").newSchema(new File(fullPath));
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(sch==null);
//		resp.setContentType("text/plain");
//		resp.getWriter().println("Hello hello, world");
//	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//ServletContext context = getServletContext();
		String fullPath = getServletContext().getRealPath("/WEB-INF/schema/xml_schema.xsd");
		BufferedReader br=req.getReader();
		String line=br.readLine();
		//XmlHandler h=new XmlHandler(fullPath, line);
		//h.queueTask(resp);
		String key=queueTask(line);
		resp.getOutputStream().write(toBytes("key"+key));
	}
}


