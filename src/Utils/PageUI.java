package Utils;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

/**
 * Lớp tiện ích xây dựng giao diện tổng quan và định dạng bảng tiêu chuẩn Swing.
 * <p>
 * Lớp này cung cấp các phương thức dùng chung trong ứng dụng để khởi tạo tiêu đề trang (header),
 * định dạng bảng hiển thị dữ liệu (thẻ header, căn lề tự động, màu sọc dòng, tô màu trạng thái),
 * tạo khung chèn bảng (table card panel), tạo thẻ thống kê chỉ số (stat card) và áp dụng kiểu chữ cho nút nhấn.
 * </p>
 *
 * @author Quản Lý Sân Bóng
 * @version 1.0
 */
public final class PageUI {

    /**
     * Khởi tạo riêng ngăn ngừa việc khởi tạo đối tượng {@code PageUI}.
     */
    private PageUI() {}

    /**
     * Tạo một Panel tiêu đề trang chuẩn bao gồm tiêu đề chính và tiêu đề phụ.
     *
     * @param title    Chuỗi tiêu đề chính của trang.
     * @param subtitle Chuỗi mô tả ngắn / tiêu đề phụ của trang.
     * @return Đối tượng {@link JPanel} chứa header trang đã trang trí.
     */
    public static JPanel createPageHeader(String title, String subtitle) {
        // Khởi tạo JPanel layout BorderLayout với khoảng cách dòng 4px
        JPanel header = new JPanel(new BorderLayout(0, 4));
        header.setBackground(UIConstants.PRIMARY); // Đặt màu nền xanh chủ đạo
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20)); // Đệm viền lề trong

        // Khởi tạo JLabel tiêu đề chính với icon quả bóng màu trắng
        JLabel t = new JLabel(title);
        t.setFont(UIConstants.FONT_TITLE);
        t.setForeground(Color.WHITE);
        t.setIcon(IconUtils.getBallWhiteIcon(24));
        t.setIconTextGap(10);

        // Khởi tạo JLabel tiêu đề phụ
        JLabel s = new JLabel(subtitle);
        s.setFont(UIConstants.FONT_SMALL);
        s.setForeground(new Color(200, 230, 201));

        // Thêm các thành phần vào vị trí NORTH và SOUTH của header
        header.add(t, BorderLayout.NORTH);
        header.add(s, BorderLayout.SOUTH);
        return header;
    }

    /**
     * Định dạng thẩm mỹ cho bảng dữ liệu Swing {@link JTable}.
     * <p>
     * Thiết lập font chữ, chiều cao dòng, renderer cho header và các ô nội dung,
     * tự động căn lề theo tên cột và highlight các trạng thái với màu sắc trực quan.
     * </p>
     *
     * @param table Đối tượng bảng {@link JTable} cần được định dạng.
     */
    public static void styleTable(JTable table) {
        // Thiết lập font chữ và chiều cao dòng tiêu chuẩn
        table.setFont(UIConstants.FONT_NORMAL);
        table.setRowHeight(34);
        table.getTableHeader().setFont(UIConstants.FONT_TABLE_HEADER);
        table.getTableHeader().setBackground(UIConstants.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);

        // Bộ dựng tiêu đề bảng tùy chỉnh (Custom Header Renderer)
        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                label.setFont(UIConstants.FONT_TABLE_HEADER);
                label.setBackground(UIConstants.PRIMARY);
                label.setForeground(Color.WHITE);
                label.setOpaque(true);

                // Căn lề tiêu đề dựa trên tên cột tương ứng
                String colName = value != null ? value.toString() : "";
                int align = getColumnAlignment(colName, null);
                label.setHorizontalAlignment(align);

                // Đường viền dưới và bên phải cho header ô
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(40, 100, 45)),
                        BorderFactory.createEmptyBorder(6, 10, 6, 10)
                ));
                return label;
            }
        };
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        table.getTableHeader().setResizingAllowed(true);
        table.setShowGrid(true);
        table.setGridColor(UIConstants.BORDER);

        // Bộ dựng ô dữ liệu chuẩn (Standard Cell Renderer)
        javax.swing.table.DefaultTableCellRenderer standardCellRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setFont(UIConstants.FONT_NORMAL);

                // Tô màu xen kẽ giữa các dòng (Zebra-striping) khi không được chọn
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : UIConstants.TABLE_ROW_ALT);
                    c.setForeground(UIConstants.TEXT_PRIMARY);
                }

                if (value != null) {
                    String strVal = value.toString().trim();
                    // Tô màu thông minh cho chuỗi trạng thái nghiệp vụ (Smart Status Formatting)
                    if (strVal.equals("Sẵn sàng") || strVal.equals("Hoạt động") || strVal.equals("Hoàn thành") || strVal.startsWith("[OK]")) {
                        setFont(UIConstants.FONT_BOLD);
                        if (!isSelected) setForeground(UIConstants.SUCCESS);
                    } else if (strVal.contains("Bảo trì") || strVal.equals("Đang thuê") || strVal.equals("Đang đá") || strVal.equals("Đang đặt") || strVal.contains("[Đã đặt]")) {
                        setFont(UIConstants.FONT_BOLD);
                        if (!isSelected) setForeground(new Color(217, 119, 6)); // Cam hổ phách
                    } else if (strVal.equals("Đã khóa") || strVal.equals("Đã khoá") || strVal.equals("Bị khóa") || strVal.equals("Đã hủy") || strVal.startsWith("[!]")) {
                        setFont(UIConstants.FONT_BOLD);
                        if (!isSelected) setForeground(UIConstants.DANGER);
                    } else if (strVal.equals("Quản trị viên") || strVal.equals("Chủ sân")) {
                        setFont(UIConstants.FONT_BOLD);
                        if (!isSelected) setForeground(new Color(156, 39, 176)); // Tím
                    } else if (strVal.equals("Nhân viên")) {
                        setFont(UIConstants.FONT_BOLD);
                        if (!isSelected) setForeground(new Color(25, 118, 210)); // Xanh dương
                    }

                    // Xác định tên cột hiện tại để tự động điều chỉnh căn lề
                    String colName = "";
                    try {
                        colName = t.getColumnName(column);
                    } catch (Exception ignored) {}

                    setHorizontalAlignment(getColumnAlignment(colName, value));
                }
                return c;
            }
        };

        // Áp dụng renderer chuẩn cho toàn bộ các cột ngoại trừ kiểu Boolean
        for (int i = 0; i < table.getColumnCount(); i++) {
            Class<?> colClass = table.getColumnClass(i);
            if (colClass != Boolean.class && colClass != boolean.class) {
                table.getColumnModel().getColumn(i).setCellRenderer(standardCellRenderer);
            }
        }
    }

    /**
     * Xác định hướng căn lề (Trái, Phải, Giữa) phù hợp với kiểu dữ liệu của cột hoặc giá trị của ô.
     *
     * @param colName   Tên của cột dữ liệu (ví dụ: "Đơn giá", "Mã sân", "Tên khách hàng").
     * @param cellValue Giá trị hiển thị tại ô.
     * @return Hằng số căn lề Swing: {@code SwingConstants.LEFT}, {@code SwingConstants.RIGHT}, hoặc {@code SwingConstants.CENTER}.
     */
    public static int getColumnAlignment(String colName, Object cellValue) {
        if (colName != null && !colName.isBlank()) {
            String lowerName = colName.toLowerCase(java.util.Locale.ROOT).trim();

            // Căn PHẢI cho các cột số tiền, doanh thu, giá cả, chi phí
            if (lowerName.contains("giá") || lowerName.contains("tiền") || lowerName.contains("doanh thu")
                    || lowerName.contains("thành tiền") || lowerName.contains("chi phí")
                    || lowerName.contains("số tiền") || lowerName.contains("thực tế")) {
                return javax.swing.SwingConstants.RIGHT;
            }

            // Căn GIỮA cho các cột mã ID, STT, SĐT, ngày giờ, trạng thái, vai trò, số lượng
            if (lowerName.contains("mã") || lowerName.equals("stt") || lowerName.equals("id")
                    || lowerName.contains("sđt") || lowerName.contains("điện thoại")
                    || lowerName.contains("trạng thái") || lowerName.contains("vai trò")
                    || lowerName.contains("loại") || lowerName.contains("đơn vị")
                    || lowerName.contains("bắt đầu") || lowerName.contains("kết thúc")
                    || lowerName.contains("giờ") || lowerName.contains("ngày")
                    || lowerName.contains("hình thức") || lowerName.contains("số lượng")
                    || lowerName.contains("số lượt")) {
                return javax.swing.SwingConstants.CENTER;
            }

            // Căn TRÁI cho các cột họ tên, mô tả, địa chỉ, ghi chú
            if (lowerName.contains("tên") || lowerName.contains("mô tả") || lowerName.contains("nội dung")
                    || lowerName.contains("khách") || lowerName.contains("nhân viên")
                    || lowerName.contains("nhà cung cấp") || lowerName.contains("địa chỉ")
                    || lowerName.contains("ghi chú") || lowerName.contains("chỉ số")
                    || lowerName.contains("báo cáo") || lowerName.contains("sân bóng")) {
                return javax.swing.SwingConstants.LEFT;
            }
        }

        // Đối soát dự phòng dựa trên giá trị ô dữ liệu nếu không căn lề được theo tên cột
        if (cellValue != null) {
            String strVal = cellValue.toString().trim();
            if (strVal.endsWith("đ") || strVal.endsWith("VNĐ") || strVal.endsWith("đ/giờ")) {
                return javax.swing.SwingConstants.RIGHT;
            }
            if (strVal.matches("^(\\d+|STT\\d*|TK\\d*|A\\d+|B\\d+|C\\d+|HH\\d*|KH\\d*|DV\\d*|BT\\d*|DL\\d*)$")) {
                return javax.swing.SwingConstants.CENTER;
            }
        }

        return javax.swing.SwingConstants.LEFT;
    }

    /**
     * Tạo một Panel dạng thẻ Card bo góc bao bọc lấy bảng dữ liệu và tiêu đề bảng.
     *
     * @param title    Tiêu đề của bảng.
     * @param lblCount Nhãn hiển thị tổng số lượng bản ghi (có thể null).
     * @param table    Đối tượng bảng {@link JTable} cần đặt vào panel.
     * @return Panel thẻ chứa bảng hiển thị.
     */
    public static JPanel createTableCardPanel(String title, JLabel lblCount, JTable table) {
        // Tạo panel card tổng thể
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        // Panel header trên đỉnh thẻ card
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(UIConstants.FONT_SUBTITLE);
        t.setForeground(UIConstants.PRIMARY);

        top.add(t, BorderLayout.WEST);
        if (lblCount != null) {
            lblCount.setFont(UIConstants.FONT_SMALL);
            lblCount.setForeground(UIConstants.TEXT_SECONDARY);
            top.add(lblCount, BorderLayout.EAST);
        }
        card.add(top, BorderLayout.NORTH);

        // Tạo JScrollPane cuộn bảng dữ liệu
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    /**
     * Định dạng kiểu chữ cho nút hành động chính (Primary Button).
     *
     * @param btn Nút bấm {@link javax.swing.JButton}.
     */
    public static void stylePrimaryButton(javax.swing.JButton btn) {
        if (btn == null) return;
        btn.setFont(UIConstants.FONT_BUTTON);
    }

    /**
     * Định dạng kiểu chữ cho nút hành động phụ (Secondary Button).
     *
     * @param btn Nút bấm {@link javax.swing.JButton}.
     */
    public static void styleSecondaryButton(javax.swing.JButton btn) {
        if (btn == null) return;
        btn.setFont(UIConstants.FONT_BUTTON);
    }

    /**
     * Định dạng kiểu chữ cho nút thực hiện thành công (Success Button).
     *
     * @param btn Nút bấm {@link javax.swing.JButton}.
     */
    public static void styleSuccessButton(javax.swing.JButton btn) {
        if (btn == null) return;
        btn.setFont(UIConstants.FONT_BUTTON);
    }

    /**
     * Định dạng kiểu chữ cho nút hành động nguy hiểm / xóa (Danger Button).
     *
     * @param btn Nút bấm {@link javax.swing.JButton}.
     */
    public static void styleDangerButton(javax.swing.JButton btn) {
        if (btn == null) return;
        btn.setFont(UIConstants.FONT_BUTTON);
    }

    /**
     * Tạo thẻ chỉ số thống kê (Stat Card) đơn giản.
     *
     * @param label  Tên chỉ số (ví dụ: "Tổng doanh thu").
     * @param value  Giá trị hiển thị (ví dụ: "1,500,000 VNĐ").
     * @param accent Màu nhấn cho thẻ.
     * @return Panel thẻ thống kê {@link JPanel}.
     */
    public static JPanel createStatCard(String label, String value, Color accent) {
        return createStatCard(label, value, null, accent);
    }

    /**
     * Tạo thẻ chỉ số thống kê (Stat Card) kèm dòng ghi chú nhỏ bên dưới.
     *
     * @param label   Tên chỉ số.
     * @param value   Giá trị chính của chỉ số.
     * @param subtext Dòng mô tả/ghi chú phụ bên dưới.
     * @param accent  Màu sắc điểm nhấn.
     * @return Panel thẻ chỉ số thống kê hoàn chỉnh.
     */
    public static JPanel createStatCard(String label, String value, String subtext, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        // Nhãn tên chỉ số
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_SMALL);
        lbl.setForeground(UIConstants.TEXT_SECONDARY);

        // Nhãn giá trị chính của chỉ số (đặt tên "value" để dễ tìm lại khi update)
        JLabel val = new JLabel(value);
        val.setName("value");
        val.setFont(UIConstants.FONT_TITLE);
        val.setForeground(UIConstants.TEXT_PRIMARY);

        card.add(lbl, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);

        // Dòng chú thích phụ nếu có
        if (subtext != null && !subtext.isBlank()) {
            JLabel sub = new JLabel(subtext);
            sub.setFont(UIConstants.FONT_SMALL);
            sub.setForeground(UIConstants.TEXT_SECONDARY);
            card.add(sub, BorderLayout.SOUTH);
        }
        return card;
    }

    /**
     * Cập nhật giá trị hiển thị mới cho một thẻ chỉ số thống kê đã tạo.
     *
     * @param card  Thẻ chỉ số thống kê {@link JPanel}.
     * @param value Giá trị chuỗi mới cần cập nhật.
     */
    public static void updateStatCard(JPanel card, String value) {
        // Duyệt các component bên trong panel để tìm nhãn có name == "value"
        for (Component c : card.getComponents()) {
            if (c instanceof JLabel l && "value".equals(l.getName())) {
                l.setText(value);
            }
        }
    }
}
