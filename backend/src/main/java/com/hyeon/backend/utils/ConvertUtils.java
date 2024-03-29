package com.hyeon.backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class ConvertUtils {

  public static final ObjectMapper jsonMapper = new ObjectMapper();

  public static String[] stringListToArrStrings(List<String> list) {
    return list.toArray(new String[0]);
  }

  public static int[] integerListTointArr(List<Integer> list) {
    return list.stream().mapToInt(Integer::intValue).toArray();
  }

  public static Integer[] integerListToIntegerArr(List<Integer> list) {
    return list.toArray(new Integer[0]);
  }

  public static <S, T> T map(S source, Class<T> targetClass) {
    return jsonMapper.convertValue(source, targetClass);
  }
}
