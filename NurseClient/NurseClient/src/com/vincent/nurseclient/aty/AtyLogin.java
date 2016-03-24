package com.vincent.nurseclient.aty;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vincent.nurseclient.R;
import com.vincent.nurseclient.net.Login;
import com.vincent.nurseclient.utils.Config;

public class AtyLogin extends Activity {

	private TextView tvBtnLogin;
	private EditText etAccountName, etAccountPassword;

	private HttpClient client;

	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_login);

		initView();
		client = new DefaultHttpClient();
	}

	private void initView() {
		etAccountName = (EditText) findViewById(R.id.etAccountName);
		etAccountPassword = (EditText) findViewById(R.id.etAccountPassword);
		tvBtnLogin = (TextView) findViewById(R.id.tvBtnLogin);
		tvBtnLogin.setOnClickListener(listener);
		dialog = new ProgressDialog(AtyLogin.this);
		dialog.setMessage("登录中......");
	}

	private OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tvBtnLogin:
				if (TextUtils.isEmpty(etAccountName.getText().toString())) {
					Toast.makeText(AtyLogin.this, "用户名不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				if (TextUtils.isEmpty(etAccountPassword.getText().toString())) {
					Toast.makeText(AtyLogin.this, "密码不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				dialog.show();
				login(etAccountName.getText().toString(), etAccountPassword.getText().toString());
				break;
			default:
				break;
			}
		}
	};

	/*
	 * 护士客户端账号登陆
	 */
	private void login(String username, String password) {

		new Login(username, password, new Login.SuccessCallback() {

			@Override
			public void onSuccess(String nurseId) {

				if (dialog.isShowing()) {
					dialog.dismiss();
				}

				Intent intent = new Intent(AtyLogin.this, AtyMainFrame.class);
				intent.putExtra(Config.KEY_NURSE_ID, nurseId);
				finish();
				startActivity(intent);
				overridePendingTransition(R.anim.push_center, R.anim.push_bottom_out);
			}
		}, new Login.FailCallback() {

			@Override
			public void onFail() {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				Toast.makeText(getApplicationContext(), "登录失败，请重新登录", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			AlertDialog.Builder builder = new AlertDialog.Builder(AtyLogin.this).setTitle("你要退出吗？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							setResult(RESULT_CANCELED);
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
}
