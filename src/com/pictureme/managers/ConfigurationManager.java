/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme.managers;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.util.Log;

public class ConfigurationManager {
	public static int DEF_CAPTURE_TIMER = 30;
	public static boolean DEF_SHUTTER_ENABLED = false;
	
	public static void writeLog(Exception exception)
	{		
		SimpleDateFormat sdf = null;
		FileWriter fWr = null;
		File file = null;
		
		try {
			if(exception != null)
			{
				Log.wtf("pictureme", "Error: "+ exception.getMessage());
				
				
				file = PictureManager.getStorageFilePath();
				file = new File(file,"pictureme-errors.log");
				
				fWr = new FileWriter(file, true);

				fWr.write("*******************************\n\n");
				fWr.write("Message: ");
				fWr.write(exception.getMessage());
				fWr.write("\n");
				fWr.write("Date: ");
				
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				fWr.write(sdf.format(Calendar.getInstance().getTime()));
				fWr.write("\n\n");

				fWr.write("\n");
				fWr.write("Stack Trace ----->");
				fWr.write("\n");
	
				fWr.write(Log.getStackTraceString(exception));
				
				fWr.write("*******************************\n");		
				fWr.close();
			}
			
		} catch (Exception e) {
				
			// DO NOTHING...  
			e.printStackTrace();
		}
		finally
		{
			file = null;
			fWr = null;
			sdf = null;
		}
		
	}
}
