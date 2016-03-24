  package com.vincent.nurseclient.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.vincent.nurseclient.utils.Config;

public class ChangeMedicine {
	public ChangeMedicine(String medicineBarcode,final SuccessCallback successCallback,final FailCallback failCallback){
		new NetConnection(Config.HTTP_CHANGE_MEDICINE, HttpMethod.Get, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				try {
					if(!Integer.toString(Config.RESULT_STATUS_FAIL).equals(result)){
						JSONObject obj = new JSONObject(result);
						
						if(successCallback!=null){
							successCallback.onSuccess(obj.getString(Config.KEY_CURR_COUNT),
									obj.getString(Config.KEY_ALL_COUNT));
						}
					}else{
						if(failCallback!=null){
							failCallback.onFail();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				if(failCallback!=null){
					failCallback.onFail();
				}
			}
		}, Config.KEY_DRUG_CODE,medicineBarcode);
	}
	
	public static interface SuccessCallback{
		void onSuccess(String currCount,String allCount);
	}
	
	public static interface FailCallback{
		void onFail();
	}
}
