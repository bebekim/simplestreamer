package peer;

import java.io.PrintWriter;

// PeerSend.java
// Thread runs alongside a Peer Thread
// Sends stuff to the out stream.. make sure to consider synchronization of that stream

public class PeerSend implements Runnable {
	
	private PrintWriter out;

	// Spawned from Peer's constructor
	public PeerSend(PrintWriter out){
		this.out = out;
	}
	
	@Override
	public void run() {
		
		///
		
	}
	
	private void sendImage() {
		// Reuse code in Peer.sendImage();
	}
	
	private void notifyNewImage() {
		// Called by broadcast?
	}
	
	
	
	
}
