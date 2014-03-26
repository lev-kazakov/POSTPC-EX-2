package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TodosDataSource {

	private static final String PARSE_CLASS_NAME = "todo";
	private SQLiteDatabase db;
	private SQLiteDBHelper SQliteHelper;
	private String[] allColumns = { SQLiteDBHelper.COLUMN_ID, SQLiteDBHelper.COLUMN_TITLE, SQLiteDBHelper.COLUMN_DUE };
	
	public TodosDataSource(Context context) {
		SQliteHelper = new SQLiteDBHelper(context);
	}
	
	public void open() {
		db = SQliteHelper.getWritableDatabase();
	}
	
	public void close() {
		SQliteHelper.close();
	}
	
	public static TodoItem cursorToTodo(Cursor cursor) {
		long id = cursor.getLong(0);
		String title = cursor.getString(1);
		Date due = new Date(cursor.getLong(2));
		
		TodoItem todo = new TodoItem(id, title, due);
		return todo;
	}
	
	public void addTodo(String title, Date dueDate) {
		ParseObject todoParseObject = new ParseObject(PARSE_CLASS_NAME);
		todoParseObject.put(SQLiteDBHelper.COLUMN_TITLE, title);
		todoParseObject.put(SQLiteDBHelper.COLUMN_DUE, dueDate.getTime());
		todoParseObject.saveInBackground();
		
		ContentValues values = new ContentValues();
		values.put(SQLiteDBHelper.COLUMN_TITLE, title);
		values.put(SQLiteDBHelper.COLUMN_DUE, dueDate.getTime());
		db.insert(SQLiteDBHelper.TABLE_TODO, null, values);
	}

	public List<TodoItem> getAllTodos() {
		List<TodoItem> todoList = new ArrayList<TodoItem>();
		
		Cursor cursor = db.query(SQLiteDBHelper.TABLE_TODO, allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (cursor.moveToNext()) {
			TodoItem todo = cursorToTodo(cursor);
			todoList.add(todo);
		}
		cursor.close();
		
		return todoList;
	}
	
	public void deleteTodo(long id) {
		Cursor c = db.query(SQLiteDBHelper.TABLE_TODO, allColumns, SQLiteDBHelper.COLUMN_ID + "=" + id, null, null, null, null);
		c.moveToFirst();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASS_NAME);
		query.whereEqualTo(SQLiteDBHelper.COLUMN_TITLE, c.getString(c.getColumnIndex(SQLiteDBHelper.COLUMN_TITLE)))
			.whereEqualTo(SQLiteDBHelper.COLUMN_DUE, c.getLong(c.getColumnIndex(SQLiteDBHelper.COLUMN_DUE)))
			.getFirstInBackground(new GetCallback<ParseObject>() {
				public void done(ParseObject object, ParseException e) {
					if (object != null) {
						object.deleteInBackground();
					}
				}});
		
		c.close();
		
		db.delete(SQLiteDBHelper.TABLE_TODO, SQLiteDBHelper.COLUMN_ID + " = " + id, null);
	}
	
	public Cursor getCursorForAllTodos() {
		Cursor cursor = db.query(SQLiteDBHelper.TABLE_TODO, allColumns, null, null, null, null, null);
		return cursor;
	}

}
