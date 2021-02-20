package com.reptilesoft.mediainventory;

import java.io.File;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

public class Synchronize extends Activity {
	
	Button button_synchronize;
	
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.synchronize);
		
		// get the sd card
		File sdcard = Environment.getExternalStorageDirectory();
		//Toast.makeText(this, getInventory(), Toast.LENGTH_LONG).show();
		//sdcard.createNewFile();
		// submit to server
		Poster sync = new Poster();
		String response = sync.postInventory(getInventory());
		Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
		
	}
	
	boolean submitToServer(){
		
		boolean result = false;
		
		// attempt submission here
		Poster sync = new Poster();
		String response = sync.postInventory(getInventory());
		
		return result;
	}
	
	String getInventory(){
		// get the entire inventory and spit an xml file of it for posting to web site
		
		String inventory_xml = "<?xml version=\"1.0\"?>";
		
		// get data, add to string
		InventoryData data = new InventoryData(this);
		Cursor inventory = data.returnInventory(); // get everything!
		
		if(inventory.getCount() > 0){
			
			inventory.moveToFirst();
			do {
				inventory_xml += "<item><id>" + inventory.getInt(inventory.getColumnIndexOrThrow("_id")) +
						"</id><title>" + inventory.getString(inventory.getColumnIndexOrThrow("title")) +
						"</title><artist>" + inventory.getString(inventory.getColumnIndexOrThrow("director")) +
						"</artist><barcode>" + inventory.getString(inventory.getColumnIndexOrThrow("barcode")) +
						"</barcode><year>" + inventory.getInt(inventory.getColumnIndexOrThrow("year")) +
						"</year><qty>" + inventory.getInt(inventory.getColumnIndexOrThrow("quantity")) + "</qty></item>";
			} while(inventory.moveToNext());
		}
		
		return inventory_xml;
	}

}
