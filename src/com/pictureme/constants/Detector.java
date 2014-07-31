/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme.constants;

//import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import org.me.constant.Constant;

import com.pictureme.camera.CameraImplements;

public class Detector extends PreferenceActivity implements Constant, Preference.OnPreferenceClickListener {

	private ListPreference mCamera;
	private ListPreference mStart;
	private Preference mPreview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onResume() {
		super.onResume();
		switch (Engine.getState()) {
			case Active.SERVICE_CHANGING:
			case Active.SERVICE_ENABLED:
				mCamera.setEnabled(false);
				mStart.setEnabled(false);
				mPreview.setEnabled(false);
			break;
			case Active.SERVICE_DISABLED:
				mCamera.setEnabled(CameraImplements.isMultiCamera());
				mStart.setEnabled(true);
				mPreview.setEnabled(true);
			break;
		}
	}

	public boolean onPreferenceClick(Preference preference) {
		if (preference.getKey().equals("selectThreshold")) {
		} else if (preference.getKey().equals("showPreview")) {
//			startActivity(new Intent(Detector.this, Preview2.class));
		}
		return true;
	}


}
