package com.xclenter.test.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpCommon {
    CloseableHttpClient httpClient;
    
    CookieStore cookieStore;
    HttpContext httpContext;
    private static HttpCommon httpCommon;
    
    private HttpCommon(){
    	httpClient = HttpClients.createDefault();
    	
    	/*
    	 * 配置cookie管理
    	 */
        cookieStore = new BasicCookieStore();
        httpContext = new BasicHttpContext();
        
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        
        get_csrf();
    }
    
    public static HttpCommon getHttpCommon(){
    	if(httpCommon == null){
    		httpCommon = new HttpCommon();
    	}
    	
    	return httpCommon;
    }
    /*
     * 初始化cookie
     */
	private  void get_csrf(){
		try {
			String url = "http://"+ServerInfo.serverIP+"/get_csrf/";
			URI uri = new URIBuilder().setPath(url).build();
			HttpGet httpGet = new HttpGet(uri);
			HttpResponse  httpResponse = httpClient.execute(httpGet,httpContext);
			StatusLine statusLine = httpResponse.getStatusLine();
			
			if(statusLine.getStatusCode() == 200){
				return;
			}else{
				throw new Exception("access server fail");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setX_csrf(HttpRequest request){
		if(cookieStore != null){
			List<Cookie> cookies = cookieStore.getCookies();
			for(Cookie cookie : cookies){
				if( cookie.getName().equals( "csrftoken" )){
					String csrf_token = cookie.getValue();
					request.setHeader("X-CSRFToken", csrf_token);
					return;
				}
			}
		}
	}
	
	public JSONObject getJsonResponseWithJSONmessage(String url,JSONObject bodymessage){
		
		try {
			URI uri = new URIBuilder().setPath(url).build();
			HttpPost httpPost= new HttpPost(uri);
			
			StringEntity requestEntity = new StringEntity(bodymessage.toString());
			requestEntity.setContentEncoding("UTF-8");
			httpPost.setHeader("Accept", "application/json");
	        httpPost.setHeader("Content-type", "application/json");
			httpPost.setEntity(requestEntity);
			setX_csrf(httpPost);
			HttpResponse  httpResponse = httpClient.execute(httpPost,httpContext);
			StatusLine statusLine = httpResponse.getStatusLine();
			
			if(statusLine.getStatusCode() == 200){
				String responseString = EntityUtils.toString(httpResponse.getEntity());
				JSONObject rejson =JSONObject.fromObject(responseString);
				return rejson;
			}else{
				throw new Exception("access server fail");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public JSONObject getJsonResponseWithParams(String url,Map<String,String> params){
		
		try {
			URI uri = new URIBuilder().setPath(url).build();
			HttpPost httpPost= new HttpPost(uri);
			List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
			for(String key : params.keySet()){
				nvps.add(new BasicNameValuePair(key , params.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps)); 
			
			setX_csrf(httpPost);
			HttpResponse  httpResponse = httpClient.execute(httpPost,httpContext);
			StatusLine statusLine = httpResponse.getStatusLine();
			
			if(statusLine.getStatusCode() == 200){
				String responseString = EntityUtils.toString(httpResponse.getEntity());
				JSONObject rejson =JSONObject.fromObject(responseString);
				return rejson;
			}else{
				throw new Exception("access server fail");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
