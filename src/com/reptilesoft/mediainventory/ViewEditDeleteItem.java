package com.reptilesoft.mediainventory;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ViewEditDeleteItem extends Activity implements OnClickListener {
	
	int _id = 0; // id from previous activity
	String barcode = "";
	EditText edit_text_barcode,
		edit_text_title,
		edit_text_artist,
		edit_text_year,
		edit_text_qty;
	Spinner spinner_format;
	Button button_edit_item, button_delete_item;
	
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.viewedititem);
		
		// get views
		getViews();
		
		// populate
		populateFields();
	}
	
	void populateFields(){ 
		
		// first, get intent extras
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			_id = extras.getInt("_id");
			InventoryData inventory_data = new InventoryData(this);
			Cursor cursor_inventory = inventory_data.getDetail(_id);
			cursor_inventory.moveToFirst();
			edit_text_barcode.setText(cursor_inventory.getString(cursor_inventory.getColumnIndexOrThrow("barcode")));
			edit_text_title.setText(cursor_inventory.getString(cursor_inventory.getColumnIndexOrThrow("title")));
			edit_text_artist.setText(cursor_inventory.getString(cursor_inventory.getColumnIndexOrThrow("director")));
			edit_text_year.setText("" + cursor_inventory.getInt(cursor_inventory.getColumnIndexOrThrow("year")));
			edit_text_qty.setText("" + cursor_inventory.getInt(cursor_inventory.getColumnIndexOrThrow("quantity")));
			//spinner_format.setSelection(position)
			
			// finish up
			inventory_data.close();
			cursor_inventory.close();
			
		} else {
			Toast.makeText(this, "Invalid request.", Toast.LENGTH_SHORT).show();
			this.finish();
		}
	}
	
	void getViews(){
		edit_text_barcode = (EditText)findViewById(R.id.edit_text_barcode);
		edit_text_title = (EditText)findViewById(R.id.edit_text_title);
		edit_text_artist = (EditText)findViewById(R.id.edit_text_artist);
		edit_text_year = (EditText)findViewById(R.id.edit_text_year);
		edit_text_qty = (EditText)findViewById(R.id.edit_text_quantity);
		button_delete_item = (Button)findViewById(R.id.button_delete_item);
		button_delete_item.setOnClickListener(this);
		button_edit_item = (Button)findViewById(R.id.button_edit_item);
		button_edit_item.setOnClickListener(this);
		spinner_format = (Spinner)findViewById(R.id.spinner_media_format);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.string_array_spinner_format, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_format.setAdapter(adapter);
	}

	public void onClick(View view) {
		
		switch(view.getId()){
		
		case R.id.button_delete_item:
		{
			InventoryData inventory = new InventoryData(this);
			inventory.deleteItem(_id);
			inventory.close();
			this.finish();
		} break;
		
		case R.id.button_edit_item:
		{
			InventoryData inventory = new InventoryData(this);
			inventory.updateItem(_id, 
					edit_text_artist.getText().toString(), 
					edit_text_title.getText().toString(), 
					"X", 
					Integer.parseInt(edit_text_year.getText().toString()), 
					Integer.parseInt(edit_text_qty.getText().toString()));
			inventory.close();
			this.finish();
		} break;
		default:break;
		}
	}
}
