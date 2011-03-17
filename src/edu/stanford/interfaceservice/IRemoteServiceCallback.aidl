package edu.stanford.interfaceservice;

oneway interface IRemoteServiceCallback {
    /** notify wifi/3G interface state change
    * state is shown as string "Connected", "Disconnected", "AsDefaultRoute"
    */
    void InterfaceStateChanged(String device, String oldState, String newState);
    
    /** send info message to the client
    * state is shown as string "Connected", "Disconnected", "AsDefaultRoute"
    */
    void updateServiceInfo(String info);
    
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}