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
 * Hộp thoại (JDialog) quản lý việc thêm mới và cập nhật thông tin mặt hàng trong Kho hàng (nước uống, áo bib, bóng...).
 * <p>
 * Dialog hỗ trợ kiểm tra ràng buộc dữ liệu trùng tên mặt hàng, số lượng tồn kho nguyên không âm, đơn giá không âm
 * và điền sẵn thông tin nhà cung cấp mặc định nếu để trống.
 * </p>
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class KhoFormDialog extends JDialog {

    /** Ô nhập tên hàng hóa / mặt hàng kho */
    private JTextField txtTenHangHoa;

    /** Ô nhập số lượng tồn kho hiện tại */
    private JTextField txtSoLuongTon;

    /** Ô nhập đơn giá bán lẻ (VNĐ) */
    private JTextField txtDonGia;

    /** Ô nhập tên nhà cung cấp */
    private JTextField txtNhaCungCap;

    /** Cờ đánh dấu chế độ chỉnh sửa (true) hay tạo mới (false) */
    private boolean isEdit;

    /** Đối tượng dịch vụ/mặt hàng kho gốc */
    private DichVu original;

    /** Đối tượng dịch vụ/mặt hàng kho kết quả sau khi lưu */
    private DichVu result;

    /** Cờ đánh dấu đã xác nhận lưu hay chưa */
    private boolean confirmed;

    // Variables declaration - do not modify
    /** Nhãn tiêu đề header dialog */
    private javax.swing.JLabel lblHeaderTitle;
    /** Panel bọc phần nội dung trung tâm */
    private javax.swing.JPanel pnlCenterWrap;
    /** Panel chứa các nút chức năng ở chân dialog */
    private javax.swing.JPanel pnlFooter;
    /** Panel chứa form nhập liệu GridBagLayout */
    private javax.swing.JPanel pnlFormCard;
    /** Panel header chứa tiêu đề */
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration

    /**
     * Khởi tạo dialog kho mặc định.
     */
    public KhoFormDialog() {
        this(null, null);
    }

    /**
     * Khởi tạo dialog kho để thêm mới hoặc chỉnh sửa mặt hàng sẵn có.
     *
     * @param parent   Cửa sổ cha (JFrame)
     * @param existing Đối tượng {@link DichVu} đại diện mặt hàng kho cần sửa, hoặc {@code null} nếu thêm mới
     */
    public KhoFormDialog(JFrame parent, DichVu existing) {
        super(parent, existing == null ? "Thêm mặt hàng kho mới" : "Sửa mặt hàng kho", true);
        this.isEdit = existing != null;
        this.original = existing;

        // Khởi tạo thành phần GUI cơ bản
        initComponents();
        // Cấu hình form tùy chỉnh và nạp dữ liệu
        customInit(parent);
    }

    /**
     * Khởi tạo các thành phần giao diện theo NetBeans GUI Builder.
     */
    private void initComponents() {
        pnlHeader = new javax.swing.JPanel();
        lblHeaderTitle = new javax.swing.JLabel();
        pnlCenterWrap = new javax.swing.JPanel();
        pnlFormCard = new javax.swing.JPanel();
        pnlFooter = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Thông tin kho");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblHeaderTitle.setFont(UIConstants.FONT_TITLE);
        lblHeaderTitle.setForeground(java.awt.Color.WHITE);
        lblHeaderTitle.setText("Thông tin mặt hàng kho");
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
     * Thiết lập kích thước dialog, tạo các trường nhập liệu và nút chức năng.
     *
     * @param parent Cửa sổ cha để định vị vị trí dialog
     */
    private void customInit(JFrame parent) {
        setSize(460, 360);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Sửa mặt hàng kho" : "Thêm mặt hàng kho mới");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        // Ô nhập tên hàng hóa
        txtTenHangHoa = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Tên hàng hóa *", txtTenHangHoa);

        // Ô nhập số lượng tồn kho
        txtSoLuongTon = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Số lượng tồn *", txtSoLuongTon);

        // Ô nhập đơn giá
        txtDonGia = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Đơn giá (VNĐ) *", txtDonGia);

        // Ô nhập tên nhà cung cấp
        txtNhaCungCap = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Nhà cung cấp", txtNhaCungCap);

        // Nút Hủy
        JButton btnCancel = new javax.swing.JButton("Hủy");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        // Nút Lưu thông tin
        JButton btnSave = new javax.swing.JButton(isEdit ? "Lưu thay đổi" : "Lưu");
        btnSave.setFont(UIConstants.FONT_BOLD);
        btnSave.addActionListener(e -> onSave());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSave);

        // Đổ thông tin hoặc thiết lập số lượng tồn mặc định = 0
        if (isEdit && original != null) {
            fillForm(original);
        } else {
            txtSoLuongTon.setText("0");
        }

        getRootPane().setDefaultButton(btnSave);
    }

    /**
     * Thêm một thành phần giao diện và nhãn tiêu đề tương ứng vào form GridBagLayout.
     *
     * @param form  Panel form
     * @param gbc   GridBagConstraints cấu hình bố cục
     * @param row   Chỉ số hàng hiện tại
     * @param label Chuỗi văn bản nhãn
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
     * Định dạng phông chữ và màu nền cho combobox.
     *
     * @param combo JComboBox cần áp dụng kiểu dáng
     */
    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    /**
     * Đổ thông tin của mặt hàng kho lên các ô nhập liệu.
     *
     * @param d Đối tượng DichVu đại diện mặt hàng kho
     */
    private void fillForm(DichVu d) {
        txtTenHangHoa.setText(d.getTenHangHoa());
        txtSoLuongTon.setText(String.valueOf(d.getSoLuongTon()));
        txtDonGia.setText(String.valueOf((long) d.getDonGia()));
        txtNhaCungCap.setText(d.getNhaCungCap());
    }

    /**
     * Kiểm tra tính hợp lệ dữ liệu và tiến hành lưu mặt hàng kho.
     */
    private void onSave() {
        String ten = txtTenHangHoa.getText().trim();
        String giaStr = txtDonGia.getText().trim().replace(",", "").replace(".", "");
        String slStr = txtSoLuongTon.getText().trim();
        String ncc = txtNhaCungCap.getText().trim();

        // 1. Kiểm tra tên hàng hóa
        if (ten.isEmpty() || ten.length() < 2) {
            JOptionPane.showMessageDialog(this, "Tên hàng hóa không hợp lệ! Vui lòng nhập từ 2 ký tự trở lên.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTenHangHoa.requestFocus();
            return;
        }

        // 2. Kiểm tra trùng tên hàng hóa trong kho
        boolean duplicateName = DataStore.get().getKhoItems().stream()
                .anyMatch(d -> d.getTenHangHoa() != null && d.getTenHangHoa().equalsIgnoreCase(ten)
                        && (!isEdit || (original != null && d.getId() != original.getId())));
        if (duplicateName) {
            JOptionPane.showMessageDialog(this,
                    "Mặt hàng kho '" + ten + "' đã tồn tại trong kho hàng. Vui lòng chọn tên khác hoặc chọn 'Nhập thêm số lượng'!",
                    "Cảnh báo trùng tên mặt hàng", JOptionPane.WARNING_MESSAGE);
            txtTenHangHoa.requestFocus();
            return;
        }

        // 3. Kiểm tra số lượng tồn phải là số nguyên không âm
        int tonVal;
        try {
            tonVal = Integer.parseInt(slStr);
            if (tonVal < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng tồn phải là số nguyên không âm (>= 0).", "Thông báo", JOptionPane.WARNING_MESSAGE);
                txtSoLuongTon.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng tồn không hợp lệ! Vui lòng nhập số nguyên.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtSoLuongTon.requestFocus();
            return;
        }

        // 4. Kiểm tra đơn giá phải là số không âm
        double price;
        try {
            price = Double.parseDouble(giaStr);
            if (price < 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá phải là số không âm (>= 0).", "Thông báo", JOptionPane.WARNING_MESSAGE);
                txtDonGia.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Đơn giá không hợp lệ! Vui lòng nhập số hợp lệ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtDonGia.requestFocus();
            return;
        }

        // 5. Khởi tạo đối tượng DichVu kết quả lưu vào kho
        result = new DichVu(isEdit ? original.getId() : 0, ten, ncc.isEmpty() ? "Tổng kho Sân bóng" : ncc, price,
                isEdit ? original.getDonVi() : "cái", "DangBan", tonVal, 5);
        result.setNhaCungCap(ncc.isEmpty() ? "Tổng kho Sân bóng" : ncc);
        confirmed = true;
        dispose();
    }

    /**
     * Trả về cờ đánh dấu kết quả lưu thành công.
     *
     * @return {@code true} nếu đã xác nhận, ngược lại {@code false}
     */
    public boolean isConfirmed() { return confirmed; }

    /**
     * Lấy mặt hàng kho kết quả sau khi lưu.
     *
     * @return Đối tượng {@link DichVu}
     */
    public DichVu getResult() { return result; }
}
