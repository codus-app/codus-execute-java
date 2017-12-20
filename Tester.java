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
  public Class<?>[] parameterTypes;
  public Class<?> resultType;

  public TestSuite(String json) {
    // Entire JSON object
    JsonObject fullJson = Json.parse(json).asObject();

    // Populate parameterTypes
    JsonArray jsonParameterTypes = fullJson.get("parameterTypes").asArray();
    this.parameterTypes = new Class<?>[jsonParameterTypes.size()];
    for (int i = 0; i < this.parameterTypes.length; i++) {
      this.parameterTypes[i] = TestSuite.javaType(jsonParameterTypes.get(i).asString());
    }

    // Record resultType
    this.resultType = TestSuite.javaType(fullJson.get("resultType").asString());

    // Populate this.cases
    JsonArray jsonCases = fullJson.get("testCases").asArray();
    this.cases = new TestCase[jsonCases.size()];
    for (int i = 0; i < this.cases.length; i++) {
      this.cases[i] = new TestCase(jsonCases.get(i).asObject());
    }
  }

  // Get a Class given the string that represents it
  static Class<?> javaType(String str) {
    switch (str) {
      // Basics
      case "Integer": return Integer.TYPE;
      case "int": return Integer.TYPE;
      case "Double": return Double.TYPE;
      case "double": return Double.TYPE;
      case "String": return String.class;
      case "Boolean": return Boolean.TYPE;
      case "boolean": return Boolean.TYPE;
      // Array forms
      case "Integer[]": return Integer[].class;
      case "int[]": return int[].class;
      case "Double[]": return Double[].class;
      case "double[]": return double[].class;
      case "String[]": return String[].class;
      case "Boolean[]": return Boolean[].class;
      case "boolean[]": return boolean[].class;
    }
    return null;
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
