package com.samuel.downloader.utils;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

public class ParseXmlUtils {
	/**
	 * 远程服务器放置一个xml文件，用来说明当前新版本的信息。包括版本号，新版本功能说明，新版下载链接。
	 * 
	 * 
	 * 
	 * 通过 InputStream 解析文件 用 pull 解析器解析服务器返回的 xml 文件(xml文件封装了版本号)
	 * 
	 * pull 解析的特点: 事件驱动。当解析器发现元素开始、元素结束、文本、文档的开始或结束等时，发送事件，程序员编写响应这些事件的代码，保存数据。
	 * 优点：不用事先调入整个文档，占用资源少
	 * 缺点：不是持久的；事件过后，若没保存数据，那么数据就丢了；无状态性；从事件中只能得到文本，但不知该文本属于哪个元素；
	 * 使用场合：只需XML文档的少量内容，很少回头访问；一次性读取；机器内存少；
	 */
	public static UpdateInfo getUpdataInfo(InputStream is) throws Exception {

		Log.i("xml", "into");
		XmlPullParser parser = Xml.newPullParser();
		// 设置解析的数据源
		parser.setInput(is, "UTF-8");
		int type = parser.getEventType();
		// 实体类(自定义)
		UpdateInfo updataInfo = new UpdateInfo();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if ("versioncode".equals(parser.getName())) {
					// 获取版本号
					updataInfo.setVersioncode(Integer.valueOf(parser.nextText().trim()));
				} else if ("versionname".equals(parser.getName())) {
					// 获取要升级的APK文件
					updataInfo.setVersionname(parser.nextText());
				} else if ("url".equals(parser.getName())) {
					// 获取要升级的APK文件
					updataInfo.setUrl(parser.nextText());
				} else if ("description".equals(parser.getName())) {
					// 获取该文件信息
					updataInfo.setDescription(parser.nextText());
				}
				break;
			}
			type = parser.next();
		}
		Log.i("xml", String.valueOf(updataInfo.getVersioncode()));
		Log.i("xml", String.valueOf(updataInfo.getVersionname()));
		Log.i("xml", updataInfo.getUrl());
		is.close();
		return updataInfo;
	}

}