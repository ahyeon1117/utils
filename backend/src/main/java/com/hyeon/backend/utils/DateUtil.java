package com.hyeon.backend.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DateUtil {

  private static final DateTimeFormatter yMdHmInputFormatter = DateTimeFormatter.ofPattern(
    "yyyyMMddHHmm"
  );
  private static final DateTimeFormatter MdEHHmm = DateTimeFormatter.ofPattern(
    "M/d (E), HH:mm"
  );
  // private static final DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern(
  //   "HH:mm"
  // );

  private static final Locale koreanLocale = new Locale("ko");

  public static String converMdEHHmmDate(String input) {
    LocalDateTime dateTime = LocalDateTime.parse(input, yMdHmInputFormatter);

    return dateTime.format(MdEHHmm.withLocale(koreanLocale));
  }

  public static String getCurrentTime() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    return now.format(formatter);
  }

  public static String getDifferenceTime(String startDt, String endDt) {
    LocalDateTime startTime = LocalDateTime.parse(startDt, yMdHmInputFormatter);
    LocalDateTime endTime = LocalDateTime.parse(endDt, yMdHmInputFormatter);

    double calculateMinutes =
      calculateMinutesDifference(startTime, endTime) / 60;
    double hoursDifference = Math.ceil(calculateMinutes); // 분을 시간으로 변환

    return String.valueOf((int) hoursDifference);
  }

  private static double calculateMinutesDifference(
    LocalDateTime dateTime1,
    LocalDateTime dateTime2
  ) {
    return ChronoUnit.MINUTES.between(dateTime1, dateTime2);
  }
}
