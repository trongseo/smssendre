package vnpt.app.vnpttask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SrcConfig extends Activity  {
	private AtomPayListAdapter adapter;
	private DBAdapter dbAdapter ;
	private static SrcConfig Obj;  
	private ContentValues ConfigValue;
	
	//
	//Button btnNotOK = (Button) findViewById(R.id.btnNotOK);
	private EditText txtPassword;
	private EditText txtPhone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.config);
		dbAdapter = new DBAdapter(this);
		ConfigValue =  dbAdapter.getConfig();
		setupText();
		setupButton();
		
	}
    public SrcConfig(){
        Obj=this;
     }

 public static SrcConfig getInstance(){
   return Obj;
    }
	private void setupText() {
		txtPhone = (EditText)findViewById(R.id.txtPhone);
		txtPassword = (EditText)findViewById(R.id.txtPassword);
		txtPhone.setText( ConfigValue.getAsString("field1"));
		
	}
	  
	    
	public String md5(String s) {
		try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest
	                .getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	 
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < messageDigest.length; i++) {
	            String h = Integer.toHexString(0xFF & messageDigest[i]);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();
	 
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	private void setupButton() {
		findViewById(R.id.btnBack).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
				
			}
		});
		
			
			findViewById(R.id.btnUpdate).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String strPhone =  txtPhone.getText().toString();
					String strPass =  txtPassword.getText().toString();
					
					
					
					if(strPass.equals("")){
						Toast.makeText(getApplicationContext(),"Hãy nhập mật khẩu!", Toast.LENGTH_SHORT).show();
						txtPassword.requestFocus();
						return;
					}
					
					String md5Pass = md5(strPass);
					String myPass = getInstance().getString(R.string.PASSWORD);
					
					if(md5Pass.equals(myPass)==false){
						Toast.makeText(getApplicationContext(),"Mật khẩu sai!Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
						txtPassword.requestFocus();
						return;
					}
					
					
					
					if(strPhone.equals("")){
						Toast.makeText(getApplicationContext(),"Hãy nhập số!", Toast.LENGTH_SHORT).show();
						txtPhone.setFocusable(true);
						
						return;
					}
					
					//"+ +"
				 dbAdapter.runSQL("update tbl_config set field1='"+txtPhone.getText().toString()+"' where _id=1");
				 Toast.makeText(getApplicationContext(),"Cập nhật thành công!", Toast.LENGTH_SHORT).show();
				 
				}
			});

	}
}
