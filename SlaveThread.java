package program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SlaveThread extends Thread implements Comparable<SlaveThread> {

	private ServerSocket serverSocket = null;
	int id;
	private Integer totalLoad;
	private ArrayList<String> c1Response;
	private ArrayList<String> c2Response;
	public ArrayList<String> jobs;

	public SlaveThread(ServerSocket s, int id, ArrayList<String> c1Response, ArrayList<String> c2Response) {
		serverSocket = s;
		this.id = id;
		this.c1Response = c1Response;
		this.c2Response = c2Response;
		jobs = new ArrayList<>();
		this.totalLoad = 0;
	}

	@Override
	public void run() {

		try (Socket clientSocket = serverSocket.accept();
				PrintWriter responseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader requestReader = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));) {
			while (true) {
				while (jobs.isEmpty()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				String job = jobs.remove(0);
				totalLoad += job.length();
				responseWriter.println(job);
				String response = requestReader.readLine();
				int clientID = Integer.parseInt(response.substring(response.length() - 1));
				if (clientID == 0) {
					synchronized (c1Response) {
						c1Response.add(response.substring(0, response.length() - 1));
					}
				} else {
					synchronized (c2Response) {
						c2Response.add(response.substring(0, response.length() - 1));
					}
				}
				totalLoad -= job.length();

			}
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port " + serverSocket.getLocalPort()
					+ " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}

	@Override
	public int compareTo(SlaveThread thread) {
		return this.totalLoad.compareTo(thread.getTotalLoad());
	}

	public Integer getTotalLoad() {
		return totalLoad;
	}

}
