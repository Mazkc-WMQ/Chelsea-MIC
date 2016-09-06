package upbox.util;

public class UnicodeUtil {
	/**
	 * 字符串转换unicode
	 */
	public static String string2Unicode(String string) {
	 
	    StringBuffer unicode = new StringBuffer();
	 
	    for (int i = 0; i < string.length(); i++) {
	 
	        // 取出每一个字符
	        char c = string.charAt(i);
	 
	        // 转换为unicode
	        unicode.append("\\u" + Integer.toHexString(c));
	    }
	 
	    return unicode.toString();
	}
}
