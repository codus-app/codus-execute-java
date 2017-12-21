import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;


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
}
