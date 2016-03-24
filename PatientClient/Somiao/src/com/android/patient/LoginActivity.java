package com.android.patient;

import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.xs.tool.demo.somiao.R;
import cn.xs.tool.demo.somiao.scan.CaptureActivity;

import com.android.constant.Constant;
import com.android.ser_exception.ThrowException;
import com.android.threadmethod.InterfaceThread;
import com.android.threadmethod.ThreadMethod;

public class LoginActivity extends Activity{

	private Button btn_scan;
	private Button btn_logic;
	private EditText et_scan;
	private ProgressDialog	pd;
	private Handler	handler;
	private InterfaceThread	inth;
	
	//旋转动画
	private ImageView	iv_animation_rotate;


	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		inth	=	new	ThreadMethod();
		btn_scan = (Button) findViewById(R.id.btn_scan);
		btn_logic = (Button) findViewById(R.id.btn_logic);
		et_scan = (EditText) findViewById(R.id.et_scan);
		
		@SuppressWarnings("static-access")
		Animation	animation	=	new	AnimationUtils().loadAnimation(this, R.anim.rotate_noback);
		iv_animation_rotate	=	(ImageView) this.findViewById(R.id.iv_animation_rotate);
		iv_animation_rotate.setAnimation(animation);
		
		handler	=	new	Handler(){

			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				String errorMessage	=	(String) msg.getData().getSerializable("ErrorMessage");

				//如果抛出的信息里面包含realname,则说明请求信息成功
				if(errorMessage!=null&&errorMessage.contains("realname")){
					Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
					Intent	intent	=	new	Intent(LoginActivity.this,PatientDealActivity.class);
					Bundle	bundle	=	new	Bundle();
					bundle.putString("userMessage", errorMessage);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}else{
					Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
				}
			}		
		};

		btn_logic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				requestLogic();
			}
		});
		btn_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_scan.setText("");
				Intent intent = new Intent(LoginActivity.this,
						CaptureActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	protected void requestLogic() {



		if(pd==null){
			pd	=	new	ProgressDialog(LoginActivity.this);
			pd.setCancelable(false);
			pd.setTitle(Constant.Message_logic_progressDialog_wait);
			pd.setMessage(Constant.Message_logic_progressDialog_load);
			pd.show();
		}				
		Thread thread	=	new	Thread(new Runnable() {

			@Override
			public void run() {
				String codeNumber	=	et_scan.getText().toString().trim();
				try{
					if(codeNumber==null||codeNumber.equals(""))
						throw new ThrowException(Constant.Message_logic_codeAndseat_NoNull);
					else
						inth.logicGet(codeNumber);

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
					exceptionSend(e.getMessage());
				}
				catch(Exception e){
					e.printStackTrace();
					exceptionSend(Constant.Message_logic_fail_logic);
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
		try {
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		thread.start();



	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		String barcode = data.getStringExtra("SCAN_RESULT");

		if ("".equals(barcode) || barcode == null) {

		} else {

			et_scan.setText(barcode);

		}
	}
}
