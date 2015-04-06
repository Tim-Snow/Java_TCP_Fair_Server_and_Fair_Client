package common;

import java.io.*;
import java.net.*;

/**
 * Datagram reader
 */
public class NetDGReader implements NetStringReader
{
  private DatagramSocket socket  = null;
  //private InetAddress    address = null;
  private int            port;

  /**
   * Constructor
   * @param aPort port number
   */
  public NetDGReader(int aPort) throws IOException
  {
    port   = aPort;
    DEBUG.trace("NetDGReader port [%5d]", port );
    socket = new DatagramSocket(port);
  }

  /**
   * Close the stream to the socket
   */
  public void close()
  {
    try
    {
      socket.close();
    } catch ( Exception e )
    {
      DEBUG.error("NetDGReader.close %s", e.getMessage() );
    }
  }

  /**
   * Read a string using a datagram, assumed that the string will fit in a single datagram
   * @return string read or null for error
   */
  public String get()
  {
    try
    {
      DEBUG.trace("NetDGReader.get() Waiting port [%5d]", port );
      byte[] buf = new byte[1400];
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet);
  
      return new String( packet.getData(), 0, packet.getLength() );
    } catch ( Exception e )
    {
      DEBUG.trace("NetDGReader.close %s", e.getMessage() );
    }
    return null;
  }
}

