/**++/
	WareHouse Software 2014
/--**/
package com.whsoftware.debugtools;

public class ConsoleStatus {
	
	
	
	public ConsoleStatus()
	{
		
	}
	
	public enum Status
	{
		OK, WARNING, ERROR;
	}
	
	public String statusString(Status status)
	{
		if(status == Status.OK)
			return "	[OK]";
		
		if(status == Status.WARNING)
			return "	[WARN]";
		
		if(status == Status.ERROR)
			return "	[ERR]";
		
		return "Invalid Status!" + status;
	}
}
