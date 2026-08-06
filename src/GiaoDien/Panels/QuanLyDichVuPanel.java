package GiaoDien.Panels;

import GiaoDien.Dialogs.*;

import Model.DichVu;
import Utils.DataStore;
import Utils.PageUI;
import Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

/**
 * Panel Quản lý danh mục Dịch vụ & Tiện ích sân bóng (QuanLyDichVuPanel).
 * <p>
 * Thực thi Use Case Quản lý dịch vụ trải nghiệm & Tiện ích:
 * Quản lý danh mục các gói dịch vụ riêng như Bữa sáng vận động viên, Giặt là trang phục thi đấu,
 * Massage hồi phục cơ bắp, Thuê trọng tài, Huấn luyện viên cá nhân...
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class QuanLyDichVuPanel extends javax.swing.JPanel {

    /**
     * Model dữ liệu bảng danh mục các gói dịch vụ.
     */
    private DefaultTableModel model;

    /**
     * Bảng hiển thị danh sách dịch vụ sân bóng.
     */
    private JTable table;

    /**
     * Nhãn hiển thị số lượng gói dịch vụ trong danh mục.
     */
    private JLabel lblCount;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Panel thân nội dung chứa bảng dữ liệu */
    private javax.swing.JPanel pnlBody;
    /** Panel bao bọc phần header tiêu đề trang */
    private javax.swing.JPanel pnlHeaderWrap;
    /** Panel hình card chứa bảng danh sách dịch vụ */
    private javax.swing.JPanel pnlTableCard;
    /** Panel thanh công cụ chứa các nút chức năng thêm, sửa, xóa, xem chi tiết */
    private javax.swing.JPanel pnlToolbar;
    // End of variables declaration//GEN-END:variables

    /**
     * Khởi tạo giao diện Quản lý dịch vụ mới.
     */
    public QuanLyDichVuPanel() {
        initComponents();
        customInit();
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     * Khởi tạo linh kiện giao diện được sinh tự động bởi NetBeans.
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
     * Cấu hình thiết lập giao diện tùy chỉnh và khởi tạo bảng dữ liệu dịch vụ.
     */
    private void customInit() {
        pnlHeaderWrap.add(PageUI.createPageHeader("Quản lý dịch vụ & Tiện ích sân bóng",
                "Các gói dịch vụ: Bữa sáng VĐV, Giặt là trang phục, Massage hồi phục, Trọng tài, HLV cá nhân..."), BorderLayout.CENTER);

        // Khởi tạo model bảng dữ liệu dịch vụ
        model = new DefaultTableModel(
                new String[]{"Mã DV", "Tên dịch vụ", "Loại dịch vụ", "Giá (VNĐ)", "Mô tả"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        PageUI.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(65);

        // Sự kiện nhấp đúp xem chi tiết dịch vụ
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && table.rowAtPoint(evt.getPoint()) >= 0) {
                    onViewInfo();
                }
            }
        });

        lblCount = new JLabel();
        lblCount.setFont(UIConstants.FONT_SMALL);
        lblCount.setForeground(UIConstants.TEXT_SECONDARY);

        buildToolbar();
        buildTableCard();

        reload();
    }

    /**
     * Xây dựng thanh công cụ nút bấm chức năng.
     */
    private void buildToolbar() {
        JButton btnAdd = new javax.swing.JButton(" Thêm DV");
        btnAdd.setIcon(Utils.IconUtils.getAddIcon(16));
        PageUI.stylePrimaryButton(btnAdd);
        btnAdd.addActionListener(e -> onAdd());

        JButton btnDetail = new javax.swing.JButton(" Xem chi tiết");
        btnDetail.setIcon(Utils.IconUtils.getOpenIcon(16));
        PageUI.styleSecondaryButton(btnDetail);
        btnDetail.addActionListener(e -> onViewInfo());

        JButton btnEdit = new javax.swing.JButton(" Sửa");
        btnEdit.setIcon(Utils.IconUtils.getEditIcon(16));
        PageUI.styleSecondaryButton(btnEdit);
        btnEdit.addActionListener(e -> onEdit());

        JButton btnDel = new javax.swing.JButton(" Xóa");
        btnDel.setIcon(Utils.IconUtils.getDeleteIcon(16));
        PageUI.styleDangerButton(btnDel);
        btnDel.addActionListener(e -> onDelete());

        pnlToolbar.add(btnAdd);
        pnlToolbar.add(btnDetail);
        pnlToolbar.add(btnEdit);
        pnlToolbar.add(btnDel);
    }

    /**
     * Xây dựng card chứa bảng danh sách các gói dịch vụ.
     */
    private void buildTableCard() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel t = new JLabel("Danh sách gói dịch vụ sân bóng (Dịch vụ riêng)");
        t.setFont(UIConstants.FONT_SUBTITLE);
        t.setForeground(UIConstants.PRIMARY);
        top.add(t, BorderLayout.WEST);
        top.add(lblCount, BorderLayout.EAST);
        pnlTableCard.add(top, BorderLayout.NORTH);
        pnlTableCard.add(new javax.swing.JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Nạp lại dữ liệu danh sách dịch vụ từ DataStore hiển thị lên bảng.
     */
    public void reload() {
        model.setRowCount(0);
        int n = 0;

        List<DichVu> list = DataStore.get().getDichVus();
        for (DichVu d : list) {
            model.addRow(new Object[]{
                    d.getMaDichVu(), d.getTenDichVu(), d.getLoaiDichVu(),
                    String.format("%,.0f VNĐ", (double) (d.getDonGia())), d.getMoTa()
            });
            n++;
        }

        lblCount.setText(n + " gói dịch vụ");
    }

    /**
     * Lấy đối tượng DichVu tương ứng với dòng được chọn trong bảng.
     * 
     * @return DichVu hoặc null nếu chưa chọn dòng nào
     */
    private DichVu selected() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        Object val = model.getValueAt(row, 0);
        String ma = val != null ? val.toString() : "";
        return DataStore.get().getDichVus().stream()
                .filter(d -> d.getMaDichVu().equalsIgnoreCase(ma) || String.valueOf(d.getId()).equals(ma))
                .findFirst().orElse(null);
    }

    /**
     * Xử lý mở hộp thoại xem thông tin chi tiết gói dịch vụ đang được chọn.
     */
    private void onViewInfo() {
        DichVu sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dịch vụ trong bảng để xem chi tiết.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        ChiTietDichVuDialog dialog = new ChiTietDichVuDialog(parent, sel);
        dialog.setVisible(true);
        if (dialog.isEditRequested()) {
            onEdit();
        }
    }

    /**
     * Xử lý mở hộp thoại thêm mới một gói dịch vụ vào hệ thống.
     */
    private void onAdd() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        DichVuFormDialog dialog = new DichVuFormDialog(parent, null);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        DichVu form = dialog.getResult();
        List<String> existingCodes = DataStore.get().getDichVus().stream().map(DichVu::getMaDichVu).toList();
        String ma = Utils.CodeGen.next("DV", existingCodes, 3);
        form.setMaDichVu(ma);
        DataStore.get().getDichVus().add(form);
        if (DataStore.isUseDatabase()) {
            try { new DAO.DichVuDAO().insert(form); } catch (Exception ignored) {}
        }
        reload();
        JOptionPane.showMessageDialog(this,
                "THÊM DỊCH VỤ THÀNH CÔNG\n"
                        + "• Mã dịch vụ: " + form.getMaDichVu() + "\n"
                        + "• Tên dịch vụ: " + form.getTenDichVu() + "\n"
                        + "• Loại dịch vụ: " + form.getLoaiDichVu() + "\n"
                        + "• Giá: " + String.format("%,.0f VNĐ", (double) (form.getDonGia())),
                "Kết quả dịch vụ", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Xử lý mở hộp thoại chỉnh sửa thông tin gói dịch vụ đang được chọn.
     */
    private void onEdit() {
        DichVu sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ trong bảng để cập nhật.");
            return;
        }
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        DichVuFormDialog dialog = new DichVuFormDialog(parent, sel);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        DichVu form = dialog.getResult();
        sel.setTenDichVu(form.getTenDichVu());
        sel.setLoaiDichVu(form.getLoaiDichVu());
        sel.setDonGia(form.getDonGia());
        sel.setMoTa(form.getMoTa());
        if (DataStore.isUseDatabase()) {
            try { new DAO.DichVuDAO().update(sel); } catch (Exception ignored) {}
        }
        reload();
        JOptionPane.showMessageDialog(this,
                "CẬP NHẬT DỊCH VỤ THÀNH CÔNG\n• Mã dịch vụ: " + sel.getMaDichVu() + "\n• Tên dịch vụ: " + sel.getTenDichVu(),
                "Kết quả dịch vụ", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Xử lý xóa gói dịch vụ đang được chọn khỏi hệ thống.
     * Thực hiện kiểm tra ràng buộc: không cho xóa nếu có lịch đặt sân đang hoạt động chứa dịch vụ này.
     */
    private void onDelete() {
        DichVu sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // KIỂM TRA LỊCH ĐẶT SÂN ĐANG DIỄN RA / CHỜ PHỤC VỤ DÙNG DỊCH VỤ NÀY
        List<Model.DatLich> activeBookings = DataStore.get().getDatLichs().stream()
                .filter(d -> !"DaHuy".equalsIgnoreCase(d.getTrangThai())
                        && !"DA_HUY".equalsIgnoreCase(d.getTrangThai())
                        && !"HoanThanh".equalsIgnoreCase(d.getTrangThai())
                        && !"HOAN_THANH".equalsIgnoreCase(d.getTrangThai()))
                .filter(d -> isServiceInBooking(sel, d))
                .toList();

        if (!activeBookings.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[!] KHÔNG THỂ XÓA DỊCH VỤ!\n\n");
            sb.append("Dịch vụ '").append(sel.getTenDichVu()).append("' (Mã: ").append(sel.getMaDichVu())
                    .append(") hiện đang được sử dụng trong ").append(activeBookings.size()).append(" lịch đặt đang diễn ra / chờ phục vụ:\n\n");

            for (Model.DatLich d : activeBookings) {
                sb.append("• Mã lịch đặt : ").append(d.getMaLichDat()).append("\n")
                  .append("  - Sân bóng     : ").append(d.getTenSan() != null ? d.getTenSan() : "-").append("\n")
                  .append("  - Khách hàng   : ").append(d.getTenKhach() != null ? d.getTenKhach() : "-")
                  .append(" (SĐT: ").append(d.getSoDienThoaiKhach() != null ? d.getSoDienThoaiKhach() : "").append(")\n")
                  .append("  - Ngày đặt     : ").append(d.getNgayDat()).append(" (Khung giờ: ").append(d.getKhungGio()).append(")\n")
                  .append("  - Dịch vụ kèm  : ").append(d.getDichVuKem() != null ? d.getDichVuKem().replace("\n", ", ") : "-").append("\n")
                  .append("  - Trạng thái   : ").append(d.getTrangThaiHienThi()).append("\n\n");
            }
            sb.append("Vui lòng hoàn tất hoặc hủy các lịch đặt có sử dụng dịch vụ này trước khi xóa!");

            JOptionPane.showMessageDialog(this, sb.toString(), "Cảnh báo lịch đặt đang diễn ra", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Xóa dịch vụ \"" + sel.getTenDichVu() + "\"?",
                "Xác nhận xóa dịch vụ", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

        DataStore.get().getDichVus().remove(sel);
        if (DataStore.isUseDatabase()) {
            try { new DAO.DichVuDAO().delete(sel.getMaDichVu()); } catch (Exception ignored) {}
        }
        reload();

        JOptionPane.showMessageDialog(this, "Đã xóa khỏi danh sách dịch vụ thành công.",
                "Kết quả cập nhật dịch vụ", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Kiểm tra xem thông tin dịch vụ chỉ định có xuất hiện trong phiếu đặt sân hay không.
     */
    private boolean isServiceInBooking(DichVu dv, Model.DatLich booking) {
        if (dv == null || booking == null) return false;
        String dvKem = booking.getDichVuKem();
        if (dvKem == null || dvKem.isBlank()) return false;
        String name = dv.getTenDichVu() != null ? dv.getTenDichVu().toLowerCase() : "";
        String code = dv.getMaDichVu() != null ? dv.getMaDichVu().toLowerCase() : "";
        String hangHoa = dv.getTenHangHoa() != null ? dv.getTenHangHoa().toLowerCase() : "";
        String lowerKem = dvKem.toLowerCase();

        return (!name.isEmpty() && lowerKem.contains(name))
                || (!code.isEmpty() && lowerKem.contains(code))
                || (!hangHoa.isEmpty() && lowerKem.contains(hangHoa));
    }
}
