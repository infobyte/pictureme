/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme;

import com.pictureme.managers.ConfigurationManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PictureMeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		try {
			if (arg1.getAction() == Intent.ACTION_BOOT_COMPLETED)
			{
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(arg0);
				if(settings.getBoolean("enabled_at_startup", false))
				{
					Intent startIntent = new Intent(arg0, MainActivity.class);
					startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startIntent.putExtra("start_taking_pictures", true);
					arg0.startActivity(startIntent);	
				}				
						}
		} catch (Exception e) {
			ConfigurationManager.writeLog(e);
		}       
	}
} 