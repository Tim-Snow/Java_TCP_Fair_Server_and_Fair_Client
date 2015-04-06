package common;

import java.io.*;
import java.net.*;

/**
 * Datagram writer
 */
public class NetDGWriter implements NetStringWriter
{
  private DatagramSocket socket  = null;
  private InetAddress    address = null;
  private String         host    = "";
  private int            port    = 0;

  /**
   * Constructor
   * @param aPort port number
   * @param aHost Address of destination
   */
  public NetDGWriter(int aPort, String aHost) throws IOException
  {
    port = aPort;
    host = aHost;
    DEBUG.trace( "NetDGWriter: Port [%5d] Host [%s]", port, host );

    socket  = new DatagramSocket();
    address = InetAddress.getByName(host);
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
   * Write a string using a datagram, assumed that the string will fit in a single datagram
   * @param message String to be written
   * @return result of operation
   */
  public boolean put( String message )
  {
    try
    {
      DEBUG.trace("Writing DG: Port [%5d] <%s>", port, message );
      byte[] buf = message.getBytes();
      // Note port number in packet
      DatagramPacket packet =
        new DatagramPacket(buf, buf.length, address, port);
      socket.send(packet);
      return true;
    } catch ( Exception e )
    {
      DEBUG.trace("NetDGReader.close %s", e.getMessage() );
    }
    return false;
  }
}

