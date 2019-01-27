import java.lang.reflect.Array;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonArray;

public class JsonInterpret {
  // Convert a JsonValue to a native Java object. Type is not passed; we do our best to infer
  public static Object toObject(JsonValue value) {
    // Can't deserialize JSON objects
    if (value.isObject()) return value.asObject();

    // Numbers could be int or double
    if (value.isNumber()) {
      try { return value.asInt(); }
      catch(NumberFormatException e) { return value.asDouble(); }
    }
    // Basics
    if (value.isString()) return value.asString();
    if (value.isBoolean()) return value.asBoolean();
    if (value.isNull()) return null;
    // Arrays require recursion
    if (value.isArray()) {
      JsonArray arr = value.asArray();
      Object[] out = new Object[arr.size()];
      for (int i = 0; i < arr.size(); i++) out[i] = JsonInterpret.toObject(arr.get(i));
      return out;
    }
    // This should never happen
    throw new IllegalArgumentException("JsonValue " + value.toString() + " is not convertible to a java object");
  }

  // Convert a JsonValue to a native Java object. Type is passed, so it's easier.
  public static Object toObject(JsonValue value, Class<?> type) {
    // If type is passed, we have an easier time
    if (!value.isArray()) {
      if (type == Integer.TYPE) return value.asInt();
      if (type == Integer.class) return value.asInt();
      if (type == Double.TYPE) return value.asDouble();
      if (type == Double.class) return value.asDouble();
      if (type == String.class) return value.asString();
      if (type == Boolean.TYPE) return value.asBoolean();
      if (type == Boolean.class) return value.asBoolean();
      else throw new IllegalArgumentException(type.getName() + " is not a supported type for conversion to JSON");
    // Array type: recur with 'type'
    } else {
      JsonArray arr = value.asArray();
      // If the component type is something like int or boolean, convert temporarily to the Object
      // subclass form (Int/Boolean) so that the array can fit generically into an Object[] so that
      // array operations can be performed.
      Class<?> clazz = JsonInterpret.getObjectClass(type.getComponentType());
      Object[] out = (Object[]) Array.newInstance(clazz, arr.size());
      // Populate the array, converting each value to a full-fledged Java object
      for (int i = 0; i < arr.size(); i++) out[i] = JsonInterpret.toObject(arr.get(i), type.getComponentType());

      // If the array was originally an array of primitive values, convert the values back and return
      if (type.getComponentType() == int.class) {
        int[] out2 = new int[out.length];
        for (int i = 0; i < out.length; i++) out2[i] = (int) out[i];
        return out2;
      }
      if (type.getComponentType() == double.class) {
        double[] out2 = new double[out.length];
        for (int i = 0; i < out.length; i++) out2[i] = (double) out[i];
        return out2;
      }
      if (type.getComponentType() == boolean.class) {
        boolean[] out2 = new boolean[out.length];
        for (int i = 0; i < out.length; i++) out2[i] = (boolean) out[i];
        return out2;
      }

      // If it wasn't just return
      return out;
    }
  }

  // Convert a JsonValue that is an array with varying types to a native Java object. Types are passed in an array.
  public static Object toObject(JsonValue value, Class<?>[] types) {
    if (!value.isArray()) throw new IllegalArgumentException("An array of types was passed but the value to convert is not an array.");
    else {
      JsonArray arr = value.asArray();
      if (arr.size() != types.length) throw new IllegalArgumentException("The 'types' array must have the same number of elements as the JSON array to convert.");

      Object[] out = new Object[arr.size()];
      for (int i = 0; i < arr.size(); i++) out[i] = JsonInterpret.toObject(arr.get(i), types[i]);
      return out;
    }
  }

  // Get the object class from a primitive class (e.g. Integer from int)
  public static Class<?> getObjectClass(Class<?> primitiveClass) {
    if (primitiveClass == int.class) return Integer.class;
    if (primitiveClass == double.class) return Double.class;
    if (primitiveClass == boolean.class) return Boolean.class;
    return primitiveClass;
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
    throw new IllegalArgumentException(str + " is not a supported type for conversion from JSON");
  }

  // Convert a Java object to a jsonValue
  public static JsonValue getJsonValue(Object value, Class<?> type) {
    // Base case - value is easily convertible
    if (!type.isArray()) {
      if (type == Integer.TYPE) return Json.value((int) value);
      if (type == Double.TYPE) return Json.value((double) value);
      if (type == String.class) return Json.value((String) value);
      if (type == Boolean.TYPE) return Json.value((boolean) value);
      else throw new IllegalArgumentException(type.getName() + " is not a supported type for conversion to JSON");
    }
    // Type is an array
    Class<?> containedType = type.getComponentType();
    JsonArray out = Json.array();
    for (int i = 0; i < Array.getLength(value); i++) {
      out.add(getJsonValue(Array.get(value, i), containedType)); // Recur
    }
    return out;
  }
}
