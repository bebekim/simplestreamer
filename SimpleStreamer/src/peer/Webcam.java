package peer;
// Webcam.java
// Responsible for obtaining images from webcam
// Aware of a list of Peers, will use something like Peer.send()

import org.apache.commons.codec.binary.Base64;
import org.bridj.Pointer;

// Optionally, use original Compressor.. but that fucking System.out.println makes it annoying
// import simplestream.Compressor;

import com.github.sarxos.webcam.ds.buildin.natives.Device;
import com.github.sarxos.webcam.ds.buildin.natives.DeviceList;
import com.github.sarxos.webcam.ds.buildin.natives.OpenIMAJGrabber;

public class Webcam implements Runnable {
	
	// Image grabber
	private OpenIMAJGrabber grabber = new OpenIMAJGrabber();
	
	// Resolution to transmit
	private int width;
	private int height;
	
	// Set up webcam
	public Webcam(int width, int height){
		
		this.width = width;
		this.height = height;
		
		// Get Device
		Device device = null;
		Pointer<DeviceList> devices = grabber.getVideoDevices();
		for (Device d : devices.get().asArrayList()) {
			device = d;
			break;
		}
		
		boolean started = grabber.startSession(width, height, 30, Pointer.pointerTo(device));
		if (!started) {
			throw new RuntimeException("Not able to start native grabber!");
		}
	}

	@Override
	public void run() {

		// Change this.. n,i just for testing
		int n = 1000;
		int i = 0;
		do {
			/* Get a frame from the webcam. */
			grabber.nextFrame();
			/* Get the raw bytes of the frame. */
			byte[] raw_image=grabber.getImage().getBytes(width * height * 3);
			/* Apply a crude kind of image compression. */
			byte[] compressed_image = Compressor.compress(raw_image);
			/* Prepare the date to be sent in a text friendly format. */
			byte[] base64_image = Base64.encodeBase64(compressed_image);
			
			// Send base64 encoded compressed image
			Peer.broadcastToPeers(base64_image);
			/*
			 * Assume we received some image data.
			 * Remove the text friendly encoding.
			 */
			
			//byte[] nobase64_image = Base64.decodeBase64(base64_image);
			/* Decompress the image */
			//byte[] decompressed_image = Compressor.decompress(nobase64_image);
			/* Give the raw image bytes to the viewer. */
			
			//viewer.ViewerInput(decompressed_image);
			
			//System.err.println("IMAGE : " + decompressed_image.length);
		} while (true);

		//grabber.stopSession();	
	}
}
