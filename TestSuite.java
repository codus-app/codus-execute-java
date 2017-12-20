import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonArray;


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
