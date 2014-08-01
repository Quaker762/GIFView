/**++/
	WareHouse Software 2014
	
	DEPRECATED!!! This won't work now
/--**/
package com.whsoftware;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

import com.whsoftware.IO.GIFIO;
import com.whsoftware.ui.GIFPanel;
import com.whsoftware.ui.GifButton;
import com.whsoftware.ui.GifSlider;

public class MainTest {
	
	private GIFIO				gio;
	private static GifSlider 	slider;
	private static GifButton	play;
	private static JMenuBar		menu;
	private static JRadioButtonMenuItem	loopItem;
	
	private static JMenu fileMenu;
	
	public MainTest()
	{
		gio = GIFPanel.gio;
		slider = GIFPanel.slider;
		play = new GifButton();
		play.setText(">");
		slider.clamp(0, gio.getFrameCountTrue());
		
		menu = new JMenuBar();
		fileMenu = new JMenu("IO");
		loopItem = new JRadioButtonMenuItem("Loop");
		
		setActionListener();
	}
	
	private void setActionListener()
	{
		if(gio.shouldLoop())
			loopItem.setSelected(true);
		else
			loopItem.setSelected(false);
		
		loopItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				if(loopItem.isSelected())
					gio.setLoop(true);
				else
					gio.setLoop(false);
			}
		});
	}
	
	//Horrible, test endless loop for debug console.
	private void getInput()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String comm;
		
		try
		{
			comm = br.readLine();
			
			if(comm.equals("pause"))
			{
				gio.setPaused(true);
			}
			
			if(comm.equals("unpause"))
			{
				gio.setPaused(false);
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Basic funtion
	private void checkInput()
	{
		while(true)
		{
			getInput();
		}
	}
	
	//Test entry point
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Test Frame");
		GIFPanel panel = new GIFPanel();
		MainTest _instance = new MainTest();
		frame.getContentPane().add(panel);
		frame.setSize(panel.getWidth() + 16, panel.getHeight() + 88); //Hack!! Weird frame issue bug... AGAIN!
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(slider, BorderLayout.SOUTH);
		frame.add(menu, BorderLayout.NORTH);
		
		menu.add(fileMenu);
		fileMenu.add(loopItem);
		_instance.checkInput();
	}
}
