package com.example.appointmentmanager;

import static com.example.appointmentmanager.Constants.TABLE_NAME;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class Delete extends Activity implements OnClickListener {
	String SelectedDate = "";
	private EventsData SelectDelete;

	protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(R.anim.fadei, R.anim.fadeo);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete);
		SelectedDate = getIntent().getStringExtra("SelectedDate");
		View DeleteAll = findViewById(R.id.delete_all_button);
		DeleteAll.setOnClickListener(this);
		View DeleteSome = findViewById(R.id.delete_some_button);
		DeleteSome.setOnClickListener(this);
		SelectDelete = new EventsData(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.delete_all_button:
			DeleteEvent();
			break;
		}
		switch (v.getId()) {
		case R.id.delete_some_button:
			Intent Create = new Intent(this, SelectDelete.class).putExtra(
					"SelectedDate", SelectedDate);
			startActivity(Create);
			finish();
			break;
		}

	}

	public void DeleteEvent() {

		SQLiteDatabase db = SelectDelete.getWritableDatabase();
		try {
			db.delete(TABLE_NAME, "DATE" + "=?", new String[] { SelectedDate });
		} finally {
			SelectDelete.close();
		}

		Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT)
				.show();
		onBackPressed();

	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.fadeo);
	}
}
