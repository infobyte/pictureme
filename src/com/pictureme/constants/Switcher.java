/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme.constants;

public class Switcher {

	private volatile boolean mSwitch = false;

	public void setSwitchOn() {
		mSwitch = true;
	}

	public void setSwitchOff() {
		mSwitch = false;
	}

	public boolean isSwitchOn() {
		return mSwitch;
	}

}
