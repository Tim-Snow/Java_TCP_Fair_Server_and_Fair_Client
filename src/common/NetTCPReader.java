package common;

import java.io.*;
import java.net.Socket;

/**
 * TCP reader
 *  implemented by using ObjectInputStream
 */
public class NetTCPReader extends ObjectInputStream 
                          implements NetStringReader
{
  /**
   * Constructor
   * @param s Socket endpoint for communication
   */
  public NetTCPReader( Socket s ) throws IOException
  {
    super( s.getInputStream() );
    DEBUG.traceA( "new NetTCPReader()" );
  }

  /**
   * Read a string from a socket
   * @return string read or null if error
   */
  public synchronized String get()      //
  {
    try                                 //
    {
      return (String) readObject();     // Return read object
    }
    catch ( Exception e )               // Reading error
    {
      //DEBUG.error("NetObjectReader.get %s", 
      //                    e.getMessage() );
      return null;                      //  On error return null
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
      DEBUG.error("NetTCPReader.close %s", e.getMessage() );
    }
  }
}
