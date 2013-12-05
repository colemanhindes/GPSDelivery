package edu.calpoly.gpsdatabase01;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.samples.wallet.Constants;
import com.google.android.gms.samples.wallet.ItemInfo;
import com.google.android.gms.samples.wallet.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ItemsDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { 
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_ITEM,
			MySQLiteHelper.COLUMN_ITEM_DESC,
			MySQLiteHelper.COLUMN_ITEM_PRICE,
			MySQLiteHelper.COLUMN_ITEM_TAX,
			MySQLiteHelper.COLUMN_STORE_NAME,
			MySQLiteHelper.COLUMN_ITEM_IMAGE
	};
	
	public static List<ItemInfo> items;
	
	public ItemsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		
		// Call addItems() here!!
	}

	public void close() {
		dbHelper.close();
	}

	public ItemInfo createItem(String item) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ITEM, item);
		long insertId = database.insert(MySQLiteHelper.TABLE_ITEMS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEMS,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		ItemInfo newItem = cursorToItemInfo(cursor);
		cursor.close();
		return newItem;
	}

	public void deleteItem(Item item) {
		long id = item.getId();
		System.out.println("Item deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_ITEMS, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<ItemInfo> getAllItems() {
		
		// Start by getting rid of table
		database.execSQL("DELETE FROM items WHERE 1;");
		
		List<ItemInfo> items = new ArrayList<ItemInfo>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEMS,
				allColumns, null, null, null, null, null);

		// Add items if they are not already in database
		if(cursor.getCount() == 0) {
			addItems();
			cursor = database.query(MySQLiteHelper.TABLE_ITEMS,
					allColumns, null, null, null, null, null);
		}
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ItemInfo item = cursorToItemInfo(cursor);
			items.add(item);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		ItemsDataSource.items = items;
		return items;
	}
	
	
	// Method to add items to the database -- should connect to a textfile online
	// Need to figure out the image src problem...
	public void addItems() {
		// Insert a fake item for now
		String sql = "insert into items (item, item_desc, item_price, item_tax, store_name, item_image) " +
						"values ('Large Pizza','A large pepperoni pizza',20000000,3000000,'A pizzaria'," + R.drawable.pizza +");";
		
		database.execSQL(sql);
	 
		sql = "insert into items (item, item_desc, item_price, item_tax, store_name, item_image) " +
						"values ('Hot Dog','A plain hot dog',2500000,3000000,'A hot dog stand'," + R.drawable.hotdog +");";

		database.execSQL(sql);
		
		sql = "insert into items (item, item_desc, item_price, item_tax, store_name, item_image) " +
						"values ('Turkey Sandwich','A turkey sandwich',4000000,3000000,'A deli'," + R.drawable.turkey +");";
				
		database.execSQL(sql);
	}
	
	public ItemInfo cursorToItemInfo(Cursor cursor) {
		ItemInfo item = new ItemInfo(cursor.getString(1), 		 // Item name
									 cursor.getString(2),		 // Item description
									 cursor.getLong(3),			 // Item price
									 cursor.getLong(4),  		 // Item delivery cost
									 Constants.CURRENCY_CODE_USD,// Currency code -> USD
									 cursor.getString(5),		 // Store name
									 cursor.getInt(6)			 // Image ID
									 );
		return item;
	}
	
	/*public ItemInfo(String name, String description, long price, long shippingPrice,
            String currencyCode, String sellerData, int imageResourceId) {*/

	private Item cursorToItem(Cursor cursor) {
		Item item = new Item();
		item.setId(cursor.getLong(0));
		item.setItem(cursor.getString(1));
		item.setDesc(cursor.getString(2));
		item.setPrice(cursor.getLong(3));
		item.setTax(cursor.getLong(4));
		item.setStore(cursor.getString(5));
		item.setImage(cursor.getInt(6));		
		return item;
	}
}