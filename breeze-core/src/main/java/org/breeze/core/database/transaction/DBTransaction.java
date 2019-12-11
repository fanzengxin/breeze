package org.breeze.core.database.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Breeze事务管理器
 * @author
 *
 */
public class DBTransaction {
	private Connection conn = null;

	private boolean commit = true;

	/**
	 * 得到数据库事务
	 *
	 * @param conn
	 * @return DBTransaction
	 * @throws SQLException
	 */
	public static DBTransaction getInstance(Connection conn) throws SQLException {
		DBTransaction dt = new DBTransaction();
		dt.setCommit(conn.getAutoCommit());//conn.getAutoCommit()
		dt.setConn(conn);
		if (dt.commit) {
			conn.setAutoCommit(false);
		}
		return dt;
	}
	/**
	 * 提交事务
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		if (conn != null ) {
			conn.commit();
			setAutoCommit();
		}
	}
	/**
	 * 事务回滚
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		if (conn != null ) {
			conn.rollback();
			setAutoCommit();
		}
	}
	/**
	 * 设置提交状态
	 * @throws SQLException
	 */
	public void setAutoCommit() throws SQLException{
		if (conn != null && !conn.isClosed()) {
			conn.setAutoCommit(commit);
		}
	}
	/**
	 * @param conn 要设置的 conn
	 */
	private void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
	 * @param commit 要设置的 commit
	 */
	private void setCommit(boolean commit) {
		this.commit = commit;
	}
}
