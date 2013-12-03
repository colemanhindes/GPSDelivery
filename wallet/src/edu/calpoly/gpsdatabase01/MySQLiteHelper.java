package edu.calpoly.gpsdatabase01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.android.gms.samples.wallet.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_ITEMS = "items";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ITEM = "item";
	public static final String COLUMN_ITEM_DESC = "item_desc";
	public static final String COLUMN_ITEM_PRICE = "item_price";
	public static final String COLUMN_ITEM_TAX = "item_tax";
	public static final String COLUMN_STORE_NAME = "store_name";
	public static final String COLUMN_ITEM_IMAGE = "item_image";

	// Figure out database location
	public static final String DATABASE_NAME = "items.db";
	public static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table if not exists "
			+ TABLE_ITEMS + "(" 
			+ COLUMN_ID	+ " integer primary key autoincrement, " 
			+ COLUMN_ITEM + " text not null," 
			+ COLUMN_ITEM_DESC + " text not null,"
			+ COLUMN_ITEM_PRICE + " decimal(8,2) not null," // check these decimal values
			+ COLUMN_ITEM_TAX + " decimal(8,2) not null,"
			+ COLUMN_STORE_NAME + " text not null,"
			+ COLUMN_ITEM_IMAGE + " integer not null" // check this integer value
			+");";
	

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Create table
		database.execSQL(DATABASE_CREATE);
		// Add items to db / ensure items are there
		
		/*URL url;
		try {
			Log.d("glen", "got to add");
			
			url = new URL("http://lib-staging.calpoly.edu/ds/gsbeebe/android/item_list.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String oneItem;
			String[] data;
			String insertSQL;
			while((oneItem = in.readLine()) != null) {
				data = oneItem.split(",");
				insertSQL = "insert into " + TABLE_ITEMS
						+ "(" + COLUMN_ITEM + "," + COLUMN_ITEM_DESC + "," + COLUMN_ITEM_PRICE + "," + COLUMN_ITEM_TAX + "," + COLUMN_STORE_NAME + "," + COLUMN_STORE_NAME + "," + COLUMN_ITEM_IMAGE + ")"
						+ "values (" 
						+ data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + data[5] + "," + R.drawable.fattes + ");";
				database.execSQL(insertSQL);
				
			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
		onCreate(db);
	}
}
