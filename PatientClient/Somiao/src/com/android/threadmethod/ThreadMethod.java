package com.android.threadmethod;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.android.constant.Constant;
import com.android.constant.Value;
import com.android.ser_exception.ThrowException;

public class ThreadMethod implements InterfaceThread{

	
	public HttpParams setTimeOut(){
		//通过params设置编码格式
		HttpParams	params	=	new	BasicHttpParams();
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		//设置连接超时
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		//设置服务器响应时间
		HttpConnectionParams.setSoTimeout(params, 3000);
		return params;
	}
	
	public ClientConnectionManager setConman(){
		SchemeRegistry	registry	=	new	SchemeRegistry();
		registry.register(new Scheme("http",PlainSocketFactory.getSocketFactory(),80));
		registry.register(new Scheme("https",PlainSocketFactory.getSocketFactory(),433));
		ClientConnectionManager	conman	=	new	ThreadSafeClientConnManager(setTimeOut(), registry);
		return conman;
	}
	@Override
	public void logicGet(String codeNumber) throws Exception{
		Thread.sleep(2000);
		String url	=	Constant.Message_URL+"login.php?codeNumber="+codeNumber;
		HttpClient	client	=	new	DefaultHttpClient(setConman(),setTimeOut());
		HttpGet	get	=	new	HttpGet(url);
		
		HttpResponse response = client.execute(get);
		if(response.getStatusLine().getStatusCode()!=200){
			throw new ThrowException(Constant.Message_logic_fail_link);
		}else{
			String result	=	EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
//			if(result.equals(codeNumber)){
//				throw new ThrowException(Constant.Message_logic_success);
//			}else{
//				throw new ThrowException(Constant.Message_logic_fail_codeOrseat_wrong);
//			}
//			if(Integer.parseInt(result)>0){
//				Value.id	=	Integer.parseInt(result);
//				throw new ThrowException(Constant.Message_logic_success);
//			}else{
//					throw new ThrowException("条码号不存在");
//			}
			if(!result.equals("")){
				throw new ThrowException(result);
			}else{
				throw new ThrowException("登录失败");
			}
		}
	}

	@Override
	public void checkMessage(int  id) throws Exception {
		String url	=	Constant.Message_URL+"getData.php?patientCode="+id;
		HttpClient	client	=	new	DefaultHttpClient(setConman(),setTimeOut());
		HttpGet	get	=	new	HttpGet(url);
		HttpResponse response = client.execute(get);
		if(response.getStatusLine().getStatusCode()!=200){
			throw new ThrowException(Constant.Message_logic_fail_link);
		}else{
			String result	=	EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			if(!result.equals("")){
				throw new ThrowException(result);
			}
		}
	}
	@Override
	public void ask(String seatId,String name,String codeNumber, String state) throws Exception {
		String url	=	Constant.Message_URL+"callNurse.php?seatId="+seatId+"&"+"patientname="+name+"&"+"nurseId="+codeNumber+"&"+"state="+state;
		HttpClient	client	=	new	DefaultHttpClient(setConman(),setTimeOut());
		HttpGet	get	=	new	HttpGet(url);
		HttpResponse response = client.execute(get);
		if(response.getStatusLine().getStatusCode()!=200){
			throw new ThrowException(Constant.Message_logic_fail_link);
		}else{
			String result	=	EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			Log.i("ask", result);
			if(!result.equals("")){
				throw new ThrowException(result);
			}
		}
	}
	@Override
	public void requestQueue(String codeNumber) throws Exception {
		String url	=	Constant.Message_URL+"getQueueCount.php?patientCode="+codeNumber;
		HttpClient	client	=	new	DefaultHttpClient(setConman(),setTimeOut());
		HttpGet	get	=	new	HttpGet(url);
		HttpResponse response = client.execute(get);
		if(response.getStatusLine().getStatusCode()!=200){
			throw new ThrowException(Constant.Message_logic_fail_link);
		}else{
			String result	=	EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			if(!result.equals("条码号不存在")){
				throw new ThrowException(result);
			}else{
				throw new ThrowException("该用户不存在");
			}
		}
	}
//	http://iamnb.sinaapp.com/app/setSeat.php?patientCode=10000001&seatId=5
	@Override
	public void requestNuresNumberFromSer(String codeNumber, String seatId)
			throws Exception {
		String url	=	Constant.Message_URL+"setSeat.php?patientCode="+codeNumber+"&seatId="+seatId;
		HttpClient	client	=	new	DefaultHttpClient(setConman(),setTimeOut());
		HttpGet	get	=	new	HttpGet(url);
		HttpResponse response = client.execute(get);
		if(response.getStatusLine().getStatusCode()!=200){
			throw new ThrowException(Constant.Message_logic_fail_link);
		}else{
			String result	=	EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			Log.i("haha", result);
			if(!result.equals("条码号不存在")){
				throw new ThrowException(result);
			}else{
				throw new ThrowException("该用户不存在");
			}
		}
	}
}
