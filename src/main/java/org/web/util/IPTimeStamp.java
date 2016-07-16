package org.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class IPTimeStamp {

	private SimpleDateFormat sdf = null;
	private String ip = null;

	public IPTimeStamp(String ip) {
		super();

		this.ip = ip;
	}

	public IPTimeStamp() {
		super();
	}

	
	/** 
	
	* @Title: getIPTimeRand 
	
	* @Description: TODO 获得通过随机数得到的字符串
	
	* @param @return   
	
	* @return String    返回类型 
	
	* @throws 
	
	*/ 
	public String getIPTimeRand() {
		
		StringBuffer buf = new StringBuffer();
		if (this.ip != null) {
			String s[] = this.ip.split("\\.");// 根据ip以点分割将IP中的数字提取
			for (int i = 0; i < s.length; i++) {
				buf.append(this.addZero(s[i], 3));// 不够三位数的补零
			}
		}
		buf.append(this.getTimeStamp());
		Random r = new Random();// 再在结尾加上三个随机数
		for (int i = 0; i < 3; i++) {
			buf.append(r.nextInt(10));
		}
		return buf.toString();
	}

	
	/** 
	
	* @Title: addZero 
	
	* @Description: TODO 不够三位数的补零
	
	* @param @param str
	* @param @param len
	* @param @return   
	
	* @return String    返回类型 
	
	* @throws 
	
	*/ 
	private String addZero(String str, int len) {

		StringBuffer s = new StringBuffer();
		s.append(str);
		while (s.length() < len) {
			s.insert(0, "0");
		}

		return s.toString();
	}

	
	/** 
	
	* @Title: getTimeStamp 
	
	* @Description: TODO 获取时间戳
	
	* @param @return   
	
	* @return String    返回类型 
	
	* @throws 
	
	*/ 
	private String getTimeStamp() {// 获取时间戳
		this.sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return this.sdf.format(new Date());
	}
}
