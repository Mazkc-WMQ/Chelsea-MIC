package upbox.pub;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;


/**
 * 专门用于取得公共properties参数的类
 * @author Administrator
 * wmq 20130730
 */
public class GetPropertiesInfo {
	private static Properties pro = null;
	private static InputStream in = null;
	
	/**
	 * 根据key得到publicArgs.properties-properties中的值
	 * @param key
	 * @return
	 */
	public static String getPropertiesInfo(String key){
		String value = "";
		try {
			pro = new Properties();
			in = GetPropertiesInfo.class.getResourceAsStream("/publicArgs.properties");
			pro.load(in);
			value = ios_UTF(pro.getProperty(key));
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("转换出错...");
		} finally{
			try {
				if (in != null) in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	

	/**
	 * 根据key得到tourist.properties-properties中的值
	 * @param key
	 * @return
	 */
	public static HashMap<String,String> getTouristPropertiesInfo(){
		String value = "";
		String key = "";
		HashMap<String,String> hash = new HashMap<String, String>();
		Enumeration<Object> en = null;
		try {
			pro = new Properties();
			in = GetPropertiesInfo.class.getResourceAsStream("/tourist.properties");
			pro.load(in);
			en = pro.keys();
			while(en.hasMoreElements()){
				key = (String) en.nextElement();
				value = ios_UTF(pro.getProperty(key));
				hash.put(key, value);
			}
		} catch (Exception e) {
			//System.out.println("转换出错...");
		} finally{
			try {
				if (in != null) in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return hash;
	}
	
	public static String ios_UTF(String str) throws UnsupportedEncodingException{
		String iso_UTF = "";
		if(str != null && !"".equals(str)){
			iso_UTF = new String(str.getBytes("ISO8859-1"),"UTF-8");
		}
		return iso_UTF;
	}
}
