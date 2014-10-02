package ssp;

import org.json.simple.JSONObject;

public class StopStream extends ProtocolMessage {
	
	public StopStream() {
		super();
	}
	
	@Override
	void FromJSON(String s) {
		// do nothing.
	}

	@Override
	public String Type() {
		return "stopstream";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		
		obj.put("type", Type());
		
		return obj.toJSONString();
	}

}
