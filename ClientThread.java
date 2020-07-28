
package program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread {

	private ServerSocket serverSocket = null;
	int id;
	private ArrayList<String> requests;
	private ArrayList<String> cResponse;

	public ClientThread(ServerSocket s, int id, ArrayList<String> requests, ArrayList<String> cResponse) {
		serverSocket = s;
		this.id = id;
		this.requests = requests;
		this.cResponse = cResponse;
	}

	@Override
	public void run() {
		try (Socket clientSocket = serverSocket.accept();
				PrintWriter responseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader requestReader = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));) {
			String requestString;
			while (true) {
				while ((requestString = requestReader.readLine()) != null) {
					System.out
							.println(requestString + " received from client: " + id + "\t\tSending to available slave");
					synchronized (requests) {
						requests.add(requestString + id);
					}
					while (cResponse.isEmpty()) {
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					responseWriter.println(cResponse.remove(0));
				}
			}
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port " + serverSocket.getLocalPort()
					+ " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
}
