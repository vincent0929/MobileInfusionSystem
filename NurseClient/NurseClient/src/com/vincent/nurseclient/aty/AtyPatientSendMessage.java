package com.vincent.nurseclient.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.vincent.nurseclient.R;

public class AtyPatientSendMessage extends Activity {

	private  TextView tvPatientSendMessage;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_patient_send_message);
       tvPatientSendMessage=(TextView) findViewById(R.id.tvPatientSendMessage);
        Intent intent = getIntent();
        if (null != intent) {
	        Bundle bundle = getIntent().getExtras();
	        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
	        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
	        tvPatientSendMessage.setText("Title : " + title + " \n " + "Content : " + content);
        }
    }
}
