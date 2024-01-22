import java.util.Arrays;

import static java.lang.System.out;

public class Endianness {

  public static int bigEndianValue(Byte[] mem) {
    int result = 0;
    for (int i = 0; i < 4; i++) {
      result = (result << 8) | (mem[i] & 0xFF); // excluding 0xff would make this so that when the byte is OR'd
      // with the int it will be sign extended as its being converted to an
      // int , so we are converting it and insigning it before with
      // & 0XFF (this is an int)
    }
    return result;
  }

  // bitshifting in other direction results in overflow from right side resulting in loss of data
  // therefore we reverse the index but do the same thing as big endian

  public static int littleEndianValue(Byte[] mem) {
    int result = 0;
    for (int i = 3; i > -1; i--) {
      result = (result << 8) | (mem[i] & 0xFF);
    }


    return result;
  }
  
  public static void main (String[] args) {
    Byte mem[] = new Byte[4];
    try {
      for (int i=0; i<4; i++) {
        mem [i] = Integer.valueOf (args[i], 16) .byteValue();
      }
    } catch (Exception e) {
      out.printf ("usage: java Endianness n0 n1 n2 n3\n");
      out.printf ("where: n0..n3 are byte values in memory at addresses 0..3 respectively, in hex (no 0x).\n");
      return;
    }
  
    int bi = bigEndianValue    (mem);
    int li = littleEndianValue (mem);
    
    out.printf ("Memory Contents\n");
    out.printf ("  Addr   Value\n");
    for (int i=0; i<4; i++)
      out.printf ("  %3d:   0x%-5x\n", i, mem[i]);
    out.printf ("The big    endian integer value at address 0 is %d\n", bi);
    out.printf ("The little endian integer value at address 0 is %d\n", li);
  }
}