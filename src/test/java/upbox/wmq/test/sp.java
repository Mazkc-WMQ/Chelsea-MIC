package upbox.wmq.test;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.org.bean.HttpRespons;
import com.org.pub.HttpUtil;

public class sp {
	public static void main(String[] args) throws IOException {
		HttpUtil hp = new HttpUtil();
		// http://lanxiongsports.com/wx/index.php?c=projects&a=zhichi&format=json&belong_id=187&type=project
		HashMap<String, String> map = new HashMap();
		HttpRespons hps = null;
		map.put("c", "projects");
		map.put("a", "zhichi");
		map.put("format", "json");
		map.put("belong_id", "187");
		map.put("type", "project");
		for (int i = 0; 1 == 1; i++) {
			hps = hp.sendGet("http://lanxiongsports.com/wx/index.php", map);
			System.out.println(hps.getContent());
		}
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
