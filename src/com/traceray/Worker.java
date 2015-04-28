package com.traceray;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
public class Worker extends TracerayServlet {
	private static ArrayList<String> split(String s){
		int partLength=100_000;
		ArrayList<String> parts=new ArrayList<String>();
		while(s.length()>0){
			if(s.length()>partLength){
				parts.add(s.substring(0, partLength));
				s=s.substring(partLength,s.length());
			}else{
				parts.add(s);
				s="";
			}
		}
		return parts;
	}
	private static void setColorAttributes(Entity employee, String result){
		ArrayList<String> parts=split(result);
		int nParts=parts.size();
		employee.setProperty("n",nParts);
		for(int i=0;i<nParts;i++){
			employee.setProperty("colors"+i, new Text(parts.get(i)));
		}
	}
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	 String key = request.getParameter("key");
    	 String xmlString=request.getParameter("xmlstring");
         if(key==null){key="noppes";}
         OutputStream out=response.getOutputStream();
         String fullPath = getServletContext().getRealPath("/WEB-INF/schema/xml_schema.xsd");
         XmlHandler h=new XmlHandler(fullPath, xmlString);
         String result=h.write();
         
         DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
         Entity employee = new Entity("Employee");
//         try{
//        	 employee.setProperty("colors", new Text(result));
//         }catch(IllegalArgumentException e){
//        	 e.printStackTrace();
//        	 employee.setProperty("colors", new Text("ecf000000"));
//         }
         setColorAttributes(employee, result);
         employee.setProperty("mykey",key);
         datastore.put(employee);
         
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("key");
        if(key==null){key="noppes";}
        OutputStream out=response.getOutputStream();
        out.write(toBytes("key"+key));
    }
}
