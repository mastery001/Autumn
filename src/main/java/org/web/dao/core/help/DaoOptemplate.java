package org.web.dao.core.help;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.database.db.DBUtil;



public class DaoOptemplate {

	//private static final Logger LOG = Logger.getLogger(DaoOptemplate.class);

	private static final SetPreparedStatement EXECUTE = new SetPreparedStatement();

	/**
	 * 设计daoOptemplate类为单例
	 */
	private DaoOptemplate() {
	}

	private static class Optemplate {
		static final DaoOptemplate daoOpt = new DaoOptemplate();
	}

	public static DaoOptemplate getInstance() {
		return Optemplate.daoOpt;
	}

	public void executeUpdate(Connection conn, String sql,
			List<ColumnMeta> list, Map<String, Object> beanMap)
			throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		EXECUTE.setPreparedStatementByPropertiesType(pstmt, list, beanMap);
		// TODO
		// sqlAdvice.print(sql);
		// 用于记录执行sql返回的次数
		pstmt.executeUpdate();
		DBUtil.close(pstmt);
	}


	private static Map<String, List<ColumnMeta>> cache = new HashMap<String, List<ColumnMeta>>();

	/**
	 * 获取对应的实体的meta元值
	 * 
	 * @param sql
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	public List<ColumnMeta> refresh(String tableName, Connection conn)
			throws SQLException {
		if (!cache.containsKey(tableName)) {
			Statement stmt = null;
			try {
				String sql = "select * from " + tableName + " where 1=0";
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				List<ColumnMeta> list = new ArrayList<ColumnMeta>();
				ColumnMeta cm = new ColumnMeta();
				// ResultSetMetaData对象是描述表的结构(字段名,字段类型,可容纳的长度)
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					cm = (ColumnMeta) cm.clone();
					cm.setColumnName(rsmd.getColumnName(i).toLowerCase());
					cm.setColumnType(rsmd.getColumnTypeName(i).toLowerCase());
					cm.setAutoIncrement(rsmd.isAutoIncrement(i));
					list.add(cm);
				}
				cache.put(tableName, list);
				DBUtil.close(rs);
			}finally {
				DBUtil.close(stmt);
			}
		}
		return cache.get(tableName);
	}

	
}
