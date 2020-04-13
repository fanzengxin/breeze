package org.breeze.admin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @description: 树形结构工具类
 * @auther: 黑面阿呆
 * @date: 2020-04-11 14:57
 * @version: 1.0.0
 */
public class TreeUtils {

    /**
     * 组装树形数据
     *
     * @param ja
     * @param parentId
     * @return
     */
    public static JSONArray buildTree(JSONArray ja, String parentId) {
        return buildTree(ja, parentId, "parentId", "id");
    }

    /**
     * 组装树形数据
     *
     * @param ja
     * @param parentId
     * @return
     */
    public static JSONArray buildTree(JSONArray ja, String parentId, String parentCode, String primaryCode) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < ja.size(); i++) {
            JSONObject node = ja.getJSONObject(i);
            if (node.getString(parentCode).equalsIgnoreCase(parentId)) {
                result.add(node);
            }
            for (int j = 0; j < ja.size(); j++) {
                JSONObject subNode = ja.getJSONObject(j);
                if (subNode.getString(parentCode).equalsIgnoreCase(node.getString(primaryCode))) {
                    if (node.getJSONArray("children") == null) {
                        node.put("children", new JSONArray());
                    }
                    node.getJSONArray("children").add(subNode);
                }
            }
        }
        return result;
    }
}
