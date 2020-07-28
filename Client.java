package program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

	public static void main(String args[]) throws IOException {

		InetAddress address = InetAddress.getLocalHost();
		String line = null;

		System.out.println("---CLIENT---");
		System.out.println("Enter two words to join together: (QUIT to end)");

		String response = null;
		try (Socket s1 = new Socket(address, 30121);
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				BufferedReader is = new BufferedReader(new InputStreamReader(s1.getInputStream()));
				PrintWriter os = new PrintWriter(s1.getOutputStream());) {
			line = br.readLine();
			while (line.compareTo("QUIT") != 0) {
				os.println(line);
				os.flush();
				response = is.readLine();
				System.out.println("***Compound Word Creation*** " + response);
				System.out.println("Enter two words to join together: (QUIT to end)");
				line = br.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Socket read Error");
		}
	}
}