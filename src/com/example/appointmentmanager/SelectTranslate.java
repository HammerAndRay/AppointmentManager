package com.example.appointmentmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.*;
import android.database.sqlite.*;
import static com.example.appointmentmanager.Constants.TABLE_NAME;
import static com.example.appointmentmanager.Constants.DATE;
import static com.example.appointmentmanager.Constants.TITLE;
import static com.example.appointmentmanager.Constants.TIME;
import static android.provider.BaseColumns._ID;

public class SelectTranslate extends Activity implements OnClickListener {
	String SelectedDate = "";
	private EventsData SelectDelete;

	protected void onCreate(Bundle savedInstanceState) {
		
		overridePendingTransition(R.anim.fadei, R.anim.fadeo);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_translate);
		SelectedDate = getIntent().getStringExtra("SelectedDate");
		View Translate = findViewById(R.id.translate);
		Translate.setOnClickListener(this);
		SelectDelete = new EventsData(this);

		try {
			Cursor cursor = getEvents();
			showEvents(cursor);
		} finally {
			SelectDelete.close();
		}

	}

	boolean ButtonPressed = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.translate:
			EditText Number = (EditText) findViewById(R.id.UserSelection);
			String numb = Number.getText().toString();
			final Intent Create = new Intent(this, Translate.class).putExtra(
					"SelectedDate", SelectedDate).putExtra("Id", numb);
			ButtonPressed = true;
			Cursor cursor = getEvents();
			showEvents(cursor);
			new AlertDialog.Builder(this)
					.setTitle("Confirmation?")
					.setMessage(
							"Would you like to translate event: " + TitleFor + "?")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									
									startActivity(Create);
									finish();
								
									ButtonPressed = true;
								}
							})
					.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.fadeo);
	}

	String TitleFor = "";

	private void showEvents(Cursor cursor) {
		StringBuilder builder = new StringBuilder("");
		while (cursor.moveToNext()) {
			long id = cursor.getLong(0);
			long time = cursor.getLong(1);
			String s = String.valueOf(time);
			s = s.substring(0, 2) + ":" + s.substring(2, s.length());
			String title = cursor.getString(2);
			builder.append(id).append(": ");
			builder.append(s).append(": ");
			builder.append(title).append("\n");
			if (ButtonPressed == true) {
				EditText Number = (EditText) findViewById(R.id.UserSelection);
				String numb = Number.getText().toString();
				int num = Integer.parseInt(numb);
				if (id == num) {
					TitleFor = title;
				}
			}

		}
		ButtonPressed = false;
		TextView Selection = (TextView) findViewById(R.id.textView1);
		Selection.setText(builder + "\n");
	}

	public void DeleteEvent() {

		SQLiteDatabase db = SelectDelete.getWritableDatabase();
		EditText Number = (EditText) findViewById(R.id.UserSelection);
		String numb = Number.getText().toString();
		int num = Integer.parseInt(numb);
		System.out.println(num);
		try {
			db.delete(TABLE_NAME, "_ID" + "=?", new String[] { numb });
		} finally {
			SelectDelete.close();
		}

		Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT)
				.show();
		onBackPressed();

	}

	private static String[] FROM = { _ID, TIME, TITLE, };
	private static String ORDER_BY = _ID + " ASC";

	private Cursor getEvents() {
		SQLiteDatabase db = SelectDelete.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, FROM, DATE + "=?",
				new String[] { SelectedDate }, null, null, ORDER_BY, null);
		startManagingCursor(cursor);

		return cursor;
	}
}


