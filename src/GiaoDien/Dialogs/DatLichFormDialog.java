package GiaoDien.Dialogs;

import GiaoDien.Panels.*;

import Model.DatLich;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog tạo mới / cập nhật lịch đặt sân bóng.
 * Hỗ trợ tạo nhanh lịch đặt khi bấm vào ô khung giờ trống trên Ma trận đặt sân.
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class DatLichFormDialog extends JDialog {

    private JComboBox<KhuVucSan> cboSan;
    private JTextField txtTenKhach;
    private JTextField txtSoDienThoai;
    private JTextField txtNgayDat;
    private JButton btnPickDate;
    private JTextField txtGioBatDau;
    private JTextField txtGioKetThuc;
    private JTextField txtGhiChu;

    // Config Dịch vụ / Đồ ăn kèm
    private JButton btnConfigDichVuDoAn;
    private JTextArea txtSummaryDichVuDoAn;
    private final List<ChonDichVuDoAnDialog.SelectedItem> configuredAddons = new ArrayList<>();
    private double addonTotalCost = 0;

    private boolean isEdit;
    private DatLich original;
    private DatLich result;
    private boolean confirmed;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblHeaderTitle;
    private javax.swing.JPanel pnlCenterWrap;
    private javax.swing.JPanel pnlFooter;
    private javax.swing.JPanel pnlFormCard;
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables

    public DatLichFormDialog() {
        this(null, null);
    }

    public DatLichFormDialog(JFrame parent, DatLich existing) {
        super(parent, existing == null ? "Tạo mới phiếu đặt lịch" : "Cập nhật phiếu đặt lịch", true);
        this.isEdit = existing != null;
        this.original = existing;

        initComponents();
        customInit(parent);
    }

    public DatLichFormDialog(JFrame parent, KhuVucSan preSelectedCourt, String dateStr, String startTimeStr, String endTimeStr) {
        super(parent, "Tạo mới phiếu đặt lịch sân trống", true);
        this.isEdit = false;
        this.original = null;

        initComponents();
        customInit(parent);

        if (preSelectedCourt != null) {
            cboSan.setSelectedItem(preSelectedCourt);
        }
        if (dateStr != null && !dateStr.isBlank()) {
            txtNgayDat.setText(dateStr);
        }
        if (startTimeStr != null && !startTimeStr.isBlank()) {
            txtGioBatDau.setText(startTimeStr);
        }
        if (endTimeStr != null && !endTimeStr.isBlank()) {
            txtGioKetThuc.setText(endTimeStr);
        }
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
        setTitle("Thông tin phiếu đặt lịch");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblHeaderTitle.setFont(UIConstants.FONT_TITLE);
        lblHeaderTitle.setForeground(java.awt.Color.WHITE);
        lblHeaderTitle.setText("Thông tin phiếu đặt lịch");
        pnlHeader.add(lblHeaderTitle, java.awt.BorderLayout.WEST);

        getContentPane().add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlCenterWrap.setBackground(UIConstants.BG);
        pnlCenterWrap.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 8, 16));
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
        setSize(560, 680);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Cập nhật phiếu đặt lịch" : "[+] Tạo mới phiếu đặt lịch");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<KhuVucSan> sans = DataStore.get().getKhuVucs();
        cboSan = new JComboBox<>(sans.toArray(new KhuVucSan[0]));
        styleCombo(cboSan);

        int row = 0;
        row = addField(pnlFormCard, gbc, row, "Khu vực sân *", cboSan);

        txtTenKhach = new javax.swing.JTextField(18);
        row = addField(pnlFormCard, gbc, row, "Tên khách hàng *", txtTenKhach);

        txtSoDienThoai = new javax.swing.JTextField(11);
        JButton btnQuickCustomer = new JButton("🔍 Khách quen");
        btnQuickCustomer.setPreferredSize(new Dimension(105, 34));
        btnQuickCustomer.addActionListener(e -> onPickQuickCustomer());

        JPanel pnlPhoneWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlPhoneWrapper.setOpaque(false);
        txtSoDienThoai.setPreferredSize(new Dimension(130, 34));
        pnlPhoneWrapper.add(txtSoDienThoai);
        pnlPhoneWrapper.add(btnQuickCustomer);

        txtSoDienThoai.getDocument().addDocumentListener(new Utils.SimpleDocListener(this::onPhoneAutoLookup));

        row = addField(pnlFormCard, gbc, row, "Số điện thoại", pnlPhoneWrapper);

        // DATE PICKER COMPONENT
        txtNgayDat = new javax.swing.JTextField(11);
        btnPickDate = new JButton("Chọn ngày");
        btnPickDate.addActionListener(e -> onOpenDatePicker());

        JPanel pnlDateChooser = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pnlDateChooser.setOpaque(false);
        txtNgayDat.setPreferredSize(new Dimension(130, 34));
        btnPickDate.setPreferredSize(new Dimension(105, 34));
        pnlDateChooser.add(txtNgayDat);
        pnlDateChooser.add(btnPickDate);

        row = addField(pnlFormCard, gbc, row, "Ngày đặt sân *", pnlDateChooser);

        txtGioBatDau = new javax.swing.JTextField(18);
        row = addField(pnlFormCard, gbc, row, "Giờ bắt đầu (HH:mm) *", txtGioBatDau);

        txtGioKetThuc = new javax.swing.JTextField(18);
        row = addField(pnlFormCard, gbc, row, "Giờ kết thúc (HH:mm) *", txtGioKetThuc);

        txtGhiChu = new javax.swing.JTextField(18);
        row = addField(pnlFormCard, gbc, row, "Ghi chú", txtGhiChu);

        // CONFIG BUTTON & SUMMARY DISPLAY
        btnConfigDichVuDoAn = new JButton("Thêm Dịch vụ & Đồ ăn");
        btnConfigDichVuDoAn.setFont(UIConstants.FONT_BOLD);
        btnConfigDichVuDoAn.setPreferredSize(new Dimension(240, 36));
        btnConfigDichVuDoAn.addActionListener(e -> onOpenConfigDialog());

        row = addField(pnlFormCard, gbc, row, "Thêm Dịch vụ & Đồ ăn", btnConfigDichVuDoAn);

        txtSummaryDichVuDoAn = new JTextArea(4, 20);
        txtSummaryDichVuDoAn.setEditable(false);
        txtSummaryDichVuDoAn.setLineWrap(true);
        txtSummaryDichVuDoAn.setFont(UIConstants.FONT_SMALL);
        txtSummaryDichVuDoAn.setText("Bấm nút [+ Thêm Dịch vụ & Đồ ăn] ở trên để chọn Dịch vụ & Đồ ăn.");
        JScrollPane spSummary = new JScrollPane(txtSummaryDichVuDoAn);
        spSummary.setPreferredSize(new Dimension(240, 80));

        addField(pnlFormCard, gbc, row, "Tổng hợp", spSummary);

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
            txtNgayDat.setText(LocalDate.now().toString());
            txtGioBatDau.setText("18:00");
            txtGioKetThuc.setText("19:00");
        }

        getRootPane().setDefaultButton(btnSave);
    }

    private void onOpenDatePicker() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        LocalDate initDate = LocalDate.now();
        try {
            if (!txtNgayDat.getText().isBlank()) {
                initDate = LocalDate.parse(txtNgayDat.getText().trim());
            }
        } catch (Exception ignored) {}

        ChonNgayDialog dialog = new ChonNgayDialog(parent, initDate);
        dialog.setVisible(true);

        if (dialog.isConfirmed() && dialog.getSelectedDate() != null) {
            txtNgayDat.setText(dialog.getSelectedDate().toString());
        }
    }

    private final java.util.Map<Integer, Integer> currentDvMap = new java.util.HashMap<>();
    private final java.util.Map<Integer, Integer> currentDoAnMap = new java.util.HashMap<>();

    private void onOpenConfigDialog() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        ChonDichVuDoAnDialog dialog = new ChonDichVuDoAnDialog(parent);
        dialog.setInitialQuantities(currentDvMap, currentDoAnMap);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            configuredAddons.clear();
            configuredAddons.addAll(dialog.getSelectedServices());
            configuredAddons.addAll(dialog.getSelectedFoodItems());
            addonTotalCost = dialog.getTotalAddonCost();

            currentDvMap.clear();
            currentDvMap.putAll(dialog.getSelectedQtyMapDichVu());
            currentDoAnMap.clear();
            currentDoAnMap.putAll(dialog.getSelectedQtyMapDoAn());

            txtSummaryDichVuDoAn.setText(dialog.getSummaryText());
            txtSummaryDichVuDoAn.setCaretPosition(0);
        }
    }

    private int addField(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        gbc.gridwidth = 1;
        form.add(new javax.swing.JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        if (!(field instanceof JPanel) && !(field instanceof JScrollPane)) {
            field.setPreferredSize(new Dimension(240, 36));
        }
        form.add(field, gbc);
        return row + 1;
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    private void fillForm(DatLich d) {
        for (int i = 0; i < cboSan.getItemCount(); i++) {
            KhuVucSan k = cboSan.getItemAt(i);
            if (k.getId() == d.getKhuVucId()) {
                cboSan.setSelectedIndex(i);
                break;
            }
        }
        cboSan.setEnabled(true);
        txtTenKhach.setText(d.getTenKhach());
        txtSoDienThoai.setText(d.getSoDienThoai());
        txtNgayDat.setText(d.getNgayDat());
        txtGioBatDau.setText(d.getGioBatDau());
        txtGioKetThuc.setText(d.getGioKetThuc());
        txtGhiChu.setText(d.getGhiChu());

        if (d.getSelectedDvMap() != null) {
            currentDvMap.putAll(d.getSelectedDvMap());
        }
        if (d.getSelectedDoAnMap() != null) {
            currentDoAnMap.putAll(d.getSelectedDoAnMap());
        }

        if (d.getDichVuKem() != null && !d.getDichVuKem().isBlank()) {
            txtSummaryDichVuDoAn.setText("📋 DỊCH VỤ KÈM HIỆN CÓ:\n" + d.getDichVuKem());
        }
    }

    private void onSave() {
        KhuVucSan san = (KhuVucSan) cboSan.getSelectedItem();
        String tk = txtTenKhach.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String ng = txtNgayDat.getText().trim();
        String bd = txtGioBatDau.getText().trim();
        String kt = txtGioKetThuc.getText().trim();

        if (san == null || tk.isEmpty() || ng.isEmpty() || bd.isEmpty() || kt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bMin = toMinutes(bd);
        int kMin = toMinutes(kt);
        if (bMin >= kMin || bMin == 0 || kMin == 0) {
            JOptionPane.showMessageDialog(this,
                    "Giờ bắt đầu và giờ kết thúc không hợp lệ (ví dụ đúng: 06:00 đến 07:30 hoặc 17:00 đến 18:30).",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int minAllowed = 5 * 60;        // 05:00 sáng
        int maxAllowed = 23 * 60 + 30;  // 23:30 đêm
        if (bMin < minAllowed || kMin > maxAllowed) {
            JOptionPane.showMessageDialog(this,
                    "[!] KHUNG GIỜ PHỤC VỤ SÂN BÓNG:\n"
                            + "Sân bóng mở cửa phục vụ đặt sân từ 05:00 sáng đến 23:00 đêm.\n"
                            + "Vui lòng nhập khung giờ đặt trong khoảng 05:00 - 23:30!",
                    "Khung giờ phục vụ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // KIỂM TRA SÂN ĐANG BẢO TRÌ
        if ("BaoTri".equalsIgnoreCase(san.getTrangThai())) {
            JOptionPane.showMessageDialog(this,
                    "[!] KHÔNG THỂ ĐẶT SÂN ĐANG BẢO TRÌ!\n\n"
                            + "• Sân bóng   : " + san.getTenSan() + "\n"
                            + "• Trạng thái : Đang bảo trì cơ sở vật chất\n\n"
                            + "Sân này đang tạm dừng hoạt động để bảo trì. Vui lòng chọn sân khác!",
                    "Cảnh báo bảo trì sân bóng", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // KIỂM TRA PHIẾU BẢO TRÌ HOẠT ĐỘNG TRÊN SÂN
        Model.BaoTri activeMaint = DataStore.get().getBaoTris().stream()
                .filter(b -> b.getKhuVucId() == san.getId()
                        && !"HoanThanh".equalsIgnoreCase(b.getTrangThai())
                        && !"DaHuy".equalsIgnoreCase(b.getTrangThai()))
                .findFirst().orElse(null);

        if (activeMaint != null) {
            JOptionPane.showMessageDialog(this,
                    "[!] KHÔNG THỂ ĐẶT SÂN ĐANG CÓ LỊCH BẢO TRÌ!\n\n"
                            + "• Sân bóng     : " + san.getTenSan() + "\n"
                            + "• Mã phiếu BT  : " + activeMaint.getMaPhieu() + "\n"
                            + "• Nội dung BT  : " + activeMaint.getNoiDung() + "\n"
                            + "• Thời gian BT : " + activeMaint.getNgayBatDau() + " ➔ " + activeMaint.getNgayKetThuc() + "\n\n"
                            + "Sân bóng này đang có lịch bảo trì cơ sở vật chất. Vui lòng chọn sân khác!",
                    "Cảnh báo bảo trì sân bóng", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // KIỂM TRA TRÙNG LỊCH ĐẶT SÂN
        DatLich conflict = findOverlapBooking(san.getId(), ng, bd, kt, isEdit ? original.getId() : 0);
        if (conflict != null) {
            JOptionPane.showMessageDialog(this,
                    "[!] CẢNH BÁO TRÙNG LỊCH ĐẶT SÂN!\n\n"
                            + "• Sân        : " + san.getTenSan() + "\n"
                            + "• Ngày đặt   : " + ng + "\n"
                            + "• Khung giờ  : " + bd + " - " + kt + "\n\n"
                            + "Đã trùng lịch với phiếu đặt sân hiện có:\n"
                            + "  - Mã phiếu : " + conflict.getMaPhieu() + "\n"
                            + "  - Khách    : " + conflict.getTenKhach() + " (" + conflict.getSoDienThoai() + ")\n"
                            + "  - Giờ đặt  : " + conflict.getKhungGio() + " [" + conflict.getTrangThaiHienThi() + "]\n\n"
                            + "Vui lòng chọn khung giờ khác hoặc chọn sân khác!",
                    "Cảnh báo trùng lịch đặt sân", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double initialCourtPrice = isEdit ? original.getTienSan() : san.getGiaTheoGio();

        result = new DatLich(isEdit ? original.getId() : 0,
                isEdit ? original.getMaPhieu() : "",
                san.getId(), san.getTenSan(), tk, sdt, ng, bd, kt,
                initialCourtPrice,
                isEdit ? original.getTrangThai() : "ChoXacNhan",
                isEdit ? original.getNhanVienLap() : "Hệ thống",
                txtGhiChu.getText().trim());

        result.setSelectedDvMap(currentDvMap);
        result.setSelectedDoAnMap(currentDoAnMap);

        // LƯU/CẬP NHẬT THÔNG TIN KHÁCH HÀNG VÀO CSDL ĐỂ TĂNG TỐC ĐẶT SÂN
        DataStore.get().saveOrUpdateKhachHang(tk, sdt, "", txtGhiChu.getText().trim());

        confirmed = true;
        dispose();
    }

    private void onPhoneAutoLookup() {
        String sdt = txtSoDienThoai.getText().trim();
        if (sdt.length() >= 9) {
            Model.KhachHang kh = DataStore.get().findKhachHangBySoDienThoai(sdt);
            if (kh != null && (txtTenKhach.getText().isBlank() || !txtTenKhach.getText().equals(kh.getHoTen()))) {
                txtTenKhach.setText(kh.getHoTen());
            }
        }
    }

    private void onPickQuickCustomer() {
        List<Model.KhachHang> khs = DataStore.get().getKhachHangs();
        if (khs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có dữ liệu khách hàng cũ.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Model.KhachHang selected = (Model.KhachHang) JOptionPane.showInputDialog(
                this,
                "Chọn khách hàng quen để điền nhanh thông tin:",
                "Khách hàng đã lưu CSDL",
                JOptionPane.QUESTION_MESSAGE,
                null,
                khs.toArray(),
                null
        );

        if (selected != null) {
            txtTenKhach.setText(selected.getHoTen());
            txtSoDienThoai.setText(selected.getSoDienThoai());
            if (selected.getGhiChu() != null && !selected.getGhiChu().isBlank() && txtGhiChu.getText().isBlank()) {
                txtGhiChu.setText(selected.getGhiChu());
            }
        }
    }

    public static DatLich findOverlapBooking(int sanId, String ngayDat, String gioBD, String gioKT, int excludeId) {
        int newStart = toMinutes(gioBD);
        int newEnd = toMinutes(gioKT);

        if (newStart >= newEnd) return null;

        for (DatLich existing : DataStore.get().getDatLichs()) {
            if (existing.getId() == excludeId) continue;
            if (existing.getKhuVucId() != sanId) continue;
            if (!existing.getNgayDat().trim().equalsIgnoreCase(ngayDat.trim())) continue;
            if ("DaHuy".equalsIgnoreCase(existing.getTrangThai())) continue;

            int exStart = toMinutes(existing.getGioBatDau());
            int exEnd = toMinutes(existing.getGioKetThuc());

            if (newStart < exEnd && newEnd > exStart) {
                return existing;
            }
        }
        return null;
    }

    private static int toMinutes(String timeStr) {
        if (timeStr == null || !timeStr.contains(":")) return 0;
        try {
            String[] parts = timeStr.trim().split(":");
            return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isConfirmed() { return confirmed; }
    public DatLich getResult() { return result; }
    public KhuVucSan getSelectedSan() { return (KhuVucSan) cboSan.getSelectedItem(); }
}
