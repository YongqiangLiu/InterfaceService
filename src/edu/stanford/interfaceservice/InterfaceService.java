package edu.stanford.interfaceservice;

import java.util.HashMap;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

/**
 * The Service which tracks the interface state and
 * manipulate interfaces
 *
 * InterfaceService class defines the AIDL of the service
 * to the remote client processes.
 * 
 * @author Yongqiang Liu (liuyq7809@gmail.com)
 *
 */

public class InterfaceService extends Service {
	public final static String TAG = "Interface Tracker Service";
	
	private InterfaceControl intfControl = null;
	
	final RemoteCallbackList<IRemoteServiceCallback> mCallbacks = 
		new RemoteCallbackList<IRemoteServiceCallback>();
	final IncomingMsgHandler mMsgHandler = new IncomingMsgHandler(InterfaceService.this);
	
	final HashMap<String, TrackerHandler> mIntfaceTrackers = new HashMap<String, TrackerHandler>(); 
	
    private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        public int getPid(){
        	Log.d(TAG, "getPid Thread id " + Thread.currentThread().getId());
            return Process.myPid();
        }
        public synchronized void setWiFiEnable(boolean enable) {
         	broadcastInfo(" WiFI is set: " + enable);
        	intfControl.enableWiFiInterface(enable);
        }
		public synchronized void set3GEnable(boolean enable) throws RemoteException {
			broadcastInfo(" 3G is set: " + enable);
			intfControl.enable3GInterface(enable);
		}
        public void basicTypes(int anInt, long aLong, boolean aBoolean,
            float aFloat, double aDouble, String aString) {
            // Does nothing
        }
        public void registerCallback(IRemoteServiceCallback cb, String device) {
        	Log.d(TAG, "register tracker : " + device);
        	if (cb == null || device == null) return;
        	mCallbacks.register(cb, device);
        	TrackerHandler tracker = mIntfaceTrackers.containsKey(device)? 
        			mIntfaceTrackers.get(device) : new TrackerHandler();
        	mIntfaceTrackers.put(device, tracker.join(device));	
        	broadcastInfo("register cb : " + device);
        }
        public void unregisterCallback(IRemoteServiceCallback cb, String device) {
        	if (cb == null) return;
        	broadcastInfo("unregister tracker : " + device);
        	mCallbacks.unregister(cb);
        	Log.d(TAG, "unregister cb : " + device);
        	if (mIntfaceTrackers.containsKey(device)) {
        		TrackerHandler tracker = mIntfaceTrackers.get(device);
        		mIntfaceTrackers.put(device, tracker.leave());
        	}
        	
        }
    };
	
    @Override
    public void onCreate() {
    	Log.d(TAG, "service is created");
    	//new Thread(this).start();
    }
    
    @Override
    public void onDestroy() {
    	Log.d(TAG, "service is destroyed");
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "service is binded");
        broadcastInfo("service is binded");
        return mBinder;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
    	Log.d(TAG, "service is unbinded");
    	broadcastInfo("service is unbinded");
        return true;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.d(TAG, "Received start id " + startId + ": " + intent);
    	intfControl = new InterfaceControl(InterfaceService.this);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    private void broadcastInfo(String info) {
    	final int N = mCallbacks.beginBroadcast();
    	for (int i = 0; i < N; i++) {
    		try {
				mCallbacks.getBroadcastItem(i).updateServiceInfo("From Service:" + info);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	mCallbacks.finishBroadcast();
    }
    
    private class TrackerHandler {
    	private int mCallers = 0;
    	private Thread mTracker = null;
    	
    	public TrackerHandler join(String device) {
    		mCallers += 1;
    		if (mTracker == null || !mTracker.isAlive()) {
    			mTracker = new Thread(new InterfaceTracker(mMsgHandler, device));
    			mTracker.start();
    		}
    		return TrackerHandler.this;
    	}
    	
    	public TrackerHandler leave() {
    		mCallers -= 1;
    		if (mCallers == 0) {
    			mTracker.interrupt();
    			mTracker = null;
    		}
    		return TrackerHandler.this;
    	}
    	
    }
}