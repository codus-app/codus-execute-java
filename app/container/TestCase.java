import java.lang.reflect.Method;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.invoke.WrongMethodTypeException;


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

  // Checks if two Objects (which must be arrays) are equal
  public boolean arraysEqual(Object a, Object b) {
    if (a.getClass().isArray() && b.getClass().isArray()) {
      if (Array.getLength(a) != Array.getLength(b)) return false;
      for (int i=0; i < Array.getLength(a); i++)
        if (!Array.get(a, i).equals(Array.get(b, i))) return false;
      return true;
    }
    return false;
  }

  // Perform this test and return either true or false to indicate success
  public TestResult run() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    // Get the run method
    Class<Solution> sclass = Solution.class;
    Method runMethod = sclass.getMethod("main", this.suite.parameterTypes);
    // If the method return type is wrong, throw
    if (runMethod.getReturnType() != this.suite.resultType) throw new WrongMethodTypeException();
    // Call method on new instance of Solution class being tested
    Solution instance = new Solution();
    Object result = runMethod.invoke(instance, this.parameters);
    // Compare result and expected result
    boolean pass;
    if (!result.getClass().isArray()) pass = result.equals(this.expectedResult); // Non-array equality check
    else pass = arraysEqual(result, this.expectedResult); // Arrays require different equality check

    return new TestResult(result, this.expectedResult, pass);
  }
}
