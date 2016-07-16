package org.web.dao.core.help;

public class ColumnMeta implements Cloneable {

	private String columnName;

	private String columnType;
	
	private boolean autoIncrement;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	@Override
	protected Object clone() {
		Object object = null;
		try {
			object = super.clone();
		} catch (CloneNotSupportedException exception) {
			System.err.println("AbstractSpoon is not Cloneable");
		}
		return object;
	}

	@Override
	public String toString() {
		return "ColumnMeta [columnName=" + columnName + ", columnType="
				+ columnType + ", autoIncrement=" + autoIncrement + "]";
	}

}
