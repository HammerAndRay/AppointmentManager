package com.example.appointmentmanager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	String Date = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(R.anim.fadei, R.anim.fadeo);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		View CreateButton = findViewById(R.id.CreateA_button);
		CreateButton.setOnClickListener(this);
		View ViewEditButton = findViewById(R.id.ViewEdit_button);
		ViewEditButton.setOnClickListener(this);
		View DeleteButton = findViewById(R.id.DeleteA_button);
		DeleteButton.setOnClickListener(this);
		View MoveButton = findViewById(R.id.MoveA_button);
		MoveButton.setOnClickListener(this);
		View SearchButton = findViewById(R.id.SearchA_button);
		SearchButton.setOnClickListener(this);
		View TranslateButton = findViewById(R.id.TranslateA_button);
		TranslateButton.setOnClickListener(this);

		CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView1);
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int The_year,
					int The_month, int The_dayOfMonth) {
				Date(The_year, The_month, The_dayOfMonth);
				Toast.makeText(
						getApplicationContext(),
						"" + The_dayOfMonth + " " + (WhatMonth(The_month))
								+ " " + The_year, Toast.LENGTH_SHORT).show();
				
				Date = ("" + The_dayOfMonth + " "
						+ (WhatMonth(The_month)) + " " + The_year);
				System.out.println(("" + The_dayOfMonth + " "
						+ (WhatMonth(The_month)) + " " + The_year));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.CreateA_button:
			Intent Create = new Intent(this, CreateA.class).putExtra("SelectedDate", Date);
			startActivity(Create);
			break;
		}
		switch (v.getId()) {
		case R.id.ViewEdit_button:
			Intent View_Edit = new Intent(this, ViewSelect.class).putExtra("SelectedDate", Date);
			startActivity(View_Edit);
			break;
		}
		switch (v.getId()) {
		case R.id.DeleteA_button:
			Intent Delete = new Intent(this, Delete.class).putExtra("SelectedDate", Date);
			startActivity(Delete);
			break;
		}
		switch (v.getId()) {
		case R.id.MoveA_button:
			Intent Move = new Intent(this, SelectMove.class).putExtra("SelectedDate", Date);
			startActivity(Move);
			break;
		}
		switch (v.getId()) {
		case R.id.SearchA_button:
			Intent Search = new Intent(this, SelectSearch.class).putExtra("SelectedDate", Date);
			startActivity(Search);
			break;
		}
		switch (v.getId()) {
		case R.id.TranslateA_button:
			Intent TranslateD = new Intent(this, SelectTranslate.class).putExtra("SelectedDate", Date);
			startActivity(TranslateD);
			break;
		}
	}

	
	public void Date(int Year, int Month, int DayOfMonth) {
	
	
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
