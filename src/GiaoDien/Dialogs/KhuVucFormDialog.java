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
 * Hộp thoại (JDialog) hỗ trợ thêm mới và cập nhật khu vực sân bóng (Sân 5, Sân 7, Sân 11).
 * <p>
 * Mã sân bóng sẽ được tự động sinh mã hệ thống, ẩn khỏi giao diện nhập liệu.
 * Dialog hỗ trợ kiểm tra tên sân trùng lặp, đơn giá thuê theo giờ phải lớn hơn 0,
 * cũng như quản lý trạng thái hoạt động / bảo trì của sân bóng.
 * </p>
 */
public class KhuVucFormDialog extends JDialog {

    /** Ô nhập tên sân bóng */
    private JTextField txtTenSan;

    /** Combobox chọn loại sân bóng (Sân 5 người, Sân 7 người, Sân 11 người) */
    private JComboBox<String> cboLoaiSan;

    /** Ô nhập giá thuê sân bóng theo giờ (VNĐ) */
    private JTextField txtGiaTheoGio;

    /** Combobox chọn trạng thái sân (Sẵn sàng / Đang Bảo trì) */
    private JComboBox<String> cboTrangThai;

    /** Cờ đánh dấu chế độ chỉnh sửa (true) hay thêm mới (false) */
    private boolean isEdit;

    /** Đối tượng khu vực sân bóng gốc trước khi chỉnh sửa */
    private KhuVucSan original;

    /** Đối tượng khu vực sân bóng kết quả sau khi lưu thành công */
    private KhuVucSan result;

    /** Cờ đánh dấu người dùng đã xác nhận lưu hay chưa */
    private boolean confirmed;

    // Variables declaration - do not modify
    /** Nhãn tiêu đề header dialog */
    private javax.swing.JLabel lblHeaderTitle;
    /** Panel bọc phần nội dung trung tâm */
    private javax.swing.JPanel pnlCenterWrap;
    /** Panel footer chứa các nút bấm hành động */
    private javax.swing.JPanel pnlFooter;
    /** Panel chứa các ô nhập liệu dạng GridBagLayout */
    private javax.swing.JPanel pnlFormCard;
    /** Panel header tiêu đề dialog */
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration

    /**
     * Khởi tạo dialog khu vực sân mặc định.
     */
    public KhuVucFormDialog() {
        this(null, null);
    }

    /**
     * Khởi tạo dialog khu vực sân bóng với thông tin có sẵn hoặc thêm mới.
     *
     * @param parent   Cửa sổ cha (JFrame)
     * @param existing Đối tượng {@link KhuVucSan} cần cập nhật hoặc {@code null} nếu thêm mới
     */
    public KhuVucFormDialog(JFrame parent, KhuVucSan existing) {
        super(parent, existing == null ? "Thêm khu vực sân bóng" : "Cập nhật khu vực sân bóng", true);
        this.isEdit = existing != null;
        this.original = existing;

        // Khởi tạo thành phần GUI cơ bản
        initComponents();
        // Cấu hình giao diện tùy chỉnh
        customInit(parent);
    }

    /**
     * Khởi tạo giao diện cấu trúc theo NetBeans GUI Builder.
     */
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
     * Khởi tạo các ô nhập tên sân, loại sân, giá giờ và trạng thái sân.
     *
     * @param parent Cửa sổ cha dùng định vị vị trí hiển thị dialog
     */
    private void customInit(JFrame parent) {
        setSize(460, 360);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Cập nhật khu vực sân" : "Thêm khu vực sân mới");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        // Ô nhập tên sân
        txtTenSan = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Tên sân *", txtTenSan);

        // Combobox quy mô / loại sân bóng
        cboLoaiSan = new JComboBox<>(new String[]{"Sân 5 người", "Sân 7 người", "Sân 11 người"});
        styleCombo(cboLoaiSan);
        row = addField(pnlFormCard, gbc, row, "Loại sân *", cboLoaiSan);

        // Ô nhập đơn giá theo giờ
        txtGiaTheoGio = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Giá/giờ (VNĐ) *", txtGiaTheoGio);

        // Combobox chọn trạng thái hoạt động của sân
        cboTrangThai = new JComboBox<>(new String[]{"Sẵn sàng", "Đang Bảo trì"});
        styleCombo(cboTrangThai);
        addField(pnlFormCard, gbc, row, "Trạng thái *", cboTrangThai);

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

        // Đổ thông tin dữ liệu sân có sẵn
        if (isEdit && original != null) {
            fillForm(original);
        }

        getRootPane().setDefaultButton(btnSave);
    }

    /**
     * Thêm hàng gồm tiêu đề và ô nhập liệu vào panel form GridBag.
     *
     * @param form  Panel chứa form
     * @param gbc   GridBagConstraints
     * @param row   Chỉ số hàng
     * @param label Chuỗi nhãn tiêu đề
     * @param field Thành phần nhập liệu
     * @return Chỉ số hàng tiếp theo
     */
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

    /**
     * Định dạng combobox theo chuẩn thiết kế UIConstants.
     *
     * @param combo Combobox cần định dạng
     */
    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    /**
     * Đổ dữ liệu đối tượng KhuVucSan lên giao diện form.
     *
     * @param k Đối tượng khu vực sân bóng
     */
    private void fillForm(KhuVucSan k) {
        txtTenSan.setText(k.getTenSan());
        cboLoaiSan.setSelectedItem(k.getLoaiSanHienThi());
        txtGiaTheoGio.setText(String.valueOf((long) k.getGiaThueTheoGio()));
        String tt = k.getTrangThaiHienThi();
        if ("Đang thuê".equals(tt)) {
            cboTrangThai.setSelectedItem("Sẵn sàng");
        } else {
            cboTrangThai.setSelectedItem(tt);
        }
    }

    /**
     * Thực hiện kiểm tra tính hợp lệ dữ liệu đầu vào và lưu thông tin khu vực sân.
     */
    private void onSave() {
        String ten = txtTenSan.getText().trim();
        String giaStr = txtGiaTheoGio.getText().trim().replace(",", "").replace(".", "");

        // 1. Kiểm tra tên sân
        if (ten.isEmpty() || ten.length() < 2) {
            JOptionPane.showMessageDialog(this, "Tên sân không hợp lệ! Vui lòng nhập từ 2 ký tự trở lên.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTenSan.requestFocus();
            return;
        }

        // Tự động phát sinh mã sân mới nếu thêm mới
        String ma = isEdit ? original.getMaSan() : Utils.CodeGen.next("SAN", DataStore.get().getKhuVucs().stream().map(KhuVucSan::getMaSan).toList(), 3);

        // 2. Kiểm tra trùng tên sân trong hệ thống
        boolean tenExists = DataStore.get().getKhuVucs().stream()
                .anyMatch(k -> k.getTenSan() != null && k.getTenSan().equalsIgnoreCase(ten)
                        && (isEdit ? !k.getMaSan().equalsIgnoreCase(original.getMaSan()) : true));
        if (tenExists) {
            JOptionPane.showMessageDialog(this,
                    "Tên sân '" + ten + "' đã tồn tại trong hệ thống. Vui lòng nhập tên khác!",
                    "Cảnh báo trùng tên sân", JOptionPane.WARNING_MESSAGE);
            txtTenSan.requestFocus();
            return;
        }

        // 3. Kiểm tra giá thuê theo giờ phải là số dương lớn hơn 0
        double price;
        try {
            price = Double.parseDouble(giaStr);
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "Giá theo giờ phải là số dương lớn hơn 0 (VNĐ).", "Thông báo", JOptionPane.WARNING_MESSAGE);
                txtGiaTheoGio.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá theo giờ không hợp lệ! Vui lòng nhập số hợp lệ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtGiaTheoGio.requestFocus();
            return;
        }

        // Ánh xạ mã loại sân
        String loaiCode = switch ((String) cboLoaiSan.getSelectedItem()) {
            case "Sân 7 người" -> "San7";
            case "Sân 11 người" -> "San11";
            default -> "San5";
        };
        // Ánh xạ mã trạng thái
        String ttCode = switch ((String) cboTrangThai.getSelectedItem()) {
            case "Đang Bảo trì" -> "BAO_TRI";
            default -> "HOAT_DONG";
        };

        // Đóng gói đối tượng KhuVucSan kết quả
        result = new KhuVucSan(ma, ten, loaiCode, price, ttCode);
        confirmed = true;
        dispose();
    }

    /**
     * Trả về trạng thái lưu thành công.
     *
     * @return {@code true} nếu đã xác nhận, ngược lại {@code false}
     */
    public boolean isConfirmed() { return confirmed; }

    /**
     * Lấy đối tượng khu vực sân bóng kết quả.
     *
     * @return Đối tượng {@link KhuVucSan}
     */
    public KhuVucSan getResult() { return result; }
}
