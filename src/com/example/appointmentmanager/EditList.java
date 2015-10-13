package com.example.appointmentmanager;

import static com.example.appointmentmanager.Constants.TABLE_NAME;
import static com.example.appointmentmanager.Constants.TABLE_NAME2;
import static com.example.appointmentmanager.Constants.list;
import static com.example.appointmentmanager.Constants._id;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditList extends Activity implements OnClickListener {

	private ListData ListOP;

	protected void onCreate(Bundle savedInstanceState) {

		overridePendingTransition(R.anim.fadei, R.anim.fadeo);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_list);
		View delete = findViewById(R.id.delete_lang);
		delete.setOnClickListener(this);
		View save = findViewById(R.id.save_lang);
		save.setOnClickListener(this);
		ListOP = new ListData(this);

		try {
			showlist();
		} finally {
			ListOP.close();
		}

	}

	String thelistid = "";
	String thelistlang = "";
	private static String[] FROM2 = { _id, list, };

	private void showlist() {
		SQLiteDatabase db = ListOP.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME2, FROM2, null, null, null, null,
				null);
		StringBuilder builder = new StringBuilder();
		while (cursor.moveToNext()) {
			thelistid = cursor.getString(0);
			builder.append(thelistid).append(": ");
			thelistlang = cursor.getString(1);
			builder.append(thelistlang).append("\n");
		}
		System.out.println(builder);

		TextView Selection = (TextView) findViewById(R.id.langlist);
		Selection.setMovementMethod(new ScrollingMovementMethod());
		Selection.setText(builder + "\n");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.delete_lang:
			DeleteEvent();
			break;
		case R.id.save_lang:
			Savelang();
			break;
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.fadeo);
	}

	public void DeleteEvent() {

		SQLiteDatabase db = ListOP.getReadableDatabase();
		EditText Number = (EditText) findViewById(R.id.UserDeleteSelection);
		String numb = Number.getText().toString();
		int num = Integer.parseInt(numb);
		System.out.println(num);
		try {
			db.delete(TABLE_NAME2, "_id" + "=?", new String[] { numb });
		} finally {
			ListOP.close();
		}

		Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT)
				.show();
		showlist();

	}

	public void Savelang() {
		EditText Number = (EditText) findViewById(R.id.new_lang);
		String lang = Number.getText().toString();
		ListOP.insertList(lang);
		Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT)
		.show();
		showlist();
	}
}