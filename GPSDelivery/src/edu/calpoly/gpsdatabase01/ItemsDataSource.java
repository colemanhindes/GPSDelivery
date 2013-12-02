package edu.calpoly.gpsdatabase01;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.samples.wallet.Constants;
import com.google.android.gms.samples.wallet.ItemInfo;

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
	
	public ItemsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Item createItem(String item) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ITEM, item);
		long insertId = database.insert(MySQLiteHelper.TABLE_ITEMS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEMS,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Item newItem = cursorToItem(cursor);
		cursor.close();
		return newItem;
	}

	public void deleteItem(Item item) {
		long id = item.getId();
		System.out.println("Item deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_ITEMS, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public ArrayList<ItemInfo> getAllItems() {
		ArrayList<ItemInfo> items = new ArrayList<ItemInfo>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEMS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ItemInfo item = cursorToItemInfo(cursor);
			items.add(item);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return items;
	}

	private ItemInfo cursorToItemInfo(Cursor cursor) {
		ItemInfo item = new ItemInfo(cursor.getString(1), // Item name
									 cursor.getString(2), // Item desc
									 cursor.getLong(3),   // Item price
									 cursor.getLong(4),   // Item tax
									 Constants.CURRENCY_CODE_USD, // Country code
									 cursor.getString(5), // Store name
									 cursor.getInt(6));   // Item image
		return item;
	}
	
	/*public ItemInfo(String name, String description, long price, long shippingPrice,
            String currencyCode, String sellerData, int imageResourceId) {
      */

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