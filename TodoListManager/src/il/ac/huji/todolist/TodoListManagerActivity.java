package il.ac.huji.todolist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TodoListManagerActivity extends Activity {

	private ArrayAdapter<String> todosAdapter;
	private ArrayList<String> todos;
	final Context context = this;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putStringArrayList("todos", todos);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		ListView lstTodoItems = (ListView) findViewById(R.id.lstTodoItems);
		lstTodoItems.setEmptyView(findViewById(android.R.id.empty));
		
		if (savedInstanceState != null) {
			todos = savedInstanceState.getStringArrayList("todos");
		}
		
		if (todos == null) {
			todos = new ArrayList<String>(); // better getResources().getStringArray(R.array.strArrTodos) ?
		}
		
		todosAdapter = new ToDoArrayAdapter<String>(getApplicationContext(),
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
				title.setText(((TextView) view.findViewById(R.id.todoItem))
						.getText());

				alertDialogBuilder.setCustomTitle(title).setPositiveButton(
						R.string.alertDelete,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								todos.remove(position);
								todosAdapter.notifyDataSetChanged();
							}
						});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemAdd:
			final EditText edtNewItem = (EditText) findViewById(R.id.edtNewItem);
			if (edtNewItem.getText().length() == 0) {
				return true;
			}
			todos.add(edtNewItem.getText().toString());
			todosAdapter.notifyDataSetChanged();
			edtNewItem.setText("");
			break;
		}
		return true;
	}

}
