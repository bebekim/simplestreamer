// SimpleStreamer.java
// Main class, responsible for setting up shit

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
		
		// Connect to all remote hosts
		for (int i = 0; i < hosts.length; i++) {
			// remote.connect(hosts[i].hostname,hosts[i].port)
			Thread new_peer = new Thread(new Peer(hosts[i].hostname, hosts[i].port, i));
			new_peer.start();
		}
		
		// Wait indefinitely for new Peers
		// Thread new Peer
	}
}
