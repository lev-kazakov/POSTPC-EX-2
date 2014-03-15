package il.ac.huji.todolist;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	private int year;
	private int month;
	private int day;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_todo_item);
		
		final EditText edtNewItem = (EditText) findViewById(R.id.edtNewItem);
		final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
		Button btnOK = (Button) findViewById(R.id.btnOK);
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		datePicker.init(year, month, day, null);
		
		btnOK.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String newItemName = edtNewItem.getText().toString();
				year = datePicker.getYear();
				month = datePicker.getMonth();
				day = datePicker.getDayOfMonth();
				Date date = new GregorianCalendar(year, month, day).getTime();
				
				Intent result = new Intent();
				result.putExtra("title", newItemName);
				result.putExtra("dueDate", date);
				
				setResult(RESULT_OK, result);
				finish();
			}
		});
		
		btnCancel.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent result = new Intent();
				setResult(RESULT_CANCELED, result);
				finish();
			}
		});
		
	}

}
