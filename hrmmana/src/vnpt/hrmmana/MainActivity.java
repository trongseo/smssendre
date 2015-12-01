package vnpt.hrmmana;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final FrameLayout.LayoutParams ZOOM_PARAMS =
			new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT,
			Gravity.BOTTOM);
    private WebView webview;
    private static final String TAG = "Main";
    private ProgressDialog progressBar;  
    private Button btnMenu;
    private Button btnNext;
	private Button btnPre;
	private Button btnLon;
	private Button btnNho;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        
        //khai bao button
        btnMenu = (Button)findViewById(R.id.btnMenu);
        btnNext = (Button)findViewById(R.id.btnNext);
        btnPre = (Button)findViewById(R.id.btnPre);
        btnLon = (Button)findViewById(R.id.btnLon);
        btnNho = (Button)findViewById(R.id.btnNho);
        
        this.webview = (WebView)findViewById(R.id.webView1);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressBar = ProgressDialog.show(MainActivity.this, "VNPT HCM", "Đang kết nối máy chủ...");
        //khong goi new browser khi click link,button
        webview.setWebViewClient(new WebViewClient() {  
       	 @Override  
       	 public boolean shouldOverrideUrlLoading(WebView view, String url)  
       	 {  
       	   view.loadUrl(url);
       	   return true;
       	 }  
       	 }); 
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " +url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                //Toast.makeText(MainActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("VNPT thông báo");
                //alertDialog.setMessage(description);
               
                webview.loadUrl("file:///android_asset/0.html");   
                alertDialog.setMessage("Không thể kết nối với máy chủ! Xin vui lòng kiểm tra internet.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return; 
                    }
                });
                alertDialog.show();
            }
        });
        //trang khoi dong
        
        webview.loadUrl("http://elocation.hcmtelecom.vn/login_androi.aspx");
        ///
        //oncreate end
	findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}});
        btnMenu.setOnClickListener(new View.OnClickListener() {			
     		@SuppressWarnings("unchecked")
     		@Override
     		public void onClick(View v) {
     			webview.loadUrl("http://elocation.hcmtelecom.vn/default_androi.aspx"); 
     		}});
        
        btnPre.setOnClickListener(new View.OnClickListener() {			
     		@SuppressWarnings("unchecked")
     		@Override
     		public void onClick(View v) {
     			//webview.loadUrl("http://elocation.hcmtelecom.vn/login_androi.aspx"); 
     			if(webview.canGoBack())
     	        {
     	             webview.goBack();
     	           
     	        }
     		}});
        
        btnNext.setOnClickListener(new View.OnClickListener() {			
     		
     			@Override
     			public void onClick(View v) {
     				//webview.loadUrl("http://elocation.hcmtelecom.vn/login_androi.aspx"); 
     				  if(webview.canGoForward()){
     		             webview.goForward();
     		        }

     			}     
     		});
        btnNho.setOnClickListener(new View.OnClickListener() {			
     	
     		@Override
     		public void onClick(View v) {
     		int deFsize =	webview.getSettings().getDefaultFontSize();
     		deFsize--;
     		webview.getSettings().setDefaultFontSize(deFsize);
     		}});
  		// specify the minimum type of characters before drop-down list is shown
         btnLon.setOnClickListener(new View.OnClickListener() {			
     		@SuppressWarnings("unchecked")
     		@Override
     		public void onClick(View v) {
     			int deFsize =	webview.getSettings().getDefaultFontSize();
     		deFsize++;
     		webview.getSettings().setDefaultFontSize(deFsize);
     		}});     
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig ){       
    super.onConfigurationChanged( newConfig);
  }

 
}