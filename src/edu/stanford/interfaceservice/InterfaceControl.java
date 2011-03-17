package edu.stanford.interfaceservice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;


/**
 * The Service which tracks the interface state and
 * manipulate interfaces
 *
 * InterfaceControl class provides the IPC to 
 * shutdown/bring up wifi and g3 interfaces
 * 
 * @author Yongqiang Liu (liuyq7809@gmail.com)
 *
 */

public class InterfaceControl {
	final static String TAG = "InterfaceControl";
	private Service context = null;
	
	public InterfaceControl(Service context) {
		this.context = context;
	}
	
	public void enableWiFiInterface(boolean enable) {
    	WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	if (wifiManager.isWifiEnabled() != enable) {
    			wifiManager.setWifiEnabled(enable);
    	}	
    }
    
    public void enable3GInterface(boolean enable) {
		if (getMobileDataEnabled() != enable) {
			setMobileDataEnabled(enable);
		}
    }
    
    private Method getMethodFromClass(Object obj, String methodName) {
    	final String TAG = "getMethodFromClass";
        Class<?> whichClass = null;
        try {
            whichClass = Class.forName(obj.getClass().getName());
        } catch (ClassNotFoundException e2) {
            // TODO Auto-generated catch block
            Log.d(TAG, "class not found");
        }
        
        Method method = null;
        try {
            //method = whichClass.getDeclaredMethod(methodName);
            Method[] methods = whichClass.getDeclaredMethods();
        	for (Method m : methods) {
        		//Log.d(TAG, "method: " + m.getName());
        		if (m.getName().contains(methodName)) {
        			method = m;
        		}
        	}
        } catch (SecurityException e2) {
            // TODO Auto-generated catch block
        	Log.d(TAG, "SecurityException for " + methodName);
        } 
        return method;
    }
    
    private Object runMethodofClass(Object obj, Method method, Object... argv) {
    	Object result = null;
    	if (method == null) return result;
    	method.setAccessible(true);
        try {
			result = method.invoke(obj, argv);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "IllegalArgumentException for " + method.getName());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "IllegalAccessException for " + method.getName());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "InvocationTargetException for " + method.getName() 
					+ "; Reason: " + e.getLocalizedMessage());
		}
		return result;
    }
    
    private boolean getMobileDataEnabled() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        Method m = getMethodFromClass(cm, "getMobileDataEnabled");
        Object enabled = runMethodofClass(cm, m);
		return (Boolean) enabled;
    }
    
    private void setMobileDataEnabled(boolean enable) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        Method m = getMethodFromClass(cm, "setMobileDataEnabled");
        runMethodofClass(cm, m, enable);
    }	
}