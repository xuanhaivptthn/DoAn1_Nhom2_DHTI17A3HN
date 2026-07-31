package GiaoDien.Dialogs;

import Model.ChuSan;
import Model.NhanVien;
import Model.TaiKhoan;
import Utils.DataStore;
import Utils.UIConstants;

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
 * Dialog thêm / sửa tài khoản dùng chung cho cả Chủ sân và Nhân viên.
 */
public class TaiKhoanFormDialog extends JDialog {

    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JPasswordField txtXacNhanMatKhau;
    private JComboBox<String> cboVaiTro;
    private JComboBox<String> cboTrangThai;

    private JTextField txtHoTen;
    private JTextField txtSoDienThoai;
    private JLabel lblDiaChi;
    private JTextField txtDiaChi;

    private boolean isEdit;
    private TaiKhoan original;
    private Predicate<String> usernameExistsChecker;

    private TaiKhoan result;
    private boolean confirmed;
    private String hoTen;
    private String soDienThoai;
    private String diaChi;

    // Variables declaration - do not modify
    private javax.swing.JLabel lblHeaderTitle;
    private javax.swing.JPanel pnlCenterWrap;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration

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
    }

    private void customInit(JFrame parent) {
        setSize(500, 580);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Cập nhật tài khoản người dùng" : "Thêm tài khoản người dùng mới");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        txtTenDangNhap = new javax.swing.JTextField(20);
        row = addField(pnlFormCard, gbc, row, new JLabel("Tên đăng nhập *"), txtTenDangNhap);

        txtMatKhau = new javax.swing.JPasswordField(20);
        row = addField(pnlFormCard, gbc, row, new JLabel(isEdit ? "Mật khẩu (để trống nếu giữ nguyên)" : "Mật khẩu *"), txtMatKhau);

        txtXacNhanMatKhau = new javax.swing.JPasswordField(20);
        row = addField(pnlFormCard, gbc, row, new JLabel(isEdit ? "Xác nhận mật khẩu" : "Xác nhận mật khẩu *"), txtXacNhanMatKhau);

        cboVaiTro = new JComboBox<>(UIConstants.VAI_TRO_HIEN_THI);
        styleCombo(cboVaiTro);
        row = addField(pnlFormCard, gbc, row, new JLabel("Vai trò *"), cboVaiTro);

        txtHoTen = new javax.swing.JTextField(20);
        row = addField(pnlFormCard, gbc, row, new JLabel("Họ và tên *"), txtHoTen);

        txtSoDienThoai = new javax.swing.JTextField(20);
        row = addField(pnlFormCard, gbc, row, new JLabel("Số điện thoại *"), txtSoDienThoai);

        lblDiaChi = new JLabel("Địa chỉ");
        txtDiaChi = new javax.swing.JTextField(20);
        row = addField(pnlFormCard, gbc, row, lblDiaChi, txtDiaChi);

        cboVaiTro.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                updateRoleFields();
            }
        });

        cboTrangThai = new JComboBox<>(UIConstants.TRANG_THAI_HIEN_THI);
        styleCombo(cboTrangThai);
        row = addField(pnlFormCard, gbc, row, new JLabel("Trạng thái *"), cboTrangThai);

        gbc.gridx = 0;
        gbc.gridy = row;
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
        updateRoleFields();

        getRootPane().setDefaultButton(btnSave);
    }

    private void updateRoleFields() {
        boolean isNhanVien = "Nhân viên".equals(cboVaiTro.getSelectedItem());
        if (lblDiaChi != null) lblDiaChi.setVisible(isNhanVien);
        if (txtDiaChi != null) txtDiaChi.setVisible(isNhanVien);
    }

    private int addField(JPanel form, GridBagConstraints gbc, int row, JLabel labelComp, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        gbc.gridwidth = 1;
        form.add(labelComp, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        field.setPreferredSize(new Dimension(240, 34));
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

        if (tk.isChuSan() || tk.isAdmin()) {
            ChuSan cs = DataStore.get().findChuSanByMaTaiKhoan(tk.getMaTaiKhoan());
            if (cs != null) {
                txtHoTen.setText(cs.getTenChuSan());
                txtSoDienThoai.setText(cs.getSoDienThoaiChuSan());
            }
        } else if (tk.isNhanVien()) {
            NhanVien nv = DataStore.get().findNhanVienByMaTaiKhoan(tk.getMaTaiKhoan());
            if (nv != null) {
                txtHoTen.setText(nv.getHoTenNhanVien());
                txtSoDienThoai.setText(nv.getSoDienThoaiNhanVien());
                txtDiaChi.setText(nv.getDiaChi());
            }
        }
    }

    private void onSave() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        String xacNhan = new String(txtXacNhanMatKhau.getPassword());
        int vaiTroIdx = cboVaiTro.getSelectedIndex();
        int trangThaiIdx = cboTrangThai.getSelectedIndex();

        String hTen = txtHoTen.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String dChi = txtDiaChi.getText().trim();

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

        if (hTen.isEmpty()) {
            showError("Vui lòng nhập họ và tên.");
            txtHoTen.requestFocus();
            return;
        }
        if (sdt.isEmpty()) {
            showError("Vui lòng nhập số điện thoại.");
            txtSoDienThoai.requestFocus();
            return;
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
        this.hoTen = hTen;
        this.soDienThoai = sdt;
        this.diaChi = dChi;

        confirmed = true;
        dispose();
    }

    public String getHoTen() { return hoTen; }
    public String getSoDienThoai() { return soDienThoai; }
    public String getDiaChi() { return diaChi; }

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
