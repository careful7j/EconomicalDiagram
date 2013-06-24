package com.example.economicaldiagram;

import java.util.HashMap;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * Computes points to build diagram Draws diagram and handles touch events.
 * 
 * @author careful7j
 * @version 1.00, 18.06.2013
 * @since android 2.3.3
 *
 */
@SuppressLint("UseSparseArrays")
public class DiagramViewController implements IControllerUI {

	/** Reference to controlled UI element */
	private DiagramView diagramView;
	/** Background stretched to diagramView size */
	protected Bitmap diagramBackground;
    /** Maximum number of percents */
    final int PERCENTS = 100;
    /** Number of degrees in circle */
    final int DEGREES_IN_CIRCLE = 360;
    /** Number of degrees in one radian */
    final float RADIAN = 57.29f;
    
    /** Default size of the DiagramView. Used before actual are received */
    final int DEFAULT_SIZE = 100;
    /** Scale of diagram. 1.00f is equal to size of diagramView */
    final float DIAGRAM_SCALE = 0.8f;
    /** Shift of Y center down in percents from diagramView size. 0,5f - middle of View */
    final float DIAGRAM_SHIFT_DOWN = 0.55f;
    /** Number of corners in circle */
    final int NMBR_CORNERS = 300;      
    
    /** UI element X size */
    int surfaceWidth = DEFAULT_SIZE;
    /** UI element Y size */
    int surfaceHeight = DEFAULT_SIZE;    
    /** Diagram center X axis */
    float center_X = surfaceWidth / 2;
    /** Diagram center Y axis */
    float center_Y = (float) ( DIAGRAM_SHIFT_DOWN * surfaceHeight);
    /** Diagram radius */
    float radius = 170;  
    /** Axis X id */
    final int AXIS_X = 0;
    /** Axis Y id */
	final int AXIS_Y = 1;
	/** Points to build one button X axis */
    float[] xPoints = { 0 };
    /** Points to build one button Y axis */
    float[] yPoints = { 0 };
 
    /** Paint used to draw custom UI element */
    private Paint paint = new Paint();
    /** Array of points to build polygon */
    private Path polygonPath = new Path();
    
    /** 
     * To handle or not user's touches. <br> 
     * Touches start to handle when user touches the screen.
     * When users removes his finger from the screen touches are not handled any more.
     * When user pulls a sector out from diagram touches are not handled any more.
     */
    protected boolean toHandle = true;
    /** User's finger touches the screen */
	boolean touched = false;
	/** X of previous point user touched */
    float eventXold = Short.MAX_VALUE;
    /** Y of previous point user touched */
    float eventYold = Short.MAX_VALUE;
    /** X of point user touches now */ 
	float eventXnew = Short.MAX_VALUE;
	/** Y of point user touches now */ 
	float eventYnew = Short.MAX_VALUE;
    /** The speed with which the user rotates the diagram */
    float motionSpeed = 0;
    
	/** Id direction to nowhere */
    final int DIR_NONE = 0;
    /** Id direction to the left */
    final int DIR_LEFT = 2;
    /** Id direction to the right */
    final int DIR_RIGHT = 3;
    /** Id direction up */
    final int DIR_UP = 4;
    /** Id direction down */
    final int DIR_DOWN = 5;
    
    /** Id Rotation direction clockwise */
    final int DIR_FWD = -1;
    /** Id Rotation direction counterclockwise */
    final int DIR_REV = 1;	  
    /** Current rotation direction */
    int direction = DIR_NONE;
    
	/** Sectors coordinates including twist angles are computed. */
	boolean sectorsComputed = false;
	/** Current twist of diagram in points which circle is based on (not angles) */ 
	int currentRotation = 0;
	
	/** Mode when sector is pulled out from diagram. If false mode is rotation. */
	boolean modeMove = false;
	/** Diagram sector that user touches now */
	int activeSectorId = 0;
	/** The speed with which the user moves the sector */
	float activeSectorMotionSpeed = 0;
	/** How far the active sector is pulled out by user */
	float activeSectorShift = 0;
	/** How far from radius sector need to be pulled to disappear */
	final float ACTIVE_SECTOR_ZONE_SIZE = 0.75f;
	/**
	 * How far the finger need to be moved to the right to sector
	 * stop rotating and start pulling out. Switches to mode move.
	 */
	final float SECTOR_ACTIVATION_DISTANCE = 10;
	/** Current finger shift to the right */
	float sectorActivationShift = 0;
	
	/** Left top corner X of sector minimizing animation */
	float animLeft = 0;
	/** Right bottom corner X of sector minimizing animation */
	float animRight = 0;
	/** Left top corner Y of sector minimizing animation */
	float animTop = 0;
	/** Right bottom corner Y of sector minimizing animation */
	float animBot = 0;
	/** Current frame number of sector minimizing animation */
	int animateAltTabStage = 10;
	/** If the sector minimizing animation is played now */
	boolean animateAltTab = false;
	
	/** X points of help animation polygon */
	float[] helpX;
	/** Y points of help animation polygon */
	float[] helpY;
	/** Current position of help animation polygon. Changes from 0 to HELP_PER_SHIFT_LIMIT */
	float currentHelpShift = 0;
	/** Speed of help animation pixels per frame */
	float HELP_SHIFT_PER_FRAME = 2.0f;
	/** Help animation restarts when this value is reached by currentHelpShift */
	float HELP_PER_FRAME_SHIFT_LIMIT = 75f;
	/** Number of points in help animation polygon */
	final int HELP_CORNERS = 6;		
	/** Animation polygon's middle points shift */
	final int HELP_CORNER_SHIFT = 32;
	/** Width of animation polygon */
	final int HELP_WIDTH = 8;
	/** Help animation start point X offset from center X plus radius */
	final int HELP_LEFT_CORRECTION = -24;
	
	/** Decreasing change of Alpha channel per frame for sectors' titles */
	final int LABEL_REV = -3;
	/** Increasing change of Alpha channel per frame for sectors' titles */
	final int LABEL_FWD = 3;
	/** Font size of sectors' titles */
	final int LABEL_TEXT_SIZE = 14;
	/** Maximal alpha value of sectors' titles */
	final int LABEL_COLOR_MAX = 220;
	/** Minimal alpha value of sectors' titles */
	final int LABEL_COLOR_MIN = 90;
	/** 
	 * Sector titles left shift.
	 * Used to prevent sectors' titles moving out from diagramView at right */
	final int LABEL_LEFT_SHIFT = -100;
	/** Current value of alpha channel for sectors' titles */
	int labelTransparency = LABEL_COLOR_MIN;
	/** Direction of sectors' titles alpha channel change */
	int labelChangeDirection = LABEL_FWD;
	/** Current sectors' title left shift. Values are: LABEL_LEFT_SHIFT or 0 */
	int labelCurrentLeftShift = 0;
	
	/** Diagram header font size */
	private final int DIAGRAM_HEADER_TEXT_SIZE = 17;
	/** X point of diagram header */
    private final int DIAGRAM_HEADER_TEXT_X_POS = 20;
    /** Y point of diagram header */
    private final int DIAGRAM_HEADER_TEXT_Y_POS = 30;
    /** Map that compares UI signals with diagram headers */
	private final HashMap < Integer, Integer > DIAGRAM_HEADERS_IDS = 
			new HashMap < Integer, Integer >(){
			private static final long serialVersionUID = -7165422719527003447L;
			{													
		        put( IMPORT_2012, 0 );
		        put( IMPORT_2010, 1 );
		        put( IMPORT_2007, 2 );
		        put( IMPORT_2004, 3 );
		        put( IMPORT_2000, 4 );
		        put( EXPORT_2012, 5 );
		        put( EXPORT_2010, 6 );
		        put( EXPORT_2007, 7 );
		        put( EXPORT_2004, 8 );
		        put( EXPORT_2000, 9 );
		    }};;
    
    /** Stack where sectors already pulled out from diagram are placed */
	Stack<Integer> diagramSectorsStack = new Stack<Integer>();
	
	/** UI signal */
	static final int EXPORT_2012 = 1;	
	/** UI signal */
	static final int EXPORT_2010 = 2;
	/** UI signal */
	static final int EXPORT_2007 = 3;
	/** UI signal */
	static final int EXPORT_2004 = 4;
	/** UI signal */
	static final int EXPORT_2000 = 5;
	/** UI signal */
	static final int IMPORT_2012 = -1;
	/** UI signal */
	static final int IMPORT_2010 = -2;
	/** UI signal */
	static final int IMPORT_2007 = -3;
	/** UI signal */
	static final int IMPORT_2004 = -4;
	/** UI signal */
	static final int IMPORT_2000 = -5;
	
	/** Map that compares UI signals with year data identifiers */
	@SuppressLint("UseSparseArrays")
	final HashMap < Integer, String > diagramDataNames = 
			new HashMap < Integer, String >(){
			private static final long serialVersionUID = 1327940003343370401L;

			{													
		        put( IMPORT_2012, "IMPORT_2012" );
		        put( IMPORT_2010, "IMPORT_2010" );
		        put( IMPORT_2007, "IMPORT_2007" );
		        put( IMPORT_2004, "IMPORT_2004" );
		        put( IMPORT_2000, "IMPORT_2000" );
		        put( EXPORT_2012, "EXPORT_2012" );
		        put( EXPORT_2010, "EXPORT_2010" );
		        put( EXPORT_2007, "EXPORT_2007" );
		        put( EXPORT_2004, "EXPORT_2004" );
		        put( EXPORT_2000, "EXPORT_2000" );
		    }};;
    		    
	/** Current sectors' sizes based on diagram is built now */
	HashMap< String, Double > categories = 
			Info.getAllData().get( "IMPORT_2012" );
	/** Current sectors names */
	String[] categoryNames =
			categories.keySet().toArray(
			new String[categories.size()] );	
	
	/** Sectors start points X axis */
	float[] sectorXfirst = new float[ categories.size() ];
	/** Sectors start points Y axis */
	float[] sectorYfirst = new float[ categories.size() ];
	/** Sectors end points X axis */
	float[] sectorXlast = new float[ categories.size() ];
	/** Sectors end points Y axis */
	float[] sectorYlast = new float[ categories.size() ];	
	/** Invisible sectors that user has already pulled out from diagram */
	boolean[] invisibleSectors = new boolean[ categories.size() ];	
	
	/** Red channel of diagram sectors' colors */
	final int[] R = Info.R;
	/** Green channel of diagram sectors' colors */
	final int[] G = Info.G;
	/** Blue channel of diagram sectors' colors */
	final int[] B = Info.B;	
	
    /** Sets up diagram's background */
    private void setBackground() {
    	Bitmap source = Info.diagramBMP;
        Matrix matrix = new Matrix();
        matrix.postScale(	(float) surfaceWidth / source.getWidth(),
        					(float) surfaceHeight / source.getHeight());

        diagramBackground = Bitmap.createBitmap( 	source,
        											0, 0,
        											source.getWidth(), 
        											source.getHeight(),
        											matrix, false);
    }
       
    @Override
    public void reInitialize(	SurfaceView diagramView,
    								int surfaceHeight,
    								int surfaceWidth )
    {
    	this.diagramView = (DiagramView) diagramView;
    	this.surfaceHeight = surfaceHeight;
    	this.surfaceWidth = surfaceWidth;
    	radius = (int) ( surfaceWidth * DIAGRAM_SCALE / 2 );  
    	center_X = surfaceWidth / 2;
    	center_Y = (float) ( DIAGRAM_SHIFT_DOWN * surfaceHeight);
    	xPoints = computeDiagramPoints( AXIS_X );        
    	yPoints = computeDiagramPoints( AXIS_Y );
    	setBackground();
        helpX = computeHelpAnimation( AXIS_X );
        helpY = computeHelpAnimation( AXIS_Y );
    }
	
    @Override
	public void animate( Canvas canvas ) {
    	try {
    	canvas.drawBitmap( diagramBackground, 0, 0, null);
    	animateSectors( canvas );
	    animateLabels( canvas );
        animateHelp( canvas );
        animateAltTab( canvas );
        animateHeader( canvas ); 
    	} catch ( Exception e ) { }
	}
    
    /**
     * Draws diagram sectors
     * @param canvas where to draw
     */
    private void animateSectors( Canvas canvas ) {
    	float frameLast = 0;
        float frameNext = 0;    
        
	    for ( 	int categoryId = 0;
        		categoryId < categories.size();
        		categoryId++ )
        {
        	frameNext = (float) (Math.round( NMBR_CORNERS / PERCENTS *
        			categories.get(categoryNames[ categoryId ]) ) );
        	polygonPath.reset(); 
            paint.setColor( Color.argb( 190,	
            							R[categoryId],
            							G[categoryId],
            							B[categoryId] ));
            boolean dontSkipThisSector = true;
            if ( ( categoryId == activeSectorId && modeMove ) ) {
            	dontSkipThisSector = false;
            }            
            if ( ( invisibleSectors[ categoryId ] ) ) {
            	frameLast += frameNext;
            	continue;
            }
            if ( dontSkipThisSector ) {
            	float circleComplete = frameNext + frameLast + currentRotation;
            	// to last sector donesn't creep first sector
            	if (categoryId == ( categories.size() - 1 ) ) {
            		circleComplete = NMBR_CORNERS + currentRotation;
            	}            	
            	polygonPath.moveTo( center_X , center_Y );                                    
                for (	int pointNext = (int) ( frameLast + currentRotation );
                		pointNext <= circleComplete;
                		pointNext++
               		)
                {
                	polygonPath.lineTo(	
                		xPoints[ pointNext % ( NMBR_CORNERS ) + NMBR_CORNERS ],
                		yPoints[ pointNext % ( NMBR_CORNERS ) + NMBR_CORNERS ]);            	  
                };
                
                //draw filled sectors
                polygonPath.lineTo( center_X , center_Y );
                paint.setStyle(Style.FILL_AND_STROKE);
                canvas.drawPath(polygonPath, paint);
                //draw white frames            
                paint.setColor( Color.argb( 190,255,255,255 ) );
                paint.setStyle(Style.STROKE);
                canvas.drawPath(polygonPath, paint);
            }        
            
            if ( categoryId == activeSectorId && modeMove ) {
            	polygonPath.reset(); 
                paint.setColor( Color.argb( 190,	
								R[categoryId],
								G[categoryId],
								B[categoryId] ));
                polygonPath.moveTo( center_X + activeSectorShift , center_Y );                                    
                for (	int pointNext = (int) ( frameLast + currentRotation );
                		pointNext <= frameNext + frameLast + currentRotation;
                		pointNext++
               		)
                {
                	polygonPath.lineTo(	
                		xPoints[ pointNext % ( NMBR_CORNERS ) + NMBR_CORNERS] +
                											activeSectorShift,
                		yPoints[ pointNext % ( NMBR_CORNERS ) + NMBR_CORNERS]);            	  
                };
                polygonPath.lineTo( center_X + activeSectorShift, center_Y );
                paint.setStyle(Style.FILL_AND_STROKE);
                canvas.drawPath(polygonPath, paint);
                paint.setColor( Color.WHITE );
                paint.setStyle(Style.STROKE);
                canvas.drawPath(polygonPath, paint);
            }

            frameLast += frameNext;           
        }
    }
    
    /**
     * Draws titles above diagram sectors
     * @param canvas where to draw
     */
    private void animateLabels(Canvas canvas ) {
    	float frameLast = 0;
        float frameNext = 0;
        
        if ( paint.getTextSize() != LABEL_TEXT_SIZE ) paint.setTextSize( LABEL_TEXT_SIZE );
        if ( labelTransparency > LABEL_COLOR_MAX  ) labelChangeDirection = LABEL_REV;
        if ( labelTransparency < LABEL_COLOR_MIN  ) labelChangeDirection = LABEL_FWD;
        labelTransparency += labelChangeDirection;
        for ( 	int categoryId = 0;
        		categoryId < categories.size();
        		categoryId++ )
        {
        	frameNext = (float) ((float) NMBR_CORNERS / PERCENTS *
        			categories.get(categoryNames[ categoryId ]));
        	paint.setColor( Color.argb( (int) labelTransparency, 255, 255, 255 ) );
            int nextPoint = 
            		(int) ( frameLast + currentRotation + frameNext / 2 ) %
            		( NMBR_CORNERS ) + NMBR_CORNERS;
            if ( xPoints[ nextPoint ] > center_X + radius / 2 ) {
            	labelCurrentLeftShift = LABEL_LEFT_SHIFT;
            } else labelCurrentLeftShift = 0;
            String text = categoryNames[ categoryId ] + " (" +
            		categories.get(categoryNames[ categoryId ]) + "%)";
            if 	( invisibleSectors[ categoryId] ||
            	( categories.get(categoryNames[ categoryId ]) == 0  ) ) { text = ""; };
            canvas.drawText(	text, 
			            		xPoints[ nextPoint ] + labelCurrentLeftShift,
			            		yPoints[ nextPoint ],
			            		paint);
            
            frameLast += frameNext;   
        }
    }
    
    /**
     * Draws help animation ( Arrow that moves to the right )
     * @param canvas where to draw
     */
    private void animateHelp( Canvas canvas ) {
    	polygonPath.reset(); 
        currentHelpShift+=HELP_SHIFT_PER_FRAME;
        currentHelpShift %= HELP_PER_FRAME_SHIFT_LIMIT;
        paint.setColor( Color.argb( (int) currentHelpShift, 255, 255, 255 ) );
        polygonPath.moveTo( helpX[ 0 ] + currentHelpShift, helpY[ 0 ] );  
        for (	int pointNext = 1; pointNext < HELP_CORNERS; pointNext++ ) {
        	polygonPath.lineTo( helpX[ pointNext ] + currentHelpShift, helpY[ pointNext ]);            	  
        };
        paint.setStyle(Style.FILL_AND_STROKE);
        canvas.drawPath(polygonPath, paint);
        polygonPath.reset();
    }
    
    /**
     * Draws text into diagram minimizing animation
     * @param canvas where to draw
     */
    private void animateAltTab( Canvas canvas ) {
    	if ( animateAltTab ) {
        	if ( animateAltTabStage <= 0f ) {
        		animateAltTab = false; 
        		};
        	animateAltTabStage-=2;
        	paint.setStyle(Style.FILL_AND_STROKE); 
        	paint.setColor( Color.argb( 50, 255, 255, 255 ) );
        	animLeft = (float) ( center_X - radius / 2f + 
        			( radius / 10f ) * animateAltTabStage );
        	animTop = (float) ( center_Y - radius / 2f - 
        			( radius / 8f ) * animateAltTabStage );
        	animRight = (float) ( center_X + radius / 2f + 
        			( radius / 10f ) * animateAltTabStage );   
        	animBot = (float) ( center_Y + radius / 2f + 
        			( radius / 8f ) * animateAltTabStage );
        	canvas.drawRect(animLeft, animTop, animRight, animBot, paint);
        }
    }
    
    /**
     * Draws a header above the diagram
     * @param canvas where to draw
     */
    private void animateHeader( Canvas canvas ) {
    	paint.setColor( Color.rgb( 255, 255, 255 ) );
    	paint.setTextSize( DIAGRAM_HEADER_TEXT_SIZE );
    	String diagramHeader = Info.getLabel( 
    			DIAGRAM_HEADERS_IDS.get( MainActivity.buttonId ) );
        canvas.drawText(	diagramHeader, 
        					DIAGRAM_HEADER_TEXT_X_POS,
        					DIAGRAM_HEADER_TEXT_Y_POS,
        					paint);
    }
    
    @Override
    public void getTouch( MotionEvent event ) {
    	if ( ( event.getAction() == MotionEvent.ACTION_MOVE ) && toHandle) {
        	handleTouch((int)event.getX(), (int)event.getY());
        	isSectorPicked();
           }
        
        if (event.getAction() == MotionEvent.ACTION_UP) {        
        	touched = false;
        	modeMove = false;
        	toHandle = true;       	
        	activeSectorShift = 0;
        	sectorsComputed = false;
        	sectorActivationShift = 0;
           }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        	touched = true;
        	eventXold = (int) event.getX();
        	eventYold = (int) event.getY();
        }   
    }    
    
    /**
     * Computes direction and rotation speed of diagram when rotation mode and
     * direction and shift speed for sector when mode move.
     * 
     * @param eventX X of user's touch point
     * @param eventY Y of user's touch point
     */
    private void handleTouch(int eventX, int eventY ) {
    	
    	eventXnew = eventX; 
    	eventYnew = eventY;
    	int moveDirH = DIR_NONE;		//horizontal
    	int moveDirV = DIR_NONE;		//vertical
    	direction = DIR_NONE;
    	
    	if ( eventX == eventXold ) moveDirH = DIR_NONE;
    	if ( eventX < eventXold ) moveDirH = DIR_LEFT;
    	if ( eventX > eventXold ) moveDirH = DIR_RIGHT;
    	    	
    	if ( eventY == eventYold ) moveDirV = DIR_NONE;
    	if ( eventY < eventYold ) moveDirV = DIR_DOWN;
    	if ( eventY > eventYold ) moveDirV = DIR_UP;    	
    	
    	// === outer part of the circle ===
    	if ( eventX < ( radius / 2 + ( surfaceWidth / 2 - radius ) ) ) {
    		if ( moveDirV == DIR_UP ) direction = DIR_FWD;
    		if ( moveDirV == DIR_DOWN ) direction = DIR_REV;
    	}
    	if ( eventY < ( radius / 2 + ( surfaceHeight / 2 - radius ) ) ) {
    		if ( moveDirH == DIR_LEFT ) direction = DIR_FWD; 
    		if ( moveDirH == DIR_RIGHT ) direction = DIR_REV;
    	}
    	if ( eventY > ( radius / 2 + ( surfaceHeight / 2 ) ) )  {
    		if ( moveDirH == DIR_LEFT ) direction = DIR_REV;
    		if ( moveDirH == DIR_RIGHT ) direction = DIR_FWD;
    	}
    	if ( eventX > ( radius / 2 + ( surfaceWidth / 2 ) ) ) {
    		if ( moveDirV == DIR_UP ) direction = DIR_REV;
    		if ( moveDirV == DIR_DOWN ) direction = DIR_FWD;
    	}
    	
    	//=== inner part of the circle ===   
    	if (	( eventX >= center_X - radius / 2 ) &&
    			( eventX <= center_X ) &&
    			( eventY >= center_Y - radius / 2 ) &&
    			( eventY <= center_Y + radius / 2 )
    		)
    			{
    				if ( moveDirV == DIR_UP ) direction = DIR_FWD;
    				if ( moveDirV == DIR_DOWN ) direction = DIR_REV;
    			}
    	
    	if (	( eventX >= center_X ) &&
    			( eventX <= center_X + radius / 2 ) &&
    			( eventY >= center_Y - radius / 2 ) &&
    			( eventY <= center_Y + radius / 2 )
    		)
    			{
    				if ( moveDirV == DIR_UP ) direction = DIR_REV;
    				if ( moveDirV == DIR_DOWN ) direction = DIR_FWD;
    			}    	
    	
    	if (	( eventX >= center_X - radius / 2 ) &&
    			( eventX <= center_X + radius / 2 ) &&
    			( eventY >= center_Y - radius / 2 ) &&
    			( eventY <= center_Y )
    		)
    			{
    				if ( moveDirH == DIR_LEFT ) direction = DIR_FWD;
    				if ( moveDirH == DIR_RIGHT ) direction = DIR_REV;
    			}

    	if (	( eventX >= center_X - radius / 2 ) &&
    			( eventX <= center_X + radius / 2 ) &&
    			( eventY >= center_Y ) &&
    			( eventY <= center_Y + radius / 2 )
    		)
    			{
    				if ( moveDirH == DIR_LEFT ) direction = DIR_REV;
    				if ( moveDirH == DIR_RIGHT ) direction = DIR_FWD;
    			}   
    	
    	motionSpeed = this.computeRotationSpeed( direction );    	

    	// Is not user ( starting to ) pulling out a diagram sector?
    	if (	( ( Math.abs(eventX - eventXold ) > Math.abs(eventY - eventYold) ) ) &&
    			( eventX <= center_X + radius ) &&
    			(	
	    			(	( eventY > center_Y ) &&
	    				( eventX > center_X ) &&
	    				( eventY < eventX )
	    			)
	    			||
	    			(	( eventY < center_Y ) &&
	    				( eventY > center_Y + center_Y - eventX ) &&
	    				( eventX > center_X )
	    			)
	    		)
	    	)
    	{
    		sectorActivationShift += Math.abs( (float) eventX - eventXold);
    		if ( sectorActivationShift > SECTOR_ACTIVATION_DISTANCE ) {
	    		float frameLast = 0;
	            float frameNext = 0;
	            // find sectors' positions
	            for ( 	int categoryId = 0;
	            		categoryId < categories.size();
	            		categoryId++ )
	            {
	            	this.computeSectorsBounds( categoryId, frameLast );
	            	frameNext = (float) ((float) NMBR_CORNERS / 100 *
	            			categories.get(categoryNames[ categoryId ]));
	            	frameLast += frameNext;
	            }
	    		activeSectorId = this.findActiveSectorId();
	    		if ( !invisibleSectors[ activeSectorId ] ) {
		    		motionSpeed = 0;
		    		modeMove = true;
	    		}
    		}
    	}    	
    	
    	if ( !modeMove ) { 
     		currentRotation = (int) (currentRotation + ( motionSpeed % NMBR_CORNERS ));
    		sectorsComputed = false;
    	} else { 
    		activeSectorMotionSpeed = ( eventX - eventXold );
    		if ( ( activeSectorShift + activeSectorMotionSpeed + center_X ) > center_X ) {
    			activeSectorShift += activeSectorMotionSpeed;
    		} else activeSectorShift = 0;
    	}
    	
    	eventXold = eventX;	
    	eventYold = eventY;	    
    }
    
    /** Computes if the sector is pulled out enough to consider it picked */
    protected void isSectorPicked() {
    	if ( activeSectorShift > radius * ACTIVE_SECTOR_ZONE_SIZE ) {
    		invisibleSectors[ activeSectorId ] = true;
        	activeSectorShift = 0;
        	toHandle = false;
        	diagramSectorsStack.push( Integer.valueOf( activeSectorId ) );
        	Log.e("","" + diagramSectorsStack.size());
        	diagramView.diagramSelected( activeSectorId );
    	}
    }
    
    /**
     * Computes the diagram's rotation speed
     * 
     * @param direction Diagram rotation's direction
     * @return Diagram's rotation speed
     */
    private float computeRotationSpeed( int direction ) {    	
    	float angle = this.getAngle( eventXold, eventYold, eventXnew, eventYnew );
    	return 	motionSpeed = 
    			( direction * angle *
    			( (float) DEGREES_IN_CIRCLE / NMBR_CORNERS ) / RADIAN );
    }
    
    /**
     * Computes sector's start and end points
     * 
     * @param categoryId Number of sector to compute points for
     * @param frameLast Previous sector end
	 */
    private void computeSectorsBounds( int categoryId, float frameLast ) {
    	if ( currentRotation < 0 ) {
    		currentRotation = NMBR_CORNERS + ( currentRotation % NMBR_CORNERS );
    	}
       	sectorXfirst[ categoryId ] =
       			xPoints[ ( (int) frameLast + currentRotation + 1 )  % NMBR_CORNERS ];
       	sectorYfirst[ categoryId ] =
       			yPoints[ ( (int) frameLast + currentRotation + 1 ) % NMBR_CORNERS ];
       	int sectorEnd = (int) ( frameLast + 
       			( categories.get(categoryNames[ categoryId ] ) * NMBR_CORNERS / PERCENTS ) );
       	sectorXlast[ categoryId ] =
       			xPoints[ ( sectorEnd + currentRotation - 1 ) % NMBR_CORNERS ];
       	sectorYlast[ categoryId ] =
       			yPoints[ ( sectorEnd + currentRotation - 1 ) % NMBR_CORNERS ];
    }
    
    /**
     * Determines which sector in now under user's finger
     * @return Sector id
     */
    private int findActiveSectorId() {   	
    	int lastId = 0;
    	float lastValue = Integer.MAX_VALUE;
        float tempValue = 0;
        float x1, x2, y1, y2;
		x1 = eventXnew;
		y1 = eventYnew;
    	for ( int 	categoryId = 0;
    				categoryId < categories.size();
    				categoryId++ )
    	{
    		x2 = sectorXfirst [ categoryId ];
    		y2 = sectorYfirst [ categoryId ];  		
    		tempValue = (float) Math.sqrt( 
    				( Math.abs( x2 - x1 ) * ( Math.abs( x2 - x1 ) ) ) +
    				( Math.abs( y2 - y1 ) * ( Math.abs( y2 - y1 ) ) ) );
    		if ( tempValue < lastValue ) {
    			lastId = categoryId;
    			lastValue = tempValue;
    		}
    		x2 = sectorXlast [ categoryId ];
    		y2 = sectorYlast [ categoryId ];  		
    		tempValue = (float) Math.sqrt( 
    				( Math.abs( x2 - x1 ) * ( Math.abs( x2 - x1 ) ) ) +
    				( Math.abs( y2 - y1 ) * ( Math.abs( y2 - y1 ) ) ) );
    		if ( tempValue < lastValue ) {
    			lastId = categoryId;
    			lastValue = tempValue;
    		}
    	}    	
    	if ( !sectorsComputed )
    	{ sectorsComputed = true; 
    	  return lastId;
    	} else return activeSectorId;
    }
    
    /**
     * Computes the angle between two points using diagram's center as the third point
     * 
     * @param oldX Value X of point 1 ( Previous touch point )
     * @param oldY Value Y of point 1 ( Previous touch point )
     * @param newX Value X of point 2 ( Point that user touches now )
     * @param newY Value Y of point 2 ( Point that user touches now )
     * @return angle between two points in radians ( diagram center as the third point )
     */
    private float getAngle( float oldX, float oldY, float newX, float newY ) {
    	Point A = new Point( (int) center_X, (int) center_Y );
    	Point B = new Point( (int) oldX,(int) oldY );
    	Point C = new Point( (int) newX, (int) newY );
    	double AB;
    	double AC;
    	double BC;
    	AB = Math.sqrt(Math.pow(B.x - A.x, 2) + Math.pow(B.y - A.y, 2));
    	AC = Math.sqrt(Math.pow(C.x - A.x, 2) + Math.pow(C.y - A.y, 2));
    	BC = Math.sqrt(Math.pow(C.x - B.x, 2) + Math.pow(C.y - B.y, 2));
    	double ratio = ( AB * AB - AC * AC - BC * BC ) / ( 2 * AC * AB );
    	return (float) ( Math.acos(ratio)*( 180 / Math.PI ) );    	
    }
 
    /**
     * Computes points of diagram polygon. 
     * Points are copied 4 times to ease rotation algorithm.
     * 
     * @param AXIS Axis points are computed for
     * @return Diagram polygon's points
     */
    private float[] computeDiagramPoints( final int AXIS ) 
	{	    
		float[] xPoints = new float[ NMBR_CORNERS ];
		float[] yPoints = new float[ NMBR_CORNERS ];
		
		for (int i = 0; i < NMBR_CORNERS; i++) {
		xPoints[i] = (float) (center_X + radius * Math.cos(2 * Math.PI * i / NMBR_CORNERS));
		yPoints[i] = (float) (center_Y + radius * Math.sin(2 * Math.PI * i / NMBR_CORNERS));
		}	    		
		if ( AXIS == AXIS_X ) 
			return concat( xPoints, concat( xPoints, concat( xPoints, xPoints ) ) );
			else return concat( yPoints, concat(yPoints, concat(yPoints, yPoints) ) );	    	    
	}
	
    /** 
     * Merges two arrays into one by values ( first array fully then second )
     * @param A array one
     * @param B array two
     * @return array that contains values of both arrays
     */
	private float[] concat(float[] A, float[] B) {
		int aLen = A.length;
		int bLen = B.length;
		float[] C= new float[aLen+bLen];
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}
	
	/**
	 * Computes points of help animation polygon.
	 * 
     * @param AXIS Axis points are computed for
     * @return Help animation polygon's points
	 */
	protected float[] computeHelpAnimation( final int AXIS ) {
		float[] xPoints = { 
				center_X + HELP_LEFT_CORRECTION + radius + HELP_CORNER_SHIFT,
				center_X + HELP_LEFT_CORRECTION + radius ,
				center_X + HELP_LEFT_CORRECTION + radius + HELP_WIDTH,
				center_X + HELP_LEFT_CORRECTION + radius + HELP_CORNER_SHIFT + HELP_WIDTH,
				center_X + HELP_LEFT_CORRECTION + radius + HELP_WIDTH,
				center_X + HELP_LEFT_CORRECTION + radius				
		};
		float[] yPoints = {
				center_Y,
				center_Y - radius / 2,
				center_Y - radius / 2,
				center_Y,
				center_Y + radius / 2,
				center_Y + radius / 2
		};
		if ( AXIS == AXIS_X ) return xPoints;
			else return yPoints;
	}
	
}
