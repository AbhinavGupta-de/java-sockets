package com.abhinavgpt;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

class MarketData
{
	final long timestamp;
	final double price;
	final int volume;

	MarketData(long ts, double p, int v)
	{
		this.timestamp = ts;
		this.price = p;
		this.volume = v;
	}
}

public class MarketFeedFast
{
	private static final int MESSAGE_SIZE = 20; // 8 + 8 + 4
	private static final int BATCH_SIZE = 1024 * 1024; // 1 MB buffer

	public static void main(String[] args) throws IOException
	{
		SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 5555));
		channel.configureBlocking(true);

		ByteBuffer buffer = ByteBuffer.allocateDirect(BATCH_SIZE);

		long start = System.nanoTime();
		int messagesRead = 0;

		while (messagesRead < 1_000_000)
		{
			buffer.clear();
			int bytesRead = channel.read(buffer);
			if (bytesRead == -1) break; // end of stream

			buffer.flip();

			while (buffer.remaining() >= MESSAGE_SIZE && messagesRead < 1_000_000)
			{
				long ts = buffer.getLong();
				double price = buffer.getDouble();
				int volume = buffer.getInt();

				// Create new object each time
				MarketData md = new MarketData(ts, price, volume);

				messagesRead++;
			}
		}

		long end = System.nanoTime();
		System.out.println("Elapsed: " + (end - start) / 1_000 + " ms");

		channel.close();
	}
}