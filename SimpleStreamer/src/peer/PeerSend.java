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
		while (true) {
			try {
				if (image_buffer != null) {
					sendImage(image_buffer);
					System.err.println("Not Empty!");
				} else {
					System.err.println("Image buffer Empty!");
				}
				Thread.sleep(10*rate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void sendImage(byte[] image) {
		// Reuse code in Peer.sendImage();
		byte[] nobase64_image = Base64.decodeBase64(image);
		byte[] decompressed_image = Compressor.decompress(nobase64_image);
		viewer.ViewerInput(decompressed_image);
	}
	
	/*
	  
	  
	 	private void sendImage(byte[] frame){

		 // Right now we send the image back to this peer (so you see your own image..)
		 // What needs to be done is to send this to the other peer (through out stream)

		Image imageMessage = new Image(frame);
		String imageStr = imageMessage.ToJSON();
		out.write(imageStr);
		out.println();

		//receiveImage(obj);
	}
	  
	 */
	
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
}
