package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TodoListManagerActivity extends Activity {

	protected static final int REQUEST_NEW_ITEM = 01;
	private ArrayAdapter<TodoItem> todosAdapter;
	private ArrayList<TodoItem> todos;
	final Context context = this;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("todos", todos);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		ListView lstTodoItems = (ListView) findViewById(R.id.lstTodoItems);
		lstTodoItems.setEmptyView(findViewById(android.R.id.empty));

		if (savedInstanceState != null) {
			todos = (ArrayList<TodoItem>) savedInstanceState
					.getSerializable("todos");
		}

		if (todos == null) {
			todos = new ArrayList<TodoItem>();
		}

		todosAdapter = new ToDoArrayAdapter<TodoItem>(getApplicationContext(),
				R.layout.row, todos);

		lstTodoItems.setAdapter(todosAdapter);
		todosAdapter.notifyDataSetChanged();

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
						.findViewById(R.id.btnDelete);

				btnDelete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						todos.remove(position);
						todosAdapter.notifyDataSetChanged();
						alertDialog.dismiss();
					}
				});

				if (itemTitle.toLowerCase(Locale.getDefault()).startsWith(
						"call")) {
					final String phoneNumber = itemTitle.substring(5).trim();
					Button btnCall = (Button) getLayoutInflater().inflate(
							R.layout.alert_call, null);
					
					if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
						btnCall.setText("Call " + phoneNumber);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 01:
			if (resultCode == RESULT_OK) {
				String title = data.getStringExtra("title");
				if (title.length() == 0) {
					return;
				}
				Date dueDate = (Date) data.getSerializableExtra("dueDate");
				todos.add(new TodoItem(title, dueDate));
				todosAdapter.notifyDataSetChanged();
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

}
