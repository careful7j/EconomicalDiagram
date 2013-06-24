package com.example.economicaldiagram;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * Controller that draws ButtonPanel and handles it's touch events.
 * 
 * @author careful7j
 * @version 1.00, 23.06.2013
 * @since android 2.3.3
 *
 */
public class ButtonPanelController implements IControllerUI {
	
	/** Reference to controlled UI element */
	protected AnimationThread animationThread;
	/** Default sizes of the ButtonPanel. Used before actual are received */
    final private int DEFAULT_SIZE = 100;
    /** UI element X size */
    private int surfaceWidth = DEFAULT_SIZE;
    /** UI element Y size */
    private int surfaceHeight = DEFAULT_SIZE;
    /** Axis X id */
    final private int AXIS_X = 0;
    /** Axis Y id */
	final private int AXIS_Y = 1;
    /** Points to build one button X axis */
	private float[] xPoints = { 0 };
    /** Points to build one button Y axis */
	private float[] yPoints = { 0 };
    /** Points to build one arrow X axis */
	private float[] xArrows = { 0 };
    /** Points to build one arrow Y axis */
	private float[] yArrows = { 0 };
    /** Points to build Russian Federation flag X axis */
	private float[] xFlag = { 0 };
    /** Points to build Russian Federation flag Y axis */
	private float[] yFlag = { 0 };	
	
	/** Number of parts in ButtonPanel */
	final private int PARTS_IN_PANEL = 8;
	/** Number of year changing buttons in ButtonPanel*/
	final private int YEAR_BUTTONS = 5;
	/** Position of import button in ButtonPanel */
	final private int IMPORT_BUTTON = 6;
	/** Position of export button in ButtonPanel */
	final private int EXPORT_BUTTON = 7;
	/** Value to multiply years to get export information */
	private int DIRECTION_EXPORT = 1;
	/** Value to multiply years to get import information */
	private int DIRECTION_IMPORT = -1;
	/** Year selected now*/
	private int highlightedYear = DiagramViewController.IMPORT_2012;
	/** Direction selected now */
	protected int highlightedDirection = DIRECTION_IMPORT;
	/** Font size of buttons' titles */
	private final int BUTTONS_TEXT_SIZE = 14;
	
	/** Color of NOT active button background */
	private int colorBackground = Color.rgb( 43, 63, 90 );
	/** Color of active button background */
	private int colorHighlight = Color.rgb( 99, 140, 170 );
	/** Color of NOT active arrow background */
	private int colorArrowBackground = Color.rgb( 28, 31, 36 );
	/** Color of right arrow when it is active */
	private int colorArrowRight = Color.rgb( 0, 255, 0 );
	/** Color of left arrow when it is active */
	private int colorArrowLeft = Color.rgb( 255, 0, 0 );
	/** Color when arrow is NOT active */
	private int colorArrow = Color.rgb( 55, 00, 00 );
	/** Color of first stripe of Russian Federation flag */
	private int flagColorOne = Color.rgb( 255, 255, 255 );
	/** Color of second stripe of Russian Federation flag */
	private int flagColorTwo = Color.rgb( 0, 0, 255 );
	/** Color of third stripe of Russian Federation flag */
	private int flagColorThree = Color.rgb( 255, 0, 0 );
	/** Color of text on NOT active button */
	private int colorText = Color.rgb( 0, 0, 0 );
	/** Color of text on active button */
	private int colorTextHighlight = Color.rgb( 255, 255, 255 );
	/** First stripe of Russian Federation flag ends here */
	private int FLAG_LINE_ONE = 4;
	/** Second stripe of Russian Federation flag ends here */
	private int FLAG_LINE_TWO = 8;
	/** Third stripe of Russian Federation flag ends here */
	private int FLAG_LINE_THREE = 12;
	/** Size of one part of Button Panel */
	private int buttonPanelPartSize = 10;
	
	/** X of point user touches now */ 
	private float eventXnew = Short.MAX_VALUE;	
    /** Paint used to draw custom UI element */
    private Paint paint = new Paint();
    /** Array of points to build polygon */
    private Path polygonPath = new Path();
    /** Titles on buttons */
    final private String[] BUTTON_TITLES = { "2012", "2010", "2007", "2004", "2000" };
    	
    @Override
    public void reInitialize(	SurfaceView buttonPanel,
								int surfaceHeight,
								int surfaceWidth )
    {
		//this.buttonPanel = (ButtonPanel) buttonPanel;
		this.surfaceHeight = surfaceHeight;
		this.surfaceWidth = surfaceWidth;	
    	xPoints = computeButtonPoints( AXIS_X );        
    	yPoints = computeButtonPoints( AXIS_Y );
    	xArrows = computeArrows( AXIS_X );        
    	yArrows = computeArrows( AXIS_Y );
    	xFlag = computeFlag( AXIS_X );        
    	yFlag = computeFlag( AXIS_Y );
	}

    @Override
	public void animate( Canvas canvas ) {
		animButtons( canvas );
		animArrowsBackground( canvas );
		animArrows( canvas );
		animFlag( canvas );
	}
	
    /**
     * Draws buttons except arrows
     * @param canvas where to draw
     */
	private void animButtons( Canvas canvas ) {		
		for ( int buttonNext = 0; buttonNext < IMPORT_BUTTON+1; buttonNext++ ) {
			polygonPath.reset();
			polygonPath.moveTo( xPoints[ 0 ] + buttonPanelPartSize * buttonNext, yPoints[ 0 ] );                                    
	        for (	int pointNext = 1;
	        		pointNext < xPoints.length;
	        		pointNext++
	       		)
	        {
	        	polygonPath.lineTo(	xPoints[ pointNext ] + buttonPanelPartSize * buttonNext,
	        		yPoints[ pointNext ]);            	  
	        };
	        int activeButton = Math.abs( highlightedYear ) - 1;
	        if ( buttonNext == activeButton ) {
	        	paint.setColor( colorHighlight );
	        } else { 
	        	paint.setColor( colorBackground );
	        }
            paint.setStyle(Style.FILL_AND_STROKE);
            canvas.drawPath(polygonPath, paint);
            if (buttonNext > 4) continue;
            paint.setTextSize( BUTTONS_TEXT_SIZE );
	        if ( buttonNext == activeButton ) {
	        	paint.setColor( colorTextHighlight );
	        } else { 
	        	paint.setColor( colorText );
	        }
	        float textHeight = 0.67f;
	        float textWidth = 0.1f;
            canvas.drawText(	BUTTON_TITLES[ buttonNext ],
            					buttonPanelPartSize * buttonNext + textWidth * buttonPanelPartSize,
            					surfaceHeight * textHeight,
            					paint);  
		}        
	}
	
	/**
	 * Draws arrows backgrounds
	 * @param canvas where to draw
	 */
	private void animArrowsBackground( Canvas canvas ) {
		int buttonNext = IMPORT_BUTTON - 1;
		for( int index = 0; index < 2; index++ ) {
			polygonPath.reset();
			polygonPath.moveTo( 
					xPoints[ 0 ] + buttonPanelPartSize * buttonNext, yPoints[ 0 ] );                                    
		    for (	int pointNext = 1;
		    		pointNext < xPoints.length;
		    		pointNext++
		   		)
		    {
		    	polygonPath.lineTo(	xPoints[ pointNext ] + buttonPanelPartSize * buttonNext,
		    						yPoints[ pointNext ]);            	  
		    };
		    paint.setColor( colorArrowBackground );
		    paint.setStyle(Style.FILL_AND_STROKE);
		    canvas.drawPath(polygonPath, paint);
		    buttonNext = EXPORT_BUTTON;
	    }
	}
	
	/**
	 * Draws arrows
	 * @param canvas where to draw
	 */
	private void animArrows( Canvas canvas ) {
		int buttonNext = IMPORT_BUTTON - 1;
		polygonPath.reset();
		polygonPath.moveTo( xArrows[ 0 ] + buttonPanelPartSize * buttonNext, yArrows[ 0 ] );                                    
	    for (	int pointNext = 1;
	    		pointNext < xArrows.length;
	    		pointNext++
	   		)
	    {
	    	polygonPath.lineTo(	xArrows[ pointNext ] + buttonPanelPartSize * buttonNext,
	    		yArrows[ pointNext ]);            	  
	    };
	    if ( highlightedDirection == DIRECTION_IMPORT ) {
	    	paint.setColor( colorArrowLeft );
	    } else paint.setColor( colorArrow );	    
	    paint.setStyle(Style.FILL_AND_STROKE);
	    canvas.drawPath(polygonPath, paint);
	    
	    buttonNext = EXPORT_BUTTON;
	    
	    polygonPath.reset();
		polygonPath.moveTo( xArrows[ 0 ] + buttonPanelPartSize * buttonNext, yArrows[ 0 ] );                                    
	    for (	int pointNext = 1;
	    		pointNext < xArrows.length;
	    		pointNext++
	   		)
	    {
	    	polygonPath.lineTo(	xArrows[ pointNext ] + buttonPanelPartSize * buttonNext,
	    		yArrows[ pointNext ]);            	  
	    };
	    if ( highlightedDirection == DIRECTION_EXPORT ) {
	    	paint.setColor( colorArrowRight );
	    } else paint.setColor( colorArrow );	    
	    paint.setStyle(Style.FILL_AND_STROKE);
	    canvas.drawPath(polygonPath, paint);
	}
	
	/**
	 * Draws the flag of Russian Federation
	 * @param canvas where to draw
	 */
	private void animFlag( Canvas canvas ) {
		int buttonNext = IMPORT_BUTTON;
		polygonPath.reset();
		polygonPath.moveTo( xFlag[ 0 ] + buttonPanelPartSize * buttonNext, yFlag[ 0 ] );                                    
	    for (	int pointNext = 1;
	    		pointNext < FLAG_LINE_ONE;
	    		pointNext++
	   		)
	    {
	    	polygonPath.lineTo(	xFlag[ pointNext ] + buttonPanelPartSize * buttonNext,
	    		yFlag[ pointNext ] );            	  
	    };
	    paint.setColor( flagColorOne );
	    paint.setStyle(Style.FILL_AND_STROKE);
	    canvas.drawPath(polygonPath, paint);
	    
	    polygonPath.reset();
		polygonPath.moveTo( xFlag[ FLAG_LINE_ONE ] +
							buttonPanelPartSize * buttonNext, yFlag[ FLAG_LINE_ONE ] );                                    
	    for (	int pointNext = FLAG_LINE_ONE + 1;
	    		pointNext < FLAG_LINE_TWO;
	    		pointNext++
	   		)
	    {
	    	polygonPath.lineTo(	xFlag[ pointNext ] + buttonPanelPartSize * buttonNext,
	    		yFlag[ pointNext ] );
	    };
	    paint.setColor( flagColorTwo );
	    paint.setStyle(Style.FILL_AND_STROKE);
	    canvas.drawPath(polygonPath, paint);
	    
	    polygonPath.reset();
		polygonPath.moveTo( xFlag[ FLAG_LINE_TWO ] +
							buttonPanelPartSize * buttonNext, yFlag[ FLAG_LINE_TWO ] );                                    
	    for (	int pointNext = FLAG_LINE_TWO + 1;
	    		pointNext < FLAG_LINE_THREE;
	    		pointNext++
	   		)
	    {
	    	polygonPath.lineTo(	xFlag[ pointNext ] + buttonPanelPartSize * buttonNext,
	    		yFlag[ pointNext ] );            	  
	    };
	    paint.setColor( flagColorThree );
	    paint.setStyle(Style.FILL_AND_STROKE);
	    canvas.drawPath(polygonPath, paint);
	}
	
	/**
	 * 
	 * Computes points of button shape polygon
	 * 
	 * @param AXIS For which axis to compute points
	 * @return Points of button shape polygon
	 */
	private float[] computeButtonPoints( final int AXIS ) {
		buttonPanelPartSize = surfaceWidth / PARTS_IN_PANEL;
		float[] xPoints = { 0, buttonPanelPartSize, buttonPanelPartSize, 0 };
		float[] yPoints = { 0, 0, surfaceHeight, surfaceHeight };
		if ( AXIS == AXIS_X ) return xPoints;
			else return yPoints;
	}	
	
	@Override
    public void getTouch( MotionEvent event ) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        	eventXnew = (int) event.getX();
        	if ( eventXnew < ( surfaceWidth / PARTS_IN_PANEL * YEAR_BUTTONS ) ) {
        		highlightedYear = (int) eventXnew / ( surfaceWidth / PARTS_IN_PANEL );
        		highlightedYear++;
        	} else {
        		if ( eventXnew <= ( surfaceWidth / PARTS_IN_PANEL * IMPORT_BUTTON ) ) {
        			highlightedDirection = DIRECTION_IMPORT;        			
        		} else {
	        		if ( eventXnew > ( surfaceWidth / PARTS_IN_PANEL * EXPORT_BUTTON ) ) {
	        			highlightedDirection = DIRECTION_EXPORT;        			
	        		}
        		}
        	}        	
        	if ( highlightedDirection == DIRECTION_EXPORT ) {
        		highlightedYear = Math.abs( highlightedYear );
        	} else 
        	if ( highlightedDirection == DIRECTION_IMPORT ) {
        		highlightedYear = - Math.abs( highlightedYear );
        	}
        	MainActivity.buttonId = highlightedYear;
    		MainActivity.refreshDiagram( highlightedYear );
        }
    }    
    
	/**
	 * Computes points of arrow polygon
	 * 
	 * @param AXIS For which axis to compute points
	 * @return Points of arrow polygon
	 */
	private float[] computeArrows( final int AXIS ) {
		buttonPanelPartSize = surfaceWidth / PARTS_IN_PANEL;
		float[] xPoints = { 
				buttonPanelPartSize * 0.2f,
				buttonPanelPartSize * 0.6f,
				buttonPanelPartSize * 0.6f,
				buttonPanelPartSize * 0.8f,
				buttonPanelPartSize * 0.6f,
				buttonPanelPartSize * 0.6f,
				buttonPanelPartSize * 0.2f };
		float[] yPoints = { 
				surfaceHeight * 0.4f,
				surfaceHeight * 0.4f,
				surfaceHeight * 0.2f,
				surfaceHeight * 0.5f,
				surfaceHeight * 0.8f,
				surfaceHeight * 0.6f,
				surfaceHeight * 0.6f };
		if ( AXIS == AXIS_X ) return xPoints;
			else return yPoints;
	}
	
	/**
	 * Computes points of Russian Federation flag polygon
	 * 
	 * @param AXIS For which axis to compute points
	 * @return Points of flag polygon
	 */
	private float[] computeFlag( final int AXIS ) {
		buttonPanelPartSize = surfaceWidth / PARTS_IN_PANEL;
		float[] xPoints = { 
				buttonPanelPartSize * 0.15f,
				buttonPanelPartSize * 0.86f,
				buttonPanelPartSize * 0.86f,
				buttonPanelPartSize * 0.15f,
				buttonPanelPartSize * 0.15f,
				buttonPanelPartSize * 0.88f,
				buttonPanelPartSize * 0.88f,
				buttonPanelPartSize * 0.15f,
				buttonPanelPartSize * 0.13f,
				buttonPanelPartSize * 0.85f,
				buttonPanelPartSize * 0.85f,
				buttonPanelPartSize * 0.13f, };
		float[] yPoints = { 
				surfaceHeight * 0.2f,
				surfaceHeight * 0.2f,
				surfaceHeight * 0.4f,
				surfaceHeight * 0.4f,
				surfaceHeight * 0.4f,
				surfaceHeight * 0.4f,
				surfaceHeight * 0.6f,
				surfaceHeight * 0.6f,
				surfaceHeight * 0.6f,
				surfaceHeight * 0.6f,
				surfaceHeight * 0.8f,
				surfaceHeight * 0.8f };
		if ( AXIS == AXIS_X ) return xPoints;
			else return yPoints;
	}
}
