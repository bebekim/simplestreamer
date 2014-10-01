// Peer.java
// Responsible for communications with a particular peer
// Is tied to a single Viewer (ie. 1 JFrame per peer)
// Also responsible for broadcasting

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class Peer implements Runnable {
	
	// Global list of peers
	private static List<Peer> peerlist = Collections.synchronizedList(new ArrayList<Peer>());
	
	int peer_no; // Thread debugging purposes
	
	String hostname;
	int port;
	
	private Viewer viewer;
	
	public Peer(String hostname, int port, int peer_no) {
		
		this.hostname = hostname;
		this.port = port;
		this.peer_no = peer_no;
		
		// Add newly created peer (this) to peerlist
		addPeer(this);
		
		// Set up Viewer
		viewer = new Viewer(320,240,"localhost",peer_no);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			System.err.println("Thread "+peer_no+" reporting!");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// Methods to handle peerlist (threadsafe)
	private static void addPeer(Peer peer){
		synchronized (peerlist) {
			// Add peer
			System.err.println("Adding Peer --- Currently "+peerlist.size());
			peerlist.add(peer);
		}
	}
	
	private void sendimage(Object obj){
		byte[] nobase64_image = Base64.decodeBase64((byte[]) obj);
		/* Decompress the image */
		byte[] decompressed_image = Compressor.decompress(nobase64_image);
		/* Give the raw image bytes to the viewer. */
		viewer.ViewerInput(decompressed_image);
	}
	
	
	// Probably call this from some exception?
	private static void removePeer(Peer peer){
		synchronized (peerlist) {
			// Remove peer
			System.err.println("Removing Peer --- Currently "+peerlist.size());
		}
	}
	
	public static void broadcastToPeers(Object obj){
		synchronized (peerlist) {
			for (int i = 0; i < peerlist.size(); i++){
				peerlist.get(i).sendimage(obj);
			}
		}  
	}
}
