package il.ac.huji.todolist;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ListViewCursorAdapter extends CursorAdapter {

	private LayoutInflater mLayoutInflater;

	public ListViewCursorAdapter(Context context, Cursor c, MySQLiteHelper db, Activity activity){
		super(context, c, 0);
		mLayoutInflater = LayoutInflater.from(context);
	}


	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		View v = mLayoutInflater.inflate(R.layout.todo_listview_layout, parent, false);
		return v;
	}


	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		TodoField todo = new TodoField();
		todo._todo = (TextView)view.findViewById(R.id.txtTodoTitle);
		todo._dueDate = (TextView)view.findViewById(R.id.txtTodoDueDate);

		String title = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_TITLE));
		String d = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_DUE));
		Date date = new Date(Long.parseLong(d));
		Format formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
		String dateStr = formatter.format(date);

		//Next set the title of the entry.
		if (todo._todo != null) {
			todo._todo.setText(title);
		}

		//Set Date
		if (todo._dueDate != null) {
			todo._dueDate.setText(dateStr);
		} 
		
		//set fields colors - red for before today, blue otherwise
		if (isBeforeToday(date)){
			((TextView) view.findViewById(R.id.txtTodoTitle)).setTextColor(Color.RED);
			((TextView) view.findViewById(R.id.txtTodoDueDate)).setTextColor(Color.RED);
		} else{
			((TextView) view.findViewById(R.id.txtTodoTitle)).setTextColor(Color.BLUE);
			((TextView) view.findViewById(R.id.txtTodoDueDate)).setTextColor(Color.BLUE);
		}
	}
	

	private boolean isBeforeToday(Date date)
	{
		Calendar c = Calendar.getInstance();

		// set the calendar to start of today
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);

		// and get that as a Date
		Date today = c.getTime();
		return date.before(today);
	}

	private class TodoField{
		TextView _todo;
		TextView _dueDate;
	}

}
