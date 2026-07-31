package GiaoDien.Dialogs;

import GiaoDien.Panels.*;

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
import java.util.List;

/**
 * Dialog kiểm tra sân khả dụng.
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class KiemTraSanDialog extends JDialog {

    private JComboBox<KhuVucSan> cboSan;
    private JTextField txtNgay;
    private JTextField txtGioBatDau;
    private JTextField txtGioKetThuc;

    private KhuVucSan selectedSan;
    private String ngay;
    private String gioBatDau;
    private String gioKetThuc;
    private boolean confirmed;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblHeaderTitle;
    private javax.swing.JPanel pnlCenterWrap;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables

    public KiemTraSanDialog() {
        this(null);
    }

    public KiemTraSanDialog(JFrame parent) {
        super(parent, "Kiểm tra sân khả dụng", true);

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
        setTitle("Kiểm tra sân khả dụng");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblHeaderTitle.setFont(UIConstants.FONT_TITLE);
        lblHeaderTitle.setForeground(java.awt.Color.WHITE);
        lblHeaderTitle.setText("Kiểm tra sân khả dụng");
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
        setSize(450, 380);
        if (parent != null) setLocationRelativeTo(parent);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<KhuVucSan> sans = DataStore.get().getKhuVucsKhongBaoTri();
        cboSan = new JComboBox<>(sans.toArray(new KhuVucSan[0]));
        styleCombo(cboSan);

        int row = 0;
        row = addField(pnlFormCard, gbc, row, "Khu vực sân *", cboSan);

        txtNgay = new javax.swing.JTextField();
        txtNgay.setText(java.time.LocalDate.now().toString());
        JPanel pnlNgay = createDatePickerPanel(txtNgay, (JFrame) getOwner());
        row = addField(pnlFormCard, gbc, row, "Ngày kiểm tra *", pnlNgay);

        txtGioBatDau = new javax.swing.JTextField(16);
        txtGioBatDau.setText("18:00");
        row = addField(pnlFormCard, gbc, row, "Giờ bắt đầu *", txtGioBatDau);

        txtGioKetThuc = new javax.swing.JTextField(16);
        txtGioKetThuc.setText("19:00");
        addField(pnlFormCard, gbc, row, "Giờ kết thúc *", txtGioKetThuc);

        JButton btnCancel = new javax.swing.JButton("Đóng");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnCheck = new javax.swing.JButton("Kiểm tra");
        btnCheck.addActionListener(e -> onCheck());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnCheck);

        getRootPane().setDefaultButton(btnCheck);
    }

    private int addField(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.38;
        gbc.gridwidth = 1;
        form.add(new javax.swing.JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.62;
        field.setPreferredSize(new Dimension(220, 36));
        form.add(field, gbc);
        return row + 1;
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    private void onCheck() {
        selectedSan = (KhuVucSan) cboSan.getSelectedItem();
        ngay = txtNgay.getText().trim();
        gioBatDau = txtGioBatDau.getText().trim();
        gioKetThuc = txtGioKetThuc.getText().trim();

        if (selectedSan == null || ngay.isEmpty() || gioBatDau.isEmpty() || gioKetThuc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() { return confirmed; }
    public KhuVucSan getSelectedSan() { return selectedSan; }
    public String getNgay() { return ngay; }
    public String getGioBatDau() { return gioBatDau; }
    public String getGioKetThuc() { return gioKetThuc; }

    private JPanel createDatePickerPanel(JTextField txtField, JFrame parent) {
        JPanel pnl = new JPanel(new BorderLayout(4, 0));
        pnl.setOpaque(false);
        txtField.setPreferredSize(new Dimension(190, 36));
        txtField.setFont(UIConstants.FONT_NORMAL);

        JButton btnPicker = new JButton();
        btnPicker.setIcon(Utils.IconUtils.getCalendarIcon(16));
        btnPicker.setPreferredSize(new Dimension(44, 36));
        btnPicker.addActionListener(e -> openDatePickerFor(txtField, parent));

        txtField.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                openDatePickerFor(txtField, parent);
            }
        });

        pnl.add(txtField, BorderLayout.CENTER);
        pnl.add(btnPicker, BorderLayout.EAST);
        return pnl;
    }

    private void openDatePickerFor(JTextField txtField, JFrame parent) {
        java.time.LocalDate initDate = java.time.LocalDate.now();
        try {
            if (!txtField.getText().isBlank()) {
                initDate = java.time.LocalDate.parse(txtField.getText().trim());
            }
        } catch (Exception ignored) {}

        ChonNgayDialog dialog = new ChonNgayDialog(parent, initDate);
        dialog.setVisible(true);
        if (dialog.isConfirmed() && dialog.getSelectedDate() != null) {
            txtField.setText(dialog.getSelectedDate().toString());
        }
    }
}
