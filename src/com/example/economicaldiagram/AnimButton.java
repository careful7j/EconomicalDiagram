package com.example.economicaldiagram;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Custom UI element Animated Button. To press it, user have to touch it and move
 * his finger from right to the left. Button has a sliding animation that plays periodically.
 * 
 * @author careful7j
 * @version 1.00, 13.06.2013
 * @since android 2.3.3
 *
 */
class AnimButton extends SurfaceView implements SurfaceHolder.Callback{
	
	/** Thread that animates this button */
    AnimationThread animationThread;
    /** Button Y size */
    protected int height;
    /** Button X size */
    protected int width;
    
    /**
     * Default Constructor inherited from surfaceView 
     * 
     * @param context Application context
     * @param attrs Attributes from XML
     */
    public AnimButton(Context context, AttributeSet attrs) {
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
        animationThread.setAnimButton( getHolder(), this );
    }
    
    /** Calls the Main activity to handle button-press action */
    protected void buttonClicked() {
    	MainActivity.returnSectorToDiagram();
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
    
    /** Initializes the button with it's actual sizes */
    @Override    
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
	    height = MeasureSpec.getSize(heightMeasureSpec);	    
	    animationThread.animButtonController.reInitialize( this, height, width );
	}
    
    /** Passes all the touches to animButtonController to handle it */
    @Override    
    public boolean onTouchEvent(MotionEvent event) {        
        animationThread.animButtonController.getTouch( event );
        return true;
    }
    
}
