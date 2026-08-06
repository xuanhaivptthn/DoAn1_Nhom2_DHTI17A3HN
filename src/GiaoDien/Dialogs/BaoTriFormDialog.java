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
 * Dialog lập / cập nhật phiếu bảo trì cơ sở vật chất sân bóng.
 * <p>
 * Cho phép tạo phiếu bảo trì mới hoặc chỉnh sửa thông tin bảo trì hiện có.
 * Hỗ trợ chọn ngày từ ChonNgayDialog và tự động cảnh báo nếu ngày bảo trì bị trùng với các lịch đặt sân hiện tại.
 * </p>
 *
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class BaoTriFormDialog extends JDialog {

    /** Combobox chọn khu vực sân bóng cần bảo trì */
    private JComboBox<KhuVucSan> cboSan;

    /** Ô nhập nội dung chi tiết công việc bảo trì */
    private JTextField txtNoiDung;

    /** Ô nhập ngày bắt đầu bảo trì (YYYY-MM-DD) */
    private JTextField txtNgayBatDau;

    /** Ô nhập ngày kết thúc bảo trì (YYYY-MM-DD) */
    private JTextField txtNgayKetThuc;

    /** Combobox chọn trạng thái phiếu bảo trì ("Đang bảo trì", "Hoàn thành", "Đã hủy") */
    private JComboBox<String> cboTrangThai;

    /** Trạng thái chế độ form: true nếu đang sửa thông tin, false nếu tạo mới */
    private boolean isEdit;

    /** Đối tượng phiếu bảo trì ban đầu được truyền vào để chỉnh sửa */
    private BaoTri original;

    /** Đối tượng phiếu bảo trì kết quả thu được sau khi nhấn Lưu thành công */
    private BaoTri result;

    /** Trạng thái xác nhận của hộp thoại */
    private boolean confirmed;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Nhãn tiêu đề header */
    private javax.swing.JLabel lblHeaderTitle;
    /** Panel bọc phần nội dung trung tâm */
    private javax.swing.JPanel pnlCenterWrap;
    /** Panel chứa các nút bấm hoàn tất/hủy phía dưới */
    private javax.swing.JPanel pnlFooter;
    /** Panel chứa các ô thông tin form nhập liệu */
    private javax.swing.JPanel pnlFormCard;
    /** Panel header màu sắc chính ở phía trên */
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables

    /**
     * Constructor mặc định phục vụ GUI Builder.
     */
    public BaoTriFormDialog() {
        this(null, null);
    }

    /**
     * Khởi tạo dialog lập mới hoặc cập nhật thông tin phiếu bảo trì.
     *
     * @param parent   Cửa sổ cha (JFrame)
     * @param existing Phiếu bảo trì hiện tại nếu sửa, hoặc null nếu tạo phiếu mới
     */
    public BaoTriFormDialog(JFrame parent, BaoTri existing) {
        super(parent, existing == null ? "Lập phiếu bảo trì" : "Cập nhật phiếu bảo trì", true);
        this.isEdit = existing != null;
        this.original = existing;

        initComponents();
        customInit(parent);
    }

    /**
     * Khởi tạo các thành phần giao diện do NetBeans GUI Builder tạo ra.
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
        lblHeaderTitle.setText("Thông tin phiếu bảo trì");
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
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cấu hình giao diện mở rộng, thêm trường chọn ngày và liên kết các sự kiện form.
     *
     * @param parent Cửa sổ cha
     */
    private void customInit(JFrame parent) {
        setSize(480, 540);
        if (parent != null) setLocationRelativeTo(parent);

        lblHeaderTitle.setText(isEdit ? "Cập nhật phiếu bảo trì" : "[+] Lập phiếu bảo trì mới");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Nạp danh sách khu vực sân từ DataStore
        List<KhuVucSan> sans = DataStore.get().getKhuVucs();
        cboSan = new JComboBox<>(sans.toArray(new KhuVucSan[0]));
        styleCombo(cboSan);

        int row = 0;
        row = addField(pnlFormCard, gbc, row, "Khu vực sân *", cboSan);

        // 2. Ô nội dung bảo trì
        txtNoiDung = new javax.swing.JTextField(18);
        row = addField(pnlFormCard, gbc, row, "Nội dung bảo trì *", txtNoiDung);

        // 3. Chọn ngày bắt đầu bảo trì
        txtNgayBatDau = new javax.swing.JTextField();
        JPanel pnlNbd = createDatePickerPanel(txtNgayBatDau, parent);
        row = addField(pnlFormCard, gbc, row, "Ngày bắt đầu *", pnlNbd);

        // 4. Chọn ngày kết thúc bảo trì
        txtNgayKetThuc = new javax.swing.JTextField();
        JPanel pnlNkt = createDatePickerPanel(txtNgayKetThuc, parent);
        row = addField(pnlFormCard, gbc, row, "Ngày kết thúc", pnlNkt);

        // 5. Combobox trạng thái phiếu bảo trì
        cboTrangThai = new JComboBox<>(new String[]{"Đang bảo trì", "Hoàn thành", "Đã hủy"});
        styleCombo(cboTrangThai);
        addField(pnlFormCard, gbc, row, "Trạng thái BT", cboTrangThai);

        // 6. Nút Hủy và Lưu
        JButton btnCancel = new javax.swing.JButton("Hủy");
        Utils.PageUI.styleSecondaryButton(btnCancel);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnSave = new javax.swing.JButton(isEdit ? "Cập nhật" : "Lưu phiếu");
        Utils.PageUI.stylePrimaryButton(btnSave);
        btnSave.addActionListener(e -> onSave());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSave);

        // Nếu là sửa thông tin thì đổ dữ liệu cũ, ngược lại để ngày bắt đầu mặc định là hôm nay
        if (isEdit && original != null) {
            fillForm(original);
        } else {
            txtNgayBatDau.setText(java.time.LocalDate.now().toString());
        }

        getRootPane().setDefaultButton(btnSave);
    }

    /**
     * Thêm một dòng thông tin gồm nhãn và ô điều khiển vào panel form.
     *
     * @param form  Panel form
     * @param gbc   GridBagConstraints
     * @param row   Chỉ số dòng
     * @param label Tiêu đề nhãn
     * @param field Thành phần UI điều khiển
     * @return Dòng kế tiếp
     */
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

    /**
     * Thiết lập font chữ và màu sắc cho JComboBox.
     *
     * @param combo Combobox cần áp dụng kiểu dáng
     */
    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    /**
     * Đổ dữ liệu từ đối tượng phiếu bảo trì có sẵn vào các ô điều khiển trên giao diện.
     *
     * @param b Đối tượng BaoTri chứa thông tin cũ
     */
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

    /**
     * Xử lý lưu thông tin phiếu bảo trì.
     * Thực hiện kiểm tra tính hợp lệ dữ liệu, phát hiện trùng lịch đặt sân và tạo đối tượng BaoTri kết quả.
     */
    private void onSave() {
        KhuVucSan san = (KhuVucSan) cboSan.getSelectedItem();
        String nd = txtNoiDung.getText().trim();
        String nbd = txtNgayBatDau.getText().trim();

        // 1. Validate khu vực sân
        if (san == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khu vực sân bóng cần bảo trì.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            cboSan.requestFocus();
            return;
        }

        // 2. Validate nội dung bảo trì
        if (nd.isEmpty() || nd.length() < 3) {
            JOptionPane.showMessageDialog(this, "Nội dung bảo trì không hợp lệ! Vui lòng nhập từ 3 ký tự trở lên.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtNoiDung.requestFocus();
            return;
        }

        // 3. Validate ngày bắt đầu
        if (nbd.isEmpty() || !nbd.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu bảo trì không hợp lệ! Vui lòng nhập theo định dạng YYYY-MM-DD (ví dụ: 2026-08-05).", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtNgayBatDau.requestFocus();
            return;
        }

        java.time.LocalDate sDate;
        try {
            sDate = java.time.LocalDate.parse(nbd);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu bảo trì không đúng định dạng ngày hợp lệ (YYYY-MM-DD).", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtNgayBatDau.requestFocus();
            return;
        }

        // 4. Validate ngày kết thúc (nếu có)
        String nkt = txtNgayKetThuc.getText().trim();
        if (!nkt.isEmpty()) {
            if (!nkt.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                JOptionPane.showMessageDialog(this, "Ngày kết thúc bảo trì không hợp lệ! Vui lòng nhập theo định dạng YYYY-MM-DD.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                txtNgayKetThuc.requestFocus();
                return;
            }
            try {
                java.time.LocalDate eDate = java.time.LocalDate.parse(nkt);
                if (eDate.isBefore(sDate)) {
                    JOptionPane.showMessageDialog(this, "Ngày kết thúc bảo trì không thể trước ngày bắt đầu bảo trì!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    txtNgayKetThuc.requestFocus();
                    return;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ngày kết thúc bảo trì không đúng định dạng ngày hợp lệ (YYYY-MM-DD).", "Thông báo", JOptionPane.WARNING_MESSAGE);
                txtNgayKetThuc.requestFocus();
                return;
            }
        }

        // 5. KIỂM TRA LỊCH ĐẶT SÂN ĐÃ CÓ TRONG THỜI GIAN BẢO TRÌ NÀY
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
                sb.append(" - ").append(nkt);
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
            sb.append("\n[Lưu ý] Vui lòng liên hệ trực tiếp với khách hàng theo SĐT trên để dời/đổi lịch!");

            JOptionPane.showMessageDialog(this, sb.toString(), "Cảnh báo trùng lịch đặt sân", JOptionPane.WARNING_MESSAGE);
        }

        // 6. Ánh xạ trạng thái hiển thị thành mã trạng thái dữ liệu
        String trangThaiPhieu = switch ((String) cboTrangThai.getSelectedItem()) {
            case "Hoàn thành" -> "HOAN_THANH";
            case "Đã hủy" -> "HUY";
            default -> "DANG_BAO_TRI"; // "Đang bảo trì"
        };

        // 7. Tạo đối tượng kết quả
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

    /**
     * Kiểm tra xem ngày đặt sân có rơi vào khoảng thời gian bảo trì hay không.
     *
     * @param bookingDateStr Chuỗi ngày đặt sân (YYYY-MM-DD)
     * @param startDateStr   Chuỗi ngày bắt đầu bảo trì (YYYY-MM-DD)
     * @param endDateStr     Chuỗi ngày kết thúc bảo trì (YYYY-MM-DD)
     * @return true nếu ngày đặt sân nằm trong khoảng bảo trì
     */
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

    /**
     * Lấy trạng thái người dùng đã xác nhận hay chưa.
     *
     * @return true nếu đã bấm nút Lưu/Cập nhật thành công
     */
    public boolean isConfirmed() { return confirmed; }

    /**
     * Lấy đối tượng BaoTri được tạo hoặc chỉnh sửa từ form.
     *
     * @return Đối tượng BaoTri kết quả
     */
    public BaoTri getResult() { return result; }

    /**
     * Lấy khu vực sân đang được chọn trên combobox.
     *
     * @return Đối tượng KhuVucSan
     */
    public KhuVucSan getSelectedSan() { return (KhuVucSan) cboSan.getSelectedItem(); }

    /**
     * Chọn sẵn khu vực sân bóng hiển thị trên combobox.
     *
     * @param targetSan KhuVucSan cần chọn mặc định
     */
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

    /**
     * Tạo một panel kết hợp ô nhập ngày và nút bấm biểu tượng lịch chọn ngày.
     *
     * @param txtField Trường JTextField để nhận ngày
     * @param parent   Cửa sổ cha JFrame
     * @return JPanel chứa thành phần chọn ngày
     */
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

    /**
     * Mở ChonNgayDialog để người dùng chọn ngày từ giao diện lịch cả tháng.
     *
     * @param txtField Ô JTextField cần gán kết quả ngày
     * @param parent   Cửa sổ cha JFrame
     */
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
