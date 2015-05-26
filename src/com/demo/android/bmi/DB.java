package com.demo.android.bmi;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {
	private Context mContext = null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_ITEM = "note";
	public static final String KEY_CREATED = "created";
	
	private static final String DATABASE_NAME = "notes.db";
	private static final int DATABASE_VERSION = 3;
	
	private static final String DATABASE_TABLE = "notes";
	
	private static final String DATABASE_CREATE = 
			"CREATE TABLE IF NOT EXISTS notes ("
			+"_id INTEGER PRIMARY KEY,"
			+"note TEXT NOT NULL,"
			+"created TIMESTAMP"
			+");";
	
	/** Constructor **/
	public DB(Context context) {
		this.mContext = context;
	}
	
	public DB open () throws SQLException {
		dbHelper = new DatabaseHelper(mContext);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Cursor getAll() {
		return db.rawQuery("SELECT * FROM  " + DATABASE_TABLE + " ORDER BY " + KEY_CREATED + " DESC", null);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS '" + DATABASE_CREATE + "'");
			onCreate(db);
		}
		
	}
}
