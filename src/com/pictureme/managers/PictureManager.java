/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.FrameLayout;
import com.pictureme.R;
import com.pictureme.entities.AlbumData;

public class PictureManager{
	public Camera cam;
	public Context viewContext;
	public FrameLayout fLayout;
	private ShutterCallback scParam;
	
	public String data_path = "";
	
	private PreviewCallback prevCall = new PreviewCallback() {
		
		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {

			cam.takePicture(scParam, null, pCall);
		}
	};	
	
	private PictureCallback pCall = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if (data != null)
				try {
					savePicture(data);
					cam.stopPreview();
				} 
				catch (Exception e) {
					ConfigurationManager.writeLog(e);
				}
		}
	};	
	
	
	public PictureManager()
	{
		this.data_path = getStorageDataPath();
	}
	
	
	private ShutterCallback sCall = new ShutterCallback() {
		
		@Override
		public void onShutter() {

		}
	};
	
	public void takePicture(Context vwContext, Boolean savePicture)
	{
		viewContext = vwContext;

		try {
			Log.w("PictureMe", "Taking Picture");

			if (getShutterSoundEnabled())
				scParam = this.sCall;
			else
				scParam = null;
			
			Camera.Parameters params = cam.getParameters();
			params.set("jpeg-quality", getQualityFromPreferences());
			params.set("orientation", "landscape");
			params.set("rotation", 90);
			params.setPictureFormat(PixelFormat.JPEG);

			Size pictureSize = getPictureSize(params);
			params.setPictureSize(pictureSize.width, pictureSize.height);
			
			cam.setParameters(params);

			cam.stopPreview();
			cam.setDisplayOrientation(0);
			cam.startPreview();

//			cam.takePicture(scParam, null, pCall);
			cam.setOneShotPreviewCallback(prevCall);
			
		}	
		catch(Exception e)
		{
			ConfigurationManager.writeLog(e);
		}
		finally{
		}
		
	}
	
	private void savePicture(byte[] pictureData) throws Exception
	{
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");

			// Create the albumname, filename, and full path
			String albumName = "pictureme-" + sdf.format(System.currentTimeMillis());
			String fileName = System.currentTimeMillis() + ".jpg";
			String imagePath = albumName + "/" + fileName;
			
			
			// Get the file stream to the directory.
			// If the fileBase is null, throw an IOException,
			// because the storage is not ready or present
			File fileBase = getStorageFilePath();
			if (fileBase == null)
			{
				throw new IOException("La unidad no existe o no se puede leer.");
			}
			
			File path=new File(fileBase, "DATA/"+albumName);
			
			// If the directory doen't exists, let's create the folder and the metadata
			if (!path.exists())
			{
				path.mkdirs();
				AlbumData album = new AlbumData();
				album.name = albumName;
				new AlbumManager().saveAlbumMetadata(album, this.viewContext);
			}
			
			FileOutputStream  outStream = new FileOutputStream(path.getAbsoluteFile() + "/" + fileName);	
			outStream.write(pictureData);
			outStream.close();
			
			// Add the picture to the android gallery
			//galleryAddPic(viewContext,path.getAbsolutePath() + "/" + fileName);
            			
		} catch (IOException e) {
			ConfigurationManager.writeLog(e);
		}
		
	}
	
	private void galleryAddPic(Context context,String mCurrentPhotoPath) {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    context.sendBroadcast(mediaScanIntent);
	}
	
	
	public boolean isCameraUsedByApp() {
	    Camera camera = null;
	    try {
	        camera = Camera.open();
	    } catch (RuntimeException e) {
	        return true;
	    } finally {
	        if (camera != null) camera.release();
	    }
	    return false;
	}
	
	public static File getStorageFilePath()
	{
		String state = Environment.getExternalStorageState();
		File file = null;
		
		if(Environment.MEDIA_MOUNTED.equals(state))
		{
			try
			{

				file = Environment.getExternalStorageDirectory();
			}
			catch (Exception ex)
			{
				file = null;
			}
		}

		return file;
	}
	
	private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }
	
	private String getStorageDataPath()
	{
		return getStorageFilePath().getAbsolutePath() + "/DATA/";
	}
	
	private int getQualityFromPreferences()
	{
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(viewContext.getApplicationContext());
		return Integer.parseInt(settings.getString("picture_quality", viewContext.getString(R.string.DEFAULT_IMAGE_QUALITY))) ;
	}
	
	private boolean getShutterSoundEnabled()
	{
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(viewContext.getApplicationContext());

		return settings.getBoolean("use_shutter_sound", Boolean.parseBoolean(viewContext.getString(R.string.DEFAULT_SHUTTER_SOUND_ENABLED)));
	}
	
	private Size getPictureSize(Parameters params)
	{
		Size selectedSize = null;
		List<Size> lst = null;
		lst = params.getSupportedPictureSizes();

		switch(getQualityFromPreferences())
		{
			case 50:
				selectedSize = lst.get(0);
				break;
			case 75:
				selectedSize = lst.get((int)Math.round(lst.size()/2));
				break;
			case 100:
				selectedSize = lst.get(lst.size()-1);
				break;
		
		}
		return selectedSize;
		
	}

}
