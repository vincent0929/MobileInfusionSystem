package com.vincent.nurseclient.net;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.widget.Toast;

import com.vincent.nurseclient.utils.Config;

public class Login {
	public Login(String userName,String password,final SuccessCallback successCallback,final FailCallback failCallback){
		new NetConnection(Config.HTTP_LOGIN, HttpMethod.Get,
				new NetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
						if (TextUtils.isEmpty(result)) {
							if (failCallback != null) {
								failCallback.onFail();
							}
						} else {
							if (successCallback != null) {
								successCallback.onSuccess(result);
							}
						}
					}
				}, new NetConnection.FailCallback() {

					@Override
					public void onFail() {
						if (failCallback != null) {
							failCallback.onFail();
						}
					}
				}, Config.KEY_USERNAME, userName, Config.KEY_PASSWORD, password);
	}
	
	public static interface SuccessCallback{
		void onSuccess(String nurseId);
	}
	
	public static interface FailCallback{
		void onFail();
	}
}
