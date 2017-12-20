import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;


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
