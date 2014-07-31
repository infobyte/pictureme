/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme.entities;

import com.pictureme.managers.ConfigurationManager;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements
        SurfaceHolder.Callback {
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    public Boolean isDestroyed = false;

    // Constructor that obtains context and camera
    public CameraPreview(Context context, Camera camera) {
        super(context);
        try
        {
        	Log.w("pictureme", "CameraPreview Instance");
            this.mCamera = camera;
            this.mSurfaceHolder = this.getHolder();
            this.mSurfaceHolder.setFixedSize(100, 100);
            this.mSurfaceHolder.addCallback(this);
            this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            setZOrderOnTop(true);
            Log.w("pictureme", "CameraPreview InstanceEND");
        }
        catch (Exception e)
        {
        	ConfigurationManager.writeLog(e);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
    		Log.d("pictureme", "surfaceCreated");

			mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
            
            Log.d("pictureme", "surfaceCreatedEND");
        } catch (Exception e) {
        	Log.w("pictureme", "surfaceCreated ERROR");
        	ConfigurationManager.writeLog(e);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    	Log.d("pictureme", "surfaceDestroyed & End");
    	this.isDestroyed = true;
        mCamera.stopPreview();
//        mCamera.release();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
            int width, int height) {
        // start preview with new settings
        try {
        	Log.d("pictureme", "surfaceChanged");
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
            Log.d("pictureme", "surfaceChangedEND");
        } catch (Exception e) {
        	ConfigurationManager.writeLog(e);
        }
    }
    
    public void finalize()
    {
    	Log.d("pictureme", "SurfacePreview finalize :(");
    }
}