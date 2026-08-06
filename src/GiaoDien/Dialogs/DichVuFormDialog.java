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
 * Hộp thoại (JDialog) hỗ trợ thêm mới và cập nhật thông tin dịch vụ sân bóng (HLV, Trọng tài, Giặt sấy...).
 * <p>
 * Dialog cung cấp form nhập liệu kiểm tra trùng tên dịch vụ, đơn giá không âm và quản lý phân loại dịch vụ.
 * </p>
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class DichVuFormDialog extends JDialog {

    /** Ô nhập tên dịch vụ */
    private JTextField txtTenDichVu;

    /** Combobox chọn hoặc nhập loại dịch vụ */
    private JComboBox<String> cboLoaiDichVu;

    /** Ô nhập đơn giá dịch vụ (VNĐ) */
    private JTextField txtDonGia;

    /** Ô nhập mô tả chi tiết dịch vụ */
    private JTextField txtMoTa;

    /** Cờ đánh dấu chế độ sửa (true) hay tạo mới (false) */
    private boolean isEdit;

    /** Đối tượng dịch vụ gốc trước khi chỉnh sửa */
    private DichVu original;

    /** Đối tượng dịch vụ kết quả sau khi lưu */
    private DichVu result;

    /** Cờ xác nhận người dùng đã lưu thành công */
    private boolean confirmed;

    // Variables declaration - do not modify
    /** Nhãn tiêu đề header dialog */
    private javax.swing.JLabel lblHeaderTitle;
    /** Panel bọc nội dung trung tâm */
    private javax.swing.JPanel pnlCenterWrap;
    /** Panel footer chứa các nút bấm */
    private javax.swing.JPanel pnlFooter;
    /** Panel card chứa form GridBagLayout */
    private javax.swing.JPanel pnlFormCard;
    /** Panel header trên cùng */
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration

    /**
     * Khởi tạo dialog dịch vụ mặc định.
     */
    public DichVuFormDialog() {
        this(null, null);
    }

    /**
     * Khởi tạo dialog dịch vụ với đối tượng dịch vụ có sẵn hoặc tạo mới.
     *
     * @param parent   Cửa sổ cha (JFrame)
     * @param existing Đối tượng {@link DichVu} cần sửa hoặc {@code null} nếu thêm mới
     */
    public DichVuFormDialog(JFrame parent, DichVu existing) {
        super(parent, existing == null ? "Thêm dịch vụ mới" : "Sửa thông tin dịch vụ", true);
        this.isEdit = existing != null;
        this.original = existing;

        // Khởi tạo giao diện đồ họa cơ bản
        initComponents();
        // Cấu hình form nhập liệu chi tiết
        customInit(parent);
    }

    /**
     * Khởi tạo các thành phần giao diện theo chuẩn NetBeans GUI Builder.
     */
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

    /**
     * Cấu hình bố cục form, thêm các ô nhập liệu và đăng ký sự kiện nút bấm.
     *
     * @param parent Cửa sổ cha để định vị dialog
     */
    private void customInit(JFrame parent) {
        setSize(460, 360);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Sửa dịch vụ" : "Thêm dịch vụ mới");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        // Ô nhập tên dịch vụ
        txtTenDichVu = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Tên dịch vụ *", txtTenDichVu);

        // Combobox cho phép chọn hoặc tự nhập loại dịch vụ mới
        cboLoaiDichVu = new JComboBox<>(new String[]{"Cho thuê", "Nhân sự", "Dịch vụ thi đấu", "HLV cá nhân", "Giặt sấy"});
        cboLoaiDichVu.setEditable(true);
        styleCombo(cboLoaiDichVu);
        row = addField(pnlFormCard, gbc, row, "Loại dịch vụ *", cboLoaiDichVu);

        // Ô nhập đơn giá
        txtDonGia = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Giá (VNĐ) *", txtDonGia);

        // Ô nhập mô tả dịch vụ
        txtMoTa = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Mô tả", txtMoTa);

        // Nút Hủy
        JButton btnCancel = new javax.swing.JButton("Hủy");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        // Nút Lưu thông tin dịch vụ
        JButton btnSave = new javax.swing.JButton(isEdit ? "Lưu thay đổi" : "Thêm dịch vụ");
        btnSave.setFont(UIConstants.FONT_BOLD);
        btnSave.addActionListener(e -> onSave());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSave);

        // Đổ thông tin dịch vụ cũ nếu là chế độ chỉnh sửa
        if (isEdit && original != null) {
            fillForm(original);
        }

        getRootPane().setDefaultButton(btnSave);
    }

    /**
     * Thêm một ô nhập vào form theo lưới GridBagLayout.
     *
     * @param form  Panel form
     * @param gbc   Cấu hình GridBagConstraints
     * @param row   Chỉ số hàng hiện tại
     * @param label Chuỗi tiêu đề nhãn
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
     * Định dạng phông chữ và màu sắc cho Combobox.
     *
     * @param combo Combobox cần định dạng
     */
    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    /**
     * Đổ dữ liệu từ đối tượng DichVu lên form giao diện.
     *
     * @param d Đối tượng dịch vụ
     */
    private void fillForm(DichVu d) {
        txtTenDichVu.setText(d.getTenDichVu());
        cboLoaiDichVu.setSelectedItem(d.getLoaiDichVu());
        txtDonGia.setText(String.valueOf((long) d.getDonGia()));
        txtMoTa.setText(d.getMoTa());
    }

    /**
     * Xử lý kiểm tra dữ liệu đầu vào và lưu thông tin dịch vụ.
     */
    private void onSave() {
        String ten = txtTenDichVu.getText().trim();
        String giaStr = txtDonGia.getText().trim().replace(",", "").replace(".", "");
        String loaiStr = cboLoaiDichVu.getSelectedItem() != null ? cboLoaiDichVu.getSelectedItem().toString().trim() : "Cho thuê";

        // Validate tên dịch vụ
        if (ten.isEmpty() || ten.length() < 2) {
            JOptionPane.showMessageDialog(this, "Tên dịch vụ không hợp lệ! Vui lòng nhập từ 2 ký tự trở lên.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTenDichVu.requestFocus();
            return;
        }

        // Validate loại dịch vụ
        if (loaiStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hoặc nhập loại dịch vụ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            cboLoaiDichVu.requestFocus();
            return;
        }

        // Kiểm tra trùng tên dịch vụ trong hệ thống
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

        // Validate đơn giá
        double price;
        try {
            price = Double.parseDouble(giaStr);
            if (price < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Đơn giá không hợp lệ (phải là số không âm).", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtDonGia.requestFocus();
            return;
        }

        // Tạo đối tượng DichVu kết quả
        result = new DichVu(isEdit ? original.getId() : 0, ten, txtMoTa.getText().trim(), price,
                loaiStr, "DangBan", isEdit ? original.getSoLuongTon() : 100, isEdit ? original.getTonToiThieu() : 5);
        if (isEdit && original != null) {
            result.setMaDichVu(original.getMaDichVu());
        }
        result.setLoaiDichVu(loaiStr);
        confirmed = true;
        dispose();
    }

    /**
     * Trả về trạng thái lưu thành công.
     *
     * @return {@code true} nếu đã xác nhận lưu, {@code false} nếu chưa
     */
    public boolean isConfirmed() { return confirmed; }

    /**
     * Lấy đối tượng dịch vụ kết quả.
     *
     * @return Đối tượng {@link DichVu}
     */
    public DichVu getResult() { return result; }
}
