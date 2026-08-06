package GiaoDien.Dialogs;

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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * Dialog cho phép Nhân viên Bán / Cho thuê các vật phẩm thuộc Kho hàng qua Quản lý Dịch vụ.
 * <p>
 * Hộp thoại này thực hiện nhập thông tin chọn vật phẩm trong kho, hình thức giao dịch (bán hoặc cho thuê)
 * và số lượng cần giao dịch. Khi xác nhận, hệ thống kiểm tra tồn kho và tự động trừ số lượng tồn trong kho hàng.
 * </p>
 *
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class BanChoThueVatPhamDialog extends JDialog {

    /** Combobox chọn vật phẩm kho hàng */
    private JComboBox<DichVu> cboVatPham;

    /** Combobox chọn hình thức giao dịch (Bán / Cho thuê) */
    private JComboBox<String> cboHinhThuc;

    /** Ô nhập số lượng bán/cho thuê */
    private JTextField txtSoLuong;

    /** Vật phẩm kho hàng đã chọn */
    private DichVu selectedVatPham;

    /** Hình thức giao dịch ("Bán vật phẩm" hoặc "Cho thuê vật phẩm") */
    private String hinhThuc;

    /** Số lượng vật phẩm bán hoặc cho thuê */
    private int soLuong;

    /** Tổng số tiền tương ứng với giao dịch */
    private double tongTien;

    /** Trạng thái xác nhận của người dùng (true nếu nhấn Xác nhận thành công) */
    private boolean confirmed;

    /**
     * Khởi tạo thoại Bán / Cho thuê vật phẩm Kho hàng.
     *
     * @param parent Cửa sổ cha (JFrame) hiển thị dialog
     */
    public BanChoThueVatPhamDialog(JFrame parent) {
        super(parent, "Bán / Cho thuê vật phẩm Kho hàng", true);
        initUI(parent);
    }

    /**
     * Khởi tạo và bố trí toàn bộ thành phần giao diện người dùng (UI).
     *
     * @param parent Cửa sổ cha dùng để căn giữa dialog
     */
    private void initUI(JFrame parent) {
        // Thiết lập kích thước dialog và không cho phép thay đổi kích thước
        setSize(460, 360);
        setResizable(false);
        if (parent != null) setLocationRelativeTo(parent);

        setLayout(new BorderLayout());

        // ── 1. Header Dialog ──────────────────────────────────────────────────
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        
        JLabel lblTitle = new JLabel("[+] Bán / Cho thuê vật phẩm Kho hàng");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        add(pnlHeader, BorderLayout.NORTH);

        // ── 2. Center Form ────────────────────────────────────────────────────
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBackground(UIConstants.BG);
        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Lấy danh sách các vật phẩm trong kho từ DataStore
        List<DichVu> khoItems = DataStore.get().getKhoItems();
        cboVatPham = new JComboBox<>(khoItems.toArray(new DichVu[0]));
        styleCombo(cboVatPham);

        // Khởi tạo combobox hình thức bán / cho thuê
        cboHinhThuc = new JComboBox<>(new String[]{"Bán vật phẩm", "Cho thuê vật phẩm"});
        styleCombo(cboHinhThuc);

        // Khởi tạo ô nhập số lượng mặc định là 1
        txtSoLuong = new JTextField(10);
        txtSoLuong.setText("1");
        txtSoLuong.setFont(UIConstants.FONT_NORMAL);

        // Bố trí các trường nhập liệu vào form
        int row = 0;
        row = addField(pnlCenter, gbc, row, "Chọn vật phẩm kho *", cboVatPham);
        row = addField(pnlCenter, gbc, row, "Hình thức *", cboHinhThuc);
        addField(pnlCenter, gbc, row, "Số lượng *", txtSoLuong);

        add(pnlCenter, BorderLayout.CENTER);

        // ── 3. Footer Buttons ─────────────────────────────────────────────────
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pnlFooter.setBackground(UIConstants.BG);

        // Nút Hủy
        JButton btnCancel = new JButton("Hủy");
        Utils.PageUI.styleSecondaryButton(btnCancel);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        // Nút Xác nhận Bán / Cho thuê
        JButton btnSubmit = new JButton("Xác nhận Bán / Cho thuê");
        Utils.PageUI.stylePrimaryButton(btnSubmit);
        btnSubmit.addActionListener(e -> onConfirm());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSubmit);
        add(pnlFooter, BorderLayout.SOUTH);

        // Thiết lập nút mặc định khi bấm Enter
        getRootPane().setDefaultButton(btnSubmit);
    }

    /**
     * Thêm một nhãn và thành phần điều khiển (component) vào dòng chỉ định trên GridBagLayout.
     *
     * @param form  Panel chứa form
     * @param gbc   Đối tượng GridBagConstraints dùng điều chỉnh vị trí
     * @param row   Chỉ số dòng hiện tại
     * @param label Tiêu đề nhãn của trường dữ liệu
     * @param field Component nhập liệu (JTextField, JComboBox...)
     * @return Chỉ số dòng tiếp theo
     */
    private int addField(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_NORMAL);
        form.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        field.setPreferredSize(new Dimension(240, 36));
        form.add(field, gbc);
        return row + 1;
    }

    /**
     * Áp dụng kiểu dáng font chữ, màu sắc đồng bộ cho JComboBox.
     *
     * @param combo Combobox cần thiết lập giao diện
     */
    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    /**
     * Xử lý sự kiện khi người dùng bấm nút "Xác nhận Bán / Cho thuê".
     * Tiến hành kiểm tra dữ liệu đầu vào, kiểm tra số lượng tồn kho và cập nhật trừ xuất kho.
     */
    private void onConfirm() {
        // 1. Kiểm tra vật phẩm đã chọn
        selectedVatPham = (DichVu) cboVatPham.getSelectedItem();
        if (selectedVatPham == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vật phẩm trong kho.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        hinhThuc = (String) cboHinhThuc.getSelectedItem();
        String slStr = txtSoLuong.getText().trim();

        // 2. Validate số lượng nhập vào phải là số nguyên dương > 0
        try {
            soLuong = Integer.parseInt(slStr);
            if (soLuong <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương (> 0).", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Kiểm tra số lượng tồn kho có đủ không
        if (soLuong > selectedVatPham.getSoLuongTon()) {
            JOptionPane.showMessageDialog(this,
                    "KHO HÀNG KHÔNG ĐỦ VẬT PHẨM!\n"
                            + "• Yêu cầu: " + soLuong + "\n"
                            + "• Tồn kho hiện tại: " + selectedVatPham.getSoLuongTon() + " " + selectedVatPham.getDonVi() + "\n"
                            + "Vui lòng nhập thêm kho hàng trước khi thực hiện.",
                    "Cảnh báo kho hàng", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Khai báo trừ tồn kho và tính tổng tiền
        selectedVatPham.xuatKho(soLuong);
        tongTien = selectedVatPham.getDonGia() * soLuong;
        confirmed = true;
        dispose();
    }

    /**
     * Kiểm tra người dùng đã xác nhận thành công giao dịch hay chưa.
     *
     * @return true nếu giao dịch được xác nhận, false nếu bị hủy
     */
    public boolean isConfirmed() { return confirmed; }

    /**
     * Lấy đối tượng vật phẩm kho hàng đã chọn.
     *
     * @return Đối tượng DichVu đại diện cho vật phẩm
     */
    public DichVu getSelectedVatPham() { return selectedVatPham; }

    /**
     * Lấy hình thức giao dịch đã chọn (Bán / Cho thuê).
     *
     * @return Chuỗi mô tả hình thức giao dịch
     */
    public String getHinhThuc() { return hinhThuc; }

    /**
     * Lấy số lượng vật phẩm đã đăng ký bán/cho thuê.
     *
     * @return Số lượng giao dịch
     */
    public int getSoLuong() { return soLuong; }

    /**
     * Lấy tổng chi phí của giao dịch bán/cho thuê vật phẩm.
     *
     * @return Tổng số tiền (VNĐ)
     */
    public double getTongTien() { return tongTien; }
}
