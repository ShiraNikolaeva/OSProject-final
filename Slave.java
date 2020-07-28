package program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Slave {

	public static void main(String args[]) throws IOException {

		InetAddress address = InetAddress.getLocalHost();
		System.out.println("---SLAVE---");

		try (Socket s1 = new Socket(address, 30122);
				BufferedReader is = new BufferedReader(new InputStreamReader(s1.getInputStream()));
				PrintWriter os = new PrintWriter(s1.getOutputStream());) {
			String response = null;
			while ((response = is.readLine()) != null) {
				int id = Integer.parseInt(response.substring(response.length() - 1, response.length()));
				response = response.substring(0, response.length() - 1);
				System.out.println("Job: " + response + " received");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				os.println(response.replaceAll(" ", "") + id);
				System.out.println("Job done ;)");
				os.flush();
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Socket read Error");
		}

	}
}