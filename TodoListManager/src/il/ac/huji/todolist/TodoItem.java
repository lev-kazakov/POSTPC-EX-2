package il.ac.huji.todolist;

import java.io.Serializable;
import java.util.Date;


public class TodoItem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id;
	private String title;
	private Date dueDate;
	
	public TodoItem(long id, String title, Date dueDate) {
		this.id = id;
		this.title = title;
		this.dueDate = dueDate;
	}

	public long getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
}
