// Peer.java
// Responsible for communications with a particular peer
// Is tied to a single Viewer (ie. 1 JFrame per peer)
// Also responsible for broadcasting

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class Peer implements Runnable {
	
	// Global list of peers
	private static List<Peer> peerlist = Collections.synchronizedList(new ArrayList<Peer>());
	
	// Stuff to Talk to peer
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	
	int peer_no; // Thread debugging purposes
	
	String hostname;
	int port;
	
	private Viewer viewer;
	
	private int jframewidth;
	private int jframeheight;
	
	public Peer(Socket socket, int peer_no, String type) throws NegotiationException {
		
		this.hostname = socket.getInetAddress().getCanonicalHostName();
		this.port = socket.getPort();
		this.peer_no = peer_no;
				
		// Set up network stuff
		this.socket = socket;
		
		try {
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out = new PrintWriter(this.socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// CLIENT is when Peer initiates connection to other Peer
		if (type.equals("CLIENT")){
			clientNegotiation();
		// SERVER is when Peer receives connection from other Peer
		} else if (type.equals("SERVER")){
			serverNegotiation();
		}

		// Set up Viewer (jframewidth/jframeheight obtained from negotiations)
		viewer = new Viewer(jframewidth,jframeheight,hostname,peer_no);
		
		// Add newly created peer (this) to peerlist
		// Always do this at end of constructor.. else null ptr from Viewer
		addPeer(this);
	}
	
	private void clientNegotiation() throws NegotiationException {
		System.err.println("Connected to peer");
		out.println("Hello From Client");
		
		// Negotiations (Obtain jframe width/height)
		jframewidth = 320;
		jframeheight = 240;
		
		//out.flush();

		//String str = in.readLine();
		//System.out.println("Server:" + str);
	}
	
	private void serverNegotiation() throws NegotiationException {
		System.err.println("Received connection from peer");
		out.println("Hello From Server");
		
		// Negotiations (Obtain jframe width/height)
		jframewidth = 320;
		jframeheight = 240;
	}
	
	@Override
	public void run() {
		// Peer infinite loop used to listen to in stream?
		while (true) {
			//System.err.println("Thread "+peer_no+" reporting!");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void sendImage(Object obj){
		/*
		 * Right now we send the image back to this peer (so you see your own image..)
		 * What needs to be done is to send this to the other peer (through out stream)
		 */
		
		receiveImage(obj);
	}
	
	private void receiveImage(Object obj){
		// Decompress then render (right now its receiving from this peer
		
		byte[] nobase64_image = Base64.decodeBase64((byte[]) obj);
		/* Decompress the image */
		byte[] decompressed_image = Compressor.decompress(nobase64_image);
		/* Give the raw image bytes to the viewer. */
		viewer.ViewerInput(decompressed_image);
	}
	
	// Methods to handle peerlist (threadsafe)
	private static void addPeer(Peer peer){
		synchronized (peerlist) {
			// Add peer
			peerlist.add(peer);
			System.err.println("Adding Peer --- Currently "+peerlist.size());
		}
	}
	
	// Probably call this from some exception?
	private static void removePeer(Peer peer){
		synchronized (peerlist) {
			// Remove peer
			System.err.println("Removing Peer --- Currently "+peerlist.size());
		}
	}
	
	// Called from Webcam.java everything frame generated..
	public static void broadcastToPeers(Object obj){
		synchronized (peerlist) {
			for (int i = 0; i < peerlist.size(); i++){
				peerlist.get(i).sendImage(obj);
			}
		}  
	}
	
	@SuppressWarnings("serial")
	class NegotiationException extends Exception {
		NegotiationException(Exception e) {
			super(e);
		}
	}
}
