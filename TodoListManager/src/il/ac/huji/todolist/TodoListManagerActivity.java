package il.ac.huji.todolist;

import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TodoListManagerActivity extends Activity {

	private static final int REQUEST_NEW_ITEM = 01;
	
	private TodosDataSource datasource;
	private ToDoCursorAdapter todosCursorAdapter;
	final Context context = this;
	
	private static final String[] DB_FROM = { SQLiteDBHelper.COLUMN_TITLE, SQLiteDBHelper.COLUMN_DUE };
	private static final int[] DB_TO = { R.id.txtTodoTitle, R.id.txtTodoDueDate };


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		ListView lstTodoItems = (ListView) findViewById(R.id.lstTodoItems);
		lstTodoItems.setEmptyView(findViewById(android.R.id.empty));
		
		datasource = new TodosDataSource(context);
		todosCursorAdapter = new ToDoCursorAdapter(getApplicationContext(),
				R.layout.row, null, DB_FROM, DB_TO);
		lstTodoItems.setAdapter(todosCursorAdapter);
		

		// update list
		UpdateTodoList update = new UpdateTodoList();
		update.execute();

		// delete task interface
		lstTodoItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(android.widget.AdapterView<?> a,
					View view, final int position, long id) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);

				TextView title = (TextView) View.inflate(context,
						R.layout.alert_title, null);
				String itemTitle = ((TextView) view
						.findViewById(R.id.txtTodoTitle)).getText().toString();
				title.setText(itemTitle);
				
				final AlertDialog alertDialog = alertDialogBuilder
						.setCustomTitle(title).create();

				LinearLayout alertView = (LinearLayout) getLayoutInflater()
						.inflate(R.layout.alert_view, null);
				Button btnDelete = (Button) alertView
						.findViewById(R.id.menuItemDelete);

				btnDelete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						DeleteTodoItem delete = new DeleteTodoItem();
						delete.execute(position);
						alertDialog.dismiss();
					}
				});

				// add call option
				if (itemTitle.toLowerCase(Locale.getDefault()).startsWith(
						"call ")) {
					final String phoneNumber = itemTitle.substring(5).trim();
					Button btnCall = (Button) getLayoutInflater().inflate(
							R.layout.alert_call, null);
					
					if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
						btnCall.setText(itemTitle);
						btnCall.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent dial = new Intent(Intent.ACTION_DIAL,
										Uri.parse("tel:" + phoneNumber));
								startActivity(dial);
								alertDialog.dismiss();
							}
						});
					} else {
						btnCall.setText("Unsupported Phone Number");
						btnCall.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								alertDialog.dismiss();
							}
						});
					}
					
					alertView.addView(btnCall);
				}

				alertDialog.setView(alertView);
				alertDialog.show();

				return true;
			}
		});

	}

	// add task interface
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 01:
			if (resultCode == RESULT_OK) {
				String title = data.getStringExtra("title");
				Date dueDate = (Date) data.getSerializableExtra("dueDate");
				if (title.length() == 0 || title == null || dueDate == null) {
					return;
				}
				
				// add a todo item
				AddTodoItem add = new AddTodoItem(title, dueDate);
				add.execute();

			}
			break;

		default:
			break;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemAdd:
			Intent intent = new Intent(getApplicationContext(),
					AddNewTodoItemActivity.class);
			startActivityForResult(intent, REQUEST_NEW_ITEM);
		}
		return true;
	}
	
	
	private class UpdateTodoList extends AsyncTask<Void, Cursor, Void> {
		
		@Override
		protected void onPreExecute() {
			datasource.open();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			Cursor cursor = datasource.getCursorForAllTodos();
			// force the query to be fully executed
			// https://groups.google.com/forum/#!topic/android-developers/wTRuxuIxDAw
			cursor.getCount();
			// just playing with the API
			publishProgress(cursor);
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Cursor... values) {
			todosCursorAdapter.changeCursor(values[0]);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			datasource.close();
		}
		
	}
	
	private class DeleteTodoItem extends AsyncTask<Integer, Cursor, Void> {
		
		@Override
		protected void onPreExecute() {
			datasource.open();
		}
		
		@Override
		protected Void doInBackground(Integer... params) {
			datasource.deleteTodo(todosCursorAdapter.getItemId(params[0]));
			Cursor cursor = datasource.getCursorForAllTodos();
			// force the query to be fully executed
			cursor.getCount();
			publishProgress(cursor);
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Cursor... values) {
			todosCursorAdapter.changeCursor(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			datasource.close();
		}
		
	}
	
	private class AddTodoItem extends AsyncTask<Void, Cursor, Void> {
			
			private String title;
			private Date dueDate;
			
			public AddTodoItem(String title, Date dueDate) {
				this.title = title;
				this.dueDate = dueDate;
			}
			
			@Override
			protected void onPreExecute() {
				datasource.open();
			}
			
			@Override
			protected Void doInBackground(Void... params) {
				datasource.addTodo(title, dueDate);
				Cursor cursor = datasource.getCursorForAllTodos();
				// force the query to be fully executed
				cursor.getCount();
				publishProgress(cursor);
				return null;
			}
			
			@Override
			protected void onProgressUpdate(Cursor... values) {
				todosCursorAdapter.changeCursor(values[0]);
			}
			
			@Override
			protected void onPostExecute(Void result) {
				datasource.close();
			}
	}

}
