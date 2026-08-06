package Utils;

import java.util.List;

/**
 * Lớp tiện ích sinh mã định danh tự động và xử lý chuỗi ký tự tiếng Việt.
 * <p>
 * Lớp này hỗ trợ sinh các mã nghiệp vụ tự động tăng (ví dụ: "DL004", "BT002") dựa trên
 * giá trị số lớn nhất hiện có trong danh sách mã, đồng thời cung cấp hàm loại bỏ dấu tiếng Việt
 * phục vụ cho việc tìm kiếm hoặc tạo username tự động.
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public final class CodeGen {

    /**
     * Khởi tạo riêng biệt ngăn không cho khởi tạo đối tượng tiện ích {@code CodeGen}.
     */
    private CodeGen() {
    }

    /**
     * Sinh mã định danh tiếp theo dựa trên tiền tố, danh sách các mã đang tồn tại và độ rộng định dạng số.
     * <p>
     * Ví dụ: Với tiền tố "DL", danh sách mã ["DL001", "DL002"], độ rộng 3 -> Trả về "DL003".
     * </p>
     *
     * @param prefix        Chuỗi tiền tố của mã (ví dụ: "DL", "BT", "HD", "SAN").
     * @param existingCodes Danh sách các mã hiện đang có trong hệ thống.
     * @param width         Độ rộng phần số nguyên sau tiền tố (số chữ số đệm số 0 phía trước).
     * @return Chuỗi mã định danh mới tiếp theo.
     */
    public static String next(String prefix, List<String> existingCodes, int width) {
        int max = 0;
        // Kiểm tra danh sách mã hiện có không bị null
        if (existingCodes != null) {
            for (String code : existingCodes) {
                // Kiểm tra mã có bắt đầu bằng đúng tiền tố đang xét hay không
                if (code != null && code.startsWith(prefix)) {
                    try {
                        // Tách lấy phần số đằng sau tiền tố và chuyển đổi sang số nguyên
                        int n = Integer.parseInt(code.substring(prefix.length()));
                        // Cập nhật giá trị số lớn nhất tìm thấy
                        if (n > max) max = n;
                    } catch (NumberFormatException ignored) {
                        // Mã không tuân theo định dạng số chuẩn đằng sau tiền tố - bỏ qua
                    }
                }
            }
        }
        // Trả về mã mới bằng cách tăng giá trị max lên 1 và định dạng số chữ số đúng bằng width
        return prefix + String.format("%0" + width + "d", max + 1);
    }

    /**
     * Loại bỏ toàn bộ các dấu thanh, dấu mũ tiếng Việt trong chuỗi để thu được chuỗi ASCII không dấu.
     * <p>
     * Ví dụ: "Nguyễn Văn Nam" -> "Nguyen Van Nam".
     * </p>
     *
     * @param str Chuỗi tiếng Việt có dấu cần xử lý.
     * @return Chuỗi không dấu tương ứng; trả về chuỗi rỗng {@code ""} nếu tham số đầu vào bị {@code null}.
     */
    public static String removeDiacritics(String str) {
        // Kiểm tra đầu vào null
        if (str == null) return "";

        // Chuẩn hóa chuỗi theo chuẩn NFD để tách riêng chữ cái và dấu tổ hợp
        String nfdNormalizedString = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        // Sử dụng Regex để tìm và loại bỏ toàn bộ các dấu kết hợp tổ hợp diacritical marks
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(nfdNormalizedString).replaceAll("");

        // Xử lý riêng ký tự chữ 'đ' và 'Đ' tiếng Việt vì Normalizer không tự tách thành d + dấu
        return result.replace("đ", "d").replace("Đ", "D");
    }
}
