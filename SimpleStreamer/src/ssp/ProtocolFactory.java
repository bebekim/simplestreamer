package ssp;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ProtocolFactory {

	private static final JSONParser parser = new JSONParser();
	
	// returns null on any problems
	public ProtocolMessage FromJSON(String s){
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(s);
		} catch (ParseException e) {
			// alert the user
			e.printStackTrace();
			return null;
		}
		if(obj!=null){
			ProtocolMessage m = null;
			if(obj.get("type").equals("startstream"))
				m = new StartStream();
			else if(obj.get("type").equals("stopstream"))
				m = new StopStream();
			else if (obj.get("type").equals("image"))
				m = new Image();
			m.FromJSON(s);
			return m;
		} else return null;
	}
	
	
}
