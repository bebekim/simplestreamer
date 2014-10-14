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
			System.out.println("Server's listening...");
			//Peer serverPeer = new Peer(socket, 2, "SERVER");
			
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			
			byte[] picture = Files.readAllBytes(Paths.get("src/test/pix.ari"));
			
			byte[] compressed_image = Compressor.compress(picture);
			
			Image image = new Image(compressed_image);
			String imageMessage = image.ToJSON();
			System.out.println(imageMessage);

			while (true) {
				//serverPeer.out.write(image.ToJSON());
				out.write(image.ToJSON());
				out.println();
				out.flush();
				System.out.println("Image Sent...");
				Thread.sleep(1000);
			}
			
		} catch (IOException e) {
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
