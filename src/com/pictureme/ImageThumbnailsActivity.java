/*
Pictureme
Copyright (C) 2014  Infobyte LLC (http://www.infobytesec.com/)
See the file 'doc/LICENSE' for the license information
*/

package com.pictureme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pictureme.constants.Extras;
import com.pictureme.managers.ConfigurationManager;
import com.pictureme.managers.PictureManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ImageThumbnailsActivity extends Activity {

	GridView imagegrid;
	TextView titulo;
	String[] imageUrls;

	DisplayImageOptions options;
	
	protected AbsListView listView;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_thumbnails);
		iniciarGrid();
	}

	
	@SuppressLint("NewApi")
	private void iniciarGrid(){
		try
		{
			getIntent().getExtras();
			imageUrls = this.obtenerImagenes(ListDirectory.PATH_SELECCIONADO);
	
			options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
	
			listView = (GridView) findViewById(R.id.PhoneImageGrid);
			
			listView.setAdapter(new ImageAdapter());
			
			((GridView) listView).setAdapter(new ImageAdapter());
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					startImagePagerActivity(position);
				}
			});
		}	
		catch(Exception ex)
		{
			ConfigurationManager.writeLog(ex);
		}
	}
	
	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ViewImage.class);
		intent.putExtra(Extras.IMAGES, imageUrls);
		intent.putExtra(Extras.IMAGE_POSITION, position);
		startActivity(intent);
		
	}
	
	@SuppressLint("SdCardPath")
	private String[] obtenerImagenes(String carpeta) {
		List<String> fileList = new ArrayList<String>();
		String[] pathImagenes = null;
		
		File root = new File(new PictureManager().data_path + carpeta + "/");

		File[] files = root.listFiles();
		fileList.clear();
		List<String> listPath = new ArrayList<String>();
		ArrayList<String> arrayList = new ArrayList<String>(listPath);
		
		
		for (File file : files) {
			if(file.getName().equals("album.md") == false)
			{
				arrayList.add("file://" + file.getPath());
				pathImagenes = arrayList.toArray(new String[listPath.size()]);
			}
		}

		return pathImagenes;
	}

	
	public void finishActivity(View v) {
		finish();
	}
	
	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
			} else {
				imageView = (ImageView) convertView;
			}

			
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.LIFO)
			.build();
			ImageLoader.getInstance().init(config);
			
			
			imageLoader.displayImage(imageUrls[position], imageView, options);

			return imageView;
		}
	}

}
