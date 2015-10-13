package com.example.appointmentmanager;

import static android.provider.BaseColumns._ID;
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
import android.widget.EditText;
import android.widget.Toast;

public class Edit extends Activity implements OnClickListener {
	private EventsData Edit;
	String SelectedDate = "";
	String Title = "";
	String Time = "";
	String Details = "";
	String TheID = "";

	protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(R.anim.fadei, R.anim.fadeo);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_view);
		SelectedDate = getIntent().getStringExtra("SelectedDate");
		TheID = getIntent().getStringExtra("Id");
		View CreateButton = findViewById(R.id.Save_button2);
		CreateButton.setOnClickListener(this);

		Edit = new EventsData(this);
		System.out.println(TheID);
			Cursor cursor = getEvents();
		FillData(cursor);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Save_button2:
			EditText TitleField = (EditText) findViewById(R.id.title_text2);
			Title = TitleField.getText().toString();
			EditText TimeField = (EditText) findViewById(R.id.time_text2);
			Time = TimeField.getText().toString();
			EditText DetailsField = (EditText) findViewById(R.id.details_text2);
			Details = DetailsField.getText().toString();
			if (Title.equals("")) {
				Toast.makeText(getApplicationContext(),
						"Error: Title can not be empty", Toast.LENGTH_SHORT)
						.show();
			} else {
				try {
					addEvent();
				} finally {
					Edit.close();
				}
				
					Toast.makeText(getApplicationContext(), "Updated",
							Toast.LENGTH_SHORT).show();
					onBackPressed();
				
			}
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.fadeo);
	}

	String FalseTitle = "";

	private void FillData(Cursor cursor) {
		while (cursor.moveToNext()) {
			String title = cursor.getString(2);
			long time = cursor.getLong(3);
			String s = String.valueOf(time);
			//s = s.substring(0, 2) + ":" + s.substring(2, s.length());
			String details = cursor.getString(4);
			EditText Title = (EditText) findViewById(R.id.title_text2);
			Title.setText(title);
			EditText Time = (EditText) findViewById(R.id.time_text2);
			Time.setText(s);
			EditText Details = (EditText) findViewById(R.id.details_text2);
			Details.setText(details);
		}
	}

	private void showEvents(Cursor cursor) {
		StringBuilder builder = new StringBuilder("Saved Events:\n");
		while (cursor.moveToNext()) {
			long id = cursor.getLong(0);
			String date = cursor.getString(1);
			String title = cursor.getString(2);
			long time = cursor.getLong(3);
			String s = String.valueOf(time);
			s = s.substring(0, 2) + ":" + s.substring(2, s.length());
			String details = cursor.getString(4);
			builder.append(id).append(": ");
			builder.append(date).append(": ");
			builder.append(title).append(": ");
			builder.append(time).append(": ");
			builder.append(details).append("\n");
			
		}
		System.out.println(builder);
	}

	private void addEvent() {
		Cursor cursor = getEvents();
		showEvents(cursor);
		
			SQLiteDatabase db = Edit.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DATE, SelectedDate);
			values.put(TITLE, Title);
			values.put(TIME, Time);
			values.put(DETAILS, Details);
			db.update(TABLE_NAME, values, "_id " + "=" + TheID, null);
		

	}

	private static String[] FROM = { _ID, DATE, TITLE, TIME, DETAILS, };
	private static String ORDER_BY = _ID + " ASC";
	private Cursor getEvents() {
		System.out.println(TheID);
		SQLiteDatabase db = Edit.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, FROM, _ID + "=?",
				new String[] { TheID }, null, null, ORDER_BY);

		return cursor;
	}
}
