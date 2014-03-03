package il.ac.huji.todolist;

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

public class ToDoArrayAdapter<S> extends ArrayAdapter<String> {
	
	private static final int TYPE_FIRST = 0;
    private static final int TYPE_SECOND = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SECOND + 1;
	
	private LayoutInflater inflater;
	private Context mContext;
	
	public ToDoArrayAdapter(Context context, int resource, List<String> list) {
		super(context, resource, list);
		mContext = context;
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
        return (position % 2 == 0) ? TYPE_FIRST : TYPE_SECOND;
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
		TextView todoItem = ViewHolder.get(view, R.id.todoItem);
		// set color
		int type = getItemViewType(position);
		switch (type) {
		case TYPE_FIRST:
			todoItem.setTextColor(Color.RED); // better mContext.getResources().getColor(R.color.firstItem)?
			break;
		case TYPE_SECOND:
			todoItem.setTextColor(Color.BLUE); // better mContext.getResources().getColor(R.color.secondItem)?
			break;
		}
		// set text
		todoItem.setText(getItem(position));
		
		Log.v("Adapter: ", "EXIT getView " + Integer.toString(position));
		
		return view;
	}
}
