import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PingClient
{
    final static String CRLF = "\r\n"; 
    public static void main(String[] args) throws Exception
    {
        if (args.length != 2) {
            System.out.println("Required arguments: host port");
            System.out.println("Example run might look like: java PingClient 127.0.0.1 6790");
            return;
        }
        InetAddress host = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);
        DatagramSocket socket = new DatagramSocket(port);
        int sequence_number = 0;
        for(int i = 0; i < 10; i++) {
            Date date = new Date();
            String message = "Ping " + i + " " + date.getTime() + CRLF;
            byte[] buf = new byte[1024];
            buf = message.getBytes();
            DatagramPacket ping = new DatagramPacket(buf, buf.length, host, port);
            socket.send(ping);
            try {
                socket.setSoTimeout(1000);
                DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
                socket.receive(response);
                printData(response, i);
            }
            catch (IOException e) {
                System.out.println("Packet #" + sequence_number + " has timed out");
            }
        }
    }

    private static void printData(DatagramPacket request, int sequence_number) throws Exception {
        byte[] buf = request.getData();
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
	    InputStreamReader isr = new InputStreamReader(bais);
	    BufferedReader br = new BufferedReader(isr);
        System.out.println("Received from " + request.getAddress().getHostAddress() + ": " + br.readLine() 
        + " Sequence Number: " + sequence_number);
	}
}