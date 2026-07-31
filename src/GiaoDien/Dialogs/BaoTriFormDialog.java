package GiaoDien.Dialogs;

import GiaoDien.Panels.*;

import Model.BaoTri;
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
 * Dialog lập / cập nhật phiếu bảo trì cơ sở vật chất.
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class BaoTriFormDialog extends JDialog {

    private JComboBox<KhuVucSan> cboSan;
    private JTextField txtNoiDung;
    private JTextField txtNgayBatDau;
    private JTextField txtNgayKetThuc;
    private JComboBox<String> cboTrangThai;

    private boolean isEdit;
    private BaoTri original;
    private BaoTri result;
    private boolean confirmed;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblHeaderTitle;
    private javax.swing.JPanel pnlCenterWrap;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables

    public BaoTriFormDialog() {
        this(null, null);
    }

    public BaoTriFormDialog(JFrame parent, BaoTri existing) {
        super(parent, existing == null ? "Lập phiếu bảo trì" : "Cập nhật phiếu bảo trì", true);
        this.isEdit = existing != null;
        this.original = existing;

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
        setTitle("Thông tin phiếu bảo trì");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblHeaderTitle.setFont(UIConstants.FONT_TITLE);
        lblHeaderTitle.setForeground(java.awt.Color.WHITE);
        lblHeaderTitle.setText("🔧 Thông tin phiếu bảo trì");
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
        setSize(480, 540);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Cập nhật phiếu bảo trì" : "[+] Lập phiếu bảo trì mới");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<KhuVucSan> sans = DataStore.get().getKhuVucs();
        cboSan = new JComboBox<>(sans.toArray(new KhuVucSan[0]));
        styleCombo(cboSan);

        int row = 0;
        row = addField(pnlFormCard, gbc, row, "Khu vực sân *", cboSan);

        txtNoiDung = new javax.swing.JTextField(18);
        row = addField(pnlFormCard, gbc, row, "Nội dung bảo trì *", txtNoiDung);

        txtNgayBatDau = new javax.swing.JTextField(18);
        row = addField(pnlFormCard, gbc, row, "Ngày bắt đầu *", txtNgayBatDau);

        txtNgayKetThuc = new javax.swing.JTextField(18);
        row = addField(pnlFormCard, gbc, row, "Ngày kết thúc", txtNgayKetThuc);

        cboTrangThai = new JComboBox<>(new String[]{"Đang bảo trì", "Hoàn thành", "Đã hủy"});
        styleCombo(cboTrangThai);
        addField(pnlFormCard, gbc, row, "Trạng thái BT", cboTrangThai);

        JButton btnCancel = new javax.swing.JButton("Hủy");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnSave = new javax.swing.JButton(isEdit ? "Cập nhật" : "Lưu phiếu");
        btnSave.addActionListener(e -> onSave());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSave);

        if (isEdit && original != null) {
            fillForm(original);
        } else {
            txtNgayBatDau.setText(java.time.LocalDate.now().toString());
        }

        getRootPane().setDefaultButton(btnSave);
    }

    private int addField(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.38;
        gbc.gridwidth = 1;
        form.add(new javax.swing.JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.62;
        field.setPreferredSize(new Dimension(240, 36));
        form.add(field, gbc);
        return row + 1;
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    private void fillForm(BaoTri b) {
        for (int i = 0; i < cboSan.getItemCount(); i++) {
            KhuVucSan k = cboSan.getItemAt(i);
            if (k.getMaSan() != null && k.getMaSan().equals(b.getMaSan())) {
                cboSan.setSelectedIndex(i);
                break;
            }
        }
        cboSan.setEnabled(false);
        txtNoiDung.setText(b.getNoiDung());
        txtNgayBatDau.setText(b.getNgayBatDau());
        txtNgayKetThuc.setText(b.getNgayKetThuc() == null ? "" : b.getNgayKetThuc());
        cboTrangThai.setSelectedItem(b.getTrangThaiHienThi());
    }

    private void onSave() {
        KhuVucSan san = (KhuVucSan) cboSan.getSelectedItem();
        String nd = txtNoiDung.getText().trim();
        String nbd = txtNgayBatDau.getText().trim();

        if (san == null || nd.isEmpty() || nbd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nkt = txtNgayKetThuc.getText().trim();

        // KIỂM TRA LỊCH ĐẶT SÂN ĐÃ CÓ TRONG THỜI GIAN BẢO TRÌ NÀY
        List<Model.DatLich> conflictingBookings = DataStore.get().getDatLichs().stream()
                .filter(d -> san.getMaSan() != null && san.getMaSan().equals(d.getMaSan())
                        && !"DaHuy".equalsIgnoreCase(d.getTrangThai())
                        && isDateInMaintenanceRange(d.getNgayDat(), nbd, nkt))
                .toList();

        if (!conflictingBookings.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[!] CẢNH BÁO: ĐÃ CÓ LỊCH ĐẶT SÂN TRONG NGÀY BẢO TRÌ!\n");
            sb.append("--------------------------------------------------------\n");
            sb.append("Sân bóng     : ").append(san.getTenSan()).append("\n");
            sb.append("Thời gian BT : ").append(nbd);
            if (!nkt.isBlank()) {
                sb.append(" ➔ ").append(nkt);
            }
            sb.append("\n\nDanh sách khách hàng cần liên hệ để dời/đổi lịch sân:\n\n");

            int idx = 1;
            for (Model.DatLich d : conflictingBookings) {
                sb.append(idx++).append(". Phiếu: ").append(d.getMaLichDat()).append("\n");
                sb.append("   • Khách hàng : ").append(d.getTenKhach()).append("\n");
                sb.append("   • SĐT liên hệ: ").append(d.getSoDienThoaiKhach()).append("\n");
                sb.append("   • Ngày đặt   : ").append(d.getNgayDat()).append(" (").append(d.getKhungGio()).append(")\n");
                sb.append("   • Trạng thái : ").append(d.getTrangThaiHienThi()).append("\n");
                sb.append("   ------------------------------------\n");
            }
            sb.append("\n📌 Vui lòng liên hệ trực tiếp với khách hàng theo SĐT trên để dời/đổi lịch!");

            JOptionPane.showMessageDialog(this, sb.toString(), "Cảnh báo trùng lịch đặt sân", JOptionPane.WARNING_MESSAGE);
        }

        String trangThaiPhieu = switch ((String) cboTrangThai.getSelectedItem()) {
            case "Hoàn thành" -> "HOAN_THANH";
            case "Đã hủy" -> "HUY";
            default -> "DANG_BAO_TRI"; // "Đang bảo trì"
        };

        result = new BaoTri();
        result.setMaPhieuBaoTri(isEdit ? original.getMaPhieuBaoTri() : "");
        result.setMaSan(san.getMaSan());
        result.setTenSan(san.getTenSan());
        result.setNoiDung(nd);
        result.setNgayBatDau(nbd);
        result.setNgayKetThuc(nkt);
        result.setTrangThaiPhieu(trangThaiPhieu);

        confirmed = true;
        dispose();
    }

    private boolean isDateInMaintenanceRange(String bookingDateStr, String startDateStr, String endDateStr) {
        if (bookingDateStr == null || bookingDateStr.isBlank()) return false;
        if (startDateStr == null || startDateStr.isBlank()) return false;

        try {
            java.time.LocalDate bDate = java.time.LocalDate.parse(bookingDateStr.trim());
            java.time.LocalDate sDate = java.time.LocalDate.parse(startDateStr.trim());
            java.time.LocalDate eDate = (endDateStr != null && !endDateStr.isBlank())
                    ? java.time.LocalDate.parse(endDateStr.trim())
                    : sDate;

            return (!bDate.isBefore(sDate)) && (!bDate.isAfter(eDate));
        } catch (Exception e) {
            return bookingDateStr.trim().equalsIgnoreCase(startDateStr.trim())
                    || (endDateStr != null && bookingDateStr.trim().equalsIgnoreCase(endDateStr.trim()));
        }
    }

    public boolean isConfirmed() { return confirmed; }
    public BaoTri getResult() { return result; }
    public KhuVucSan getSelectedSan() { return (KhuVucSan) cboSan.getSelectedItem(); }

    public void setSelectedSan(KhuVucSan targetSan) {
        if (targetSan == null || cboSan == null) return;
        for (int i = 0; i < cboSan.getItemCount(); i++) {
            KhuVucSan k = cboSan.getItemAt(i);
            if (k != null && k.getMaSan() != null && k.getMaSan().equalsIgnoreCase(targetSan.getMaSan())) {
                cboSan.setSelectedIndex(i);
                break;
            }
        }
    }
}
