package pl.looksok.listviewdemo;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	String TAG = "DBAdapter";

	public static final String STU_TABLE = "tbl_sms";
	public static final String STU_ID = "_id";// 0 integer
//	+ ", sms TEXT NOT NULL"
//	+ ", isFinish TEXT  NULL"
//	+ ", phoneNumber TEXT NOT NULL"  
//	+ ", dateCreate DATE DEFAULT CURRENT_DATE NOT NULL" + ")";
	public SQLiteDatabase db;
	private DBHelper dbHelper;

	public DBAdapter(Context context) {
		dbHelper = new DBHelper(context);
		this.open();
	}

	/**
	 * 
	 */
	public void open() {
		if (null == db || !db.isOpen()) {
			try {
				db = dbHelper.getWritableDatabase();
			} catch (SQLiteException sqLiteException) {
				Log.e(TAG, "Die");
			}
		}
	}

	/**
	 * 
	 */
	public void close() {
		if (db != null) {
			db.close();
		}
	}

	/**
	 * 
	 * @param table
	 *            
	 * @param values
	 *            
	 * @return 
	 */
	public int insert(String table, ContentValues values) {
		 open();
		Log.e(TAG, "insert" + values);
		int resulI = (int)db.insert(table, null, values);
		db.close();
		return resulI;
		
	}
	public void deleteTable(){
		
		db.execSQL("delete from "+STU_TABLE+";");
		db.close();
		
	}
	//  new String[] { "sss","ddd" } where age =? and kd=?
	public void runSQL(String Sql,String[] arrCon){
		open(); 
		db.execSQL(Sql,arrCon);
		db.close();
		
	}
	/**
	 *
	 * 
	 * @param table
	 *           new String[] { "sss","ddd" } where age =? and kd=?
	 * @return 
	 */
	public ArrayList<ContentValues> getData(String Sql,String[] arrCon) {
		open(); 
		 ArrayList<ContentValues> retVal = new ArrayList<ContentValues>();
		 ContentValues map;
		String selectQuery = Sql;
		 Cursor c = db.rawQuery(selectQuery,  arrCon);//new String[]{}
		if(c.moveToFirst()) {       
			   do {
			        map = new ContentValues();
			        DatabaseUtils.cursorRowToContentValues(c, map);                 
			        retVal.add(map);
			    } while(c.moveToNext());
			}
	
			c.close();
			close(); 
			return retVal;
	}
 
 public static ContentValues cursorRowToContentValues(Cursor cursor) {
	    ContentValues values = new ContentValues();
	    String[] columns = cursor.getColumnNames();
	    int length = columns.length;
	    for (int i = 0; i < length; i++) {
	        switch (cursor.getType(i)) {
	            case Cursor.FIELD_TYPE_NULL:
	                values.putNull(columns[i]);
	                break;
	            case Cursor.FIELD_TYPE_INTEGER:
	                values.put(columns[i], cursor.getLong(i));
	                break;
	            case Cursor.FIELD_TYPE_FLOAT:
	                values.put(columns[i], cursor.getDouble(i));
	                break;
	            case Cursor.FIELD_TYPE_STRING:
	                values.put(columns[i], cursor.getString(i));
	                break;
	            case Cursor.FIELD_TYPE_BLOB:
	                values.put(columns[i], cursor.getBlob(i));
	                break;
	        }
	    }
	    return values;
	}
	/**
	 * SQLite 
	 * 
	 * @author tangyuchun
	 * 
	 */

	private class DBHelper extends SQLiteOpenHelper {
		private static final int VERSION = 1;
		private static final String DB_NAME = "smsdb1.db";

		public DBHelper(Context context) {
			super(context, DB_NAME, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			String create_sql = "CREATE TABLE IF NOT EXISTS " + STU_TABLE + "("
					+ STU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
					+ ", sms TEXT NOT NULL"
					+ ", isFinish TEXT  NULL"
					+ ", phoneNumber TEXT NOT NULL"  
					+ ", dateCreate datetime DEFAULT current_timestamp NOT NULL" + ")";

			db.execSQL(create_sql);
			Log.d(TAG, "Create" + STU_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + STU_TABLE);
		}
	}
}