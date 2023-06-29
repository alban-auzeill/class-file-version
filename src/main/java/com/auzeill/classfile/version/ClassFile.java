package com.auzeill.classfile.version;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class ClassFile {

  public static final int MIN_HEADER_SIZE = 8;
  public static final long MAGIC_NUMBER_FOR_CLASS_FILE_FORMAT = 0xCAFEBABEL; // unsigned 4 bytes
  public static final int MINOR_VERSION_INDEX = 4; // unsigned 2 bytes
  public static final int MAJOR_VERSION_INDEX = 6; // unsigned 2 bytes

  private ClassFile() {
    // utility class
  }

  public static byte[] readHeader(Path path) throws IOException {
    try (InputStream in = Files.newInputStream(path)) {
      byte[] header = new byte[MIN_HEADER_SIZE];
      int size = in.read(header);
      return Arrays.copyOf(header, size);
    }
  }

  public static void overwriteVersion(Path path, Version newVersion) throws IOException {
    byte[] data = Files.readAllBytes(path);
    overwriteVersion(data, newVersion);
    Files.write(path, data);
  }

  public static boolean matchesMagicNumber(byte[] header) {
    long magicNumber = (((long) unsigned2Bytes(header, 0)) << 16) | unsigned2Bytes(header, 2);
    return magicNumber == MAGIC_NUMBER_FOR_CLASS_FILE_FORMAT;
  }

  public static Version version(byte[] header) {
    return new Version(
      unsigned2Bytes(header, MAJOR_VERSION_INDEX),
      unsigned2Bytes(header, MINOR_VERSION_INDEX));
  }

  public static void overwriteVersion(byte[] data, Version version) {
    writeUnsigned2Byte(data, MAJOR_VERSION_INDEX, version.major);
    writeUnsigned2Byte(data, MINOR_VERSION_INDEX, version.minor);
  }

  public static int unsigned2Bytes(byte[] header, int index) {
    return (unsignedByte(header, index) << 8) | unsignedByte(header, index + 1);
  }

  public static int unsignedByte(byte[] header, int index) {
    return index < header.length ? Byte.toUnsignedInt(header[index]) : 0;
  }

  public static void writeUnsigned2Byte(byte[] data, int index, int value) {
    writeUnsignedByte(data, index, value >>> 8);
    writeUnsignedByte(data, index + 1, value & 0xff);
  }

  public static void writeUnsignedByte(byte[] data, int index, int value) {
    data[index] = (byte) value;
  }

}
