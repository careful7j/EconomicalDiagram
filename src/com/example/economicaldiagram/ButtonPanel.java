package com.example.economicaldiagram;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Custom UI element Button Panel. There is one radioGroup of 5 radioButtons and one
 * radioGroup of 2 arrow-like radioButtons inside that panel.
 * 
 * @author careful7j
 * @version 1.00, 13.06.2013
 * @since android 2.3.3
 *
 */
class ButtonPanel extends SurfaceView implements SurfaceHolder.Callback{
	
	/** Thread that animates this ButtonPanel */
    AnimationThread animationThread;
    /** ButtonPanel Y size */
    protected int height;
    /** ButtonPanel X size */
    protected int width;
    
    /**
     * Default Constructor inherited from surfaceView 
     * 
     * @param context Application context
     * @param attrs Attributes from XML
     */
    public ButtonPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback( this );
        setFocusable(true);
    }
    
    /**
     * Sets the thread that animates this button
     * 
     * @param animationThread thread that animates Custom UI elements
     */
    protected void setThread(	AnimationThread animationThread ) {
        this.animationThread = animationThread;
        animationThread.setButtonPanel( getHolder(), this );
    }
      
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }
 
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }
 
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    
    @Override
    /** Initializes the button panel with it's actual sizes */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
	    height = MeasureSpec.getSize(heightMeasureSpec);	    
	    animationThread.buttonPanelController.reInitialize( this, height, width );
	}
    
    @Override
    /** Passes all the touches to buttonPanelController to handle it */
    public boolean onTouchEvent(MotionEvent event) {        
        animationThread.buttonPanelController.getTouch( event );
        return true;
    }
    
}
