package common;

import java.io.*;
import java.net.*;

/**
 * Multicast writer
 */
public class NetMCWriter implements NetStringWriter
{
  private MulticastSocket socket = null;
  private InetAddress     group  = null;
  private int             port   = 0;

  /**
   * Constructor
   * @param aPort port number
   * @param mca Multicast Address of host
   */
  public NetMCWriter(int aPort, String mca) throws IOException
  {
    port = aPort;
    DEBUG.traceA( "NetMCWrite: port [%5d] MCA [%s]", port, mca );
    socket = new MulticastSocket( port );
    group  = InetAddress.getByName( mca );
    socket.setTimeToLive(40);          // Live for 40 hops
  }

  /**
   * Close the stream to the socket
   */
  public void close()
  {
    try
    {
      socket.leaveGroup(group);
      socket.close();
    } catch ( Exception e )
    {
      DEBUG.error("NetTCPReader.close %s", e.getMessage() );
    }
  }

  /**
   * Write a string using Multicast, 
   *  assumed that the string will fit in a single datagram
   * @param message String to be written
   * @return result of operation
   */
  public synchronized boolean put( String message )
  {
    try
    {
      DEBUG.trace("MCWrite: port [%5d] <%s>", port, message );

      byte[] buf = message.getBytes();
      DatagramPacket packet =
        new DatagramPacket(buf, buf.length, group, port);
      socket.send(packet);
      return true;
    } catch ( Exception e )
    {
      DEBUG.error( "NetMCWriter.put() " + e.getMessage() );
      return false;
    }
    
  }
}
