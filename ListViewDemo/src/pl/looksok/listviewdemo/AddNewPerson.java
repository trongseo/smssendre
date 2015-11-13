package pl.looksok.listviewdemo;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class AddNewPerson extends Activity {

	private AtomPayListAdapter adapter;
	private DBAdapter dbAdapter ;
	private static AddNewPerson Obj;  

    public AddNewPerson(){
           Obj=this;
        }

    public static AddNewPerson getInstance(){
      return Obj;
       }
    public void refresh(){
        //refresh Logic here
    	  Toast.makeText(this, "Tai du lieu", Toast.LENGTH_SHORT).show();
    	  loadDataListView("0");
        }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view);
		
		setupListViewAdapter();
		
		setupAddPaymentButton();
	
		dbAdapter = new DBAdapter(this);
		loadDataListView("0");
	}
	public void loadDataListView(String isFinish) {
		
		if(isFinish=="1"){
			setupListViewAdapterOk();	
		}else{
			setupListViewAdapter();
		}
		adapter.clear();
//		+ ", sms TEXT NOT NULL"
//		+ ", isFinish TEXT  NULL"
//		+ ", phoneNumber TEXT NOT NULL"  
//		+ ", dateCreate DATE DEFAULT CURRENT_DATE NOT NULL" + ")";
		String[] ar= new String[]{};
		ArrayList<ContentValues> arrData =  dbAdapter.getData("Select * from tbl_sms where isFinish='"+isFinish+"' order by dateLong desc" , new String[]{});
		int postx=0;
		for(ContentValues myRow: arrData){
			 String mySms = (String)	myRow.get("sms");
			 Integer myId = Integer.parseInt( myRow.get("_id").toString());
			 String dateCreate = myRow.get("dateCreate").toString();
			 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 try {
				 Date date = format.parse(dateCreate);
				 dateCreate= new SimpleDateFormat("HH:mm dd.MM ").format(date);
				 
				} catch (Exception e) {
				    e.printStackTrace();
				}
			 
			
				adapter.insert(new AtomPayment(mySms,dateCreate, myId), postx);
				postx++;
			}
	}


	AtomPayment itemToRemove;
	public void removeAtomPayOnClickHandler(View v) {
		
		 itemToRemove = (AtomPayment)v.getTag();
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { switch (which) { 
			case DialogInterface.BUTTON_POSITIVE: // Yes button clicked
				//Toast.makeText(AddNewPerson.this, "Yes Clicked", Toast.LENGTH_LONG).show();
				 removeAtomPay(itemToRemove);
				break; 
				case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
					//Toast.makeText(AddNewPerson.this, "No Clicked", Toast.LENGTH_LONG).show();
					
					break;
					
					} } }; 
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					String smsS="Bạn đã hoàn thành công việc ?";
					if(adapter.isF==true){
						 smsS="Bạn muốn xóa ?";
					}
					builder.setMessage(smsS) .setPositiveButton("Ok", dialogClickListener) .setNegativeButton("Chưa", dialogClickListener).show(); 
					
		
		
	}
	public void removeAtomPay(AtomPayment itemToRemove){
		adapter.remove(itemToRemove);
		double idre = itemToRemove.getValue();
		String[] arrCon= new String[]{};
		String smsS="Bạn đã hoàn thành công việc ?";
		if(adapter.isF==true){
			dbAdapter.runSQL("delete from tbl_sms where  _id="+idre, arrCon);
		}else
		{
			dbAdapter.runSQL("update tbl_sms set isFinish=1 where _id="+idre, arrCon);	
		}
		
	}

	private void setupListViewAdapter() {
		adapter = new AtomPayListAdapter(AddNewPerson.this, R.layout.atom_pay_list_item, new ArrayList<AtomPayment>(),false);
		ListView atomPaysListView = (ListView)findViewById(R.id.EnterPays_atomPaysList);
		atomPaysListView.setAdapter(adapter);
		
	}
	private void setupListViewAdapterOk() {
		adapter = new AtomPayListAdapter(AddNewPerson.this, R.layout.atom_pay_list_item_ok, new ArrayList<AtomPayment>(),true);
		ListView atomPaysListView = (ListView)findViewById(R.id.EnterPays_atomPaysList);
		atomPaysListView.setAdapter(adapter);
	}

	private void setupAddPaymentButton() {
		findViewById(R.id.EnterPays_addAtomPayment).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),"Đang tải..", Toast.LENGTH_SHORT).show();
//				
				int numBreak=0;
				 String SORT_ORDER = "date DESC";
				Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, SORT_ORDER);
				  String msgData = "";
				if (cursor.moveToFirst()) { // must check the result to prevent exception
				    do {
				     String myMsg ="";
				     String myInsert="";
				     String sodienthoai="";
				     String noidung="";
				     String ngaynhan="";
				     String ngaygui="";
				     for(int idx=0;idx<cursor.getColumnCount();idx++){
					        myMsg+= " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
					  }
				     
				      noidung =cursor.getString(cursor.getColumnIndexOrThrow("body"));
				      Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow("date")));
		           	  String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
		           	  ngaynhan = formattedDate;
		           	  sodienthoai= cursor.getString(cursor.getColumnIndexOrThrow("address"));
		           	  String smsSave =ngaynhan +System.getProperty("line.separator")+cursor.getString(cursor.getColumnIndexOrThrow("body"))+";"+sodienthoai;
				      AddSms( smsSave,  sodienthoai,date);
				      numBreak++;
				    	//if(numBreak==200) break;
				       // use msgData
				    } while (cursor.moveToNext());
				} else {
				   // empty box, no SMS
				}
				refresh();
				Toast.makeText(getApplicationContext(),"Đã tải xong..", Toast.LENGTH_SHORT).show();
				
			}
		});
		
			findViewById(R.id.btnNotOK).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Button btnNotOK = (Button) findViewById(R.id.btnNotOK);
				Button btnOK = (Button) findViewById(R.id.btnOK);
				btnNotOK.setBackgroundColor(Color.RED);
				btnOK.setBackgroundColor(Color.GRAY);
				
				String isFinishFalse= "0";
				loadDataListView(isFinishFalse);
				//adapter.insert(new AtomPayment("","", 0), 0);
			}
		});
			findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Button btnNotOK = (Button) findViewById(R.id.btnNotOK);
					Button btnOK = (Button) findViewById(R.id.btnOK);
					btnOK.setBackgroundColor(Color.RED);
					btnNotOK.setBackgroundColor(Color.GRAY);
					String isFinishTrue= "1";
					loadDataListView(isFinishTrue);
					//adapter.insert(new AtomPayment("","", 0), 0);
				}
			});

	}
	//region dss
	private String getDateTime(Date dt) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat(
	                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	   
	        dateFormat.format(dt);
	      //endregion beFound

	         
	        return dateFormat.format(dt);
	}
	private boolean isExist(long datestr){
		
		ArrayList<ContentValues> ar = dbAdapter.getData("Select * from tbl_sms where dateLong="+datestr+"", new String[]{});
		if(ar.size()==1)
			return true;
		return false;
	}
	public void AddSms(String msg,String phoneNu,Date dateCreate){
		 ContentValues values = new ContentValues();
	     String myNumber =phoneNu;
	   //kiem tra tin nhan trung,co the lay tat ca tin nhan duoc gui den neu con trong may
	     String sms = msg;
			values.put("sms",sms);
			values.put("isFinish", "0");
			values.put("phoneNumber", myNumber);
			String dateStr = getDateTime(dateCreate);
			values.put("dateCreate", dateStr);
			values.put("dateLong", dateCreate.getTime());
			if(myNumber.equals(this.getString(R.string.PHONE_NUMBER)))
			if(isExist( dateCreate.getTime())==false)
			if (dbAdapter.insert(DBAdapter.STU_TABLE, values) < 0) {
				Log.e("Error", "fff");
				return;
			}	
	}
	//endregion dss

}
