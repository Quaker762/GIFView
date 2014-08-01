/**++/
	WareHouse Software 2014
	
	GIFPanel to be attached to a JFrame. Paints and animates gif file.
/--**/
package com.whsoftware.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import com.whsoftware.IO.GIFIO;
import com.whsoftware.debugtools.ConsoleStatus;
import com.whsoftware.debugtools.Debugger;

@SuppressWarnings("serial")
public class GIFPanel extends JPanel implements KeyListener{
	
	public static GIFIO 	gio = new GIFIO(); //Make sure there's only one instance of the GIF IO;
	public static GifSlider slider;
	public ConsoleStatus	status; 
	
	private int 			fCount;	//Frame counter variable. Keeps count of the frame position
	private int 			width;
	private int 			height;	
	private int 			delay;
	private int 			sliderVal; //Slider value
	private Debugger		debug;	//Debugger
	
	/**
	 * Class Constructor
	 */
	public GIFPanel()
	{
		super();
		debug = gio.debug;
		width = gio.getWidth();
		height = gio.getHeight();
		fCount = 0;
		setSize(width, height);
		slider = new GifSlider();
		status = GIFIO.status;
		setOpaque(false);
		addKeyListener(this);
		setFocusable(true);
	}
	
	/**
	 * Paints the component.
	 * Refreshes according to the delay time of each individual .gif file.
	 */
	public void paintComponent(Graphics g) //TODO: Start this on a separate thread if possible, so we don't bog down general Swing and AWT stuff
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		if(gio.initSuccess() == true)  //Check to make sure the program has initialised succesfully
		{
			requestFocusInWindow();		//Weird hack to fix the pause button not working 
			delay = gio.getDelay();		//Probably a bad idea, but initialise delay here so it can be modified.
			sliderVal = slider.getValue();
			//Clamp fCount and the slider value so it can't go below or out of bounds.
			if(fCount < 0)
				fCount = 0;
			if(fCount > gio.getFrameCountTrue())
				fCount = gio.getFrameCountTrue();
			
			if(slider.getValueIsAdjusting() && gio.paused() == false) //Pause if we detect the slider value is adjusting.
				gio.setPaused(true);
			
			//Check if the gif is paused.
			if(gio.paused() == false)
			{
				BufferedImage temp = gio.getImageAt(sliderVal);
				g.drawImage(temp, 0, 0, null);
				slider.setValue(fCount);
				fCount++;
			}
			else //The gif is paused!
			{
				BufferedImage temp = gio.getImageAt(sliderVal);
				//fCount = slider.getValue(); Breaks pausing..
				g.drawImage(temp, 0, 0, null);
			}
			
			//Loop the gif
			if(fCount > gio.getFrameCountTrue() && gio.shouldLoop())
				fCount = 0;
			//Sleep the thread, thereby setting the animation delay (TODO this should be another thread, not the AWT one!)
			try 
			{
				//Only sleep the thread if we are running the GIF
				if(gio.paused() == false)
				{
					Thread.sleep(gio.getFramerate());
					g.dispose();
				}
				else //Don't sleep the thread
				{
					g.dispose();
				}
				repaint();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
				debug.log("Thread interruped!" +  status.statusString(ConsoleStatus.Status.ERROR));
				debug.log(e);
			} 
			catch (InstantiationException e)
			{
				e.printStackTrace();
				debug.log("Instantiation Exception!" +  status.statusString(ConsoleStatus.Status.ERROR));
				debug.log(e);
			} 
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
				debug.log("Access Violation!" +  status.statusString(ConsoleStatus.Status.ERROR));
				debug.log(e);
			} 
			catch(IllegalArgumentException e)
			{
				e.printStackTrace();
				debug.log("IllegalArgumentException!:" + status.statusString(ConsoleStatus.Status.ERROR));
				debug.log(e);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				debug.log("Reading Metadata:" +  status.statusString(ConsoleStatus.Status.ERROR));
				debug.log(e);
			}
		}
		else
		{
			return; //Get me out of here
		}
	}
	
	/**
	 * Sets the frame position
	 * @param position
	 */
	public void setFramePos(int position)
	{
		fCount = position;
	}
	
	/**
	 * Get the current frame position
	 */
	public int getFramePos()
	{
		return fCount;
	}
	
	
	
	
	/**
	 * KeyListener for the panel
	 * P = Pause image
	 * L = Loop status
	 * Left = Rewind
	 * Right = Fast-Forward
	 */
	public void keyPressed(KeyEvent ev)
	{
		int key = ev.getKeyCode();
		
		if(key == KeyEvent.VK_P && gio.paused() == false)
			gio.setPaused(true);
		else if(key == KeyEvent.VK_P && gio.paused() == true)
			gio.setPaused(false);
		
		if(key == KeyEvent.VK_L && gio.shouldLoop() == true)
			gio.setLoop(false);
		else if(key == KeyEvent.VK_L && gio.shouldLoop() == false)
			gio.setLoop(true);
		
		if(key == KeyEvent.VK_LEFT)
		{
			fCount--;
			//TODO: This don't work, yo!
			Graphics g = getGraphics();
			BufferedImage temp = gio.getImageAt(fCount);
			g.drawImage(temp, 0, 0, null);
			repaint();
		}
		
		if(key == KeyEvent.VK_RIGHT) //Fast-Forward! 
		{
			fCount++;
			//TODO: This don't work, yo!
			Graphics g = getGraphics();
			BufferedImage temp = gio.getImageAt(fCount);
			g.drawImage(temp, 0, 0, null);
			repaint();
		}
		
		if(key == KeyEvent.VK_D)
			debug.setVisible();
	}

	public void keyReleased(KeyEvent ev)
	{
		
	}
	public void keyTyped(KeyEvent ev)
	{
		
	}	
}