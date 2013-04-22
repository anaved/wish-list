package be.wishto;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AndroidDatabaseActivity extends ListActivity {
	/** Called when the activity is first created. */
	private int editRequestCode = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// passed a List layout of how the list to be shown, or avoid it all
		// together cause
		// ListActivity class contains ListView thats why if we are using
		// contentview then declare android.R.id.list to override it
		// setContentView(R.layout.contact_list);
		DisplayContact();
	}

	public void DisplayContact() {
		DBAdapter db = new DBAdapter(this);
		db.open();
		// db.insertContact("Own a Ferrari", "naved@postcode53.com");
		// db.insertContact("Visit Nazka", "soumya.mty@gmail.com");
		// db.insertContact("Meet MJ", "soumya.mty@gmail.com");

		Cursor c = db.getAllContacts();
		String[] from = new String[] { DBAdapter.KEY_NAME };
		int[] to = new int[] { R.id.label };
		// Now create an array adapter and set it to display using our row
		// passed a layout of how every element of the list to be shown
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.c_list, c, from, to);
		setListAdapter(notes);
		db.close();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Activity returns an result if called with startActivityForResult
		Intent i = new Intent(this, EditWishActivity.class);
		Bundle extras = new Bundle();
		extras.putLong("ID", id);
		i.putExtras(extras);
		startActivityForResult(i, editRequestCode);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		switch (item.getItemId()) {
		case R.id.insert:
			
			startActivityForResult(new Intent(this, EditWishActivity.class),
					editRequestCode);
			return true;
		}
		return true;

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == editRequestCode) {
			if (resultCode == RESULT_OK) {
			//reload the list
				DisplayContact();

			}
		}
	}

}