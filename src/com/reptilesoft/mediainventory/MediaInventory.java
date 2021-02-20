package com.reptilesoft.mediainventory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MediaInventory extends Activity implements OnClickListener{
	
	Button button_view_library, button_synchronize;
	ImageButton button_scan_new;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.main);
        
        button_scan_new = (ImageButton)findViewById(R.id.button_scan_barcode);
        button_scan_new.setOnClickListener(this);     
        button_view_library = (Button)findViewById(R.id.button_view_library);
        button_view_library.setOnClickListener(this);
        button_synchronize = (Button)findViewById(R.id.button_synchronize);
        button_synchronize.setOnClickListener(this);
    }

    public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.button_scan_barcode:
		{
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	        intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
	        startActivityForResult(intent, 0);
		} break;
		case R.id.button_view_library:
		{
			Intent intent_view_inventory = new Intent(this,ViewInventory.class);
			this.startActivity(intent_view_inventory);
		} break;
		case R.id.button_synchronize:
		{
			Intent intent_synchronize = new Intent(this,Synchronize.class);
			this.startActivity(intent_synchronize);
		}
			default:
				break;
		}
		
	}
	
	public Button.OnClickListener mScan = new Button.OnClickListener() {
	    public void onClick(View v) {
	        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	        startActivityForResult(intent, 0);
	    }
	};

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	            String contents = intent.getStringExtra("SCAN_RESULT");
	            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	            // Handle successful scan
	            //Toast.makeText(this, contents + " scanned OK.", Toast.LENGTH_SHORT).show();

	            // check if already exists in db
	            InventoryData inventory_data = new InventoryData(this);
	            boolean barcode_exists = false;
	            barcode_exists = inventory_data.checkIfBarcodeExists(contents);
	            if(barcode_exists){
	            	// dupe netry attempt
	            	Toast.makeText(this, "Entry already exists, please scan another.", Toast.LENGTH_SHORT).show();
	            } else {
	            	// TODO: search online db for entry, save automatically if found, else show entry form
	            	
	            	// start a new activity with a form, barcode already filled in
		            Intent intent_AddNewItem = new Intent(this,AddNewItem.class);
		            intent_AddNewItem.putExtra("barcode", contents);
		            this.startActivity(intent_AddNewItem);
	            }
	            
	            // close the db
	            inventory_data.close();
	            
	        } else if (resultCode == RESULT_CANCELED) {
	            // Handle cancel
	        }
	    }
	}
}