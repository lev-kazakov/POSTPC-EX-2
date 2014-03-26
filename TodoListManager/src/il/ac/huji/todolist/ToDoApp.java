package il.ac.huji.todolist;

import com.parse.Parse;
import com.parse.ParseUser;

import android.app.Application;

public class ToDoApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		Parse.initialize(this, "hR9KVSBekShAlSENwE6SZKz0tzqiHystl9HKJyIw", "mdYlcbUxYHi2WZxivVo3vEafsuUhlLPRMpbRZsuW");
		ParseUser.enableAutomaticUser();
//		ParseACL defaultACL = new ParseACL();
//		defaultACL.setPublicReadAccess(true);
//		defaultACL.setPublicWriteAccess(true);
//		ParseACL.setDefaultACL(defaultACL, true);
	}

}
