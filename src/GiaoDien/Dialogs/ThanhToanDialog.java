package GiaoDien.Dialogs;

import GiaoDien.Panels.*;

import Model.DatLich;
import Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Hộp thoại (JDialog) chọn hình thức thanh toán cho phiếu đặt lịch sân bóng.
 * <p>
 * Dialog hiển thị mã phiếu đặt, tên khách hàng, tổng tiền thu thực tế
 * và cho phép chọn hình thức thanh toán (Tiền mặt, Chuyển khoản, Thẻ).
 * </p>
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class ThanhToanDialog extends JDialog {

    /** Nhãn hiển thị mã phiếu đặt lịch */
    private JLabel lblMaPhieu;

    /** Nhãn hiển thị tên khách hàng */
    private JLabel lblKhach;

    /** Nhãn hiển thị tổng số tiền cần thanh toán */
    private JLabel lblTongTien;

    /** Combobox chọn hình thức thanh toán (Tiền mặt / Chuyển khoản / Thẻ) */
    private JComboBox<String> cboHinhThuc;

    /** Đối tượng phiếu đặt lịch cần thanh toán */
    private DatLich datLich;

    /** Chuỗi hình thức thanh toán được chọn */
    private String hinhThuc;

    /** Cờ xác nhận người dùng đã nhấn nút Thanh toán */
    private boolean confirmed;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Nhãn tiêu đề header dialog */
    private javax.swing.JLabel lblHeaderTitle;
    /** Panel bọc nội dung trung tâm */
    private javax.swing.JPanel pnlCenterWrap;
    /** Panel footer chứa các nút hành động */
    private javax.swing.JPanel pnlFooter;
    /** Panel form card dạng GridBagLayout */
    private javax.swing.JPanel pnlFormCard;
    /** Panel header tiêu đề dialog */
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables

    /**
     * Khởi tạo dialog thanh toán mặc định.
     */
    public ThanhToanDialog() {
        this(null, null);
    }

    /**
     * Khởi tạo dialog thanh toán cho một phiếu đặt lịch cụ thể.
     *
     * @param parent  Cửa sổ cha (JFrame)
     * @param datLich Đối tượng {@link DatLich} cần thực hiện thanh toán
     */
    public ThanhToanDialog(JFrame parent, DatLich datLich) {
        super(parent, "Thanh toán phiếu đặt lịch", true);
        this.datLich = datLich;

        // Khởi tạo thành phần GUI do NetBeans sinh ra
        initComponents();
        // Cấu hình giao diện tùy chỉnh
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
        setTitle("Thanh toán phiếu đặt lịch");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblHeaderTitle.setFont(UIConstants.FONT_TITLE);
        lblHeaderTitle.setForeground(java.awt.Color.WHITE);
        lblHeaderTitle.setText("Thanh toán phiếu đặt lịch");
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

    /**
     * Khởi tạo chi tiết thông tin thanh toán, combobox hình thức và gán sự kiện.
     *
     * @param parent Cửa sổ cha dùng định vị hiển thị dialog
     */
    private void customInit(JFrame parent) {
        setSize(450, 360);
        if (parent != null) setLocationRelativeTo(parent);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tạo các nhãn hiển thị thông tin phiếu
        lblMaPhieu = new javax.swing.JLabel(datLich != null ? datLich.getMaLichDat() : "-");
        lblKhach = new javax.swing.JLabel(datLich != null ? datLich.getTenKhach() : "-");

        // Nhãn tổng số tiền cần thanh toán
        lblTongTien = new JLabel(datLich != null ? String.format("%,.0f VNĐ", (double) (datLich.getTongTien())) : "0 đ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongTien.setForeground(UIConstants.PRIMARY);

        // Combobox lựa chọn hình thức thanh toán
        cboHinhThuc = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản", "Thẻ"});
        styleCombo(cboHinhThuc);

        int row = 0;
        row = addField(pnlFormCard, gbc, row, "Mã phiếu:", lblMaPhieu);
        row = addField(pnlFormCard, gbc, row, "Khách hàng:", lblKhach);
        row = addField(pnlFormCard, gbc, row, "Tổng tiền thu:", lblTongTien);
        addField(pnlFormCard, gbc, row, "Hình thức *", cboHinhThuc);

        // Nút Hủy
        JButton btnCancel = new javax.swing.JButton("Hủy");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        // Nút Xác nhận Thanh toán
        JButton btnPay = new javax.swing.JButton("Thanh toán");
        btnPay.addActionListener(e -> {
            hinhThuc = (String) cboHinhThuc.getSelectedItem();
            confirmed = true;
            dispose();
        });

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnPay);

        getRootPane().setDefaultButton(btnPay);
    }

    /**
     * Thêm hàng gồm tiêu đề và thành phần hiển thị vào form GridBagLayout.
     *
     * @param form  Panel form
     * @param gbc   GridBagConstraints
     * @param row   Chỉ số hàng
     * @param label Nhãn văn bản
     * @param field Thành phần hiển thị
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
     * Định dạng kiểu phông chữ và màu sắc cho combobox.
     *
     * @param combo Combobox cần định dạng
     */
    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    /**
     * Trả về cờ xác nhận thanh toán của người dùng.
     *
     * @return {@code true} nếu đã bấm Thanh toán, {@code false} nếu Hủy
     */
    public boolean isConfirmed() { return confirmed; }

    /**
     * Lấy hình thức thanh toán được chọn.
     *
     * @return Chuỗi hình thức thanh toán (Tiền mặt / Chuyển khoản / Thẻ)
     */
    public String getHinhThuc() { return hinhThuc; }
}
