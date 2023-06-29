package com.auzeill.classfile.version;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {
  static final Path SAMPLE_CLASS_FILE = Paths.get("src", "test", "resources", "Sample.class");
  static final String NL = System.lineSeparator();
  final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
  final PrintStream out = new PrintStream(outStream, true, UTF_8);

  @Test
  void print_version() {
    Main.main(new String[] {SAMPLE_CLASS_FILE.toString()}, out);
    assertEquals("55.0 (Java 11)" + NL, outStream.toString(UTF_8));
  }

  @Test
  void not_a_class_file() {
    Main.main(new String[] {"-p", "build.gradle.kts"}, out);
    assertEquals("build.gradle.kts [ERROR, not a class file]" + NL, outStream.toString(UTF_8));
  }

  @Test
  void invalid_file() {
    Main.main(new String[] {"-p", "invalid-file.class"}, out);
    assertEquals("invalid-file.class [NoSuchFileException: invalid-file.class]" + NL, outStream.toString(UTF_8));
  }

  @Test
  void print_prefixed_version() {
    Main.main(new String[] {"-p", SAMPLE_CLASS_FILE.toString()}, out);
    assertEquals(SAMPLE_CLASS_FILE + " 55.0 (Java 11)" + NL, outStream.toString(UTF_8));
  }

  @Test
  void print_usage() {
    Main.main(new String[] {}, out);
    assertTrue(outStream.toString(UTF_8).startsWith("Usage:"));
  }

  @Test
  void print_nothing() {
    Main.main(new String[] {"-p"});
    Main.main(new String[] {"-p"}, out);
    assertEquals("", outStream.toString(UTF_8));
  }

  @Test
  void useful_overwrite_version(@TempDir Path tempDir) throws IOException {
    Path classFile = tempDir.resolve("Tmp.class");
    Files.write(classFile, Files.readAllBytes(SAMPLE_CLASS_FILE));
    Main.main(new String[] {"--limit-version=52", classFile.toString()}, out);
    assertEquals("55.0 (Java 11)" + NL +
      "  change version 55.0 (Java 11) => 52.0 (Java 8)" + NL, outStream.toString(UTF_8));
    outStream.reset();
    Main.main(new String[] {classFile.toString()}, out);
    assertEquals("52.0 (Java 8)" + NL, outStream.toString(UTF_8));
  }

  @Test
  void useless_overwrite_version(@TempDir Path tempDir) throws IOException {
    Path classFile = tempDir.resolve("Tmp.class");
    Files.write(classFile, Files.readAllBytes(SAMPLE_CLASS_FILE));
    Main.main(new String[] {"--limit-version=61", classFile.toString()}, out);
    assertEquals("55.0 (Java 11)" + NL, outStream.toString(UTF_8));
  }

}
