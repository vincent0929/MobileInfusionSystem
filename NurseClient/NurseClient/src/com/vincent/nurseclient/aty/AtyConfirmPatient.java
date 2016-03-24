package com.vincent.nurseclient.aty;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vincent.nurseclient.R;
import com.vincent.nurseclient.net.ConfirmPatient;
import com.vincent.nurseclient.scan.CaptureActivity;
import com.vincent.nurseclient.utils.Config;

public class AtyConfirmPatient extends Activity {
	protected static final int REQUEST_CODE_CONFIRM_MEDICINE_BARCODE = 0;
	
	private EditText etMedicineBarcode;
	private Button btnAtyConfirmPatientBackToMainActivity;
	private TextView tvBtnConfirm,tvBtnScanMedicineBarcode;
	
	private HttpClient client;
	
	private String patientCode;
	private String resultConfirmPatient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_confirm_patient);
		
		patientCode=getIntent().getStringExtra(Config.KEY_PATIENT_CODE);
		
		init();
		
	}

	private void init() {
		etMedicineBarcode=(EditText) findViewById(R.id.etMedicineBarcode);
		btnAtyConfirmPatientBackToMainActivity=
				(Button) findViewById(R.id.btnAtyConfirmPatientBackToMainActivity);
		btnAtyConfirmPatientBackToMainActivity.setOnClickListener(listener);
		tvBtnScanMedicineBarcode=(TextView) findViewById(R.id.tvBtnScanMedicineBarcode);
		tvBtnScanMedicineBarcode.setOnClickListener(listener);
		tvBtnConfirm=(TextView) findViewById(R.id.tvBtnConfirm);
		tvBtnConfirm.setOnClickListener(listener);
		
		client=new DefaultHttpClient();
	}
	
	private OnClickListener listener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.tvBtnScanMedicineBarcode:
				etMedicineBarcode.setText("");
				intent=new Intent(AtyConfirmPatient.this, CaptureActivity.class);
				startActivityForResult(intent, REQUEST_CODE_CONFIRM_MEDICINE_BARCODE);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
				break;
			case R.id.btnAtyConfirmPatientBackToMainActivity:
				setResult(RESULT_CANCELED);
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
				break;
			case R.id.tvBtnConfirm:
				confirmPatient(patientCode,etMedicineBarcode.getText().toString());
				break;
			default:
				break;
			}
		}	
	};
	
	private void confirmPatient(String patientCode,String drugCode) {
		if(drugCode.isEmpty()){
			Toast.makeText(getApplicationContext(), "确认患者身份失败", Toast.LENGTH_LONG).show();
			return;
		}
		
		new ConfirmPatient(patientCode,drugCode, new ConfirmPatient.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				resultConfirmPatient=result;
				if(Integer.parseInt(result)==Config.RESULT_STATUS_SUCCESS){
					Toast.makeText(getApplicationContext(), "病人和药瓶匹配正确", Toast.LENGTH_SHORT).show();
					finish();
					overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
				}else{
					Toast.makeText(getApplicationContext(), "病人和药瓶匹配不正确", Toast.LENGTH_SHORT).show();					
				}
			}
		}, new ConfirmPatient.FailCallback() {
			
			@Override
			public void onFail() {
				
				Toast.makeText(getApplicationContext(), "病人和药品匹配不正确", Toast.LENGTH_SHORT).show();		
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String barcode = data.getStringExtra("SCAN_RESULT");
		switch (requestCode) {
		case REQUEST_CODE_CONFIRM_MEDICINE_BARCODE:
			if(barcode!=null){
				if(!barcode.isEmpty()){
					etMedicineBarcode.setText(barcode);
				}
			}else{
				Toast.makeText(getApplicationContext(), "扫描失败", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			setResult(RESULT_CANCELED);
			finish();
			overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
