package com.abhinavgpt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient
{
	public static void main(String[] args)
	{
		String host = "localhost";
		int port = 8023;

		try (Socket socket = new Socket(host, port))
		{
			System.out.println("Connecting to server...");

			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true);
			String message;

			System.out.println("Type message (type 'exit' to quit): ");

			// Loop continues until userInput.readLine() returns null (EOF or input stream closed)
			while ((message = userInput.readLine()) != null)
			{
				serverOutput.println(message);
				String response = serverInput.readLine();
				System.out.println(response);

				// System.exit(0) is used here to forcefully terminate the JVM when 'exit' is typed,
				// which immediately stops all threads and closes resources. This is abrupt and can
				// skip finally blocks or shutdown hooks, but is simple for CLI tools.
				if (message.equalsIgnoreCase("exit")) System.exit(0);
			}

			// These close calls are redundant due to try-with-resources, but if the loop exits
			// without System.exit (e.g. EOF on stdin), they ensure explicit cleanup.
			userInput.close();
			serverInput.close();
			serverOutput.close();

		} catch (IOException e)
		{
			// Wrapping IOException in RuntimeException propagates errors up without checked handling,
			throw new RuntimeException(e);
		}
	}
}
