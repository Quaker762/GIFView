/**++/
	WareHouse Software 2014
/--**/
package com.whsoftware.debugtools;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Debugger {
	
	private JDialog 		debugDialog;
	private JTextArea 		debugArea;
	private JScrollPane 	sPane;
	
	public Debugger()
	{
		debugDialog = new JDialog();
		debugArea = new JTextArea();
		sPane = new JScrollPane(debugArea);
		debugDialog.setSize(400, 200);
		debugDialog.setTitle("Debug Console");
		initUI();
	}
	
	/**
	 * Initialise the UI
	 */
	private void initUI()
	{
		debugArea.setBackground(Color.BLACK);
		debugArea.setForeground(Color.GREEN);
		debugArea.setFont(new Font("Courier New", 12, 12));
		debugArea.setEditable(false);
		debugDialog.add(sPane);
	}
	
	/**
	 * Log an object to the console
	 * @param obj
	 */
	public void log(Object obj)
	{
		debugArea.append(obj.toString() + "\n");
		System.out.println(obj.toString());
	}
	
	/**
	 * Log a String to the console
	 * @param string
	 */
	public void log(String string)
	{
		debugArea.append(string + "\n");
		System.out.println(string);
	}
	
	/**
	 * Set the debug Window Visible
	 */
	public void setVisible()
	{
		debugDialog.setVisible(true);
	}
	
	/**
	 * Checks whether the JVM is executing the program in debug mode.
	 * @return boolean JVM debug mode checker
	 */
	public static boolean isProgDebug()
	{
		return java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("jdwp") >= 0;
	}
}
