package com.vincent.nurseclient.aty;

import java.util.ArrayList;

import com.vincent.nurseclient.InfusionPatientDataAdapter;
import com.vincent.nurseclient.R;
import com.vincent.nurseclient.net.GetInfusioningData;
import com.vincent.nurseclient.utils.Config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AtyPatientInfusioning extends Activity {

	private ListView lvInfusionPatientsData;

	private ArrayList<String> infusionPatientsCode = new ArrayList<String>();
	private ArrayList<String> infusionPatientsSeatId = new ArrayList<String>();
	private ArrayList<String> infusionPatientsDrugCode = new ArrayList<String>();
	private InfusionPatientDataAdapter infusionAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_patient_infusioning);

		initView();
	}

	@Override
	protected void onResume() {
		flushInfusioningPatientData(Config.getNurseId());
		super.onResume();
	}

	private void flushInfusioningPatientData(String nurseId) {

		infusionPatientsCode.clear();
		infusionPatientsDrugCode.clear();
		infusionPatientsSeatId.clear();

		new GetInfusioningData(Config.getNurseId(), new GetInfusioningData.SuccessCallback() {

			@Override
			public void onSuccess(String patientCode, String seatId, String patientIdInQueue, String drugCode) {
				infusionPatientsCode.add(patientCode);
				infusionPatientsDrugCode.add(drugCode);
				infusionPatientsSeatId.add(seatId);
				System.out.println("------------------------");
				System.out.println("infusionPatientsCode：" + infusionPatientsCode);
				System.out.println("infusionPatientsDrugCode：" + infusionPatientsDrugCode);
				System.out.println("infusionPatientsSeatId：" + infusionPatientsSeatId);
				System.out.println("+++++++++++++++");
			}
		}, new GetInfusioningData.FailCallback() {

			@Override
			public void onFail() {

			}
		});
	}

	private void initView() {
		lvInfusionPatientsData = (ListView) findViewById(R.id.lvInfusionPatientsData);
		infusionAdapter = new InfusionPatientDataAdapter(AtyPatientInfusioning.this, infusionPatientsCode, infusionPatientsSeatId,
				infusionPatientsDrugCode);
		lvInfusionPatientsData.setAdapter(infusionAdapter);
		lvInfusionPatientsData.setOnItemClickListener(itemListener);
	}

	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switch (parent.getId()) {
			case R.id.lvInfusionPatientsData:
				if (position != 0) {
					Intent intent = new Intent(AtyPatientInfusioning.this, AtyChangeMedicine.class);
					intent.putExtra(Config.KEY_PATIENT_CODE, infusionPatientsCode);
					intent.putExtra(Config.KEY_SEAT_ID, infusionPatientsSeatId);
					startActivityForResult(intent, Config.REQUEST_CODE_CHANGE_MEDICAMENT);
					overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
				}
				break;

			default:
				break;
			}
		}
	};
}
