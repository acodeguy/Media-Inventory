package com.reptilesoft.mediainventory;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class InventoryData extends SQLiteOpenHelper {

	/* variables */
	final static String TAG="InventoryData"; // for logging
	final static String DB_NAME="mediainventory.db";
	final static int DB_VERSION=4;
	final static String TBL_INVENTORY="item";
	final static String QRY_CREATE_INVENTORY_TABLE = "CREATE TABLE " + TBL_INVENTORY +
					"(_id integer primary key autoincrement," +
					"barcode text not null unique," +
					"format text," + // BR, DVD, HD, etc
					"director text," +
					"title text," +
					"year integer," +
					"quantity integer);";
	
	SQLiteDatabase sqldb;
		
	public InventoryData(Context context) {
		super(context,DB_NAME,null,DB_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/* db is create for first time, create tables */
		Log.i("onCreate","Creating db");
		
		try
			{
				// create the movie table
				db.execSQL(QRY_CREATE_INVENTORY_TABLE); 
			}catch(Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/* upgrading db, alter tables, drop? */
		
		Log.i("onUpgrade()","Dropping table");
		sqldb.execSQL("drop table " + TBL_INVENTORY + ";");
		
		Log.i("onUpgrade()","Creating new table after dropping table");
		sqldb.execSQL(QRY_CREATE_INVENTORY_TABLE);
		
	}
		
	public void addNewMedia(
			String format, 
			String barcode, 
			String authorArtistDirector, 
			String title,
			String genre,
			int year,
			int quantity) {
		// TODO: search for dupes
		
		try
		{
			sqldb=this.getWritableDatabase();
			sqldb.execSQL("INSERT INTO " + TBL_INVENTORY + "(format, barcode, director, title, year, quantity)" +
					"VALUES('" + format + "'" + 
					"," + barcode +
					",'" + authorArtistDirector + "'" +
					", '" + title + "'" +
					"," + year +
					"," + quantity +
					");"
				);
		}catch(Exception e) {
			e.printStackTrace();
			}finally{sqldb.close();}
	}
	
	boolean checkIfBarcodeExists(String barcode){
		
		boolean result = false;
		Cursor cursor_inventory = null;
		
		try {
			sqldb = this.getReadableDatabase();
			cursor_inventory = sqldb.rawQuery("select _id from " + TBL_INVENTORY + " where barcode=" + barcode, null);
			Log.i("checking barcode","FOUND BARCODES: " + cursor_inventory.getCount());
			if(cursor_inventory.getCount() == 0){
				// doesn exists, return true
				Log.i("checkIfBarcodeExists()","Doesn't exist, carrying on.");
				return false;
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			
		}
		
		Log.i("checkIfBarcodeExists()","Dupe found, returning false.");
		return true;
	}
	
	Cursor getFormats(){
		
		Cursor format_list = null;
		
		try {
			sqldb = this.getReadableDatabase();
			format_list = sqldb.rawQuery("select distinct(format) from " + TBL_INVENTORY + " order by format asc;", null);
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			
		}
		
		return format_list;
	}
	
	Cursor getDetail(int _id){
		
		Cursor item_data = null;
		
		try {
			sqldb = this.getReadableDatabase();
			item_data = sqldb.rawQuery("select * from " + TBL_INVENTORY + " where _id=" + _id, null);
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			
		}
		
		return item_data;
	}
	
	Cursor getTitles(){
		
		Log.d("getTitles","started");
		
		Cursor titles_list = null;
		
		try {
			sqldb = this.getReadableDatabase();
			titles_list = sqldb.rawQuery("select title,_id,director from " + TBL_INVENTORY + " order by title asc;", null);
		} catch(Exception e){
			e.printStackTrace();
		}finally {
			
		}
		
		return titles_list;
	}
	
	public Cursor returnInventory() {
		// return entire inventory
		
		Cursor inventory = null;
		
		try{
			sqldb=this.getReadableDatabase();
			inventory = sqldb.rawQuery("select * from " + TBL_INVENTORY + " order by title asc;", null);
		} catch(Exception e){
			e.printStackTrace();
		}
			finally {
				
			}
		return inventory;

	}
	
	public void deleteItem(int _id) {
		/* delete an item from the db 
		 * mediaType is critical to access correct table
		 * */
		
		try{
			sqldb=this.getWritableDatabase();
			sqldb.execSQL("DELETE FROM " + TBL_INVENTORY + " WHERE _id=" + _id + ";");
		} catch(Exception e){
			e.printStackTrace();
		}
			finally{sqldb.close();
		}

	}
	
	public void updateItem(int _id, String artist, String title, String format, int year, int quantity){
		// update and already existing item
		
		Log.i("updateItem()","Called on _id: #" + _id);
		
		try{
			sqldb=this.getWritableDatabase();
			sqldb.execSQL("update " + TBL_INVENTORY + 
					" set director = '" + artist + "'," +
					" title = '" + title + "'," +
					" format = '" + format + "'," +
					" year = " + year + "," +
					" quantity = " + quantity +
					" where _id=" + _id + ";");
		} catch(Exception e){
			e.printStackTrace();
		}
			finally{sqldb.close();
		}
	}

}
