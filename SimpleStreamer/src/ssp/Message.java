package ssp;

import org.json.simple.parser.*;

abstract class Message {
	
	protected static final JSONParser parser = new JSONParser();
	
	abstract public String Type();
	
	abstract public String ToJSON();
	
	abstract void FromJSON(String s);
	
}
