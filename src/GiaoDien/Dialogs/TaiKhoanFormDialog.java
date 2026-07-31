package GiaoDien.Dialogs;

import GiaoDien.Panels.*;

import Model.ChuSan;
import Model.TaiKhoan;
import Utils.DataStore;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Predicate;

/**
 * Dialog thêm / sửa tài khoản.
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class TaiKhoanFormDialog extends JDialog {

    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JPasswordField txtXacNhanMatKhau;
    private JComboBox<String> cboVaiTro;
    private JComboBox<String> cboTrangThai;
    private JPanel pnlChuSan;
    private JTextField txtTenChuSan;
    private JTextField txtSoDienThoaiChuSan;

    private boolean isEdit;
    private TaiKhoan original;
    private Predicate<String> usernameExistsChecker;

    private TaiKhoan result;
    private boolean confirmed;
    private boolean chuSanRole;
    private String tenChuSan;
    private String soDienThoaiChuSan;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblHeaderTitle;
    private javax.swing.JPanel pnlCenterWrap;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables

    public TaiKhoanFormDialog() {
        this(null, null, null);
    }

    public TaiKhoanFormDialog(JFrame parent, TaiKhoan existing, Predicate<String> usernameExistsChecker) {
        super(parent, existing == null ? "Thêm tài khoản mới" : "Sửa thông tin tài khoản", true);
        this.isEdit = existing != null;
        this.original = existing;
        this.usernameExistsChecker = usernameExistsChecker;

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
        setTitle("Thông tin tài khoản");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblHeaderTitle.setFont(UIConstants.FONT_TITLE);
        lblHeaderTitle.setForeground(java.awt.Color.WHITE);
        lblHeaderTitle.setText("Thông tin tài khoản");
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
        setSize(480, 560);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Cập nhật tài khoản" : "Thêm tài khoản mới");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        txtTenDangNhap = new javax.swing.JTextField(20);
        row = addField(pnlFormCard, gbc, row, "Tên đăng nhập *", txtTenDangNhap);

        txtMatKhau = new javax.swing.JPasswordField(20);
        row = addField(pnlFormCard, gbc, row, isEdit ? "Mật khẩu (để trống nếu giữ nguyên)" : "Mật khẩu *", txtMatKhau);

        txtXacNhanMatKhau = new javax.swing.JPasswordField(20);
        row = addField(pnlFormCard, gbc, row, isEdit ? "Xác nhận mật khẩu" : "Xác nhận mật khẩu *", txtXacNhanMatKhau);

        cboVaiTro = new JComboBox<>(UIConstants.VAI_TRO_HIEN_THI);
        styleCombo(cboVaiTro);
        row = addField(pnlFormCard, gbc, row, "Vai trò *", cboVaiTro);

        pnlChuSan = new JPanel(new GridBagLayout());
        pnlChuSan.setOpaque(false);
        GridBagConstraints gbcChuSan = new GridBagConstraints();
        gbcChuSan.insets = new Insets(8, 8, 8, 8);
        gbcChuSan.anchor = GridBagConstraints.WEST;
        gbcChuSan.fill = GridBagConstraints.HORIZONTAL;
        txtTenChuSan = new javax.swing.JTextField(20);
        txtSoDienThoaiChuSan = new javax.swing.JTextField(20);
        int chuSanRow = 0;
        chuSanRow = addField(pnlChuSan, gbcChuSan, chuSanRow, "Tên chủ sân", txtTenChuSan);
        addField(pnlChuSan, gbcChuSan, chuSanRow, "SĐT chủ sân", txtSoDienThoaiChuSan);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        pnlFormCard.add(pnlChuSan, gbc);
        gbc.gridwidth = 1;
        row++;

        cboVaiTro.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                updateChuSanVisibility();
            }
        });

        cboTrangThai = new JComboBox<>(UIConstants.TRANG_THAI_HIEN_THI);
        styleCombo(cboTrangThai);
        addField(pnlFormCard, gbc, row, "Trạng thái *", cboTrangThai);

        gbc.gridx = 0;
        gbc.gridy = row + 1;
        gbc.weighty = 1;
        pnlFormCard.add(new JLabel(), gbc);

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
        updateChuSanVisibility();

        getRootPane().setDefaultButton(btnSave);
    }

    private void updateChuSanVisibility() {
        boolean isAdmin = "Admin".equals(UIConstants.VAI_TRO[Math.max(cboVaiTro.getSelectedIndex(), 0)]);
        pnlChuSan.setVisible(isAdmin);
    }

    private int addField(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        gbc.gridwidth = 1;
        form.add(new javax.swing.JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        field.setPreferredSize(new Dimension(240, 36));
        form.add(field, gbc);
        return row + 1;
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
        combo.setBorder(javax.swing.BorderFactory.createLineBorder(UIConstants.BORDER));
    }

    private void fillForm(TaiKhoan tk) {
        txtTenDangNhap.setText(tk.getTenDangNhap());
        txtTenDangNhap.setEditable(false);
        txtTenDangNhap.setBackground(new Color(240, 240, 240));
        cboVaiTro.setSelectedItem(tk.getQuyenHanHienThi());
        cboTrangThai.setSelectedItem(tk.getTrangThaiHienThi());

        ChuSan cs = DataStore.get().findChuSanByMaTaiKhoan(tk.getMaTaiKhoan());
        if (cs != null) {
            txtTenChuSan.setText(cs.getTenChuSan());
            txtSoDienThoaiChuSan.setText(cs.getSoDienThoaiChuSan());
        }
    }

    private void onSave() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        String xacNhan = new String(txtXacNhanMatKhau.getPassword());
        int vaiTroIdx = cboVaiTro.getSelectedIndex();
        int trangThaiIdx = cboTrangThai.getSelectedIndex();

        if (tenDangNhap.isEmpty()) {
            showError("Vui lòng nhập tên đăng nhập.");
            txtTenDangNhap.requestFocus();
            return;
        }
        if (tenDangNhap.length() < 3) {
            showError("Tên đăng nhập phải có ít nhất 3 ký tự.");
            txtTenDangNhap.requestFocus();
            return;
        }
        if (!isEdit) {
            if (matKhau.isEmpty()) {
                showError("Vui lòng nhập mật khẩu.");
                txtMatKhau.requestFocus();
                return;
            }
            if (matKhau.length() < 6) {
                showError("Mật khẩu phải có ít nhất 6 ký tự.");
                txtMatKhau.requestFocus();
                return;
            }
            if (!matKhau.equals(xacNhan)) {
                showError("Mật khẩu xác nhận không khớp.");
                txtXacNhanMatKhau.requestFocus();
                return;
            }
            if (usernameExistsChecker != null && usernameExistsChecker.test(tenDangNhap)) {
                showError("Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.");
                txtTenDangNhap.requestFocus();
                return;
            }
        } else {
            if (!matKhau.isEmpty()) {
                if (matKhau.length() < 6) {
                    showError("Mật khẩu phải có ít nhất 6 ký tự.");
                    txtMatKhau.requestFocus();
                    return;
                }
                if (!matKhau.equals(xacNhan)) {
                    showError("Mật khẩu xác nhận không khớp.");
                    txtXacNhanMatKhau.requestFocus();
                    return;
                }
            }
        }
        String finalPassword;
        if (isEdit) {
            finalPassword = matKhau.isEmpty() ? original.getMatKhau() : matKhau;
        } else {
            finalPassword = matKhau;
        }

        String quyenHan = switch (UIConstants.VAI_TRO[vaiTroIdx]) {
            case "Admin" -> "ADMIN";
            case "NhanVien" -> "NHAN_VIEN";
            default -> UIConstants.VAI_TRO[vaiTroIdx].toUpperCase();
        };
        String trangThai = switch (UIConstants.TRANG_THAI[trangThaiIdx]) {
            case "HoatDong" -> "HOAT_DONG";
            case "Khoa" -> "KHOA";
            default -> UIConstants.TRANG_THAI[trangThaiIdx].toUpperCase();
        };

        result = new TaiKhoan(
                isEdit ? original.getMaTaiKhoan() : null,
                tenDangNhap,
                finalPassword,
                quyenHan,
                trangThai
        );
        this.chuSanRole = "ADMIN".equals(quyenHan);
        this.tenChuSan = txtTenChuSan.getText().trim();
        this.soDienThoaiChuSan = txtSoDienThoaiChuSan.getText().trim();
        confirmed = true;
        dispose();
    }

    /** true nếu vai trò được chọn là Chủ sân (Admin) — dùng để quyết định có lưu hồ sơ ChuSan hay không. */
    public boolean isChuSanRole() { return chuSanRole; }
    public String getTenChuSan() { return tenChuSan; }
    public String getSoDienThoaiChuSan() { return soDienThoaiChuSan; }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.WARNING_MESSAGE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public TaiKhoan getResult() {
        return result;
    }
}
