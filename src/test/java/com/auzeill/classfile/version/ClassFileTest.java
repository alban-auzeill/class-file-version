package com.auzeill.classfile.version;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassFileTest {

  @Test
  void magic_number() {
    assertFalse(ClassFile.matchesMagicNumber(new byte[] {}));
    assertFalse(ClassFile.matchesMagicNumber(new byte[] {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0x05}));
    assertFalse(ClassFile.matchesMagicNumber(new byte[] {(byte) 0x0A, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE, (byte) 0x00}));
    assertTrue(ClassFile.matchesMagicNumber(new byte[] {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE}));
    assertTrue(ClassFile.matchesMagicNumber(new byte[] {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE, (byte) 0x00}));
  }

  @Test
  void version() {
    assertEquals(new Version(55, 0), ClassFile.version(new byte[] {0, 0, 0, 0, 0, 0, 0, 55, 0, 0, 0}));
    assertEquals(new Version(45, 3), ClassFile.version(new byte[] {0, 0, 0, 0, 0, 3, 0, 45, 0, 0, 0}));
    assertEquals(new Version(39030,4660), ClassFile.version(new byte[] {0, 0, 0, 0, 0x12, 0x34, (byte) 0x98, 0x76, 0, 0, 0}));
    assertEquals(new Version(65535, 65535), ClassFile.version(new byte[] {0, 0, 0, 0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0, 0, 0}));
  }

  @Test
  void overwrite_version() {
    byte[] data = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    Version v45_3 = new Version(45, 3);
    Version v39030_4660 = new Version(39030,4660);
    ClassFile.overwriteVersion(data, v45_3);
    assertEquals(v45_3, ClassFile.version(data));
    ClassFile.overwriteVersion(data, v39030_4660);
    assertEquals(v39030_4660, ClassFile.version(data));
  }

  @Test
  void read_header() throws IOException {
    Path sampleClassFile = Paths.get("src", "test", "resources", "Sample.class");
    byte[] data = ClassFile.readHeader(sampleClassFile);
    assertEquals(ClassFile.MIN_HEADER_SIZE, data.length);
    assertTrue(ClassFile.matchesMagicNumber(data));
    assertEquals("55.0 (Java 11)", ClassFile.version(data).toString());
  }

}
