package com.boxer.browser;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.boxer.util.UrlList;

public class DataBaseListHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ListManager";

	// Contacts table name
	private static final String TABLE_LIST = "list";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_URL = "url";
	private static final String KEY_TITTLE = "tittle";
	private static final String KEY_STATUS = "status";
	private static final String KEY_POSITION = "position";

	public DataBaseListHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LIST + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_URL + " TEXT,"
				+ KEY_TITTLE + " TEXT," + KEY_STATUS + " TEXT," + KEY_POSITION
				+ " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	public void addUrl(UrlList url) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		Log.e("Adding Url in New db" + " " + KEY_POSITION, url.getUrl());

		values.put(KEY_URL, url.getUrl()); // Contact Name
		values.put(KEY_POSITION, url.getPosition());
		values.put(KEY_STATUS, url.getStatus());
		values.put(KEY_TITTLE, url.getTittle());

		// Inserting Row
		db.insert(TABLE_LIST, null, values);
		db.close(); // Closing database connection
	}

	// // Getting single contact
	// public Url getUrl(int id) {
	// SQLiteDatabase db = this.getReadableDatabase();
	//
	// Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
	// KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
	// new String[] { String.valueOf(id) }, null, null, null, null);
	// if (cursor != null)
	// cursor.moveToFirst();
	//
	// Url contact = new Url(Integer.parseInt(cursor.getString(0)),
	// cursor.getString(1));
	// // return contact
	// return contact;
	// }

	// Getting All Contacts
	public List<UrlList> getAllUrl() {
		List<UrlList> urlList = new ArrayList<UrlList>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_LIST;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {

			do {
				UrlList url = new UrlList();
				url.set_id(Integer.parseInt(cursor.getString(0)));
				url.setUrl(cursor.getString(1));
				url.setPosition(cursor.getString(4));
				url.setStatus(cursor.getString(3));
				url.setTittle(cursor.getString(2));
				// Adding contact to list
				urlList.add(url);
			} while (cursor.moveToNext());
		}

		// return contact list
		return urlList;
	}

	// Updating single contact
	public int updateContact(UrlList url) {
		SQLiteDatabase db = this.getWritableDatabase();
		Log.e("Updating Url new db" + " " + url.get_id(), url.getUrl());
		ContentValues values = new ContentValues();

		values.put(KEY_URL, url.getUrl()); // Contact Name
		values.put(KEY_POSITION, url.getPosition());
		values.put(KEY_STATUS, url.getStatus());
		values.put(KEY_TITTLE, url.getTittle());
		// updating row
		return db.update(TABLE_LIST, values, "id=" + url.get_id(), null);

	}

	// Deleting single contact
	public void deleteUrl(UrlList urlist) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_LIST, KEY_ID + " = ?",
				new String[] { String.valueOf(urlist.get_id()) });
		Log.e("deleted Url  id ", "" + urlist.get_id());
		db.close();
	}

	public void deleteDB_Data() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from " + TABLE_LIST);
		Log.e("deleted   ", "All Entries");
		db.close();
	}

	// Getting contacts Count
	public int getContactsCount() {

		SQLiteDatabase db = this.getReadableDatabase();
		String countQuery = "SELECT  * FROM " + TABLE_LIST;
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
