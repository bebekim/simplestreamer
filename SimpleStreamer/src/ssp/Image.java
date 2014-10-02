package ssp;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.*; 


public class Image extends ProtocolMessage {

	byte[] image;
	
	public Image() {
		super();
	}
	
	public Image(byte[] data) {
		this.image = data;
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
			this.image = Base64.decodeBase64((String) obj.get("data"));
		}
	}

	@Override
	public String Type() {
		return "image";
	}
	
	public byte[] Data() {
		return this.image;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj=new JSONObject();
		obj.put("type", Type());
		try {
			obj.put("data", new String(Base64.encodeBase64(image),"US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return obj.toJSONString();
		
	}

}
