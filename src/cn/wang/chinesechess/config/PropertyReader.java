
package cn.wang.chinesechess.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;

import cn.wang.chinesechess.ChessUtil;


/**
 * 定义自己的属性读取文件
 * 
 * JDK自带的Properties方法必须读取ISO-8859-1编码的字符，这样编辑汉字不方便
 * 
 * @author wanghualiang
 */
public class PropertyReader {
	private static HashMap<String, String> properties;

	static {
		properties = new HashMap<String, String>();
		try {

			// FileReader reader = new FileReader(new
			// File("config/StringConstants.properties"));
			// 不能含有中文路径-bug
			URL url = getConfigUrl("StringConstants.properties");
			FileReader reader = new FileReader(url.getFile());

			String oneLine = "";
			BufferedReader bufferedReader = new BufferedReader(reader);
			while ((oneLine = bufferedReader.readLine()) != null) {
				// System.out.println(oneLine);
				String[] str = oneLine.split("=");
				// System.out.println(str.length);
				if (str != null && str.length == 2) {
					// 必须调用trim防止前后都空格字符
					properties.put(str[0].trim(), str[1].trim());
					// System.out.println("str[0]:" + str[0] + " str[1]:" +
					// str[1]);
				}
			}
			bufferedReader.close();// 关闭流
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String get(String key) {
		return properties.get(key);
	}

	public static URL getConfigUrl(String name) {
		String path = "config/" + name;
		URL url = null;
		try {
			//getClassLoader直接是从classpath下查找的，不能是‘/’开头的
			url = ChessUtil.class.getClassLoader().getResource(path);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return url;
	}

	public static void main(String[] args) {
		System.out.println(PropertyReader.get("JU_TOOLTIP"));
		System.out.println(PropertyReader.get("MA_TOOLTIP"));
		/*
		 * URL path = PropertyReader.class.getResource("tooltip.properties");
		 * System.out.println(path); System.out.println(path.getPath());
		 * System.out.println(path.getFile());
		 */

	}
}
