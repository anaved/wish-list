package be.wishto;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

public class EditWishActivity extends Activity {
	int yr, month, day;
	static final int DATE_DIALOG_ID = 1;
	Long ROW_ID;
	DBAdapter db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_wish);

		// Set datefield listener
		// EditText wDate=(EditText) findViewById(R.id.w_date);
		db = new DBAdapter(this);
		db.open();
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			addWish();
		} else {
			ROW_ID = extras.getLong("ID");
			editWish(ROW_ID);
		}

	}

	private void addWish() {
		// TODO Auto-generated method stub

	}

	private void editWish(Long id) {

		Cursor data = db.getContact(id);
		EditText summary = (EditText) findViewById(R.id.todo_edit_summary);
		summary.setText(data.getString(data
				.getColumnIndexOrThrow(DBAdapter.KEY_NAME)));

		EditText desc = (EditText) findViewById(R.id.todo_edit_description);
		desc.setText(data.getString(data
				.getColumnIndexOrThrow(DBAdapter.KEY_EMAIL)));

		Spinner tag = (Spinner) findViewById(R.id.category);
		tag.setSelection(data.getInt(data
				.getColumnIndexOrThrow(DBAdapter.KEY_TAG)));

		EditText edate = (EditText) findViewById(R.id.w_date);
		edate.setText(data.getString(data
				.getColumnIndexOrThrow(DBAdapter.KEY_EDATE)));

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			yr = year;
			month = monthOfYear;
			day = dayOfMonth;
			EditText w_date = (EditText) findViewById(R.id.w_date);
			w_date.setText("" + (month + 1) + "/" + day + "/" + yr);
		}
	};

	public void date_click(View view) {
		showDialog(DATE_DIALOG_ID);
	}

	public void save_wish(View view) {

		EditText sum = (EditText) findViewById(R.id.todo_edit_summary);
		String sum_text = sum.getText().toString();

		EditText desc = (EditText) findViewById(R.id.todo_edit_description);
		String desc_text = desc.getText().toString();

		// RatingBar score=(RatingBar) findViewById(R.id.ratingBar1);
		// long score_val=(long)score.getRating();

		Spinner sp = (Spinner) findViewById(R.id.category);
		long tag = sp.getSelectedItemPosition();

		long score_val = 1;

		EditText edate = (EditText) findViewById(R.id.w_date);
		String edate_text = edate.getText().toString();

		if (ROW_ID != null) {
			db.updateContact(ROW_ID, sum_text, desc_text, tag, score_val,
					edate_text);
		} else {
			db.insertContact(sum_text, desc_text, tag, score_val, edate_text);
		}

		db.close();
		Intent data = new Intent();
		setResult(RESULT_OK, data);
		finish();
	}

	public void cancel(View view) {
		finish();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			EditText w_date = (EditText) findViewById(R.id.w_date);
			String d_text = w_date.getText().toString();
			if (d_text.equals("")) {
				Calendar today = Calendar.getInstance();
				yr = today.get(Calendar.YEAR);
				month = today.get(Calendar.MONTH);
				day = today.get(Calendar.DAY_OF_MONTH);
			} else {
				DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
				try {
					Date d = (Date) df.parse(d_text);
					day = d.getDate();
					yr = d.getYear();
					month = d.getMonth();
				} catch (Exception e) {
					Log.w("Dialog Creation", e.toString());
				}

			}

			return new DatePickerDialog(this, mDateSetListener, yr, month, day);
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_menu, menu);
		return true;
	}

	public void show_web(MenuItem item) {
		Intent i = new Intent(this, WishWebView.class);
		startActivityForResult(i, 1);
	}
}
