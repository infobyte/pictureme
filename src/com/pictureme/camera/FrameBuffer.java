/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme.camera;

import android.graphics.Rect;

public class FrameBuffer {

	private volatile boolean mState = false;
	private byte[] mArray = null;
	private int mWidth;
	private int mHeight;

	public FrameBuffer(int size, int width, int height) {
		mWidth = width;
		mHeight = height;
		mArray = new byte[size];
	}

	public byte[] getFrame() {
		return mArray;
	}

	public int getLength() {
		if (mArray != null) {
			return mArray.length;
		}
		return 0;
	}

	public boolean isAcquire() {
		return !mState;
	}

	public void setAcquire() {
		mState = true;
	}

	public void remAcquire() {
		mState = false;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public Rect getRect() {
		return new Rect(0, 0, mWidth, mHeight);
	}

}

