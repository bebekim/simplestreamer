package ssp;

import org.json.simple.parser.*;

public abstract class ProtocolMessage {
	
	protected static final JSONParser parser = new JSONParser();
	
	abstract public String Type();
	
	abstract public String ToJSON();
	
	abstract void FromJSON(String s);
	
}
