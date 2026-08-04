package GiaoDien.Dialogs;

import GiaoDien.Panels.*;

import Model.DichVu;
import Utils.DataStore;
import Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Dialog thêm / sửa dịch vụ kho.
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class DichVuFormDialog extends JDialog {

    private JTextField txtTenDichVu;
    private JComboBox<String> cboLoaiDichVu;
    private JTextField txtDonGia;
    private JTextField txtMoTa;

    private boolean isEdit;
    private DichVu original;
    private DichVu result;
    private boolean confirmed;

    // Variables declaration - do not modify
    private javax.swing.JLabel lblHeaderTitle;
    private javax.swing.JPanel pnlCenterWrap;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration

    public DichVuFormDialog() {
        this(null, null);
    }

    public DichVuFormDialog(JFrame parent, DichVu existing) {
        super(parent, existing == null ? "Thêm dịch vụ mới" : "Sửa thông tin dịch vụ", true);
        this.isEdit = existing != null;
        this.original = existing;

        initComponents();
        customInit(parent);
    }

    private void initComponents() {
        pnlHeader = new javax.swing.JPanel();
        lblHeaderTitle = new javax.swing.JLabel();
        pnlCenterWrap = new javax.swing.JPanel();
        pnlFormCard = new javax.swing.JPanel();
        pnlFooter = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Thông tin dịch vụ");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblHeaderTitle.setFont(UIConstants.FONT_TITLE);
        lblHeaderTitle.setForeground(java.awt.Color.WHITE);
        lblHeaderTitle.setText("Thông tin dịch vụ");
        lblHeaderTitle.setIcon(Utils.IconUtils.getBallWhiteIcon(24));
        lblHeaderTitle.setIconTextGap(10);
        pnlHeader.add(lblHeaderTitle, java.awt.BorderLayout.WEST);

        getContentPane().add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlCenterWrap.setBackground(UIConstants.BG);
        pnlCenterWrap.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 8, 16));
        pnlCenterWrap.setLayout(new java.awt.BorderLayout());

        pnlFormCard.setLayout(new java.awt.GridBagLayout());
        pnlCenterWrap.add(pnlFormCard, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnlCenterWrap, java.awt.BorderLayout.CENTER);

        pnlFooter.setBackground(UIConstants.BG);
        pnlFooter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 12, 16));
        pnlFooter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 12));
        getContentPane().add(pnlFooter, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void customInit(JFrame parent) {
        setSize(460, 360);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Sửa dịch vụ" : "Thêm dịch vụ mới");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        txtTenDichVu = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Tên dịch vụ *", txtTenDichVu);

        cboLoaiDichVu = new JComboBox<>(new String[]{"Cho thuê", "Nhân sự", "Đồ uống", "Dịch vụ thi đấu", "HLV cá nhân", "Hồi phục & Sức khỏe", "Bữa/Suất", "Giặt sấy"});
        cboLoaiDichVu.setEditable(true);
        styleCombo(cboLoaiDichVu);
        row = addField(pnlFormCard, gbc, row, "Loại dịch vụ *", cboLoaiDichVu);

        txtDonGia = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Giá (VNĐ) *", txtDonGia);

        txtMoTa = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Mô tả", txtMoTa);

        JButton btnCancel = new javax.swing.JButton("Hủy");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnSave = new javax.swing.JButton(isEdit ? "Lưu thay đổi" : "Thêm dịch vụ");
        btnSave.setFont(UIConstants.FONT_BOLD);
        btnSave.addActionListener(e -> onSave());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSave);

        if (isEdit && original != null) {
            fillForm(original);
        }

        getRootPane().setDefaultButton(btnSave);
    }

    private int addField(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        gbc.gridwidth = 1;
        form.add(new javax.swing.JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        field.setPreferredSize(new Dimension(220, 36));
        form.add(field, gbc);
        return row + 1;
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    private void fillForm(DichVu d) {
        txtTenDichVu.setText(d.getTenDichVu());
        cboLoaiDichVu.setSelectedItem(d.getLoaiDichVu());
        txtDonGia.setText(String.valueOf((long) d.getDonGia()));
        txtMoTa.setText(d.getMoTa());
    }

    private void onSave() {
        String ten = txtTenDichVu.getText().trim();
        String giaStr = txtDonGia.getText().trim().replace(",", "").replace(".", "");
        String loaiStr = cboLoaiDichVu.getSelectedItem() != null ? cboLoaiDichVu.getSelectedItem().toString().trim() : "Cho thuê";

        if (ten.isEmpty() || ten.length() < 2) {
            JOptionPane.showMessageDialog(this, "Tên dịch vụ không hợp lệ! Vui lòng nhập từ 2 ký tự trở lên.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTenDichVu.requestFocus();
            return;
        }

        if (loaiStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc nhập loại dịch vụ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            cboLoaiDichVu.requestFocus();
            return;
        }

        boolean duplicateName = DataStore.get().getDichVus().stream()
                .anyMatch(d -> d.getTenDichVu() != null && d.getTenDichVu().equalsIgnoreCase(ten)
                        && (!isEdit || (original != null && !d.getMaDichVu().equalsIgnoreCase(original.getMaDichVu()))));
        if (duplicateName) {
            JOptionPane.showMessageDialog(this,
                    "Tên dịch vụ '" + ten + "' đã tồn tại trong hệ thống. Vui lòng nhập tên khác!",
                    "Cảnh báo trùng tên dịch vụ", JOptionPane.WARNING_MESSAGE);
            txtTenDichVu.requestFocus();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(giaStr);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Đơn giá không hợp lệ (phải là số không âm).", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtDonGia.requestFocus();
            return;
        }

        result = new DichVu(isEdit ? original.getId() : 0, ten, txtMoTa.getText().trim(), price,
                loaiStr, "DangBan", isEdit ? original.getSoLuongTon() : 100, isEdit ? original.getTonToiThieu() : 5);
        if (isEdit && original != null) {
            result.setMaDichVu(original.getMaDichVu());
        }
        result.setLoaiDichVu(loaiStr);
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() { return confirmed; }
    public DichVu getResult() { return result; }
}
