package edu.stanford.interfaceservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.os.Process;

/**
 * The Service which tracks the interface state and
 * manipulate interfaces
 *
 * MainActivity is test client Activity 
 * 
 * @author Yongqiang Liu (liuyq7809@gmail.com)
 *
 */

public class MainActivity extends Activity {
	public static String TAG = "MainActivity";
	IRemoteService mService = null;
	ToggleButton serviceButton = null;
	ToggleButton wifiControl = null;
	ToggleButton wifiTracker = null;
	ToggleButton g3Control = null;
	ToggleButton g3Tracker = null;
	
	private OnClickListener controlLisener = new OnClickListener() {
			public void onClick(View v) {
				ToggleButton button = (ToggleButton) v;
				boolean state = (button.isChecked() == true)? true : false;
				try {
					if (button.getId() == R.id.wifiControl)
						mService.setWiFiEnable(state);
					else
						mService.set3GEnable(state);
				} catch (RemoteException e) {
					Log.d(TAG, "set WiFi Enable Error: " + e.getMessage());
				}
			}	
	};
	
	private OnClickListener trackerLisener = new OnClickListener() {
		public void onClick(View v) {
			ToggleButton button = (ToggleButton) v;
			String device = (button.getId() == R.id.wifiTracker)? "eth0" : "rmnet0";
			try {
				if (button.isChecked())
					mService.registerCallback(mCallback, device);
				else
					mService.unregisterCallback(mCallback, device);
			} catch (RemoteException e) {
				Log.d(TAG, device + " Tracing State Error: " + e.getMessage());
			}
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "calling onCreate(), Thread:" + Thread.currentThread().getId());
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);
    	serviceButton = (ToggleButton) findViewById(R.id.service);
    	wifiControl = (ToggleButton) findViewById(R.id.wifiControl);
    	wifiTracker = (ToggleButton) findViewById(R.id.wifiTracker);
    	g3Control = (ToggleButton) findViewById(R.id.g3Control);
    	g3Tracker = (ToggleButton) findViewById(R.id.g3Tracker);
    	setAllButtonsStatus(false);
    	serviceButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (((ToggleButton)v).isChecked()) {
							Log.d(TAG, "start Service");
					    	Intent intent = new Intent(MainActivity.this, InterfaceService.class);
					    	startService(intent);
				            bindService(new Intent(MainActivity.this, InterfaceService.class),
				                    mConnection, Context.BIND_AUTO_CREATE);
				            setAllButtonsStatus(true);
						} else {
							Log.d(TAG, "stop Service");
							try {
								if (mService != null) Process.killProcess(mService.getPid());
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							setAllButtonsStatus(false);
						}
					}
		});
    	wifiControl.setOnClickListener(controlLisener);
    	wifiTracker.setOnClickListener(trackerLisener);
    	g3Control.setOnClickListener(controlLisener);
    	g3Tracker.setOnClickListener(trackerLisener);
    }
   
    private void setAllButtonsStatus(boolean enable) {
    	wifiControl.setEnabled(enable);
    	wifiTracker.setEnabled(enable);
    	g3Control.setEnabled(enable);
    	g3Tracker.setEnabled(enable);
    }
    
    private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName classname, IBinder service) {
			Log.d(TAG, "Attached.");
			mService = IRemoteService.Stub.asInterface(service);
		}
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "unAttached.");
			mService = null;
		}
    };
    
    private final IRemoteServiceCallback.Stub mCallback = new IRemoteServiceCallback.Stub() {

		public void InterfaceStateChanged(String device, String oldState, String newState) {
			//Log.d(TAG, device + " state is " + newState);
			String info = device + " changes from " + oldState + " to " + newState;
			mMsgHandler.sendMessage(mMsgHandler.obtainMessage(Utility.MSG_UPDATE_TXTVIEW, info));
		}
		public void updateServiceInfo(String info) {
			mMsgHandler.sendMessage(mMsgHandler.obtainMessage(Utility.MSG_UPDATE_TXTVIEW, info));
		}
		public void basicTypes(int anInt, long aLong, boolean aBoolean,
				float aFloat, double aDouble, String aString)
				throws RemoteException {
			// TODO Auto-generated method stub
		}
    };
    
    private final Handler mMsgHandler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
    			case Utility.MSG_UPDATE_TXTVIEW:
    				updateViewTxt((String)msg.obj);
    				break;
    		}
    	}
    	
    	public void updateViewTxt(String info) {
    		TextView report = (TextView) findViewById(R.id.report);
    		report.append(info + "\n");
    		ScrollView scroll = (ScrollView) findViewById(R.id.txt_scrollview);
    		scroll.scrollTo(0, report.getHeight());
    	}
    };
}