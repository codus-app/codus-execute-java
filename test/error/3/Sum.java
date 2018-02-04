public class Sum {

  // Errors at runtime because Integer.parseInt can't handle arbitrary strings
  public int main(int a, int b) {
    return Integer.parseInt("this is not an integer");
  }

}
