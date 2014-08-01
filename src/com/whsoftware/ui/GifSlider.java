/**++/
	WareHouse Software 2014
/--**/
package com.whsoftware.ui;

import javax.swing.JSlider;

import com.whsoftware.IO.GIFIO;

@SuppressWarnings("serial")
public class GifSlider extends JSlider {
	
	private static GIFIO gio = GIFPanel.gio;
	
	public GifSlider()
	{
		super();
		setPaintTicks(true);
		setPaintLabels(true);
	}
	
	/**
	 * Clamp values of the slider to their minimum and maximum values.
	 * @param min
	 * @param max
	 */
	public void clamp(int min, int max)
	{
		setMinimum(min);
		setMaximum(max);
		setMajorTickSpacing(10);
	}
}
