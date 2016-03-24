package com.vincent.nurseclient.aty;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.vincent.nurseclient.FragmentAdapter;
import com.vincent.nurseclient.InfusionPatientDataAdapter;
import com.vincent.nurseclient.R;
import com.vincent.nurseclient.WaitingPatientDataAdapter;
import com.vincent.nurseclient.net.GetInfusioningData;
import com.vincent.nurseclient.net.GetWaitingInfusionData;
import com.vincent.nurseclient.push.ExampleUtil;
import com.vincent.nurseclient.utils.Config;

public final class AtyMainFrame extends FragmentActivity {

	public static final String MESSAGE_RECEIVED_ACTION = "com.vincent.nurseclient.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	public static final String TAG = "JPush";

	protected static final int MSG_SET_ALIAS = 1001;
	protected static final int MSG_SET_TAGS = 1002;

	public static final int MSG_JPUSH_DATA = 1;

	private Button btnWaitingInfusion, btnInfusioning;
	private TextView tvNurseId, tvJPushDataMessage, tvJPushDataExtra,tvBtnFlushData;
	private ListView lvWaitingPatientsData, lvInfusionPatientsData;

	private String nurseId = "";

	private ArrayList<String> waitingPatientsCode = new ArrayList<String>();
	private ArrayList<String> waitingPatientsDrugCode = new ArrayList<String>();
	private ArrayList<String> waitingPatientsSeatId = new ArrayList<String>();
	private ArrayList<String> waitingPatientsIdInQueue = new ArrayList<String>();
	private WaitingPatientDataAdapter waitingAdapter;

	private ArrayList<String> infusionPatientsCode = new ArrayList<String>();
	private ArrayList<String> infusionPatientsSeatId = new ArrayList<String>();
	private ArrayList<String> infusionPatientsDrugCode = new ArrayList<String>();
	private InfusionPatientDataAdapter infusionAdapter;

	public static boolean isForeground = false;

	private MessageReceiver mMessageReceiver;
	
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		nurseId = getIntent().getStringExtra(Config.KEY_NURSE_ID);
		System.out.println("nurseId:" + nurseId);
		setAlias(nurseId);
		
		initView();
		initJPush();
		registerMessageReceiver();
		
		flushInfusionData(nurseId);
	}

	private void initView() {
		
		tvBtnFlushData=(TextView) findViewById(R.id.tvBtnFlushData);
		tvBtnFlushData.setOnClickListener(listener);
		
		dialog=new ProgressDialog(AtyMainFrame.this);
		dialog.setMessage("刷新......");

		lvWaitingPatientsData = (ListView) findViewById(R.id.lvWaitingPatientsData);
		waitingAdapter = new WaitingPatientDataAdapter(AtyMainFrame.this, waitingPatientsCode, 
				waitingPatientsDrugCode,waitingPatientsSeatId, waitingPatientsIdInQueue);
		lvWaitingPatientsData.setAdapter(waitingAdapter);
		lvWaitingPatientsData.setOnItemClickListener(itemListener);

		lvInfusionPatientsData = (ListView) findViewById(R.id.lvInfusionPatientsData);
		infusionAdapter = new InfusionPatientDataAdapter(AtyMainFrame.this, infusionPatientsCode, infusionPatientsSeatId, infusionPatientsDrugCode);
		lvInfusionPatientsData.setAdapter(infusionAdapter);
		lvInfusionPatientsData.setOnItemClickListener(itemListener);

	}

	private void initJPush() {
		JPushInterface.init(getApplicationContext());
		JPushInterface.setDebugMode(true);
	}

	@Override
	protected void onResume() {
		isForeground = true;

		super.onResume();
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	private OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.btnWaitingInfusion:
				break;
			case R.id.btnInfusioning:
				break;
			case R.id.tvBtnFlushData:
				dialog.show();
				flushInfusionData(nurseId);
				break;
			default:
				break;
			}
		}
	};

	private OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			switch (parent.getId()) {
			case R.id.lvWaitingPatientsData:
				if (position != 0) {
					if (!waitingPatientsCode.get(position - 1).equals("无人等待")) {
						Intent intent = new Intent(AtyMainFrame.this, AtyConfirmPatient.class);
						intent.putExtra(Config.KEY_PATIENT_CODE, waitingPatientsCode.get(position - 1));
						startActivityForResult(intent, Config.REQUEST_CODE_CONFIRM_PATIENT);
						overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
					}
				}
				break;
			case R.id.lvInfusionPatientsData:
				if (position != 0) {
					if (!infusionPatientsCode.get(position - 1).equals("无人输液")) {
						Intent intent = new Intent(AtyMainFrame.this, AtyChangeMedicine.class);
						intent.putExtra(Config.KEY_PATIENT_CODE, infusionPatientsCode);
						intent.putExtra(Config.KEY_SEAT_ID, infusionPatientsSeatId);
						startActivityForResult(intent, Config.REQUEST_CODE_CHANGE_MEDICAMENT);
						overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
					}
				}
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case Config.REQUEST_CODE_CONFIRM_PATIENT:
			flushInfusionData(nurseId);
			break;
		case Config.REQUEST_CODE_CHANGE_MEDICAMENT:
			flushInfusionData(nurseId);
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void flushInfusionData(String nurseId) {

		waitingPatientsCode.clear();
		waitingPatientsDrugCode.clear();
		waitingPatientsSeatId.clear();
		waitingPatientsIdInQueue.clear();
		waitingAdapter.notifyDataSetChanged();

		infusionPatientsCode.clear();
		infusionPatientsDrugCode.clear();
		infusionPatientsSeatId.clear();
		infusionAdapter.notifyDataSetChanged();

		new GetWaitingInfusionData(nurseId, new GetWaitingInfusionData.SuccessCallback() {

			@Override
			public void onSuccess(String patientCode,String drugCode, String patientIdInQueue,String seatId) {

				waitingPatientsCode.add(patientCode);
				waitingPatientsDrugCode.add(drugCode);
				waitingPatientsSeatId.add(seatId);
				waitingPatientsIdInQueue.add(patientIdInQueue);
				System.out.println("------------------------");
				System.out.println("waitingPatientsCode：" + waitingPatientsCode);
				System.out.println("waitingPatientsDrugCode：" + waitingPatientsDrugCode);
				System.out.println("waitingPatientsSeatId：" + waitingPatientsSeatId);
				System.out.println("waitingPatientsIdInQueue：" + waitingPatientsIdInQueue);
				System.out.println("+++++++++++++++");
				waitingAdapter.notifyDataSetChanged();
				
				if(dialog.isShowing()){
					dialog.dismiss();
				}
			}
		}, new GetWaitingInfusionData.FailCallback() {

			@Override
			public void onFail() {
				Toast.makeText(getApplicationContext(), "连接服务器失败，请检查网络", Toast.LENGTH_SHORT).show();
				
				if(dialog.isShowing()){
					dialog.dismiss();
				}
			}
		});

		new GetInfusioningData(nurseId, new GetInfusioningData.SuccessCallback() {

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
				infusionAdapter.notifyDataSetChanged();
				
				if(dialog.isShowing()){
					dialog.dismiss();
				}
			}
		}, new GetInfusioningData.FailCallback() {

			@Override
			public void onFail() {
				if(dialog.isShowing()){
					dialog.dismiss();
				}
			}
		});
	}

	private void setJPush(String alias) {
		setAlias(alias);
		setStyleBasic();
		setStyleCustom();
	}

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	/*
	 * 设置Alias
	 */
	private void setAlias(String alias) {
		if (TextUtils.isEmpty(alias)) {
			Toast.makeText(getApplicationContext(), R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!ExampleUtil.isValidTagAndAlias(alias)) {
			Toast.makeText(getApplicationContext(), R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
			return;
		}

		// 调用JPush API设置Alias
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}

	/**
	 * 设置通知提示方式 - 基础属性
	 */
	private void setStyleBasic() {
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(AtyMainFrame.this);
		builder.statusBarDrawable = R.drawable.ic_launcher;
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL; // 设置为点击后自动消失
		builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE; // 设置为铃声（
																									// Notification.DEFAULT_SOUND）或者震动（
																									// Notification.DEFAULT_VIBRATE）
		JPushInterface.setPushNotificationBuilder(1, builder);
		Toast.makeText(getApplicationContext(), "Basic Builder - 1", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 设置通知栏样式 - 定义通知栏Layout
	 */
	private void setStyleCustom() {
		CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(AtyMainFrame.this, R.layout.customer_notitfication_layout,
				R.id.icon, R.id.title, R.id.text);
		builder.layoutIconDrawable = R.drawable.ic_launcher;
		builder.developerArg0 = "developerArg2";
		JPushInterface.setPushNotificationBuilder(2, builder);
		Toast.makeText(getApplicationContext(), "Custom Builder - 2", Toast.LENGTH_SHORT).show();
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				Log.i(TAG, logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Log.i(TAG, logs);
				if (ExampleUtil.isConnected(getApplicationContext())) {
					mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
				} else {
					Log.i(TAG, "No network");
				}
				break;

			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, logs);
			}

//			ExampleUtil.showToast(logs, getApplicationContext());
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			AlertDialog.Builder builder = new AlertDialog.Builder(AtyMainFrame.this).setTitle("你要退出吗？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.show();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setCostomMsg(String msg) {

	}

	private void sendPushDataToMianActivity(String messge, String extras) {
		Message message = new Message();
		Bundle bundle = new Bundle();
		bundle.putString(AtyMainFrame.KEY_MESSAGE, messge);
		bundle.putString(AtyMainFrame.KEY_EXTRAS, extras);
		message.setData(bundle);
		message.what = MSG_JPUSH_DATA;
		mHandler.sendMessage(message);
	}

	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				setCostomMsg(showMsg.toString());

				sendPushDataToMianActivity(messge, extras);

			}
		}
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				Log.d(TAG, "Set alias in handler.");
				JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
				break;
			case MSG_JPUSH_DATA:
				String jpushDataMessage = msg.getData().getString(AtyMainFrame.KEY_MESSAGE, "null");
				String jpushDataExtra = msg.getData().getString(AtyMainFrame.KEY_EXTRAS, "null");
				tvJPushDataMessage.setText(jpushDataMessage);
				tvJPushDataExtra.setText(jpushDataExtra);
				break;
			default:
				Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};
}
