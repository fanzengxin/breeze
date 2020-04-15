package org.breeze.core.utils.string;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: SQL处理工具类
 * @Auther: 黑面阿呆
 * @Date: 2019/2/23 13:08
 * @Version: 1.0.0
 */
public class UtilSqlFormat {

    /**
     * 获取待处理的SQL语句
     *
     * @param sql    待处理的sql语句
     *               <p>
     *               所有需判断的动态sql部分需用{}包括，内部包含条件判断
     *               如：select * from test where 1=1 {and name = #:nameValue:# and id>10}
     *               如本次传入参数nameValue值为空，则最终sql为 select * from test where 1=1 and id>10
     *               如本次传入参数nameValue值不为空，则最终sql为 select * from test where 1=1 and name=? and id>10， nameValue作为prepareStatement参数传入sql
     *               {:?判断条件, 拼接SQL}
     *               支持自定义条件，如：{?:nameValue>2:? and name=:#nameValue}
     *               表示当nameValue大于2时，sql会拼接后面的and name=#nameValue部分，目前支持 !=、>=、<=、>、<、= 六中条件，支持多条件，用&& 或 || 连接
     *               其中大小判断分为数字判断和字符串判断，当判断符号后面的参数带单引号时，将走字符判断，如果不带单引号，则转为数字判断
     *               <p>
     *               需要替换通配符的部分使用#: :#标记
     *               如：name = #:nameValue:# 最终会变为 name = ? ，其中nameValue(值假设'admin')会作为prepareStatement参数传入sql
     *               支持模糊匹配，如: name like #:%nameValue%:#, 最终会变为 name = ? ，其中nameValue(值会由'admin'变为'%admin%')会作为prepareStatement参数传入sql
     *               <p>
     *               需要直接替换SQL的部分使用$: :$标记
     *               如：name = $:nameValue:$ 最终会变为 name = 'admin' ，其中nameValue的值会直接替换sql文件（此处假设nameValue的值为'admin'）
     *               <p>
     *               需要循环替换SQL的部分使用for: :for标记
     *               如：name in (for:nameValue:for) 如name值（必须为List类型）为[1,2,3]，则最终会变为 name in (?,?,?), 其中name的值会以此作为prepareStatement参数传入sql
     * @param params 待处理的查询参数
     * @return
     */
    public static Map<String, Object> prepareSql(String sql, Map<String, Object> params) {
        // 定义返回结果
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (UtilString.isNullOrEmpty(sql)) {
            return resultMap;
        }
        // 获取动态sql
        String resultSql = getSql(sql, params);
        // 处理所有${}，即直接替换的字段
        while (resultSql.indexOf("$:") != -1) {
            String param = UtilString.subString(resultSql, "$:", ":$").trim();
            if (params.get(param) != null && params.get(param) instanceof String) {
                resultSql = resultSql.replace("$:" + param + ":$", "'" + params.get(param.trim()) + "'");
            } else {
                resultSql = resultSql.replace("$:" + param + ":$", String.valueOf(params.get(param.trim())));
            }
        }
        List<Object> paramList = new ArrayList<Object>();
        // 处理所有#{}，即用占位符替换的字段
        while (resultSql.indexOf("#:") != -1 || resultSql.indexOf("for:") != -1) {
            if (resultSql.indexOf("for:") == -1 || (resultSql.indexOf("#:") != -1 && resultSql.indexOf("#:") < resultSql.indexOf("for:"))) {
                // 处理:#占位符
                String param = UtilString.subString(resultSql, "#:", ":#");
                resultSql = resultSql.replace("#:" + param + ":#", "?");
                param = param.trim();
                // 判断是否有like的%通配符
                if (param.indexOf("%") != -1) {
                    String value = String.valueOf(params.get(param.replaceAll("%", "")));
                    if (param.startsWith("%")) {
                        value = "%" + value;
                    }
                    if (param.endsWith("%")) {
                        value = value + "%";
                    }
                    paramList.add(value);
                } else {
                    paramList.add(params.get(param));
                }
            } else {
                // 处理:for 循环
                String param = UtilString.subString(resultSql, "for:", ":for");
                List list = (List) params.get(param.trim());
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append("?");
                    paramList.add(list.get(i));
                }
                resultSql = resultSql.replace("for:" + param + ":for", sb.toString());
            }
        }
        resultMap.put("sql", resultSql);
        resultMap.put("param", paramList);
        return resultMap;
    }

    /**
     * 根据参数动态拼接SQL
     *
     * @param sql
     * @param params
     * @return
     */
    private static String getSql(String sql, Map<String, Object> params) {
        // 定义最终结果字符串
        StringBuffer sqlStr = new StringBuffer();
        String[] sqls = sql.split("\\{");
        for (int i = 0; i < sqls.length; i++) {
            String[] subSqls = sqls[i].split("\\}");
            if (i > 0) {
                if (subSqls[0].indexOf(("?:")) != -1) {
                    // :? 判断条件处理
                    String whereCheckStr = UtilString.subString(subSqls[0], "?:", ":?");
                    if (whereCheck(whereCheckStr, params)) {
                        sqlStr.append(" ").append(UtilString.subString(subSqls[0], ":?"));
                    }
                } else if (subSqls[0].indexOf("for:") != -1) {
                    // :for 判断条件处理
                    String field = UtilString.subString(subSqls[0], "for:", ":for").trim();
                    Object o = params.get(field);
                    if (o != null) {
                        sqlStr.append(" ").append(subSqls[0]);
                    }
                } else if (subSqls[0].indexOf("#:") != -1) {
                    // 占位符条件判断
                    String field = UtilString.subString(subSqls[0], "#:", ":#").replaceAll("%", "").trim();
                    if (params.get(field) != null && UtilString.isNotEmpty(params.get(field).toString())) {
                        sqlStr.append(" ").append(subSqls[0]);
                    }
                } else if (subSqls[0].indexOf("$:") != -1) {
                    // 文本替换条件判断
                    String field = UtilString.subString(subSqls[0], "$:", ":$").replaceAll("%", "").trim();
                    if (params.get(field) != null && UtilString.isNotEmpty(params.get(field).toString())) {
                        sqlStr.append(" ").append(subSqls[0]);
                    }
                } else {
                    sqlStr.append(" ").append(subSqls[0]);
                }
            }
            if (i == 0 || subSqls.length > 1) {
                String subSql = subSqls[0];
                if (i > 0) {
                    subSql = subSqls[1];
                }
                sqlStr.append(" ").append(subSql);
            }
        }
        return sqlStr.toString();
    }

    /**
     * 验证SQL条件判断
     *
     * @param sql    待验证的条件string
     * @param params 验证参数
     * @return
     */
    private static boolean whereCheck(String sql, Map<String, Object> params) {
        while (sql.indexOf("(") != -1 || sql.indexOf(")") != -1) {
            sql = sql.substring(0, sql.lastIndexOf("(")) + whereSubOrCheck(sql.substring(sql.lastIndexOf("(") + 1,
                    sql.indexOf(")")), params) + sql.substring(sql.indexOf(")") + 1);
        }
        return whereSubOrCheck(sql, params);
    }

    /**
     * 执行sql参数判断or条件
     *
     * @param sql
     * @param params
     * @return
     */
    private static boolean whereSubOrCheck(String sql, Map<String, Object> params) {
        String[] subSqls = sql.split("\\|\\|");
        for (int i = 0; i < subSqls.length; i++) {
            if (whereSubAndCheck(subSqls[i].trim(), params)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 执行sql参数判断and条件
     *
     * @param sql
     * @param params
     * @return
     */
    private static boolean whereSubAndCheck(String sql, Map<String, Object> params) {
        String[] subSqls = sql.split("&&");
        for (int i = 0; i < subSqls.length; i++) {
            String subWhere = subSqls[i].trim();
            if (subWhere.indexOf("!=") != -1) {
                // 不等于的业务处理
                String[] subChecks = subWhere.split("!=");
                if (subChecks.length <= 1 || subChecks[1] == null || subChecks[1].trim().equals(String.valueOf
                        (params.get(subChecks[0].trim())).trim())) {
                    return false;
                }
            } else if (subWhere.indexOf(">=") != -1) {
                // 大于等于的业务处理
                String[] subChecks = subWhere.split(">=");
                if (subChecks.length <= 1 || subChecks[1] == null || params.get(subChecks[0]) == null) {
                    return false;
                }
                if (subChecks[1].startsWith("'") && subChecks[1].endsWith("'") && params.get(subChecks[0].trim()).toString().trim()
                        .compareTo(subChecks[1].trim().substring(1, subChecks[1].trim().length() - 1)) == -1) {
                    return false;
                } else if (new BigDecimal(params.get(subChecks[0]).toString().trim())
                        .compareTo(new BigDecimal(subChecks[1].trim())) == -1) {
                    return false;
                }
            } else if (subWhere.indexOf("<=") != -1) {
                // 小于等于的业务处理
                String[] subChecks = subWhere.split("<=");
                if (subChecks.length <= 1 || subChecks[1] == null || params.get(subChecks[0].trim()) == null) {
                    return false;
                }
                if (subChecks[1].startsWith("'") && subChecks[1].endsWith("'") && params.get(subChecks[0].trim()).toString().trim()
                        .compareTo(subChecks[1].trim().substring(1, subChecks[1].trim().length() - 1)) == 1) {
                    return false;
                } else if (new BigDecimal(params.get(subChecks[0].trim()).toString().trim())
                        .compareTo(new BigDecimal(subChecks[1].trim())) == 1) {
                    return false;
                }
            } else if (subWhere.indexOf(">") != -1) {
                // 大于的业务处理
                String[] subChecks = subWhere.split(">");
                if (subChecks.length <= 1 || subChecks[1] == null || params.get(subChecks[0].trim()) == null) {
                    return false;
                }
                if (subChecks[1].startsWith("'") && subChecks[1].endsWith("'") && params.get(subChecks[0].trim()).toString().trim()
                        .compareTo(subChecks[1].trim().substring(1, subChecks[1].trim().length() - 1)) < 1) {
                    return false;
                } else if (subChecks.length <= 1 || subChecks[1] == null || new BigDecimal(params.get(subChecks[0].trim()).toString().trim())
                        .compareTo(new BigDecimal(subChecks[1].trim())) < 1) {
                    return false;
                }
            } else if (subWhere.indexOf("<") != -1) {
                // 小于的业务处理
                String[] subChecks = subWhere.split("<");
                if (subChecks.length <= 1 || subChecks[1] == null || params.get(subChecks[0].trim()) == null) {
                    return false;
                }
                if (subChecks[1].startsWith("'") && subChecks[1].endsWith("'") && params.get(subChecks[0].trim()).toString().trim()
                        .compareTo(subChecks[1].trim().substring(1, subChecks[1].trim().length() - 1)) > -1) {
                    return false;
                } else if (subChecks.length <= 1 || subChecks[1] == null || new BigDecimal(params.get(subChecks[0].trim()).toString().trim())
                        .compareTo(new BigDecimal(subChecks[1].trim())) > -1) {
                    return false;
                }
            } else if (subWhere.indexOf("=") != -1) {
                // 等于的业务处理
                String[] subChecks = subWhere.split("=");
                if (subChecks.length <= 1 || subChecks[1] == null) {
                    return false;
                }
                if (!subChecks[1].trim().equals(String.valueOf(params.get(subChecks[0].trim())).trim())) {
                    return false;
                }
            } else if ("false".equalsIgnoreCase(subWhere)) {
                return false;
            }
        }
        return true;
    }
}
