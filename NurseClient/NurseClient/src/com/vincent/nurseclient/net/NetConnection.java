package com.vincent.nurseclient.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

import com.vincent.nurseclient.utils.Config;

public class NetConnection {
	public NetConnection(final String url,final HttpMethod method,
			final SuccessCallback successCallback,final FailCallback failCallback,final String ...kvs){
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				
				StringBuffer paramsStr=new StringBuffer();
				for(int i=0;i<kvs.length;i+=2){
					paramsStr.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
				}
				
				try {
					URLConnection uc=null;
					switch (method) {
					case Post:
							uc=new URL(url).openConnection();
							uc.setDoOutput(true);
							OutputStream os=uc.getOutputStream();
							OutputStreamWriter osw=new OutputStreamWriter(os, Config.CHARSET);
							BufferedWriter bw=new BufferedWriter(osw);
							bw.write(paramsStr.toString());
							bw.flush();	
						break;
					default:
							uc=new URL(url+"?"+paramsStr.toString()).openConnection();
						break;
					}
					
					System.out.println("Request url:"+uc.getURL());
					System.out.println("Request data:"+paramsStr.toString());
					
					InputStream is=uc.getInputStream();
					InputStreamReader isr=new InputStreamReader(is, Config.CHARSET);
					BufferedReader br=new BufferedReader(isr);
					String line="";
					StringBuffer resultBuffer=new StringBuffer();
					if((line=br.readLine())!=null){
						resultBuffer.append(line);
					}
					System.out.println("Result:"+resultBuffer.toString());
					
					return resultBuffer.toString();
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					if (successCallback != null) {
						successCallback.onSuccess(result);
					}

				}else{
					if(failCallback!=null){
						failCallback.onFail();
					}
				}
				super.onPostExecute(result);
			}
			
		}.execute();
	}
	
	public static interface SuccessCallback{
		void onSuccess(String result);
	}
	
	public static interface FailCallback{
		void onFail();
	}
}
