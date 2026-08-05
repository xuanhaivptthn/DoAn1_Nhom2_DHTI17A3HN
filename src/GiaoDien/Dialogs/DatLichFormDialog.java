package GiaoDien.Dialogs;

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
    private JComboBox<String> cboGioBatDau;
    private JComboBox<String> cboGioKetThuc;
    private JTextField txtGhiChu;

    // Config Dịch vụ / Đồ ăn kèm tách biệt
    private JLabel lblStatusDichVu;
    private JLabel lblStatusDoAn;

    private final List<ChonDichVuDialog.SelectedItem> configuredAddons = new ArrayList<>();
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
            setComboTime(cboGioBatDau, startTimeStr);
        }
        if (endTimeStr != null && !endTimeStr.isBlank()) {
            setComboTime(cboGioKetThuc, endTimeStr);
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
        lblHeaderTitle.setIcon(Utils.IconUtils.getBallWhiteIcon(24));
        lblHeaderTitle.setIconTextGap(10);
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
        setSize(560, 660);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Cập nhật phiếu đặt lịch" : "[+] Tạo mới phiếu đặt lịch");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<KhuVucSan> sans = DataStore.get().getKhuVucsKhongBaoTri();
        cboSan = new JComboBox<>(sans.toArray(new KhuVucSan[0]));
        styleCombo(cboSan);

        int row = 0;
        row = addField(pnlFormCard, gbc, row, "Khu vực sân *", cboSan);

        txtTenKhach = new javax.swing.JTextField();
        styleTextField(txtTenKhach);
        row = addField(pnlFormCard, gbc, row, "Tên khách hàng *", txtTenKhach);

        txtSoDienThoai = new javax.swing.JTextField();
        styleTextField(txtSoDienThoai);
        txtSoDienThoai.getDocument().addDocumentListener(new Utils.SimpleDocListener(this::onPhoneAutoLookup));

        JButton btnQuickCustomer = new JButton(" Khách quen");
        btnQuickCustomer.setIcon(Utils.IconUtils.getSearchIcon(16));
        btnQuickCustomer.setFont(UIConstants.FONT_BUTTON);
        btnQuickCustomer.setPreferredSize(new Dimension(115, 34));
        btnQuickCustomer.addActionListener(e -> onPickQuickCustomer());

        JPanel pnlPhoneWrapper = new JPanel(new BorderLayout(6, 0));
        pnlPhoneWrapper.setOpaque(false);
        pnlPhoneWrapper.add(txtSoDienThoai, BorderLayout.CENTER);
        pnlPhoneWrapper.add(btnQuickCustomer, BorderLayout.EAST);

        row = addField(pnlFormCard, gbc, row, "Số điện thoại *", pnlPhoneWrapper);

        // DATE PICKER COMPONENT
        txtNgayDat = new javax.swing.JTextField();
        styleTextField(txtNgayDat);

        JButton btnPickDate = new JButton("Chọn ngày");
        btnPickDate.setFont(UIConstants.FONT_BUTTON);
        btnPickDate.setPreferredSize(new Dimension(115, 34));
        btnPickDate.addActionListener(e -> onOpenDatePicker());

        txtNgayDat.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onOpenDatePicker();
            }
        });

        JPanel pnlDateChooser = new JPanel(new BorderLayout(6, 0));
        pnlDateChooser.setOpaque(false);
        pnlDateChooser.add(txtNgayDat, BorderLayout.CENTER);
        pnlDateChooser.add(btnPickDate, BorderLayout.EAST);

        row = addField(pnlFormCard, gbc, row, "Ngày đặt sân *", pnlDateChooser);

        // DROPDOWN CHO GIỜ BẮT ĐẦU & GIỜ KẾT THÚC
        cboGioBatDau = createTimeComboBox();
        row = addField(pnlFormCard, gbc, row, "Giờ bắt đầu *", cboGioBatDau);

        cboGioKetThuc = createTimeComboBox();
        row = addField(pnlFormCard, gbc, row, "Giờ kết thúc *", cboGioKetThuc);

        txtGhiChu = new javax.swing.JTextField();
        styleTextField(txtGhiChu);
        row = addField(pnlFormCard, gbc, row, "Ghi chú", txtGhiChu);

        // TÁCH NÚT THÊM DỊCH VỤ VÀ NÚT THÊM ĐỒ ĂN THÀNH 2 MỤC RIÊNG
        JButton btnChonDichVu = new JButton("+ Chọn Dịch vụ");
        btnChonDichVu.setFont(UIConstants.FONT_BOLD);
        btnChonDichVu.setPreferredSize(new Dimension(135, 34));
        btnChonDichVu.addActionListener(e -> onOpenConfigDialog(0));

        lblStatusDichVu = new JLabel("Chưa chọn dịch vụ");
        lblStatusDichVu.setFont(UIConstants.FONT_SMALL);
        lblStatusDichVu.setForeground(UIConstants.TEXT_SECONDARY);

        JPanel pnlDVWrapper = new JPanel(new BorderLayout(8, 0));
        pnlDVWrapper.setOpaque(false);
        pnlDVWrapper.add(btnChonDichVu, BorderLayout.WEST);
        pnlDVWrapper.add(lblStatusDichVu, BorderLayout.CENTER);

        row = addField(pnlFormCard, gbc, row, "Thêm Dịch vụ", pnlDVWrapper);

        JButton btnChonDoAn = new JButton("+ Chọn Đồ ăn");
        btnChonDoAn.setFont(UIConstants.FONT_BOLD);
        btnChonDoAn.setPreferredSize(new Dimension(135, 34));
        btnChonDoAn.addActionListener(e -> onOpenConfigDialog(1));

        lblStatusDoAn = new JLabel("Chưa chọn đồ ăn");
        lblStatusDoAn.setFont(UIConstants.FONT_SMALL);
        lblStatusDoAn.setForeground(UIConstants.TEXT_SECONDARY);

        JPanel pnlDoAnWrapper = new JPanel(new BorderLayout(8, 0));
        pnlDoAnWrapper.setOpaque(false);
        pnlDoAnWrapper.add(btnChonDoAn, BorderLayout.WEST);
        pnlDoAnWrapper.add(lblStatusDoAn, BorderLayout.CENTER);

        row = addField(pnlFormCard, gbc, row, "Thêm Đồ ăn / Hàng kho", pnlDoAnWrapper);

        JButton btnCancel = new javax.swing.JButton("Hủy");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnSave = new javax.swing.JButton(isEdit ? "Cập nhật" : "Lưu phiếu");
        btnSave.setFont(UIConstants.FONT_BUTTON);
        btnSave.setBackground(UIConstants.PRIMARY);
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> onSave());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSave);

        if (isEdit && original != null) {
            fillForm(original);
        } else {
            txtNgayDat.setText(LocalDate.now().toString());
            setComboTime(cboGioBatDau, "18:00");
            setComboTime(cboGioKetThuc, "19:00");
        }

        getRootPane().setDefaultButton(btnSave);
    }

    private JComboBox<String> createTimeComboBox() {
        List<String> times = new ArrayList<>();
        for (int h = 6; h <= 23; h++) {
            times.add(String.format("%02d:00", h));
        }
        JComboBox<String> combo = new JComboBox<>(times.toArray(new String[0]));
        styleCombo(combo);
        return combo;
    }

    private void setComboTime(JComboBox<String> combo, String timeStr) {
        if (timeStr == null || timeStr.isBlank()) return;
        timeStr = timeStr.trim();
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equalsIgnoreCase(timeStr)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.addItem(timeStr);
        combo.setSelectedItem(timeStr);
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

    private void onOpenConfigDialog(int mode) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (mode == 0) {
            ChonDichVuDialog dialog = new ChonDichVuDialog(parent);
            dialog.setInitialQuantities(currentDvMap);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                currentDvMap.clear();
                currentDvMap.putAll(dialog.getSelectedQtyMap());
                rebuildConfiguredAddons();
            }
        } else {
            ChonVatPhamKhoDialog dialog = new ChonVatPhamKhoDialog(parent);
            dialog.setInitialQuantities(currentDoAnMap);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                currentDoAnMap.clear();
                currentDoAnMap.putAll(dialog.getSelectedQtyMap());
                rebuildConfiguredAddons();
            }
        }
    }

    private void rebuildConfiguredAddons() {
        configuredAddons.clear();
        addonTotalCost = 0;

        for (Model.DichVu dv : DataStore.get().getDichVus()) {
            int qty = currentDvMap.getOrDefault(dv.getId(), 0);
            if (qty > 0) {
                ChonDichVuDialog.SelectedItem item = new ChonDichVuDialog.SelectedItem(dv, qty);
                configuredAddons.add(item);
                addonTotalCost += item.getThanhTien();
            }
        }

        for (Model.DichVu khoItem : DataStore.get().getKhoItems()) {
            int qty = currentDoAnMap.getOrDefault(khoItem.getId(), 0);
            if (qty > 0) {
                ChonDichVuDialog.SelectedItem item = new ChonDichVuDialog.SelectedItem(khoItem, qty);
                configuredAddons.add(item);
                addonTotalCost += item.getThanhTien();
            }
        }

        updateAddonStatusLabels(currentDvMap.size(), currentDoAnMap.size());
    }

    private void updateAddonStatusLabels(int svcCount, int foodCount) {
        if (lblStatusDichVu != null) {
            lblStatusDichVu.setText(svcCount > 0 ? "Đã chọn " + svcCount + " dịch vụ" : "Chưa chọn dịch vụ");
            lblStatusDichVu.setForeground(svcCount > 0 ? UIConstants.PRIMARY : UIConstants.TEXT_SECONDARY);
        }
        if (lblStatusDoAn != null) {
            lblStatusDoAn.setText(foodCount > 0 ? "Đã chọn " + foodCount + " món/vật phẩm" : "Chưa chọn đồ/vật phẩm");
            lblStatusDoAn.setForeground(foodCount > 0 ? UIConstants.PRIMARY : UIConstants.TEXT_SECONDARY);
        }
    }

    private int addField(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BOLD);
        form.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        field.setPreferredSize(new Dimension(250, 36));
        form.add(field, gbc);
        return row + 1;
    }

    private void styleTextField(JTextField txt) {
        txt.setFont(UIConstants.FONT_NORMAL);
        txt.setPreferredSize(new Dimension(250, 36));
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    private void fillForm(DatLich d) {
        for (int i = 0; i < cboSan.getItemCount(); i++) {
            KhuVucSan k = cboSan.getItemAt(i);
            if (k.getMaSan() != null && k.getMaSan().equals(d.getMaSan())) {
                cboSan.setSelectedIndex(i);
                break;
            }
        }
        cboSan.setEnabled(true);
        txtTenKhach.setText(d.getTenKhach());
        txtSoDienThoai.setText(d.getSoDienThoaiKhach());
        txtNgayDat.setText(d.getNgayDat());
        setComboTime(cboGioBatDau, d.getGioBatDau());
        setComboTime(cboGioKetThuc, d.getGioKetThuc());
        txtGhiChu.setText(d.getGhiChu());

        if (d.getSelectedDvMap() != null) {
            currentDvMap.putAll(d.getSelectedDvMap());
        }
        if (d.getSelectedDoAnMap() != null) {
            currentDoAnMap.putAll(d.getSelectedDoAnMap());
        }

        rebuildConfiguredAddons();
    }

    private void onSave() {
        KhuVucSan san = (KhuVucSan) cboSan.getSelectedItem();
        String tk = txtTenKhach.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String ng = txtNgayDat.getText().trim();
        String bd = cboGioBatDau.getSelectedItem() != null ? cboGioBatDau.getSelectedItem().toString().trim() : "";
        String kt = cboGioKetThuc.getSelectedItem() != null ? cboGioKetThuc.getSelectedItem().toString().trim() : "";

        // BẮT BUỘC NHẬP SỐ ĐIỆN THOẠI VÀ THÔNG TIN BẮT BUỘC
        if (san == null || tk.isEmpty() || sdt.isEmpty() || ng.isEmpty() || bd.isEmpty() || kt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin bắt buộc (Bao gồm Tên khách hàng và Số điện thoại).",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // XÁC MINH DỮ LIỆU NHẬP VÀO: TÊN KHÁCH VÀ SỐ ĐIỆN THOẠI
        if (tk.length() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Tên khách hàng không hợp lệ! Vui lòng nhập từ 2 ký tự trở lên.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTenKhach.requestFocus();
            return;
        }

        if (!sdt.matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại không hợp lệ! Vui lòng nhập đúng 10 chữ số (ví dụ: 0912345678).",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtSoDienThoai.requestFocus();
            return;
        }

        // KHÔNG CHO PHÉP ĐẶT LỊCH TRONG QUÁ KHỨ
        try {
            LocalDate bookingDate = LocalDate.parse(ng);
            LocalDate today = LocalDate.now();
            if (bookingDate.isBefore(today)) {
                JOptionPane.showMessageDialog(this,
                        "[!] KHÔNG THỂ ĐẶT LỊCH TRONG QUÁ KHỨ!\n\n"
                                + "• Ngày đã chọn  : " + ng + "\n"
                                + "• Ngày hiện tại  : " + today + "\n\n"
                                + "Vui lòng chọn ngày hôm nay hoặc một ngày trong tương lai!",
                        "Ngày đặt không hợp lệ", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (bookingDate.isEqual(today)) {
                java.time.LocalTime bookingStart = java.time.LocalTime.parse(bd);
                java.time.LocalTime nowTime = java.time.LocalTime.now();
                if (bookingStart.isBefore(nowTime)) {
                    JOptionPane.showMessageDialog(this,
                            "[!] KHÔNG THỂ ĐẶT LỊCH KHUNG GIỜ TRONG QUÁ KHỨ!\n\n"
                                    + "• Khung giờ chọn : " + bd + "\n"
                                    + "• Giờ hiện tại   : " + nowTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) + "\n\n"
                                    + "Vui lòng chọn khung giờ từ thời điểm hiện tại trở về sau!",
                            "Khung giờ không hợp lệ", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        } catch (Exception ignored) {}

        int bMin = toMinutes(bd);
        int kMin = toMinutes(kt);
        if (bMin >= kMin || bMin == 0 || kMin == 0) {
            JOptionPane.showMessageDialog(this,
                    "Giờ bắt đầu và giờ kết thúc không hợp lệ (ví dụ: 06:00 đến 07:00 hoặc 17:00 đến 18:00).",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int minAllowed = 6 * 60;   // 06:00 sáng
        int maxAllowed = 23 * 60;  // 23:00 đêm
        if (bMin < minAllowed || kMin > maxAllowed) {
            JOptionPane.showMessageDialog(this,
                    "[!] KHUNG GIỜ PHỤC VỤ SÂN BÓNG:\n\n"
                            + "Sân bóng mở cửa phục vụ đặt sân từ 06:00 sáng đến 23:00 đêm.\n"
                            + "Vui lòng chọn khung giờ đặt trong khoảng 06:00 - 23:00!",
                    "Khung giờ phục vụ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // KIỂM TRA SÂN ĐANG BẢO TRÌ (Trạng thái sân + Phiếu bảo trì + Khoảng ngày bảo trì)
        if (DataStore.get().isSanBaoTriVoiNgay(san, ng)) {
            Model.BaoTri activeMaint = DataStore.get().getBaoTris().stream()
                    .filter(b -> san.getMaSan() != null && san.getMaSan().equals(b.getMaSan())
                            && !"DaHuy".equalsIgnoreCase(b.getTrangThaiPhieu())
                            && !"HUY".equalsIgnoreCase(b.getTrangThaiPhieu())
                            && !"HoanThanh".equalsIgnoreCase(b.getTrangThaiPhieu())
                            && !"HOAN_THANH".equalsIgnoreCase(b.getTrangThaiPhieu()))
                    .findFirst().orElse(null);

            String detailInfo = "Trạng thái sân: " + san.getTrangThaiHienThi();
            if (activeMaint != null) {
                detailInfo = "• Trạng thái sân : " + san.getTrangThaiHienThi() + "\n"
                           + "• Mã phiếu BT    : " + activeMaint.getMaPhieuBaoTri() + "\n"
                           + "• Nội dung BT    : " + activeMaint.getNoiDung() + "\n"
                           + "• Thời gian BT   : " + activeMaint.getNgayBatDau() + " - " + activeMaint.getNgayKetThuc() + "\n"
                           + "• Trạng thái BT  : " + activeMaint.getTrangThaiHienThi();
            }

            JOptionPane.showMessageDialog(this,
                    "[!] KHÔNG THỂ ĐẶT SÂN ĐANG BẢO TRÌ!\n\n"
                            + "Sân bóng " + san.getTenSan() + " hiện đang trong thời gian bảo trì cơ sở vật chất:\n\n"
                            + detailInfo + "\n\n"
                            + "Vui lòng chọn ngày khác hoặc chọn sân khác!",
                    "Cảnh báo bảo trì sân bóng", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // KIỂM TRA TRÙNG LỊCH ĐẶT SÂN
        DatLich conflict = findOverlapBooking(san.getMaSan(), ng, bd, kt, isEdit ? original.getMaLichDat() : null);
        if (conflict != null) {
            JOptionPane.showMessageDialog(this,
                    "[!] CẢNH BÁO TRÙNG LỊCH ĐẶT SÂN!\n\n"
                            + "• Sân        : " + san.getTenSan() + "\n"
                            + "• Ngày đặt   : " + ng + "\n"
                            + "• Khung giờ  : " + bd + " - " + kt + "\n\n"
                            + "Đã trùng lịch với phiếu đặt sân hiện có:\n"
                            + "  - Mã phiếu : " + conflict.getMaLichDat() + "\n"
                            + "  - Khách    : " + conflict.getTenKhach() + " (" + conflict.getSoDienThoaiKhach() + ")\n"
                            + "  - Giờ đặt  : " + conflict.getKhungGio() + " [" + conflict.getTrangThaiHienThi() + "]\n\n"
                            + "Vui lòng chọn khung giờ khác hoặc chọn sân khác!",
                    "Cảnh báo trùng lịch đặt sân", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double durationHours = (kMin - bMin) / 60.0;
        double initialCourtPrice = isEdit ? original.getTienSan() : (san.getGiaThueTheoGio() * durationHours);

        DatLich candidate = new DatLich();
        candidate.setMaLichDat(isEdit ? original.getMaLichDat() : "");
        candidate.setMaSan(san.getMaSan());
        candidate.setTenSan(san.getTenSan());
        candidate.setTenKhach(tk);
        candidate.setSoDienThoaiKhach(sdt);
        candidate.setNgayDat(ng);
        candidate.setGioBatDau(bd);
        candidate.setGioKetThuc(kt);
        candidate.setTienSan(initialCourtPrice);
        candidate.setTrangThai(isEdit ? original.getTrangThai() : "ChoXacNhan");
        candidate.setMaTaiKhoan(isEdit ? original.getMaTaiKhoan() : "Hệ thống");

        StringBuilder noteBuilder = new StringBuilder(txtGhiChu.getText().trim());
        if (!configuredAddons.isEmpty()) {
            StringBuilder svcStr = new StringBuilder();
            for (ChonDichVuDialog.SelectedItem item : configuredAddons) {
                if (svcStr.length() > 0) svcStr.append(", ");
                svcStr.append(item.getSoLuong()).append(" ").append(item.getDichVu().getTenDichVu())
                      .append(" (").append(item.getDichVu().getMaDichVu()).append(")");
            }
            String noteAdd = "Dặn trước: " + svcStr;
            if (!noteBuilder.toString().contains("Dặn trước:")) {
                if (noteBuilder.length() > 0) noteBuilder.append(". ");
                noteBuilder.append(noteAdd);
            }
        }
        candidate.setGhiChu(noteBuilder.toString());

        candidate.setSelectedDvMap(currentDvMap);
        candidate.setSelectedDoAnMap(currentDoAnMap);

        // HIỂN THỊ DIALOG TỔNG HỢP THÔNG TIN CHO NGƯỜI DÙNG KIỂM TRA & XÁC NHẬN / HỦY
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        TongHopThongTinDialog summaryDialog = new TongHopThongTinDialog(parent, candidate, configuredAddons, addonTotalCost);
        summaryDialog.setVisible(true);

        if (summaryDialog.isConfirmed()) {
            // LƯU/CẬP NHẬT THÔNG TIN KHÁCH HÀNG VÀO CSDL (Không lưu ghi chú lần đặt vào hồ sơ khách hàng)
            Model.KhachHang khachHang = DataStore.get().saveOrUpdateKhachHang(tk, sdt);
            if (khachHang != null) {
                candidate.setMaKhachHang(khachHang.getMaKhachHang());
            }

            this.result = candidate;
            this.confirmed = true;
            dispose();
        }
    }

    private void onPhoneAutoLookup() {
        String sdt = txtSoDienThoai.getText().trim();
        if (sdt.length() >= 9) {
            Model.KhachHang kh = DataStore.get().findKhachHangBySoDienThoai(sdt);
            if (kh != null && (txtTenKhach.getText().isBlank() || !txtTenKhach.getText().equals(kh.getTenKhachHang()))) {
                txtTenKhach.setText(kh.getTenKhachHang());
            }
        }
    }

    private void onPickQuickCustomer() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        ChonKhachHangDialog dialog = new ChonKhachHangDialog(parent);
        dialog.setVisible(true);

        if (dialog.isConfirmed() && dialog.getSelectedCustomer() != null) {
            Model.KhachHang selected = dialog.getSelectedCustomer();
            txtTenKhach.setText(selected.getTenKhachHang());
            txtSoDienThoai.setText(selected.getSoDienThoai());
        }
    }

    public static DatLich findOverlapBooking(String maSan, String ngayDat, String gioBD, String gioKT, String excludeMaLichDat) {
        int newStart = toMinutes(gioBD);
        int newEnd = toMinutes(gioKT);

        if (newStart >= newEnd) return null;

        for (DatLich existing : DataStore.get().getDatLichs()) {
            if (excludeMaLichDat != null && excludeMaLichDat.equals(existing.getMaLichDat())) continue;
            if (!java.util.Objects.equals(existing.getMaSan(), maSan)) continue;
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
