// JSON
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
// Reflection
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;


// Represents a single test case that can pass or fail
class TestCase {
  TestSuite suite;
  public JsonArray parameters;
  public JsonValue expectedResult;

  public TestCase(JsonObject jsonCase, TestSuite suite) {
    this.suite = suite;
    this.parameters = jsonCase.get("parameters").asArray();
    this.expectedResult = jsonCase.get("result");
  }

  public String toString() {
    return "TestCase " + this.parameters.toString() + " -> " + this.expectedResult.toString();
  }

  // Return the plain-java representation of a JsonValue
  public static Object getJavaObject(JsonValue value) {
    // Basics
    if (value.isString()) return value.asString();
    if (value.isBoolean()) return value.asBoolean();
    if (value.isNull()) return null;
    // Numbers could be int or double
    if (value.isNumber()) {
      if (value.asDouble() == (int) value.asDouble()) return value.asInt();
      else return value.asDouble();
    }
    // Can't deserialize JSON object
    if (value.isObject()) return value.asObject();
    // Arrays require recursion
    if (value.isArray()) {
      JsonArray arr = value.asArray();
      Object[] out = new Object[arr.size()];
      for (int i = 0; i < arr.size(); i++) out[i] = getJavaObject(arr.get(i));
      return out;
    }
    return null;
  }

  // Perform this test and return either true or false to indicate success
  public boolean run() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    // Get the run method
    Class<Solution> sclass = Solution.class;
    Method runMethod = sclass.getMethod("main", this.suite.parameterTypes);
    // Create an instance of Solution class being tested
    Solution instance = new Solution();
    // parameters to be passed as plain Java objects
    Object[] params = (Object[]) TestCase.getJavaObject(this.parameters);
    // Call method
    Object result = runMethod.invoke(instance, params);
    // Compare method to
    return result == TestCase.getJavaObject(this.expectedResult);
  }
}
