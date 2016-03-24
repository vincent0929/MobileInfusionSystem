package com.android.patient;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.xs.tool.demo.somiao.R;
import com.android.constant.Constant;
import com.android.progressbar.RoundProgressBar;
import com.android.ser_exception.ThrowException;
import com.android.threadmethod.InterfaceThread;
import com.android.threadmethod.ThreadMethod;

public class PatientDealActivity extends Activity implements OnClickListener{
	private Handler handler;
	private Button btn_check;
	private Button	btn_ask;
	private InterfaceThread	inth;
	private ProgressDialog	pd;
	private Timer timer;
	private String askState;
	private	RoundProgressBar	roundProgressBar;//圆形进度条
	private ProgressBar	progressBar;//横向进度条
	private LinearLayout	ll_progress;
	private	int progress_c	=	0;
	private	int progress_h	=	0;

	private LinearLayout	ll_queue_patient;
	private TextView	tv_sumqueue_patient;
	private TextView	tv_welcome_patient;
	private TextView	tv_textqueue_patient;
	private TextView	tv_drug_message;

	private TextView	tv_name_patient;
	private TextView	tv_code_patient;
	private TextView	tv_seat_patient;
	private TextView	tv_phone_patient;
	private TextView	tv_sex_patient;
	private TextView	tv_idcard_patient;
	private TextView	tv_corp_patient;
	private TextView	tv_time_patient;
	private TextView	tv_address_patient;

	private LinearLayout	ll_nurse_start;
	private Button	btn_askNurse1;
	private Button	btn_askNurse2;
	private Button	btn_askNurse3;

	private TextView	tv_nurse_text;
	private TextView	tv_nurse_code;
	//动画红十字
	private ImageView	iv_red_ten;
	
	//输液时间
	private TextView	tv_time_shuye;
	private int count_shuye;//当前输液的瓶数
	private int all_count_shuye;//输液总瓶数
	private String[] time_shuye;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.patient_deal);
		init();
		addAnimation();
		initUserMessage();
		responseClick();
	}

	private void addAnimation() {
		@SuppressWarnings("static-access")
		Animation	animation	=	new	AnimationUtils().loadAnimation(this, R.anim.rotate_back);
		iv_red_ten	=	(ImageView) this.findViewById(R.id.iv_redten_patient);
		iv_red_ten.setAnimation(animation);

	}

	public void requestNuresNumber(final String patientCode,final String seatId){

		if(pd==null){
			pd	=	new	ProgressDialog(PatientDealActivity.this);
			pd.setCancelable(false);
			pd.setTitle(Constant.Message_logic_progressDialog_wait);
			pd.setMessage(Constant.Message_logic_progressDialog_load);
			pd.show();
		}	
		Thread thread	=	new Thread(new Runnable(){
			@Override
			public void run() {
				try{
					inth.requestNuresNumberFromSer(patientCode,seatId);

				}catch(ConnectTimeoutException e){
					e.printStackTrace();
					exceptionSend(Constant.Message_logic_fail_linkTimeOut);
				}
				catch(SocketTimeoutException e){
					e.printStackTrace();
					exceptionSend(Constant.Message_logic_fail_responseTimeOut);
				}
				catch(ThrowException e){
					e.printStackTrace();
					Message	message	=	new	Message();
					Bundle	bundle	=	new	Bundle();
					bundle.putSerializable("NurseCodeMessage", e.getMessage());
					Log.i("haha", e.getMessage());
					message.setData(bundle);
					handler.sendMessage(message);
				}
				catch(Exception e){
					e.printStackTrace();
					exceptionSend("请求失败");
				}finally{
					if(pd!=null){
						pd.dismiss();
						pd=null;
					}
				}
			}

			private void exceptionSend(String msg) {
				Message	message	=	new	Message();
				Bundle	bundle	=	new	Bundle();
				bundle.putSerializable("ErrorMessage", msg);
				message.setData(bundle);
				handler.sendMessage(message);
			}
		});
		thread.start();
	}
	/**
	 * 初始化用户信息
	 */
	private void initUserMessage() {
		//获取用户信息
		Bundle	bundle	=	PatientDealActivity.this.getIntent().getExtras();
		String userMessage	=	bundle.getString("userMessage");	

		try{
			JSONObject	jo	=	new	JSONObject(userMessage);
			tv_name_patient.setText(jo.getString("realname"));
			tv_idcard_patient.setText(jo.getString("idcard"));
			tv_sex_patient.setText(jo.getString("sex"));
			tv_phone_patient.setText(jo.getString("mobile"));
			tv_code_patient.setText(jo.getString("barcode"));
			tv_corp_patient.setText(jo.getString("corpname"));
			tv_seat_patient.setText(jo.getString("seatId"));
			tv_address_patient.setText(jo.getString("address"));
		}catch(Exception e){
			e.printStackTrace();
			Log.i("haha1", userMessage);
			Toast.makeText(PatientDealActivity.this, "数据处理出错，请重新登录", 1).show();        
			//			Toast.makeText(PatientDealActivity.this, userMessage+"数据处理出错，请重新登录", 1).show();
			finish();
		}

	}

	private void responseClick() {

		btn_askNurse1.setOnClickListener(this);
		btn_askNurse2.setOnClickListener(this);
		btn_askNurse3.setOnClickListener(this);

		btn_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if(tv_nurse_code!=null&&!tv_nurse_code.getText().toString().equals("")){
					if(tv_drug_message==null||tv_drug_message.getText().toString().equals("")){
						if(pd==null){
							pd	=	new	ProgressDialog(PatientDealActivity.this);
							pd.setCancelable(false);
							pd.setTitle(Constant.Message_logic_progressDialog_wait);
							pd.setMessage(Constant.Message_logic_progressDialog_load);
							pd.show();
						}	
						Thread thread	=	new Thread(new Runnable(){
							@Override
							public void run() {
								try{
									inth.checkMessage(Integer.parseInt(tv_code_patient.getText().toString().trim()));

								}catch(ConnectTimeoutException e){
									e.printStackTrace();
									exceptionSend(Constant.Message_logic_fail_linkTimeOut);
								}
								catch(SocketTimeoutException e){
									e.printStackTrace();
									exceptionSend(Constant.Message_logic_fail_responseTimeOut);
								}
								catch(ThrowException e){
									e.printStackTrace();
									Message	message	=	new	Message();
									Bundle	bundle	=	new	Bundle();
									bundle.putSerializable("ThrowCheckMessage", e.getMessage());
									Log.i("haha", e.getMessage());
									message.setData(bundle);
									handler.sendMessage(message);
								}
								catch(Exception e){
									e.printStackTrace();
									exceptionSend("查询失败");
								}finally{
									if(pd!=null){
										pd.dismiss();
										pd=null;
									}
								}
							}

							private void exceptionSend(String msg) {
								Message	message	=	new	Message();
								Bundle	bundle	=	new	Bundle();
								bundle.putSerializable("ErrorMessage", msg);
								message.setData(bundle);
								handler.sendMessage(message);
							}
						});
						thread.start();
					}else{
						tv_drug_message.setText("");
						btn_check.setText("查询数据");
					}
				}else{
					Toast.makeText(PatientDealActivity.this, "输液之前不能查询输液信息", 1).show();
				}
			}
		});

		btn_ask.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {


				//				if(Integer.parseInt(tv_sumqueue_patient.getText().toString().trim())<=0&&seatNumber==null){
				if(tv_sumqueue_patient.getVisibility()==View.GONE){
					final EditText et = new EditText(PatientDealActivity.this); 
					et.setInputType(InputType.TYPE_CLASS_NUMBER);
					new AlertDialog.Builder(PatientDealActivity.this).setTitle("请输入座位号")  
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(et)  
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
						@Override  
						public void onClick(DialogInterface arg0, int arg1) {  
							//数据获取  
							tv_seat_patient.setText(et.getText().toString());
							if(tv_seat_patient==null||tv_seat_patient.getText().toString().equals("")){
								Toast.makeText(PatientDealActivity.this, "座位号不能为空", 1).show();
							}
							else{
								requestNuresNumber(tv_code_patient.getText().toString().trim(),tv_seat_patient.getText().toString());}
						}  
					}).setNegativeButton("取消", null).show(); 
				}else{
					if(tv_sumqueue_patient.getText().toString().trim().equals("等待获取排队消息")){
						Toast.makeText(PatientDealActivity.this, "请检查网络或其他用户信息",  
								Toast.LENGTH_LONG).show(); 
					}else	if(Integer.parseInt(tv_sumqueue_patient.getText().toString().trim())>0) {
						Toast.makeText(PatientDealActivity.this, "请耐心等待排队结束",  
								Toast.LENGTH_LONG).show(); 
					} 
				}
			}
		});
	}
	@SuppressLint("HandlerLeak")
	private void init() {
		timer = new Timer();
		ll_progress	=	(LinearLayout) this.findViewById(R.id.ll_progressbar_horizontal);
		progressBar	=	(ProgressBar)findViewById(R.id.progressbar_h);//横向进度条初始化
		roundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);//圆形进度条初始化

		tv_name_patient	=	(TextView) this.findViewById(R.id.tv_name_patient);
		tv_code_patient	=	(TextView) this.findViewById(R.id.tv_code_patient);
		tv_seat_patient	=	(TextView) this.findViewById(R.id.tv_seat_patient);
		tv_phone_patient	=	(TextView) this.findViewById(R.id.tv_phone_patient);
		tv_idcard_patient	=	(TextView) this.findViewById(R.id.tv_idcard_patient);
		tv_sex_patient	=	(TextView) this.findViewById(R.id.tv_sex_patient);
		tv_corp_patient	=	(TextView) this.findViewById(R.id.tv_corp_patient);
		tv_time_patient	=	(TextView) this.findViewById(R.id.tv_time_patient);
		tv_address_patient	=	(TextView) this.findViewById(R.id.tv_address_patient);


		ll_queue_patient	=	(LinearLayout) this.findViewById(R.id.ll_queue_patient);
		tv_welcome_patient	=	(TextView) this.findViewById(R.id.tv_welcome_patient);
		tv_textqueue_patient	=	(TextView) this.findViewById(R.id.tv_queue_patient);
		tv_sumqueue_patient	=	(TextView) this.findViewById(R.id.tv_sumqueue_patient);
		tv_drug_message	=	(TextView) this.findViewById(R.id.tv_drug_message);

		ll_nurse_start	=	(LinearLayout) this.findViewById(R.id.ll_nurse_patient);
		btn_askNurse1	=	(Button) this.findViewById(R.id.btn_asknurse1_patient);
		btn_askNurse2	=	(Button) this.findViewById(R.id.btn_asknurse2_patient);
		btn_askNurse3	=	(Button) this.findViewById(R.id.btn_asknurse3_patient);


		tv_nurse_text	=	(TextView) this.findViewById(R.id.tv_text_nurse);
		tv_nurse_code	=	(TextView) this.findViewById(R.id.tv_nurse_code);

		btn_ask	=	(Button) this.findViewById(R.id.btn_ask);
		btn_check	=	(Button) this.findViewById(R.id.btn_check);
		
		tv_time_shuye	=	(TextView)this.findViewById(R.id.time_shuye);//输液时间
		inth	=	new	ThreadMethod();
		handler	=	new	Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				String QueueMessage	=	(String)msg.getData().getSerializable("QueueMessage");
				String NurseCodeMessage	=	(String)msg.getData().getSerializable("NurseCodeMessage");
				String errorMessage	=	(String) msg.getData().getSerializable("ErrorMessage");
				String ThrowCheckMessage	=	(String) msg.getData().getSerializable("ThrowCheckMessage");
				String ThrowAskMessage	=	(String) msg.getData().getSerializable("ThrowAskMessage");
				
				//获取护士条码号消息处理
				if(NurseCodeMessage!=null&&!NurseCodeMessage.equals("")){		
					if(!NurseCodeMessage.equals(Constant.Message_logic_fail_linkTimeOut)&&
							!NurseCodeMessage.equals(Constant.Message_logic_fail_responseTimeOut)&&
							!NurseCodeMessage.equals(Constant.Message_logic_fail_link)){
						//隐藏控件
						btn_ask.setVisibility(View.GONE);
						ll_queue_patient.setVisibility(View.GONE);
						//显示控件
						ll_nurse_start.setVisibility(View.VISIBLE);
						tv_nurse_text.setVisibility(View.VISIBLE);
						//护士信息
						tv_nurse_code.setVisibility(View.VISIBLE);
						tv_nurse_code.setText(NurseCodeMessage);
						Toast.makeText(PatientDealActivity.this, "护士编号："+NurseCodeMessage+"为您服务", 1).show();
					}else{
						Toast.makeText(PatientDealActivity.this, NurseCodeMessage, 1).show();
					}
				}else{
					//处理因服务器繁忙导致返回的空数据
					if(NurseCodeMessage!=null&&NurseCodeMessage.equals(""))
						Toast.makeText(PatientDealActivity.this, "分配护士出错,请稍候再试", 1).show();
				}


				//获取排队信息处理
				if(QueueMessage!=null&&!QueueMessage.equals("")){
					if(!QueueMessage.equals(Constant.Message_logic_fail_linkTimeOut)&&
							!QueueMessage.equals(Constant.Message_logic_fail_responseTimeOut)&&
							!QueueMessage.equals(Constant.Message_logic_fail_link)){
						if(Integer.parseInt(QueueMessage.trim())==0){
							tv_textqueue_patient.setTextColor(Color.RED);
							tv_textqueue_patient.getPaint().setFakeBoldText(true);
							tv_textqueue_patient.setText("排队结束，请呼叫护士前来输液!");
							timer.cancel();//排队结束后关闭定时器
							tv_sumqueue_patient.setVisibility(View.GONE);
						}else{
							tv_sumqueue_patient.setText(QueueMessage);
						}
					}else{
						Toast.makeText(PatientDealActivity.this,QueueMessage , 1).show();
					}
				}else{
					if(QueueMessage!=null&&QueueMessage.equals(""))
						Toast.makeText(PatientDealActivity.this, "获取排队号出错,请等待重连", 1).show();
				}

				//查询信息处理
				if(ThrowCheckMessage!=null&&!ThrowCheckMessage.equals("")){
					if(!ThrowCheckMessage.equals(Constant.Message_logic_fail_linkTimeOut)&&
							!ThrowCheckMessage.equals(Constant.Message_logic_fail_responseTimeOut)&&
							!ThrowCheckMessage.equals(Constant.Message_logic_fail_link)){
						try {
							//							Toast.makeText(PatientDealActivity.this, ThrowCheckMessage, 1).show();
							String drugName	=	ThrowCheckMessage.split("###")[0];
							String drugMessage	=	ThrowCheckMessage.split("###")[1];
							String timeMessage	=	ThrowCheckMessage.split("###")[2];
							time_shuye	=	timeMessage.split(",");
							time_shuye[0]	=	String.valueOf(System.currentTimeMillis()-1000*60*5);//测试测试
							JSONObject	jo	=	new	JSONObject(drugMessage);
							String currCount	=	jo.getString("currCount");
							String allCount	=	jo.getString("allCount");
							if(Integer.parseInt(currCount)>Integer.parseInt(allCount)){
								currCount	=	String.valueOf(Integer.parseInt(currCount)-1);
							}
							if(Integer.parseInt(currCount)<=Integer.parseInt(allCount)){
								tv_drug_message.setText("您输液的成分是:\n"+drugName+"\n"+"正在输液第"+currCount+"瓶"+"\n"+"一共有"+allCount+"瓶");
								count_shuye	=	Integer.parseInt(currCount);
								all_count_shuye	=	Integer.parseInt(allCount);
								//圆形进度条处理
								
								btn_check.setVisibility(View.GONE);
								roundProgressBar.setVisibility(View.VISIBLE);
								
								long needShuYeTime	=	(System.currentTimeMillis()-Long.parseLong(time_shuye[0]))/1000;
								Log.i("haha", needShuYeTime+"bb");
								roundProgressBar.setProgress((int)((needShuYeTime*100)/(Long.parseLong(time_shuye[count_shuye])*60)));
								//横向进度条处理
								ll_progress.setVisibility(View.VISIBLE);
								long shuYeTimeTotal	=	0;
								for (String shijian : time_shuye) {
									if(shijian.length()<4){
										Log.i("haha", Integer.parseInt(shijian)*60+"haha");
										shuYeTimeTotal+=Integer.parseInt(shijian)*60;
									}
								}
								tv_time_shuye.setText("预计完成时间:"+timeLongTodateTime(shuYeTimeTotal*1000+Long.parseLong(time_shuye[0])));//时间轴转换为time.输液时间显示
								progressBar.setProgress((int)((needShuYeTime*100)/shuYeTimeTotal));
								Log.i("haha", (int)(shuYeTimeTotal)+"aa"+time_shuye[0]);
							}else{
								tv_drug_message.setText("您输液的成分是:\n"+drugName+"\n"+"正在输液第"+allCount+"瓶"+"\n"+"一共有"+allCount+"瓶");
							}

//							btn_check.setText("隐藏数据");
//------------------------------------------------------------------------------------------------------------------------------------------------------------
						/*
							//进度条处理
							btn_check.setVisibility(View.GONE);
							roundProgressBar.setVisibility(View.VISIBLE);
							
							new Thread(new Runnable() {

								@Override
								public void run() {
									while(progress_c <= 100){
										progress_c += 3;
										roundProgressBar.setProgress(progress_c);

										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}

								}
							}).start();
							
							ll_progress.setVisibility(View.VISIBLE);
							new Thread(new Runnable() {

								@Override
								public void run() {
									while(progress_h <= 100){
										progress_h+=3;
										progressBar.setProgress(progress_h);
										try {
											Thread.sleep(100);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}
							}).start();
							*/
//------------------------------------------------------------------------------------------------------------------------------------------------------------						







							Toast.makeText(PatientDealActivity.this,"查询成功", 1).show();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						Toast.makeText(PatientDealActivity.this, ThrowCheckMessage, 1).show();
					}
				}else{
					if(ThrowCheckMessage!=null&&ThrowCheckMessage.equals(""))
						Toast.makeText(PatientDealActivity.this, "查询信息出错,请稍候再试", 1).show();
				}

				//呼叫信息处理
				//				if(ThrowAskMessage!=null)
				//				Toast.makeText(PatientDealActivity.this, ""+ThrowAskMessage.length(), 1).show();
				if(ThrowAskMessage!=null&&!ThrowAskMessage.equals("")){
					if(!ThrowAskMessage.equals(Constant.Message_logic_fail_linkTimeOut)&&
							!ThrowAskMessage.equals(Constant.Message_logic_fail_responseTimeOut)&&
							!ThrowAskMessage.equals(Constant.Message_logic_fail_link)){
						if(ThrowAskMessage.contains("Success")){
							Toast.makeText(PatientDealActivity.this, "呼叫"+askState+"成功", 1).show();
						}else{
							Toast.makeText(PatientDealActivity.this, "呼叫"+askState+"失败", 1).show();
						}
						//						if(ThrowAskMessage.length()==2){
						//							Toast.makeText(PatientDealActivity.this, "呼叫"+ThrowAskMessage+"成功", 1).show();
						//						}
					}else{
						Toast.makeText(PatientDealActivity.this, ThrowAskMessage, 1).show();
					}
				}else{
					if(ThrowAskMessage!=null&&ThrowAskMessage.equals(""))
						Toast.makeText(PatientDealActivity.this, "呼叫异常", 1).show();
				}
				//异常信息显示
				if(errorMessage!=null&&!errorMessage.equals("")){
					Toast.makeText(PatientDealActivity.this, errorMessage, 1).show();
				}
			}

			private String timeLongTodateTime(long date) {
				SimpleDateFormat	dateFormat	=	new	SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String time	=	dateFormat.format(new Date(date));
				return time;
			}			
		};
		TimerTask task = new TimerTask() { 
			public void run() {
				try{
					inth.requestQueue(tv_code_patient.getText().toString().trim());
				}catch(ConnectTimeoutException e){
					e.printStackTrace();
				}
				catch(SocketTimeoutException e){
					e.printStackTrace();
				}
				catch(ThrowException e){
					e.printStackTrace();
					Message	message	=	new	Message();
					Bundle	bundle	=	new	Bundle();
					bundle.putSerializable("QueueMessage", e.getMessage());
					Log.i("haha", e.getMessage());
					message.setData(bundle);
					handler.sendMessage(message);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		};

		timer.schedule(task, 2000, 2000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_asknurse1_patient:
			//					Toast.makeText(PatientDealActivity.this, "换药", 1).show();
			askState	=	"换药";
			askNurseForSomeThing(1);
			break;//换药状态位
		case R.id.btn_asknurse2_patient:
			//					Toast.makeText(PatientDealActivity.this, "特殊", 1).show();
			askState	=	"特殊";
			askNurseForSomeThing(2);
			break;//特殊状态位
		case R.id.btn_asknurse3_patient:
			//					Toast.makeText(PatientDealActivity.this, "拔针", 1).show();
			askState	=	"拔针";
			askNurseForSomeThing(3);
			break;//拔针状态位
		default:
			break;
		}

	}

	private void askNurseForSomeThing(final int state) {
		//		Toast.makeText(PatientDealActivity.this, tv_code_patient.getText().toString().trim()+":"+tv_seat_patient.getText().toString().trim()+":"+state, 1).show();
		if(tv_nurse_code!=null&&!tv_nurse_code.getText().toString().equals("")){ 
			if(pd==null){
				pd	=	new	ProgressDialog(PatientDealActivity.this);
				pd.setCancelable(false);
				pd.setTitle(Constant.Message_logic_progressDialog_wait);
				pd.setMessage(Constant.Message_logic_progressDialog_load);
				pd.show();
			}	
			Thread thread	=	new Thread(new Runnable(){
				@Override
				public void run() {
					try{
						inth.ask(
								tv_seat_patient.getText().toString().trim(),
								tv_name_patient.getText().toString().trim(),
								tv_nurse_code.getText().toString().trim(),
								state+"");

					}catch(ConnectTimeoutException e){
						e.printStackTrace();
						exceptionSend(Constant.Message_logic_fail_linkTimeOut);
					}
					catch(SocketTimeoutException e){
						e.printStackTrace();
						exceptionSend(Constant.Message_logic_fail_responseTimeOut);
					}
					catch(ThrowException e){
						e.printStackTrace();
						Message	message	=	new	Message();
						Bundle	bundle	=	new	Bundle();
						bundle.putSerializable("ThrowAskMessage", e.getMessage());
						Log.i("ask", e.getMessage());
						message.setData(bundle);
						handler.sendMessage(message);
					}
					catch(Exception e){
						e.printStackTrace();
						exceptionSend("呼叫失败");
					}finally{
						if(pd!=null){
							pd.dismiss();
							pd=null;
						}
					}
				}

				private void exceptionSend(String msg) {
					Message	message	=	new	Message();
					Bundle	bundle	=	new	Bundle();
					bundle.putSerializable("ErrorMessage", msg);
					message.setData(bundle);
					handler.sendMessage(message);
				}
			});
			thread.start();
		}
	}
}
