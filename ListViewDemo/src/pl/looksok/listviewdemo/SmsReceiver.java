package pl.looksok.listviewdemo;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";            
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
                str += " " + msgs[i].getOriginatingAddress();                     
                str += " :";
                str += msgs[i].getMessageBody().toString();
                //str += "\n";      
                DBAdapter dbAdapter = new DBAdapter(context);
                ContentValues values = new ContentValues();
              //kiem tra tin nhan trung,co the lay tat ca tin nhan duoc gui den neu con trong may
                String sms = msgs[i].getOriginatingAddress()+";"+ msgs[i].getMessageBody().toString();
        		values.put("sms",sms);
        		values.put("isFinish", "0");
        		values.put("phoneNumber", msgs[i].getOriginatingAddress());
        		if (dbAdapter.insert(DBAdapter.STU_TABLE, values) < 0) {
        			Log.e("Error", "fff");
        			return;
        		}
            }
            //---display the new SMS message---
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            
        }                 		
	}
}