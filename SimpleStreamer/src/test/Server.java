package test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import peer.*;
import ssp.*;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Socket socket = null;
		ServerSocket serverSocket = null;
				
		try {
			int port = 6262;
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			System.out.println("Server's listening...");
			Peer serverPeer = new Peer(socket, 2, "SERVER");
			
			byte[] picture = Files.readAllBytes(Paths.get("src/test/pocoyo.jpg"));
			Image image = new Image(picture.toString());

			while (true) {
				out.write(image.ToJSON());
				out.flush();
				System.out.println("Image Sent...");
				Thread.sleep(1000);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NegotiationException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
