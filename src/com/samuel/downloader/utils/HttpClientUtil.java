package com.samuel.downloader.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

	public static HttpClient httpClientUtil = new DefaultHttpClient();

	public static String getRequest(final String BASE_URL,
			final String reqparams) throws InterruptedException,
			ExecutionException {
		String result = null;
		try {

			System.out.println(BASE_URL + reqparams);
			// String url = BASE_URL + AppMarketUtils.encodeingUrl( reqparams);
			// System.out.println(url);

			HttpGet get = new HttpGet(BASE_URL + reqparams);
			HttpClient httpClientUtil = new DefaultHttpClient();

			HttpResponse httpResponse = httpClientUtil.execute(get);
			System.out.println("respcode is  "
					+ httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				result = EntityUtils.toString(httpResponse.getEntity());
				if (result != null && result.length() > 0) {
					return result;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static InputStream getRequestXML(final String requestUrl)
			throws InterruptedException, ExecutionException {
		try {
			if (requestUrl != null && requestUrl != "" && requestUrl != "null") {

				System.out.println(requestUrl);
				HttpGet get = new HttpGet(requestUrl);
				HttpClient httpClientUtil = new DefaultHttpClient();
				HttpResponse httpResponse = httpClientUtil.execute(get);
				System.out.println("respcode is  "
						+ httpResponse.getStatusLine().getStatusCode());
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					return httpResponse.getEntity().getContent();
				} else {
					return null;
				}
			}else{
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getRequest2(final String BASE_URL,
			final String reqparams) throws InterruptedException,
			ExecutionException {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					public String call() throws Exception {
						System.out.println(BASE_URL + reqparams);
						HttpGet get = new HttpGet(BASE_URL + reqparams);
						HttpResponse httpResponse = httpClientUtil.execute(get);
						System.out.println("respcode is  "
								+ httpResponse.getStatusLine().getStatusCode());
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							String result = EntityUtils.toString(httpResponse
									.getEntity());
							return result;
						}
						return null;
					}
				});

		new Thread(task).start();
		return task.get();
	}

	/**
	 * 从流中获得服务器返回的字符串数据
	 * 
	 * @param response
	 * @return String
	 * @throws IOException
	 */
	public static String getDataStr(HttpResponse response) throws IOException {
		InputStream is = response.getEntity().getContent();
		Reader reader = new BufferedReader(new InputStreamReader(is), 4000);

		StringBuilder buffer = new StringBuilder((int) response.getEntity()
				.getContentLength());
		try {
			char[] tmp = new char[1024];
			int l;
			while ((l = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}
		} finally {
			reader.close();
		}
		return buffer.toString();
	}

	public static String postRequest(final String url,
			final Map<String, String> rawParams) throws InterruptedException,
			ExecutionException {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					public String call() throws Exception {
						HttpPost post = new HttpPost(url);
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						for (String key : rawParams.keySet()) {
							params.add(new BasicNameValuePair(key, rawParams
									.get(key)));
						}
						post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
						HttpResponse httpResponse = httpClientUtil
								.execute(post);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							String resut = EntityUtils.toString(httpResponse
									.getEntity());
							return resut;
						}
						return null;
					}
				});
		new Thread(task).start();
		return task.get();
	}

	public static int ok = 200;
	public static int Partial_Content = 206;
	public static int Not_Found = 404;

	public static int check(int check) {
		switch (check) {
		case 200:
			return 200;
		case 206:
			return 206;
		case 404:
			throw new RuntimeException("没锟斤拷锟揭碉拷锟侥硷拷");
		}
		return -1;
	}
}
