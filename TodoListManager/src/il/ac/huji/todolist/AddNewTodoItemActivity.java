package il.ac.huji.todolist;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewTodoItemActivity extends Activity {
	
	private static final String DATE = "date";
	private static final String TODO = "todo";
	private static final String NO_TODO_MESSAGE = "Please add something to do:";
	private EditText edtNewItemET;
	private DatePicker datePicker;
	private Button btnOK;
	private Button btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_todo_list);
		
		edtNewItemET = (EditText)findViewById(R.id.edtNewItem);
		datePicker = (DatePicker)findViewById(R.id.datePicker);
		btnOK = (Button)findViewById(R.id.btnOK);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED, getIntent());
				finish();
			}
		});
		
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String todo = edtNewItemET.getText().toString();
				if (todo.equals("")){
					Toast.makeText(getApplicationContext(), NO_TODO_MESSAGE,Toast.LENGTH_LONG).show();
					return;
				}
				
				int day = datePicker.getDayOfMonth();
			    int month = datePicker.getMonth();
			    int year =  datePicker.getYear();

			    Calendar calendar = Calendar.getInstance();
			    calendar.set(year, month, day);

			    Date date = calendar.getTime();
				Intent intent = getIntent();
				
				intent.putExtra(DATE, date.getTime());
				intent.putExtra(TODO, todo);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

}
