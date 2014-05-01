package il.ac.huji.todolist;

import il.ac.huji.todolist.TodoListManagerActivity.TodoTask;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "TodoDB.db";
	private static final String DATABASE_TABLE_NAME = "todo";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DUE = "due";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_TODO_TABLE = "CREATE TABLE " +
				DATABASE_TABLE_NAME + "("
				+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TITLE 
				+ " TEXT," + COLUMN_DUE + " LONG" + ")";
		db.execSQL(CREATE_TODO_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public Cursor getAllData(){

		Log.d("db get all items", "OK");

		String selectAllQuery = "SELECT  * FROM " + DATABASE_TABLE_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d("db get all items", "ended");
		return db.rawQuery(selectAllQuery, null);
	}
	
	public void addItem(TodoTask tt){
		Log.d("db add item", "OK");
		SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, tt._todo);
        values.put(COLUMN_DUE, String.valueOf(tt._dueDate.getTime()));

        // Inserting Row
        db.insert(DATABASE_TABLE_NAME, null, values);
        db.close(); // Closing database connection
		Log.d("db add item", "ended");
	}
	
	public void deleteItem(long id){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(DATABASE_TABLE_NAME, COLUMN_ID + "=" + id, null);
		db.close();
	}

}
