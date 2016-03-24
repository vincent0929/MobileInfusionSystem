package com.vincent.nurseclient;

import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.InstrumentedActivity;

import com.vincent.nurseclient.aty.AtyLogin;

public class MainActivity extends InstrumentedActivity{

	private static final int REQUEST_LOGIN_IN = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent=new Intent(MainActivity.this, AtyLogin.class);
		startActivityForResult(intent, REQUEST_LOGIN_IN);
		overridePendingTransition(R.anim.login_animation, R.anim.push_center);	
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_LOGIN_IN:
			if(resultCode==RESULT_CANCELED){
				finish();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}