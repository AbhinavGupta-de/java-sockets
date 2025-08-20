package com.abhinavgpt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer
{
	public static void main(String[] args)
	{
		int port = 8023;

		try (ServerSocket serverSocket = new ServerSocket(port))
		{
			System.out.println("Echo server started on port " + port);

			Socket clientSocket = serverSocket.accept();
			System.out.println("Client connected: " + clientSocket.getInetAddress());

			BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			String message;

			while ((message = inputReader.readLine()) != null)
			{
				System.out.println("Received: " + message);

				if (message.equalsIgnoreCase("exit"))
				{
					System.out.println("Exiting server.");
					break;
				}

				out.println("Echo: " + message);
				System.out.println("Sent: Echo: " + message);
			}

			inputReader.close();
			out.close();
			clientSocket.close();
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
