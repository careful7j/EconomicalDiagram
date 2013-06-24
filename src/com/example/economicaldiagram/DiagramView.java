package com.example.economicaldiagram;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Custom UI element DiagramView. User can rotate diagram and extract it parts out
 * to the right. Diagram sectors sizes can be changed dynamically and can be 
 * returned back. Number of sectors can be any while one sector size 
 * can't be less than 0.5% of diagram size. Full diagram size should be 100%.
 * DiagramView takes it's data from Info class. 
 *
 * @author careful7j
 * @version 1.00, 13.06.2013
 * @since android 2.3.3
 *
 */
class DiagramView extends SurfaceView implements SurfaceHolder.Callback{
	
	/** Thread that animates this DiagramView */
    AnimationThread animationThread;
    /** Diagram Y size */
    protected int height;
    /** Diagram X size */
    protected int width;
 
    /**
     * Default surfaceView constructor also starts Animation Thread 
     * that will animate all the custom UI elements of MainActivity of this application.
     * 
     * @param context Application context
     * @param attrs Attributes from XML
     */
    public DiagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback( this );
        animationThread = new AnimationThread();
        animationThread.setDiagram( getHolder(), this );
        setFocusable(true);
    }
    
    /**
     * Fetches the animation thread.
     * @return animation thread
     */
    protected AnimationThread getThread() {
		return animationThread;
    }
  
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }
 
    @Override
    /** Restarts animation when application is restored from hidden state */
    public void surfaceCreated(SurfaceHolder holder) {
    	//============Saving state========================================
    	AnimButton animButton = animationThread.animButton;
    	ButtonPanel buttonPanel = animationThread.buttonPanel;
    	DiagramViewController diagramController = animationThread.diagramController;
    	AnimButtonController animButtonController = animationThread.animButtonController;
    	ButtonPanelController buttonPanelController = animationThread.buttonPanelController;
    	//===========Loading state========================================
    	animationThread = new AnimationThread();
    	animationThread.setDiagram( getHolder(), this );
    	animationThread.setAnimButton( animButton.getHolder(), animButton );
    	animationThread.setButtonPanel( buttonPanel.getHolder(), buttonPanel );
    	MainActivity.animationThread = animationThread;
    	animationThread.diagramController = diagramController;
    	animationThread.animButtonController = animButtonController;
    	animationThread.buttonPanelController = buttonPanelController;
    	animationThread.isInitialized = false;
    	//===========Running======================================
    	animationThread.setRunning(true);
    	animationThread.start();
    }
 
    @Override
    /** Joins the animation thread when diagram surface is destroyed */
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        animationThread.setRunning(false);
        while (retry) {
            try {
                animationThread.join();
                retry = false;
            } catch (InterruptedException e) { }
        }
    }
    
    @Override
    /** Initializes the diagramView with it's actual sizes */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
	    height = MeasureSpec.getSize(heightMeasureSpec);	    
	    animationThread.diagramController.reInitialize( this, height, width );
	}
    
    @Override
    /** Passes all the touches to diagramViewController to handle it */
    public boolean onTouchEvent(MotionEvent event) {        
        animationThread.diagramController.getTouch( event );
        return true;
    }
    
    /**
     * Launches the task that loads text in right side of the screen
     * @param pageId Id of text to load
     */
    protected void diagramSelected( int pageId ) {
     MainActivity.loadText( pageId );
    }
}
