package com.vincent.nurseclient;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WaitingPatientDataAdapter extends BaseAdapter {

	private ArrayList<String> patientsCode;
	private ArrayList<String> drugsCode;
	private ArrayList<String> patientsSeatId;
	private ArrayList<String> patientsIdInQueue;
	
	private LayoutInflater inflater;
		
	public WaitingPatientDataAdapter(Context context,ArrayList<String> patientsCode,
			ArrayList<String> drugsCode,ArrayList<String> patientsSeatId,
			ArrayList<String> patientsIdInQueue){
		this.patientsCode=patientsCode;
		this.drugsCode=drugsCode;
		this.patientsSeatId=patientsSeatId;
		this.patientsIdInQueue=patientsIdInQueue;
		
		inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return patientsCode.size()+1;
	}

	@Override
	public String getItem(int position) {
		return patientsCode.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout ll;
		ll=(LinearLayout) inflater.inflate(R.layout.list_item_waiting_patient_data, null);
		TextView tvPatientName=(TextView) ll.findViewById(R.id.tvPatientName);
		TextView tvDrugCode=(TextView) ll.findViewById(R.id.tvDrugCode);
		TextView tvPatientSeatId=(TextView) ll.findViewById(R.id.tvPatientSeatId);
		TextView tvPatientIdInQueue=(TextView) ll.findViewById(R.id.tvPatientIdInQueue);
		
		if(position!=0){
			tvPatientName.setText(patientsCode.get(position-1));
			tvDrugCode.setText(drugsCode.get(position-1));
			tvPatientSeatId.setText(patientsSeatId.get(position-1));
			tvPatientIdInQueue.setText(patientsIdInQueue.get(position-1));
		}else{
			ll.setBackgroundColor(Color.parseColor("#999999"));			
		}
		
		return ll;
	}

}
