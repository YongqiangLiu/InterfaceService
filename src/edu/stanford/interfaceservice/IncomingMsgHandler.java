package edu.stanford.interfaceservice;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

/**
 * The Service which tracks the interface state and
 * manipulate interfaces
 *
 * IncomingMsgHandler class handles messages from other
 * threads and clients 
 * 
 * @author Yongqiang Liu (liuyq7809@gmail.com)
 *
 */

public class IncomingMsgHandler extends Handler {
	public final static String TAG = "IncomingMsgHandler of IntfaceService";
	private InterfaceService mService;
	
	public IncomingMsgHandler(InterfaceService service) {
		mService = service;
	}
	
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case Utility.MSG_REPORT_INTFACE_STATE:
				InterfaceTracker.IntfaceState report = (InterfaceTracker.IntfaceState) msg.obj;
				notifyStateToClient(report.mDevice, report.mOldState, report.mNewState);
				break;
			default:
				super.handleMessage(msg);
		}
	}
	
	void notifyStateToClient(String device, String oldState, String newState) {
		final int num = mService.mCallbacks.beginBroadcast();
		for (int i = 0; i < num; i++) {
			String dev = (String) mService.mCallbacks.getBroadcastCookie(i);
			if (dev.equals(device)) {
				try {
					mService.mCallbacks.getBroadcastItem(i).InterfaceStateChanged(
							device, oldState, newState);
				} catch (RemoteException e) {
					Log.d(TAG, "notify client Error: " + e.getMessage());
					
				}
			}
		}
		mService.mCallbacks.finishBroadcast();
	}
}