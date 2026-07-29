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
 */
public class ChiTietDichVuDialog extends JDialog {

    private final DichVu dichVu;
    private boolean editRequested = false;

    public ChiTietDichVuDialog(JFrame parent, DichVu dichVu) {
        super(parent, "Chi tiết dịch vụ - " + (dichVu != null ? dichVu.getMaDichVu() : ""), true);
        this.dichVu = dichVu;
        initUI(parent);
    }

    private void initUI(JFrame parent) {
        setSize(480, 400);
        setResizable(false);
        if (parent != null) setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel lblTitle = new JLabel("[i] CHI TIẾT GÓI DỊCH VỤ");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        add(pnlHeader, BorderLayout.NORTH);

        // Content Form Card
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBackground(UIConstants.BG);
        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        if (dichVu != null) {
            int row = 0;
            row = addInfoRow(pnlCenter, gbc, row, "Mã dịch vụ:", dichVu.getMaDichVu());
            row = addInfoRow(pnlCenter, gbc, row, "Tên dịch vụ:", dichVu.getTenDichVu());
            row = addInfoRow(pnlCenter, gbc, row, "Loại dịch vụ:", dichVu.getLoaiDichVu());
            row = addInfoRow(pnlCenter, gbc, row, "Đơn giá:", String.format("%,.0f VNĐ", (double) dichVu.getDonGia()));

            // Description area
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

        // Footer Buttons
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pnlFooter.setBackground(UIConstants.BG);

        JButton btnEdit = new JButton("✎ Sửa dịch vụ");
        btnEdit.setFont(UIConstants.FONT_BUTTON);
        btnEdit.addActionListener(e -> {
            editRequested = true;
            dispose();
        });

        JButton btnClose = new JButton("Đóng");
        btnClose.setFont(UIConstants.FONT_BUTTON);
        btnClose.addActionListener(e -> dispose());

        pnlFooter.add(btnEdit);
        pnlFooter.add(btnClose);

        add(pnlFooter, BorderLayout.SOUTH);
    }

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

    public boolean isEditRequested() {
        return editRequested;
    }
}
