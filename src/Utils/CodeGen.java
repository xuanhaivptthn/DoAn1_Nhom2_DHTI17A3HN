package Utils;

import java.util.List;

/**
 * Sinh mã định danh mới (VD: "DL004", "BT002") dựa trên số lớn nhất
 * đang tồn tại trong danh sách mã hiện có, thay cho cách sinh id cũ
 * dựa trên hashCode (Model.*.getId() đã bị loại bỏ).
 */
public final class CodeGen {

    private CodeGen() {
    }

    public static String next(String prefix, List<String> existingCodes, int width) {
        int max = 0;
        if (existingCodes != null) {
            for (String code : existingCodes) {
                if (code != null && code.startsWith(prefix)) {
                    try {
                        int n = Integer.parseInt(code.substring(prefix.length()));
                        if (n > max) max = n;
                    } catch (NumberFormatException ignored) {
                        // mã không theo định dạng số - bỏ qua
                    }
                }
            }
        }
        return prefix + String.format("%0" + width + "d", max + 1);
    }
}
