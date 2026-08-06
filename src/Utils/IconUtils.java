package Utils;

import javax.swing.ImageIcon;
import java.net.URL;

/**
 * Lớp tiện ích tải biểu tượng (Icon) từ tài nguyên ứng dụng trong thư mục `/resources/icons/`.
 * <p>
 * Lớp này chịu trách nhiệm nạp các hình ảnh icon biểu tượng PNG theo từng kích thước yêu cầu.
 * Nếu kích thước yêu cầu không sẵn có file icon tương ứng, phương thức tự động sử dụng cơ chế dự phòng (fallback)
 * lấy icon thuộc danh sách kích thước hiện có (48, 32, 24, 16 px) và co giãn mượt mà (smooth scaling) sang kích thước mong muốn.
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public final class IconUtils {

    /**
     * Khởi tạo riêng ngăn chặn tạo đối tượng tiện ích {@code IconUtils}.
     */
    private IconUtils() {
    }

    /**
     * Nạp đối tượng {@link ImageIcon} theo tên và kích thước chuẩn pixel.
     *
     * @param name Tên biểu tượng (ví dụ: "add", "edit", "ball").
     * @param size Kích thước vuông mong muốn tính theo pixel (ví dụ: 16, 24, 32, 48).
     * @return Đối tượng {@link ImageIcon} nếu tìm thấy hoặc co giãn thành công; trả về {@code null} nếu không tìm thấy ảnh.
     */
    public static ImageIcon getIcon(String name, int size) {
        // Xây dựng đường dẫn tương đối tới file icon theo tên và kích thước
        String path = "/resources/icons/" + name + "_" + size + "x" + size + ".png";
        URL url = IconUtils.class.getResource(path);
        // Nếu tìm thấy tài nguyên icon với kích thước chính xác -> trả về ImageIcon trực tiếp
        if (url != null) {
            return new ImageIcon(url);
        }
        // Cơ chế dự phòng (Fallback): Thử tìm các kích thước có sẵn (48, 32, 24, 16) và scale mượt
        int[] availableSizes = {48, 32, 24, 16};
        for (int s : availableSizes) {
            URL fallbackUrl = IconUtils.class.getResource("/resources/icons/" + name + "_" + s + "x" + s + ".png");
            if (fallbackUrl != null) {
                ImageIcon original = new ImageIcon(fallbackUrl);
                // Thực hiện scale hình ảnh theo thuật toán SCALE_SMOOTH để có chất lượng sắc nét
                java.awt.Image scaled = original.getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        }
        return null; // Không tìm thấy bất kỳ file biểu tượng nào
    }

    /**
     * Lấy icon Thêm mới ("add").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getAddIcon(int size) {
        return getIcon("add", size);
    }

    /**
     * Lấy icon Chỉnh sửa ("edit").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getEditIcon(int size) {
        return getIcon("edit", size);
    }

    /**
     * Lấy icon Xóa ("delete").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getDeleteIcon(int size) {
        return getIcon("delete", size);
    }

    /**
     * Lấy icon Làm mới / Tải lại ("refresh").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getRefreshIcon(int size) {
        return getIcon("refresh", size);
    }

    /**
     * Lấy icon Mở file / Mở dữ liệu ("open").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getOpenIcon(int size) {
        return getIcon("open", size);
    }

    /**
     * Lấy icon Quả bóng ("ball").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getBallIcon(int size) {
        return getIcon("ball", size);
    }

    /**
     * Lấy icon Quả bóng màu đen ("ball").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getBallBlackIcon(int size) {
        return getIcon("ball", size);
    }

    /**
     * Lấy icon Quả bóng màu trắng ("ball_white").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getBallWhiteIcon(int size) {
        return getIcon("ball_white", size);
    }

    /**
     * Lấy icon Đã xác nhận / Hoàn tất ("check").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getCheckIcon(int size) {
        return getIcon("check", size);
    }

    /**
     * Lấy icon Xuất dữ liệu / Excel ("export").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getExportIcon(int size) {
        return getIcon("export", size);
    }

    /**
     * Lấy icon Tìm kiếm ("search").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getSearchIcon(int size) {
        return getIcon("search", size);
    }

    /**
     * Lấy icon Bảo trì sân bóng ("maintenance").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getMaintenanceIcon(int size) {
        return getIcon("maintenance", size);
    }

    /**
     * Lấy icon Trạng thái ("status").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getStatusIcon(int size) {
        return getIcon("status", size);
    }

    /**
     * Lấy icon Tài khoản / Người dùng ("user").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getUserIcon(int size) {
        return getIcon("user", size);
    }

    /**
     * Lấy icon Tiền tệ / Tài chính ("money").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getMoneyIcon(int size) {
        return getIcon("money", size);
    }

    /**
     * Lấy icon Lịch / Thời gian ("calendar").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getCalendarIcon(int size) {
        return getIcon("calendar", size);
    }

    /**
     * Lấy icon Bóng đá ("ball").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getSoccerIcon(int size) {
        return getIcon("ball", size);
    }

    /**
     * Lấy icon Hộp / Kho hàng ("box").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getBoxIcon(int size) {
        return getIcon("box", size);
    }

    /**
     * Lấy icon Thẻ nhãn / Giá ("tag").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getTagIcon(int size) {
        return getIcon("tag", size);
    }

    /**
     * Lấy icon Đồ uống / Dịch vụ ("drink").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getDrinkIcon(int size) {
        return getIcon("drink", size);
    }

    /**
     * Lấy icon Hóa đơn ("receipt").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getReceiptIcon(int size) {
        return getIcon("receipt", size);
    }

    /**
     * Lấy icon Đóng / Thoát ("close").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getCloseIcon(int size) {
        return getIcon("close", size);
    }

    /**
     * Lấy icon Ghim / Vị trí ("pin").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getPinIcon(int size) {
        return getIcon("pin", size);
    }

    /**
     * Lấy icon Cảnh báo / Chú ý ("warning").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getWarningIcon(int size) {
        return getIcon("warning", size);
    }

    /**
     * Lấy icon Khởi chạy / Play ("play").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getPlayIcon(int size) {
        return getIcon("play", size);
    }

    /**
     * Lấy icon Mũi tên sang phải ("arrow_right").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getArrowRightIcon(int size) {
        return getIcon("arrow_right", size);
    }

    /**
     * Lấy icon Trang trước ("prev").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getPrevIcon(int size) {
        return getIcon("prev", size);
    }

    /**
     * Lấy icon Trang sau ("next").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getNextIcon(int size) {
        return getIcon("next", size);
    }

    /**
     * Lấy icon Trang đầu tiên ("first").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getFirstIcon(int size) {
        return getIcon("first", size);
    }

    /**
     * Lấy icon Trang cuối cùng ("last").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getLastIcon(int size) {
        return getIcon("last", size);
    }

    /**
     * Lấy icon Dấu chấm / Chấm tròn ("dot").
     *
     * @param size Kích thước hiển thị (px).
     * @return Biểu tượng {@link ImageIcon} tương ứng.
     */
    public static ImageIcon getDotIcon(int size) {
        return getIcon("dot", size);
    }
}
