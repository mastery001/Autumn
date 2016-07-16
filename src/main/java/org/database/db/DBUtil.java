package org.database.db;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import tool.mastery.log.Logger;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * JDBC操作的工具类,加载数据库驱动,获取数据库连接
 * 
 * @author AntsMarch
 * 
 */
public class DBUtil {
	
	private static final Logger Log = Logger.getLogger(DBUtil.class);
	
	private static Connection conn = null;
	// Druid数据源变量
	private static DruidDataSource druidDataSource = null;

	private DBUtil() {
	}

	/**
	 * 加载数据库连接驱动
	 */
	// 加载Druid数据库连接池配置文件dbconfig.properties，获取数据库连接驱动，url,user信息等；
	static {
		Properties prop = new Properties();
		InputStream in = DBUtil.class.getClassLoader().getResourceAsStream(
				"db.properties");
		try {
			prop.load(in);
			druidDataSource = (DruidDataSource) DruidDataSourceFactory
					.createDataSource(prop);
		} catch (Exception e) {
			Log.debug(e.getMessage() , e);
		}
	}

	/*
	 * static { InputStream is =
	 * DBUtil.class.getClassLoader().getResourceAsStream(
	 * ConfigLibrary.DBPROPERTIESPATH); Properties prop = new Properties(); try
	 * { prop.load(is); DriverName = prop.getProperty("DRIVER_NAME"); Url =
	 * prop.getProperty("DRIVER_URL"); //databaseName =
	 * prop.getProperty("DATABASENAME"); username =
	 * prop.getProperty("USERNAME"); password = prop.getProperty("PASSWORD"); }
	 * catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } try { Class.forName(DriverName); } catch
	 * (ClassNotFoundException e) { e.printStackTrace(); } }
	 */
	/**
	 * 获取数据库连接
	 * 
	 * @return Connection conn
	 */
	public static Connection getConnection() {
		/*
		 * String url = "jdbc:mysql://localhost:3306/" + databaseName +
		 * "?useUnicode=true&characterEncoding=utf8";
		 */
		try {
			// conn = DriverManager.getConnection(url, "root", "root");
			conn = druidDataSource.getConnection();
		} catch (SQLException e) {
			Log.debug(e.getMessage() , e);
		}

		return conn;
	}

	public static void close(ResultSet rs, PreparedStatement pst, Connection con) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
			Log.debug(e.getMessage() , e);
		}

	}

	public static void close() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			Log.debug(e.getMessage() , e);
		}
	}

	/**
	 * 
	 * @Title: getPstmt
	 * 
	 * @Description: TODO 获取数据库的预处理命令类PreparedStatement
	 * 
	 * @param conn
	 *            数据库的连接
	 * @param sql
	 *            需要执行的sql语句
	 * @param
	 * 
	 * @return PreparedStatement 返回类型
	 * 
	 * @throws
	 */
	public static PreparedStatement getPstmt(Connection conn, String sql) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			Log.debug(e.getMessage() , e);
		}
		return pstmt;
	}

	/**
	 * @Title getPstmt
	 * @param conn
	 * @param sql
	 * @Discription 获取数据库处理对象
	 * @return PreparedStatement
	 */
	public static PreparedStatement getPstmt(Connection conn, String sql,
			int autoGenerateKeys) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql, autoGenerateKeys);
		} catch (SQLException e) {
			Log.debug(e.getMessage() , e);
		}
		return pstmt;
	}

	/**
	 * 
	 * @Title: getRs
	 * 
	 * @Description: TODO 获取结果集
	 * 
	 * @param pstmt
	 *            PreparedStatement对象
	 * @param
	 * 
	 * @return ResultSet 返回类型
	 * 
	 * @throws
	 */
	public static ResultSet getRs(PreparedStatement pstmt) {
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			Log.debug(e.getMessage() , e);
		}
		return rs;
	}

	/**
	 * 
	 * @Title: close
	 * 
	 * @Description: TODO 关闭结果集的连接
	 * 
	 * @param rs
	 *            结果集
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 */
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				Log.debug(e.getMessage() , e);
			}
			rs = null;
		}
	}

	/**
	 * 
	 * @Title: close
	 * 
	 * @Description: TODO 关闭PreparedStatement的连接
	 * 
	 * @param pstmt
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 */
	public static void close(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				Log.debug(e.getMessage() , e);
			}
			pstmt = null;
		}
	}

	/**
	 * 
	 * @Title: close
	 * 
	 * @Description: TODO 关闭PreparedStatement的连接
	 * 
	 * @param pstmt
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 */
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				Log.debug(e.getMessage() , e);
			}
			stmt = null;
		}
	}

	/**
	 * 
	 * @Title: closeDatabase
	 * 
	 * @Description: TODO 关闭所有连接
	 * 
	 * @param rs
	 * @param pstmt
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 */
	public static void closeDatabase(ResultSet rs, PreparedStatement pstmt) {
		DBUtil.close(rs);
		DBUtil.close(pstmt);
	}

	public static boolean executeStatement(Connection conn, String sql) {
		boolean flag = false;
		try {
			Statement stmt = conn.createStatement();
			flag = stmt.execute(sql);
		} catch (SQLException e) {
			Log.debug(e.getMessage() , e);
		}
		return flag;
	}
}
