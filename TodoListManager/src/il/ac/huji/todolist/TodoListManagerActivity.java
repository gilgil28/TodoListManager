package il.ac.huji.todolist;

import il.ac.huji.todolist.R.id;

import java.util.Date;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

//a class that represents a line in the ListView
public class TodoListManagerActivity extends Activity {

	class TodoTask{
		String _todo;
		Date _dueDate;

		public TodoTask(String todo, Date dueDate){
			_todo = todo;
			_dueDate = dueDate;
		}
	}

	//upon this prefix, the "call" action is added
	private static final String CALL_PREFIX = "Call ";

	private ListView todoItemsLV;
	private CursorAdapter adapter;
	private Cursor cursor;
	private MySQLiteHelper db;
	private ProgressDialog progress;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		//define layout items
		todoItemsLV = (ListView)findViewById(R.id.lstTodoItems);

		//listView items and adapter
		adapter = new ListViewCursorAdapter(getApplicationContext(), cursor, db, this);
		db = new MySQLiteHelper(getApplicationContext());
		todoItemsLV.setAdapter(adapter);

		//load db if exists
		new LoadDBTask().execute(null, null, null);

		//attach context menu to listview
		registerForContextMenu(todoItemsLV);
	}
	
	private class LoadDBTask extends AsyncTask<Void, Void, Void>{
				
		
		@Override
		protected void onPreExecute(){ 
			super.onPreExecute();
			showLoadingMessage();
		}

		@Override
		protected Void doInBackground(Void... params) {
			   
			Log.d("asynctask", "doInBackground");
			adapter.changeCursor(db.getAllData());
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			Log.d("asynctask", "onPostExecute");
			dismissLoadingMessage();
		}
		
	}
	
	protected void showLoadingMessage(){
		
		Log.d("progress message", "starting...");
		progress = new ProgressDialog(this);
		progress.setMessage("Loading...");
		progress.show(); 
	}
	
	protected void dismissLoadingMessage(){
		
		progress.dismiss();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);

		//get item info and inflate contextmenu with the corresponding header
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		Cursor cursor = (Cursor) adapter.getItem(info.position);
		String todoTitle = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TITLE));
		menu.setHeaderTitle(todoTitle);

		//if the todo item is calling someone, add an option to dial
		if (todoTitle.startsWith(CALL_PREFIX)){
			menu.add(0,id.menuItemCall, 0, todoTitle);
		}
		getMenuInflater().inflate(R.menu.list_view_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		switch(item.getItemId()){  

		//attach an action for the delete button(delete the item)
		case R.id.menuItemDelete: 
			db.deleteItem(info.id);
			adapter.changeCursor(db.getAllData());
			return true;    

			//attach an action for the call button (dials that number)
		case R.id.menuItemCall:
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			Cursor cursor = (Cursor) adapter.getItem(info.position);
			String todoTitle = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_TITLE));
			callIntent.setData(Uri.parse("tel:" + todoTitle.substring(todoTitle.lastIndexOf(" ")+1)));
			startActivity(callIntent);
			return true;
		}
		return super.onContextItemSelected(item);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		menu.findItem(R.id.item_add);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){

		Intent intent = new Intent(getApplicationContext(), AddNewTodoItemActivity.class);
		startActivityForResult(intent, 1);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED){
			return;
		}
		String todo = data.getStringExtra("todo");
		Date date = new Date();
		date.setTime(data.getLongExtra("date",-1));
		Log.d("the new todo: ", todo);
		addItem(new TodoTask(todo, date));
	}

	private void addItem(TodoTask newItem) {

		db.addItem(newItem);
		Log.d(MySQLiteHelper.COLUMN_TITLE + "&&&", newItem._todo);
		adapter.changeCursor(db.getAllData());
		Toast.makeText(getApplicationContext(), newItem._todo + " Added Sucessfully", Toast.LENGTH_LONG).show();
		Log.d("addItem", "ended");
	}
}