package com.traceray;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Text;
public class GetImage extends TracerayServlet{
	 protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        String key = request.getParameter("key");
	        if(key==null){key="noppes";}
	        Filter propertyFilter =new FilterPredicate("mykey",
	        		                      FilterOperator.EQUAL,
	        		                      key);
	        Query q =  new Query("Employee").setFilter(propertyFilter);
	        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	        Iterable<Entity> imageList=datastore.prepare(q).asIterable();
	        boolean found=false;
	        String zeroResultString="ecf000000";
	        String resultString="";
	        String partResultString;
	        Text partResultText;
	        for(Entity result:imageList){
	        	found=true;
	        	int nParts=1;
	        	try{
	        		nParts=(int)(long)result.getProperty("n");
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}
	        	for(int i=0;i<nParts;i++){
	        		try{
	        			partResultText=(Text)result.getProperty("colors"+i);
	        			partResultString=partResultText.getValue();
	        			if(partResultString==null){break;}
	        			resultString+=partResultString;
	        		}catch(ClassCastException e){
	        			e.printStackTrace();
	        			break;
	        		}
	        	}
	        	break;
	        }
	        OutputStream out=response.getOutputStream();
	        if(found){
	        	if(resultString==null||resultString.equals("")){resultString=zeroResultString;}
	        	out.write(toBytes(resultString));
	        }else{
	        	out.write(toBytes("key"+key));
	        }
	    }
}
