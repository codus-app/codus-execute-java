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

  // Perform this test and return either true or false to indicate success
  public boolean run() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    // Get the run method
    Class<Solution> sclass = Solution.class;
    Method runMethod = sclass.getMethod("main", this.suite.parameterTypes);
    // Create an instance of Solution class being tested
    Solution instance = new Solution();
    // parameters to be passed as plain Java objects
    Object[] params = (Object[]) JsonInterpret.toObject(this.parameters);
    // Call method
    Object result = runMethod.invoke(instance, params);
    Object expected = JsonInterpret.toObject(this.expectedResult);

    // Compare method to
    return result.equals(expected);
  }
}
