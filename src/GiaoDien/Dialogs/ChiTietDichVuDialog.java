package GiaoDien.Dialogs;

import Model.DichVu;
import Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Dialog xem chi tiết thông tin dịch vụ trong Quản lý dịch vụ.
 * <p>
 * Hiển thị đầy đủ thông tin mã dịch vụ, tên dịch vụ, loại dịch vụ, đơn giá
 * và phần mô tả chi tiết của gói dịch vụ. Cho phép người dùng chọn nút Sửa để chuyển sang chỉnh sửa.
 * </p>
 *
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class ChiTietDichVuDialog extends JDialog {

    /** Đối tượng dịch vụ được chọn để xem chi tiết */
    private final DichVu dichVu;

    /** Cờ đánh dấu người dùng có chọn yêu cầu chỉnh sửa dịch vụ hay không */
    private boolean editRequested = false;

    /**
     * Khởi tạo dialog xem chi tiết dịch vụ.
     *
     * @param parent Cửa sổ cha (JFrame)
     * @param dichVu Đối tượng gói dịch vụ cần hiển thị thông tin
     */
    public ChiTietDichVuDialog(JFrame parent, DichVu dichVu) {
        super(parent, "Chi tiết dịch vụ - " + (dichVu != null ? dichVu.getMaDichVu() : ""), true);
        this.dichVu = dichVu;
        initUI(parent);
    }

    /**
     * Khởi tạo và thiết lập bố cục toàn bộ giao diện thông tin chi tiết dịch vụ.
     *
     * @param parent Cửa sổ cha dùng để căn giữa dialog
     */
    private void initUI(JFrame parent) {
        setSize(480, 400);
        setResizable(false);
        if (parent != null) setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // ── 1. Header Panel ──────────────────────────────────────────────────
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel lblTitle = new JLabel("[i] CHI TIẾT GÓI DỊCH VỤ");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        add(pnlHeader, BorderLayout.NORTH);

        // ── 2. Content Form Card ──────────────────────────────────────────────
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBackground(UIConstants.BG);
        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        if (dichVu != null) {
            int row = 0;
            // Hiển thị các thông tin cơ bản
            row = addInfoRow(pnlCenter, gbc, row, "Mã dịch vụ:", dichVu.getMaDichVu());
            row = addInfoRow(pnlCenter, gbc, row, "Tên dịch vụ:", dichVu.getTenDichVu());
            row = addInfoRow(pnlCenter, gbc, row, "Loại dịch vụ:", dichVu.getLoaiDichVu());
            row = addInfoRow(pnlCenter, gbc, row, "Đơn giá:", String.format("%,.0f VNĐ", (double) dichVu.getDonGia()));

            // Ô hiển thị vùng mô tả dài của dịch vụ
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0.3;
            JLabel lblMoTaTitle = new JLabel("Mô tả:");
            lblMoTaTitle.setFont(UIConstants.FONT_BOLD);
            lblMoTaTitle.setForeground(UIConstants.TEXT_PRIMARY);
            pnlCenter.add(lblMoTaTitle, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            JTextArea txtMoTa = new JTextArea(dichVu.getMoTa() != null && !dichVu.getMoTa().isEmpty() ? dichVu.getMoTa() : "Chưa có mô tả.");
            txtMoTa.setFont(UIConstants.FONT_NORMAL);
            txtMoTa.setLineWrap(true);
            txtMoTa.setWrapStyleWord(true);
            txtMoTa.setEditable(false);
            txtMoTa.setBackground(Color.WHITE);
            txtMoTa.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));

            JScrollPane spMoTa = new JScrollPane(txtMoTa);
            spMoTa.setPreferredSize(new Dimension(240, 80));
            spMoTa.setBorder(javax.swing.BorderFactory.createLineBorder(UIConstants.BORDER));
            pnlCenter.add(spMoTa, gbc);
        }

        add(pnlCenter, BorderLayout.CENTER);

        // ── 3. Footer Buttons ─────────────────────────────────────────────────
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pnlFooter.setBackground(UIConstants.BG);

        // Nút Sửa dịch vụ
        JButton btnEdit = new JButton(" Sửa dịch vụ");
        btnEdit.setIcon(Utils.IconUtils.getEditIcon(16));
        Utils.PageUI.stylePrimaryButton(btnEdit);
        btnEdit.addActionListener(e -> {
            editRequested = true;
            dispose();
        });

        // Nút Đóng
        JButton btnClose = new JButton("Đóng");
        Utils.PageUI.styleSecondaryButton(btnClose);
        btnClose.addActionListener(e -> dispose());

        pnlFooter.add(btnEdit);
        pnlFooter.add(btnClose);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    /**
     * Thêm một dòng thông tin hiển thị gồm nhãn tiêu đề và giá trị vào form.
     *
     * @param panel Panel form
     * @param gbc   GridBagConstraints
     * @param row   Dòng hiện tại
     * @param label Tiêu đề nhãn
     * @param value Giá trị chuỗi hiển thị
     * @return Dòng tiếp theo
     */
    private int addInfoRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BOLD);
        lbl.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel val = new JLabel(value != null ? value : "");
        val.setFont(UIConstants.FONT_NORMAL);
        val.setForeground(UIConstants.TEXT_PRIMARY);
        panel.add(val, gbc);

        return row + 1;
    }

    /**
     * Kiểm tra xem người dùng có kích hoạt yêu cầu bấm nút Sửa dịch vụ hay không.
     *
     * @return true nếu người dùng bấm nút Sửa dịch vụ, false nếu đóng thoại
     */
    public boolean isEditRequested() {
        return editRequested;
    }
}
