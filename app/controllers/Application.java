
package controllers;


import main.Main;
import play.mvc.*;

public class Application extends Controller {
	private static int _instance = 0;
	
	public static Result index() {
		return ok("");
	}

	public static Result getNotified() {		
		if (0 == _instance) {			
			System.out.println("server started...");			
			new Main().start();
		} else {
			System.out.println("connected... " + _instance);
		}		
		_instance++;
		return ok("");
	}
}
