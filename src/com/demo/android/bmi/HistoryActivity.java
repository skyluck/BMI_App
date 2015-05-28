package com.demo.android.bmi;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.view.View;

public class HistoryActivity extends ListActivity {
	private DB mDbHelper;
	private Cursor mCursor;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_ITEM = "note";
	public static final String KEY_CREATED = "created";
	
	
	static final String[] records = new String[] {
		"20",
		"21",
		"22",
		"23",
		"24"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		getListView().setEmptyView(findViewById(R.id.empty));
		setAdapter();
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id){
				mDbHelper.delete(position);
				setAdapter();
			}
		});
	}
	
	private void setAdapter () {
		mDbHelper = new DB(this);
		mDbHelper.open();
		
		mCursor = mDbHelper.getAll();
		startManagingCursor(mCursor);
		
		String[] from_column = new String[]{KEY_ITEM,KEY_CREATED};
		int[] to_layout = new int[]{android.R.id.text1,android.R.id.text2};
		
		//Now create a simple cursor adapter
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, mCursor, from_column, to_layout);
		setListAdapter(adapter);
	}
	
}
