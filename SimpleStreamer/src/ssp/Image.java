package ssp;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.*; 


public class Image extends Message {

	byte[] data;
	
	public Image() {
		super();
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
			this.data = Base64.decodeBase64((String) obj.get("bytes"));
		}
	}

	@Override
	public String Type() {
		return "image";
	}
	
	public byte[] Data() {
		return this.data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj=new JSONObject();
		try {
			obj.put("type", Type());
			obj.put("data", new String(Base64.encodeBase64(this.data),"US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return obj.toJSONString();
	}

}
