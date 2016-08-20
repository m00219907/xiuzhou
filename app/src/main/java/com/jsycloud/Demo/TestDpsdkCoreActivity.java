package com.jsycloud.Demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jsycloud.ir.xiuzhou.R;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Login_Info_t;
import com.jsycloud.ir.xiuzhou.AppApplication;
import com.jsycloud.groupTree.GroupListActivity;


public class TestDpsdkCoreActivity extends Activity {
    /** Called when the activity is first created. */
	
	Button m_btLogin;
	static IDpsdkCore dpsdkcore = new IDpsdkCore();
	EditText m_serverIp;
	EditText m_serverPort;
	EditText m_serverUserName;
	EditText m_serverPassword;
	Resources res;
	
	//鏍囪鏄惁绗竴娆＄櫥鍏�
	private String isfirstLogin;
    protected ProgressDialog mProgressDialog;
    private AppApplication mAPP = AppApplication.get();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        mAPP.initApp();
           
    	m_btLogin 			= (Button)findViewById(R.id.buttonLogin);
    	m_serverIp 			= (EditText)findViewById(R.id.editText_server);
    	m_serverPort 		= (EditText)findViewById(R.id.editText_server_Port);
    	m_serverUserName 	= (EditText)findViewById(R.id.editText_server_user);
    	m_serverPassword 	= (EditText)findViewById(R.id.editText_server_password);
    	isfirstLogin = getSharedPreferences("LOGININFO", 0).getString("ISFIRSTLOGIN", "");
    	if(isfirstLogin.equals("false")){
    		setEditTextContent();
    	}
    	
    	m_btLogin.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
			    showLoadingProgress(R.string.login);
				new LoginTask().execute();
			}
    	});	
        
    }

    class LoginTask extends AsyncTask<Void, Integer, Integer>{

		@Override
		protected Integer doInBackground(Void... arg0) {               //鍦ㄦ澶勫鐞哢I浼氬鑷村紓甯�
//			if (mloginHandle != 0) {
//	    		IDpsdkCore.DPSDK_Logout(m_loginHandle, 30000);
//        		m_loginHandle = 0;
//	    	}
	    	Login_Info_t loginInfo = new Login_Info_t();
	    	loginInfo.szIp 	= m_serverIp.getText().toString().getBytes();
	    	String strPort 	= m_serverPort.getText().toString().trim();
	    	loginInfo.nPort = Integer.parseInt(strPort);
	    	loginInfo.szUsername = m_serverUserName.getText().toString().getBytes();
	    	loginInfo.szPassword = m_serverPassword.getText().toString().getBytes();
	    	loginInfo.nProtocol = 2;
	    	saveLoginInfo();
	    	int nRet = IDpsdkCore.DPSDK_Login(mAPP.getDpsdkCreatHandle(), loginInfo, 30000);
    		return nRet;
		}

		@Override
		protected void onPostExecute(Integer result) {
			
			super.onPostExecute(result);
			mProgressDialog.dismiss();   
    		if (result == 0) {
		    	Log.d("DpsdkLogin success:",result+"");
		    	IDpsdkCore.DPSDK_SetCompressType(mAPP.getDpsdkCreatHandle(), 0);
		    	mAPP.setLoginHandler(1);
	    	//	m_loginHandle = 1;
	    		jumpToItemListActivity();
	    	} else {
		    	Log.d("DpsdkLogin failed:",result+"");
		    	Toast.makeText(getApplicationContext(), "login failed" + result, Toast.LENGTH_SHORT).show();
		    	mAPP.setLoginHandler(0);
		    	//m_loginHandle = 0;
	    		//jumpToContentListActivity();
	    	}
		}

    }
    /**
     * 鍙栧嚭 sharedpreference鐨勭櫥褰曚俊鎭苟鏄剧ず
     */
    private void setEditTextContent(){
    	SharedPreferences sp = getSharedPreferences("LOGININFO", 0);
    	String content = sp.getString("INFO", "");
    	String[] loginInfo = content.split(",");
    	if(loginInfo != null){
    		m_serverIp.setText(loginInfo[0]);
        	m_serverPort.setText(loginInfo[1]);
        	m_serverPassword.setText(loginInfo[2]);
        	m_serverUserName.setText(loginInfo[3]);
    	}
    	Log.i("TestDpsdkCoreActivity", "setEditTextContent" + content);
    }
    private void saveLoginInfo(){
    	SharedPreferences sp = getSharedPreferences("LOGININFO", 0);
		Editor ed = sp.edit();
		StringBuilder sb = new StringBuilder();
		sb.append(m_serverIp.getText().toString()).append(",").append(m_serverPort.getText().toString()).append(",")
			.append(m_serverPassword.getText().toString()).append(",").append(m_serverUserName.getText().toString());
		ed.putString("INFO", sb.toString());
		ed.putString("ISFIRSTLOGIN", "false");
		ed.commit(); 	
		Log.i("TestDpsdkCoreActivity", "saveLoginInfo" + sb.toString());
    }
    

    protected void showLoadingProgress(int resId) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
        } else {
            mProgressDialog = ProgressDialog.show(this, null, getString(resId));
            mProgressDialog.setCancelable(false);
        }
    }

  
    public void jumpToItemListActivity()
    {
		Intent intent = new Intent();
		intent.setClass(this, GroupListActivity.class);
		//intent.setClass(this, ItemListActivity.class);
		startActivityForResult(intent, 2);
    }
    
    public void Logout()
    {
    	if (mAPP.getLoginHandler() == 0)
    	{
    		return;
    	}
    	int nRet = IDpsdkCore.DPSDK_Logout(mAPP.getDpsdkCreatHandle(), 30000);
    	
    	if ( 0 == nRet )
    	{
    		//m_loginHandle = 0;
    		mAPP.setLoginHandler(0);
    	}
    }
    
	@Override
	protected void onDestroy() 
	{   
		Logout();
		
		IDpsdkCore.DPSDK_Destroy(mAPP.getDpsdkCreatHandle());
		
		super.onDestroy();		
	}
}