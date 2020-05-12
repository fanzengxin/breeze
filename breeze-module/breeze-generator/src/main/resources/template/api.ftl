import request from '@/router/axios'
import {cleanPostData} from '@/util/util'

/**
 * 获取${desc_function}数据列表
 * @param query 查询条件
 */
export function fetchList(query) {
  return request({
    url: '/${package_name}/${code_function_low}/page',
    method: 'get',
    params: query
  });
}

/**
 * 根据主键获取单条${desc_function}数据
 * @param ${primary_key}  主键
 */
export function getObj(${primary_key}) {
  return request({
    url: '/${package_name}/${code_function_low}/get',
    method: 'get',
    params: {
      ${primary_key}: ${primary_key}
    }
  });
}

/**
 * 新增${desc_function}数据
 * @param obj ${desc_function}数据
 */
export function addObj(obj) {
  return request({
    url: '/${package_name}/${code_function_low}',
    method: 'post',
    data: {
      data: cleanPostData(obj)
    }
  });
}

/**
 * 修改单条${desc_function}数据
 * @param obj ${desc_function}数据
 */
export function putObj(obj) {
  return request({
    url: '/${package_name}/user',
    method: 'put',
    data: {
      data: cleanPostData(obj)
    }
  });
}

/**
 * 根据主键删除单条${desc_function}数据
 * @param ${primary_key}  主键
 */
export function delObj(${primary_key}) {
  return request({
    url: '/${package_name}/${code_function_low}',
    method: 'delete',
    params: {
      ${primary_key}: ${primary_key}
    }
  });
}