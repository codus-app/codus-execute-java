import java.lang.reflect.InvocationTargetException;


// Represents a set of multiple test cases
// Contains all the information derivable from a tests.json file
class TestSuite {
  public TestCase[] cases;
  public Class<?>[] parameterTypes;
  public Class<?> resultType;

  public TestSuite(TestCase[] testCases, Class<?>[] parameterTypes, Class<?> resultType) {
    this.cases = testCases;
    this.parameterTypes = parameterTypes;
    this.resultType = resultType;
    // Register this as the suite for each test case
    for (TestCase c : this.cases) {
      c.suite = this;
    }
  }

  public String toString() {
    String out = "["; // Beginning
    for (TestCase tc : this.cases) out += tc.toString() + ", "; // Add all test cases, each followed by ", "
    out = out.substring(0, out.length() - 2); // Cut out last ", "
    out += "]"; // Add closing bracket

    return out;
  }

  // Run all test cases and return a boolean[] containing the result of each (pass/fail)
  public TestResult[] run() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    TestResult[] results = new TestResult[this.cases.length];

    for (int i = 0; i < this.cases.length; i++) {
      results[i] = this.cases[i].run();
    }

    return results;
  }
}
