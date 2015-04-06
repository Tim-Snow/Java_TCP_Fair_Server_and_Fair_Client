package common;

import java.io.*;
import java.net.Socket;

/**
 * TCP writer
 *  implemented by using ObjectOutputStream
 */
public class NetTCPWriter extends ObjectOutputStream 
                          implements NetStringWriter
{
  /**
   * Constructor
   * @param s Socket endpoint for communication
   */
  public NetTCPWriter( Socket s ) throws IOException
  {
    super( s.getOutputStream() );
    DEBUG.traceA( "new NetTCPWriter()" );
    s.setTcpNoDelay(true);              // Send data immediately
  }

  /**
   * Write a string to a socket
   * @param data The string to be written
   * @return result of writing
   */
  public synchronized boolean put( String data )
  {
    try
    {
      writeObject( data );                    // Write object
      flush();                                // Flush
      return true;                            // Ok
    }
    catch ( IOException e )
    {
      DEBUG.trace("NetObjectWriter.get %s", e.getMessage());
      return false;                           // Failed write
    }
  }
  
  /**
   * Close the stream to the socket
   */
  public void close()
  {
    try
    {
      super.close();
    } catch ( Exception e )
    {
      DEBUG.error("NetTCPWriter.close %s", e.getMessage() );
    }
  }
}
