package com.auzeill.classfile.version;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

  private static final String PREFIX_WITH_PATH = "-p";
  private static final String LIMIT_VERSION_PREFIX = "--limit-version=";

  public static void main(String[] args) {
    main(args, System.out);
  }

  public static void main(String[] args, PrintStream out) {
    if (args.length == 0) {
      out.println("Usage:");
      out.println("  Display class file version:");
      out.println("    java -jar class-file-version.jar <options> <class-file-path> ...");
      out.println("  Options:");
      out.println("    -p");
      out.println("       Prefix the result with class file path");
      out.println("    --limit-version=<version>");
      out.println("       Limit the class file version if greater (version format: 55 or 55.0)");
      return;
    }
    boolean prefixWithPath = false;
    Version limitVersion = null;
    for (String arg : args) {
      if (arg.startsWith(LIMIT_VERSION_PREFIX)) {
        limitVersion = Version.from(arg.substring(LIMIT_VERSION_PREFIX.length()));
      } else if (arg.equals(PREFIX_WITH_PATH)) {
        prefixWithPath = true;
      }
    }
    for (String arg : args) {
      if (!arg.startsWith(LIMIT_VERSION_PREFIX) && !arg.equals(PREFIX_WITH_PATH)) {
        execute(arg, limitVersion, prefixWithPath, out);
      }
    }
  }

  private static void execute(String classFilePath, Version limitVersion, boolean prefixWithPath, PrintStream out) {
    if (prefixWithPath) {
      out.print(classFilePath);
      out.print(" ");
    }
    try {
      Path path = Paths.get(classFilePath);
      byte[] header = ClassFile.readHeader(path);
      if (!ClassFile.matchesMagicNumber(header)) {
        out.println("[ERROR, not a class file]");
      } else {
        Version version = ClassFile.version(header);
        out.println(version);
        if (limitVersion != null && version.compareTo(limitVersion) > 0) {
          out.println("  change version " + version + " => " + limitVersion);
          ClassFile.overwriteVersion(path, limitVersion);
        }
      }
    } catch (RuntimeException | IOException e) {
      out.println("[" + e.getClass().getSimpleName() + ": " + e.getMessage() + "]");
    }
  }
}
