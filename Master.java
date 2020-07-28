package program;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;

public class Master {

	public static void main(String[] args) {
		ArrayList<String> requests = new ArrayList<>();
		ArrayList<String> c1Response = new ArrayList<>();
		ArrayList<String> c2Response = new ArrayList<>();

		args = new String[] { "30121", "30122" };

		if (args.length != 2) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);
		int portNumber2 = Integer.parseInt(args[1]);
		final int THREADS = 2;
		System.out.println("---MASTER---");

		try (ServerSocket clientSocket = new ServerSocket(portNumber);
				ServerSocket slaveSocket = new ServerSocket(portNumber2)) {
			ArrayList<ClientThread> clientThreads = new ArrayList<ClientThread>();
			ArrayList<SlaveThread> slaveThreads = new ArrayList<SlaveThread>();

			for (int i = 0; i < THREADS; i++) {
				if (i == 0) {
					clientThreads.add(new ClientThread(clientSocket, i, requests, c1Response));
				} else {
					clientThreads.add(new ClientThread(clientSocket, i, requests, c2Response));
				}
			}
			for (ClientThread t : clientThreads) {
				t.start();
			}

			for (int i = 0; i < THREADS; i++) {
				slaveThreads.add(new SlaveThread(slaveSocket, i, c1Response, c2Response));
			}
			for (SlaveThread t : slaveThreads) {
				t.start();
			}
			while (true) {
				while (requests.isEmpty()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while (!requests.isEmpty()) {
					Collections.sort(slaveThreads);
					slaveThreads.get(0).jobs.add(requests.remove(0));
				}
			}
		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + "30122" + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}
}
