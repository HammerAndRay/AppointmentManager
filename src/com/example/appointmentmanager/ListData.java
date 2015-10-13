package com.example.appointmentmanager;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.example.appointmentmanager.Constants._id;
import static com.example.appointmentmanager.Constants.list;
import static com.example.appointmentmanager.Constants.TABLE_NAME2;

public class ListData  extends SQLiteOpenHelper {
	private static final int DB_VERSION = 1;
	private static final String DATABASE_NAME = "list.db";

	public ListData(Context ctx) {
		super(ctx, DATABASE_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME2 + "(" + _id
				+ " INTEGER PRIMARY KEY," + list + " TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + TABLE_NAME2);
		onCreate(db);
	}

	public void insertList(String nameoflist) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(list, nameoflist);
		db.insert(TABLE_NAME2, null, values);
		db.close();
	}

	public Set<String> getlist() {
		Set<String> set = new HashSet<String>();

		String selectQuery = "select * from " + TABLE_NAME2;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				set.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return set;
	}
}