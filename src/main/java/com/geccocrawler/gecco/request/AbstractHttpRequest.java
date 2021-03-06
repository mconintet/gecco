package com.geccocrawler.gecco.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

public abstract class AbstractHttpRequest implements HttpRequest, Comparable<HttpRequest>, Serializable {
	
	private static final long serialVersionUID = -7284636094595149962L;

	//private static BeanCopier copier = BeanCopier.create(AbstractHttpRequest.class, AbstractHttpRequest.class, false);
	
	private String url;
	
	private String charset;
	
	private Map<String, String> parameters;
	
	private Map<String, String> cookies;
	
	private Map<String, String> headers;
	
	private int priority;
	
	public AbstractHttpRequest() {
		this.parameters = new HashMap<String, String>(1);
		this.headers = new HashMap<String, String>(1);
		this.cookies = new HashMap<String, String>(1);
	}
	
	public AbstractHttpRequest(String url) {
		this();
		this.setUrl(url);
	}

	public void addCookie(String name, String value) {
		cookies.put(name, value);
	}
	
	@Override
	public void addParameter(String name, String value) {
		parameters.put(name, value);
	}
	
	@Override
	public void setParameters(Map<String, String> parameters) {
		this.parameters.putAll(parameters);
	}

	@Override
	public String getParameter(String name) {
		return parameters.get(name);
	}

	@Override
	public HttpRequest subRequest(String url) {
		try {
			HttpRequest request = (HttpRequest)clone();
			request.setUrl(url);
			request.refer(this.getUrl());
			return request;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public void addHeader(String name, String value) {
		headers.put(name, value);
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public Map<String, String> getHeaders() {
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<String, String> entry : cookies.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
		}
		headers.put("Cookie", sb.toString());
		return headers;
	}

	@Override
	public void refer(String refer) {
		this.addHeader("Referer", refer);
	}

	@Override
	public Map<String, String> getParameters() {
		return parameters;
	}

	@Override
	public String getCharset() {
		return charset;
	}

	@Override
	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public void setPriority(int prio) {
		this.priority = prio;
	}

	@Override
	public Map<String, String> getCookies() {
		return cookies;
	}

	@Override
	public void setUrl(String url) {
		this.url = StringUtils.substringBefore(url, "#");
	}

	/**
	 * 数字小，优先级高  
	 */
	@Override
	public int compareTo(HttpRequest o) {
		return this.priority > o.getPriority() ? 1 : this.priority < o.getPriority() ? -1 : 0;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		//BeanCopier copier = BeanCopier.create(this.getClass(), this.getClass(), false);
		//HttpRequest request = this.getClass().getConstructor().newInstance();
		//copier.copy(this, request, null);
		//通过json的序列号和反序列化实现对象的深度clone
		String text = JSON.toJSONString(this); //序列化
		HttpRequest request = JSON.parseObject(text, this.getClass()); //反序列化
		return request;
	}

	public static void main(String[] args) {
		HttpRequest request = new HttpGetRequest("aaa");
		
	}
	
}
