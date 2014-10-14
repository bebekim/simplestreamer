package peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputCapture implements Runnable {

	BufferedReader br;

	public InputCapture() {
		br = new BufferedReader(new InputStreamReader(System.in));
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				String message = br.readLine();
				System.err.println("CLI Input : " + message);
				
				// Broadcast StopStream to all Peers
				Peer.broadcastStopStream();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
