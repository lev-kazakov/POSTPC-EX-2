package il.ac.huji.todolist;

import com.parse.Parse;
import com.parse.ParseUser;

import android.app.Application;

public class ToDoApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		Parse.initialize(this, "8NFAjS98ukTgBZmu1ezIUekXLbJgSLfznCUBg5Vm", "tcWKBajTWuT4aL2mFqw3AdJswmBedr2b70dNe2Ba");
		ParseUser.enableAutomaticUser();
//		ParseACL defaultACL = new ParseACL();
//		defaultACL.setPublicReadAccess(true);
//		defaultACL.setPublicWriteAccess(true);
//		ParseACL.setDefaultACL(defaultACL, true);
	}

}
