// File IO
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.IOException;


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

  public static void main(String[] args) throws IOException {
    String json = Tester.readFile("./tests.json");
    System.out.println("tests.json --------------------------------------------------------------\n");
    System.out.println(json);
    System.out.println("-------------------------------------------------------------------------");
    TestSuite suite1 = new TestSuite(json);
    System.out.println(suite1);
  }
}
