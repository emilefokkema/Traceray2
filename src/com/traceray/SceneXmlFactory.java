package com.traceray;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;
public class SceneXmlFactory {
	public static SceneXmlFactory getInstance(String xmlSchemaPath){
		Schema sch=null;
		try{
			sch=SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema").newSchema(new File(xmlSchemaPath));
		}catch(SAXException e){
			e.printStackTrace();
		}
		SceneXmlFactory f=new SceneXmlFactory();
		if(sch!=null){
			f.setSchema(sch);
			return f;
			}
		return null;
	}
	private Schema sch;
	private DocumentBuilderFactory dbf;
	private void setSchema(Schema sch){
		this.sch=sch;
		this.dbf=DocumentBuilderFactory.newInstance();
		dbf.setSchema(sch);
		dbf.setNamespaceAware(true);
	}
	public Document getSceneXml(String xmlString){
		return getSceneXml(new InputSource(new StringReader(xmlString)));
	}
	public Document getSceneXml(InputSource xmlInputSource){
		if(this.sch!=null){
			try{
				DocumentBuilder db=dbf.newDocumentBuilder();
				Document xml=db.parse(xmlInputSource);
				return xml;
			}catch(ParserConfigurationException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public Document getSceneXml(InputStream input){
		if(this.sch!=null){
			try{
				DocumentBuilder db=dbf.newDocumentBuilder();
				Document xml=db.parse(input);
				return xml;
			}catch(ParserConfigurationException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
//	public Document getSceneXml(String xmlPath){
//		if(this.sch!=null){
//			try{
//				DocumentBuilder db=dbf.newDocumentBuilder();
//				Document xml=db.parse(new File(xmlPath));
//				return xml;
//			}catch(ParserConfigurationException e){
//				e.printStackTrace();
//			}catch(IOException e){
//				e.printStackTrace();
//			} catch (SAXException e) {
//				e.printStackTrace();
//			}
//			
//			
//		}
//		return null;
//	}
	
}
