package com.jsycloud.activity;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.dh.DpsdkCore.Alarm_Enable_By_Dep_Info_t;
import com.dh.DpsdkCore.Alarm_Enable_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.fDPSDKAlarmCallback;
import com.jsycloud.ir.xiuzhou.AppApplication;
import com.jsycloud.bean.AlarmItem;
import com.jsycloud.ir.xiuzhou.R;


public class AlarmbuKongActivity extends Activity {
 
	private int m_pDLLHandle = 0;
	private static final String TAG = "AlarmbuKongActivity"; 
	private List<AlarmItem> AlarmbkList = new ArrayList<AlarmItem>();
	private AlarmbkAdapter alarmbkadapter;
	private String strSelectedItem;
	private String numSelectedType;
	private String[] strType;
	private String[] numType;
	private String[] titleType;
	private String eventNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_bukong);
		
		m_pDLLHandle = AppApplication.get().getDpsdkHandle();
		strType = getResources().getStringArray(R.array.alarm_type);
		numType = getResources().getStringArray(R.array.alarm_type_num);
		titleType = getResources().getStringArray(R.array.title_listview);
	    alarmbkadapter = new AlarmbkAdapter();
	    ListView alarmbkListView = (ListView)findViewById(R.id.listview_alarm_activity);
		alarmbkListView.setAdapter(alarmbkadapter);
		
		showSpinner();
		TextView vChannalName = (TextView)findViewById(R.id.video_channal_name);
	    TextView aChannalName = (TextView)findViewById(R.id.alarm_channel_name);
		TextView txtDeviceId = (TextView)findViewById(R.id.device_id);
		txtDeviceId.setText(getIntent().getStringExtra("deviceId"));
	   
		Button btnShaKong = (Button) findViewById(R.id.sakong);
		btnShaKong.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int ret = IDpsdkCore.DPSDK_DisableAlarm(m_pDLLHandle, 10*1000);
				showToast(ret);
			}
		});
		
		//回调
		int nRet = IDpsdkCore.DPSDK_SetDPSDKAlarmCallback(m_pDLLHandle,new fDPSDKAlarmCallback(){
			@Override
			public void invoke(int nPDLLHandle, byte[] szAlarmId,
					int nDeviceType, byte[] szCameraId,
					byte[] szDeviceName, byte[] szChannelName,
					byte[] szCoding, byte[] szMessage, int nAlarmType,
					int nEventType, int nLevel, long nTime,
					byte[] pAlarmData, int nAlarmDataLen,
					byte[] pPicData, int nPicDataLen) {
				String event;
//				AlarmbkList.add(String.valueOf(nEventType));    //报警发生类型，参考dpsdk_event_type_e
//				AlarmbkList.add(String.valueOf(nTime));
				if (strSelectedItem != null) {
					event = strSelectedItem;
				} else {
					event = strType[0];
				}
//				AlarmbkList.add(new String(szDeviceName));
//				AlarmbkList.add(new String(szChannelName));
				
				AlarmItem alarmItem = new AlarmItem(new String(szDeviceName), new String(szChannelName), 
						event, String.valueOf(nEventType), String.valueOf(nTime * 1000));
				AlarmbkList.add(alarmItem);
				Log.i(TAG,AlarmbkList.toString());
				
			    Message message = mHandler.obtainMessage(1);
			    message.sendToTarget();
//				mHandler.post(new Runnable() {
//					
//					@Override
//					public void run() {
//						alarmbkadapter.notifyDataSetChanged();
//					}
//				});
				Log.i(TAG, "回调DPSDK_SetDPSDKAlarmCallback 添加数据");
			}
			
		});
		
		if(nRet != 0){
			Toast.makeText(getApplicationContext(), "回调结果" + String.valueOf(nRet), Toast.LENGTH_SHORT).show();
		}
		
		Button btnBuKong = (Button) findViewById(R.id.bukong);
		btnBuKong.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Alarm_Enable_Info_t alarm_enable_info = new Alarm_Enable_Info_t(1);
				alarm_enable_info.sources[0].szAlarmDevId = getIntent().getStringExtra("deviceId").getBytes();
				alarm_enable_info.sources[0].nVideoNo = -1;
				alarm_enable_info.sources[0].nAlarmInput = -1;
				//alarm_enable_info.sources[0].nAlarmType = dpsdk_alarm_type_e.DPSDK_CORE_ALARM_TYPE_VIDEO_LOST;
				if (numSelectedType != null) {
					alarm_enable_info.sources[0].nAlarmType = Integer.parseInt(numSelectedType);   //spinner中选择的报警类型
				} else {
					alarm_enable_info.sources[0].nAlarmType = Integer.parseInt(numType[0]); 
				}
				          
				int ret = IDpsdkCore.DPSDK_EnableAlarm(m_pDLLHandle, alarm_enable_info, 10000);
				showToast(ret);
			}
		});
		
		Button btnBuKongByDepart = (Button) findViewById(R.id.according_bumeng_bukong);
		btnBuKongByDepart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			
				Alarm_Enable_By_Dep_Info_t alarm_Enable_By_Dep_Info_t = new Alarm_Enable_By_Dep_Info_t(1);
				EditText editTextDcode = (EditText)findViewById(R.id.edit_department_code);
				String Dcode = editTextDcode.getText().toString();
				if(Dcode != null){
					alarm_Enable_By_Dep_Info_t.sources[0].szAlarmDepartmentCode = Dcode.getBytes();
				} else {
					alarm_Enable_By_Dep_Info_t.sources[0].szAlarmDepartmentCode = "001".getBytes();
				}
				alarm_Enable_By_Dep_Info_t.sources[0].nVideoNo = -1;
				alarm_Enable_By_Dep_Info_t.sources[0].nAlarmInput = -1;
				if (numSelectedType != null) {
					alarm_Enable_By_Dep_Info_t.sources[0].nAlarmType = Integer.parseInt(numSelectedType);   //spinner中选择的报警类型
				} else {
					alarm_Enable_By_Dep_Info_t.sources[0].nAlarmType = Integer.parseInt(numType[0]); 
				}
				int ret = IDpsdkCore.DPSDK_EnableAlarmByDepartment(m_pDLLHandle, alarm_Enable_By_Dep_Info_t, 10000);
				showToast(ret);		
				
			}
		});
		
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				alarmbkadapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
		
	};
	class AlarmbkAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return AlarmbkList.size();
		}

		@Override
		public Object getItem(int position) {
			return AlarmbkList.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(view == null) {
				LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.listview_item_alarmbk, null);
				holder = new ViewHolder();
				holder.type = (TextView)view.findViewById(R.id.type);
				holder.time = (TextView)view.findViewById(R.id.date);
				holder.event = (TextView)view.findViewById(R.id.event);
				holder.device_name = (TextView)view.findViewById(R.id.device_name);
				holder.channel_name = (TextView)view.findViewById(R.id.channel_name);
				view.setTag(holder);
			}else {
				holder = (ViewHolder)view.getTag();
			}
			//setItemText(holder);               //设置listview的内容
			holder.device_name.setText(AlarmbkList.get(position).deviceName);
			holder.channel_name.setText(AlarmbkList.get(position).channelName);
			holder.event.setText(AlarmbkList.get(position).event);
			if(AlarmbkList.get(position).type.equals("1")) {
				holder.type.setText(getResources().getString(R.string.event_type_cs));
			} else {
				holder.type.setText(getResources().getString(R.string.event_type_xs));
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//String date = FORMAT_DISPLAY.format(AlarmbkList.get(position));
			Date date = new Date(Long.parseLong(AlarmbkList.get(position).date));
			holder.time.setText(sdf.format(date));
			
			Log.i(TAG, "AlarmbkList.toString()" + AlarmbkList.toString());
//			LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//			view = inflater.inflate(R.layout.listview_item_alarmbk, null);
//			TextView textView = (TextView)view.findViewById(R.id.txt_value);
//			switch (position) {
//				case 0:
//					if (AlarmbkList.get(position).equals("1")) {
//						textView.setText(getResources().getString(R.string.event_type_cs));
//					} else {
//						textView.setText(getResources().getString(R.string.event_type_xs));
//					}
//					                                                    //dpsdk_event_type_e
//					break;
//				case 1:
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					//String date = FORMAT_DISPLAY.format(AlarmbkList.get(position));
//					Date date = new Date(Long.parseLong(AlarmbkList.get(position)));
//					textView.setText(sdf.format(date));
//					break;
//				default:
//					textView.setText(AlarmbkList.get(position));
//					break;
//			}
//			TextView nTextView = (TextView)view.findViewById(R.id.txt_name);
//			nTextView.setText(titleType[position]);
			
			return view;
		}
		
		public class ViewHolder {
			 TextView type;
			 TextView time;
			 TextView event;
			 TextView device_name;
			 TextView channel_name; 
		}
		
	}
	
	private void showToast(int ret){
		if(ret == 0){
			Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(getApplicationContext(), "failed " + String.valueOf(ret), Toast.LENGTH_SHORT).show();
		}
	}
	private void showSpinner(){
		
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), 
        		android.R.layout.simple_spinner_item, strType);
        //改变字体颜色
        adapter.setDropDownViewResource(R.layout.spinner_item);
        Spinner sp = (Spinner)findViewById(R.id.type_alarm);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				strSelectedItem = strType[position];
				numSelectedType = numType[position];
				
				Log.i(TAG, "spinner" + strSelectedItem);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
