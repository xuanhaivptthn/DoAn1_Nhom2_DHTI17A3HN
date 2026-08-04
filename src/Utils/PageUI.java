package Utils;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

/**
 * Tiện ích tạo header và định dạng bảng tiêu chuẩn Swing.
 */
public final class PageUI {

    private PageUI() {}

    public static JPanel createPageHeader(String title, String subtitle) {
        JPanel header = new JPanel(new BorderLayout(0, 4));
        header.setBackground(UIConstants.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel t = new JLabel(title);
        t.setFont(UIConstants.FONT_TITLE);
        t.setForeground(Color.WHITE);
        t.setIcon(IconUtils.getBallWhiteIcon(24));
        t.setIconTextGap(10);

        JLabel s = new JLabel(subtitle);
        s.setFont(UIConstants.FONT_SMALL);
        s.setForeground(new Color(200, 230, 201));

        header.add(t, BorderLayout.NORTH);
        header.add(s, BorderLayout.SOUTH);
        return header;
    }

    public static void styleTable(JTable table) {
        table.setFont(UIConstants.FONT_NORMAL);
        table.setRowHeight(34);
        table.getTableHeader().setFont(UIConstants.FONT_TABLE_HEADER);
        table.getTableHeader().setBackground(UIConstants.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);

        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                label.setFont(UIConstants.FONT_TABLE_HEADER);
                label.setBackground(UIConstants.PRIMARY);
                label.setForeground(Color.WHITE);
                label.setOpaque(true);

                String colName = value != null ? value.toString() : "";
                int align = getColumnAlignment(colName, null);
                label.setHorizontalAlignment(align);

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

        javax.swing.table.DefaultTableCellRenderer standardCellRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setFont(UIConstants.FONT_NORMAL);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : UIConstants.TABLE_ROW_ALT);
                    c.setForeground(UIConstants.TEXT_PRIMARY);
                }

                if (value != null) {
                    String strVal = value.toString().trim();
                    // Smart Status Formatting
                    if (strVal.equals("Sẵn sàng") || strVal.equals("Hoạt động") || strVal.equals("Hoàn thành") || strVal.startsWith("[OK]")) {
                        setFont(UIConstants.FONT_BOLD);
                        if (!isSelected) setForeground(UIConstants.SUCCESS);
                    } else if (strVal.contains("Bảo trì") || strVal.equals("Đang thuê") || strVal.equals("Đang đá") || strVal.equals("Đang đặt") || strVal.contains("[Đã đặt]")) {
                        setFont(UIConstants.FONT_BOLD);
                        if (!isSelected) setForeground(new Color(217, 119, 6)); // Amber / Orange
                    } else if (strVal.equals("Đã khóa") || strVal.equals("Bị khóa") || strVal.equals("Đã hủy") || strVal.startsWith("[!]")) {
                        setFont(UIConstants.FONT_BOLD);
                        if (!isSelected) setForeground(UIConstants.DANGER);
                    } else if (strVal.equals("Quản trị viên") || strVal.equals("Chủ sân")) {
                        setFont(UIConstants.FONT_BOLD);
                        if (!isSelected) setForeground(new Color(156, 39, 176)); // Purple
                    } else if (strVal.equals("Nhân viên")) {
                        setFont(UIConstants.FONT_BOLD);
                        if (!isSelected) setForeground(new Color(25, 118, 210)); // Blue
                    }

                    // Column Name Based Alignment (Ensures 100% consistency across all rows in a column)
                    String colName = "";
                    try {
                        colName = t.getColumnName(column);
                    } catch (Exception ignored) {}

                    setHorizontalAlignment(getColumnAlignment(colName, value));
                }
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            Class<?> colClass = table.getColumnClass(i);
            if (colClass != Boolean.class && colClass != boolean.class) {
                table.getColumnModel().getColumn(i).setCellRenderer(standardCellRenderer);
            }
        }
    }

    public static int getColumnAlignment(String colName, Object cellValue) {
        if (colName != null && !colName.isBlank()) {
            String lowerName = colName.toLowerCase(java.util.Locale.ROOT).trim();

            // Right alignment: Prices, Amounts, Revenue, Expenses, Real values
            if (lowerName.contains("giá") || lowerName.contains("tiền") || lowerName.contains("doanh thu")
                    || lowerName.contains("thành tiền") || lowerName.contains("chi phí")
                    || lowerName.contains("số tiền") || lowerName.contains("thực tế")) {
                return javax.swing.SwingConstants.RIGHT;
            }

            // Center alignment: Codes, IDs, STT, Phones, Dates, Times, Status, Role, Unit, Type, Quantities
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

            // Left alignment: Names, Descriptions, Addresses, Notes, Customer, Staff, Report Title, Pitch Name
            if (lowerName.contains("tên") || lowerName.contains("mô tả") || lowerName.contains("nội dung")
                    || lowerName.contains("khách") || lowerName.contains("nhân viên")
                    || lowerName.contains("nhà cung cấp") || lowerName.contains("địa chỉ")
                    || lowerName.contains("ghi chú") || lowerName.contains("chỉ số")
                    || lowerName.contains("báo cáo") || lowerName.contains("sân bóng")) {
                return javax.swing.SwingConstants.LEFT;
            }
        }

        // Fallback checks by cell value type/suffix
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

    public static JPanel createTableCardPanel(String title, JLabel lblCount, JTable table) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

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

        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    public static void stylePrimaryButton(javax.swing.JButton btn) {
        btn.setFont(UIConstants.FONT_BUTTON);
        btn.setBackground(null);
        btn.setForeground(null);
        btn.setFocusPainted(false);
    }

    public static void styleSecondaryButton(javax.swing.JButton btn) {
        btn.setFont(UIConstants.FONT_BUTTON);
        btn.setBackground(null);
        btn.setForeground(null);
        btn.setFocusPainted(false);
    }

    public static void styleSuccessButton(javax.swing.JButton btn) {
        btn.setFont(UIConstants.FONT_BUTTON);
        btn.setBackground(null);
        btn.setForeground(null);
        btn.setFocusPainted(false);
    }

    public static void styleDangerButton(javax.swing.JButton btn) {
        btn.setFont(UIConstants.FONT_BUTTON);
        btn.setBackground(null);
        btn.setForeground(null);
        btn.setFocusPainted(false);
    }

    public static JPanel createStatCard(String label, String value, Color accent) {
        return createStatCard(label, value, null, accent);
    }

    public static JPanel createStatCard(String label, String value, String subtext, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_SMALL);
        lbl.setForeground(UIConstants.TEXT_SECONDARY);

        JLabel val = new JLabel(value);
        val.setName("value");
        val.setFont(UIConstants.FONT_TITLE);
        val.setForeground(UIConstants.TEXT_PRIMARY);

        card.add(lbl, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);

        if (subtext != null && !subtext.isBlank()) {
            JLabel sub = new JLabel(subtext);
            sub.setFont(UIConstants.FONT_SMALL);
            sub.setForeground(UIConstants.TEXT_SECONDARY);
            card.add(sub, BorderLayout.SOUTH);
        }
        return card;
    }

    public static void updateStatCard(JPanel card, String value) {
        for (Component c : card.getComponents()) {
            if (c instanceof JLabel l && "value".equals(l.getName())) {
                l.setText(value);
            }
        }
    }
}
