package common;

/**
 * Implemented by classes that can read Strings
 *  across the network using TCP, UDP or MultiCast
 */
public interface NetStringReader
{
  public String get();
  public void close();
}
