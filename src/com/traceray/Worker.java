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
	private static int 	PARTLENGTH=100_000;
	private static int MAXNPARTS=9;
	private static ArrayList<String> split(String s, int partLength){
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
	private static ArrayList<String> first(ArrayList<String> s, int n){
		ArrayList<String> first=new ArrayList<String>();
		for(int i=0;i<n;i++){
			first.add(s.get(i));
		}
		return first;
	}
	private static ArrayList<String> allButFirst(ArrayList<String> s, int n){
		ArrayList<String> allButFirst=new ArrayList<String>();
		for(int i=n;i<s.size();i++){
			allButFirst.add(s.get(i));
		}
		return allButFirst;
	}
	private static ArrayList<ArrayList<String>> split(ArrayList<String> s, int maxNParts){
		ArrayList<ArrayList<String>> parts=new ArrayList<ArrayList<String>>();
		while(s.size()>0){
			if(s.size()>maxNParts){
				parts.add(first(s, maxNParts));
				s=allButFirst(s, maxNParts);
			}else{
				parts.add(s);
				s=new ArrayList<String>();
			}
		}
		return parts;
	}
	private static void setColorAttributes(Entity employee, ArrayList<String> parts){
		int nParts=parts.size();
		employee.setProperty("n",nParts);
		for(int i=0;i<nParts;i++){
			employee.setProperty("colors"+i, new Text(parts.get(i)));
		}
	}
	private static void putDone(String myKey, DatastoreService datastore, int howManyEmployees){
		Entity employee=new Entity("Employee");
		employee.setProperty("mykey", myKey+"_done");
		employee.setProperty("n",howManyEmployees);
		datastore.put(employee);
	}
	private static int putEmployees(String result, String myKey, DatastoreService datastore){
		ArrayList<String> allParts=split(result, PARTLENGTH);
		ArrayList<ArrayList<String>> forEmployee=split(allParts, MAXNPARTS);
		Entity employee;
		for(int i=0;i<forEmployee.size();i++){
			employee=new Entity("Employee");
			setColorAttributes(employee, forEmployee.get(i));
			employee.setProperty("mykey",myKey+"_"+i);
			datastore.put(employee);
		}
		return forEmployee.size();
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
         int nEmployees=putEmployees(result, key, datastore);
         putDone(key, datastore, nEmployees);
//         Entity employee = new Entity("Employee");
//         try{
//       	 employee.setProperty("colors", new Text(result));
//         }catch(IllegalArgumentException e){
//        	 e.printStackTrace();
//       	 employee.setProperty("colors", new Text("ecf000000"));
//         }
//         setColorAttributes(employee, split(result, 100_000));
//         employee.setProperty("mykey",key);
//         datastore.put(employee);
         
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("key");
        if(key==null){key="noppes";}
        OutputStream out=response.getOutputStream();
        out.write(toBytes("key"+key));
    }
}
