package com.android.threadmethod;

public interface InterfaceThread {
	public void logicGet(String codeNumber) throws Exception;
	public void checkMessage(int id) throws Exception;
	public void ask(String seatId,String name,String codeNumber, String state) throws Exception;
	public void requestQueue(String codeNumber) throws Exception;
	public void requestNuresNumberFromSer(String codeNumber,String seatId) throws Exception;
}
