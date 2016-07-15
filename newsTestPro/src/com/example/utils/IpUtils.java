package com.example.utils;

public class IpUtils {
	public static final int Port=8080;
	public static final String Ip="192.168.1.102";
	public static final String Ip1="192.168.0.104";
	public static final String MainIpServer="http://"+Ip+":"+Port;
	
	

	public static final String SERVER_URL ="http://"+Ip+":"+Port+"/zhbj";
	public static final String CATEGORIES_URL = SERVER_URL + "/categories.json";// 获取分类信息的接口
}
