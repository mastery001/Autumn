package org.web.dao.core.help;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 给PreparedStatement的预编译的语句进行赋值
 * @author Administrator
 *
 */
class SetPreparedStatement {

	/**
	 * 声明8大基本数据类型
	 */
	private final static int IntegerType = 1;

	private final static int StringType = 2;

	private final static int DateType = 3;

	public void setPreparedStatementByPropertiesType(PreparedStatement pstmt,
			List<ColumnMeta> list, Map<String, Object> beanMap)
			throws SQLException {
		int index = 1;
		for (ColumnMeta cm : list) {
			if (cm.isAutoIncrement()) {
				continue;
			}
			// 获取此字段的类型
			int columnType = changePropertiesTypeToInt(cm.getColumnType()
					.toLowerCase());
			// 获取此字段的值
			Object parameter = beanMap.get(cm.getColumnName());
			/*System.out.println("index is : " + index + ",columnType is "+ columnType + "=_="
					+ cm.getColumnName() + "'value is : " + parameter);*/
			if (parameter == null) {
				pstmt.setNull(index, columnType);
				index++;
				continue;
			}
			switch (columnType) {
			case IntegerType:
				pstmt.setInt(index, (Integer) parameter);
				break;
			case StringType:
				pstmt.setString(index, String.valueOf(parameter));
				break;
			case DateType: {
				Date time = (Date) parameter;
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String dateString = formatter.format(time);
				Timestamp timestamp = Timestamp.valueOf(dateString);
				pstmt.setTimestamp(index, timestamp);
			}
				break;
			default:
				throw new SQLException("配置的数据类型出现错误！！！！");
			}
			index++;
		}
	}

	/**
	 * 通过传入的类型字符串,来匹配得到声明的基本数据类型的int值.
	 * 
	 * @param type
	 * @return
	 */
	private int changePropertiesTypeToInt(String type) {
		int result = -1;
		if (type.indexOf("int") != -1 || type.indexOf("integer") != -1
				|| type.indexOf("float") != -1 || type.indexOf("double") != -1
				|| type.indexOf("nember") != -1
				|| type.indexOf("numeric") != -1) {
			result = IntegerType;
		} else if (type.indexOf("date") != -1) {
			result = DateType;
		} else {
			result = StringType;
		}
		return result;
	}
}
