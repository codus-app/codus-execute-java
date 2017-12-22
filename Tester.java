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
import com.eclipsesource.json.WriterConfig;

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

  // Initializes a TestSuite based on the information in a file, then runs it and prints results as JSON
  public static void main(String[] args) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    // Read + parse JSON from file
    String jsonString = Tester.readFile("./tests.json");
    JsonObject fullJson = Json.parse(jsonString).asObject();

    // Parse parameter types from JSON into an array of Classes
    JsonArray jsonParameterTypes = fullJson.get("parameterTypes").asArray();
    Object[] parameterTypeNames = (Object[]) JsonInterpret.toObject(jsonParameterTypes);
    Class<?>[] parameterTypes = new Class<?>[parameterTypeNames.length];
    for (int i = 0; i < parameterTypes.length; i++) {
      parameterTypes[i] = JsonInterpret.getJavaType((String) parameterTypeNames[i]);
    }

    // Extract expected result type from JSON to a Class
    Class<?> resultType = JsonInterpret.getJavaType(fullJson.get("resultType").asString());

    // Build an array of TestCases
    JsonArray jsonTestCases = fullJson.get("testCases").asArray(); // JSON representation
    TestCase[] testCases = new TestCase[jsonTestCases.size()];     // Array to be filled
    for (int i = 0; i < testCases.length; i++) {
      // Get JSON representation of test case
      JsonObject jsonTestCase = jsonTestCases.get(i).asObject();
      // Extract parameters as array of Java objects
      Object[] parameters = (Object[]) JsonInterpret.toObject(jsonTestCase.get("parameters").asArray());
      // Extract expected result as Java object
      Object expectedResult = JsonInterpret.toObject(jsonTestCase.get("result"));
      // Create the instance of the TestCase class
      testCases[i] = new TestCase(parameters, expectedResult);
    }

    // Initialize TestSuite
    TestSuite suite = new TestSuite(testCases, parameterTypes, resultType);

    // Run tests
    boolean[] results = suite.run();

    // Print as JSON to console
    System.out.println(Json.array(results).toString(WriterConfig.PRETTY_PRINT));
  }
}
