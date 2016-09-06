package upbox.xiao.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.org.pub.PublicMethod;
import com.sun.org.apache.bcel.internal.generic.DADD;

import sun.util.logging.resources.logging;

public class Test {
	public static void main(String[] args) throws ParseException, Exception {
//		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//	    long between = 0;
//	    try {
//	        java.util.Date begin = dfs.parse("2009-07-10 10:22:21.214");
//	        java.util.Date end = dfs.parse("2009-07-10 11:24:49.145");
//	        between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
//	    } catch (Exception ex) {
//	        ex.printStackTrace();
//	    }
////	    long month=between/(60 * 60 * 1000);
//	    long day = between / (24 * 60 * 60 * 1000);
//	    long hour = (between / (60 * 60 * 1000) - day * 24);
//	    long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
//	    long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
//	    long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
//	            - min * 60 * 1000 - s * 1000);
//	    System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
//	            + "毫秒");
//		long between=new Date().getTime()-PublicMethod.getStringToDate("2015-08-05 11:55", "yyyy-MM-dd HH:mm").getTime();
//		long day = between / (24 * 60 * 60 * 1000);
//	    long hour = between/(60*60*1000);
//	    long month=day/28;
//	    long year=month/12;
//		if(day<=0){
//			System.out.println(hour+"小时前");
//		}else if(day>=2&&day<7){
//			System.out.println(day+"天前");
//		}else if(day>=7&&day<=28){
//			System.out.println(day/7+"周前");
//		}else if(month<=12){
//			System.out.println(month+"月前");
//		}else if(year>=1){
//			System.out.println(year+"年");
//		}
//		System.out.println(month);
//		
//		System.out.println((302/100)%2);
//		System.out.println(672%100);
		
		String a="187";
		System.out.println(a.substring(a.length()-4));
	}
}
