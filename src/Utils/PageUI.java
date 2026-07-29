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

        JLabel s = new JLabel(subtitle);
        s.setFont(UIConstants.FONT_SMALL);
        s.setForeground(new Color(200, 230, 201));

        header.add(t, BorderLayout.NORTH);
        header.add(s, BorderLayout.SOUTH);
        return header;
    }

    public static void styleTable(JTable table) {
        table.setFont(UIConstants.FONT_NORMAL);
        table.setRowHeight(32);
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
                label.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(40, 100, 45)),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)
                ));
                return label;
            }
        };
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        table.setShowGrid(true);
        table.setGridColor(UIConstants.BORDER);
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
