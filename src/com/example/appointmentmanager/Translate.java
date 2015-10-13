package com.example.appointmentmanager;

import static android.provider.BaseColumns._ID;
import static com.example.appointmentmanager.Constants.DATE;
import static com.example.appointmentmanager.Constants.DETAILS;
import static com.example.appointmentmanager.Constants.TABLE_NAME;
import static com.example.appointmentmanager.Constants.TABLE_NAME2;
import static com.example.appointmentmanager.Constants.TIME;
import static com.example.appointmentmanager.Constants.TITLE;
import static com.example.appointmentmanager.Constants.list;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Translate extends Activity implements OnClickListener {

	private class GetAccessTokenTask extends AsyncTask<Void, Void, String> {

		protected void onPostExecute(String access_token) {
			accessToken = access_token;
		}

		protected String doInBackground(Void... v) {
			String result = null;
			HttpURLConnection con = null;
			String clientID = "RahimIsTheBest";
			String clientSecret = "AndroidCourseworkTwo";
			String strTranslatorAccessURI = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
			String strRequestDetails = "grant_type="
					+ " client_credentials&client_id="
					+ URLEncoder.encode(clientID) + "&client_secret="
					+ URLEncoder.encode(clientSecret)
					+ "&scope=http://api.microsofttranslator.com";
			try {
				URL url = new URL(strTranslatorAccessURI);
				con = (HttpURLConnection) url.openConnection();
				con.setReadTimeout(10000/* milliseconds */);
				con.setConnectTimeout(15000/* milliseconds */);
				con.setRequestMethod("POST");
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setChunkedStreamingMode(0);
				// Startthequery
				con.connect();
				OutputStream out = new BufferedOutputStream(
						con.getOutputStream());
				out.write(strRequestDetails.getBytes());
				out.flush();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "UTF-8"));
				String payload = reader.readLine();
				reader.close();
				out.close();
				// Parsetogettranslatedtext
				JSONObject jsonObject = new JSONObject(payload);
				result = jsonObject.getString("access_token");

			} catch (IOException e) {
				Log.e(TAG, "IOException", e);
			} catch (JSONException e) {
				Log.e(TAG, "JSONException", e);
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
			return result;
		}

	}

	private static final String TAG = "TranslateTask";
	private Spinner fromSpinner;
	private Spinner toSpinner;
	private EditText origText;
	private TextView transText;
	private Button transButton;
	private String fromLang;
	private String toLang;
	private TextWatcher textWatcher;
	private OnItemSelectedListener itemListener;
	private OnClickListener buttonListener;
	// neededtomaketranslaterequeststoMicrosoft
	private String accessToken;
	String Date = "";
	private EventsData TranslateD;
	String TheID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(R.anim.fadei, R.anim.fadeo);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.translate);

		FillList = new ListData(this);
		findViews();
		setAdapters();
		setListeners();
		// gettheaccesstokenfromMicrosoft
		new GetAccessTokenTask().execute();

		TranslateD = new EventsData(this);
		TheID = getIntent().getStringExtra("Id");
		View Save = findViewById(R.id.SaveTranslate);
		Save.setOnClickListener(this);
		View EditListV = findViewById(R.id.EditListView);
		EditListV.setOnClickListener(this);
		try {
			Cursor cursor = getEvents();
			GetData(cursor);
		} finally {
			FillList.close();
		}
		

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.SaveTranslate:
			new AlertDialog.Builder(this)
					.setTitle("Confirmation?")
					.setMessage("Would you like to save details?")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									SaveTranslate();

								}
							})
					.setNegativeButton("NO",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			break;

		case R.id.EditListView:
			Intent Editlist = new Intent(this, EditList.class);
			startActivity(Editlist);
			System.out.println("Is this working???????????");
			break;
		}

	}

	private void GetData(Cursor cursor) {
		while (cursor.moveToNext()) {

			String details = cursor.getString(0);

			EditText Details = (EditText) findViewById(R.id.original_text);
			Details.setText(details);
		}
	}

	private void SaveTranslate() {
		SQLiteDatabase db = TranslateD.getWritableDatabase();
		ContentValues values = new ContentValues();
		EditText TranslatedField = (EditText) findViewById(R.id.translated_text);
		String NewTran = TranslatedField.getText().toString();
		values.put(DETAILS, NewTran);
		db.update(TABLE_NAME, values, "_id " + "=" + TheID, null);

		Toast.makeText(getApplicationContext(), "Translation Saved",
				Toast.LENGTH_SHORT).show();
		onBackPressed();

	}

	private static String[] FROM = { DETAILS, };

	private Cursor getEvents() {
		System.out.println(TheID);
		SQLiteDatabase db = TranslateD.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, FROM, _ID + "=?",
				new String[] { TheID }, null, null, null);

		return cursor;
	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.fadeo);
	}

	public void onResume() {
		super.onResume();
		setAdapters();
	}

	private void findViews() {
		fromSpinner = (Spinner) findViewById(R.id.from_language);
		toSpinner = (Spinner) findViewById(R.id.to_language);
		origText = (EditText) findViewById(R.id.original_text);
		transText = (TextView) findViewById(R.id.translated_text);
		transButton = (Button) findViewById(R.id.translate_button);
	}

	private ListData FillList;
	String thelist = "";
	private static String[] FROM2 = { list, };

	private void setAdapters() {
		SQLiteDatabase db = FillList.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME2, FROM2, null, null, null, null,
				null);
		StringBuilder builder = new StringBuilder("Saved List:\n");
		int thecount = cursor.getCount();
		if (thecount == 0) {
			FillList.insertList("Bulgarian (bg)");
			FillList.insertList("Chinese Simplified (zh-CHS)");
			FillList.insertList("Chinese Traditional (zh-CHT)");
			FillList.insertList("Catalan (ca)");
			FillList.insertList("Croatian (hr)");
			FillList.insertList("Czech (cs)");
			FillList.insertList("Danish (da)");
			FillList.insertList("Dutch (nl)");
			FillList.insertList("English (en)");
			FillList.insertList("Filipino (tl)");
			FillList.insertList("Finnish (fi)");
			FillList.insertList("French (fr)");
			FillList.insertList("German (de)");
			FillList.insertList("Greek (el)");
			FillList.insertList("Indonesian (id)");
			FillList.insertList("Italian (it)");
			FillList.insertList("Japanese (ja)");
			FillList.insertList("Korean (ko)");
			FillList.insertList("Latvian (lv)");
			FillList.insertList("Lithuanian (lt)");
			FillList.insertList("Norwegian (no)");
			FillList.insertList("Polish (pl)");
			FillList.insertList("Portuguese (pt-PT)");
			FillList.insertList("Romanian (ro)");
			FillList.insertList("Russian (ru)");
			FillList.insertList("Spanish (es)");
			FillList.insertList("Serbian (sr)");
			FillList.insertList("Slovak (sk)");
			FillList.insertList("Slovenian (sl)");
			FillList.insertList("Swedish (sv)");
			FillList.insertList("Ukrainian (uk)");
			SQLiteDatabase db2 = FillList.getReadableDatabase();
			cursor = db2.query(TABLE_NAME2, FROM2, null, null, null, null, null);
		}
		while (cursor.moveToNext()) {
			thelist = cursor.getString(0);
			builder.append(thelist).append("\n");
		}
		// System.out.println(builder);

		ArrayAdapter<String> adapter;
		Set<String> set = FillList.getlist();
		try {
			ArrayList<String> list = new ArrayList<String>(set);
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
		} finally {
		}

		fromSpinner.setAdapter(adapter);
		toSpinner.setAdapter(adapter);
		fromSpinner.setSelection(16);// English(en)
		toSpinner.setSelection(21);// Spanish(es)

		// ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		// this, R.array.languages, android.R.layout.simple_spinner_item);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// fromSpinner.setAdapter(adapter);
		// toSpinner.setAdapter(adapter);
		// // Automaticallyselecttwospinneritems
		// fromSpinner.setSelection(8);// English(en)
		// toSpinner.setSelection(25);// Spanish(es)
	}

	private void setListeners() {
		textWatcher = new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				/*
				 * doTranslate2(origText.getText().toString().trim(),
				 * fromLang,toLang);
				 */}

			public void afterTextChanged(Editable s) {
			}
		};
		origText.addTextChangedListener(textWatcher);
		itemListener = new OnItemSelectedListener() {
			public void onItemSelected(AdapterView parent, View v,
					int position, long id) {
				fromLang = getLang(fromSpinner);
				toLang = getLang(toSpinner);
				if (accessToken != null)
					doTranslate2(origText.getText().toString().trim(),
							fromLang, toLang);
			}

			public void onNothingSelected(AdapterView parent) {
				/* Donothing */
			}
		};
		fromSpinner.setOnItemSelectedListener(itemListener);
		toSpinner.setOnItemSelectedListener(itemListener);
		buttonListener = new OnClickListener() {
			public void onClick(View v) {
				if (accessToken != null)
					doTranslate2(origText.getText().toString().trim(),
							fromLang, toLang);
			}
		};
		transButton.setOnClickListener(buttonListener);
	}

	private String getLang(Spinner spinner) {
		String result = spinner.getSelectedItem().toString();
		int lparen = result.indexOf('(');
		int rparen = result.indexOf(')');
		result = result.substring(lparen + 1, rparen);
		return result;
	}

	private void doTranslate2(String original, String from, String to) {
		if (accessToken != null)
			new TranslationTask().execute(original, from, to);
	}

	private class TranslationTask extends AsyncTask<String, Void, String> {

		protected void onPostExecute(String translation) {
			transText.setText(translation);
		}

		protected String doInBackground(String... s) {
			HttpURLConnection con2 = null;
			String result = getResources()
					.getString(R.string.translation_error);
			String original = s[0];
			String from = s[1];
			String to = s[2];
			try { // Readresultsfromthequery
				BufferedReader reader;
				String uri = "http://api.microsofttranslator.com"
						+ "/v2/Http.svc/Translate?text="
						+ URLEncoder.encode(original) + "&from=" + from
						+ "&to=" + to;
				URL url_translate = new URL(uri);
				String authToken = "Bearer" + " " + accessToken;
				con2 = (HttpURLConnection) url_translate.openConnection();
				con2.setRequestProperty("Authorization", authToken);
				con2.setDoInput(true);
				con2.setReadTimeout(10000/* milliseconds */);
				con2.setConnectTimeout(15000/* milliseconds */);
				reader = new BufferedReader(new InputStreamReader(
						con2.getInputStream(), "UTF-8"));
				String translated_xml = reader.readLine();
				reader.close();
				// parsetheXMLreturned
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document doc = builder.parse(new InputSource(new StringReader(
						translated_xml)));
				NodeList node_list = doc.getElementsByTagName("string");
				NodeList l = node_list.item(0).getChildNodes();
				Node node;
				String translated = null;
				if (l != null && l.getLength() > 0) {
					node = l.item(0);
					translated = node.getNodeValue();
				}
				if (translated != null)
					result = translated;
			} catch (IOException e) {
				Log.e(TAG, "IOException", e);
			} catch (SAXException e1) {
				e1.printStackTrace();
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				if (con2 != null) {
					con2.disconnect();
				}
			}
			return result;
		}
	}

}
