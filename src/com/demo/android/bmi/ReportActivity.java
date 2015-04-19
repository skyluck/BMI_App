package com.demo.android.bmi;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReportActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		initViews();
		showResults();
		setListensers();
	}
	
	private Button button_back;
	private TextView show_result;
	private TextView show_suggest;
	private double BMI;
	private static final String TAG = "Main";
	
	private void initViews () {
		Log.d(TAG, "init Views");
		button_back = (Button) findViewById(R.id.report_back);
		show_result = (TextView) findViewById(R.id.result);
		show_suggest = (TextView) findViewById(R.id.suggest);
	}
	
	private void showResults () {
		DecimalFormat nf = new DecimalFormat("0.00");
		
		Bundle bundle = this.getIntent().getExtras();
		double height = Double.parseDouble(bundle.getString("KEY_HEIGHT"))/100;
		double weight = Double.parseDouble(bundle.getString("KEY_WEIGHT"));
		BMI = weight / (height*height);
		show_result.setText(getString(R.string.report_title) + nf.format(BMI));
		
		//Give health advice
		if (BMI > 25) {
			show_suggest.setText(R.string.advice_heavy);
			showNotification(BMI);
		} else if (BMI < 20) {
			show_suggest.setText(R.string.advice_light);
		} else {
			show_suggest.setText(R.string.advice_average);
		}
	}
	
	//Listen for button clicks
	private void setListensers () {
		Log.d(TAG, "set Listensers");
		button_back.setOnClickListener(backtoMain);
	}
	
	private Button.OnClickListener backtoMain = new Button.OnClickListener () {
		public void onClick (View v) {
			// Close this Activity
			DecimalFormat nf = new DecimalFormat("0.00");
			
			Bundle bundle = new Bundle();
			bundle.putString("BMI", nf.format(BMI));
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			ReportActivity.this.finish();
		}
	};
	
	protected void showNotification (double BMI) {
		NotificationManager barManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
//		Notification barMsg = new Notification(
//			R.drawable.ic_launcher,
//			"歐歐，你過重囉!",
//			System.currentTimeMillis()
//		);
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this , MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
		
//		barMsg.setLatestEventInfo(ReportActivity.this, "您的BMI值過高", "通知監督人", contentIntent);
		
		Notification.Builder barMsg = new Notification.Builder(this)
			.setTicker("歐歐，你過重囉!")
			.setContentTitle("您的BMI值過高")
			.setContentText("通知監督人")
			.setSmallIcon(android.R.drawable.stat_sys_warning)
			.setContentIntent(contentIntent);
		
		
		barManager.notify(0,barMsg.build());
	}
}
