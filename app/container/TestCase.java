import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;


// Contains info on a TestCase's result
class TestResult {
  public Object value;
  public Object expected;
  public boolean pass;

  public TestResult(Object value, Object expected, boolean pass) {
    this.value = value;
    this.expected = expected;
    this.pass = pass;
  }
}


// Represents a single test case that can pass or fail
class TestCase {
  TestSuite suite;
  public Object[] parameters;
  public Object expectedResult;

  public TestCase(Object[] parameters, Object expectedResult) {
    this.parameters = parameters;
    this.expectedResult = expectedResult;
  }

  public String toString() {
    return "TestCase " + this.parameters.toString() + " -> " + this.expectedResult.toString();
  }

  // Perform this test and return either true or false to indicate success
  public boolean run() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    // Get the run method
    Class<Solution> sclass = Solution.class;
    Method runMethod = sclass.getMethod("main", this.suite.parameterTypes);
    // Call method on new instance of Solution class being tested
    Solution instance = new Solution();
    Object result = runMethod.invoke(instance, this.parameters);
    // Compare result and expected result
    return result.equals(this.expectedResult);
  }
}
