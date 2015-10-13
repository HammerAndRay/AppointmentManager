package com.example.appointmentmanager;

import static android.provider.BaseColumns._ID;
import static com.example.appointmentmanager.Constants.DATE;
import static com.example.appointmentmanager.Constants.DETAILS;
import static com.example.appointmentmanager.Constants.TABLE_NAME;
import static com.example.appointmentmanager.Constants.TIME;
import static com.example.appointmentmanager.Constants.TITLE;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class SelectSearch extends Activity implements OnClickListener {
	private EventsData Search;
	String SelectedDate = "";

	protected void onCreate(Bundle savedInstanceState) {
		Search = new EventsData(this);
		overridePendingTransition(R.anim.fadei, R.anim.fadeo);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_search);
		SelectedDate = getIntent().getStringExtra("SelectedDate");
		View Searchbutton = findViewById(R.id.Search_button);
		Searchbutton.setOnClickListener(this);
		View more_detail = findViewById(R.id.more_detail);
		more_detail.setOnClickListener(this);

		// Cursor cursor = getEvents();
		// FillData(cursor);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Search_button:
			TextView SearchResults = (TextView) findViewById(R.id.search_results);
			SearchResults.setText("");
			try {
				Cursor cursor = getEvents();
				showEvents(cursor);
			} finally {
				Search.close();
			}
			break;
		case R.id.more_detail:
			EditText Number = (EditText) findViewById(R.id.select_search_numb);
			String numb = Number.getText().toString();
			final Intent Create = new Intent(this, Edit.class).putExtra(
					"SelectedDate", SelectedDate).putExtra("Id", numb);
		
			startActivity(Create);
			finish();
			break;
		}
	}

	private static String[] FROM = { _ID, DATE, TITLE, TIME, DETAILS, };

	private Cursor getEvents() {
		SQLiteDatabase db = Search.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, FROM, DATE + ">?",
				new String[] { SelectedDate }, null, null, null);
		startManagingCursor(cursor);

		return cursor;
	}

	private void showEvents(Cursor cursor) {
		getEvents();
		StringBuilder builder = new StringBuilder("Saved Events:\n");
		long[] Ids = new long[cursor.getCount()];
		String[] Dates = new String[cursor.getCount()];
		String[] Titles = new String[cursor.getCount()];
		String[] Times = new String[cursor.getCount()];
		String[] Detailss = new String[cursor.getCount()];
		int counter = 0;
		while (cursor.moveToNext()) {
			long id = cursor.getLong(0);
			Ids[counter] = cursor.getLong(0);

			String date = cursor.getString(1);
			Dates[counter] = cursor.getString(1);

			String title = cursor.getString(2);
			Titles[counter] = cursor.getString(2);

			long time = cursor.getLong(3);
			Times[counter] = cursor.getString(3);

			String details = cursor.getString(4);
			Detailss[counter] = cursor.getString(4);

			builder.append(id).append(": ");
			builder.append(date).append(": ");
			builder.append(title).append(": ");
			builder.append(time).append(": ");
			builder.append(details).append("\n");
			counter++;
		}
		EditText Number = (EditText) findViewById(R.id.Search_Text);
		String Compare = Number.getText().toString();
		Compare = Compare.toLowerCase();
		System.out.println("Users enters : " + Compare);
		TextView SearchResults = (TextView) findViewById(R.id.search_results);
		SearchResults.setText("");
		boolean found = false;
		for (int count = 0; count < cursor.getCount(); count++) {

			int intIndex = Titles[count].toLowerCase().indexOf(Compare);
			int intIndex2 = Detailss[count].toLowerCase().indexOf(Compare);
			if (Compare.equals("")) {
				intIndex = -1;
				intIndex2 = -1;
			}
			if (intIndex == -1) {
				System.out.println("Title was: " + intIndex);
			} else {
				found = true;
				System.out
						.println("match Found at Title index of: " + intIndex);
				String addon = "";
				addon = (String) SearchResults.getText();
				SearchResults.setText(addon + "\n" + Ids[count] + " : "
						+ Dates[count] + " : " + Titles[count] + " : "
						+ Times[count] + " : " + Detailss[count]);
			}

			if (intIndex2 == -1) {
				System.out.println("Details was: " + intIndex2);
			} else {
				if (found == true) {
				} else {
					System.out.println("match Found at details index of: "
							+ intIndex2);
					String addon = "";
					addon = (String) SearchResults.getText();
					SearchResults.setText(addon + "\n" + Ids[count] + " : "
							+ Dates[count] + " : " + Titles[count] + " : "
							+ Times[count] + " : " + Detailss[count]);
				}
			}
		}

		System.out.println(builder);
	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.fadeo);
	}
}
