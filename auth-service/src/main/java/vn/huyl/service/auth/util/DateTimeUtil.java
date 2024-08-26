package vn.huyl.service.auth.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    private static final DateTimeFormatter formatterDefault = DateTimeFormatter
            .ofPattern(ConstantUtil.DateFormat.VN_01.getValue());

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatterDefault);
    }
}
