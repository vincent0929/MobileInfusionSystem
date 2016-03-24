 package com.vincent.nurseclient.net;

import com.vincent.nurseclient.utils.Config;

public class ConfirmPatient {
	public ConfirmPatient(String patientCode,String drugCode,final SuccessCallback successCallback,final FailCallback failCallback){
		new NetConnection(Config.HTTP_CONFIRM_PATIENT, HttpMethod.Get, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				if(Integer.toString(Config.RESULT_STATUS_SUCCESS).equals(result)){
					if(successCallback!=null){
						successCallback.onSuccess(result);
					}
				}else{
					if(failCallback!=null){
						failCallback.onFail();
					}
				}
			}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				if(failCallback!=null){
					failCallback.onFail();
				}
			}
		},Config.KEY_PATIENT_CODE,patientCode,Config.KEY_DRUG_CODE,drugCode);
	}
	
	public static interface SuccessCallback{
		void onSuccess(String result);
	}
	
	public static interface FailCallback{
		 void onFail();
	}
}
