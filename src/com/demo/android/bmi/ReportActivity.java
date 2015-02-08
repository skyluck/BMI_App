package com.demo.android.bmi;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	
	private void initViews () {
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
		show_result.setText(getString(R.string.bmi_height) + nf.format(BMI));
		
		//Give health advice
		if (BMI > 25) {
			show_suggest.setText(R.string.advice_heavy);
		} else if (BMI < 20) {
			show_suggest.setText(R.string.advice_light);
		} else {
			show_suggest.setText(R.string.advice_average);
		}
	}
	
	//Listen for button clicks
	private void setListensers () {
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
}
