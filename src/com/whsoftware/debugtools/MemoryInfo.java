/**++/
	WareHouse Software 2014
/--**/
package com.whsoftware.debugtools;

public class MemoryInfo {
	
	public static final int KILOBYTE = 1024;
	public static final int MEGABYTE = 1024 * 1024;
	public static final int GIGABYTE = 1024 * 1024 * 1024;
	
	public MemoryInfo()
	{
		
	}
	
	private static long memUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();	//Memory being used by the JVM
	private static long memFree = Runtime.getRuntime().freeMemory();	//Free memory available
	private static int x86Available = Runtime.getRuntime().availableProcessors();	//Amount of processors(cores) the system contains.
}
