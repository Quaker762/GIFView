/**++/
	WareHouse Software 2014
	
	GIFIO class. Handles all IO.
/--**/
package com.whsoftware.IO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import com.whsoftware.debugtools.ConsoleStatus;
import com.whsoftware.debugtools.Debugger;

public class GIFIO {
	
	private BufferedImage[] 		frames;
	private ImageReader 			reader;
	private ImageInputStream 		is;
	private File 					file;
	public static Debugger			debug;
	
	public static ConsoleStatus 	status;
	
	private int 					width; 
	private int						height;
	private int 					delay;
	private boolean 				loop = true;
	private boolean 				paused = false;
	private boolean					initSuccess = false;
	
	public GIFIO()
	{
		//Lets start a debugger and console with an instance of GIFIO
		status = new ConsoleStatus();
		debug  = new Debugger();
	}
	
	/**
	 * fOpen, opens the file for use. Called by constructor.
	 * This should be called before read() when loading a .gif
	 * @param String fName
	 */
	public void fOpen(String fname) throws IOException
	{
		//Placeholder.
		file = new File(fname);
		reader = ImageIO.getImageReadersBySuffix("GIF").next();
		is = ImageIO.createImageInputStream(new FileInputStream(file));
		reader.setInput(is);  
			
		//Width and height remain constant, so just grab it from the first frame in the index.
		width = reader.getWidth(0);
		height = reader.getHeight(0);
		reader.dispose();
		setDelay(90);	//Set the delay. A pretty bad hack
		debug.log("Loading Image:" + status.statusString(ConsoleStatus.Status.OK));
	}
	
	/**
	 *	Read the .gif file and store each frame into the frames[] array.
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void read() throws IOException, InstantiationException, IllegalAccessException
	{
		frames = new BufferedImage[reader.getNumImages(true)];
		
		for(int framen = 0; framen < reader.getNumImages(true); framen++)
			frames[framen] = reader.read(framen);
		
		debug.log("Loaded " + file.getName() + status.statusString(ConsoleStatus.Status.OK));
		debug.log("=============================");
		debug.log("GIF Frame Count = " + getFrameCountTrue());
		debug.log("Framerate = " + getFramerate() + status.statusString(ConsoleStatus.Status.OK));
		debug.log("Image Width = " + getWidth() + "\nImage Height = " + getHeight());
		for(int i = 0; i < getMetadataAttribs().length; i++)
			debug.log("Attrib:" + getMetadataAttribs()[i]);
		
		initSuccess(true);
	}
	
	/**
	 * Manually set position in the .gif file. 
	 * @param i position in the frames array
	 */
	public void setFramePosition(int i)
	{
		//TODO: Set position of frames at position i. 
	}
	
	/**
	 * Returns the number of frames in the .gif.
	 * @return frames[].length
	 */
	public int getFrameCount()
	{
		return frames.length;
	}
	
	/**
	 * Returns the number of frames in the .gif, including 0.
	 * @return frames[] length - 1
	 */
	public int getFrameCountTrue()
	{
		return frames.length - 1;
	}
	
	/**
	 * Return public, readable versions of the image array.
	 * @return BufferedImage at Index int i
	 */
	public BufferedImage getImageAt(int i)
	{
		return frames[i];
	}
	
	public BufferedImage[] getImage()
	{
		return frames;
	}
	
	/**
	 * Get the delay to wait between frames
	 * The delay is stored from 1 - 10 in a .gif's metadata, so we must times the result by 10!
	 * This is still really buggy, and only some .gifs are compatible.
	 * TODO Reasearch how to read delay properly.
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public int getFramerate() throws IOException, InstantiationException, IllegalAccessException
	{
		IIOMetadata meta = reader.getImageMetadata(0); //Read the Metadata from the first frame, seeing as it never changes.
		IIOMetadataNode metanode = (IIOMetadataNode)meta.getAsTree("javax_imageio_gif_image_1.0");	//Get the Metadata roots
		
		//Cycle through all nodes/branches in the metadata list...
		for(int i = 0; i < metanode.getChildNodes().getLength(); i++)
		{
			IIOMetadataNode subnode = (IIOMetadataNode)metanode.getChildNodes().item(i);
			if (!subnode.getNodeName().equals("GraphicControlExtension")) continue;
			
			if(Integer.parseInt(subnode.getAttribute("delayTime")) > 10)	//HACK HACK HACK HACK HACKITY HACK!
				return Integer.parseInt(subnode.getAttribute("delayTime")) ;
			else if(Integer.parseInt(subnode.getAttribute("delayTime")) <= 0)
				return 90;	//Return something that works
			else
				return Integer.parseInt(subnode.getAttribute("delayTime")) * 10; //Return the delayTime and times it by 10
		}
		return -1; //Return negative one, signalling some kind of error(Will return an InvalidArgumentException, because we can't sleep for -1ms);
	}
	
	/**
	 * Get all metadata attributes.
	 * @return all the metadata labels from the GraphicsControlExtension Branch
	 * @throws IOException
	 */
	public String[] getMetadataAttribs() throws IOException
	{
		//TODO: Get metadata from image!!!
		return reader.getImageMetadata(0).getMetadataFormat("javax_imageio_gif_image_1.0").getAttributeNames("GraphicControlExtension");
	}
	
	/**
	 * Return the file of this instance of GIFIO.
	 * @return file
	 */
	public File getFile()
	{
		return this.file;
	}
	
	/**
	 * Return width of image.
	 * @return width
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Return height of image.
	 * @return height
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Returns user set loop flag.
	 * (User option not implements TODO:)
	 * @return shouldLoop
	 */
	public boolean shouldLoop()
	{
		return loop;
	}
	
	/**
	 * Sets the state of loop.
	 * @param loop
	 */
	public void setLoop(boolean loop)
	{
		this.loop = loop;
	}
	
	public void setPaused(boolean paused)
	{
		if(paused == true)
			debug.log("Pausing Image!");
		else
			debug.log("Unpausing Image!");
		
		this.paused = paused;
	}
	
	/**
	 * Returns if the gif should pause.
	 * @return paused
	 */
	public boolean paused()
	{
		return paused;
	}
	
	/**
	 * Set init success
	 */
	public void initSuccess(boolean success)
	{
		initSuccess = success;
	}
	
	/**
	 * Return value of initSuccess
	 */
	public boolean initSuccess()
	{
		return initSuccess;
	}
	
	/**
	 * Set the delay of the image
	 */
	public void setDelay(int delayms)
	{
		delay = delayms;
	}
	
	/**
	 * Get delay time (in ms)
	 * @return delay
	 */
	public int getDelay()
	{
		return delay;
	}
}