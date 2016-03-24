package com.vincent.nurseclient;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfusionPatientDataAdapter extends BaseAdapter {

	private ArrayList<String> infusionPatientsBarcode;
	private ArrayList<String> infusionPatientsSeatId;
	private ArrayList<String> infusionPatientsDrugCode;
	
	private LayoutInflater inflater;
	
	
	public InfusionPatientDataAdapter(Context context,ArrayList<String> patientsBarcode,
			ArrayList<String> patientsSeatId,ArrayList<String> patientDrugCode){
		this.infusionPatientsBarcode=patientsBarcode;
		this.infusionPatientsSeatId=patientsSeatId;
		this.infusionPatientsDrugCode=patientDrugCode;
		
		inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return infusionPatientsBarcode.size()+1;
	}

	@Override
	public String getItem(int position) {
		return infusionPatientsBarcode.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout ll;
		ll=(LinearLayout) inflater.inflate(R.layout.list_item_infusion_patient_data, null);
		TextView tvInfusionPatientName=(TextView) ll.findViewById(R.id.tvInfusionPatientName);
		TextView tvInfusionPatientSeatId=(TextView) ll.findViewById(R.id.tvInfusionPatientSeatId);
		TextView tvInfusionPatientDrugCode=(TextView) ll.findViewById(R.id.tvInfusionPatientDrugCode);
		
		if(position!=0){
			tvInfusionPatientName.setText(infusionPatientsBarcode.get(position-1));
			tvInfusionPatientSeatId.setText(infusionPatientsSeatId.get(position-1));
			tvInfusionPatientDrugCode.setText(infusionPatientsDrugCode.get(position-1));
		}
		else{
			ll.setBackgroundColor(Color.parseColor("#999999"));			
		}
		
		return ll;
	}

}
