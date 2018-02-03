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
  public Class<?> solutionClass;
  public String methodName;

  public TestCase(Object[] parameters, Object expectedResult, Class<?> solutionClass, String methodName) {
    this.parameters = parameters;
    this.expectedResult = expectedResult;
    this.solutionClass = solutionClass;
    this.methodName = methodName;
  }

  public String toString() {
    return "TestCase " + this.parameters.toString() + " -> " + this.expectedResult.toString();
  }

  // Checks if two Objects (which must be arrays) are equal
  public static boolean arraysEqual(Object a, Object b) {
    if (a.getClass().isArray() && b.getClass().isArray()) {
      if (Array.getLength(a) != Array.getLength(b)) return false;
      for (int i=0; i < Array.getLength(a); i++)
        if (!Array.get(a, i).equals(Array.get(b, i))) return false;
      return true;
    }
    return false;
  }

  // Perform this test and return either true or false to indicate success
  public TestResult run() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    // Get the run method
    Method runMethod = this.solutionClass.getMethod(this.methodName, this.suite.parameterTypes);
    // If the method return type is wrong, throw
    if (runMethod.getReturnType() != this.suite.resultType) throw new WrongMethodTypeException();
    // Call method on new instance of solution class being tested
    Object instance = solutionClass.newInstance();
    Object result = runMethod.invoke(instance, this.parameters);

    // Compare result and expected result
    boolean pass;
    if (!result.getClass().isArray()) pass = result.equals(this.expectedResult); // Non-array equality check
    else pass = TestCase.arraysEqual(result, this.expectedResult); // Arrays require different equality check

    return new TestResult(result, this.expectedResult, pass);
  }
}
