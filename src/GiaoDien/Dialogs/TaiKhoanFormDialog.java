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
 * Hộp thoại (JDialog) thêm mới và cập nhật tài khoản người dùng hệ thống dùng chung cho cả Chủ sân và Nhân viên.
 * <p>
 * Dialog hỗ trợ đăng ký/sửa thông tin tài khoản, độ dài tên đăng nhập, xác nhận mật khẩu,
 * phân quyền vai trò (Admin, Nhân viên, Chủ sân) và tự động thay đổi hiển thị các trường liên quan (như Địa chỉ khi chọn vai trò Nhân viên).
 * </p>
 */
public class TaiKhoanFormDialog extends JDialog {

    /** Ô nhập tên đăng nhập */
    private JTextField txtTenDangNhap;

    /** Ô nhập mật khẩu */
    private JPasswordField txtMatKhau;

    /** Ô nhập xác nhận lại mật khẩu */
    private JPasswordField txtXacNhanMatKhau;

    /** Combobox chọn vai trò (Admin / Nhân viên / Chủ sân) */
    private JComboBox<String> cboVaiTro;

    /** Combobox chọn trạng thái tài khoản (Hoạt động / Khóa) */
    private JComboBox<String> cboTrangThai;

    /** Ô nhập họ và tên chủ tài khoản */
    private JTextField txtHoTen;

    /** Ô nhập số điện thoại chủ tài khoản */
    private JTextField txtSoDienThoai;

    /** Nhãn tiêu đề cho ô Địa chỉ (ẩn/hiện tùy thuộc vai trò) */
    private JLabel lblDiaChi;

    /** Ô nhập địa chỉ liên hệ (dành cho nhân viên) */
    private JTextField txtDiaChi;

    /** Cờ đánh dấu chế độ sửa (true) hay thêm mới (false) */
    private boolean isEdit;

    /** Đối tượng tài khoản gốc trước khi sửa */
    private TaiKhoan original;

    /** Hàm kiểm tra trùng tên đăng nhập */
    private Predicate<String> usernameExistsChecker;

    /** Đối tượng tài khoản kết quả sau khi lưu */
    private TaiKhoan result;

    /** Cờ đánh dấu người dùng đã lưu thành công hay chưa */
    private boolean confirmed;

    /** Họ tên bổ sung cho chủ tài khoản */
    private String hoTen;

    /** Số điện thoại bổ sung cho chủ tài khoản */
    private String soDienThoai;

    /** Địa chỉ bổ sung cho nhân viên */
    private String diaChi;

    // Variables declaration - do not modify
    /** Nhãn tiêu đề header dialog */
    private javax.swing.JLabel lblHeaderTitle;
    /** Panel bọc phần nội dung trung tâm */
    private javax.swing.JPanel pnlCenterWrap;
    /** Panel footer chứa các nút hành động */
    private javax.swing.JPanel pnlFooter;
    /** Panel form card chứa GridBagLayout */
    private javax.swing.JPanel pnlFormCard;
    /** Panel header tiêu đề dialog */
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration

    /**
     * Khởi tạo dialog tài khoản mặc định.
     */
    public TaiKhoanFormDialog() {
        this(null, null, null);
    }

    /**
     * Khởi tạo dialog tài khoản với đối tượng sẵn có và hàm kiểm tra trùng tên đăng nhập.
     *
     * @param parent                 Cửa sổ cha (JFrame)
     * @param existing               Đối tượng {@link TaiKhoan} cần sửa hoặc {@code null} nếu thêm mới
     * @param usernameExistsChecker  Hàm lambda kiểm tra tên đăng nhập đã tồn tại trong hệ thống chưa
     */
    public TaiKhoanFormDialog(JFrame parent, TaiKhoan existing, Predicate<String> usernameExistsChecker) {
        super(parent, existing == null ? "Thêm tài khoản mới" : "Sửa thông tin tài khoản", true);
        this.isEdit = existing != null;
        this.original = existing;
        this.usernameExistsChecker = usernameExistsChecker;

        // Khởi tạo các thành phần giao diện NetBeans
        initComponents();
        // Cấu hìnhform nhập liệu chi tiết
        customInit(parent);
    }

    /**
     * Khởi tạo cấu trúc giao diện đồ họa cơ bản.
     */
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
        lblHeaderTitle.setText("Thông tin tài khoản hệ thống");
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

    /**
     * Thiết lập thông số kích thước, bố cục form nhập liệu và sự kiện kiểm tra vai trò.
     *
     * @param parent Cửa sổ cha dùng để định vị hiển thị dialog
     */
    private void customInit(JFrame parent) {
        setSize(500, 580);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Cập nhật tài khoản người dùng" : "Thêm tài khoản người dùng mới");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Ô nhập tên đăng nhập
        txtTenDangNhap = new javax.swing.JTextField(20);
        row = addField(pnlFormCard, gbc, row, new JLabel("Tên đăng nhập *"), txtTenDangNhap);

        // Ô nhập mật khẩu
        txtMatKhau = new javax.swing.JPasswordField(20);
        row = addField(pnlFormCard, gbc, row, new JLabel(isEdit ? "Mật khẩu (để trống nếu giữ nguyên)" : "Mật khẩu *"), txtMatKhau);

        // Ô nhập xác nhận lại mật khẩu
        txtXacNhanMatKhau = new javax.swing.JPasswordField(20);
        row = addField(pnlFormCard, gbc, row, new JLabel(isEdit ? "Xác nhận mật khẩu" : "Xác nhận mật khẩu *"), txtXacNhanMatKhau);

        // Combobox vai trò
        cboVaiTro = new JComboBox<>(UIConstants.VAI_TRO_HIEN_THI);
        styleCombo(cboVaiTro);
        row = addField(pnlFormCard, gbc, row, new JLabel("Vai trò *"), cboVaiTro);

        // Ô nhập Họ và tên người dùng
        txtHoTen = new javax.swing.JTextField(20);
        row = addField(pnlFormCard, gbc, row, new JLabel("Họ và tên *"), txtHoTen);

        // Ô nhập số điện thoại
        txtSoDienThoai = new javax.swing.JTextField(20);
        row = addField(pnlFormCard, gbc, row, new JLabel("Số điện thoại *"), txtSoDienThoai);

        // Ô nhập địa chỉ (dành cho Nhân viên)
        lblDiaChi = new JLabel("Địa chỉ");
        txtDiaChi = new javax.swing.JTextField(20);
        row = addField(pnlFormCard, gbc, row, lblDiaChi, txtDiaChi);

        // Tự động ẩn/hiện trường địa chỉ khi chọn lại vai trò
        cboVaiTro.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                updateRoleFields();
            }
        });

        // Combobox chọn trạng thái tài khoản
        cboTrangThai = new JComboBox<>(UIConstants.TRANG_THAI_HIEN_THI);
        styleCombo(cboTrangThai);
        row = addField(pnlFormCard, gbc, row, new JLabel("Trạng thái *"), cboTrangThai);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weighty = 1;
        pnlFormCard.add(new JLabel(), gbc);

        // Nút Hủy
        JButton btnCancel = new javax.swing.JButton("Hủy");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        // Nút Lưu/Cập nhật
        JButton btnSave = new javax.swing.JButton(isEdit ? "Cập nhật" : "Lưu");
        btnSave.addActionListener(e -> onSave());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSave);

        // Nạp dữ liệu cũ nếu ở chế độ sửa
        if (isEdit && original != null) {
            fillForm(original);
        }
        updateRoleFields();

        getRootPane().setDefaultButton(btnSave);
    }

    /**
     * Cập nhật ẩn/hiện các ô nhập liệu tùy thuộc theo vai trò tài khoản được chọn trong combobox.
     */
    private void updateRoleFields() {
        boolean isNhanVien = "Nhân viên".equals(cboVaiTro.getSelectedItem());
        if (lblDiaChi != null) lblDiaChi.setVisible(isNhanVien);
        if (txtDiaChi != null) txtDiaChi.setVisible(isNhanVien);
    }

    /**
     * Thêm nhãn và thành phần giao diện vào panel form GridBag.
     *
     * @param form      Panel form
     * @param gbc       GridBagConstraints
     * @param row       Chỉ số hàng
     * @param labelComp Thành phần nhãn JLabel
     * @param field     Thành phần nhập liệu
     * @return Chỉ số hàng tiếp theo
     */
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

    /**
     * Định dạng combobox theo chuẩn giao diện.
     *
     * @param combo Combobox cần định dạng
     */
    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
        combo.setBorder(javax.swing.BorderFactory.createLineBorder(UIConstants.BORDER));
    }

    /**
     * Đổ thông tin tài khoản có sẵn và các hồ sơ thông tin cá nhân liên quan (Chủ sân / Nhân viên).
     *
     * @param tk Đối tượng TaiKhoan
     */
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

    /**
     * Thực hiện xác minh dữ liệu nhập vào (tên đăng nhập, mật khẩu, họ tên, SĐT) và lưu tài khoản.
     */
    private void onSave() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        String xacNhan = new String(txtXacNhanMatKhau.getPassword());
        int vaiTroIdx = cboVaiTro.getSelectedIndex();
        int trangThaiIdx = cboTrangThai.getSelectedIndex();

        String hTen = txtHoTen.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String dChi = txtDiaChi.getText().trim();

        // 1. Kiểm tra tên đăng nhập
        if (tenDangNhap.isEmpty()) {
            showError("Vui lòng nhập tên đăng nhập.");
            txtTenDangNhap.requestFocus();
            return;
        }
        if (!tenDangNhap.matches("^[a-zA-Z0-9_]{3,30}$")) {
            showError("Tên đăng nhập không hợp lệ! Vui lòng chỉ dùng chữ cái, chữ số, dấu gạch dưới và dài từ 3-30 ký tự.");
            txtTenDangNhap.requestFocus();
            return;
        }

        // 2. Kiểm tra mật khẩu (khi thêm mới hoặc khi đổi mật khẩu)
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

        // 3. Kiểm tra Họ và tên
        if (hTen.isEmpty() || hTen.length() < 2) {
            showError("Họ và tên không hợp lệ! Vui lòng nhập từ 2 ký tự trở lên.");
            txtHoTen.requestFocus();
            return;
        }

        // 4. Kiểm tra số điện thoại 10 chữ số
        if (sdt.isEmpty()) {
            showError("Vui lòng nhập số điện thoại.");
            txtSoDienThoai.requestFocus();
            return;
        }
        if (!sdt.matches("^\\d{10}$")) {
            showError("Số điện thoại không hợp lệ! Vui lòng nhập đúng 10 chữ số (ví dụ: 0912345678).");
            txtSoDienThoai.requestFocus();
            return;
        }

        // Giữ mật khẩu cũ nếu chỉnh sửa và để trống mật khẩu mới
        String finalPassword;
        if (isEdit) {
            finalPassword = matKhau.isEmpty() ? original.getMatKhau() : matKhau;
        } else {
            finalPassword = matKhau;
        }

        // Ánh xạ vai trò và trạng thái thành mã hệ thống
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

        // Đóng gói đối tượng TaiKhoan kết quả
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

    /**
     * Lấy họ tên chủ tài khoản nhập trên form.
     *
     * @return Chuỗi họ tên
     */
    public String getHoTen() { return hoTen; }

    /**
     * Lấy số điện thoại chủ tài khoản nhập trên form.
     *
     * @return Chuỗi số điện thoại
     */
    public String getSoDienThoai() { return soDienThoai; }

    /**
     * Lấy địa chỉ của nhân viên nhập trên form.
     *
     * @return Chuỗi địa chỉ
     */
    public String getDiaChi() { return diaChi; }

    /**
     * Hiển thị cảnh báo lỗi cho người dùng bằng JOptionPane.
     *
     * @param message Thông điệp lỗi
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Trả về cờ đánh dấu đã xác nhận thành công.
     *
     * @return {@code true} nếu đã lưu thành công, ngược lại {@code false}
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Lấy đối tượng tài khoản kết quả.
     *
     * @return Đối tượng {@link TaiKhoan}
     */
    public TaiKhoan getResult() {
        return result;
    }
}
