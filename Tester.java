// File IO
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
// JSON
import com.eclipsesource.json.*;


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
    System.out.println("tests.json --------------------------------------------------------------\n");
    System.out.println(
      Tester.readFile("./test/tests.json")
    );
    System.out.println("-------------------------------------------------------------------------");
  }
}
