package be.wishto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_TAG = "tag";
	public static final String KEY_SCORE = "score";
	public static final String KEY_EDATE = "edate";
	private static final String TAG = "DBAdapter";
	private static final String DATABASE_NAME = "MyDB";
	private static final String DATABASE_TABLE = "contacts";
	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_CREATE = "create table contacts (_id integer primary key autoincrement, name text not null, email text not null,tag integer not null,score integer not null,edate text not null);";
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
				Log.w("ceate","creating db");
				//insert default data, can come from a config file				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS contacts");
			onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert a contact into the database---
	public long insertContact(String name, String email,long tag,long score,String edate) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_EMAIL, email);
		initialValues.put(KEY_TAG, tag);
		initialValues.put(KEY_SCORE, score);
		initialValues.put(KEY_EDATE, edate);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	// ---deletes a particular contact---
	public boolean deleteContact(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	// ---retrieves all the contacts---
	public Cursor getAllContacts() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_EMAIL,KEY_TAG,KEY_SCORE,KEY_EDATE }, null, null, null, null, null);
	}

	// ---retrieves a particular contact---
	public Cursor getContact(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_NAME, KEY_EMAIL,KEY_TAG,KEY_SCORE,KEY_EDATE }, KEY_ROWID + "=" + rowId,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---updates a contact---
	public boolean updateContact(long rowId, String name, String email,long tag,long score,String edate) {
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		args.put(KEY_EMAIL, email);
		args.put(KEY_TAG, tag);
		args.put(KEY_SCORE, score);
		args.put(KEY_EDATE, edate);
		return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}