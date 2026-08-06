package GiaoDien.Panels;

import GiaoDien.Dialogs.*;

import Model.DichVu;
import Utils.DataStore;
import Utils.PageUI;
import Utils.SimpleDocListener;
import Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Panel Quản lý kho hàng & Vật tư sân bóng (QuanLyKhoPanel).
 * <p>
 * Thực thi Use Case Quản lý kho:
 * Quản lý danh sách hàng hóa kho, vật tư (Nước suối, lưới bóng, găng tay, bóng thi đấu,...),
 * thực hiện các thao tác xem chi tiết, thêm mặt hàng mới, nhập kho bổ sung, xuất kho bán hàng
 * và kiểm tra tồn kho cảnh báo mức tối thiểu.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class QuanLyKhoPanel extends javax.swing.JPanel {

    /**
     * Model dữ liệu bảng vật tư & hàng hóa trong kho.
     */
    private DefaultTableModel model;

    /**
     * Bảng hiển thị danh sách hàng hóa trong kho.
     */
    private JTable table;

    /**
     * Nhãn hiển thị số lượng mặt hàng và số mặt hàng sắp hết.
     */
    private JLabel lblCount;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Panel thân nội dung chính */
    private javax.swing.JPanel pnlBody;
    /** Panel bao bọc phần header tiêu đề trang */
    private javax.swing.JPanel pnlHeaderWrap;
    /** Panel hình card chứa bảng kho hàng */
    private javax.swing.JPanel pnlTableCard;
    /** Panel thanh công cụ chứa các nút bấm nhập kho, xuất kho, kiểm tra tồn */
    private javax.swing.JPanel pnlToolbar;
    // End of variables declaration//GEN-END:variables

    /**
     * Khởi tạo giao diện Quản lý kho mới.
     */
    public QuanLyKhoPanel() {
        initComponents();
        customInit();
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     * Khởi tạo linh kiện giao diện tự động bởi NetBeans.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeaderWrap = new javax.swing.JPanel();
        pnlBody = new javax.swing.JPanel();
        pnlToolbar = new javax.swing.JPanel();
        pnlTableCard = new javax.swing.JPanel();

        setBackground(UIConstants.BG);
        setLayout(new java.awt.BorderLayout());

        pnlHeaderWrap.setOpaque(false);
        pnlHeaderWrap.setLayout(new java.awt.BorderLayout());
        add(pnlHeaderWrap, java.awt.BorderLayout.NORTH);

        pnlBody.setBackground(UIConstants.BG);
        pnlBody.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlBody.setLayout(new java.awt.BorderLayout(0, 12));

        pnlToolbar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));
        pnlBody.add(pnlToolbar, java.awt.BorderLayout.NORTH);

        pnlTableCard.setLayout(new java.awt.BorderLayout(0, 8));
        pnlBody.add(pnlTableCard, java.awt.BorderLayout.CENTER);

        add(pnlBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cấu hình thiết lập giao diện tùy chỉnh và khởi tạo bảng danh sách kho.
     */
    private void customInit() {
        pnlHeaderWrap.add(PageUI.createPageHeader("Quản lý kho hàng & Vật tư",
                "Quản lý vật phẩm: Nước suối, lưới bóng, găng tay, bóng thi đấu, trang thiết bị..."), BorderLayout.CENTER);

        // Khởi tạo Model & JTable quản lý kho
        model = new DefaultTableModel(
                new String[]{"Mã HH", "Tên hàng hóa", "Số lượng", "Đơn giá", "Nhà cung cấp"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        PageUI.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);

        lblCount = new JLabel();
        lblCount.setFont(UIConstants.FONT_SMALL);
        lblCount.setForeground(UIConstants.TEXT_SECONDARY);

        buildToolbar();
        buildTableCard();

        reload();
    }

    /**
     * Xây dựng thanh công cụ chứa các nút Nhập kho, Kiểm tra tồn kho và Xuất kho.
     */
    private void buildToolbar() {
        pnlToolbar.setLayout(new BorderLayout());
        pnlToolbar.setOpaque(false);

        // Phía TRÁI: Nhập kho / Kiểm tra tồn kho
        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlLeft.setOpaque(false);

        JButton btnNhap = new javax.swing.JButton(" Nhập kho");
        btnNhap.setIcon(Utils.IconUtils.getAddIcon(16));
        PageUI.stylePrimaryButton(btnNhap);
        btnNhap.addActionListener(e -> onNhapKho());

        JButton btnKiemTra = new javax.swing.JButton(" Kiểm tra tồn kho");
        btnKiemTra.setIcon(Utils.IconUtils.getCheckIcon(16));
        PageUI.styleSecondaryButton(btnKiemTra);
        btnKiemTra.addActionListener(e -> onKiemTraTon());

        pnlLeft.add(btnNhap);
        pnlLeft.add(btnKiemTra);

        // Phía PHẢI: Xuất kho
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlRight.setOpaque(false);

        JButton btnXuat = new javax.swing.JButton(" Xuất kho");
        btnXuat.setIcon(Utils.IconUtils.getExportIcon(16));
        PageUI.styleSecondaryButton(btnXuat);
        btnXuat.addActionListener(e -> onXuatKho());

        pnlRight.add(btnXuat);

        pnlToolbar.add(pnlLeft, BorderLayout.WEST);
        pnlToolbar.add(pnlRight, BorderLayout.EAST);
    }

    /**
     * Xây dựng card chứa bảng danh sách mặt hàng kho.
     */
    private void buildTableCard() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel t = new JLabel("Danh sách mặt hàng vật tư trong kho");
        t.setFont(UIConstants.FONT_SUBTITLE);
        t.setForeground(UIConstants.PRIMARY);
        top.add(t, BorderLayout.WEST);
        top.add(lblCount, BorderLayout.EAST);
        pnlTableCard.add(top, BorderLayout.NORTH);
        pnlTableCard.add(new javax.swing.JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Nạp lại danh sách vật tư kho hàng từ DataStore và tính toán số mặt hàng sắp hết.
     */
    public void reload() {
        model.setRowCount(0);
        List<DichVu> list = DataStore.get().getKhoItems();
        int n = 0;
        int low = 0;
        for (DichVu d : list) {
            if (d.isSapHet()) low++;
            model.addRow(new Object[]{
                    "HH" + d.getMaHangHoa(), d.getTenHangHoa(), d.getSoLuongTon(),
                    String.format("%,.0f VNĐ", d.getDonGia()), d.getNhaCungCap()
            });
            n++;
        }
        lblCount.setText(n + " mặt hàng  |  " + low + " sắp hết hàng");
    }

    /**
     * Lấy đối tượng mặt hàng DichVu đang được chọn trong bảng kho.
     * 
     * @return DichVu hoặc null nếu chưa chọn dòng nào
     */
    private DichVu selected() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        Object val = model.getValueAt(row, 0);
        String maStr = val != null ? val.toString().replace("HH", "") : "";
        int id;
        try {
            id = Integer.parseInt(maStr);
        } catch (Exception e) {
            return null;
        }
        return DataStore.get().getKhoItems().stream().filter(d -> d.getId() == id).findFirst().orElse(null);
    }

    /**
     * Xem thông tin chi tiết các mặt hàng hoặc mặt hàng kho được chọn.
     */
    private void onViewInfo() {
        DichVu sel = selected();
        if (sel == null) {
            StringBuilder sb = new StringBuilder("===== XEM DANH SÁCH KHO VẬT TƯ =====\n");
            for (DichVu d : DataStore.get().getKhoItems()) {
                sb.append(String.format("• [HH%d] %s | Tồn: %d | Đơn giá: %s | NCC: %s\n",
                        d.getMaHangHoa(), d.getTenHangHoa(), d.getSoLuongTon(),
                        String.format("%,.0f VNĐ", d.getDonGia()), d.getNhaCungCap()));
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Xem danh sách kho", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String info = "======== THÔNG TIN MẶT HÀNG KHO ========\n"
                + "• Mã hàng hóa  : HH" + sel.getMaHangHoa() + "\n"
                + "• Tên hàng hóa : " + sel.getTenHangHoa() + "\n"
                + "• Số lượng : " + sel.getSoLuongTon() + "\n"
                + "• Đơn giá      : " + String.format("%,.0f VNĐ", sel.getDonGia()) + "\n"
                + "• Nhà cung cấp : " + sel.getNhaCungCap() + "\n"
                + "======================================";
        JOptionPane.showMessageDialog(this, info, "Thông tin mặt hàng kho", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Xử lý thực hiện xuất kho cho mặt hàng kho được chọn.
     * Kiểm tra số lượng tồn kho khả dụng trước khi trừ tồn.
     */
    private void onXuatKho() {
        DichVu sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Chọn mặt hàng cần xuất kho.");
            return;
        }
        String input = JOptionPane.showInputDialog(this,
                "Mặt hàng: " + sel.getTenDichVu()
                        + "\nTồn hiện tại: " + sel.getSoLuongTon() + " " + sel.getDonVi()
                        + "\nNhập số lượng xuất kho:",
                "Xuất kho — " + sel.getTenDichVu(), JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.isBlank()) return;
        int sl;
        try {
            sl = Integer.parseInt(input.trim());
            if (sl <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ.");
            return;
        }
        // Kiểm tra tồn kho có đủ số lượng xuất không
        if (sl > sel.getSoLuongTon()) {
            JOptionPane.showMessageDialog(this,
                    "KIỂM TRA TỒN KHO — TỪ CHỐI XUẤT\n"
                            + "• Yêu cầu: " + sl + "\n"
                            + "• Tồn: " + sel.getSoLuongTon() + "\n"
                            + "Không đủ hàng trong kho.",
                    "Kiểm tra tồn kho", JOptionPane.ERROR_MESSAGE);
            return;
        }
        sel.xuatKho(sl);
        reload();
        JOptionPane.showMessageDialog(this,
                "XUẤT KHO THÀNH CÔNG\n• " + sel.getTenDichVu()
                        + "\n• −" + sl + " → tồn còn lại: " + sel.getSoLuongTon()
                        + (sel.isSapHet() ? "\n[Cảnh báo] Tồn dưới mức tối thiểu!" : ""),
                "Kết quả xuất kho", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Thống kê kiểm tra toàn bộ danh sách kho và cảnh báo các mặt hàng chạm hoặc dưới mức tồn tối thiểu.
     */
    private void onKiemTraTon() {
        List<DichVu> low = DataStore.get().getKhoItems().stream()
                .filter(DichVu::isSapHet).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder("===== KIỂM TRA TỒN KHO VẬT TƯ =====\n\n");
        sb.append(String.format("Tổng mặt hàng: %d\n", DataStore.get().getKhoItems().size()));
        sb.append(String.format("Sắp hết / dưới ngưỡng: %d\n\n", low.size()));
        if (low.isEmpty()) {
            sb.append("[OK] Tất cả mặt hàng đủ tồn kho.\n");
        } else {
            sb.append("--- Cần nhập kho ---\n");
            for (DichVu d : low) {
                sb.append(String.format("[!] %s: tồn %d (tối thiểu %d) %s\n",
                        d.getTenDichVu(), d.getSoLuongTon(), d.getTonToiThieu(), d.getDonVi()));
            }
        }
        sb.append("\n==================================");
        JOptionPane.showMessageDialog(this, sb.toString(), "Kiểm tra tồn kho", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mở tùy chọn nhập kho (Nhập mới mặt hàng chưa có hoặc Nhập thêm số lượng cho mặt hàng có sẵn).
     */
    private void onNhapKho() {
        String[] options = {"➕ Nhập mới mặt hàng", "📦 Nhập thêm số lượng (Hàng có sẵn)", "Hủy"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Vui lòng chọn hình thức nhập kho:",
                "Tùy chọn nhập kho",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            onAddMoi();
        } else if (choice == 1) {
            onAddDaCo();
        }
    }

    /**
     * Mở thoại tạo mới một mặt hàng vật tư kho chưa có trong danh mục.
     */
    private void onAddMoi() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        KhoFormDialog dialog = new KhoFormDialog(parent, null);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        DichVu form = dialog.getResult();
        List<String> existingCodes = DataStore.get().getKhoItems().stream().map(d -> "HH" + d.getMaHangHoa()).toList();
        String maCode = Utils.CodeGen.next("HH", existingCodes, 3);
        form.setMaDichVu(maCode);
        form.setLoaiDichVu("Vật tư kho");
        DataStore.get().getKhoItems().add(form);
        if (DataStore.isUseDatabase()) {
            try { new DAO.DichVuDAO().insert(form); } catch (Exception ignored) {}
        }
        reload();

        JOptionPane.showMessageDialog(this,
                "THÊM MẶT HÀNG MỚI THÀNH CÔNG\n"
                        + "• Mã hàng: " + form.getMaDichVu() + "\n"
                        + "• Tên hàng: " + form.getTenDichVu() + "\n"
                        + "• Giá: " + String.format("%,.0f VNĐ", (double) form.getDonGia()) + "\n"
                        + "• Số lượng: " + form.getSoLuongTon(),
                "Kết quả cập nhật kho", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mở thoại chọn mặt hàng kho đã có sẵn và cộng thêm số lượng nhập kho.
     */
    private void onAddDaCo() {
        List<DichVu> list = DataStore.get().getKhoItems();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kho hiện tại chưa có mặt hàng nào. Vui lòng sử dụng 'Thêm mới mặt hàng'.");
            return;
        }

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        DichVu preSelected = selected();
        ChonNhapKhoDialog dialog = new ChonNhapKhoDialog(parent, preSelected);
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) return;

        DichVu sel = dialog.getSelectedVatPham();
        int sl = dialog.getSoLuongNhap();

        if (sel == null || sl <= 0) return;

        sel.nhapKho(sl);
        if (DataStore.isUseDatabase()) {
            try { new DAO.DichVuDAO().update(sel); } catch (Exception ignored) {}
        }
        reload();

        JOptionPane.showMessageDialog(this,
                "CẬP NHẬT NHẬP KHO THÀNH CÔNG\n"
                        + "• Mặt hàng: " + sel.getTenDichVu() + "\n"
                        + "• Mã hàng: HH" + sel.getMaHangHoa() + "\n"
                        + "• Đã cộng thêm: +" + sl + " " + sel.getDonVi() + "\n"
                        + "• Tổng tồn kho hiện tại: " + sel.getSoLuongTon() + " " + sel.getDonVi(),
                "Kết quả cập nhật kho", JOptionPane.INFORMATION_MESSAGE);
    }
}
