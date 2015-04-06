package common;

/**
 * Print information about the running program
 *  @author Mike Smith University of Brighton
 *  @version 1.0
 */

public class DEBUG
{
  private static boolean debug = false;

  /**
   * Set true/false to print debugging information
   *  @param state Debugging true false
   *  @return debugging previous state true/ false
   */
  public static synchronized boolean set( boolean state )
  {
    boolean oldState = debug;
    debug = state;
    return oldState;
  }

  /**
   * Display text for debugging purposes
   *  @param fmt  The same as printf etc
   *  @param params parameters interpolated into the string
   */
  public static void trace( String fmt, Object... params )
  {
    if ( debug )
    {
      synchronized( DEBUG.class )
      {
        System.out.printf( fmt + "\n", params );
      }
    }
  }

  /**
   * Always trace
   *  @param fmt The same as printf etc
   *  @param fmt  The same as printf etc
   *  @param params parameters interpolated into the string
   */
  public static void traceA( String fmt, Object... params )
  {
    synchronized( DEBUG.class )
    {
      System.out.printf( fmt + "\n", params );
    }
  }

  /**
   * Display a fatal message if the assertion fails (is false)
   *  @param ok assertion is (ok) true
   *  @param fmt The same as printf etc
   *  @param fmt  The same as printf etc
   *  @param params parameters interpolated into the string
   */
  public static void assertTrue( boolean ok, String fmt, Object... params )
  {
    if ( ! ok )
    {
      error( "Assert - " + fmt, params );
    }
  }

  /**
   * Display a fatal message
   *  @param fmt The same as printf etc
   *  @param params parameters interpolated into the string
   */
  public static synchronized void error(String fmt, Object... params )
  {
    System.out.printf( "ERROR: " + fmt + "\n", params );
    System.exit(-1);
  }

}
