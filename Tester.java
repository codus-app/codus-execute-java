// File IO
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
// JSON
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;


// Represents a set of multiple test cases
class TestSuite {
  public TestCase[] cases;
  public TestSuite(String json) {
    JsonArray jsonCases = Json.parse(json).asObject().get("testCases").asArray();
    int numTests = jsonCases.values().size();
    this.cases = new TestCase[numTests];

    for (int i = 0; i < numTests; i++) {
      this.cases[i] = new TestCase(jsonCases.get(i).asObject());
    }
  }

  public String toString() {
    String out = "["; // Beginning
    for (TestCase tc : this.cases) out += tc.toString() + ", "; // Add all test cases, each followed by ", "
    out = out.substring(0, out.length() - 2); // Cut out last ", "
    out += "]"; // Add closing bracket

    return out;
  }
}


// Represents a single test case that can pass or fail
class TestCase {
  public JsonArray parameters;
  public JsonValue result;

  public TestCase(JsonObject jsonCase) {
    this.parameters = jsonCase.get("parameters").asArray();
    this.result = jsonCase.get("result");
  }

  public String toString() {
    return "TestCase " + this.parameters.toString() + " -> " + this.result.toString();
  }
}

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
    String json = Tester.readFile("./test/tests.json");
    System.out.println("tests.json --------------------------------------------------------------\n");
    System.out.println(json);
    System.out.println("-------------------------------------------------------------------------");
    TestSuite suite1 = new TestSuite(json);
    System.out.println(suite1);
  }
}
