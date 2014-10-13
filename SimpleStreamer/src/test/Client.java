package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import ssp.*;
import peer.*;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Socket socket = null;
		try {
			int port = 6262;
			String host = "localhost";
			socket = new Socket(host, port);
			
			try {
				Peer clientPeer = new Peer(socket, 100,	320, 240, 8, "CLIENT");
				clientPeer.run();
				
			} catch (NegotiationException e) {
				System.err.println("Client Negotiation Failed.");
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
					System.err.println("Client Disconnected.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
