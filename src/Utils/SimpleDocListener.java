package Utils;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Lớp bọc lắng nghe sự thay đổi văn bản (DocumentListener) đơn giản hóa cho JTextField trong Swing.
 * <p>
 * Lớp này triển khai giao diện {@link DocumentListener} và quy gom cả 3 sự kiện thay đổi văn bản
 * (chèn thêm văn bản, xóa văn bản, thay đổi định dạng) về một hàm lắng nghe {@link Runnable} duy nhất.
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public class SimpleDocListener implements DocumentListener {

    /**
     * Hàm gọi lại (Callback Runnable) được kích hoạt khi văn bản trong JTextField có bất kỳ sự thay đổi nào.
     */
    private final Runnable callback;

    /**
     * Khởi tạo một {@code SimpleDocListener} với hàm callback xử lý sự kiện.
     *
     * @param callback Khối lệnh {@link Runnable} cần thực thi khi nội dung ô nhập liệu thay đổi.
     */
    public SimpleDocListener(Runnable callback) {
        this.callback = callback;
    }

    /**
     * Kích hoạt khi văn bản được chèn thêm vào ô nhập liệu.
     *
     * @param e Đối tượng sự kiện văn bản {@link DocumentEvent}.
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        // Gọi hàm callback thực thi xử lý logic khi chèn thêm văn bản
        callback.run();
    }

    /**
     * Kích hoạt khi văn bản bị xóa bớt khỏi ô nhập liệu.
     *
     * @param e Đối tượng sự kiện văn bản {@link DocumentEvent}.
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        // Gọi hàm callback thực thi xử lý logic khi xóa bớt văn bản
        callback.run();
    }

    /**
     * Kích hoạt khi thuộc tính văn bản thay đổi (định dạng, thuộc tính ô).
     *
     * @param e Đối tượng sự kiện văn bản {@link DocumentEvent}.
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
        // Gọi hàm callback thực thi xử lý logic khi thay đổi thuộc tính văn bản
        callback.run();
    }
}
