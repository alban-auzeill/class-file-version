package com.auzeill.classfile.version;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VersionTest {

  @Test
  void compare() {
    Version v45_0 = new Version(45, 0);
    Version v45_3 = new Version(45, 3);
    Version v46_0 = new Version(46, 0);
    assertEquals(0, v45_0.compareTo(v45_0));
    assertEquals(0, v45_0.compareTo(new Version(45, 0)));
    assertEquals(-1, v45_0.compareTo(v46_0));
    assertEquals(1, v46_0.compareTo(v45_0));
    assertEquals(1, v45_3.compareTo(v45_0));
    assertEquals(-1, v45_3.compareTo(v46_0));
  }

  @Test
  void equals() {
    Version v45_0 = new Version(45, 0);
    Version v45_3 = new Version(45, 3);
    Version v46_0 = new Version(46, 0);
    assertTrue( v45_0.equals(v45_0));
    assertEquals( new Version(45, 3).hashCode(), v45_3.hashCode());
    assertTrue( v45_3.equals(new Version(45, 3)));
    assertFalse( v45_3.equals(v45_0));
    assertFalse( v45_0.equals(v45_3));
    assertFalse( v45_0.equals(v46_0));
    assertFalse( v45_0.equals(null));
    assertFalse( v45_0.equals(""));
  }

  @Test
  void to_string() {
    assertEquals("42.0", new Version(42, 0).toString());
    assertEquals("42.42", new Version(42, 42).toString());
    assertEquals("45.0 (Java 1.0)", new Version(45, 0).toString());
    assertEquals("45.3 (Java 1.1)", new Version(45, 3).toString());
    assertEquals("45.666", new Version(45, 666).toString());
    assertEquals("46.0 (Java 1.2)", new Version(46, 0).toString());
    assertEquals("47.0 (Java 1.3)", new Version(47, 0).toString());
    assertEquals("47.666", new Version(47, 666).toString());
    assertEquals("48.0 (Java 1.4)", new Version(48, 0).toString());
    assertEquals("49.0 (Java 5)", new Version(49, 0).toString());
    assertEquals("52.0 (Java 8)", new Version(52, 0).toString());
    assertEquals("52.666", new Version(52, 666).toString());
    assertEquals("65.0 (Java 21)", new Version(65, 0).toString());
  }

  @Test
  void from() {
    assertEquals(new Version(55, 0), Version.from("55"));
    assertEquals(new Version(45, 3), Version.from("45.3"));
    assertThrows(NumberFormatException.class, () -> Version.from("foo"));
    assertThrows(NumberFormatException.class, () -> Version.from("foo.3"));
    assertThrows(NumberFormatException.class, () -> Version.from("45.foo"));
  }
}
