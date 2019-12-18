package org.breeze.core.bean.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.breeze.core.log.Log;
import org.breeze.core.log.LogFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @Description: 数据集合对象
 * @Auther: 黑面阿呆
 * @Date: 2019/2/18 23:51
 * @Version: 1.0.0
 */
public class DataList extends ArrayList {

    private static Log log = LogFactory.getLog(DataList.class);
    /**
     *
     */
    private static final long serialVersionUID = -8209947358332289897L;
    /**
     * 当前页
     */
    private int nowPage = 0;
    /**
     * 每页多少条数据
     */
    private int pageSize = 0;
    /**
     * 总数据数
     */
    private int dataTotal = 0;

    private boolean isCount = true;

    /**
     * 得到求和的sql
     */
    private String countSql;

    /**
     * 求和的sql所需要的参数
     */
    private String params;

    /**
     * 得到汇总的sql
     *
     * @return
     */
    public String getCountSql() {
        return countSql;
    }

    public boolean isCount() {
        return isCount;
    }

    public void setCount(boolean isCount) {
        this.isCount = isCount;
    }

    public String getParams() {
        return params;
    }

    /**
     * 构造函数
     */
    public DataList() {
        super();
    }

    /**
     * 构造函数
     *
     * @param index 长度
     */
    public DataList(int index) {
        super(index);
    }

    /**
     * 构造函数，将指定地址的文件读取出来，生成DataList
     *
     * @param urlStr xml文件的地址串
     */
    public DataList(String urlStr) {
        super();
    }

    public boolean addAll(DataList dataList) {
        super.addAll(dataList);
        return true;
    }

    /**
     * 增加数据
     *
     * @param data 一条数据
     * @return 增加成功返回true
     */
    public boolean add(Data data) {
        return super.add(data);
    }

    public boolean set(Data data) {
        clear();
        return super.add(data);
    }

    public Object remove(int index) {
        return super.remove(index);
    }

    public boolean remove(Object o) {
        return super.remove(o);
    }

    /**
     * 得到指定的数据
     *
     * @param i 索引号
     * @return 一条数据
     */
    public Data getData(int i) {
        return (Data) get(i);
    }

    /**
     * 得到当前页
     *
     * @return int 如果没有分页 返回0<br>
     * 如果分页但还没有查询数据 返回-1
     */
    public int getNowPage() {
        if (pageSize != 0 && nowPage == 0) {
            return -1;
        }
        if (pageSize == 0) {
            return 0;
        }
        return nowPage;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    /**
     * 得到每页多少条记录
     *
     * @return int
     */
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 得到一共多少条数据
     *
     * @return int
     */
    public int getDataTotal() {
        if (pageSize != 0) {
            return dataTotal;
        }
        return size();
    }

    public void setDataTotal(int dataTotal) {
        this.dataTotal = dataTotal;
    }

    /**
     * 得到一共有多少页
     *
     * @return int
     */
    public int getPageTotal() {
        int pageTotal = 1;
        int dataTotal = getDataTotal();
        int nowPage = getNowPage();
        if (nowPage < 1) {
            return 1;
        }
        if (nowPage > 0 && pageSize > 0 && dataTotal > 0) {
            pageTotal = dataTotal / pageSize;
            if (dataTotal % pageSize > 0) {
                pageTotal++;
            }
        }
        return pageTotal;
    }

    /**
     * 转换为XML(未实现)
     *
     * @return XML格式的字符串
     */
    public StringBuffer toXml() throws IOException, SQLException {
        StringBuffer buff = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<datas page=\"");
        buff.append(this.getNowPage()).append("\" dataTotal=\"").append(getDataTotal()).append("\" pageNum=\"").append(getPageTotal());
        buff.append("\" pageTotal=\"").append(getPageTotal()).append("\">\n");
        for (int i = 0; i < size(); i++) {
            buff.append(getData(i).toXml());
        }
        buff.append("</datas>");
        return buff;
    }

    /**
     * 转换为XML字符串
     *
     * @return XML字符串
     */
    public String toXmlStr() throws IOException, SQLException {
        return toXml().toString();
    }

    /**
     * 转换为JSON串
     *
     * @return
     */
    public String toJsonStr() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"page\":\"").append(getNowPage()).append("\"");
        sb.append(",\"pageSize\":\"").append(getPageSize()).append("\"");
        sb.append(",\"pageTotal\":\"").append(getPageTotal()).append("\"");
        sb.append(",\"dataTotal\":\"").append(getDataTotal()).append("\"");
        sb.append(",\"dataList\":[");
        int len = size();
        for (int i = 0; i < len; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(getData(i).toJsonStr());
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * json数组转dataList
     *
     * @param jsonStr
     * @return
     */
    public static DataList parseDataList(String jsonStr) {
        try {
            JSONObject json = JSONObject.parseObject(jsonStr);
            DataList dataList = new DataList();
            dataList.setDataTotal(json.getIntValue("dataTotal"));
            dataList.setPageSize(json.getIntValue("pageSize"));
            dataList.setNowPage(json.getIntValue("page"));
            JSONArray ja = json.getJSONArray("dataList");
            for (int i = 0; i < ja.size(); i++) {
                JSONObject js = ja.getJSONObject(i);
                Data data = Data.parseData(js, null);
                dataList.add(data);
            }
            return dataList;
        } catch (Exception e) {
            log.logError("字符串转DataList失败：{}", e, jsonStr);
        }
        return null;
    }

    /**
     * json数组转dataList
     *
     * @param jsonStr
     * @return
     */
    public static DataList parseDataList(String jsonStr, String pk, String... field) {
        try {
            JSONArray jsonArray = JSONArray.parseArray(jsonStr);
            return parseDataList(jsonArray, pk, field);
        } catch (Exception e) {
            log.logInfo("json解析失败");
            throw new JSONException("20010");
        }
    }

    /**
     * json数组转dataList
     *
     * @param jsonArray
     * @param pk
     * @param field
     * @return
     */
    public static DataList parseDataList(JSONArray jsonArray, String pk, String... field) {
        DataList dataList = new DataList();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject js = jsonArray.getJSONObject(i);
            Data data = Data.parseData(js, pk, field);
            dataList.add(data);
        }
        return dataList;
    }

    /**
     * 判断dataList是否为空
     *
     * @param dataList
     * @return
     */
    public static boolean isNotEmpty(DataList dataList) {
        return dataList == null && dataList.size() > 0;
    }

    public String toString() {
        return toJsonStr();
    }
}
