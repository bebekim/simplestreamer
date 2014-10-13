package peer;
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

import ssp.*;

public class Peer implements Runnable {
	
	// Global list of peers
	private static List<Peer> peerlist = Collections.synchronizedList(new ArrayList<Peer>());
	
	// Stuff to Talk to peer
	Socket socket;
	
	//TODO: Temporary PUBLIC visibility for IO.
	
	private BufferedReader in;
	private PrintWriter out;
	
	int peer_no; // Thread debugging purposes
	
	String hostname;
	int port;
	
	private Viewer viewer;
	
	private int jframewidth;
	private int jframeheight;
	
	private PeerSend sender;
	
	public Peer(Socket socket, int rate, int peer_no, String type) throws NegotiationException {
		
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
		
		System.err.println("Begin Negotiations");
		// CLIENT is when Peer initiates connection to other Peer
		if (type.equals("CLIENT")){
			clientNegotiation();
		// SERVER is when Peer receives connection from other Peer
		} else if (type.equals("SERVER")){
			serverNegotiation();
		}
		System.err.println("End Negotiations");
		
		// Set up Viewer (jframewidth/jframeheight obtained from negotiations)
		viewer = new Viewer(jframewidth,jframeheight,hostname,peer_no);
		
		// Set up Sender
		sender = new PeerSend(out, rate, viewer); // temporary viewer
		Thread sendthread = new Thread(sender);
		sendthread.start();
		
		// Add newly created peer (this) to peerlist
		// Always do this at end of constructor.. else null ptr from Viewer
		addPeer(this);
	}
	
	private void clientNegotiation() throws NegotiationException {
		
		char[] buf;
		
		System.err.println("Client attempting Neogotiations");
		
		// TODO: TEMPORARY, Negotiations (Obtain jframe width/height)
		jframewidth = 320;
		jframeheight = 240;
		
		StartStream m = new StartStream("raw", jframewidth, jframeheight);
		buf = m.ToJSON().toCharArray();

		out.println(buf);
		System.err.println("Client sent StartStream");
		
		//String str = in.readLine();
		//System.out.println("Server:" + str);
	}
	
	private void serverNegotiation() throws NegotiationException {
		
		System.err.println("Waiting for Client to Negotiate");
		
		ProtocolFactory pmFac = new ProtocolFactory();
		
		try {
			String mStr = in.readLine();
			System.err.println("Received Line: " + mStr);
			ProtocolMessage pm = pmFac.FromJSON(mStr);
			if (pm.Type().equals("startstream")) {
				StartStream startMessage = (StartStream) pm;
				this.jframeheight = startMessage.Height();
				this.jframewidth = startMessage.Width();
			} else {
				throw new NegotiationException("Invalid Negotiation Received");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// Peer infinite loop used to listen to in stream?
		while (true) {
			//System.err.println("Thread "+peer_no+" reporting!");
			try {
				receiveImage();
				//Thread.sleep(100);
			} catch (ProtocolException e) {
				e.printStackTrace();
				System.err.println("Protocol Exception | Interrupted Exception");
				System.exit(-1);
			}
		}
	}
	
	
	private void sendImage(byte[] frame){
		/*
		 * Right now we send the image back to this peer (so you see your own image..)
		 * What needs to be done is to send this to the other peer (through out stream)
		 */
		Image imageMessage = new Image(frame);
		String imageStr = imageMessage.ToJSON();
		out.write(imageStr);
		out.println();

		//receiveImage(obj);
	}
	
	private void receiveImage() throws ProtocolException{
		// Decompress then render (right now its receiving from this peer
		System.err.println("Waiting for Image..");
		ProtocolFactory pmFac = new ProtocolFactory();
		byte[] nobase64_image;
		byte[] decompressed_image;
		
		try {
			String mStr = in.readLine();
			System.err.println("Something came in...");
			System.err.println("Received Line : "+mStr);
			ProtocolMessage pm = pmFac.FromJSON(mStr);
			if (pm.Type().equals("image")) {
				Image imageMessage = (Image) pm;
				nobase64_image = imageMessage.Data();
				decompressed_image = Compressor.decompress(nobase64_image);
				System.out.println("Image Received...");
			} else {
				throw new ProtocolException("Invalid Protocol Message Received.");
			}
			viewer.ViewerInput(decompressed_image);
		} catch (IOException e) {
			//TODO: @JunMin, you might want to move this to throwables too to handle this error elsewhere. 
			e.printStackTrace();
		}
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
				peerlist.get(i).sender.addImageToBuffer((byte[]) obj);
			}
		}  
	}
}
