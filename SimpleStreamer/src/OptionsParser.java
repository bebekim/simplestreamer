// OptionsParser.java
// Handles command line args parsing

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import org.kohsuke.args4j.Option;

public class OptionsParser {
	
	@Option(name = "-sport", required = false)
	private int sport = 6262;
	
	@Option(name = "-remote", required = false)
	private String hostnamestring;
	
	@Option(name = "-rport", required = false)
	private String rportstring;
	
	@Option(name = "-width", required = false)
	private int width = 320;
	
	@Option(name = "-height", required = false)
	private int height = 240;
	
	@Option(name = "-rate", required = false)
	private int rate = 100; // milliseconds
	
	private HostPortTuple[] host_port_tuples;
	
	public OptionsParser(String[] args) throws CmdLineException{
		CmdLineParser parser = new CmdLineParser(this);
		// Handle CmdLineException
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
		    System.err.println(e.getMessage());
		    parser.printUsage(System.err);
		    System.exit(1);	// Parsing fucked up
		}
		
		// Parse remote ports
		Integer[] rports;
		if (rportstring != null) {
			// Parse remote hostports into list
			String[] unparsed_rports = rportstring.split(",");
			rports = new Integer[unparsed_rports.length];
			// parse String's in unparsed_rports to Integers
			for (int i = 0; i < unparsed_rports.length; i++){
				rports[i] = Integer.parseInt(unparsed_rports[i]);
			}
			/*
			for (int i = 0; i < rports.length; i++) {
				System.err.println(rports[i]);
			}
			*/
		} else {
			rports = new Integer[]{};
		}
		
		// Parse remote hostnames (with ports if given)
		if (hostnamestring != null) {
			// Parse hostnames into list
			String[] hostnames = hostnamestring.split(",");
			
			// Create HostPortTuple
			host_port_tuples = new HostPortTuple[hostnames.length];
			
			for (int i = 0; i < hostnames.length; i ++) {
				//System.err.println(hostnames[i]);
				// if port exists for hostname, use that
				if (i < rports.length) {
					host_port_tuples[i] = new HostPortTuple(hostnames[i],rports[i]);
				} else {
					// Default port 6262 when port not specified
					host_port_tuples[i] = new HostPortTuple(hostnames[i],6262);
				}
			}
		} else {
			// no remote hosts specified, set to empty
			host_port_tuples = new HostPortTuple[]{};
		}
	}
	
	public int getSPort(){
		return sport;
	}
	
	// Return array of String,Integer tuples  
	public HostPortTuple[] getHosts(){
		return host_port_tuples;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getRate(){
		return rate;
	}
}
