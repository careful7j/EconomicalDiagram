package com.example.economicaldiagram;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * Controller that draws button and handles it's touch events.
 * 
 * @author careful7j
 * @version 1.00, 23.06.2013
 * @since android 2.3.3
 * 
 */
public class AnimButtonController implements IControllerUI {

	/** Reference to controlled UI element */
	private AnimButton animButton;
    /** Default sizes of the button. Used before actual is received */
    final private int DEFAULT_SIZE = 100;
    /** UI element X size */
    private int surfaceWidth = DEFAULT_SIZE;
    /** UI element Y size */
    private int surfaceHeight = DEFAULT_SIZE; 
    /** Background stretched to button size */
	protected Bitmap animButtonBackground;
	/** Central point X axis */
	private int center_X = DEFAULT_SIZE;
	/** Central point Y axis */
	private int center_Y = DEFAULT_SIZE;
    /** Axis X id */
    final private int AXIS_X = 0;
    /** Axis Y id */
	final private int AXIS_Y = 1;
    /** Color of invisible button Should be the same as the color under the button */
    private int INVISIBLE_COLOR = Color.BLACK;
    
	/** Help animation polygon X points array */
	private float[] helpX;
	/** Help animation polygon Y points array */
	private float[] helpY;
	/** Points in help animation polygon */
	final private int HELP_POINTS = 5;
	/** Current position of help animation polygon */	
	private float currentHelpShift = 0;
	/** Size of button's zone where animation is played. 1.00f = all button */
	final private float WAVE_ACTIVE_ZONE_SIZE = 0.34f;
	/** Help animation restarts when this value is reached by currentHelpShift */
	private float lastHelpShift = 0 - ( surfaceWidth * WAVE_ACTIVE_ZONE_SIZE );
	/** 
	 * Speed of help animation pixels per frame. 
	 * When is negative moves from right to the left. Otherwise on the contrary. 
	 */
	final private float WAVE_MOTION = -1.2f;
	/** When animation reached this point animation polygon starts fading */
	private float helpVisibilityLeftBound = 0 - ( surfaceWidth * 0.20f );
	/** Current value of alpha channel of animation polygon */
	private float helpVisibility = 0;	
	/** Maximal value of alpha channel of animation polygon */
	final private int VISIBILITY_MAX = 70;
	/** Minimal value of alpha channel of animation polygon */
	final private int VISIBILITY_MIN = 0;
	/** Alpha channel change per animation frame */
	final private float VISIBILITY_INC = 7f;
	/** Button title X position */
	private int xText = 0;
	/** Button title X position */
    private int yText = 0;
	/** Size of a button title */
    private final int BUTTON_TEXT_SIZE = 16;
    /** That value is a Y text shift down in percents from button size */
    private final float TEXT_DOWN_SHIFT = 0.7f;
    
    /** Paint used to draw custom UI element */
    private Paint paint = new Paint();
    /** Array of points to build polygon */
    private Path polygonPath = new Path();
    
	/** X of previous point user touched */
    private int eventXold = surfaceWidth / 2;
    /** Distance user has moved his finger to the left touching the device screen */
    private int touchLeft = 0;
    /**  After button is pressed it's not handled anymore until user untouches the screen */
    private boolean toHandle = false;
    /** 
     * When button is not Active its invisible.
     * Button is invisible when no diagram sectors are picked (diagram is full)
     */
    protected boolean isButtonActive = false;
    
	
    /** Sets up the button's background */
    private void setBackground() {
    	Bitmap source = Info.animButtonBMP;
        Matrix matrix = new Matrix();
        matrix.postScale(	(float) surfaceWidth / source.getWidth(),
        					(float) surfaceHeight / source.getHeight());

        animButtonBackground = Bitmap.createBitmap( source,
        											0, 0,
        											source.getWidth(), 
        											source.getHeight(),
        											matrix, false);
    }
    @Override
    public void reInitialize(	SurfaceView animButton,
									int surfaceHeight,
									int surfaceWidth )
	{
		this.animButton = (AnimButton) animButton;
		this.surfaceHeight = surfaceHeight;
		this.surfaceWidth = surfaceWidth;
		this.center_X = surfaceWidth / 2;
		this.center_Y = surfaceHeight / 2;
		this.lastHelpShift = 0 - ( surfaceWidth * 0.34f );
		this.helpVisibilityLeftBound = 0 - ( surfaceWidth * 0.28f );
		setBackground();
        helpX = computeHelpAnimation( AXIS_X );
        helpY = computeHelpAnimation( AXIS_Y );   
        yText = (int) ( surfaceHeight * TEXT_DOWN_SHIFT );
        xText = (int) ( surfaceWidth * WAVE_ACTIVE_ZONE_SIZE );
        eventXold = surfaceWidth / 2;
	}
    @Override
    public void animate( Canvas canvas ) {
    	canvas.drawBitmap( animButtonBackground, 0, 0, null);
    	animateHelp( canvas );
    	if ( !isButtonActive ) hideButton( canvas );
    }
    
    /**
     * Plays help animation
     * @param canvas where to draw
     */
    public void animateHelp( Canvas canvas ) {
    	polygonPath.reset(); 
        currentHelpShift += WAVE_MOTION;
        if ( currentHelpShift < lastHelpShift ) { currentHelpShift = 0;}
        if ( currentHelpShift > helpVisibilityLeftBound ) {
        	if ( helpVisibility < VISIBILITY_MAX ) {
        		helpVisibility += VISIBILITY_INC;
        	}
        } else if ( currentHelpShift < helpVisibilityLeftBound ) {
        	if ( helpVisibility >= VISIBILITY_MIN + VISIBILITY_INC ) {
        		helpVisibility -= VISIBILITY_INC;
        	}
        }        	
        paint.setColor( Color.argb( (int) helpVisibility, 0, 0, 0 ) );
        polygonPath.moveTo( helpX[ 0 ] + currentHelpShift, helpY[ 0 ] );  
        for (	int pointNext = 1; pointNext < HELP_POINTS; pointNext++ ) {
        	polygonPath.lineTo( helpX[ pointNext ] + currentHelpShift, helpY[ pointNext ]);            	  
        };
        paint.setStyle(Style.FILL_AND_STROKE);
        canvas.drawPath(polygonPath, paint);
        polygonPath.reset();
        paint.setColor( Color.argb( (int) ( helpVisibility * 2.4 ), 255, 255, 255 ) );
    	paint.setTextSize( BUTTON_TEXT_SIZE );
    	canvas.drawText( "Свернуть", xText, yText, paint );
    }
    
    /**
     * Makes button invisible by painting it above with INVISIBLE_COLOR
     * @param canvas where to draw
     */
    protected void hideButton( Canvas canvas ) {
    	polygonPath.reset();
    	polygonPath.moveTo( 0, 0 );
    	polygonPath.lineTo( surfaceWidth, 0 );
    	polygonPath.lineTo( surfaceWidth, surfaceHeight );
    	polygonPath.lineTo( 0, surfaceHeight );
    	paint.setStyle(Style.FILL_AND_STROKE);
        paint.setColor( INVISIBLE_COLOR );
        canvas.drawPath(polygonPath, paint);    	
    }
    
    @Override
    public void getTouch( MotionEvent event ) {
    	if ( ( event.getAction() == MotionEvent.ACTION_MOVE ) && toHandle ) {
        	if (	(int)event.getX() <= eventXold && 
        			( eventXold - (int)event.getX() < ( int) surfaceWidth / 4 ) ) 
        	{
        		touchLeft += eventXold - (int)event.getX();
        	} else touchLeft = 0;
        	if ( touchLeft > ( ( int) surfaceWidth / 3 ) ) {
        		toHandle = false;
            	touchLeft = 0;
            	//RUN ACTION
            	animButton.buttonClicked();            	
        	}
           };
        
        if (event.getAction() == MotionEvent.ACTION_UP) {        
        	toHandle = false;
        	touchLeft = 0;
           }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        	toHandle = true;
        	eventXold = (int) event.getX();
        }
    }
    
    /**
	 * Computes points of help animation polygon
	 * 
	 * @param AXIS For which axis to compute points
	 * @return Points to build help animation polygon
	 */
	protected float[] computeHelpAnimation( final int AXIS ) {
		
		float[] xPoints = {
				center_X - ( surfaceWidth * 0.15f ),
				center_X - ( surfaceWidth * 0.1f ),
				center_X + ( surfaceWidth * 0.9f ),
				center_X + ( surfaceWidth * 0.9f ),
				center_X - ( surfaceWidth * 0.1f )
		};
		float[] yPoints = {
				center_Y,
				0,
				0,
				surfaceHeight,
				surfaceHeight
		};
		if ( AXIS == AXIS_X ) return xPoints;
			else return yPoints;
	}
}
