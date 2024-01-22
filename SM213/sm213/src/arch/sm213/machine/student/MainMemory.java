package arch.sm213.machine.student;

import machine.AbstractMainMemory;

import java.util.Arrays;


/**
 * Main Memory of Simple CPU.
 *
 * Provides an abstraction of main memory (DRAM).
 */

public class MainMemory extends AbstractMainMemory {
  private byte [] mem;
  
  /**
   * Allocate memory.
   * @param byteCapacity size of memory in bytes.
   */
  public MainMemory (int byteCapacity) {
    mem = new byte [byteCapacity];
  }
  
  /**
   * Determine whether an address is aligned to specified length.
   * @param address memory address.
   * @param length byte length.
   * @return true iff address is aligned to length.
   */
  @Override public boolean isAccessAligned (int address, int length) {
      return (address % length) == 0;
  }
  
  /**
   * Convert an sequence of four bytes into a Big Endian integer.
   * @param byteAtAddrPlus0 value of byte with lowest memory address (base address).
   * @param byteAtAddrPlus1 value of byte at base address plus 1.
   * @param byteAtAddrPlus2 value of byte at base address plus 2.
   * @param byteAtAddrPlus3 value of byte at base address plus 3 (highest memory address).
   * @return Big Endian integer formed by these four bytes.
   */
  @Override public int bytesToInteger (byte byteAtAddrPlus0, byte byteAtAddrPlus1, byte byteAtAddrPlus2, byte byteAtAddrPlus3) {
    return (byteAtAddrPlus3 & 0xff) +
            ((byteAtAddrPlus2 << 8) & 0xff00) +
            ((byteAtAddrPlus1 << 16) & 0xff0000) +
            ((byteAtAddrPlus0 << 24 ) & 0xff000000);
  }

  // int i = 3   <-------   0x00000003
  // byte j = (byte) i  <------ 0x03


  // 0x01 0xFF 0x03 0x04
  // int i1 = (int) bytes[0]
  // int i2 = (int) bytes[1]  <---- 0xFFFFFFFF
  // ...
  // ...
  //

  // 0x01FF0203    0x01 0xFF 0x02 0x03
  // byte b1 = (byte)

  /**
   * Convert a Big Endian integer into an array of 4 bytes organized by memory address.
   * @param  i an Big Endian integer.
   * @return an array of byte where [0] is value of low-address byte of the number etc.
   */
  @Override public byte[] integerToBytes (int i) {
    byte[] array = new byte[4];

    for (int j = 4; j > 0; j--) {
      array[(4-j)] = (byte) (0xff & (i >> (8 * (j-1))));
    }
    return array;
  }
  
  /**
   * Fetch a sequence of bytes from memory.
   * @param address address of the first byte to fetch.
   * @param length  number of bytes to fetch.
   * @throws InvalidAddressException  if any address in the range address to address+length-1 is invalid.
   * @return an array of byte where [0] is memory value at address, [1] is memory value at address+1 etc.
   */
  @Override public byte[] get (int address, int length) throws InvalidAddressException {
    if ((address < 0) || ((address + length) > length())) {
      throw new InvalidAddressException();
    }
    byte[] array = new byte[length];
    for (int i = 0; i < length; i++) {
      array[i] = mem[address + i];
    }
    return array;
  }
  
  /**
   * Store a sequence of bytes into memory.
   * @param  address                  address of the first byte in memory to receive the specified value.
   * @param  value                    an array of byte values to store in memory at the specified address.
   * @throws InvalidAddressException  if any address in the range address to address+value.length-1 is invalid.
   */
  @Override public void set (int address, byte[] value) throws InvalidAddressException {

    int byteLength = value.length;
    if ((address < 0) || (address + (byteLength)) > length()) {
      throw new InvalidAddressException();
    }
    for (int i = 0; i < (byteLength); i++) {
      mem[i + address] = value[i];
    }
  }
  
  /**
   * Determine the size of memory.
   * @return the number of bytes allocated to this memory.
   */
  @Override public int length () {
    return mem.length;
  }
}
