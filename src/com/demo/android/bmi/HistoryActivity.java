package com.demo.android.bmi;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.support.v4.widget.CursorAdapter;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

public class HistoryActivity extends ListActivity {
	private DB mDbHelper;
	private Cursor mCursor;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_ITEM = "note";
	public static final String KEY_CREATED = "created";
	
	private ActionMode.Callback mCallback;
	private ActionMode mMode;
	
	
	static final String[] records = new String[] {
		"20",
		"21",
		"22",
		"23",
		"24"
	};
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.context_history, menu);
		menu.setHeaderTitle("要怎麼處理這筆項目?");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info;
		info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.action_delete:
			mDbHelper.delete(info.id);
			setAdapter();
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		getListView().setEmptyView(findViewById(R.id.empty));
		//registerForContextMenu(getListView());
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		setAdapter();
		
		mCallback = new ActionMode.Callback() {
			
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub
				mMode = null;
			}
			
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				mode.setTitle("Demo");
				getMenuInflater().inflate(R.menu.context_history, menu);
				return true;
			}
			
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
				case R.id.action_delete:
					//AdapterView.AdapterContextMenuInfo info;
					//info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
					int position = getListView().getCheckedItemPosition();
					mCursor.moveToPosition(position);
					mDbHelper.delete(mCursor.getLong(0));
					setAdapter();
					mode.finish();
					break;
				}
				return false;
			}
		};
		
		OnItemLongClickListener listener = new OnItemLongClickListener() {
			 
            @Override
            public boolean onItemLongClick(AdapterView<?> view, View row,
					int position, long id) {
                if(mMode!=null)
                    return false;
                else
                    mMode = startActionMode(mCallback);
                    getListView().setItemChecked(position, true);
                return true;
             }
        };
        
        getListView().setOnItemLongClickListener(listener);
	}
	
	private void setAdapter () {
		mDbHelper = new DB(this);
		mDbHelper.open();
		
		mCursor = mDbHelper.getAll();
		startManagingCursor(mCursor);
		
		String[] from_column = new String[]{KEY_ITEM,KEY_CREATED};
		int[] to_layout = new int[]{R.id.text1,R.id.text2};
		
		//Now create a simple cursor adapter
		
		ListCursorAdapter adapter = new ListCursorAdapter(this, mCursor);
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id){
				mDbHelper.delete(id);
				setAdapter();
			}
		});
	}
	
	public class ListCursorAdapter extends CursorAdapter {

		public ListCursorAdapter(Context context, Cursor c) {
			super(context, c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			TextView text1 = (TextView)view.findViewById(R.id.text1);
			TextView text2 = (TextView)view.findViewById(R.id.text2);
			
			text1.setText(cursor.getString(cursor.getColumnIndex(DB.KEY_ITEM)));
			text2.setText(cursor.getString(cursor.getColumnIndex(DB.KEY_CREATED)));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.list_row, parent,false);
			bindView(v, context, cursor);
			
			return v;
		}
		
	}
	
}
