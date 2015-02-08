package com.demo.android.bmi;

import java.text.DecimalFormat;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        setListensers();
    }
    
    private Button button_calc;
    private EditText num_height;
    private EditText num_weight;
    private TextView show_result;
    private TextView show_suggest;
    private static final int ACTIVITY_REPORT = 1000;
    
    private void initViews() {
    	button_calc = (Button) findViewById(R.id.submit);
    	num_height = (EditText) findViewById(R.id.height);
    	num_weight = (EditText) findViewById(R.id.weight);
    	show_result = (TextView) findViewById(R.id.result);
    	show_suggest = (TextView) findViewById(R.id.suggest);
    }
    
    //Listen for button clicks
    private void setListensers() {
    	button_calc.setOnClickListener(calcBMI);
    }
    
    private Button.OnClickListener calcBMI = new OnClickListener () {
    	public void onClick(View v) {
//    		DecimalFormat nf = new DecimalFormat("0.00");
//    		try {
//    			double height = Double.parseDouble(num_height.getText().toString())/100;
//        		double weight = Double.parseDouble(num_weight.getText().toString());
//        		double BMI = weight / (height*height);
//        		
//        		//Present result
//        		show_result.setText(getText(R.string.bmi_result) + nf.format(BMI));
//        		
//        		//Give health advice
//        		if (BMI > 25) {
//        			show_suggest.setText(R.string.advice_heavy);
//        		} else if (BMI < 20) {
//        			show_suggest.setText(R.string.advice_light);
//        		} else {
//        			show_suggest.setText(R.string.advice_average);
//        		}
//    			
//    		} catch (Exception obj) {
//    			Toast popup = Toast.makeText(MainActivity.this, R.string.input_error, Toast.LENGTH_SHORT);
//    	    	popup.show();
//    		}
    		
    		//Switch to report page
    		Intent intent = new Intent();
    		intent.setClass(MainActivity.this, ReportActivity.class);
    		Bundle bundle = new Bundle();
    		bundle.putString("KEY_HEIGHT", num_height.getText().toString());
    		bundle.putString("KEY_WEIGHT", num_weight.getText().toString());
    		intent.putExtras(bundle);
    		//startActivity(intent);
    		startActivityForResult(intent, ACTIVITY_REPORT);
     	}
    };
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	if (resultCode == RESULT_OK) {
    		if (requestCode == ACTIVITY_REPORT) {
    			Bundle bundle = intent.getExtras();
    			String bmi = bundle.getString("BMI");
    			show_suggest.setText(getString(R.string.advice_history) + bmi);
    			num_weight.setText(R.string.input_empty);
    			num_weight.requestFocus();
    		}
    	}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
        case R.id.action_about:
        	openOptionsDialog();
        	break;
        case R.id.action_close:
        	finish();
        	break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void openOptionsDialog() {
    	AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
    	dialog.setTitle("關於Android BMI");
    	dialog.setMessage("Android BMI Calc");
    	dialog.setPositiveButton(android.R.string.ok, 
    			new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
    	dialog.setNegativeButton(R.string.homepage_label, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//open browser
				Uri uri = Uri.parse(getString(R.string.homepage_uir));
				Intent intent = new Intent(Intent.ACTION_VIEW,uri);
				startActivity(intent);
			}
		});
    	dialog.show();
    }
}
