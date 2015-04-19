package com.demo.android.bmi;

import java.text.DecimalFormat;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	private static final String TAG = MainActivity.class.getSimpleName();
	//public static final String PREF = "BMI_PREF";
	//public static final String PREF_HEIGHT = "BMI_HEIGHT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG, "onCreate");
        initViews();
        //restorePres();
        setListensers();
    }
    
    
    //------------------------------------------Process Test
    public void onRestart () {
    	super.onRestart();
    	Log.v(TAG, "onReStart");
    }
    
    public void onStart () {
    	super.onStart();
    	Log.v(TAG, "onStart");
    }
    
    public void onResume() {
    	super.onResume();
    	Log.v(TAG, "onResume");
    	restorePres();
    }
    
    public void onPause () {
    	super.onPause();
    	Log.v(TAG, "onPause");
    	// Save user preferences. use Editor object to make changes.
    	//SharedPreferences settings = getSharedPreferences(PREF, 0);
    	//Editor editor = settings.edit();
    	//editor.putString(PREF_HEIGHT, num_height.getText().toString());
    	//editor.commit();
    	Pref.setHeight(this, num_height.getText().toString());
    }
    
    public void onStop () {
    	super.onStop();
    	Log.v(TAG, "onStop");
    }
    
    public void onDestroy () {
    	super.onDestroy();
    	Log.v(TAG, "onDestroy");
    }
    //--------------------------------------------
    
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
    
 // Restore preferences
    private void restorePres () {
    	//SharedPreferences settings = getSharedPreferences (PREF, 0);
    	//String pref_height = settings.getString(PREF_HEIGHT, "");
    	String pref_height = Pref.getHeight(this);
    	if (! "".equals(pref_height)) {
    		num_height.setText(pref_height);
    		num_weight.requestFocus();
    	}
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
        	//openOptionsDialog();
        	Intent intent = new Intent(Intent.ACTION_VIEW);
        	intent.setClass(MainActivity.this, Pref.class);
        	startActivity(intent);
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
