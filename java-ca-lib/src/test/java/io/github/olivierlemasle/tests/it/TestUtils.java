package io.github.olivierlemasle.tests.it;

import java.io.File;
import java.io.IOException;

public class TestUtils {

  static boolean isWindows() {
    final String osName = System.getProperty("os.name");
    if (osName == null)
      return false;
    return osName.startsWith("Windows");
  }

  static boolean isWindowsAdministrator() {
    try {
      String programFiles = System.getenv("ProgramFiles");
      if (programFiles == null) {
        programFiles = "C:\\Program Files";
      }
      final File temp = new File(programFiles, "foo.txt");
      if (temp.createNewFile()) {
        temp.delete();
        return true;
      } else
        return false;
    } catch (final IOException e) {
      return false;
    }
  }

  static boolean opensslExists() {
    try {
      return 0 == new ProcessBuilder("openssl", "help").start().waitFor();
    } catch (InterruptedException | IOException e) {
      return false;
    }
  }

}
