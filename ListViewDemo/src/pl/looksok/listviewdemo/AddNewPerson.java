package pl.looksok.listviewdemo;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.graphics.Color;
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
		ArrayList<ContentValues> arrData =  dbAdapter.getData("Select * from tbl_sms where isFinish='"+isFinish+"'" , new String[]{});
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
					builder.setMessage("Bạn đã hoàn thành công việc ?") .setPositiveButton("Ok", dialogClickListener) .setNegativeButton("Chưa", dialogClickListener).show(); 
					
		
		
	}
	public void removeAtomPay(AtomPayment itemToRemove){
		adapter.remove(itemToRemove);
		double idre = itemToRemove.getValue();
		String[] arrCon= new String[]{};
		dbAdapter.runSQL("update tbl_sms set isFinish=1 where _id="+idre, arrCon);
	}

	private void setupListViewAdapter() {
		adapter = new AtomPayListAdapter(AddNewPerson.this, R.layout.atom_pay_list_item, new ArrayList<AtomPayment>());
		ListView atomPaysListView = (ListView)findViewById(R.id.EnterPays_atomPaysList);
		atomPaysListView.setAdapter(adapter);
		
	}
	private void setupListViewAdapterOk() {
		adapter = new AtomPayListAdapter(AddNewPerson.this, R.layout.atom_pay_list_item_ok, new ArrayList<AtomPayment>());
		ListView atomPaysListView = (ListView)findViewById(R.id.EnterPays_atomPaysList);
		atomPaysListView.setAdapter(adapter);
	}
	private void addData(String sms){
		
//		+ ", sms TEXT NOT NULL"
//		+ ", isFinish TEXT  NULL"
//		+ ", phoneNumber TEXT NOT NULL"  
//		+ ", dateCreate DATE DEFAULT CURRENT_DATE NOT NULL" + ")";
		ContentValues values = new ContentValues();
		values.put("sms", sms);
		values.put("isFinish", "0");
		values.put("phoneNumber", "0989858867");
		if (dbAdapter.insert(DBAdapter.STU_TABLE, values) < 0) {
			Log.e("Error", "fff");
			return;
		}
	}
	private void setupAddPaymentButton() {
		findViewById(R.id.EnterPays_addAtomPayment).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addData("259/78/82c phan xich long tucap 109:"+Math.random());
				String isFinishFalse= "0";
				loadDataListView(isFinishFalse);
				//adapter.insert(new AtomPayment("","", 0), 0);
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
}
