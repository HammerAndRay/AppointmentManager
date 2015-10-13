package com.example.appointmentmanager;

import static com.example.appointmentmanager.Constants.DATE;
import static com.example.appointmentmanager.Constants.DETAILS;
import static com.example.appointmentmanager.Constants.TABLE_NAME;
import static com.example.appointmentmanager.Constants.TIME;
import static com.example.appointmentmanager.Constants.TITLE;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

public class Move extends Activity {
	String Date = "";
	private EventsData Move;
	String TheID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(R.anim.fadei, R.anim.fadeo);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.move);
		Move = new EventsData(this);
		TheID = getIntent().getStringExtra("Id");

		CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView1);
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int The_year,
					int The_month, int The_dayOfMonth) {
				Date = ("" + The_dayOfMonth + " " + (WhatMonth(The_month))
						+ " " + The_year);
				MoveEvent();
				Toast.makeText(
						getApplicationContext(),
						"" + The_dayOfMonth + " " + (WhatMonth(The_month))
								+ " " + The_year, Toast.LENGTH_SHORT).show();
				onBackPressed();
			}
		});
	}

	private void MoveEvent() {

		SQLiteDatabase db = Move.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DATE, Date);
		db.update(TABLE_NAME, values, "_id " + "=" + TheID, null);
	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.fadeo);
	}

	public String WhatMonth(int Month) {
		String MonthName = "";
		int month = Month + 1;
		switch (month) {
		case 1:
			MonthName = "January";
			break;
		case 2:
			MonthName = "February";
			break;
		case 3:
			MonthName = "March";
			break;
		case 4:
			MonthName = "April";
			break;
		case 5:
			MonthName = "May";
			break;
		case 6:
			MonthName = "June";
			break;
		case 7:
			MonthName = "July";
			break;
		case 8:
			MonthName = "August";
			break;
		case 9:
			MonthName = "September";
			break;
		case 10:
			MonthName = "October";
			break;
		case 11:
			MonthName = "November";
			break;
		case 12:
			MonthName = "December";
			break;
		}
		return MonthName;
	}
}
