package com.example.economicaldiagram;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * Common methods for each surfaceView inherited element's controller.
 * 
 * @author careful7j
 * @version 1.00, 23.06.2013
 * @since android 2.3.3
 *
 */
interface IControllerUI {
	/**
	 * Redraws custom UI element parts in turn
	 * 
	 * @param canvas Where to draw
	 */
	void animate(Canvas canvas);
	/** 
	 * Initializes and reinitializes custom UI elements 
	 * 
	 * @param surfaceView UI element that is reinitialized
	 * @param surfaceHeight size Y of custom UI element
	 * @param surfaceWidth size X of custom UI element
	 */
	void reInitialize(SurfaceView surfaceView, int surfaceHeight,
			int surfaceWidth);
	/** 
	 * Handles the user's touches
	 * 
	 * @param event Touch event
	 */
	void getTouch(MotionEvent event);
}
