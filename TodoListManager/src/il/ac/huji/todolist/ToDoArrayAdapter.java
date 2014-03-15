package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ToDoArrayAdapter<S> extends ArrayAdapter<TodoItem> {
	
	private static final int TYPE_PUNCTUAL = 0;
    private static final int TYPE_OVERDUE = 1;
    private static final int TYPE_MAX_COUNT = TYPE_OVERDUE + 1;
	
	private LayoutInflater inflater;
	private SimpleDateFormat dft;
	
	public ToDoArrayAdapter(Context context, int resource, List<TodoItem> list) {
		super(context, resource, list);
		dft = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
	}

    @Override
    public long getItemId(int position) {
        return position;
    }
	
    @Override
    public int getViewTypeCount() {
    	return TYPE_MAX_COUNT;
    }
    
    public int getItemViewType(int position) {
    	TodoItem todoItem = getItem(position);
    	Calendar c = new GregorianCalendar();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date today = c.getTime();
        return (today.after(todoItem.getDueDate())) ? TYPE_OVERDUE : TYPE_PUNCTUAL;
    }
    
    public static class ViewHolder {
        @SuppressWarnings("unchecked")
        public static <T extends View> T get(View view, int id) {
        	Log.v("Adapter: ", "-	ENTER ViewHolder");
            SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
            if (viewHolder == null) {
            	Log.v("Adapter: ", "-	Setting tag");
                viewHolder = new SparseArray<View>();
                view.setTag(viewHolder);
            }
            
            View childView = viewHolder.get(id);
            if (childView == null) {
            	Log.v("Adapter: ", "-	adding child to array");
                childView = view.findViewById(id);
                viewHolder.put(id, childView);
            }
            Log.v("Adapter: ", "-	EXIT ViewHolder");
            return (T) childView;
        }
    }
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("Adapter: ", "ENTER getView " + position + " " + (convertView != null));
		
		View view = convertView;
		
		if (view == null) {
			inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(R.layout.row, parent, false);
		}
		
		// get todoItem
		TextView title = ViewHolder.get(view, R.id.txtTodoTitle);
		TextView dueDate = ViewHolder.get(view, R.id.txtTodoDueDate);
		
		// set color
		int type = getItemViewType(position);
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
		title.setText(getItem(position).getTitle());
		dueDate.setText(dft.format(getItem(position).getDueDate()));
		
		Log.v("Adapter: ", "EXIT getView " + Integer.toString(position));
		
		return view;
	}
}
