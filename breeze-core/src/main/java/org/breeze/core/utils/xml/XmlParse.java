package org.breeze.core.utils.xml;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class XmlParse {
	public static Element parseTxt(String xmlString) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		StringReader xReader = null;
		InputSource xInputSource = null;
		try {
			// 解析文档
			DocumentBuilder db = dbf.newDocumentBuilder();
			xReader = new StringReader(xmlString);
			xInputSource = new InputSource(xReader);
			doc = db.parse(xInputSource);
			doc.normalize();
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		} finally {
			if (xReader != null) {
				xReader.close();
			}
		}
		// 得到根节点
		if (doc != null)
			return doc.getDocumentElement();
		return null;
	}
	public static Element parse(String urlStr) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// 解析文档
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(urlStr);
			doc.normalize();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 得到根节点
		if (doc != null)
			return doc.getDocumentElement();
		return null;
	}
	public static void main(String[] args) {
		Element root = parseTxt("<tns:root><tns:LocationRule relationship=\"before\"><tns:LinkageTpowActivities><tns:LinkageTpowActivity linkageTpowActivityID=\"High-level Req/design review\" /></tns:LinkageTpowActivities></tns:LocationRule></tns:root>");
		//NodeList childNodes = root.getChildNodes();
		 Node node = root.getFirstChild();
		 Node tNode = node.getFirstChild().getFirstChild();
		 Node idNode = tNode.getAttributes().getNamedItem("linkageTpowActivityID");
		 System.out.println(idNode.getNodeValue());
//如果xml中只有一个这个名称的节点，或者是有多个想一起取出来，可以用这个方法：
		 NodeList list = root.getElementsByTagName("tns:LinkageTpowActivity");
		 NamedNodeMap oNode = list.item(0).getAttributes();
		 String str = oNode.getNamedItem("linkageTpowActivityID").getNodeValue();
		 System.out.println(str);


	}
}
