package com.reptilesoft.mediainventory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddNewItem extends Activity implements OnClickListener, OnItemSelectedListener {
	
	String barcode = "";
	EditText edit_text_barcode,
		edit_text_title,
		edit_text_artist,
		edit_text_year,
		edit_text_qty;
	Spinner spinner_format;
	Button btn_submit_new_item;
	
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.addnewitem);
		
		// get intent extras, show form
		getViews();
		getBarcode();
	}
	
	void getBarcode(){
		Bundle scan_extras = getIntent().getExtras();
		if(scan_extras != null){
			barcode = scan_extras.getString("barcode");
			edit_text_barcode.setText(barcode);
		}
	}
	
	void getViews(){
		
		// editable text controls
		edit_text_barcode = (EditText)findViewById(R.id.edit_text_barcode);
		edit_text_title = (EditText)findViewById(R.id.edit_text_title);
		edit_text_artist = (EditText)findViewById(R.id.edit_text_artist);
		edit_text_year = (EditText)findViewById(R.id.edit_text_year);
		edit_text_qty = (EditText)findViewById(R.id.edit_text_quantity);
		
		// format spinner
		spinner_format = (Spinner) findViewById(R.id.spinner_media_format);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.string_array_spinner_format, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner_format.setAdapter(adapter);
	    // setup onclick listener for spinner
	    spinner_format.setOnItemSelectedListener(this);
	    
	    // buttons
	    btn_submit_new_item = (Button)findViewById(R.id.btn_submit_new_item);
	    btn_submit_new_item.setOnClickListener(this);
	}
	
	void validateInput(){
		
	}
	
	void addToInventory(){
			
		int year=0, qty=0;
		InventoryData inventory_db = new InventoryData(this);
		
		try {
			inventory_db.addNewMedia(
					spinner_format.getSelectedItem().toString(), // format 
					barcode, // barcode 
					edit_text_artist.getText().toString(), // artist 
					edit_text_title.getText().toString(), // title 
					"", // genre
					Integer.parseInt("" + edit_text_year.getText()), // year
					Integer.parseInt("" + edit_text_qty.getText())); // qty
		}
		finally{
			inventory_db.close();
			this.finish();
		}
	}

	public void onClick(View view) {
		switch(view.getId()){
		case R.id.btn_submit_new_item:{
			addToInventory();
		}break;
		default:break;
		}
		
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
		if(spinner_format.getSelectedItem() == "DVD"){
			// enable/disable form elements as applicable
		}
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
