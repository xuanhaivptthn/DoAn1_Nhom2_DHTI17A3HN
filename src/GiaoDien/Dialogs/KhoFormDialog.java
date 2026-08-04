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
 * Dialog thêm / sửa mặt hàng trong Kho.
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class KhoFormDialog extends JDialog {

    private JTextField txtTenHangHoa;
    private JTextField txtSoLuongTon;
    private JTextField txtDonGia;
    private JTextField txtNhaCungCap;

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

    public KhoFormDialog() {
        this(null, null);
    }

    public KhoFormDialog(JFrame parent, DichVu existing) {
        super(parent, existing == null ? "Thêm mặt hàng kho mới" : "Sửa mặt hàng kho", true);
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

    private void customInit(JFrame parent) {
        setSize(460, 360);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Sửa mặt hàng kho" : "Thêm mặt hàng kho mới");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        txtTenHangHoa = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Tên hàng hóa *", txtTenHangHoa);

        txtSoLuongTon = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Số lượng tồn *", txtSoLuongTon);

        txtDonGia = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Đơn giá (VNĐ) *", txtDonGia);

        txtNhaCungCap = new javax.swing.JTextField(16);
        row = addField(pnlFormCard, gbc, row, "Nhà cung cấp", txtNhaCungCap);

        JButton btnCancel = new javax.swing.JButton("Hủy");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnSave = new javax.swing.JButton(isEdit ? "Lưu thay đổi" : "Lưu");
        btnSave.setFont(UIConstants.FONT_BOLD);
        btnSave.addActionListener(e -> onSave());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSave);

        if (isEdit && original != null) {
            fillForm(original);
        } else {
            txtSoLuongTon.setText("0");
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
        txtTenHangHoa.setText(d.getTenHangHoa());
        txtSoLuongTon.setText(String.valueOf(d.getSoLuongTon()));
        txtDonGia.setText(String.valueOf((long) d.getDonGia()));
        txtNhaCungCap.setText(d.getNhaCungCap());
    }

    private void onSave() {
        String ten = txtTenHangHoa.getText().trim();
        String giaStr = txtDonGia.getText().trim().replace(",", "").replace(".", "");
        String slStr = txtSoLuongTon.getText().trim();
        String ncc = txtNhaCungCap.getText().trim();

        if (ten.isEmpty() || ten.length() < 2) {
            JOptionPane.showMessageDialog(this, "Tên hàng hóa không hợp lệ! Vui lòng nhập từ 2 ký tự trở lên.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTenHangHoa.requestFocus();
            return;
        }

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

        result = new DichVu(isEdit ? original.getId() : 0, ten, ncc.isEmpty() ? "Tổng kho Sân bóng" : ncc, price,
                isEdit ? original.getDonVi() : "cái", "DangBan", tonVal, 5);
        result.setNhaCungCap(ncc.isEmpty() ? "Tổng kho Sân bóng" : ncc);
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() { return confirmed; }
    public DichVu getResult() { return result; }
}
