package com.auzeill.classfile.version;

import java.util.Objects;

public class Version implements Comparable<Version> {
  public final int major;
  public final int minor;

  public Version(int major, int minor) {
    this.major = major;
    this.minor = minor;
  }

  public static Version from(String version) throws NumberFormatException {
    int sep = version.indexOf('.');
    if (sep == -1) {
      return new Version(Integer.parseInt(version), 0);
    }
    return new Version(
      Integer.parseInt(version.substring(0, sep)),
      Integer.parseInt(version.substring(sep + 1)));
  }

  @Override
  public String toString() {
    String suffix = "";
    if (major == 45 && minor == 0) {
      suffix = " (Java 1.0)";
    } else if (major == 45 && minor == 3) {
      suffix = " (Java 1.1)";
    } else if ((major >= 46 && major <= 48) && minor == 0) {
      suffix = " (Java 1." + (major - 44) + ")";
    } else if (major >= 49 && minor == 0) {
      suffix = " (Java " + (major - 44) + ")";
    }
    return major + "." + minor + suffix;
  }

  @Override
  public int compareTo(Version other) {
    int compare = Integer.compare(this.major, other.major);
    if (compare == 0) {
      compare = Integer.compare(this.minor, other.minor);
    }
    return compare;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Version)) {
      return false;
    }
    Version version = (Version) o;
    return major == version.major && minor == version.minor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(major, minor);
  }
}
