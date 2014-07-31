/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.pictureme.adapters.ListAdapter;
import com.pictureme.entities.AlbumData;
import com.pictureme.managers.AlbumManager;
import com.pictureme.managers.ConfigurationManager;
import com.pictureme.managers.ElementManager;
import com.pictureme.managers.PictureManager;

public class ListDirectory extends Activity {

	public static String PATH_SELECCIONADO = "";
	public static String NAME_FOLDER = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_directory);
		
		loadGrid();
	}

	@SuppressLint("SimpleDateFormat")
	private Date ConvertToDate(String dateString) throws java.text.ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(dateString);
		} 
		catch (ParseException e) {
			ConfigurationManager.writeLog(e);
		}
		return convertedDate;
	}

	private void loadGrid() {
		try {
			File[] lisFolders = new File(new PictureManager().data_path).listFiles();
			ArrayList<ElementManager> data = new ArrayList<ElementManager>();

			if (lisFolders != null) {
				for (File folder : lisFolders) {

					if (folder.isDirectory()) {
						if(folder.getName().startsWith("pictureme-"))
						{
							try {
								String fechaFormateada;
								
								if (new File(folder.getAbsolutePath() + "/album.md").exists())
								{
									AlbumData album = new AlbumManager().getAlbumMetadata(folder.getAbsolutePath() + "/album.md");
		
									fechaFormateada = DateFormat.getDateInstance(
														DateFormat.MEDIUM).format(ConvertToDate(album.dateCreated));
								}
								else
								{
									fechaFormateada = DateFormat.getDateInstance(
											DateFormat.MEDIUM).format(Calendar.getInstance().getTime());
								}
								
								data.add(new ElementManager(R.drawable.ic_folder,
										fechaFormateada, folder.getPath(), folder
										.getName()));	
							} 
							catch (java.text.ParseException e) {
								ConfigurationManager.writeLog(e);
							}
						}

					}
				}

				ListView lista = (ListView) findViewById(R.id.lstDir);
				lista.setAdapter(new ListAdapter(this,
						R.layout.element_list_directory, data) {

					@Override
					public void onEntry(Object entrada, View view) {
						TextView texto_superior_entrada = (TextView) view
								.findViewById(R.id.textView_superior);
						texto_superior_entrada.setText(((ElementManager) entrada)
								.get_textoEncima());

						TextView texto_inferior_entrada = (TextView) view
								.findViewById(R.id.textView_inferior);
						texto_inferior_entrada.setText(((ElementManager) entrada)
								.get_textoDebajo());

						ImageView imagen_entrada = (ImageView) view
								.findViewById(R.id.imageView_imagen);
						imagen_entrada.setImageResource(((ElementManager) entrada)
								.get_idImagen());

					}

				});

				lista.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> pariente, View view,
							int posicion, long id) {
						ElementManager elegido = (ElementManager) pariente
								.getItemAtPosition(posicion);
						PATH_SELECCIONADO = elegido.get_nameFolder();
						NAME_FOLDER = elegido.get_textoEncima();
						Intent in = new Intent(pariente.getContext(),
								ImageThumbnailsActivity.class);
						startActivity(in);
					}
				});
			}else{
				
			}
		} 
		catch (Exception e) {
			ConfigurationManager.writeLog(e);
		}
	}

	public void finishActivity(View v) {
		finish();
	}

}
