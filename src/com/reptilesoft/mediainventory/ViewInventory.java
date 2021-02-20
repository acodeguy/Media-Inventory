package com.reptilesoft.mediainventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

public class ViewInventory extends ListActivity {
	
	ListView list_view_inventory;
	String[] item_title = {"Loading from the list","Failed"};
	int[] item_id;
	Context context_ViewInventory;
	
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		
		//setContentView(R.layout.viewinventory);
		
		// get context
		context_ViewInventory = getApplicationContext();
		
		// build list
		buildInventory();
	}
	
	void setupListView(){
		list_view_inventory = getListView();
		list_view_inventory.setTextFilterEnabled(true);
		list_view_inventory.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Log.i("onItemClick()", "ID: " + item_id[position] + ", TITLE: " + item_title[position]);
				
				// launch a new activity to view/edit/delete details
				Intent intent_detail = new Intent(context_ViewInventory,ViewEditDeleteItem.class);
				intent_detail.putExtra("_id", item_id[position]);
				intent_detail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context_ViewInventory.startActivity(intent_detail);
				}
		});
	}
	
	void getInventory(){
		
		Cursor cursor_item = null;
		
		InventoryData inventory_data = new InventoryData(this);
		cursor_item = inventory_data.getTitles();
		
		if(cursor_item.getCount()>0){
			item_title = new String[cursor_item.getCount()];
			item_id = new int[cursor_item.getCount()];
			int index_title = cursor_item.getColumnIndexOrThrow("title");
			int index_id = cursor_item.getColumnIndexOrThrow("_id");
			int index_artist = cursor_item.getColumnIndexOrThrow("director");
			int counter = 0;
			
			cursor_item.moveToFirst();
			do {
				Log.i("getInventory","Adding to spinner: " + cursor_item.getString(index_title));
				item_title[counter] = cursor_item.getString(index_title) + " (" + cursor_item.getString(index_artist) + ")";
				item_id[counter] = cursor_item.getInt(index_id); // assign id into array, for later use
				counter++;
			} while(cursor_item.moveToNext());
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, item_title));
		} else {
			setListAdapter(null);
		}
		
		// close up and return
		inventory_data.close();
		cursor_item.close();
	}	
	
	void buildInventory(){
		setupListView();
		getInventory();
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, item_title));
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		// rebuid inventory
		buildInventory();
	}
	
}
