package ssp;

import org.json.simple.*;

public class StartStream extends ProtocolMessage {

	private String format;
	private int width;
	private int height; 
	
	public StartStream(String format, int width, int height) {
		super();
		this.format = format;
		this.width = width;
		this.height = height;
	}
	
	public StartStream() {
		super();
	}
	
	@Override
	void FromJSON(String s) {
		JSONObject obj = null;		
		try {
			obj = (JSONObject) parser.parse(s);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		if(obj!=null){
			this.format = obj.get("format").toString();
			this.width = Integer.parseInt(obj.get("width").toString());
			this.height = Integer.parseInt(obj.get("height").toString());
		} 	
	}

	@Override
	public String Type() {
		return "startstream";
	}
	
	public String Format() {
		return this.format;
	}
	
	public int Width() {
		return this.width;
	}
	
	public int Height() {
		return this.height;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		
		JSONObject obj=new JSONObject();
		
		obj.put("type", Type());
		obj.put("format", Format());
		obj.put("width", Width());
		obj.put("height", Height());
		
		return obj.toJSONString();
	}

}
