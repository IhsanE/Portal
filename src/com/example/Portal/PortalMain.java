package com.example.Portal;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import com.example.portal_v1.R;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class PortalMain extends Activity {

	RenderGame LJS;
	DisplayMetrics metrics;
	Bitmap bmp;
	Utilities ls = new Utilities();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_portal_main);
		
		
		        DisplayMetrics metrics = new DisplayMetrics();
		        getWindowManager().getDefaultDisplay().getMetrics(metrics);
		        int height = metrics.heightPixels;
		        int width = metrics.widthPixels;
					LJS =new RenderGame(this, height, width,ls);
			
		        setContentView(LJS);
		        //getWindow().setBackgroundDrawableResource(R.drawable.androidgameadea_portal) ;
		        
	}
	@Override
	public boolean onCreateOptionsMenu (Menu menu){
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item){
		
		
		switch(item.getItemId()){
		
		
		
		case R.id.menu_LockScreen:
			
			Toast.makeText(PortalMain.this, "Screen is locked", Toast.LENGTH_SHORT).show();
			ls.Locked = true;
			return true;

		case R.id.menu_UnlockScreen:
			
			Toast.makeText(PortalMain.this, "Screen is unlocked", Toast.LENGTH_SHORT).show();
			ls.Locked = false;
			return true;
			default:
			return super.onOptionsItemSelected(item);
		}
		
		
		
	}
	
}
