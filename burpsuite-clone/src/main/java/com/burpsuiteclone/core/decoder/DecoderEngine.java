package com.burpsuiteclone.core.decoder;

/**
 * Decoder — encoding/decoding utilities.
 */
public interface DecoderEngine {
    String base64Encode(String input);
    String base64Decode(String input);
    String urlEncode(String input);
    String urlDecode(String input);
    String htmlEncode(String input);
    String htmlDecode(String input);
    String hexEncode(String input);
    String hexDecode(String input);
    byte[] gzipCompress(byte[] input);
    byte[] gzipDecompress(byte[] input);
    String smartDecode(String input);
}
