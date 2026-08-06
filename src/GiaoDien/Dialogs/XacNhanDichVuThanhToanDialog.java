package GiaoDien.Dialogs;

import Model.DatLich;
import Model.DichVu;
import Utils.DataStore;
import Utils.PageUI;
import Utils.UIConstants;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Hộp thoại (JDialog) xác nhận và tích chọn lại các dịch vụ / đồ ăn phát sinh thực tế
 * theo Ghi chú hoặc yêu cầu của khách hàng trước khi chuyển trạng thái phiếu đặt sang "Hoàn thành (Đã thanh toán)".
 * <p>
 * Dialog hỗ trợ 2 Tab:
 * 1. Dịch vụ sân bóng (Trọng tài, HLV, Quay phim...)
 * 2. Đồ ăn / Mặt hàng kho (Nước uống, Áo bib, Bóng...)
 * Tự động tính toán tổng tiền thanh toán mới và cập nhật chuỗi ghi chú dịch vụ đi kèm vào phiếu đặt lịch.
 * </p>
 */
public class XacNhanDichVuThanhToanDialog extends JDialog {

    /** Đối tượng phiếu đặt lịch cần hoàn tất thanh toán */
    private final DatLich booking;

    /** Cờ xác nhận người dùng đồng ý hoàn tất dịch vụ & thanh toán */
    private boolean confirmed = false;

    /** Map danh sách các dịch vụ được chọn (key: ID dịch vụ, value: 1 nếu chọn, 0 nếu không chọn) */
    private final Map<Integer, Integer> checkedDvMap = new HashMap<>();

    /** Map danh sách hàng kho / đồ ăn được chọn (key: ID hàng kho, value: số lượng chọn) */
    private final Map<Integer, Integer> checkedDoAnMap = new HashMap<>();

    /** Model bảng dữ liệu dịch vụ riêng */
    private DefaultTableModel modelDv;

    /** Model bảng dữ liệu đồ ăn / mặt hàng kho */
    private DefaultTableModel modelDoAn;

    /** Bảng JTable hiển thị dịch vụ riêng */
    private JTable tableDv;

    /** Bảng JTable hiển thị đồ ăn / mặt hàng kho */
    private JTable tableDoAn;

    /** Nhãn hiển thị tiền sân bóng */
    private JLabel lblTienSan;

    /** Nhãn hiển thị tổng tiền dịch vụ / đồ ăn phát sinh */
    private JLabel lblTienDichVu;

    /** Nhãn hiển thị tổng số tiền phải thanh toán */
    private JLabel lblTongTien;

    /** Tổng giá trị dịch vụ & đồ ăn kèm theo hiện tại */
    private double totalSvcCost = 0;

    /**
     * Khởi tạo dialog xác nhận tích chọn lại dịch vụ trước khi thanh toán.
     *
     * @param parent  Cửa sổ cha (JFrame)
     * @param booking Đối tượng phiếu đặt lịch {@link DatLich}
     */
    public XacNhanDichVuThanhToanDialog(JFrame parent, DatLich booking) {
        super(parent, "Xác nhận & Tích lại dịch vụ thanh toán - " + (booking != null ? booking.getMaLichDat() : ""), true);
        this.booking = booking;

        // Đổ thông tin các dịch vụ và đồ ăn đã được tích chọn trước đó từ phiếu đặt
        if (booking != null) {
            if (booking.getSelectedDvMap() != null) checkedDvMap.putAll(booking.getSelectedDvMap());
            if (booking.getSelectedDoAnMap() != null) checkedDoAnMap.putAll(booking.getSelectedDoAnMap());
        }

        // Khởi tạo thành phần UI
        initUI(parent);
    }

    /**
     * Xây dựng cấu trúc giao diện tab, các bảng JTable và panel tổng hợp chi phí.
     *
     * @param parent Cửa sổ cha dùng căn giữa dialog
     */
    private void initUI(JFrame parent) {
        setSize(760, 680);
        if (parent != null) setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // --- Header Panel ---
        JPanel pnlHeader = PageUI.createPageHeader(
                " TÍCH CHỌN DỊCH VỤ THEO GHI CHÚ KHI THANH TOÁN",
                "Phiếu đặt: " + (booking != null ? booking.getMaLichDat() : "") + " | Khách: " + (booking != null ? booking.getTenKhach() : "")
        );
        pnlHeader.setBackground(UIConstants.PRIMARY);
        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // --- Center Panel ---
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Khung hiển thị Ghi chú của phiếu đặt lịch
        JPanel pnlInfoNoteCard = new JPanel(new BorderLayout(8, 8));
        pnlInfoNoteCard.setBackground(Color.WHITE);
        pnlInfoNoteCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        String noteText = (booking != null && booking.getGhiChu() != null && !booking.getGhiChu().isBlank())
                ? booking.getGhiChu() : "(Không có ghi chú thêm)";

        JLabel lblNoteHeader = new JLabel("📌 Ghi chú lần đặt sân:");
        lblNoteHeader.setFont(UIConstants.FONT_BOLD);
        lblNoteHeader.setForeground(UIConstants.PRIMARY);

        JTextArea txtNoteDisplay = new JTextArea(noteText);
        txtNoteDisplay.setFont(UIConstants.FONT_NORMAL);
        txtNoteDisplay.setEditable(false);
        txtNoteDisplay.setLineWrap(true);
        txtNoteDisplay.setWrapStyleWord(true);
        txtNoteDisplay.setBackground(new Color(248, 249, 250));
        txtNoteDisplay.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        pnlInfoNoteCard.add(lblNoteHeader, BorderLayout.NORTH);
        pnlInfoNoteCard.add(txtNoteDisplay, BorderLayout.CENTER);
        pnlCenter.add(pnlInfoNoteCard, BorderLayout.NORTH);

        // Tabs cho Dịch vụ & Đồ ăn kho
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setFont(UIConstants.FONT_BOLD);

        // --- Tab 1: Dịch vụ sân bóng ---
        JPanel pnlDvTab = new JPanel(new BorderLayout());
        modelDv = new DefaultTableModel(new String[]{"Mã DV", "Tên dịch vụ", "Đơn giá", "Tích chọn (Thanh toán)"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 3; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? Boolean.class : String.class;
            }
        };
        tableDv = new JTable(modelDv);
        PageUI.styleTable(tableDv);
        tableDv.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableDv.getColumnModel().getColumn(1).setPreferredWidth(280);
        tableDv.getColumnModel().getColumn(2).setPreferredWidth(140);
        tableDv.getColumnModel().getColumn(3).setPreferredWidth(120);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        tableDv.getColumnModel().getColumn(0).setCellRenderer(centerRender);

        // Đăng ký listener sự kiện thay đổi dữ liệu bảng dịch vụ
        modelDv.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) {
                int r = e.getFirstRow();
                if (r >= 0 && r < modelDv.getRowCount()) {
                    String maStr = modelDv.getValueAt(r, 0).toString();
                    DichVu item = DataStore.get().getDichVus().stream()
                            .filter(d -> maStr.equalsIgnoreCase(d.getMaDichVu()))
                            .findFirst().orElse(null);
                    if (item != null) {
                        boolean isChecked = Boolean.TRUE.equals(modelDv.getValueAt(r, 3));
                        checkedDvMap.put(item.getId(), isChecked ? 1 : 0);
                        recalcTotal();
                    }
                }
            }
        });
        pnlDvTab.add(new JScrollPane(tableDv), BorderLayout.CENTER);
        tabPane.addTab("1. Dịch vụ riêng (Trọng tài, HLV, Quay phim...)", pnlDvTab);

        // --- Tab 2: Đồ ăn / Vật tư kho ---
        JPanel pnlDoAnTab = new JPanel(new BorderLayout());
        modelDoAn = new DefaultTableModel(new String[]{"Mã SP", "Tên sản phẩm / đồ ăn", "Đơn giá", "Tồn kho", "Số lượng dùng (Thanh toán)"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return c == 4; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 4 ? Integer.class : String.class;
            }
        };
        tableDoAn = new JTable(modelDoAn);
        PageUI.styleTable(tableDoAn);
        tableDoAn.getColumnModel().getColumn(0).setPreferredWidth(70);
        tableDoAn.getColumnModel().getColumn(1).setPreferredWidth(260);
        tableDoAn.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableDoAn.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableDoAn.getColumnModel().getColumn(4).setPreferredWidth(140);
        tableDoAn.getColumnModel().getColumn(0).setCellRenderer(centerRender);

        // Đăng ký listener sự kiện thay đổi số lượng sử dụng mặt hàng kho
        modelDoAn.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 4) {
                int r = e.getFirstRow();
                if (r >= 0 && r < modelDoAn.getRowCount()) {
                    String maStr = modelDoAn.getValueAt(r, 0).toString();
                    DichVu item = DataStore.get().getKhoItems().stream()
                            .filter(k -> maStr.equalsIgnoreCase(k.getMaDichVu()))
                            .findFirst().orElse(null);
                    if (item != null) {
                        Object val = modelDoAn.getValueAt(r, 4);
                        int qty = (val instanceof Integer num) ? num : 0;
                        // Cảnh báo nếu số lượng chọn vượt quá số lượng tồn kho khả dụng
                        if (qty > item.getSoLuongTon()) {
                            JOptionPane.showMessageDialog(this, "Số lượng chọn vượt quá số tồn kho (" + item.getSoLuongTon() + ")!", "Cảnh báo kho", JOptionPane.WARNING_MESSAGE);
                            qty = item.getSoLuongTon();
                            modelDoAn.setValueAt(qty, r, 4);
                        }
                        checkedDoAnMap.put(item.getId(), Math.max(0, qty));
                        recalcTotal();
                    }
                }
            }
        });
        pnlDoAnTab.add(new JScrollPane(tableDoAn), BorderLayout.CENTER);
        tabPane.addTab("2. Đồ ăn / Vật phẩm kho (Nước, Áo bib, Bóng...)", pnlDoAnTab);

        pnlCenter.add(tabPane, BorderLayout.CENTER);
        getContentPane().add(pnlCenter, BorderLayout.CENTER);

        // --- Footer Summary & Buttons ---
        JPanel pnlFooter = new JPanel(new BorderLayout(12, 0));
        pnlFooter.setBackground(UIConstants.BG);
        pnlFooter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JPanel pnlSummary = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        pnlSummary.setOpaque(false);

        double tienSanVal = booking != null ? booking.getTienSan() : 0;
        lblTienSan = new JLabel(String.format("Tiền sân: %,.0f đ", tienSanVal));
        lblTienSan.setFont(UIConstants.FONT_BOLD);

        lblTienDichVu = new JLabel("Tiền dịch vụ/kho: 0 đ");
        lblTienDichVu.setFont(UIConstants.FONT_BOLD);
        lblTienDichVu.setForeground(UIConstants.PRIMARY);

        lblTongTien = new JLabel("Tổng thanh toán: 0 đ");
        lblTongTien.setFont(UIConstants.FONT_TITLE);
        lblTongTien.setForeground(UIConstants.SUCCESS);

        pnlSummary.add(lblTienSan);
        pnlSummary.add(new JLabel(" | "));
        pnlSummary.add(lblTienDichVu);
        pnlSummary.add(new JLabel(" | "));
        pnlSummary.add(lblTongTien);

        pnlFooter.add(pnlSummary, BorderLayout.WEST);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setOpaque(false);

        // Nút Hủy
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFont(UIConstants.FONT_BUTTON);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        // Nút Xác nhận & Thanh toán
        JButton btnConfirm = new JButton("✔ Xác nhận & Thanh toán");
        btnConfirm.setFont(UIConstants.FONT_BUTTON);
        btnConfirm.setBackground(UIConstants.SUCCESS);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.addActionListener(e -> onConfirm());

        pnlButtons.add(btnCancel);
        pnlButtons.add(btnConfirm);

        pnlFooter.add(pnlButtons, BorderLayout.EAST);
        getContentPane().add(pnlFooter, BorderLayout.SOUTH);

        // Nạp danh sách dữ liệu lên bảng
        reloadTables();
    }

    /**
     * Tải dữ liệu các dịch vụ và mặt hàng kho vào 2 bảng JTable tương ứng.
     */
    private void reloadTables() {
        modelDv.setRowCount(0);
        for (DichVu dv : DataStore.get().getDichVus()) {
            int qty = checkedDvMap.getOrDefault(dv.getId(), 0);
            modelDv.addRow(new Object[]{
                    dv.getMaDichVu(),
                    dv.getTenDichVu(),
                    String.format("%,.0f đ", dv.getDonGia()),
                    qty > 0
            });
        }

        modelDoAn.setRowCount(0);
        for (DichVu item : DataStore.get().getKhoItems()) {
            int qty = checkedDoAnMap.getOrDefault(item.getId(), 0);
            modelDoAn.addRow(new Object[]{
                    item.getMaDichVu(),
                    item.getTenDichVu(),
                    String.format("%,.0f đ", item.getDonGia()),
                    item.getSoLuongTon(),
                    qty
            });
        }

        recalcTotal();
    }

    /**
     * Tính toán lại tổng chi phí dịch vụ/kho và cập nhật số tiền hiển thị trên các nhãn tổng cộng.
     */
    private void recalcTotal() {
        totalSvcCost = 0;
        for (DichVu dv : DataStore.get().getDichVus()) {
            int qty = checkedDvMap.getOrDefault(dv.getId(), 0);
            if (qty > 0) {
                totalSvcCost += dv.getDonGia() * qty;
            }
        }
        for (DichVu item : DataStore.get().getKhoItems()) {
            int qty = checkedDoAnMap.getOrDefault(item.getId(), 0);
            if (qty > 0) {
                totalSvcCost += item.getDonGia() * qty;
            }
        }

        double tienSanVal = booking != null ? booking.getTienSan() : 0;
        double totalVal = tienSanVal + totalSvcCost;

        if (lblTienDichVu != null) lblTienDichVu.setText(String.format("Tiền dịch vụ/kho: %,.0f VNĐ", totalSvcCost));
        if (lblTongTien != null) lblTongTien.setText(String.format("Tổng thanh toán: %,.0f VNĐ", totalVal));
    }

    /**
     * Xử lý xác nhận lưu danh sách dịch vụ và đồ ăn đã chọn vào đối tượng phiếu đặt lịch.
     */
    private void onConfirm() {
        if (booking != null) {
            booking.setSelectedDvMap(checkedDvMap);
            booking.setSelectedDoAnMap(checkedDoAnMap);

            StringBuilder newDichVuKem = new StringBuilder();
            double newSvcTotal = 0;

            // Xây dựng mô tả chi tiết dịch vụ riêng kèm tiền
            for (DichVu dv : DataStore.get().getDichVus()) {
                int qty = checkedDvMap.getOrDefault(dv.getId(), 0);
                if (qty > 0) {
                    double c = dv.getDonGia() * qty;
                    newSvcTotal += c;
                    if (newDichVuKem.length() > 0) newDichVuKem.append("\n");
                    newDichVuKem.append(String.format("%s (x%d): %,.0f VNĐ", dv.getTenDichVu(), qty, c));
                }
            }

            // Xây dựng mô tả chi tiết đồ ăn / hàng kho kèm tiền
            for (DichVu item : DataStore.get().getKhoItems()) {
                int qty = checkedDoAnMap.getOrDefault(item.getId(), 0);
                if (qty > 0) {
                    double c = item.getDonGia() * qty;
                    newSvcTotal += c;
                    if (newDichVuKem.length() > 0) newDichVuKem.append("\n");
                    newDichVuKem.append(String.format("%s (x%d): %,.0f VNĐ", item.getTenDichVu(), qty, c));
                }
            }

            booking.setDichVuKem(newDichVuKem.toString());
            booking.setTienDichVu(newSvcTotal);
            booking.recalc();
        }

        confirmed = true;
        dispose();
    }

    /**
     * Trả về cờ xác nhận người dùng đã đồng ý lưu và hoàn tất thanh toán.
     *
     * @return {@code true} nếu đã xác nhận, ngược lại {@code false}
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}
