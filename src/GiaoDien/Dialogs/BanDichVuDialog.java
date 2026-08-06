package GiaoDien.Dialogs;

import Model.DatLich;
import Model.DichVu;
import Utils.DataStore;
import Utils.UIConstants;
import Utils.PageUI;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dialog bán dịch vụ / đồ ăn đi kèm cho phiếu đặt sân.
 * <p>
 * Thiết kế giao diện hiển thị rõ ràng thông tin chi tiết của đơn đặt sân hiện tại,
 * kết hợp hỗ trợ mở sub-dialog để chọn các gói dịch vụ (trọng tài, huấn luyện viên...)
 * và mặt hàng đồ ăn / dụng cụ kho hàng đi kèm.
 * </p>
 *
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class BanDichVuDialog extends JDialog {

    // ── State ─────────────────────────────────────────────────────────────────
    /** Đối tượng phiếu đặt sân được chọn bán dịch vụ */
    private final DatLich datLich;

    /** Trạng thái xác nhận của hộp thoại (true nếu người dùng nhấn nút Xác nhận bán) */
    private boolean confirmed = false;

    /** Map lưu số lượng dịch vụ đã chọn (mã ID dịch vụ -> số lượng chọn) */
    private final Map<Integer, Integer> currentDvMap   = new HashMap<>();

    /** Map lưu số lượng đồ ăn / vật phẩm kho đã chọn (mã ID mặt hàng -> số lượng chọn) */
    private final Map<Integer, Integer> currentDoAnMap = new HashMap<>();

    /** Danh sách các món / dịch vụ được tổng hợp chọn mua */
    private final List<ChonDichVuDialog.SelectedItem> selectedItems = new ArrayList<>();

    /** Tổng số tiền phát sinh thêm do chọn mua dịch vụ / đồ ăn đi kèm */
    private double totalAddonCost = 0;

    // ── UI Labels trạng thái ──────────────────────────────────────────────────
    /** Nhãn hiển thị trạng thái số lượng dịch vụ đã chọn */
    private JLabel lblStatusDichVu;

    /** Nhãn hiển thị trạng thái số lượng đồ ăn / vật phẩm kho đã chọn */
    private JLabel lblStatusDoAn;

    /** Nhãn hiển thị tổng số tiền phát sinh thêm */
    private JLabel lblTongCong;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Nhãn tiêu đề header chính của dialog */
    private javax.swing.JLabel lblHeaderTitle;
    /** Panel bọc phần nội dung trung tâm */
    private javax.swing.JPanel pnlCenterWrap;
    /** Panel footer chứa các nút chức năng bên dưới */
    private javax.swing.JPanel pnlFooter;
    /** Panel thẻ chứa thông tin form mẫu */
    private javax.swing.JPanel pnlFormCard;
    /** Panel phần header phía trên */
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables

    /**
     * Constructor mặc định không đối số, phục vụ thiết kế NetBeans GUI Builder.
     */
    public BanDichVuDialog() {
        this(null, null);
    }

    /**
     * Khởi tạo dialog bán dịch vụ đi kèm cho một phiếu đặt sân cụ thể.
     *
     * @param parent  Cửa sổ cha (JFrame)
     * @param datLich Đối tượng phiếu đặt sân cần áp dụng bán dịch vụ
     */
    public BanDichVuDialog(JFrame parent, DatLich datLich) {
        super(parent, "Bán dịch vụ / Đồ ăn đi kèm", true);
        this.datLich = datLich;
        initComponents();
        customInit(parent);
    }

    /**
     * Phương thức tự động tạo cấu trúc giao diện từ NetBeans GUI Builder.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        pnlHeader     = new javax.swing.JPanel();
        lblHeaderTitle = new javax.swing.JLabel();
        pnlCenterWrap  = new javax.swing.JPanel();
        pnlFormCard    = new javax.swing.JPanel();
        pnlFooter      = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Bán dịch vụ / Đồ ăn đi kèm");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 20, 14, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblHeaderTitle.setFont(UIConstants.FONT_TITLE);
        lblHeaderTitle.setForeground(java.awt.Color.WHITE);
        lblHeaderTitle.setText("Bán dịch vụ đi kèm");
        lblHeaderTitle.setIcon(Utils.IconUtils.getBallWhiteIcon(24));
        lblHeaderTitle.setIconTextGap(10);
        pnlHeader.add(lblHeaderTitle, java.awt.BorderLayout.WEST);
        getContentPane().add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlCenterWrap.setBackground(UIConstants.BG);
        pnlCenterWrap.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 8, 20));
        pnlCenterWrap.setLayout(new java.awt.BorderLayout(0, 14));
        pnlFormCard.setLayout(new java.awt.GridBagLayout());
        pnlCenterWrap.add(pnlFormCard, java.awt.BorderLayout.CENTER);
        getContentPane().add(pnlCenterWrap, java.awt.BorderLayout.CENTER);

        pnlFooter.setBackground(UIConstants.BG);
        pnlFooter.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER),
                javax.swing.BorderFactory.createEmptyBorder(8, 20, 12, 20)
        ));
        pnlFooter.setLayout(new java.awt.BorderLayout());
        getContentPane().add(pnlFooter, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Tùy chỉnh nâng cao giao diện, nạp dữ liệu phiếu đặt sân và liên kết sự kiện hành động.
     *
     * @param parent Cửa sổ cha (JFrame)
     */
    private void customInit(JFrame parent) {
        setSize(560, 540);
        if (parent != null) setLocationRelativeTo(parent);

        // ── Header subtitle ──────────────────────────────────────────────────
        if (datLich != null) {
            lblHeaderTitle.setText("Bán dịch vụ đi kèm — " + datLich.getMaLichDat());

            // Nhãn tiêu đề phụ hiển thị tên phiếu và sân
            JLabel lblSub = new JLabel("Phiếu: " + datLich.getMaLichDat() + " · " + datLich.getTenSan());
            lblSub.setFont(UIConstants.FONT_SMALL);
            lblSub.setForeground(new Color(200, 230, 201));
            pnlHeader.add(lblSub, BorderLayout.SOUTH);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // ── THÔNG TIN ĐƠN ĐẶT SÂN ────────────────────────────────────────────
        if (datLich != null) {
            row = addSectionHeader(pnlFormCard, gbc, row, "📋 Thông tin phiếu đặt sân");

            row = addInfoRow(pnlFormCard, gbc, row, "Mã phiếu:",   datLich.getMaLichDat(), false);
            row = addInfoRow(pnlFormCard, gbc, row, "Sân bóng:",   datLich.getTenSan() != null ? datLich.getTenSan() : "-", false);
            row = addInfoRow(pnlFormCard, gbc, row, "Khách hàng:", datLich.getTenKhach() != null ? datLich.getTenKhach() : "-", false);
            row = addInfoRow(pnlFormCard, gbc, row, "Điện thoại:", datLich.getSoDienThoaiKhach() != null ? datLich.getSoDienThoaiKhach() : "-", false);
            row = addInfoRow(pnlFormCard, gbc, row, "Ngày đặt:",   formatDate(datLich.getNgayDat()), false);
            row = addInfoRow(pnlFormCard, gbc, row, "Khung giờ:",  datLich.getKhungGio(), false);
            row = addInfoRow(pnlFormCard, gbc, row, "Tiền sân:",   String.format("%,.0f VNĐ", datLich.getTienSan()), true);
            row = addInfoRow(pnlFormCard, gbc, row, "Trạng thái:", datLich.getTrangThaiHienThi(), false);

            row = addSeparator(pnlFormCard, gbc, row);
        }

        // ── THÊM DỊCH VỤ / ĐỒ ĂN ───────────────────────────────────────────
        row = addSectionHeader(pnlFormCard, gbc, row, "🛒 Chọn dịch vụ / đồ ăn đi kèm");

        // Nút bấm mở Dialog chọn Dịch vụ sân bóng
        JButton btnChonDichVu = new JButton("+ Chọn Dịch vụ");
        btnChonDichVu.setFont(UIConstants.FONT_BOLD);
        btnChonDichVu.setPreferredSize(new Dimension(140, 34));
        btnChonDichVu.addActionListener(e -> onOpenDichVuDialog(parent));

        lblStatusDichVu = new JLabel("Chưa chọn dịch vụ");
        lblStatusDichVu.setFont(UIConstants.FONT_SMALL);
        lblStatusDichVu.setForeground(UIConstants.TEXT_SECONDARY);

        JPanel pnlDVRow = new JPanel(new BorderLayout(8, 0));
        pnlDVRow.setOpaque(false);
        pnlDVRow.add(btnChonDichVu, BorderLayout.WEST);
        pnlDVRow.add(lblStatusDichVu, BorderLayout.CENTER);
        row = addField(pnlFormCard, gbc, row, "Dịch vụ:", pnlDVRow);

        // Nút bấm mở Dialog chọn Đồ ăn & Vật phẩm kho
        JButton btnChonDoAn = new JButton("+ Chọn Đồ ăn");
        btnChonDoAn.setFont(UIConstants.FONT_BOLD);
        btnChonDoAn.setPreferredSize(new Dimension(140, 34));
        btnChonDoAn.addActionListener(e -> onOpenDoAnDialog(parent));

        lblStatusDoAn = new JLabel("Chưa chọn đồ ăn");
        lblStatusDoAn.setFont(UIConstants.FONT_SMALL);
        lblStatusDoAn.setForeground(UIConstants.TEXT_SECONDARY);

        JPanel pnlDoAnRow = new JPanel(new BorderLayout(8, 0));
        pnlDoAnRow.setOpaque(false);
        pnlDoAnRow.add(btnChonDoAn, BorderLayout.WEST);
        pnlDoAnRow.add(lblStatusDoAn, BorderLayout.CENTER);
        row = addField(pnlFormCard, gbc, row, "Đồ ăn / Kho:", pnlDoAnRow);

        // Tổng số tiền dịch vụ phát sinh thêm
        lblTongCong = new JLabel("0 VNĐ");
        lblTongCong.setFont(UIConstants.FONT_BOLD);
        lblTongCong.setForeground(UIConstants.PRIMARY);
        addField(pnlFormCard, gbc, row, "Phát sinh thêm:", lblTongCong);

        // ── FOOTER ────────────────────────────────────────────────────────────
        JLabel lblTongFull = new JLabel();
        lblTongFull.setFont(UIConstants.FONT_SMALL);
        lblTongFull.setForeground(UIConstants.TEXT_SECONDARY);
        if (datLich != null) {
            lblTongFull.setText("Tổng tiền hiện tại: " + String.format("%,.0f VNĐ", datLich.getTongTien()));
        }

        JPanel pnlFooterLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        pnlFooterLeft.setOpaque(false);
        pnlFooterLeft.add(lblTongFull);
        pnlFooter.add(pnlFooterLeft, BorderLayout.WEST);

        JPanel pnlFooterRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        pnlFooterRight.setOpaque(false);

        // Nút Hủy
        JButton btnCancel = new JButton("Hủy");
        Utils.PageUI.styleSecondaryButton(btnCancel);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        // Nút Xác nhận bán
        JButton btnSave = new JButton(" Xác nhận bán");
        btnSave.setIcon(Utils.IconUtils.getCheckIcon(16));
        Utils.PageUI.stylePrimaryButton(btnSave);
        btnSave.addActionListener(e -> onSave());

        pnlFooterRight.add(btnCancel);
        pnlFooterRight.add(btnSave);
        pnlFooter.add(pnlFooterRight, BorderLayout.EAST);

        // Nút bấm mặc định khi nhấn Enter
        getRootPane().setDefaultButton(btnSave);
    }

    // ── Helper UI builder methods ─────────────────────────────────────────────

    /**
     * Thêm một tiêu đề phân đoạn (Section Header) trong form layout.
     *
     * @param form Panel chứa form
     * @param gbc  GridBagConstraints
     * @param row  Dòng hiện tại
     * @param text Văn bản hiển thị tiêu đề
     * @return Dòng tiếp theo
     */
    private int addSectionHeader(JPanel form, GridBagConstraints gbc, int row, String text) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 4, 4, 4);

        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.FONT_SUBTITLE);
        lbl.setForeground(UIConstants.PRIMARY);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, UIConstants.PRIMARY),
                BorderFactory.createEmptyBorder(2, 8, 2, 0)
        ));
        form.add(lbl, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 4, 5, 4);
        return row + 1;
    }

    /**
     * Thêm một đường phân cách nằm ngang giữa các nhóm trường thông tin.
     *
     * @param form Panel form
     * @param gbc  GridBagConstraints
     * @param row  Dòng hiện tại
     * @return Dòng tiếp theo
     */
    private int addSeparator(JPanel form, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(4, 0, 4, 0);

        JSeparator sep = new JSeparator();
        sep.setForeground(UIConstants.BORDER);
        form.add(sep, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 4, 5, 4);
        return row + 1;
    }

    /**
     * Thêm một dòng hiển thị thông tin chỉ đọc (Key - Value).
     *
     * @param form      Panel form
     * @param gbc       GridBagConstraints
     * @param row       Dòng hiện tại
     * @param label     Tiêu đề thuộc tính
     * @param value     Giá trị thuộc tính
     * @param highlight Có làm nổi bật màu sắc thuộc tính không
     * @return Dòng tiếp theo
     */
    private int addInfoRow(JPanel form, GridBagConstraints gbc, int row, String label, String value, boolean highlight) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.32;
        gbc.gridwidth = 1;

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BOLD);
        lbl.setForeground(UIConstants.TEXT_SECONDARY);
        form.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.68;
        JLabel val = new JLabel(value);
        if (highlight) {
            val.setFont(UIConstants.FONT_BOLD);
            val.setForeground(UIConstants.PRIMARY);
        } else {
            val.setFont(UIConstants.FONT_NORMAL);
            val.setForeground(UIConstants.TEXT_PRIMARY);
        }
        form.add(val, gbc);
        return row + 1;
    }

    /**
     * Thêm một trường nhập liệu hoặc điều khiển vào form.
     *
     * @param form  Panel form
     * @param gbc   GridBagConstraints
     * @param row   Dòng hiện tại
     * @param label Tiêu đề trường
     * @param field Thành phần UI nhập dữ liệu
     * @return Dòng tiếp theo
     */
    private int addField(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.32;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BOLD);
        lbl.setForeground(UIConstants.TEXT_PRIMARY);
        form.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.68;
        field.setPreferredSize(new Dimension(280, 36));
        form.add(field, gbc);
        return row + 1;
    }

    // ── Event handlers ────────────────────────────────────────────────────────

    /**
     * Mở hộp thoại chọn gói dịch vụ (Trọng tài, Huấn luyện viên, Đèn chiếu sáng...)
     * và cập nhật kết quả chọn vào trạng thái của dialog.
     *
     * @param parent Cửa sổ cha
     */
    private void onOpenDichVuDialog(JFrame parent) {
        ChonDichVuDialog dialog = new ChonDichVuDialog(parent);
        dialog.setInitialQuantities(currentDvMap);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            currentDvMap.clear();
            currentDvMap.putAll(dialog.getSelectedQtyMap());
            rebuildSelected();
        }
    }

    /**
     * Mở hộp thoại chọn Đồ ăn & Vật phẩm kho (Nước suối, bóng, giày, áo lưới...)
     * và cập nhật kết quả chọn vào trạng thái dialog.
     *
     * @param parent Cửa sổ cha
     */
    private void onOpenDoAnDialog(JFrame parent) {
        ChonVatPhamKhoDialog dialog = new ChonVatPhamKhoDialog(parent);
        dialog.setInitialQuantities(currentDoAnMap);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            currentDoAnMap.clear();
            currentDoAnMap.putAll(dialog.getSelectedQtyMap());
            rebuildSelected();
        }
    }

    /**
     * Tổng hợp lại danh sách tất cả các mục dịch vụ & đồ ăn đã chọn,
     * đồng thời tính toán tổng tiền chi phí bổ sung và cập nhật lại giao diện.
     */
    private void rebuildSelected() {
        selectedItems.clear();
        totalAddonCost = 0;

        // 1. Duyệt qua các dịch vụ từ DataStore
        for (DichVu dv : DataStore.get().getDichVus()) {
            int qty = currentDvMap.getOrDefault(dv.getId(), 0);
            if (qty > 0) {
                ChonDichVuDialog.SelectedItem item = new ChonDichVuDialog.SelectedItem(dv, qty);
                selectedItems.add(item);
                totalAddonCost += item.getThanhTien();
            }
        }

        // 2. Duyệt qua các vật phẩm kho hàng
        for (DichVu kho : DataStore.get().getKhoItems()) {
            int qty = currentDoAnMap.getOrDefault(kho.getId(), 0);
            if (qty > 0) {
                ChonDichVuDialog.SelectedItem item = new ChonDichVuDialog.SelectedItem(kho, qty);
                selectedItems.add(item);
                totalAddonCost += item.getThanhTien();
            }
        }

        // 3. Cập nhật các nhãn trạng thái UI
        long dvCount = currentDvMap.values().stream().filter(v -> v > 0).count();
        long doAnCount = currentDoAnMap.values().stream().filter(v -> v > 0).count();

        if (lblStatusDichVu != null) {
            lblStatusDichVu.setText(dvCount > 0 ? "Đã chọn " + dvCount + " dịch vụ" : "Chưa chọn dịch vụ");
            lblStatusDichVu.setForeground(dvCount > 0 ? UIConstants.PRIMARY : UIConstants.TEXT_SECONDARY);
        }
        if (lblStatusDoAn != null) {
            lblStatusDoAn.setText(doAnCount > 0 ? "Đã chọn " + doAnCount + " món/vật phẩm" : "Chưa chọn đồ ăn");
            lblStatusDoAn.setForeground(doAnCount > 0 ? UIConstants.PRIMARY : UIConstants.TEXT_SECONDARY);
        }
        if (lblTongCong != null) {
            lblTongCong.setText(String.format("+%,.0f VNĐ", totalAddonCost));
            lblTongCong.setForeground(totalAddonCost > 0 ? UIConstants.SUCCESS : UIConstants.TEXT_SECONDARY);
        }
    }

    /**
     * Xử lý sự kiện lưu xác nhận bán dịch vụ.
     * Hiển thị thông báo nếu chưa chọn bất kỳ mục nào.
     */
    private void onSave() {
        if (selectedItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Chưa chọn dịch vụ hoặc đồ ăn nào để bán!\nVui lòng chọn ít nhất 1 dịch vụ hoặc đồ ăn.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        confirmed = true;
        dispose();
    }

    /**
     * Định dạng chuỗi ngày định dạng yyyy-MM-dd thành dd/MM/yyyy để hiển thị.
     *
     * @param raw Chuỗi ngày gốc
     * @return Chuỗi ngày dạng dd/MM/yyyy
     */
    private String formatDate(String raw) {
        if (raw == null || raw.isBlank()) return "-";
        try {
            String[] parts = raw.trim().split("-");
            if (parts.length == 3) return parts[2] + "/" + parts[1] + "/" + parts[0];
        } catch (Exception ignored) {}
        return raw;
    }

    // ── Public getters ────────────────────────────────────────────────────────

    /**
     * Kiểm tra dialog có được xác nhận thành công hay không.
     *
     * @return true nếu đã chọn và xác nhận bán thành công
     */
    public boolean isConfirmed() { return confirmed; }

    /**
     * Lấy danh sách các đối tượng dịch vụ / đồ ăn đi kèm được chọn bán.
     *
     * @return Danh sách SelectedItem chứa dịch vụ và số lượng chọn
     */
    public List<ChonDichVuDialog.SelectedItem> getSelectedItems() { return selectedItems; }

    /**
     * Lấy tổng chi phí phát sinh bổ sung do bán dịch vụ.
     *
     * @return Tổng số tiền phát sinh (VNĐ)
     */
    public double getTotalAddonCost() { return totalAddonCost; }

    /**
     * Lấy dịch vụ đầu tiên được chọn trong danh sách.
     *
     * @return Đối tượng DichVu hoặc null nếu danh sách rỗng
     * @deprecated Dùng {@link #getSelectedItems()} để lấy toàn bộ danh sách
     */
    @Deprecated
    public DichVu getSelectedDichVu() {
        if (!selectedItems.isEmpty()) return selectedItems.get(0).getDichVu();
        return null;
    }

    /**
     * Lấy số lượng dịch vụ đầu tiên được chọn.
     *
     * @return Số lượng chọn hoặc 0 nếu danh sách rỗng
     * @deprecated Dùng {@link #getSelectedItems()} để lấy toàn bộ danh sách
     */
    @Deprecated
    public int getSoLuong() {
        if (!selectedItems.isEmpty()) return selectedItems.get(0).getSoLuong();
        return 0;
    }
}
