package peer;

import java.io.PrintWriter;

import org.apache.commons.codec.binary.Base64;

import ssp.Image;

// PeerSend.java
// Thread runs alongside a Peer Thread
// Sends stuff to the out stream.. make sure to consider synchronization of that stream

public class PeerSend implements Runnable {
	
	private PrintWriter out;
	private int rate;
	private volatile byte[] image_buffer = null; // Current image to be sent, need to synchronize this
	
	private volatile boolean stopstream = false;
	
	// Temp
	private Viewer viewer;
	
	// Spawned from Peer's constructor
	public PeerSend(PrintWriter out, int rate, Viewer viewer){
		this.out = out;
		this.rate = rate;
		this.viewer = viewer;
	}
	
	@Override
	public void run() {
		while (!stopstream) {
			try {
				if (image_buffer != null) {
					sendImage(image_buffer);
					//System.err.println("Not Empty!");
				} else {
					//System.err.println("Image buffer Empty!");
				}
				Thread.sleep(rate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void sendImage(byte[] image) {
		
		// Send image json
		Image imagejson = new Image(image);
		char[] buf = imagejson.ToJSON().toCharArray();
		out.println(buf);
		out.flush();
	}
	
	// For Webcam to broadcast
	public void addImageToBuffer(byte[] image){
		this.image_buffer = image;
	}
	
	// 
	private byte[] getImageFromBuffer(){
		byte[] image = image_buffer;
		image_buffer = null;
		return image;
	}
	
	public void stopStreaming(){
		stopstream = true;
	}
}
