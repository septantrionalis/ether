package org.tdod.ether.output;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.tdod.ether.ta.output.GameOutput;

/**
 * A mock implementation of the output.  This is used for testing.
 * @author Ron Kinney
 */
public class MockOutput implements GameOutput {

   //private static Log _log = LogFactory.getLog(MockOutput.class);

   private StringBuffer _buffer = new StringBuffer();

   //**********
   // Methods overwritten from GameOutput.
   //**********

   /**
    * Prints the specified output in color with a carriage return.
    * @param s the string to output.
    */
   public void println(String s) {
      _buffer.append(s);
      //_log.info(s);
   }

   /**
    * Prints the specified output in color with no carriage return.
    * @param s the string to output.
    */
   public void print(String s) {
      _buffer.append(s);
      //_log.info(s);
   }

   /**
    * Prints the specified output without color with a carriage return.
    * @param s the string to output.
    */
   public void printlnWithoutColor(String s) {
      _buffer.append(s);
      //_log.info(s);
   }

   /**
    * Prints the specified output without color with no carriage return.
    * @param s the string to output.
    */
   public void printWithoutColor(String s) {
      _buffer.append(s);
      //_log.info(s);
   }

   //**********
   // Public methods
   //**********

   /**
    * Clears the internal buffer.
    */
   public void clearBuffer() {
      _buffer = new StringBuffer();
   }

   /**
    * Gets the internal buffer.
    * @return the internal buffer.
    */
   public String getBuffer() {
      return _buffer.toString();
   }

}
