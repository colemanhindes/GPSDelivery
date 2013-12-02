package edu.calpoly.gpsdatabase01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_ITEMS = "items";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ITEM = "item";
	public static final String COLUMN_ITEM_NAME = "item_name";
	public static final String COLUMN_ITEM_DESC = "item_desc";
	public static final String COLUMN_ITEM_PRICE = "item_price";
	public static final String COLUMN_ITEM_TAX = "item_tax";
	public static final String COLUMN_STORE_NAME = "store_name";
	public static final String COLUMN_ITEM_IMAGE = "item_image";

	// Figure out database location
	private static final String DATABASE_NAME = "items.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
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
		database.execSQL(DATABASE_CREATE);
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
