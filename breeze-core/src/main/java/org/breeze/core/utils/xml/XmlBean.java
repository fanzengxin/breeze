/**
 *
 */
package org.breeze.core.utils.xml;

import java.io.Serializable;
import java.util.*;

/**
 * xml解释的对象<br>
 *
 * @author 黑面阿呆
 *
 */
public class XmlBean implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4153176873570427491L;
	/**
	 * 节点的名称
	 */
	private String nodeName;
	/**
	 * 节点的类型
	 */
	private Short nodeType;
	/**
	 * 所有属性
	 */
	private Map<String, String> attribute;
	/**
	 * 节点的值
	 */
	private String nodeValue;
	/**
	 * 节点的子节点
	 */
	private List<XmlBean> nodeList;

	/**
	 * 得到节点的名称
	 *
	 * @return 节点的名称
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * 设置节点的名称
	 *
	 * @param nodeName
	 *            设置的节点的名称
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * 得到节点的类型
	 *
	 * @return 节点类型对应的值
	 */
	public Short getNodeType() {
		return nodeType;
	}

	/**
	 * 设置节点类型
	 *
	 * @param nodeType
	 *            节点类型
	 */
	public void setNodeType(Short nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * 设置节点的值
	 *
	 * @return 节点值
	 */
	public String getNodeValue() {
//		if (nodeValue == null) {
//			nodeValue = "";
//		}
		return nodeValue;
	}

	/**
	 * 设置节点的值
	 *
	 * @param nodeValue
	 *            节点值
	 */
	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}

	/**
	 * 得到子节点
	 *
	 * @return 子节点的列表
	 */
	public List<XmlBean> getXmlBeans() {
		return nodeList;
	}

	/**
	 * 子节点的长度
	 *
	 * @return
	 */
	public int nodeSize() {
		if (nodeList == null) {
			return 0;
		}
		return nodeList.size();
	}

	/**
	 * 得到指定索引的XmlBean
	 *
	 * @param index
	 *            索引
	 * @return
	 */
	public XmlBean getXmlBean(int index) {
		return nodeList.get(index);
	}

	/**
	 * 得到指定节点名称的XmlBean列表
	 *
	 * @param nodeName
	 *            节点的名称
	 * @return
	 */
	public List<XmlBean> getXmlBeans(String nodeName) {
		ListIterator<XmlBean> iterator = nodeList.listIterator();
		List<XmlBean> xmlBeans = new ArrayList<XmlBean>();
		while (iterator.hasNext()) {
			XmlBean x = iterator.next();
			if (nodeName.equals(x.getNodeName())) {
				xmlBeans.add(x);
			}
		}
		return xmlBeans;
	}
	/**
	 * 得到指定名称的XmlBean
	 * @param nodeName
	 * @return
	 */
	public XmlBean getXmlBean(String nodeName) {
		List<XmlBean> xbs = getXmlBeans(nodeName);
		if(xbs!=null && xbs.size()>0){
			return xbs.get(0);
		}
		return null;
	}

	/**
	 * 增加一个XmlBean
	 *
	 * @param xmlBean
	 *            XmlBean
	 */
	public void addXmlBean(XmlBean xmlBean) {
		if (nodeList == null) {
			nodeList = new ArrayList<XmlBean>();
		}
		nodeList.add(xmlBean);
	}

	/**
	 * 移除指定的XmlBean
	 *
	 * @param xmlBean
	 *            XmlBean
	 */
	public void removeXmlBean(XmlBean xmlBean) {
		if (nodeList == null) {
			nodeList = new ArrayList<XmlBean>();
		}
		nodeList.remove(xmlBean);
	}

	/**
	 * 清除子节点
	 */
	public void clearBean() {
		if (nodeList == null) {
			nodeList = new ArrayList<XmlBean>();
		} else {
			nodeList.clear();
		}
	}

	/**
	 * 增加属性
	 *
	 * @param key
	 *            属性
	 * @param value
	 *            属性值
	 */
	public void addAttribute(String key, String value) {
		initAttribute();
		attribute.put(key, value);
	}

	/**
	 * 得到属性的值
	 *
	 * @param key
	 *            属性的名字
	 * @return 属性的值
	 */
	public String getAttribute(String key) {
		initAttribute();
		return attribute.get(key);
	}

	/**
	 * 得到属性集合
	 *
	 * @return 节点的属性集合
	 */
	public String[] getAttributeNames() {
		initAttribute();
		Object[] names = attribute.keySet().toArray();
		int l = names.length;
		String[] strs = new String[l];
		for (int i = 0; i < l; i++) {
			strs[i] = (String) names[i];
		}
		return strs;
	}
	/**
	 * 得到所有的属性
	 * @return 所有的属性
	 */
	public Map<String,String> getAttributes(){
		return attribute;
	}
	/**
	 * 删除指定的属性
	 *
	 * @param key
	 *            属性
	 */
	public void removeAttribute(String key) {
		initAttribute();
		attribute.remove(key);
	}

	private void initAttribute() {
		if (attribute == null) {
			attribute = new HashMap<String, String>();
		}
	}
//	/**
//	 * toString()方法仅供测试使用，<br>请勿用于正式程序。
//	 */
//	public String toString(){
//		return new JSONObject(this).toString();
//		//return "[nodeName:" + nodeName + "]Attribute:" + attribute;
//	}
	/**
	 * 清除属性
	 */
	public void clearAttribute() {
		initAttribute();
		attribute.clear();
	}
}
