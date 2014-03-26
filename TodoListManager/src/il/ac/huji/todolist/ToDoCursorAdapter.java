package il.ac.huji.todolist;

import android.widget.SimpleCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class ToDoCursorAdapter extends SimpleCursorAdapter {

	private static final int TYPE_PUNCTUAL = 0;
	private static final int TYPE_OVERDUE = 1;
	
	private SimpleDateFormat dft;
	
	@SuppressWarnings("deprecation")
	public ToDoCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		dft = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
	}
    
	@Override
    public void bindView(View v, Context context, Cursor c) {
 
		TextView title = (TextView) v.findViewById(R.id.txtTodoTitle);
		TextView dueDate = (TextView) v.findViewById(R.id.txtTodoDueDate);
		
		// get todoItem
		TodoItem todoItem = TodosDataSource.cursorToTodo(c);
		
		// set color
		int type = getItemViewType(todoItem);
		switch (type) {
		case TYPE_OVERDUE:
			title.setTextColor(Color.RED); // better mContext.getResources().getColor(R.color.firstItem)?
			dueDate.setTextColor(Color.RED);
			break;
		case TYPE_PUNCTUAL:
			title.setTextColor(Color.BLUE); // better mContext.getResources().getColor(R.color.secondItem)?
			dueDate.setTextColor(Color.BLUE);
			break;
		}
		// set text
		title.setText(todoItem.getTitle());
		dueDate.setText(dft.format(todoItem.getDueDate()));
    }
	
    public int getItemViewType(TodoItem todo) {
    	Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();
        return (today.after(todo.getDueDate())) ? TYPE_OVERDUE : TYPE_PUNCTUAL;
    }
    
}

