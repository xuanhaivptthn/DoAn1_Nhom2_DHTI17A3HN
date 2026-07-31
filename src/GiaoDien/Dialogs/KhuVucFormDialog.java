package GiaoDien.Dialogs;

import GiaoDien.Panels.*;

import Model.ChuSan;
import Model.KhuVucSan;
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
 * Dialog thêm / sửa khu vực sân bóng.
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class KhuVucFormDialog extends JDialog {

    private JTextField txtMaSan;
    private JTextField txtTenSan;
    private JComboBox<String> cboLoaiSan;
    private JTextField txtGiaTheoGio;
    private JComboBox<String> cboTrangThai;

    private boolean isEdit;
    private KhuVucSan original;
    private KhuVucSan result;
    private boolean confirmed;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblHeaderTitle;
    private javax.swing.JPanel pnlCenterWrap;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables

    public KhuVucFormDialog() {
        this(null, null);
    }

    public KhuVucFormDialog(JFrame parent, KhuVucSan existing) {
        super(parent, existing == null ? "Thêm khu vực sân bóng" : "Cập nhật khu vực sân bóng", true);
        this.isEdit = existing != null;
        this.original = existing;

        initComponents();
        customInit(parent);
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblHeaderTitle = new javax.swing.JLabel();
        pnlCenterWrap = new javax.swing.JPanel();
        pnlFormCard = new javax.swing.JPanel();
        pnlFooter = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Thông tin khu vực sân");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblHeaderTitle.setFont(UIConstants.FONT_TITLE);
        lblHeaderTitle.setForeground(java.awt.Color.WHITE);
        lblHeaderTitle.setText("Thông tin khu vực sân");
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
    }// </editor-fold>//GEN-END:initComponents

    private void customInit(JFrame parent) {
        setSize(460, 420);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Cập nhật khu vực sân" : "Thêm khu vực sân mới");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        txtMaSan = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Mã sân *", txtMaSan);

        txtTenSan = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Tên sân *", txtTenSan);

        cboLoaiSan = new JComboBox<>(new String[]{"Sân 5 người", "Sân 7 người", "Sân 11 người"});
        styleCombo(cboLoaiSan);
        row = addField(pnlFormCard, gbc, row, "Loại sân *", cboLoaiSan);

        txtGiaTheoGio = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Giá/giờ (VNĐ) *", txtGiaTheoGio);

        cboTrangThai = new JComboBox<>(new String[]{"Sẵn sàng", "Đang thuê", "Bảo trì"});
        styleCombo(cboTrangThai);
        addField(pnlFormCard, gbc, row, "Trạng thái *", cboTrangThai);

        JButton btnCancel = new javax.swing.JButton("Hủy");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnSave = new javax.swing.JButton(isEdit ? "Cập nhật" : "Lưu");
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

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    private void fillForm(KhuVucSan k) {
        txtMaSan.setText(k.getMaSan());
        txtTenSan.setText(k.getTenSan());
        cboLoaiSan.setSelectedItem(k.getLoaiSanHienThi());
        txtGiaTheoGio.setText(String.valueOf((long) k.getGiaThueTheoGio()));
        cboTrangThai.setSelectedItem(k.getTrangThaiHienThi());
    }

    private void onSave() {
        String ma = txtMaSan.getText().trim();
        String ten = txtTenSan.getText().trim();
        String giaStr = txtGiaTheoGio.getText().trim().replace(",", "").replace(".", "");

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã sân và tên sân không được để trống.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(giaStr);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá theo giờ không hợp lệ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String loaiCode = switch ((String) cboLoaiSan.getSelectedItem()) {
            case "Sân 7 người" -> "San7";
            case "Sân 11 người" -> "San11";
            default -> "San5";
        };
        String ttCode = switch ((String) cboTrangThai.getSelectedItem()) {
            case "Đang thuê" -> "DangThue";
            case "Bảo trì" -> "BaoTri";
            default -> "SanSang";
        };

        result = new KhuVucSan(ma, ten, loaiCode, price, ttCode);
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() { return confirmed; }
    public KhuVucSan getResult() { return result; }
}
