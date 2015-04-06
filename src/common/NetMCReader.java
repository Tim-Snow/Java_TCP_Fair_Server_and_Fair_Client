package common;

import java.io.*;
import java.net.*;

/**
 * Multicast writer
 *   IP Range 224.0.0.0 to 239.255.255.255
 */
public class NetMCReader implements NetStringReader
{
  private MulticastSocket socket = null;
  private InetAddress     group  = null;
  private int             port   = 0;

  /**
   * Constructor
   * @param aPort port number
   * @param mca Multicast Address of server
   */
  public NetMCReader(int aPort, String mca ) throws IOException
  {
    port   = aPort;
    DEBUG.traceA("MCRead: C port [%s] MCA [%s]", port, mca );
    socket = new MulticastSocket( port );
    group  = InetAddress.getByName( mca );
    socket.joinGroup(group);
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
      DEBUG.error("NetMCReader.close %s", e.getMessage() );
    }
  }

  /**
   * Read a string from a multicast socket
   * @return string read or null if error
   */
  public synchronized String get()
  {
    try
    {
      DEBUG.trace("MCRead: on port [%d]", port );
      byte[] buf = new byte[1400];
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      socket.receive(packet);

      String m = new String( packet.getData(), 0, packet.getLength() );
      DEBUG.trace("MCRead: Read <%s>", m );
      return m;
     } catch ( Exception e )
     {
       DEBUG.trace("MCRead: Read Failure %s", e.getMessage() );
       return null;
     }
  }
}
