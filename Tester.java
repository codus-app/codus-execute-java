// File IO
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
// JSON output
import com.eclipsesource.json.Json;

import java.lang.reflect.InvocationTargetException;

public class Tester {
  // Returns a String with the contents of a file
  static String readFile(String path, Charset encoding) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    return new String(bytes, encoding);
  }

  // Default encoding of UTF-8
  static String readFile(String path) throws IOException {
    return Tester.readFile(path, StandardCharsets.UTF_8);
  }

  public static void main(String[] args) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    // Read file
    String json = Tester.readFile("./tests.json");
    // Make TestSuite from file
    TestSuite suite = new TestSuite(json);
    // Run tests
    boolean[] results = suite.run();
    // Print as JSON to console
    System.out.println(Json.array(results));
  }
}
