package Utils;

import java.util.List;

/**
 * Sinh mã định danh mới (VD: "DL004", "BT002") dựa trên số lớn nhất
 * đang tồn tại trong danh sách mã hiện có.
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

    public static String removeDiacritics(String str) {
        if (str == null) return "";
        String nfdNormalizedString = java.text.Normalizer.normalize(str, java.text.Normalizer.Form.NFD);
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(nfdNormalizedString).replaceAll("");
        return result.replace("đ", "d").replace("Đ", "D");
    }
}
