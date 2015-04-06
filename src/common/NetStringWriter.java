package common;

/**
 * Implemented by classes that can write Strings
 *  sent from across the network using TCP, UDP or MultiCast
 */
public interface NetStringWriter
{
  public boolean put( String message );
  public void close();
}
