import socket
import struct
import time
import random

HOST = "127.0.0.1"
PORT = 5555

def generate_market_data():
    timestamp = int(time.time_ns())  # nanoseconds
    price = random.uniform(90.0, 110.0)  # random price
    volume = random.randint(1, 1000)  # random volume
    return struct.pack("!qdI", timestamp, price, volume)
    # ! = network byte order (big-endian)
    # q = long (8 bytes), d = double (8 bytes), I = unsigned int (4 bytes)

def main():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind((HOST, PORT))
        s.listen(1)
        print(f"Server listening on {HOST}:{PORT}")

        conn, addr = s.accept()
        with conn:
            print(f"Connected by {addr}")
            for _ in range(1_000_000):  # send 1 million messages
                msg = generate_market_data()
                conn.sendall(msg)
                # Optional: slow down to simulate real feed
                # time.sleep(0.0001)  # 100 microseconds
            print("Finished sending data")

if __name__ == "__main__":
    main()