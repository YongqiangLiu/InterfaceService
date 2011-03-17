// IRemoteService.aidl
package edu.stanford.interfaceservice;

import edu.stanford.interfaceservice.IRemoteServiceCallback;

// Declare any non-default types here with import statements

interface IRemoteService {
    /** Request the process ID of this service, to do evil things with it. */
    int getPid();
    
    /** set WiFi enabled, shutdown/bring up*/
    void setWiFiEnable(boolean enable);
    
    /** set 3G enabled, shutdown/bring up*/
    void set3GEnable(boolean enable);    
    
    /** Register Callback Interface Handler*/
    void registerCallback(IRemoteServiceCallback cb, String device);  

    /** Unregister Callback Interface Handler*/
    void unregisterCallback(IRemoteServiceCallback cb, String device);  

    /** Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}