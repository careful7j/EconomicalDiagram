package com.example.economicaldiagram;

import java.util.EmptyStackException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.economicaldiagam.R;

import static com.example.economicaldiagram.DiagramViewController.*;

/**
 * Main app Activity. Show diagram and text.
 * 
 * @author careful7j
 * @version 1.00, 13.06.2013
 * @since android 2.3.3
 *
 */
public class MainActivity extends Activity {

	/** Custom UI element DiagramView */
	protected DiagramView diagramView;
	/** Custom UI element AnimButton */
	protected AnimButton animButton;
	/** Custom UI element ButtonPanel */
	protected ButtonPanel buttonPanel;
	/**
	 * AsyncTask that loads text to the right side of the screen after user
	 * picks a diagram sector.
	 */
	public static MyTask loadText;
	/** AsyncTask that returns diagram sector back to diagram after animButton is pressed. */
	public static MyTask2 returnSectorToDiagram;
	/** Id of text to load to the right side of the screen */
	static int textId = 0;
	/** Id of buttons in ButtonPanel is pressed */
	static int buttonId = IMPORT_2012;	
	/** Thread that animates custom UI elements of this activity */
	static AnimationThread animationThread;
	
	@Override
	/** Sets the AsyncTasks and fixes activity screen orientation */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		loadText = new MyTask();
		returnSectorToDiagram = new MyTask2();
		this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
		setContentView(R.layout.activity_main);		
	}

	@Override
	/** Sets up the thread to animate custom UI elements */
	protected void onStart() {
		super.onStart();
		diagramView = (DiagramView)findViewById(R.id.diagramView);
		animButton = (AnimButton)findViewById(R.id.animButton);
		buttonPanel = (ButtonPanel)findViewById(R.id.buttonPanel);
		Info.diagramBMP = 
				BitmapFactory.decodeResource( 
						getBaseContext().getResources(), R.drawable.watson );
		Info.animButtonBMP = 
				BitmapFactory.decodeResource( 
						getBaseContext().getResources(), R.drawable.but_alttab );
		animationThread = diagramView.getThread();
		animButton.setThread( animationThread );
		buttonPanel.setThread( animationThread );
		animationThread.buttonPanelController.animationThread = animationThread;
	}
	
	@Override
	/** Finishes the animation thread */
	public void finish() {		
		super.finish();
		animationThread.setRunning( false );
	}

	/** Starts the AsyncTask that loads data to the right side of the screen */
	public static void loadText( int textIdOut ) {
		textId = textIdOut;
		loadText.selfrestart();
		loadText.execute();
	}
	
	/** Starts the AsyncTask that returns sector back to diagram */
	public static void returnSectorToDiagram() {
		returnSectorToDiagram.selfrestart();
		returnSectorToDiagram.execute();
	}
	
	/**
	 * Handles all onClick actions for all buttons of this activity.
	 * @deprecated replaced with ButtonPanel
	 * @param v Id of button pressed
	 */
	public void onClick(View v)
	{	
		/*
        switch ( v.getId() ) {
        
	        case R.id.button2012: buttonId = buttonId > 0 ? EXPORT_2012 : IMPORT_2012;	
            break;	
	        case R.id.button2010: buttonId = buttonId > 0 ? EXPORT_2010 : IMPORT_2010;		        
	        break;
	        case R.id.button2007: buttonId = buttonId > 0 ? EXPORT_2007 : IMPORT_2007;			        
	        break;		        
	        case R.id.button2004: buttonId = buttonId > 0 ? EXPORT_2004 : IMPORT_2004;			        
	        break;		        
	        case R.id.button2000: buttonId = buttonId > 0 ? EXPORT_2000 : IMPORT_2000;			        
	        break;		        
	        case R.id.buttonExport: buttonId = buttonId < 0 ? buttonId * -1 : buttonId;
	        break;		        
	        case R.id.buttonImport: buttonId = buttonId > 0 ? buttonId * -1 : buttonId;
	        break;
        }	        
        animationThread.diagramController.categories = Info.getAllData()
        	.get( animationThread.diagramController.diagramDataNames.get( buttonId ) );
        	
        */
    }
	
	/**
	 * Changes the data based on the diagram is built
	 *  
	 * @param dataId id of new data year and import-export direction
	 */
	public static void refreshDiagram( int dataId ) {
		animationThread.diagramController.categories = Info.getAllData()
	        	.get( animationThread.diagramController.diagramDataNames.get( dataId ) );
	}
	

		

	@Override
	/**
	 * Restores diagramView data and UI elements' states after app is returned
	 * from hidden state 
	 */
	protected void onResume() {
		super.onResume();
		if ( animationThread.diagramController.diagramSectorsStack.size() > 0 ) {
			findViewById(R.id.helpMessage).setVisibility(View.INVISIBLE);
			((TextView) findViewById(R.id.header)).setText(Info.getHeader( textId ) );
		    ((TextView) findViewById(R.id.body)).setText(Info.getBody( textId ) );
		} else {
			findViewById(R.id.helpMessage).setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.header)).setText("" );
		    ((TextView) findViewById(R.id.body)).setText("");
		}
        animationThread.diagramController.categories = Info.getAllData()
	        	.get( animationThread.diagramController.diagramDataNames.get( buttonId ) );
	}


	/**
	 * AsyncTask that loads text to the right side of the screen after user
	 * picks a diagram sector. Also changes UI elements states if necessary.
	 * 
	 * @author careful7j
	 * @version 1.00, 13.06.2013
	 * @since android 2.3.3
	 *
	 */
	public class MyTask extends AsyncTask<Void, Void, Void> {

	    @Override
	    protected void onPreExecute() {
		  super.onPreExecute();
	    }

	    @Override
	    protected Void doInBackground(Void... params) {
	      return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
	      animationThread.animButtonController.isButtonActive = true;
	      findViewById(R.id.helpMessage).setVisibility(View.INVISIBLE);
	      ((TextView) findViewById(R.id.header)).setText(Info.getHeader( textId ) );
	      ((TextView) findViewById(R.id.body)).setText(Info.getBody( textId ) );
	    }
	    
	    /** Prepares task for one more usage */
	    public void selfrestart() {
	    	if ( loadText.getStatus() == AsyncTask.Status.RUNNING ) {
	    		loadText.cancel( true );
	    	}
	    	loadText = null;
	    	loadText = new MyTask();
	    }
	  }
	
	/**
	 * AsyncTask that minimizes text data back to diagram and
	 * changes the UI elements' states if necessary.
	 * 
	 * @author careful7j
	 * @version 1.00, 18.06.2013
	 * @since android 2.3.3
	 *
	 */
	public class MyTask2 extends AsyncTask<Void, Void, Void> {

	    @Override
	    protected void onPreExecute() {
		  super.onPreExecute();
	    }

	    @Override
	    protected Void doInBackground(Void... params) {
	      return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
			try {
				int sectorToClose = 
						animationThread.diagramController.diagramSectorsStack.pop();
				Log.e("debug", "" + sectorToClose );
				animationThread.diagramController.animateAltTab = true;
				animationThread.diagramController.animateAltTabStage = 10;
				animationThread.diagramController.invisibleSectors[ sectorToClose ] = false;
				if ( animationThread.diagramController.diagramSectorsStack.size() == 0 ) {
					animationThread.animButtonController.isButtonActive = false;
					findViewById(R.id.helpMessage).setVisibility(View.VISIBLE);
					((TextView) findViewById(R.id.header)).setText("");
				    ((TextView) findViewById(R.id.body)).setText("");
				} else {
					((TextView) findViewById(R.id.header))
						.setText( Info.getHeader(
						animationThread.diagramController.diagramSectorsStack.peek() ) );
					((TextView) findViewById(R.id.body))
		    			.setText( Info.getBody( 
		    			animationThread.diagramController.diagramSectorsStack.peek() ) );
					findViewById(R.id.helpMessage).setVisibility(View.INVISIBLE);
				}	       
			} catch (EmptyStackException e) { }
	    }
	    
	    /** Prepares task for one more usage */
	    public void selfrestart() {
	    	if ( returnSectorToDiagram.getStatus() == AsyncTask.Status.RUNNING ) {
	    		returnSectorToDiagram.cancel( true );
	    	}
	    	returnSectorToDiagram = null;
	    	returnSectorToDiagram = new MyTask2();
	    }
	  }
}
