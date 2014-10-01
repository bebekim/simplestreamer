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
		HostPortTuple[] hosts = options.getHosts();	// null means no remote hosts specified
		
		// Debugging Messages - Print out arguments
		System.err.println("SimpleStreamer starting on Port "+sport);
		System.err.println("JFrame window width: "+width);
		System.err.println("JFrame window height: "+height);
		if (hosts != null) {
			System.err.println("List of Remote Hosts/Ports:");
			for (int i = 0; i < hosts.length; i++) {
				System.err.println("hostname: "+hosts[i].hostname+" | port: "+hosts[i].port);
			}
		} else {
			System.err.println("No Remote Hosts found, will only listen for connections.");
		}
		System.err.println("Rate limit of "+rate+" ms");
		
		// Start server at sport
		
		
		// Connect to all remote hosts
		for (int i = 0; i < hosts.length; i++) {
			// remote.connect(hosts[i].hostname,hosts[i].port)
		}
		
		
	}
}
