package com.vincent.nurseclient.aty;

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
import com.vincent.nurseclient.net.ChangeMedicine;
import com.vincent.nurseclient.scan.CaptureActivity;

public class AtyChangeMedicine extends Activity {
	
	protected static final int CHANGE_MEDICINE_REQUEST_CODE = 0;
	private Button btnAtyChangeMedicineBackToMainActivity;
	private EditText etDrugCode;
	private TextView tvBtnScanMedicineBarcode,tvBtnChangeDrug;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_change_medicine);
		
		initView();
	}

	private void initView() {
		btnAtyChangeMedicineBackToMainActivity=(Button) findViewById(
				R.id.btnAtyChangeMedicineBackToMainActivity);
		btnAtyChangeMedicineBackToMainActivity.setOnClickListener(listener);
		tvBtnChangeDrug=(TextView) findViewById(R.id.tvBtnChangeDrug);
		tvBtnChangeDrug.setOnClickListener(listener);
		etDrugCode=(EditText) findViewById(R.id.etDrugCode);
		tvBtnScanMedicineBarcode=(TextView) findViewById(R.id.tvBtnScanDrugCode);
		tvBtnScanMedicineBarcode.setOnClickListener(listener);
	}
	
	private OnClickListener listener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnAtyChangeMedicineBackToMainActivity:
				ExchangeAtyFromRightToLeft();
				break;
			case R.id.tvBtnScanDrugCode:
				etDrugCode.setText("");
				Intent intent=new Intent(AtyChangeMedicine.this, CaptureActivity.class);
				startActivityForResult(intent, CHANGE_MEDICINE_REQUEST_CODE);
				overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
				break;
			case R.id.tvBtnChangeDrug:
				String drugCode=etDrugCode.getText().toString();
				if(!drugCode.isEmpty()){
					postMedicineBarcode(drugCode);
					setResult(RESULT_OK);
					finish();
					overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
				}
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String medicineBarcode = data.getStringExtra("SCAN_RESULT");
		if(medicineBarcode!=null){
			if(!medicineBarcode.isEmpty()){
				etDrugCode.setText(medicineBarcode);
				postMedicineBarcode(medicineBarcode);
				ExchangeAtyFromRightToLeft();
			}
		}else{
			Toast.makeText(getApplicationContext(), "扫描失败", Toast.LENGTH_LONG).show();
		}
	}

	private void postMedicineBarcode(String medicineBarcode) {
		new ChangeMedicine(medicineBarcode, new ChangeMedicine.SuccessCallback() {
			@Override
			public void onSuccess(String currCount, String allCount) {
				if(currCount==allCount){
					Toast.makeText(getApplicationContext(), "输液完毕", Toast.LENGTH_LONG).show();
				}else{
					if(Integer.valueOf(currCount)<=Integer.valueOf(allCount)){
						Toast.makeText(getApplicationContext(), "换药信息发送成功\n总共" + allCount + "瓶药，正在输第" + currCount + "瓶药", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(getApplicationContext(), "患者输液完毕" , Toast.LENGTH_LONG).show();
						
					}
				}
				ExchangeAtyFromRightToLeft();
			}
		}, new ChangeMedicine.FailCallback() {
			
			@Override
			public void onFail() {
				Toast.makeText(getApplicationContext(), "换药信息发送失败", Toast.LENGTH_LONG).show();
			}
		});
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
	
	private void ExchangeAtyFromRightToLeft(){
		finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}
}
