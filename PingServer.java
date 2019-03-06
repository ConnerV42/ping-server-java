import java.io.*;
import java.net.*;
import java.util.*;

public class PingServer
{
   public static void main(String[] args) throws Exception
   {
      if (args.length != 1) {
         System.out.println("Required arguments: port");
         return;
      }
      int port = Integer.parseInt(args[0]);

      // DatagramSocket receives and sends UDP packets through specified port
      DatagramSocket socket = new DatagramSocket(port);

      while (true) {
         // Create a datagram packet to hold incoming UDP packet.
         DatagramPacket request = new DatagramPacket(new byte[1024], 1024);

         // Block until the host receives a UDP packet.
         socket.receive(request);
         
         // Print the recieved data.
         printData(request);

         // Send reply.
         InetAddress clientHost = request.getAddress();
         int clientPort = request.getPort();
         byte[] buf = request.getData();
         DatagramPacket reply = new DatagramPacket(buf, buf.length, clientHost, clientPort);
         socket.send(reply);

         System.out.println("Reply sent.");
      }
   }
 
   private static void printData(DatagramPacket request) throws Exception
   {
      byte[] buf = request.getData();

      // Wrap bytes in byte array input stream, for reading data
      // as stream of bytes
      ByteArrayInputStream bais = new ByteArrayInputStream(buf);

      // Wrap the byte array input stream in an input stream reader,
      // so you can read the data as a stream of characters.
      InputStreamReader isr = new InputStreamReader(bais);

      // Wrap the input stream reader in a buffered reader,
      // so you can read the character data as a sequence of chars
      // terminated by any combination of \r and \n. (line)
      BufferedReader br = new BufferedReader(isr);

      System.out.println("Received from " + request.getAddress().getHostAddress() + ": " + br.readLine());
   }
}