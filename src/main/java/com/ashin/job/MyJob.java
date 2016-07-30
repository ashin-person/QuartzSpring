package com.ashin.job;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyJob {
	public void run(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
		String time = sdf.format(date);
		System.out.println("正在调用MyJob "+time);
	}
}
