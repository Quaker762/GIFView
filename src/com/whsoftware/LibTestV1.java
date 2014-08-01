/**++/
	WareHouse Software 2014
	
	This is a proper test of the library.
/--**/
package com.whsoftware;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable;

import com.whsoftware.IO.GIFIO;
import com.whsoftware.debugtools.ConsoleStatus;
import com.whsoftware.debugtools.Debugger;
import com.whsoftware.ui.GIFPanel;
import com.whsoftware.ui.GifSlider;

public class LibTestV1 {
	
	private GIFIO 					gio;
	private GIFPanel				panel;
	private GifSlider 				slider;
	private JMenuBar				menu;
	private JRadioButtonMenuItem	loopItem;
	private JRadioButtonMenuItem	pauseItem;
	private JDialog					gifInfoMenu;
	
	private JFrame					frame;
	private JMenu 					fileMenu;
	private JMenuItem 				loadFile;
	private JMenuItem				info;
	private JMenuItem 				exit;
	private Debugger				debug;
	
	public LibTestV1()
	{
		gio = GIFPanel.gio;	//This is the correct instance of gio that must be used ALWAYS!
		panel = new GIFPanel();
		slider = GIFPanel.slider;
		
		gifInfoMenu = new JDialog();
		
		menu = new JMenuBar();
		fileMenu = new JMenu("IO");
		loadFile = new JMenuItem("Load GIF");
		loopItem = new JRadioButtonMenuItem("Loop");
		pauseItem = new JRadioButtonMenuItem("Pause");
		info = new JMenuItem("Gif Info");
		exit = new JMenuItem("Exit");
		debug = gio.debug;
		
		if(Debugger.isProgDebug())
		{
			debug.log("Program(JVM) is in Debug Mode!");
			debug.log("==============================");
		}
		
		setActionListener();
	}
	
	private void setActionListener()
	{
		loopItem.setSelected(gio.shouldLoop());
		pauseItem.setSelected(gio.paused());
		
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
		
		pauseItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				if(pauseItem.isSelected())
					gio.setPaused(true);
				else
					gio.setPaused(false);
			}
		});
		
		info.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				/**
				JTextArea area = new JTextArea();
				area.setText
				(
				"Gif Size on Disk: " + gio.getFile().length() / 1024 +  "KB\n" +
				"Gif Framerate : " + gio.getFrameCountTrue() + "\n" +
				"Gif Frame Delay: " + gio.getDelay() + "\n" + 
				"Gif Width:" + gio.getWidth() +  "\n" + 
				"Gif Height:" + gio.getHeight()
				);
				area.setEditable(false);
				gifInfoMenu.add(area);
				gifInfoMenu.setTitle("Image Info");
				gifInfoMenu.pack();
				gifInfoMenu.setVisible(true);
				**/
				String[] columns = {"Attribute", "Value"};
				
				Object[][] data = {	{"Gif Size on Disk: ", gio.getFile().length() / 1024},
								   	{"Gif Framerate : " , (Object)gio.getFrameCountTrue()},
									{"Gif Frame Delay: " + gio.getDelay()},
									{"Gif Width:" + gio.getWidth()},
									{"Gif Height:" + gio.getHeight()}		
									};
				
				JTable table = new JTable(data, columns);
				gifInfoMenu.add(table);
				gifInfoMenu.pack();
				gifInfoMenu.setVisible(true);
			}
		});
		
		loadFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				gio.setPaused(true); //Pause here so we're not sleeping the AWT thread.
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(panel);
				try
				{
					gio.fOpen(fc.getSelectedFile().getAbsolutePath());
					gio.read();		//Should this be added to fOpen, or should we give the programmer a choice when and where to call it?
				} 
				catch (IOException ex)
				{
					ex.printStackTrace();
					System.err.println("Reading file:" + gio.status.statusString(ConsoleStatus.Status.ERROR));
					System.err.println(ex.toString());
					debug.log("Reading file:" + gio.status.statusString(ConsoleStatus.Status.ERROR));
					debug.log(ex);
				} 
				catch (InstantiationException ex)
				{
					// TODO Auto-generated catch block
					ex.printStackTrace();
					System.err.println("Reading file:" + gio.status.statusString(ConsoleStatus.Status.ERROR));
					System.err.println(ex.toString());
					debug.log("Reading file:" + gio.status.statusString(ConsoleStatus.Status.ERROR));
					debug.log(ex);
				} 
				catch (IllegalAccessException ex)
				{
					ex.printStackTrace();
					System.err.println("Reading file:" + gio.status.statusString(ConsoleStatus.Status.ERROR));
					System.err.println(ex.toString());
					debug.log("Reading file:" + gio.status.statusString(ConsoleStatus.Status.ERROR));
					debug.log(ex);
				} 
				catch(Exception ex)
				{
					System.err.println("Reading file:" + gio.status.statusString(ConsoleStatus.Status.ERROR));
					System.err.println(ex.toString());
					debug.log("Reading file:" + gio.status.statusString(ConsoleStatus.Status.ERROR));
					debug.log(ex);
				}
				slider.clamp(0, gio.getFrameCountTrue()); //Clamp slider values.
				frame.setSize(gio.getImageAt(0).getWidth() + 7, gio.getImageAt(0).getHeight() + 88); //Hack!! Weird frame issue bug... AGAIN!
				frame.setLocationRelativeTo(null);
				gio.setPaused(false);
				frame.setTitle(gio.getFile().getName());
				System.gc(); //Should we GC here? I suppose we should as we are always creating a new JFileChooser
			}
		});
		
		exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				System.exit(0);
			}
		});
	}
	
	//Program initialisation routine
	private void init()
	{
		frame = new JFrame("Test prog");
		frame.getContentPane().add(panel);
		frame.setSize(400, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.getContentPane().add(slider, BorderLayout.SOUTH);
		frame.add(menu, BorderLayout.NORTH);
	
		menu.add(fileMenu);
		fileMenu.add(loadFile);
		fileMenu.add(pauseItem);
		fileMenu.add(loopItem);
		fileMenu.add(info);
		fileMenu.add(exit);
	}
	
	//Program entry point
	public static void main(String[] args)
	{
		LibTestV1 _instance = new LibTestV1();
		try
		{
			_instance.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			GIFIO.debug.log("Initialisation Exception!");
			GIFIO.debug.log(ex);
		}
	}
}