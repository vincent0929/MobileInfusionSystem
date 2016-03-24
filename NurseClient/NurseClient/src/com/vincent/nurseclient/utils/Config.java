package com.vincent.nurseclient.utils;

import java.nio.charset.Charset;

public class Config {
	public static final String CHARSET="utf-8";
	public static final String HTTP_LOGIN="http://iamnb.sinaapp.com/app/nurse/login.php";
	public static final String HTTP_GET_WAITONG_INFUSION_DATA="http://iamnb.sinaapp.com/app/nurse/getPatientList.php";
	public static final String HTTP_GET_INFUSIONING_DATA="http://iamnb.sinaapp.com/app/nurse/getCurrentPai.php";
	public static final String HTTP_CONFIRM_PATIENT="http://iamnb.sinaapp.com/app/nurse/truePatient.php";
	public static final String HTTP_CHANGE_MEDICINE="http://iamnb.sinaapp.com/app/nurse/changeDrug.php";
	public static final String KEY_USERNAME="username";
	public static final String KEY_PASSWORD="password";
	public static final String KEY_NURSE_ID="nurseId";
	public static final String KEY_PATIENT_CODE="patientCode";
	public static final String KEY_SEAT_ID="seatId";
	public static final String KEY_DRUG_CODE="drugCode";
	public static final String KEY_PATIENT_ID_IN_QUEUE="id";
	public static final String KEY_PATIENT_IS_PREPARED="patientIsPrepared";
	public static final String KEY_PATIENT_STATUS="patientStatus";
	public static final String KEY_RESULT_CONFIRM_PATIENT="resultConfirmPatient";
	public static final String KEY_CURR_COUNT="currCount";
	public static final String KEY_ALL_COUNT="allCount";
	
	public static final int RESULT_STATUS_SUCCESS=1;
	public static final int RESULT_STATUS_FAIL=0;
	
	public static final int REQUEST_CODE_LOGIN = 0;
	public static final int REQUEST_CODE_CONFIRM_PATIENT = 1;
	public static final int REQUEST_CODE_CHANGE_MEDICAMENT = 2;
	
	private static String nurseId;
	public static String getNurseId(){
		return nurseId;
	}
	public static void setNurseId(String nurseId){
		Config.nurseId=nurseId;
	}
}
