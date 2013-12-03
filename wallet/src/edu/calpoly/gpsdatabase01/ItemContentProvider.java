package edu.calpoly.gpsdatabase01;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Class that provides content from a SQLite database to the application.
 * Provides item information to a ListView through a CursorAdapter. The database stores
 * item data in a two-dimensional table, where each row is an item and each column is a
 * property of an item (ID, name, rating, install URI, whether or not the item is installed
 * on the device).
 * 
 * Note that CursorLoaders require a ContentProvider, which is why this application wraps a
 * SQLite database into a content provider instead of managing the database<-->application
 * transactions manually.
 */
public class ItemContentProvider extends ContentProvider {

	/*****************/
	/** STATIC DATA **/
	/*****************/
	
	/** The app database. */
	private MySQLiteHelper database;
	
	/** Values for the URIMatcher. */
	private static final int ALL_ITEMS = 1;
	
	/** The authority for this content provider. */
	private static final String AUTHORITY = "edu.calpoly.gpsdatabase01.contentprovider";
	
	/** The database table to read from and write to, and also the root path for use in
	 * the URI matcher. This value is essentially a pointer for applications trying to use
	 * the content provider; it points accessors to a database table, which contains a
	 * two-dimensional array representation of the database filled with rows of items whose
	 * columns contain item data. */
	private static final String BASE_PATH = MySQLiteHelper.DATABASE_NAME;
	
	/** The default root content URI. May be extended to perform various operations on the
	 * database. */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	/** Matches content URIs requested by accessing applications with possible
	 * expected content URI formats to take specific actions in this provider. */
	private static final UriMatcher s_URIMatcher;
	static {
		s_URIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		s_URIMatcher.addURI(AUTHORITY, BASE_PATH + "/items", ALL_ITEMS);
	}

	/**
	 * Initialize your SQLiteDatabase here. Make sure to return the correct boolean value.
	 */
	@Override
	public boolean onCreate() {
		this.database = new MySQLiteHelper(getContext());
		return false;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	/**
	 * Fetches rows from the item table. Given a specified URI that contains the base path and
	 * a sorting order, returns a list of items from the item table matching that order in the
	 * form of a Cursor.<br><br>
	 * 
	 * Overrides the built-in version of <b>query(...)</b> provided by ContentProvider.<br><br>
	 * 
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
		String sortOrder) {
		
		//Use a helper class to perform a query for us.
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		//Make sure the projection is proper before querying.
		checkColumns(projection);
		
		//Set up helper to query our app table.
		queryBuilder.setTables("items");
		
		String orderBy = null;
		
		//Match the passed-in URI to an expected URI format.
		int uriType = s_URIMatcher.match(uri);
		
		switch(uriType)	{
		
		case ALL_ITEMS:
			
			//Default sort order if none specified
			if(sortOrder == null || TextUtils.isEmpty(sortOrder)) {
				orderBy = MySQLiteHelper.COLUMN_ITEM;
			}
			
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		//Perform the database query.
		SQLiteDatabase db = this.database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null,
			orderBy);
		
		//Set the cursor to automatically alert listeners for content/view refreshing.
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	/**
	 * Inserts an item into the itemtable. Given a specific URI that contains an
	 * item and the values of that item, writes a new row in the table filled
	 * with that app's information and gives the item a new ID, then returns a URI
	 * containing the ID of the inserted item.<br><br>
	 * 
	 * Overrides the built-in version of <b>insert(...)</b> provided by ContentProvider.<br><br>
	 * 
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		//Open the database for writing.
		SQLiteDatabase sqlDB = this.database.getWritableDatabase();
		
		//Will eventually contain the ID of the inserted ITEM.
		long id = 0;
		
		//Match the passed-in URI to an expected URI format.
		int uriType = s_URIMatcher.match(uri);
		
		switch(uriType)	{
		
		case ALL_ITEMS:
			
			//Perform the database insert, placing the app at the bottom of the table.
			id = sqlDB.insert(MySQLiteHelper.DATABASE_NAME, null, values);
			break;
			
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		//Only alert if item was successfully inserted
		if(id != -1) {
			
			//Alert any watchers of an underlying data change for content/view refreshing.
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return Uri.parse(BASE_PATH + "/" + id);
	}

	/**
	 * Removes a row from the item table. Given a specific URI containing an app ID,
	 * removes rows in the table that match the ID and returns the number of rows removed.
	 * Since IDs are automatically incremented on insertion, this will only ever remove
	 * a single row from the item table.<br><br>
	 * 
	 * Overrides the built-in version of <b>delete(...)</b> provided by ContentProvider.<br><br>
	 * 
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		//Open the database for writing. Deletion is a write operation.
		SQLiteDatabase sqlDB = database.getWritableDatabase();
	    
		//Keep track of the number of deleted rows for the return value.
	    int rowsDeleted = 0;
	    
	    //Match the passed-in URI to an expected URI format.
	    int uriType = s_URIMatcher.match(uri);
	    switch (uriType) {
	    
	    //Remove all rows from the item table with the matching ID.
	    case ALL_ITEMS:
    	   	rowsDeleted = sqlDB.delete(MySQLiteHelper.DATABASE_NAME,
	    		null, null);
    	   	break;
	      
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    
	    //Only alert if rows were actually removed.
	    if(rowsDeleted > 0) {
	    	
		    //Alert any watchers of an underlying data change for content/view refreshing.
		    getContext().getContentResolver().notifyChange(uri, null);
	    }
	    
	    return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		
		throw new UnsupportedOperationException();
		/*
		//Open the database for writing.
		SQLiteDatabase sqlDB = database.getWritableDatabase();
	    
		//Keep track of the number of updated rows for the return value.
	    int rowsUpdated = 0;
	    
	    //Match the passed-in URI to an expected URI format.
	    int uriType = s_URIMatcher.match(uri);
	    switch (uriType) {
	    
	    //Update a row in the app table with the matching ID.
	    case ITEM_ID:
	    	String id = uri.getLastPathSegment();
	    	
	    	//Perform the actual update in the table.
	    	if(!TextUtils.isEmpty(selection)) {
		    	rowsUpdated = sqlDB.update(AppTable.DATABASE_TABLE_APP, values,
		    		AppTable.APP_KEY_ID + "=" + id + " AND " + selection, null);
	    	} else {
		    	rowsUpdated = sqlDB.update(AppTable.DATABASE_TABLE_APP, values,
		    		AppTable.APP_KEY_ID + "=" + id, null);
	    	}
	      
	      break;
	      
	    default:
	      throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    
	    //Only alert if rows were actually removed.
	    if(rowsUpdated > 0) {
	    	
		    //Alert any watchers of an underlying data change for content/view refreshing.
		    getContext().getContentResolver().notifyChange(uri, null);
	    }
	    
	    return rowsUpdated; */
	}	

	/**
	 * Verifies the correct set of columns to return data from when performing a query.
	 * 
	 * @param projection
	 * 						The set of columns about to be queried.
	 */
	private void checkColumns(String[] projection)
	{
		String[] available = { 
				MySQLiteHelper.COLUMN_ID,
				MySQLiteHelper.COLUMN_ITEM,
				MySQLiteHelper.COLUMN_ITEM_DESC,
				MySQLiteHelper.COLUMN_ITEM_PRICE,
				MySQLiteHelper.COLUMN_ITEM_TAX,
				MySQLiteHelper.COLUMN_STORE_NAME,
				MySQLiteHelper.COLUMN_ITEM_IMAGE
		};
		
		if(projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			
			if(!availableColumns.containsAll(requestedColumns))	{
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
}
