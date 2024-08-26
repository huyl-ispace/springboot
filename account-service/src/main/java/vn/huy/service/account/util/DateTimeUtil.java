package vn.huy.service.account.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    private static final DateTimeFormatter formatterDefault = DateTimeFormatter
            .ofPattern(ConstantUtil.DateFormat.VN_01.getValue());

    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, formatterDefault);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatterDefault);
    }

    public static LocalDateTime convertFormatDateTime(LocalDateTime dateTime) {
        String dateTimeStr = formatDateTime(dateTime);
        return LocalDateTime.parse(dateTimeStr, formatterDefault);
    }
}
