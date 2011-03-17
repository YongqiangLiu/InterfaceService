package edu.stanford.interfaceservice;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * The Service which tracks the interface state and
 * manipulate interfaces
 *
 * InterfaceTracker class monitors continuously the 
 * interfaces state and feedbacks it the registered
 * client's callback
 *  
 * @author Yongqiang Liu (liuyq7809@gmail.com)
 *
 */

public class InterfaceTracker implements Runnable {
	public static final String TAG = "InterfaceTracker";
	
	private Handler mMsgHandler = null;
	private String mDevice = null;
	private String mState = null;
	
	public InterfaceTracker(Handler msgHandler, String device) {
		mMsgHandler = msgHandler;
		mDevice = device;
	}
	
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			String state = getInfaceState(mDevice);
			Log.d(TAG, mDevice + " is " + state);
			if (mState == null || mState != state) {
				Message msg = Message.obtain();
				msg.what = Utility.MSG_REPORT_INTFACE_STATE;
				msg.obj = new IntfaceState(mDevice, mState, state);
				mMsgHandler.sendMessage(msg);
				mState = state;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e){
				Thread.currentThread().interrupt();
			}
		}
	}
	
	
	public static String getInfaceState(String device) {
		String state = "Disconnected";
		final String gwNetmask = "00000000"; //indicate this route direct to default gw
		for (String line : Utility.readLinesFromFile("/proc/net/route")) {
				String[] items = line.split("\t| +");
				if (items[0].equals(device)) {
					state = "Connected";
					if (items[1].equals(gwNetmask)) state = "AsDefaultRoute";
				}
		}
		return state;
	}
	
	public class IntfaceState {
		public String mDevice = null;
		public String mOldState = null;
		public String mNewState = null;
		public IntfaceState(String dev, String oldState, String newState) {
			mDevice = dev;
			mOldState = oldState;
			mNewState = newState;
		}
	}
	
}

