import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonArray;

public class JsonInterpret {
  // Convert a JsonValue to a native Java object
  public static Object toObject(JsonValue value) {
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
      for (int i = 0; i < arr.size(); i++) out[i] = JsonInterpret.toObject(arr.get(i));
      return out;
    }
    return null;
  }

  // Get a Class given the string that represents it
  public static Class<?> getJavaType(String str) {
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

}
