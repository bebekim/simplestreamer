package ssp;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.*; 


public class Image extends ProtocolMessage {

	String data;
	
	public Image() {
		super();
	}
	
	public Image(Object data) {
		this.data = data.toString();
	}
	
	@Override
	void FromJSON(String s) {
		JSONObject obj=null;
		
		try {
			obj = (JSONObject) parser.parse(s);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		if(obj!=null){
			this.data = Base64.decodeBase64((String) obj.get("data")).toString();
		}
	}

	@Override
	public String Type() {
		return "image";
	}
	
	public String Data() {
		return this.data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj=new JSONObject();
		obj.put("type", Type());
		obj.put("data", data);
		return obj.toJSONString();
	}

}
