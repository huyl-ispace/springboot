package vn.huyl.service.auth.util;

import lombok.Getter;

public class ConstantUtil {

    public static final String PREFIX_ROLE = "ROLE_";

    public static final String CLAIM_REALM_ACCESS = "realm_access";

    public static final String CLAIM_RESOURCE_ACCESS = "resource_access";

    public static final String CLAIM_ROLES = "roles";

    public static final String NOT_EMPTY = "bắt buộc nhập";

    public static final String INVALID = "không hợp lệ";

    @Getter
    public enum ErrorMessage {
        AUTHENTICATION_FAILURE("Lỗi xác thực."),
        ACCESS_DENIED("Không có quyền truy cập."),
        MALFORMED_JSON("Yêu cầu JSON không đúng định dạng."),
        INTERNAL_SEVER_ERROR("Lỗi không xác định."),
        INVALID_REQUEST("Tham số không hợp lệ: '%s'."),
        INVALID_JSON("Định dạng JSON không hợp lệ: %s."),
        TYPE_MISMATCH("Trường '%s' có kiểu dữ liệu không hợp lệ. " +
                "Hệ thống mong muốn kiểu '%s' nhưng nhận được kiểu '%s'."),
        HTTP_METHOD("Phương thức HTTP '%s' không được hỗ trợ."),
        EXISTED("Trường '%s' có giá trị đã tồn tại."),
        NOT_EXISTED("Trường '%s' có giá trị không tồn tại."),
        NOT_FOUND_URL("Đường dẫn '%s' không thể tìm thấy."),
        ARRAY_NOT_EMPTY("Danh sách '%s' không thể rỗng."),
        SUCCESS("Thành công.");

        private final String value;

        ErrorMessage(String value) {
            this.value = value;
        }
    }

    @Getter
    public enum DateFormat {
        VN_01("dd/MM/yyyy HH:mm:ss");

        private final String value;

        DateFormat(String value) {
            this.value = value;
        }
    }
}
