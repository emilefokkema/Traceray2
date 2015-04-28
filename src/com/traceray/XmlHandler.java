package com.traceray;

import java.io.IOException;
import java.io.OutputStream;

public class XmlHandler{
	private SceneImageWriter w;
	private String xmlString;
	private Scene s;
	private Viewport v;
	public XmlHandler(String schemaPath, String xmlString){
		System.out.println("[new XmlHandler]");
		SceneXmlFactory f=SceneXmlFactory.getInstance(schemaPath);
		SceneXml sceneXml=new SceneXml(f.getSceneXml(xmlString));
		this.s=sceneXml.getScene();
		this.v=sceneXml.getViewPort();
		this.xmlString=xmlString;
		this.w=new SceneImageWriter(sceneXml.getScene(), sceneXml.getViewPort(), 3);
	}
	public String write() throws IOException{
		return this.w.write();
	}
}
