package org.breeze.core.utils.xml;

import org.breeze.core.exception.BasicException;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;


/**
 * xml解析类
 *
 * @author 黑面阿呆
 */
public class UtilXmlParse {

    private static Log log = LogFactory.getLog(UtilXmlParse.class);

    /**
     * 解析xml文件，得到root
     *
     * @param urlStr 需要解析xml文件的地址字符串
     * @return <Element> Document的Root
     */
    public static Element parse(String urlStr) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // 解析文档
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(urlStr);
            doc.normalize();
            log.logDebug("XML解析的文件路径为：{}", urlStr);
        } catch (ParserConfigurationException e) {
            log.logError("解析构造解析异常", e);
        } catch (SAXException e) {
            log.logError("文件解析异常", e);
        } catch (IOException e) {
            log.logError("I/O操作异常", e);
        }
        if (doc != null) {
            return doc.getDocumentElement();
        } else {
            return null;
        }
    }

    /**
     * 解析Xml文件
     *
     * @param url XML文件的URI,类型为java.net.URL
     * @return <Element> Document的Root
     */
    public static Element parse(URL url) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // 解析文档
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(url.toString());
            doc.normalize();
            log.logDebug("XML解析的文件路径为：{}", url.toString());
        } catch (ParserConfigurationException e) {
            log.logError("解析构造解析异常", e);
        } catch (SAXException e) {
            log.logError("文件解析异常", e);
        } catch (IOException e) {
            log.logError("I/O操作异常", e);
        }
        // 得到根节点
        if (doc != null) {
            return doc.getDocumentElement();
        } else {
            return null;
        }
    }

    /**
     * 解析Xml文件
     *
     * @param xmlString 要解析的字符串
     * @return <Element> Document的Root
     */
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
            log.logDebug("解析XML字符串的内容为：{}", xmlString);
        } catch (ParserConfigurationException e) {
            log.logError("解析构造解析异常", e);
        } catch (SAXException e) {
            log.logError("文件解析异常", e);
        } catch (IOException e) {
            log.logError("I/O操作异常", e);
        } finally {
            if (xReader != null) {
                xReader.close();
            }
        }
        // 得到根节点
        if (doc != null) {
            return doc.getDocumentElement();
        } else {
            return null;
        }
    }

    /**
     * 得到想要的属性
     *
     * @param node  要解析节点
     * @param name  属性的名字
     * @param valid 是否是必须要有的属性
     * @return 该属性的值
     * @throws BasicException 抛出的错误信息
     */
    public static String getAttributesValue(Node node, String name, boolean valid) throws BasicException {
        NamedNodeMap attrs = node.getAttributes();
        return getAttributesValue(attrs, name, valid);
    }

    /**
     * 得到节点的值
     *
     * @param node 节点
     * @return
     */
    public static String getNodeValue(Node node) {
        if (node == null) {
            return null;
        }
        short nodeType = node.getNodeType();
        log.logDebug("解析的节点类型为：{}", nodeType);
        String v = null;
        switch (nodeType) {
            case Node.NOTATION_NODE:
                v = node.getFirstChild().getNodeValue();
                break;
            case Node.ATTRIBUTE_NODE:
                v = node.getNodeValue();
                break;
            case Node.ELEMENT_NODE:
                Node fc = node.getFirstChild();
                if (fc == null) {
                    v = null;
                } else {
                    v = fc.getNodeValue();
                }
                break;
            case Node.TEXT_NODE:
                v = node.getNodeValue();
                break;
            default:
                v = node.getNodeValue();
                break;
        }
        return v;
    }

    /**
     * 得到想要的属性
     *
     * @param node          要解析节点
     * @param name          属性的名字
     * @param valid         是否是必须要有的属性
     * @param ExceptionText 如果出现错误，抛出信息将包含此信息
     * @return 该属性的值
     * @throws BasicException 抛出的错误信息
     */
    public static String getAttributesValue(Node node, String name, boolean valid, String ExceptionText) throws BasicException {
        NamedNodeMap attrs = node.getAttributes();
        return getAttributesValue(attrs, name, valid, ExceptionText);
    }

    /**
     * 得到想要的属性
     *
     * @param attrs 要解析节点的NamedNodeMap
     * @param name  属性的名字
     * @param valid 是否是必须要有的属性
     * @return 该属性的值
     * @throws BasicException 抛出的错误信息
     */
    private static String getAttributesValue(NamedNodeMap attrs, String name, boolean valid) throws BasicException {
        Node attr = attrs.getNamedItem(name);
        if (attr == null) {
            if (valid) {
                throw new BasicException("节点属性不存在，请查看XML的完整性.");
            } else {
                return "";
            }
        }
        return attr.getNodeValue().toString();
    }

    /**
     * 得到想要的属性
     *
     * @param attrs         要解析节点的NamedNodeMap
     * @param name          属性的名字
     * @param valid         是否是必须要有的属性
     * @param ExceptionText 如果出现错误，抛出信息将包含此信息
     * @return 该属性的值
     * @throws BasicException 抛出的错误信息
     */
    private static String getAttributesValue(NamedNodeMap attrs, String name, boolean valid, String ExceptionText)
            throws BasicException {
        Node attr = attrs.getNamedItem(name);
        if (attr == null) {
            if (valid) {
                throw new BasicException("节点属性不存在，请查看XML的完整性.");
            } else {
                return "";
            }
        }
        return attr.getNodeValue().toString();
    }
}
