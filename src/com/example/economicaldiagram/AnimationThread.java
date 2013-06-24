package com.example.economicaldiagram;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Thread that handles all the animation of custom UI elements
 * 
 * @author careful7j
 * @version 1.00, 13.06.2013
 * @since android 2.3.3
 *
 */
class AnimationThread extends Thread {	

    /** Thread finishing flag. While true animation thread keeps drawing UI elements */
    private boolean _run = false;
	/** Element that holds diagramView surface */
    protected SurfaceHolder diagramSurfaceHolder;
	/** Element that holds animButton surface */
    protected SurfaceHolder animButtonSurfaceHolder;
	/** Element that holds buttonPanel surface  */
    protected SurfaceHolder buttonPanelSurfaceHolder;
	/** Reference on DiagramView UI element */    
	protected DiagramView diagramView;
	/** Reference on AnimButton UI element */    
	protected AnimButton animButton;
	/** Reference on ButtonPanel UI element */    
	protected ButtonPanel buttonPanel;
	/** Controller that animates DiagramView and handles all the touch events */
	protected DiagramViewController diagramController = new DiagramViewController();
	/** Controller that animates AnimButton and handles all the touch events */
	protected AnimButtonController animButtonController = new AnimButtonController();
	/** Controller that animates ButtonPanel and handles all the touch events */
	protected ButtonPanelController buttonPanelController = new ButtonPanelController();
	/** Timeout between surfaces to redraw. 34ms is equal to 29 frames per second */
    final int FRAMES_TIMEOUT = 34;	    
    /** Default custom UI element sizes acting before real are fetched from XML */
    final int DEFAULT_SIZE = 100;
    /** If custom UI elements already are initialized */    	
	protected boolean isInitialized = false;
	
	/**
	 * Sets diagramView and it's surfaceHolder
	 * @param surfaceHolder Element that holds DiagramView surface
	 * @param diagramView Reference on DiagramView UI element
	 */
    protected void setDiagram( 	SurfaceHolder surfaceHolder, DiagramView diagramView ) {
		diagramSurfaceHolder = surfaceHolder;
		this.diagramView = diagramView;
    }
    
	/**
	 * Sets animButton and it's surfaceHolder
	 * 
	 * @param surfaceHolder Element that holds AnimButton surface
	 * @param animButton Reference on AnimButton UI element
	 */
    protected void setAnimButton( 	SurfaceHolder surfaceHolder, AnimButton animButton ) {
		animButtonSurfaceHolder = surfaceHolder;
		this.animButton = animButton;
    }
    
	/**
	 * Sets buttonPanel and it's surfaceHolder
	 * 
	 * @param surfaceHolder Element that holds ButtonPanel surface
	 * @param buttonPanel Reference on ButtonPanel UI element
	 */
    protected void setButtonPanel( 	SurfaceHolder surfaceHolder, ButtonPanel buttonPanel ) {
		buttonPanelSurfaceHolder = surfaceHolder;
		this.buttonPanel = buttonPanel;
    }
    
    /**
     * Starts and finishes Animation Thread
     * @param run Is thread running
     */
    public void setRunning(boolean run) {
        _run = run;
    }    
    
    @Override
    /** Animates Custom UI elements */
    public void run() {
        Canvas diagramCanvas;
        Canvas animButtonCanvas;
        Canvas buttonPanelCanvas;
        while ( _run ) {
            diagramCanvas = null;
            animButtonCanvas = null;
            buttonPanelCanvas = null;
            try {
                diagramCanvas = diagramSurfaceHolder.lockCanvas(null); 
                animButtonCanvas = animButtonSurfaceHolder.lockCanvas(null);
                buttonPanelCanvas = buttonPanelSurfaceHolder.lockCanvas(null);
                checkInitialization();
                // possibly not the best solution but this runs just fine
                synchronized ( diagramSurfaceHolder ) {                	
                	diagramController.animate( diagramCanvas );
                    synchronized ( animButtonSurfaceHolder ) {
                       	animButtonController.animate( animButtonCanvas );
                        synchronized ( buttonPanelSurfaceHolder ) {
                           	buttonPanelController.animate( buttonPanelCanvas );
                        }
                    }
                }
            } finally {
                if ( diagramCanvas != null ) {
                	diagramSurfaceHolder.unlockCanvasAndPost( diagramCanvas );
                }
                if ( animButtonCanvas != null ) {
                	animButtonSurfaceHolder.unlockCanvasAndPost( animButtonCanvas );
                }
                if ( buttonPanelCanvas != null ) {
                	buttonPanelSurfaceHolder.unlockCanvasAndPost( buttonPanelCanvas );
                }
            }
        standBy( FRAMES_TIMEOUT );            
        }
    }   
    
    /**
     * Sleeps the animation thread to reduce CPU usage
     * 
     * @param How long to sleep before UI elements to redraw.
     */
    private void standBy( int timeout ) {
    	try {
			Thread.sleep( timeout ); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    /** Checks if Custom UI elements are initialized and initializes them if necessary */
    protected void checkInitialization() {
        if ( !isInitialized ) {
        	diagramController.reInitialize( diagramView,
											diagramView.height,
											diagramView.width );        	
        	animButtonController.reInitialize( 	animButton,
												animButton.height,
												animButton.width ); 
        	buttonPanelController.reInitialize( buttonPanel,
												buttonPanel.height,
												buttonPanel.width );
        	isInitialized = true;
        }
    }    
 	
}
