import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonArray;

import java.lang.reflect.InvocationTargetException;


// Represents a set of multiple test cases
// Contains all the information derivable from a tests.json file
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
      this.parameterTypes[i] = TestSuite.getJavaType(jsonParameterTypes.get(i).asString());
    }

    // Record resultType
    this.resultType = TestSuite.getJavaType(fullJson.get("resultType").asString());

    // Populate this.cases
    JsonArray jsonCases = fullJson.get("testCases").asArray();
    this.cases = new TestCase[jsonCases.size()];
    for (int i = 0; i < this.cases.length; i++) {
      this.cases[i] = new TestCase(jsonCases.get(i).asObject(), this);
    }
  }

  public String toString() {
    String out = "["; // Beginning
    for (TestCase tc : this.cases) out += tc.toString() + ", "; // Add all test cases, each followed by ", "
    out = out.substring(0, out.length() - 2); // Cut out last ", "
    out += "]"; // Add closing bracket

    return out;
  }

  // Get a Class given the string that represents it
  public static Class<?> getJavaType(String str) {
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

  // Run all test cases and return a boolean[] containing the result of each (pass/fail)
  public boolean[] run() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    boolean[] results = new boolean[this.cases.length];

    for (int i = 0; i < this.cases.length; i++) {
      results[i] = this.cases[i].run();
    }

    return results;
  }
}
