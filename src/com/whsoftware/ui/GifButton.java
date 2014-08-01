/**++/
	WareHouse Software 2014
/--**/
package com.whsoftware.ui;

import javax.swing.JButton;

import com.whsoftware.IO.GIFIO;
@SuppressWarnings("serial")
public class GifButton extends JButton{
	
	private GIFIO gio = GIFPanel.gio;
	private String name;

	public GifButton()
	{
		super();
	}
	
	public GifButton(String name)
	{
		this.name = name;
	}
	
	public String getButtonName()
	{
		return this.name;
	}
}
