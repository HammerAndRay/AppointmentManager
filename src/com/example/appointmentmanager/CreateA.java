package com.example.appointmentmanager;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import android.content.*;
import android.database.sqlite.*;
import static com.example.appointmentmanager.Constants.TABLE_NAME;
import static com.example.appointmentmanager.Constants.DATE;
import static com.example.appointmentmanager.Constants.TITLE;
import static com.example.appointmentmanager.Constants.TIME;
import static com.example.appointmentmanager.Constants.DETAILS;
import static android.provider.BaseColumns._ID;

public class CreateA extends Activity implements OnClickListener {
	private EventsData CreateA;
	String SelectedDate = "";
	String Title = "";
	String Time = "";
	String Details = "";

	protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(R.anim.fadei, R.anim.fadeo);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);
		View CreateButton = findViewById(R.id.Save_button);
		CreateButton.setOnClickListener(this);
		SelectedDate = getIntent().getStringExtra("SelectedDate");

		CreateA = new EventsData(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Save_button:
			EditText TitleField = (EditText) findViewById(R.id.title_text);
			Title = TitleField.getText().toString();
			EditText TimeField = (EditText) findViewById(R.id.time_text);
			Time = TimeField.getText().toString();
			EditText DetailsField = (EditText) findViewById(R.id.details_text);
			Details = DetailsField.getText().toString();
			if (Title.equals("")) {
				Toast.makeText(getApplicationContext(),
						"Error: Title can not be empty", Toast.LENGTH_SHORT)
						.show();
			} else {
				try {
					addEvent();
				} finally {
					CreateA.close();
				}

				if (CheckTitle == true) {
					Toast.makeText(
							getApplicationContext(),
							"Appointment Meeting with \""
									+ FalseTitle
									+ "\" already exists, please choose a different event title ",
							Toast.LENGTH_LONG).show();
					CheckTitle = false;
				} else {
					Toast.makeText(getApplicationContext(), "Saved",
							Toast.LENGTH_SHORT).show();
					onBackPressed();
				}
			}
			break;
		}
	}

	boolean CheckTitle = false;
	String FalseTitle = "";

	private void showEvents(Cursor cursor) {
		StringBuilder builder = new StringBuilder("Saved Events:\n");
		while (cursor.moveToNext()) {
			long id = cursor.getLong(0);
			String date = cursor.getString(1);
			String title = cursor.getString(2);
			long time = cursor.getLong(3);
			String details = cursor.getString(4);
			builder.append(id).append(": ");
			builder.append(date).append(": ");
			builder.append(title).append(": ");
			builder.append(time).append(": ");
			builder.append(details).append("\n");
			if (title.equals(Title) && date.equals(SelectedDate)) {
				CheckTitle = true;
				FalseTitle = Title;
			}
		}
		System.out.println(builder);
	}

	private void addEvent() {
		Cursor cursor = getEvents();
		showEvents(cursor);
		if (CheckTitle == true) {
		} else {
			SQLiteDatabase db = CreateA.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DATE, SelectedDate);
			values.put(TITLE, Title);
			values.put(TIME, Time);
			values.put(DETAILS, Details);
			db.insertOrThrow(TABLE_NAME, null, values);
		}

	}

	private static String[] FROM = { _ID, DATE, TITLE, TIME, DETAILS, };

	private Cursor getEvents() {
		SQLiteDatabase db = CreateA.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, FROM, null, null, null, null, null);
		startManagingCursor(cursor);

		return cursor;
	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.fadeo);
	}
}
