package org.breeze.admin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.breeze.admin.dao.DeptDao;
import org.breeze.admin.util.TreeUtils;
import org.breeze.core.annotation.common.AutoAdd;
import org.breeze.core.annotation.service.Service;
import org.breeze.core.bean.data.Data;
import org.breeze.core.bean.data.DataList;
import org.breeze.core.bean.log.Serial;
import org.breeze.core.bean.login.LoginInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 部门服务层
 * @auther: 黑面阿呆
 * @date: 2020-04-04 20:23
 * @version: 1.0.0
 */
@Service
public class DeptService {

    @AutoAdd
    private DeptDao deptDao;


    /**
     * 获取部门树
     *
     * @param serial
     * @return
     */
    public JSONArray getDeptTree(Serial serial) {
        DataList dataList = deptDao.getDeptList(null, serial);
        JSONArray ja = new JSONArray();
        for (int i = 0; i < dataList.size(); i++) {
            Data d = dataList.getData(i);
            JSONObject json = new JSONObject();
            json.put("id", d.getLong("ID"));
            json.put("parentId", d.getLong("PARENT_ID"));
            json.put("label", d.getString("DEPT_NAME"));
            json.put("children", new JSONArray());
            ja.add(json);
        }
        return TreeUtils.buildTree(ja, "0");
    }

    /**
     * 获取部门列表
     *
     * @param serial
     * @return
     */
    public DataList getDeptList(Long id, Serial serial) {
        return deptDao.getDeptList(id, serial);
    }

    /**
     * 根据用户id获取部门信息
     *
     * @param userId
     * @param serial
     * @return
     */
    public List<String> getDeptByUser(String userId, Serial serial) {
        DataList dl = deptDao.getDeptsByUser(userId, serial);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < dl.size(); i++) {
            list.add(dl.getData(i).getString("dept_id"));
        }
        return list;
    }

    /**
     * 保存部门数据
     *
     * @param loginInfo
     * @param data
     * @param serial
     * @return
     */
    public boolean create(LoginInfo loginInfo, Data data, Serial serial) {
        data.add("create_id", loginInfo.getUid());
        return deptDao.save(data, serial);
    }

    /**
     * 更新部门数据
     *
     * @param loginInfo
     * @param data
     * @param serial
     * @return
     */
    public boolean update(LoginInfo loginInfo, Data data, Serial serial) {
        data.setPrimaryKey("ID");
        data.add("update_id", loginInfo.getUid());
        return deptDao.update(data, serial);
    }

    /**
     * 删除部门数据
     *
     * @param id
     * @param serial
     * @return
     */
    public int delete(String id, Serial serial) {
        Data remove = new Data();
        remove.add("id", id, true);
        return deptDao.remove(remove, serial);
    }

}
