/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme.managers;

public class ElementManager {
	private int idImagen; 
	private String textoEncima; 
	private String textoDebajo; 
	private String nameFolder;

	public ElementManager (int idImagen, String textoEncima, String textoDebajo, String nameFolder) { 
	    this.idImagen = idImagen; 
	    this.textoEncima = textoEncima; 
	    this.textoDebajo = textoDebajo;
	    this.nameFolder = nameFolder;
	}

	public String get_textoEncima() { 
	    return textoEncima; 
	}

	public String get_textoDebajo() { 
	    return textoDebajo; 
	}

	public int get_idImagen() {
	    return idImagen; 
	}
	public String get_nameFolder() { 
	    return nameFolder; 
	}
}