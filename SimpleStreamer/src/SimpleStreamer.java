// SimpleStreamer.java
// Main class, responsible for setting up shit

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.kohsuke.args4j.CmdLineException;

public class SimpleStreamer {
	
	public static void main(String[] args) throws CmdLineException {

		// Parse command line options
		OptionsParser options = new OptionsParser(args);
		
		// Grab parsed options
		int sport = options.getSPort();
		int width = options.getWidth();
		int height = options.getHeight();
		int rate = options.getRate();
		HostPortTuple[] hosts = options.getHosts();	// empty list means no remote hosts specified
		
		// Debugging Messages - Print out arguments
		System.err.println("SimpleStreamer starting on Port "+sport);
		System.err.println("JFrame window width: "+width);
		System.err.println("JFrame window height: "+height);
		if (hosts.length > 0) {
			System.err.println("List of Remote Hosts/Ports:");
			for (int i = 0; i < hosts.length; i++) {
				System.err.println("hostname: "+hosts[i].hostname+" | port: "+hosts[i].port);
			}
		} else {
			System.err.println("No Remote Hosts found, will only listen for connections.");
		}
		System.err.println("Rate limit of "+rate+" ms");
		
		// Set up webcam
		//Thread webcam = new Thread(new Webcam(width,height));
		Thread webcam = new Thread(new Webcam(width,height));
		webcam.start();
		
		// Setup sockets and shit
		
		int peer_no = 1;	// Keep track of peers
		
		// Connect to all remote peers (this behaves like a client)
		for (int i = 0; i < hosts.length; i++) {
			Socket socket = null;	// Careful might have to synchronize this
			try {
				socket = new Socket(hosts[i].hostname, hosts[i].port);
				Thread connect_to_peer = new Thread(new Peer(socket, peer_no, "CLIENT"));
				connect_to_peer.start();
				peer_no++;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NegotiationException e) {
				// Negotiations failed
				e.printStackTrace();
			}
		}
		
		// Wait indefinitely for new Peers
		// Wait for peers to connect (this behaves like a server)
		ServerSocket serversocket = null;
		Socket socket = null;
		try {
			serversocket = new ServerSocket(sport);
			System.err.println("Server listening for incoming connection!");
			while (true) {
				socket = serversocket.accept();
				System.err.println("Connected.");
				// Thread new Peer
				Thread receive_connecting_peer = new Thread(new Peer(socket,peer_no, "SERVER"));
				receive_connecting_peer.start();
				
				peer_no++;
			}
			// out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NegotiationException e) {
			// Negotiations failed
			e.printStackTrace();
		} finally {
			if (socket != null)
				try {
					socket.close();
					serversocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
