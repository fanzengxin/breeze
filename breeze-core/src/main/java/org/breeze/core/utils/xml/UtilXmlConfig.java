package org.breeze.core.utils.xml;

import org.breeze.core.bean.api.ApiConfig;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;
import org.breeze.core.utils.url.UtilURL;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Date;

/**
 * Xml配置文件读取类
 *
 * @author 黑面阿呆
 */
public class UtilXmlConfig {
    /**
     * 缓存key的前缀（防止缓存key冲突）
     */
    private static final String CONFIG_PREFIX = "system.config.xml.";
    private static final String XML_NAME = "XML.BASE.CACHE";
    /**
     * 日志对象（记录日志用）
     */
    private static Log log = LogFactory.getLog(UtilXmlConfig.class);

    /**
     * 得到配置文件对象XmlBean
     *
     * @param configName 配置文件的名称
     * @return XmlBean
     */
    public static XmlBean getXmlConfig(String configName) {
        return getXmlConfig(configName, false);
    }

    /**
     * 得到配置文件对象XmlBean
     *
     * @param configName 配置文件的名称
     * @return XmlBean
     */
    public static XmlBean getXmlConfig(String configName, boolean absolutePath) {
        return getXmlConfigNoCache(configName, absolutePath);
    }

    /**
     * 得到配置文件对象XmlBean（不使用缓存）
     *
     * @param configName 配置文件的名称
     * @return XmlBean
     */
    public static XmlBean getXmlConfigNoCache(String configName) {
        return getXmlConfigNoCache(configName, false);
    }

    public static XmlBean getXmlConfigNoCache(String configName, boolean absolutePath) {
        //声明返回值
        XmlBean xb;
        //configName 配置文件config目录的相对路径，例如：workflow/workflow-configuration.cfg.xml
        //UtilURL.getDefaultXmlConfigUriString(configName) 将相对路径转换为绝对路径
        //将xml文件解析为w3c.dom的Element对象
        Element root = null;
        if (absolutePath) {
            root = UtilXmlParse.parse(configName);
        } else {
            root = UtilXmlParse.parse(UtilURL.getDefaultXmlConfigUriString(configName));
        }
        //初始化XmlBean
        xb = new XmlBean();
        //得到xml根节点下的所有属性
        NamedNodeMap atta = root.getAttributes();
        //将属性设置到XmlBean中
        setAttribute(xb, atta);
        //得到根节点的名字，并设置到XmlBean中
        xb.setNodeName(root.getNodeName());
        //得到所有的子节点
        NodeList nodeList = root.getChildNodes();
        //得到根节点的类型 ELEMENT_NODE;ATTRIBUTE_NODE;TEXT_NODE等等，具体有哪些类型见：org.w3c.dom.Node
        short nodeType = root.getNodeType();
        //将节点类型设置到XmlBean中
        xb.setNodeType(nodeType);
        //如果节点为文本类型的，那么就把文本值设置到XmlBean中
        if (Node.TEXT_NODE == nodeType) {
            xb.setNodeValue(root.getNodeValue());
        }
        //将所有子节点解析并设置到XmlBean中
        setXmlBeans(xb, nodeList);
        //返回解析的XmlBean
        return xb;

    }

    /**
     * 将属性设置到XmlBean中
     *
     * @param xb   存储属性的XmlBean对象
     * @param atta 节点中所有属性的集合
     */
    private static void setAttribute(XmlBean xb, NamedNodeMap atta) {
        //如果没有属性，则不进行操作
        if (atta == null) {
            return;
        }
        //得到属性的个数
        int l = atta.getLength();

        for (int i = 0; i < l; i++) {
            //循环得到每一个属性对象
            Node node = atta.item(i);
            //将属性的名字和值对应设置到XmlBean中
            xb.addAttribute(node.getNodeName(), node.getNodeValue());
        }
    }

    /**
     * 将所有子节点解析并设置到XmlBean中
     *
     * @param xb
     * @param nodeList
     */
    private static void setXmlBeans(XmlBean xb, NodeList nodeList) {
        //如果没有子节点，就不进行操作
        if (nodeList == null) {
            return;
        }
        //得到节点的数量
        int l = nodeList.getLength();
        //循环将每个节点信息设置到XmlBean中
        for (int i = 0; i < l; i++) {
            //得到一个节点
            Node node = nodeList.item(i);
            //得到节点类型
            short nodeType = node.getNodeType();
            //如果节点类型为Element，则解析，其他类型不解析（TEXT_NODE已经在上级中解析了）
            if (Node.ELEMENT_NODE == nodeType) {
                //设置子节点的XmlBean
                XmlBean xb1 = new XmlBean();
                //得到属性列表
                NamedNodeMap atta = node.getAttributes();
                //将属性设置到子节点的XmlBean中
                setAttribute(xb1, atta);
                //获得2次子节点是为了防止在处理数据时影响到另外一个处理的对象（这步操作可能时白担心）
                NodeList nl = node.getChildNodes();
                NodeList vns = node.getChildNodes();
                //获得子节点的数量
                int vl = vns.getLength();
                //将节点的名字设置到XmlBean中
                xb1.setNodeName(node.getNodeName());
                //循环子节点，如果发现子节点中有文本节点，就将文本节点的值设置到XmlBean中
                for (int k = 0; k < vl; k++) {
                    //拿到子节点
                    Node vn = vns.item(k);
                    //得到节点类型
                    short vType = vn.getNodeType();
                    //如果是文本类型，则将数据设置到XmlBean
                    if (Node.TEXT_NODE == vType) {
                        String vv = vn.getNodeValue();
                        if (vv != null && !"".equals(vv.trim())) {
                            xb1.setNodeValue(vv.trim());
                            break;
                        }
                    }
                    //CDATA 是一种可以带xml格式的文本类型，所以如果是CDATA也设置XmlBean
                    if (Node.CDATA_SECTION_NODE == vType) {
                        String vv = vn.getNodeValue();
                        if (vv != null && !"".equals(vv.trim())) {
                            xb1.setNodeValue(vv.trim());
                            break;
                        }
                    }

                }
                //将节点类型设置到XmlBean
                xb1.setNodeType(nodeType);
                //递归的方式，将子节点设置到XmlBean
                setXmlBeans(xb1, nl);
                //将子节点的XmlBean加入到上级的XmlBean中
                xb.addXmlBean(xb1);
            }
        }
    }
}
