/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/leo/workspace/InterfaceService/src/edu/stanford/interfaceservice/IRemoteServiceCallback.aidl
 */
package edu.stanford.interfaceservice;
public interface IRemoteServiceCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements edu.stanford.interfaceservice.IRemoteServiceCallback
{
private static final java.lang.String DESCRIPTOR = "edu.stanford.interfaceservice.IRemoteServiceCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an edu.stanford.interfaceservice.IRemoteServiceCallback interface,
 * generating a proxy if needed.
 */
public static edu.stanford.interfaceservice.IRemoteServiceCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof edu.stanford.interfaceservice.IRemoteServiceCallback))) {
return ((edu.stanford.interfaceservice.IRemoteServiceCallback)iin);
}
return new edu.stanford.interfaceservice.IRemoteServiceCallback.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_InterfaceStateChanged:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.InterfaceStateChanged(_arg0, _arg1, _arg2);
return true;
}
case TRANSACTION_updateServiceInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.updateServiceInfo(_arg0);
return true;
}
case TRANSACTION_basicTypes:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
long _arg1;
_arg1 = data.readLong();
boolean _arg2;
_arg2 = (0!=data.readInt());
float _arg3;
_arg3 = data.readFloat();
double _arg4;
_arg4 = data.readDouble();
java.lang.String _arg5;
_arg5 = data.readString();
this.basicTypes(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements edu.stanford.interfaceservice.IRemoteServiceCallback
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/** notify wifi/3G interface state change
    * state is shown as string "Connected", "Disconnected", "AsDefaultRoute"
    */
public void InterfaceStateChanged(java.lang.String device, java.lang.String oldState, java.lang.String newState) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(device);
_data.writeString(oldState);
_data.writeString(newState);
mRemote.transact(Stub.TRANSACTION_InterfaceStateChanged, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
/** send info message to the client
    * state is shown as string "Connected", "Disconnected", "AsDefaultRoute"
    */
public void updateServiceInfo(java.lang.String info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(info);
mRemote.transact(Stub.TRANSACTION_updateServiceInfo, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, java.lang.String aString) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(anInt);
_data.writeLong(aLong);
_data.writeInt(((aBoolean)?(1):(0)));
_data.writeFloat(aFloat);
_data.writeDouble(aDouble);
_data.writeString(aString);
mRemote.transact(Stub.TRANSACTION_basicTypes, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_InterfaceStateChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_updateServiceInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_basicTypes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
/** notify wifi/3G interface state change
    * state is shown as string "Connected", "Disconnected", "AsDefaultRoute"
    */
public void InterfaceStateChanged(java.lang.String device, java.lang.String oldState, java.lang.String newState) throws android.os.RemoteException;
/** send info message to the client
    * state is shown as string "Connected", "Disconnected", "AsDefaultRoute"
    */
public void updateServiceInfo(java.lang.String info) throws android.os.RemoteException;
public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, java.lang.String aString) throws android.os.RemoteException;
}
