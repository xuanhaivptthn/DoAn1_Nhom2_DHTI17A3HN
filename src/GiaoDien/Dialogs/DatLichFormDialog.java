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
 * Hộp thoại (JDialog) quản lý việc tạo mới và cập nhật phiếu đặt lịch sân bóng.
 * <p>
 * Dialog hỗ trợ điền nhanh thông tin khi người dùng nhấp chọn ô khung giờ trống trên Ma trận đặt sân,
 * tự động tra cứu thông tin khách hàng qua số điện thoại, quản lý dịch vụ và đồ ăn đi kèm,
 * cũng như kiểm tra các điều kiện ràng buộc kinh doanh (bảo trì sân, trùng giờ đặt, đặt lịch trong quá quá khứ).
 * </p>
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class DatLichFormDialog extends JDialog {

    /** Combobox chọn khu vực sân bóng khả dụng */
    private JComboBox<KhuVucSan> cboSan;

    /** Ô nhập tên khách hàng đặt sân */
    private JTextField txtTenKhach;

    /** Ô nhập số điện thoại khách hàng */
    private JTextField txtSoDienThoai;

    /** Ô nhập/hiển thị ngày đặt sân bóng (định dạng YYYY-MM-DD) */
    private JTextField txtNgayDat;

    /** Combobox chọn giờ bắt đầu đặt sân */
    private JComboBox<String> cboGioBatDau;

    /** Combobox chọn giờ kết thúc đặt sân */
    private JComboBox<String> cboGioKetThuc;

    /** Ô nhập ghi chú bổ sung cho phiếu đặt lịch */
    private JTextField txtGhiChu;

    /** Nhãn hiển thị trạng thái số lượng dịch vụ đã chọn */
    private JLabel lblStatusDichVu;

    /** Nhãn hiển thị trạng thái số lượng đồ ăn / hàng kho đã chọn */
    private JLabel lblStatusDoAn;

    /** Danh sách các món/dịch vụ phụ trợ đã chọn kèm theo phiếu */
    private final List<ChonDichVuDialog.SelectedItem> configuredAddons = new ArrayList<>();

    /** Tổng giá trị tiền dịch vụ và đồ ăn kèm theo */
    private double addonTotalCost = 0;

    /** Cờ xác định dialog ở chế độ chỉnh sửa (true) hay tạo mới (false) */
    private boolean isEdit;

    /** Đối tượng phiếu đặt lịch ban đầu (dùng khi chỉnh sửa) */
    private DatLich original;

    /** Đối tượng phiếu đặt lịch kết quả sau khi người dùng lưu thành công */
    private DatLich result;

    /** Cờ đánh dấu người dùng đã xác nhận lưu phiếu hay chưa */
    private boolean confirmed;

    /** Map lưu trữ mã dịch vụ và số lượng đã chọn (key: ID dịch vụ, value: số lượng) */
    private final java.util.Map<Integer, Integer> currentDvMap = new java.util.HashMap<>();

    /** Map lưu trữ mã hàng kho/đồ ăn và số lượng đã chọn (key: ID hàng kho, value: số lượng) */
    private final java.util.Map<Integer, Integer> currentDoAnMap = new java.util.HashMap<>();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Nhãn tiêu đề header dialog */
    private javax.swing.JLabel lblHeaderTitle;
    /** Panel bọc phần nội dung trung tâm */
    private javax.swing.JPanel pnlCenterWrap;
    /** Panel chứa các nút chức năng ở chân dialog */
    private javax.swing.JPanel pnlFooter;
    /** Panel chứa form nhập liệu với GridBagLayout */
    private javax.swing.JPanel pnlFormCard;
    /** Panel header chứa tiêu đề và icon */
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables

    /**
     * Khởi tạo dialog đặt lịch mặc định không có tham số cha và phiếu đặt.
     */
    public DatLichFormDialog() {
        this(null, null);
    }

    /**
     * Khởi tạo dialog đặt lịch để thêm mới hoặc chỉnh sửa phiếu đặt có sẵn.
     *
     * @param parent   Cửa sổ cha (JFrame)
     * @param existing Đối tượng {@link DatLich} cần chỉnh sửa, hoặc {@code null} nếu tạo mới
     */
    public DatLichFormDialog(JFrame parent, DatLich existing) {
        super(parent, existing == null ? "Tạo mới phiếu đặt lịch" : "Cập nhật phiếu đặt lịch", true);
        this.isEdit = existing != null;
        this.original = existing;

        // Khởi tạo các thành phần giao diện do NetBeans GUI Builder sinh ra
        initComponents();
        // Cấu hình giao diện tùy chỉnh và đổ dữ liệu
        customInit(parent);
    }

    /**
     * Khởi tạo dialog đặt lịch với thông tin điền sẵn từ khung giờ chọn trên ma trận đặt sân.
     *
     * @param parent           Cửa sổ cha (JFrame)
     * @param preSelectedCourt Khu vực sân bóng được chọn trước
     * @param dateStr          Chuỗi ngày đặt (YYYY-MM-DD)
     * @param startTimeStr     Giờ bắt đầu (HH:mm)
     * @param endTimeStr       Giờ kết thúc (HH:mm)
     */
    public DatLichFormDialog(JFrame parent, KhuVucSan preSelectedCourt, String dateStr, String startTimeStr, String endTimeStr) {
        super(parent, "Tạo mới phiếu đặt lịch sân trống", true);
        this.isEdit = false;
        this.original = null;

        // Khởi tạo thành phần GUI cơ bản
        initComponents();
        // Cấu hình bố cục form tùy chỉnh
        customInit(parent);

        // Thiết lập thông tin mặc định được truyền vào
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

    /**
     * Xây dựng giao diện nhập liệu chi tiết, các nút chọn nhanh và bắt sự kiện.
     *
     * @param parent Cửa sổ cha để định vị vị trí dialog
     */
    private void customInit(JFrame parent) {
        // Kích thước cố định cho form đặt lịch
        setSize(560, 660);
        if (parent != null) setLocationRelativeTo(parent);

        // Đặt tiêu đề header tương ứng chế độ
        lblHeaderTitle.setText(isEdit ? "Cập nhật phiếu đặt lịch" : "[+] Tạo mới phiếu đặt lịch");

        // Thiết lập ràng buộc bố cục GridBag
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tải danh sách sân bóng không thuộc trạng thái bảo trì
        List<KhuVucSan> sans = DataStore.get().getKhuVucsKhongBaoTri();
        cboSan = new JComboBox<>(sans.toArray(new KhuVucSan[0]));
        styleCombo(cboSan);

        int row = 0;
        row = addField(pnlFormCard, gbc, row, "Khu vực sân *", cboSan);

        // Tạo trường nhập tên khách hàng
        txtTenKhach = new javax.swing.JTextField();
        styleTextField(txtTenKhach);
        row = addField(pnlFormCard, gbc, row, "Tên khách hàng *", txtTenKhach);

        // Trường nhập số điện thoại kèm tính năng tra cứu tự động và chọn từ danh sách khách quen
        txtSoDienThoai = new javax.swing.JTextField();
        styleTextField(txtSoDienThoai);
        // Tự động tìm kiếm thông tin khách hàng cũ khi gõ số điện thoại
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

        // Trường chọn ngày đặt sân cùng nút mở lịch
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

        // Tạo combobox danh sách khung giờ từ 06:00 đến 23:00
        cboGioBatDau = createTimeComboBox();
        row = addField(pnlFormCard, gbc, row, "Giờ bắt đầu *", cboGioBatDau);

        cboGioKetThuc = createTimeComboBox();
        row = addField(pnlFormCard, gbc, row, "Giờ kết thúc *", cboGioKetThuc);

        // Trường nhập ghi chú
        txtGhiChu = new javax.swing.JTextField();
        styleTextField(txtGhiChu);
        row = addField(pnlFormCard, gbc, row, "Ghi chú", txtGhiChu);

        // Chọn dịch vụ phụ trợ (HLV, Trọng tài, Dịch vụ thi đấu...)
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

        // Chọn đồ ăn / sản phẩm từ kho hàng
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

        // Nút Hủy bỏ
        JButton btnCancel = new javax.swing.JButton("Hủy");
        Utils.PageUI.styleSecondaryButton(btnCancel);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        // Nút Lưu phiếu đặt
        JButton btnSave = new javax.swing.JButton(isEdit ? "Cập nhật" : "Lưu phiếu");
        Utils.PageUI.stylePrimaryButton(btnSave);
        btnSave.addActionListener(e -> onSave());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSave);

        // Điền dữ liệu nếu là sửa, hoặc đặt giá trị mặc định cho tạo mới
        if (isEdit && original != null) {
            fillForm(original);
        } else {
            txtNgayDat.setText(LocalDate.now().toString());
            setComboTime(cboGioBatDau, "18:00");
            setComboTime(cboGioKetThuc, "19:00");
        }

        // Đặt nút mặc định khi nhấn Enter
        getRootPane().setDefaultButton(btnSave);
    }

    /**
     * Tạo combobox chứa danh sách các khung giờ hoạt động (từ 06:00 đến 23:00).
     *
     * @return {@link JComboBox} danh sách chuỗi giờ dạng HH:00
     */
    private JComboBox<String> createTimeComboBox() {
        List<String> times = new ArrayList<>();
        for (int h = 6; h <= 23; h++) {
            times.add(String.format("%02d:00", h));
        }
        JComboBox<String> combo = new JComboBox<>(times.toArray(new String[0]));
        styleCombo(combo);
        return combo;
    }

    /**
     * Chọn hoặc thêm một mốc thời gian cụ thể vào JComboBox giờ.
     *
     * @param combo   Combobox cần thiết lập
     * @param timeStr Chuỗi thời gian (HH:mm)
     */
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

    /**
     * Mở hộp thoại chọn ngày dạng lịch (ChonNgayDialog) để cập nhật vào trường ngày đặt.
     */
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

    /**
     * Mở hộp thoại chọn dịch vụ phụ trợ hoặc đồ ăn kho tùy theo mode.
     *
     * @param mode 0 nếu chọn Dịch vụ, 1 nếu chọn Đồ ăn / Mặt hàng kho
     */
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

    /**
     * Tổng hợp lại danh sách dịch vụ & đồ ăn đã chọn và tính toán tổng tiền chi phí phụ trợ.
     */
    private void rebuildConfiguredAddons() {
        configuredAddons.clear();
        addonTotalCost = 0;

        // Tính tiền dịch vụ
        for (Model.DichVu dv : DataStore.get().getDichVus()) {
            int qty = currentDvMap.getOrDefault(dv.getId(), 0);
            if (qty > 0) {
                ChonDichVuDialog.SelectedItem item = new ChonDichVuDialog.SelectedItem(dv, qty);
                configuredAddons.add(item);
                addonTotalCost += item.getThanhTien();
            }
        }

        // Tính tiền đồ ăn / mặt hàng kho
        for (Model.DichVu khoItem : DataStore.get().getKhoItems()) {
            int qty = currentDoAnMap.getOrDefault(khoItem.getId(), 0);
            if (qty > 0) {
                ChonDichVuDialog.SelectedItem item = new ChonDichVuDialog.SelectedItem(khoItem, qty);
                configuredAddons.add(item);
                addonTotalCost += item.getThanhTien();
            }
        }

        // Cập nhật các nhãn trạng thái hiển thị số lượng
        updateAddonStatusLabels(currentDvMap.size(), currentDoAnMap.size());
    }

    /**
     * Cập nhật văn bản nhãn hiển thị số lượng dịch vụ và đồ ăn đã chọn.
     *
     * @param svcCount  Số lượng dịch vụ đã chọn
     * @param foodCount Số lượng đồ ăn/hàng kho đã chọn
     */
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

    /**
     * Thêm một hàng gồm nhãn và ô thành phần giao diện vào panel form bằng GridBagLayout.
     *
     * @param form  Panel chứa form
     * @param gbc   Đối tượng GridBagConstraints
     * @param row   Chỉ số hàng hiện tại
     * @param label Tiêu đề nhãn
     * @param field Thành phần nhập liệu (Component)
     * @return Chỉ số hàng tiếp theo
     */
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

    /**
     * Áp dụng kiểu dáng chuẩn cho ô nhập văn bản.
     *
     * @param txt Ô JTextField cần định dạng
     */
    private void styleTextField(JTextField txt) {
        txt.setFont(UIConstants.FONT_NORMAL);
        txt.setPreferredSize(new Dimension(250, 36));
    }

    /**
     * Áp dụng kiểu dáng chuẩn cho combobox.
     *
     * @param combo JComboBox cần định dạng
     */
    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    /**
     * Đổ dữ liệu từ đối tượng DatLich có sẵn lên các thành phần form.
     *
     * @param d Đối tượng phiếu đặt lịch
     */
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

    /**
     * Xử lý sự kiện lưu phiếu đặt lịch: kiểm tra hợp lệ thông tin, kiểm tra trùng lịch đặt,
     * cảnh báo sân đang bảo trì, tính tiền sân và hiển thị bảng tổng hợp thông tin trước khi lưu.
     */
    private void onSave() {
        KhuVucSan san = (KhuVucSan) cboSan.getSelectedItem();
        String tk = txtTenKhach.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String ng = txtNgayDat.getText().trim();
        String bd = cboGioBatDau.getSelectedItem() != null ? cboGioBatDau.getSelectedItem().toString().trim() : "";
        String kt = cboGioKetThuc.getSelectedItem() != null ? cboGioKetThuc.getSelectedItem().toString().trim() : "";

        // 1. Kiểm tra các thông tin bắt buộc không được bỏ trống
        if (san == null || tk.isEmpty() || sdt.isEmpty() || ng.isEmpty() || bd.isEmpty() || kt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin bắt buộc (Bao gồm Tên khách hàng và Số điện thoại).",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Kiểm tra độ dài tên khách hàng
        if (tk.length() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Tên khách hàng không hợp lệ! Vui lòng nhập từ 2 ký tự trở lên.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtTenKhach.requestFocus();
            return;
        }

        // 3. Kiểm tra định dạng 10 chữ số của số điện thoại
        if (!sdt.matches("^\\d{10}$")) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại không hợp lệ! Vui lòng nhập đúng 10 chữ số (ví dụ: 0912345678).",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtSoDienThoai.requestFocus();
            return;
        }

        // 4. Kiểm tra ngày đặt và thời gian không nằm trong quá quá khứ
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

        // 5. Kiểm tra logic giờ bắt đầu phải nhỏ hơn giờ kết thúc
        int bMin = toMinutes(bd);
        int kMin = toMinutes(kt);
        if (bMin >= kMin || bMin == 0 || kMin == 0) {
            JOptionPane.showMessageDialog(this,
                    "Giờ bắt đầu và giờ kết thúc không hợp lệ (ví dụ: 06:00 đến 07:00 hoặc 17:00 đến 18:00).",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 6. Kiểm tra nằm trong khung giờ mở cửa của sân bóng (06:00 đến 23:00)
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

        // 7. Kiểm tra trạng thái bảo trì của sân theo ngày
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

        // 8. Kiểm tra trùng lịch đặt sân với phiếu khác
        DatLich conflict = new Controller.DatLichController().findOverlapBooking(san.getMaSan(), ng, bd, kt, isEdit ? original.getMaLichDat() : null);
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

        // 9. Tính thời lượng đặt sân (giờ) và tiền sân tạm tính
        double durationHours = (kMin - bMin) / 60.0;
        double initialCourtPrice = san.getGiaThueTheoGio() * durationHours;

        // 10. Đóng gói dữ liệu vào đối tượng DatLich ứng viên
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

        candidate.setGhiChu(txtGhiChu.getText().trim());

        candidate.setSelectedDvMap(currentDvMap);
        candidate.setSelectedDoAnMap(currentDoAnMap);

        // 11. Mở dialog tổng hợp thông tin để người dùng kiểm tra lần cuối
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        TongHopThongTinDialog summaryDialog = new TongHopThongTinDialog(parent, candidate, configuredAddons, addonTotalCost);
        summaryDialog.setVisible(true);

        if (summaryDialog.isConfirmed()) {
            // Trừ số lượng tồn kho sản phẩm tương ứng
            Controller.KhoController khoController = new Controller.KhoController();
            for (ChonDichVuDialog.SelectedItem item : configuredAddons) {
                if (item.getDichVu() != null && item.getSoLuong() > 0) {
                    khoController.giamStock(item.getDichVu(), item.getSoLuong());
                }
            }

            // Lưu/Cập nhật thông tin khách hàng vào danh mục quản lý khách hàng
            Model.KhachHang khachHang = DataStore.get().saveOrUpdateKhachHang(tk, sdt);
            if (khachHang != null) {
                candidate.setMaKhachHang(khachHang.getMaKhachHang());
            }

            this.result = candidate;
            this.confirmed = true;
            dispose();
        }
    }

    /**
     * Tự động tra cứu họ tên khách hàng dựa vào số điện thoại nhập vào.
     */
    private void onPhoneAutoLookup() {
        String sdt = txtSoDienThoai.getText().trim();
        if (sdt.length() >= 9) {
            Model.KhachHang kh = DataStore.get().findKhachHangBySoDienThoai(sdt);
            if (kh != null && (txtTenKhach.getText().isBlank() || !txtTenKhach.getText().equals(kh.getTenKhachHang()))) {
                txtTenKhach.setText(kh.getTenKhachHang());
            }
        }
    }

    /**
     * Mở hộp thoại chọn khách hàng quen thuộc (ChonKhachHangDialog) để điền nhanh thông tin.
     */
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

    /**
     * Tìm kiếm và kiểm tra phiếu đặt sân trùng lịch trên cùng mã sân và ngày.
     *
     * @param maSan            Mã sân bóng cần kiểm tra
     * @param ngayDat          Ngày đặt sân (YYYY-MM-DD)
     * @param gioBD            Giờ bắt đầu (HH:mm)
     * @param gioKT            Giờ kết thúc (HH:mm)
     * @param excludeMaLichDat Mã phiếu đặt cần loại trừ (dùng khi chỉnh sửa chính phiếu đó)
     * @return Đối tượng {@link DatLich} bị trùng, hoặc {@code null} nếu không trùng
     */
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

            // Kiểm tra giao khoảng thời gian [newStart, newEnd] và [exStart, exEnd]
            if (newStart < exEnd && newEnd > exStart) {
                return existing;
            }
        }
        return null;
    }

    /**
     * Chuyển đổi chuỗi thời gian dạng "HH:mm" thành số phút từ đầu ngày.
     *
     * @param timeStr Chuỗi thời gian (ví dụ "18:30")
     * @return Tổng số phút, hoặc 0 nếu định dạng không hợp lệ
     */
    private static int toMinutes(String timeStr) {
        if (timeStr == null || !timeStr.contains(":")) return 0;
        try {
            String[] parts = timeStr.trim().split(":");
            return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Trả về trạng thái xác nhận thành công của người dùng.
     *
     * @return {@code true} nếu đã lưu thành công, ngược lại {@code false}
     */
    public boolean isConfirmed() { return confirmed; }

    /**
     * Lấy phiếu đặt lịch kết quả sau khi tạo mới/chỉnh sửa thành công.
     *
     * @return Đối tượng {@link DatLich}
     */
    public DatLich getResult() { return result; }

    /**
     * Lấy sân bóng được chọn trong combobox.
     *
     * @return Đối tượng {@link KhuVucSan}
     */
    public KhuVucSan getSelectedSan() { return (KhuVucSan) cboSan.getSelectedItem(); }
}
