package GiaoDien.Panels;

import GiaoDien.Dialogs.*;
import Utils.PageUI;

import Model.BaoTri;
import Model.KhuVucSan;
import Utils.DataStore;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

/**
 * Panel Quản lý khu vực sân bóng (QuanLyKhuVucPanel).
 * <p>
 * Thực thi Use Case Quản lý khu vực sân bóng:
 * Quản lý danh sách các khu vực sân bóng (Sân 5 người, Sân 7 người, Sân 11 người),
 * thực hiện các chức năng xem, thêm sân mới, sửa giá thuê/loại sân/trạng thái hoạt động, xóa sân,
 * đồng thời kết nối tự động với quy trình bảo trì sân.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class QuanLyKhuVucPanel extends javax.swing.JPanel {

    /**
     * Model dữ liệu bảng khu vực sân bóng.
     */
    private DefaultTableModel model;

    /**
     * Bảng hiển thị danh sách các khu vực sân bóng.
     */
    private JTable table;

    /**
     * Nhãn hiển thị số lượng khu vực sân bóng.
     */
    private JLabel lblCount;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Panel thân nội dung chính */
    private javax.swing.JPanel pnlBody;
    /** Panel bao bọc phần header tiêu đề trang */
    private javax.swing.JPanel pnlHeaderWrap;
    /** Panel hình card chứa bảng danh sách khu vực sân */
    private javax.swing.JPanel pnlTableCard;
    /** Panel thanh công cụ chứa các nút chức năng thêm, sửa, xóa sân */
    private javax.swing.JPanel pnlToolbar;
    // End of variables declaration//GEN-END:variables

    /**
     * Khởi tạo giao diện Quản lý khu vực sân bóng mới.
     */
    public QuanLyKhuVucPanel() {
        initComponents();
        customInit();
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     * Khởi tạo linh kiện giao diện tự động bởi NetBeans GUI Builder.
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

        pnlToolbar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 4));
        pnlBody.add(pnlToolbar, java.awt.BorderLayout.NORTH);

        pnlTableCard.setLayout(new java.awt.BorderLayout(0, 8));
        pnlBody.add(pnlTableCard, java.awt.BorderLayout.CENTER);

        add(pnlBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cấu hình khởi tạo giao diện tùy chỉnh và bảng hiển thị danh sách khu vực sân bóng.
     */
    private void customInit() {
        pnlHeaderWrap.add(PageUI.createPageHeader("Quản lý khu vực sân bóng",
                "Quản lý khu vực sân bóng - Kết quả cập nhật khu vực"), BorderLayout.CENTER);

        // Khởi tạo Model & JTable quản lý khu vực sân
        model = new DefaultTableModel(
                new String[]{"Mã sân", "Tên sân", "Loại sân", "Giá/giờ", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        PageUI.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        lblCount = new JLabel();
        lblCount.setFont(UIConstants.FONT_SMALL);
        lblCount.setForeground(UIConstants.TEXT_SECONDARY);

        buildToolbar();
        buildTableCard();
        reload();
    }

    /**
     * Xây dựng thanh công cụ nút bấm chức năng thao tác sân bóng.
     */
    private void buildToolbar() {
        pnlToolbar.setLayout(new BorderLayout(0, 6));
        pnlToolbar.setOpaque(false);

        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlLeft.setOpaque(false);

        JButton btnAdd = new javax.swing.JButton(" Thêm sân");
        btnAdd.setIcon(Utils.IconUtils.getAddIcon(16));
        PageUI.stylePrimaryButton(btnAdd);
        btnAdd.addActionListener(e -> onAdd());

        JButton btnEdit = new javax.swing.JButton(" Sửa");
        btnEdit.setIcon(Utils.IconUtils.getEditIcon(16));
        PageUI.styleSecondaryButton(btnEdit);
        btnEdit.addActionListener(e -> onEdit());

        JButton btnDelete = new javax.swing.JButton(" Xóa");
        btnDelete.setIcon(Utils.IconUtils.getDeleteIcon(16));
        PageUI.styleDangerButton(btnDelete);
        btnDelete.addActionListener(e -> onDelete());

        pnlLeft.add(btnAdd);
        pnlLeft.add(btnEdit);
        pnlLeft.add(btnDelete);

        pnlToolbar.add(pnlLeft, BorderLayout.WEST);
    }

    /**
     * Xây dựng card chứa bảng danh sách khu vực sân bóng.
     */
    private void buildTableCard() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel t = new JLabel("Danh sách khu vực sân");
        t.setFont(UIConstants.FONT_SUBTITLE);
        t.setForeground(UIConstants.PRIMARY);
        top.add(t, BorderLayout.WEST);
        top.add(lblCount, BorderLayout.EAST);
        pnlTableCard.add(top, BorderLayout.NORTH);
        pnlTableCard.add(new javax.swing.JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Nạp lại danh sách các khu vực sân bóng từ DataStore.
     */
    public void reload() {
        model.setRowCount(0);
        List<KhuVucSan> list = DataStore.get().getKhuVucs();
        for (KhuVucSan k : list) {
            String ttStr = DataStore.get().getTrangThaiSanHienTai(k);
            model.addRow(new Object[]{
                    k.getMaSan(), k.getTenSan(), k.getLoaiSanHienThi(),
                    String.format("%,.0f VNĐ", (double) (k.getGiaThueTheoGio())), ttStr
            });
        }
        lblCount.setText(list.size() + " khu vực");
    }

    /**
     * Lấy đối tượng KhuVucSan tương ứng với dòng được chọn trong bảng.
     * 
     * @return KhuVucSan hoặc null nếu chưa chọn dòng nào
     */
    private KhuVucSan selected() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        String maSan = (String) model.getValueAt(row, 0);
        return DataStore.get().getKhuVucs().stream().filter(k -> maSan.equals(k.getMaSan())).findFirst().orElse(null);
    }

    /**
     * Xử lý mở hộp thoại thêm mới một khu vực sân bóng.
     */
    private void onAdd() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        KhuVucFormDialog dialog = new KhuVucFormDialog(parent, null);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        KhuVucSan form = dialog.getResult();
        DataStore.get().getKhuVucs().add(form);
        if (DataStore.isUseDatabase()) {
            try { new DAO.KhuVucSanDAO().insert(form); } catch (Exception ignored) {}
        }
        reload();

        if (parent instanceof GiaoDien.MainFrame mf) {
            mf.refreshDataPanels();
        }

        JOptionPane.showMessageDialog(this,
                "Cập nhật khu vực thành công!\n• Mã: " + form.getMaSan()
                        + "\n• Tên: " + form.getTenSan()
                        + "\n• Giá: " + String.format("%,.0f VNĐ", (double) (form.getGiaThueTheoGio())) + "/giờ",
                "Kết quả cập nhật khu vực", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Xử lý mở hộp thoại chỉnh sửa thông tin khu vực sân bóng đang được chọn.
     * Đồng thời tự động phát hiện chuyển đổi trạng thái Bảo trì ↔ Hoạt động để nhắc nhở quy trình bảo trì.
     */
    private void onEdit() {
        KhuVucSan sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Chọn một khu vực để sửa.");
            return;
        }
        boolean wasBaoTri = DataStore.get().isSanBaoTri(sel);

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        KhuVucFormDialog dialog = new KhuVucFormDialog(parent, sel);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        KhuVucSan form = dialog.getResult();

        boolean isSwitchingToBaoTri = !wasBaoTri && ("BaoTri".equalsIgnoreCase(form.getTrangThai()) || "BAO_TRI".equalsIgnoreCase(form.getTrangThai()));

        // Nếu sân từ trạng thái Bảo trì quay lại Hoạt động, hỏi hoàn thành phiếu bảo trì
        if (wasBaoTri && ("SanSang".equalsIgnoreCase(form.getTrangThai()) || "HOAT_DONG".equalsIgnoreCase(form.getTrangThai()))) {
            checkAndUpdateRelatedMaintenance(sel);
        }

        sel.setMaSan(form.getMaSan());
        sel.setTenSan(form.getTenSan());
        sel.setLoaiSan(form.getLoaiSan());
        sel.setGiaThueTheoGio(form.getGiaThueTheoGio());
        sel.setTrangThai(form.getTrangThai());
        if (DataStore.isUseDatabase()) {
            try { new DAO.KhuVucSanDAO().update(sel); } catch (Exception ignored) {}
        }
        reload();

        if (parent instanceof GiaoDien.MainFrame mf) {
            mf.refreshDataPanels();
        }

        JOptionPane.showMessageDialog(this, "Đã cập nhật khu vực \"" + sel.getMaSan() + "\".",
                "Kết quả cập nhật khu vực", JOptionPane.INFORMATION_MESSAGE);

        // Nếu sân chuyển sang trạng thái Bảo trì, nhắc lập phiếu bảo trì mới
        if (isSwitchingToBaoTri) {
            promptCreateMaintenanceTicket(sel);
        }
    }

    /**
     * Xử lý xóa khu vực sân bóng đang chọn khỏi hệ thống.
     * Ràng buộc: Không cho xóa nếu sân đang có lịch đặt chờ phục vụ hoặc đang diễn ra.
     */
    private void onDelete() {
        KhuVucSan sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Chọn một khu vực sân bóng để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // KIỂM TRA LỊCH ĐẶT SÂN ĐANG DIỄN RA / CHỜ PHỤC VỤ TRÊN SÂN
        List<Model.DatLich> activeBookings = DataStore.get().getDatLichs().stream()
                .filter(d -> (d.getMaSan() != null && d.getMaSan().equalsIgnoreCase(sel.getMaSan()))
                        || (d.getTenSan() != null && d.getTenSan().toLowerCase().contains(sel.getTenSan().toLowerCase())))
                .filter(d -> !"DaHuy".equalsIgnoreCase(d.getTrangThai())
                        && !"DA_HUY".equalsIgnoreCase(d.getTrangThai())
                        && !"HoanThanh".equalsIgnoreCase(d.getTrangThai())
                        && !"HOAN_THANH".equalsIgnoreCase(d.getTrangThai()))
                .toList();

        if (!activeBookings.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[!] KHÔNG THỂ XÓA SÂN BÓNG!\n\n");
            sb.append("Sân bóng '").append(sel.getTenSan()).append("' (Mã: ").append(sel.getMaSan())
                    .append(") hiện đang có ").append(activeBookings.size()).append(" lịch đặt đang diễn ra / chờ phục vụ:\n\n");

            for (Model.DatLich d : activeBookings) {
                sb.append("• Mã lịch đặt : ").append(d.getMaLichDat()).append("\n")
                  .append("  - Khách hàng   : ").append(d.getTenKhach() != null ? d.getTenKhach() : "-")
                  .append(" (SĐT: ").append(d.getSoDienThoaiKhach() != null ? d.getSoDienThoaiKhach() : "").append(")\n")
                  .append("  - Ngày đặt     : ").append(d.getNgayDat()).append(" (Khung giờ: ").append(d.getKhungGio()).append(")\n")
                  .append("  - Trạng thái   : ").append(d.getTrangThaiHienThi()).append("\n\n");
            }
            sb.append("Vui lòng hoàn tất hoặc hủy các lịch đặt liên quan trước khi xóa sân bóng!");

            JOptionPane.showMessageDialog(this, sb.toString(), "Cảnh báo lịch đặt đang diễn ra", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Xóa sân " + sel.getMaSan() + " (" + sel.getTenSan() + ")?",
                "Xác nhận xóa sân", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

        DataStore.get().getKhuVucs().remove(sel);
        if (DataStore.isUseDatabase()) {
            try { new DAO.KhuVucSanDAO().delete(sel.getMaSan()); } catch (Exception ignored) {}
        }
        reload();

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parent instanceof GiaoDien.MainFrame mf) {
            mf.refreshDataPanels();
        }

        JOptionPane.showMessageDialog(this, "Đã xóa khu vực sân bóng thành công.", "Kết quả cập nhật khu vực", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Kiểm tra và hỏi xác nhận hoàn thành phiếu bảo trì liên quan khi sân bóng chuyển từ Bảo trì sang Hoạt động.
     */
    private void checkAndUpdateRelatedMaintenance(KhuVucSan sel) {
        if (sel == null) return;
        List<Model.BaoTri> listBT = DataStore.get().getBaoTris();
        Model.BaoTri activeMaint = listBT.stream()
                .filter(b -> !"HoanThanh".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && !"HOAN_THANH".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && !"DaHuy".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && !"HUY".equalsIgnoreCase(b.getTrangThaiPhieu())
                        && ((sel.getMaSan() != null && sel.getMaSan().equalsIgnoreCase(b.getMaSan()))
                        || (b.getTenSan() != null && b.getTenSan().toLowerCase().contains(sel.getMaSan().toLowerCase()))))
                .findFirst().orElse(null);

        if (activeMaint != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Phát hiện sân " + sel.getMaSan() + " (" + sel.getTenSan() + ") có phiếu bảo trì liên quan:\n"
                            + "• Mã phiếu  : " + activeMaint.getMaPhieuBaoTri() + "\n"
                            + "• Nội dung  : " + activeMaint.getNoiDung() + "\n"
                            + "• Trạng thái: " + activeMaint.getTrangThaiHienThi() + "\n\n"
                            + "Bạn có muốn cập nhật phiếu bảo trì này thành 'Hoàn thành' không?",
                    "Xác nhận cập nhật phiếu bảo trì",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                activeMaint.setTrangThaiPhieu("HoanThanh");
                if (DataStore.isUseDatabase()) {
                    try { new DAO.BaoTriDAO().update(activeMaint); } catch (Exception ignored) {}
                }
                JOptionPane.showMessageDialog(this,
                        "Đã cập nhật phiếu bảo trì " + activeMaint.getMaPhieuBaoTri() + " sang trạng thái 'Hoàn thành'.",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Nhắc nhở người dùng lập phiếu bảo trì mới khi sân bóng được chuyển sang trạng thái 'Bảo trì'.
     */
    private void promptCreateMaintenanceTicket(KhuVucSan sel) {
        if (sel == null) return;
        int choice = JOptionPane.showConfirmDialog(this,
                "Sân " + sel.getMaSan() + " (" + sel.getTenSan() + ") đã được chuyển sang trạng thái 'Bảo trì'.\n\n"
                        + "Bạn có muốn lập PHIẾU BẢO TRÌ mới cho sân này ngay bây giờ không?",
                "Tạo phiếu bảo trì mới",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            BaoTriFormDialog dialog = new BaoTriFormDialog(parent, null);
            dialog.setSelectedSan(sel);
            dialog.setVisible(true);

            if (dialog.isConfirmed() && dialog.getResult() != null) {
                BaoTri form = dialog.getResult();
                String ma = Utils.CodeGen.next("BT", DataStore.get().getBaoTris().stream().map(BaoTri::getMaPhieuBaoTri).toList(), 3);

                BaoTri b = new BaoTri();
                b.setMaPhieuBaoTri(ma);
                b.setMaSan(sel.getMaSan());
                b.setTenSan(sel.getTenSan());
                b.setNoiDung(form.getNoiDung());
                b.setNgayBatDau(form.getNgayBatDau());
                b.setNgayKetThuc(form.getNgayKetThuc());
                b.setTrangThaiPhieu(form.getTrangThaiPhieu());

                DataStore.get().getBaoTris().add(b);
                if (DataStore.isUseDatabase()) {
                    try { new DAO.BaoTriDAO().insert(b); } catch (Exception ignored) {}
                }

                if (parent instanceof GiaoDien.MainFrame mf) {
                    mf.refreshDataPanels();
                }

                JOptionPane.showMessageDialog(this,
                        "Đã tạo phiếu bảo trì " + ma + " cho sân " + sel.getMaSan() + " thành công!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Chuyển nhanh trạng thái hoạt động của sân được chọn.
     * 
     * @param targetStatus Trạng thái mục tiêu (SanSang, BaoTri, HOAT_DONG, BAO_TRI)
     */
    private void onQuickSetStatus(String targetStatus) {
        KhuVucSan sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sân bóng trong bảng để chuyển nhanh trạng thái.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean wasBaoTri = DataStore.get().isSanBaoTri(sel);

        if (wasBaoTri && ("SanSang".equalsIgnoreCase(targetStatus) || "HOAT_DONG".equalsIgnoreCase(targetStatus))) {
            checkAndUpdateRelatedMaintenance(sel);
        }

        sel.setTrangThai(targetStatus);
        if (DataStore.isUseDatabase()) {
            try { new DAO.KhuVucSanDAO().update(sel); } catch (Exception ignored) {}
        }

        reload();

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parent instanceof GiaoDien.MainFrame mf) {
            mf.refreshDataPanels();
        }

        String label = ("SanSang".equalsIgnoreCase(targetStatus) || "HOAT_DONG".equalsIgnoreCase(targetStatus)) ? "Hoạt động" : "Bảo trì";
        JOptionPane.showMessageDialog(this,
                "Đã chuyển nhanh trạng thái sân " + sel.getMaSan() + " (" + sel.getTenSan() + ") sang: " + label,
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        if (!wasBaoTri && ("BaoTri".equalsIgnoreCase(targetStatus) || "BAO_TRI".equalsIgnoreCase(targetStatus))) {
            promptCreateMaintenanceTicket(sel);
        }
    }
}
