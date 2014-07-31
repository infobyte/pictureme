/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.pictureme.constants.Active;
import com.pictureme.managers.ConfigurationManager;


public class MainActivity extends Activity {
	private Boolean isTimerActive = false;
	Button btnStart = null, btnGallery = null, btnTest = null;
	SharedPreferences sPrefs = null;
	
	/**
	 * 
	 * Global button listener
	 * 
	 */
	OnClickListener ocListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			handleButtonClick(v);
		}
	};
	
	@Override
	protected void onResume()
	{		
		super.onResume();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			bindControls();
			
			// Get the intent that has called the MainActivity
			Intent intent = getIntent();
			if (intent != null)
			{
				// If the intent has the "start_taking_pictures" extra, 
				// let's start taking pictures automatically on the startup
				if (intent.getBooleanExtra("start_taking_pictures", false) == true)	
				{
					enableService();
				}
			}
			Log.v("pictureme", "OnCreate");
		} 
		catch (Exception e) {
			ConfigurationManager.writeLog(e);
		}
	
	}
	
	@Override
	public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(getString(R.string.exit))
                .setMessage(getString(R.string.are_you_sure_exit))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    	try {

//                    		TODO: Ask to stop the service or put the app in background
														
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.addCategory(Intent.CATEGORY_HOME);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							finish();
							
						} catch (Exception e) {
							ConfigurationManager.writeLog(e);
						}
                    }
                }).setNegativeButton(getString(R.string.no), null).show();
    } 
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		try
		{
			sPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			Editor editor = sPrefs.edit();
			editor.putBoolean("is_timer_active", isTimerActive);
			editor.commit();
			
			editor = null;
			sPrefs = null;
		}
		catch (Exception e)
		{
			ConfigurationManager.writeLog(e);
		}
		
	}
	
	@Override
	public void onRestoreInstanceState (Bundle savedInstanceState)
	{
		try
		{
			sPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			isTimerActive = sPrefs.getBoolean("is_timer_active", false);
			
			setStartButtonProperties(isTimerActive);
		}
		catch (Exception e)
		{
			ConfigurationManager.writeLog(e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try
		{
			// Inflate the menu; 
			//this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.activity_main, menu);
		}
		catch (Exception e)
		{
			ConfigurationManager.writeLog(e);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    // This will handle the menu events
	    switch (item.getItemId()) {
	        case R.id.menu_settings:
	        	Intent i = new Intent(this, SettingsActivity.class );
	    		startActivity(i);
	    		return true;
	        default:
	        	return super.onOptionsItemSelected(item);
	    			
	    }
	}
	
	/**
	 * 
	 * Bind the class variables to their corresponding 
	 * views. Since the findViewById is too expensive, 
	 * this function will be called only in the onCreate
	 *  
	 */
	private void bindControls()
	{
		try
		{
			// Get the button instances
			btnStart = (Button) findViewById(R.id.btnStart);
			btnGallery = (Button) findViewById(R.id.btnGallery);
			btnTest = (Button) findViewById(R.id.btnTest);
			
			// Set the click listeners
			btnStart.setOnClickListener(ocListener);
			btnGallery.setOnClickListener(ocListener);
			btnTest.setOnClickListener(ocListener);
			
		}
		catch (Exception e)
		{
			ConfigurationManager.writeLog(e);
		}
	
	}

	
	/**
	 * 
	 * Global handler for the button clicks
	 * 
	 * @param view Button that raised the event
	 * 
	 */
	private void handleButtonClick(View v) {
		try {
			switch (v.getId()) {
				case R.id.btnStart:
					enableService();
//					takePicture();
					break;
				case R.id.btnGallery:
					startActivity(new Intent(this, ListDirectory.class));
					break;
				case R.id.btnTest:
//					enableService();
					break;
				
			}
		} catch (Exception e) {
			ConfigurationManager.writeLog(e);
		} 
	}
	
	private void setStartButtonProperties(boolean isTimerActive)
	{
		try {
			if(isTimerActive)
			{
				btnStart.setText(R.string.btnStop);
				btnStart.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_red_pm_stopcapture));
				
			}
			else
			{
				btnStart.setText(R.string.btnStart);
				btnStart.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_pm_capture));
			}
		} catch (Exception e) {
			ConfigurationManager.writeLog(e);
		} 
	}

	private void enableService()
	{
		if (!isTimerActive)
		{
			isTimerActive = true;
		}
		else
		{
			isTimerActive = false;
		}
		setStartButtonProperties(isTimerActive);
		// Start the capture service
		startService(Active.getServiceIntent(MainActivity.this, Bundle.EMPTY));
	}
}

