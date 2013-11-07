package edu.calpoly.gpsdatabase01;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ItemsDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_ITEM };

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

	public List<Item> getAllItems() {
		List<Item> items = new ArrayList<Item>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEMS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Item item = cursorToItem(cursor);
			items.add(item);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return items;
	}

	private Item cursorToItem(Cursor cursor) {
		Item item = new Item();
		item.setId(cursor.getLong(0));
		item.setItem(cursor.getString(1));
		return item;
	}
} 
