package com.google.luna.client.utils;

import java.util.Iterator;
/**
 * Utility functions for encoding and decoding a bit set as a string.
 */
public class BitVectorCodec {

  // Change this if you change how encoding works to avoid reading stale
  // encoded data.
  private static final char kVersion = 'a';

  private static char toBase64(int value) {
    return kBase64.charAt(value);
  }

  private static int fromBase64(char value) {
    if ('a' <= value && value <= 'z') {
      return value - 'a';
    } else if ('A' <= value && value <= 'Z') {
      return (value - 'A') + 26;
    } else if ('0' <= value && value <= '9') {
      return (value - '0') + 52;
    } else if ('-' == value) {
      return 62;
    } else {
      assert '*' == value;
      return 63;
    }
  }

  private static final String kBase64 = "abcdefghijklmnopqrstuvwxyz" +
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-*";

  private static final int kChunkSize = 6;

  /**
   * Encode the given bits as a string.
   */
  public static String encode(Iterable<Integer> bits) {
    StringBuffer buf = new StringBuffer();
    buf.append(kVersion).append(":");
    Iterator<Integer> iter = bits.iterator();
    int cursor = 0;
    int chunk = 0;
    int chunkLength = 0;
    // Run through the bits, chop them into blocks of 6 and encode them
    // as simple characters.
    while (iter.hasNext()) {
      // Grab the next set bit
      int nextSet = iter.next();
      // Scan until we need that bit.
      while (cursor <= nextSet) {
        // Scan until we meet the bit or have a full chunk.
        while (cursor < nextSet && chunkLength < kChunkSize) {
          int dist = nextSet - cursor;
          if (chunkLength + dist >= kChunkSize) {
            // If the next bit is beyond this chunk we just finish the
            // chunk in one go.
            int delta = (kChunkSize - chunkLength);
            chunk <<= delta;
            cursor += delta;
            chunkLength = kChunkSize;
          } else {
            // Otherwise we add the remaining non-set bits and loop
            // around again to grab the next bit.
            chunk <<= dist;
            cursor += dist;
            chunkLength += dist;
          }
        }
        if (chunkLength == kChunkSize) {
          buf.append(toBase64(chunk));
          chunk = 0;
          chunkLength = 0;
        }
        if (cursor == nextSet) {
          chunk = (chunk << 1) | 1;
          chunkLength++;
          cursor++;
        }
      }
    }
    // Finish the current chunk if we've started one.
    if (chunkLength > 0) {
      chunk <<= (kChunkSize - chunkLength);
      buf.append(kBase64.charAt(chunk));
    }
    return buf.toString();
  }


  /**
   * Returns an iterator the yields the true bits of a bit set encoded
   * using #{@link BitVectorCodec#encode(Iterable)}.
   */
  public static Iterable<Integer> decode(String code) {
    if (code.length() < 2 || code.charAt(0) != kVersion || code.charAt(1) != ':')
      return null;
    final String data = code.substring(2);
    FlatBitVector vector = new FlatBitVector(kChunkSize * code.length());
    for (int i = 0; i < data.length(); i++) {
      int chunk = fromBase64(data.charAt(i));
      for (int j = 0; j < kChunkSize; j++) {
        if ((chunk & (1 << (kChunkSize - j - 1))) != 0)
          vector.set(i * kChunkSize + j, true);
      }
    }
    return vector;
  }

}
