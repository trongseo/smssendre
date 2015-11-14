package vnpt.app.vnpttask;
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

	public static final String tbl_config = "tbl_config";
	public static final String Col_Number = "field1";
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
		 open();
		db.execSQL("delete from "+STU_TABLE+";");
		db.close();
		
	}
	//  new String[] { "sss","ddd" } where age =? and kd=?
	public void runSQL(String Sql,String[] arrCon){
		open(); 
		db.execSQL(Sql,arrCon);
		db.close();
		
	}
	
	public void runSQL(String Sql){
		open(); 
		db.execSQL(Sql,new String[]{});
		db.close();
		
	}
	
	public ContentValues getConfig(){
		open(); 
		ArrayList<ContentValues>  onvl = getData("Select * from "+ tbl_config);
		if(onvl.size()==0)
			return new ContentValues();
		
		ContentValues ct= onvl.get(0);
		db.close();
		
		return ct;
	}
	public ArrayList<ContentValues> getData(String Sql) {
		return getData( Sql,new String[]{});
	}
	/**
	 *
	 * ArrayList<ContentValues> arrData =  dbAdapter.getData("Select * from tbl_sms where isFinish='"+isFinish+"' order by dateLong desc" , new String[]{});
		int postx=0;
		for(ContentValues myRow: arrData){
			 String mySms = (String)	myRow.get("sms");
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
					+ ", dateLong number NOT NULL"  
					+ ", dateCreate datetime DEFAULT current_timestamp NOT NULL" + ")";

			db.execSQL(create_sql);
			
			create_sql = "CREATE TABLE IF NOT EXISTS  tbl_config ("
					+ STU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
					+ ", field1 DEFAULT ''"
					+ ", field2 DEFAULT ''"
					+ ", field3 DEFAULT ''"  
					+ ", field4 DEFAULT ''"  
					+ ", field5 DEFAULT ''"  
					+ ", field6 DEFAULT ''"  
					+ ", field7 DEFAULT ''"  
					+ ", field8 DEFAULT ''"  
					+ ", field9 DEFAULT ''"  
					+ ", field10 DEFAULT ''"  
					+ ", field11 DEFAULT ''"  
					+ ", dateCreate datetime DEFAULT current_timestamp NOT NULL" + ")";

			db.execSQL(create_sql);
			db.execSQL("insert into tbl_config(field1) values('')");
			Log.d(TAG, "Create" + STU_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + STU_TABLE);
			db.execSQL("DROP TABLE IF EXISTS tbl_config " );
			
		}
	}
}