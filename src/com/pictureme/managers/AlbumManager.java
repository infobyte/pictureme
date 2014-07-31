/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlSerializer;
import android.content.Context;
import android.util.Xml;
import com.pictureme.entities.*;

public class AlbumManager {
	public void saveAlbumMetadata(AlbumData album, Context context) throws Exception
	{
		XmlSerializer xmlSer = Xml.newSerializer();
		StringWriter sWriter = new StringWriter();
		FileOutputStream fileStream;
		
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			album.dateCreated = sdf.format(date);
			album.name = "pictureme-" + album.dateCreated;
			album.dataFilePath = PictureManager.getStorageFilePath() + "/DATA/" + album.name; // + "/album.md";
			
			xmlSer.setOutput(sWriter);
			xmlSer.startDocument("UTF-8", true);
			
			xmlSer.startTag("", "Album");
			
			xmlSer.startTag("", "DateCreated");
			xmlSer.text(album.dateCreated);
			xmlSer.endTag("", "DateCreated");
			
			xmlSer.startTag("", "Name");
			xmlSer.text(album.name);
			xmlSer.endTag("", "Name");
			
			xmlSer.endTag("", "Album");
			xmlSer.endDocument();
			
			File path = new File(album.dataFilePath);
			
			if (!path.exists())
				path.mkdirs();
			
			fileStream = new FileOutputStream(path.getAbsoluteFile() + "/album.md");
			fileStream.write(sWriter.toString().getBytes());
			xmlSer.flush();
			fileStream.close();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	
	public boolean albumExists(String name)
	{
		return new File(name).exists();
	}
	
	public AlbumData getAlbumMetadata(String albumPath) throws Exception
	{
		File fXmlFile = new File(albumPath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		try
		{
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName("Album");
			
			AlbumData album = null;
			if(nodeList.getLength() > 0)
			{
				Node node = nodeList.item(0);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element)node;
					album = new AlbumData();
					album.name = element.getElementsByTagName("Name").item(0).getTextContent();
					album.dateCreated = element.getElementsByTagName("DateCreated").item(0).getTextContent();
					album.dataFilePath = albumPath;
				}	
			}
			return album;
			
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
}
