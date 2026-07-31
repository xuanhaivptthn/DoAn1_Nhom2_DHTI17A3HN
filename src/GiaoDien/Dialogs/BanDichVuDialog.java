package GiaoDien.Dialogs;

import GiaoDien.Panels.*;

import Model.DatLich;
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
import java.util.List;

/**
 * Dialog bán dịch vụ đi kèm cho lịch đặt sân.
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class BanDichVuDialog extends JDialog {

    private JComboBox<DichVu> cboDichVu;
    private JTextField txtSoLuong;

    private DatLich datLich;
    private DichVu selectedDichVu;
    private int soLuong;
    private boolean confirmed;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblHeaderTitle;
    private javax.swing.JPanel pnlCenterWrap;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables

    public BanDichVuDialog() {
        this(null, null);
    }

    public BanDichVuDialog(JFrame parent, DatLich datLich) {
        super(parent, "Bán dịch vụ đi kèm", true);
        this.datLich = datLich;

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
        setTitle("Bán dịch vụ đi kèm");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblHeaderTitle.setFont(UIConstants.FONT_TITLE);
        lblHeaderTitle.setForeground(java.awt.Color.WHITE);
        lblHeaderTitle.setText("Bán dịch vụ đi kèm");
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
        setSize(440, 320);
        if (parent != null) setLocationRelativeTo(parent);

        if (datLich != null) {
            lblHeaderTitle.setText("Bán dịch vụ — " + datLich.getMaLichDat());
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<DichVu> selling = new java.util.ArrayList<>();
        selling.addAll(DataStore.get().getKhoItems());
        selling.addAll(DataStore.get().getDichVus());
        cboDichVu = new JComboBox<>(selling.toArray(new DichVu[0]));
        styleCombo(cboDichVu);

        int row = 0;
        row = addField(pnlFormCard, gbc, row, "Chọn dịch vụ *", cboDichVu);

        txtSoLuong = new javax.swing.JTextField(10);
        txtSoLuong.setText("1");
        addField(pnlFormCard, gbc, row, "Số lượng *", txtSoLuong);

        JButton btnCancel = new javax.swing.JButton("Hủy");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnSave = new javax.swing.JButton("Thêm dịch vụ");
        btnSave.addActionListener(e -> onSave());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSave);

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

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    private void onSave() {
        selectedDichVu = (DichVu) cboDichVu.getSelectedItem();
        if (selectedDichVu == null) {
            JOptionPane.showMessageDialog(this, "Chưa chọn dịch vụ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() { return confirmed; }
    public DichVu getSelectedDichVu() { return selectedDichVu; }
    public int getSoLuong() { return soLuong; }
}
