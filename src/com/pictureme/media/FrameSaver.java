/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme.media;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.me.constant.Constant;

import com.pictureme.camera.FrameBuffer;

public class FrameSaver implements Constant {

	private static final String FILE_NAME_FORMAT = "%s_%d_%d%s";
	private String mFolder = null;
	private List<String> mFiles = new ArrayList<String>();

	public FrameSaver() {
		createFolder();
	}
	
	public void saveFrame(Context context, FrameBuffer frame)
	{
		FileOutputStream output = null;
		File file = null;
		try {
//			if (first) {
//				mFiles.clear();
//			}
			mFiles.clear();
			
			file = getFile(Math.round(Math.random()), (int)Math.round(Math.random()), (int)Math.round(Math.random()));
			
			output = new FileOutputStream(file);
			YuvImage yuvImage = new YuvImage(frame.getFrame(), ImageFormat.NV21, frame.getWidth(), frame.getHeight(), null);
			yuvImage.compressToJpeg(frame.getRect(), 100, output);
			mFiles.add(file.getPath());
		} catch (Exception e) {
			Log.d(LOG, "Exception ", e);
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				Log.d(LOG, "Exception ", e);
			}
//			if (file != null) {
//				file.setLastModified(time);
//			}
		}
//		if (index && last) {
//			MediaScannerConnection.scanFile(context.getApplicationContext(), mFiles.toArray(new String[mFiles.size()]), null, null);
//		}
	}

	public void saveFrame(Context context, long time, FrameBuffer frame, boolean first, boolean last, boolean index, int frameCount, int frameTotal) {
		FileOutputStream output = null;
		File file = null;
		try {
			if (first) {
				mFiles.clear();
			}
			file = getFile(time, frameCount, frameTotal);
			output = new FileOutputStream(file);
			YuvImage yuvImage = new YuvImage(frame.getFrame(), ImageFormat.NV21, frame.getWidth(), frame.getHeight(), null);
			yuvImage.compressToJpeg(frame.getRect(), 100, output);
			mFiles.add(file.getPath());
		} catch (Exception e) {
			Log.d(LOG, "Exception ", e);
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				Log.d(LOG, "Exception ", e);
			}
			if (file != null) {
				file.setLastModified(time);
			}
		}
		if (index && last) {
			MediaScannerConnection.scanFile(context.getApplicationContext(), mFiles.toArray(new String[mFiles.size()]), null, null);
		}
	}

	private File getFile(long time, int frameCount, int frameTotal) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");

		// Create the albumname, filename, and full path
		String albumName = "pictureme-" + sdf.format(System.currentTimeMillis());
		// Create the albumname, filename, and full path
		String fileName = System.currentTimeMillis() + ".jpg";
		
		// Create the directory with the format "pictureme-date";-
		File pmFolder = new File(mFolder, albumName);
		
		if (!pmFolder.exists())
		{
			pmFolder.mkdirs();
		}
		
		return new File(mFolder, fileName);
	}

	private void createFolder() {
		File file = new File(Environment.getExternalStorageDirectory(), DUMP_FOLDER_NAME);
		if (!file.exists()) {
			file.mkdirs();
		}
		mFolder = file.getPath();
	}

	public static boolean mediaMount() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

}
