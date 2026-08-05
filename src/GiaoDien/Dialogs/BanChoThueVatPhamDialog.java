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
 * Tự động trừ số lượng tồn trong Kho hàng khi hoàn tất.
 */
public class BanChoThueVatPhamDialog extends JDialog {

    private JComboBox<DichVu> cboVatPham;
    private JComboBox<String> cboHinhThuc;
    private JTextField txtSoLuong;

    private DichVu selectedVatPham;
    private String hinhThuc;
    private int soLuong;
    private double tongTien;
    private boolean confirmed;

    public BanChoThueVatPhamDialog(JFrame parent) {
        super(parent, "Bán / Cho thuê vật phẩm Kho hàng", true);
        initUI(parent);
    }

    private void initUI(JFrame parent) {
        setSize(460, 360);
        setResizable(false);
        if (parent != null) setLocationRelativeTo(parent);

        setLayout(new BorderLayout());

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JLabel lblTitle = new JLabel("[+] Bán / Cho thuê vật phẩm Kho hàng");
        lblTitle.setFont(UIConstants.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        add(pnlHeader, BorderLayout.NORTH);

        // Center Form
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBackground(UIConstants.BG);
        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<DichVu> khoItems = DataStore.get().getKhoItems();
        cboVatPham = new JComboBox<>(khoItems.toArray(new DichVu[0]));
        styleCombo(cboVatPham);

        cboHinhThuc = new JComboBox<>(new String[]{"Bán vật phẩm", "Cho thuê vật phẩm"});
        styleCombo(cboHinhThuc);

        txtSoLuong = new JTextField(10);
        txtSoLuong.setText("1");
        txtSoLuong.setFont(UIConstants.FONT_NORMAL);

        int row = 0;
        row = addField(pnlCenter, gbc, row, "Chọn vật phẩm kho *", cboVatPham);
        row = addField(pnlCenter, gbc, row, "Hình thức *", cboHinhThuc);
        addField(pnlCenter, gbc, row, "Số lượng *", txtSoLuong);

        add(pnlCenter, BorderLayout.CENTER);

        // Footer
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pnlFooter.setBackground(UIConstants.BG);

        JButton btnCancel = new JButton("Hủy");
        Utils.PageUI.styleSecondaryButton(btnCancel);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnSubmit = new JButton("Xác nhận Bán / Cho thuê");
        Utils.PageUI.stylePrimaryButton(btnSubmit);
        btnSubmit.addActionListener(e -> onConfirm());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSubmit);
        add(pnlFooter, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnSubmit);
    }

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

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    private void onConfirm() {
        selectedVatPham = (DichVu) cboVatPham.getSelectedItem();
        if (selectedVatPham == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vật phẩm trong kho.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        hinhThuc = (String) cboHinhThuc.getSelectedItem();
        String slStr = txtSoLuong.getText().trim();

        try {
            soLuong = Integer.parseInt(slStr);
            if (soLuong <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương (> 0).", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate warehouse stock
        if (soLuong > selectedVatPham.getSoLuongTon()) {
            JOptionPane.showMessageDialog(this,
                    "KHO HÀNG KHÔNG ĐỦ VẬT PHẨM!\n"
                            + "• Yêu cầu: " + soLuong + "\n"
                            + "• Tồn kho hiện tại: " + selectedVatPham.getSoLuongTon() + " " + selectedVatPham.getDonVi() + "\n"
                            + "Vui lòng nhập thêm kho hàng trước khi thực hiện.",
                    "Cảnh báo kho hàng", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Deduct from warehouse
        selectedVatPham.xuatKho(soLuong);
        tongTien = selectedVatPham.getDonGia() * soLuong;
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() { return confirmed; }
    public DichVu getSelectedVatPham() { return selectedVatPham; }
    public String getHinhThuc() { return hinhThuc; }
    public int getSoLuong() { return soLuong; }
    public double getTongTien() { return tongTien; }
}
