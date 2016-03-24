package com.vincent.nurseclient.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.vincent.nurseclient.net.GetWaitingInfusionData.FailCallback;
import com.vincent.nurseclient.net.GetWaitingInfusionData.SuccessCallback;
import com.vincent.nurseclient.utils.Config;

public class GetInfusioningData {
	public GetInfusioningData(String nurseId, final SuccessCallback successCallback, final FailCallback failCallback) {
		new NetConnection(Config.HTTP_GET_INFUSIONING_DATA, HttpMethod.Get, new NetConnection.SuccessCallback() {

			@Override
			public void onSuccess(String result) {
				try {
					if (!"".equals(result)) {
						result = result.substring(0, result.length() - 1);
						String[] jsonStrs = result.split("#");
						for (String string : jsonStrs) {
							JSONObject obj = new JSONObject(string);
							if (successCallback != null) {
								successCallback.onSuccess(obj.getString(Config.KEY_PATIENT_CODE), obj.getString(Config.KEY_SEAT_ID),
										obj.getString(Config.KEY_PATIENT_ID_IN_QUEUE), obj.getString(Config.KEY_DRUG_CODE));
							}
						}
					}else{
						if(successCallback!=null){
							successCallback.onSuccess("无人输液", "", "", "");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if (failCallback != null) {
						failCallback.onFail();
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
		}, "id", nurseId);
	}

	public static interface SuccessCallback {
		void onSuccess(String patientCode, String seatId, String patientIdInQueue, String drugCode);
	}

	public static interface FailCallback {
		void onFail();
	}
}
