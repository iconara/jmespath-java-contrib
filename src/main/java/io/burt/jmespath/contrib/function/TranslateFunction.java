package io.burt.jmespath.contrib.function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.burt.jmespath.Adapter;
import io.burt.jmespath.JmesPathType;
import io.burt.jmespath.function.ArgumentConstraints;
import io.burt.jmespath.function.FunctionArgument;

public class TranslateFunction extends SubstringMatchingFunction {
  public TranslateFunction() {
    super(ArgumentConstraints.listOf(3, 3, ArgumentConstraints.typeOf(JmesPathType.STRING)));
  }

  @Override
  protected <T> T callFunction(Adapter<T> runtime, List<FunctionArgument<T>> arguments) {
    String arg = runtime.toString(arguments.get(0).value());
    String map = runtime.toString(arguments.get(1).value());
    String trans = runtime.toString(arguments.get(2).value());

    if (isEmpty(arg)) {
      return runtime.createString("");
    } else {
      return runtime.createString(replaceChars(arg, map, trans));
    }
  }

  protected static String replaceChars(String input, String from, String to) {
    StringBuilder sb = new StringBuilder();
    Map<Character, Character> map = buildTranslationMap(from, to);

    for (int i = 0; i < input.length(); ++i) {
      Character ch = input.charAt(i);
      if (map.containsKey(ch)) {
        Character tr = map.get(ch);
        if (null != tr) {
          sb.append(tr);
        }
      } else {
        sb.append(ch);
      }
    }
    return sb.toString();
  }

  private static Map<Character, Character> buildTranslationMap(String from, String to) {
    Map<Character, Character> map = new HashMap<>();
    for (int i = 0; i < from.length(); ++i) {
      Character ch  = from.charAt(i);
      if (!map.containsKey(ch)) {
        map.put(from.charAt(i), i < to.length() ? to.charAt(i) : null);
      }
    }
    return map;
  }
}
