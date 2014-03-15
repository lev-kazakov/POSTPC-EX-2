package il.ac.huji.todolist;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class TodoItem implements Serializable{

	private String title;
	private Date dueDate;
	
	public TodoItem(String title, Date dueDate) {
		this.title = title;
		this.dueDate = dueDate;
	}

	public String getTitle() {
		return title;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
}
