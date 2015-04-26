package com.traceray;

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

@SuppressWarnings("serial")
public class Traceray2Servlet extends HttpServlet {
	private static byte[] toBytes(String s){
		byte[] bytes=new byte[s.length()];
		for(int i=0;i<s.length();i++){
			bytes[i]=(byte)s.charAt(i);
		}
		return bytes;
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		ServletContext context = getServletContext();
		String fullPath = context.getRealPath("WEB-INF/schema/xml_schema.xsd");
		System.out.println(fullPath);
		Schema sch=null;
		try {
			sch=SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema").newSchema(new File(fullPath));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sch==null);
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello hello, world");
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		ServletContext context = getServletContext();
		String fullPath = context.getRealPath("/WEB-INF/schema/xml_schema.xsd");
		InputStream input=req.getInputStream();
		resp.setContentType("text/plain");
		OutputStream out=resp.getOutputStream();
		XmlHandler h=new XmlHandler(fullPath, input, out);
//		try{
//			h.write();
//		}catch(IOException e){
//			out.write(new byte[]{'o', 'o', 'p', 's'});
//		}
		out.write(toBytes("oops"));
	}
}

class XmlHandler{
	private SceneImageWriter w;
	public XmlHandler(String schemaPath, InputStream xmlInput, OutputStream imageOutput){
		SceneXmlFactory f=SceneXmlFactory.getInstance(schemaPath);
		Document xmlScene=f.getSceneXml(xmlInput);
		SceneXml sceneXml=new SceneXml(xmlScene);
		this.w=new SceneImageWriter(sceneXml.getScene(), sceneXml.getViewPort(), imageOutput, 3);
	}
	public XmlHandler(String schemaPath, String xmlString, OutputStream imageOutput){
		SceneXmlFactory f=SceneXmlFactory.getInstance(schemaPath);
		Document xmlScene=f.getSceneXml(xmlString);
		SceneXml sceneXml=new SceneXml(xmlScene);
		this.w=new SceneImageWriter(sceneXml.getScene(), sceneXml.getViewPort(), imageOutput, 3);
	}
	public void write() throws IOException{
		this.w.write();
	}
}
