package com.traceray;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class Worker extends TracerayServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("key");
        OutputStream out=response.getOutputStream();
        out.write(toBytes(key));
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("key");
        if(key==null){key="noppes";}
        OutputStream out=response.getOutputStream();
        String fullPath = getServletContext().getRealPath("/WEB-INF/schema/xml_schema.xsd");
        XmlHandler h=new XmlHandler(fullPath, key, out);
        h.write();
        //out.write(toBytes(key));
    }
}
