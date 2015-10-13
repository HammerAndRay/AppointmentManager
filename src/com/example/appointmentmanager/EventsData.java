package com.example.appointmentmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;
import static com.example.appointmentmanager.Constants.TABLE_NAME;
import static com.example.appointmentmanager.Constants.DATE;
import static com.example.appointmentmanager.Constants.TITLE;
import static com.example.appointmentmanager.Constants.TIME;
import static com.example.appointmentmanager.Constants.DETAILS;

public class EventsData extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "events.db";
	private static final int DATABASE_VERSION = 1;

	public EventsData(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE
				+ " TEXT NOT NULL," + TITLE + " TEXT NOT NULL," + TIME
				+ " INTEGER," + DETAILS + " TEXT NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
