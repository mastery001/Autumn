package org.web.framework.action.config;

public class ResultConfig {

	// result属性的名字
	private String name;

	// 跳转类型
	private String typeName;

	// 跳转路径
	private String dispatcherPath;
	
	public ResultConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResultConfig(String name, String typeName, String dispatcherPath) {
		super();
		this.name = name;
		this.typeName = typeName;
		this.dispatcherPath = dispatcherPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getDispatcherPath() {
		return dispatcherPath;
	}

	public void setDispatcherPath(String dispatcherPath) {
		this.dispatcherPath = dispatcherPath;
	}

	@Override
	public String toString() {
		return "ResultConfig [name=" + name + ", typeName=" + typeName
				+ ", dispatcherPath=" + dispatcherPath + "]";
	}

}
