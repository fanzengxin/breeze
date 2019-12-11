package org.breeze.core.database.manager;

import org.breeze.core.exception.DBException;
import org.breeze.core.utils.xml.XmlBean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;


/**
 * 连接池接口
 * @author 黑面阿呆
 *
 */
public interface IConnection {

	/**
	 * 获取数据库链接
	 * @param databaseName
	 * @return
	 * @throws DBException
	 */
	Connection getConnection(String databaseName)throws DBException;

	/**
	 * 获取数据源
	 * @param databaseName
	 * @return
	 */
	DataSource getDataSource(String databaseName);
}
