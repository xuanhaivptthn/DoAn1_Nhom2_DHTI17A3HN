package GiaoDien.Panels;

import GiaoDien.Dialogs.*;
import Utils.PageUI;

import Model.BaoTri;
import Model.KhuVucSan;
import Utils.DataStore;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

/**
 * Panel Quản lý bảo trì sân bóng (QuanLyBaoTriPanel).
 * <p>
 * Thực thi Use Case Quản lý bảo trì:
 * Xem Bảng tình trạng cơ sở vật chất (CSVC) sân bóng, cập nhật trạng thái bảo trì,
 * xem lịch sử phiếu bảo trì và lập phiếu bảo trì mới cho các sân bóng.
 * </p>
 * 
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class QuanLyBaoTriPanel extends javax.swing.JPanel {

    /**
     * Model dữ liệu bảng danh sách phiếu bảo trì sân bóng.
     */
    private DefaultTableModel model;

    /**
     * Bảng hiển thị danh sách lịch sử & phiếu bảo trì.
     */
    private JTable table;

    /**
     * Nhãn hiển thị tổng số lượng phiếu bảo trì trong danh sách.
     */
    private JLabel lblCount;

    /**
     * Model dữ liệu bảng Tình trạng Cơ sở vật chất sân bóng.
     */
    private DefaultTableModel modelCsvc;

    /**
     * Bảng hiển thị tình trạng cơ sở vật chất các khu vực sân bóng.
     */
    private JTable tableCsvc;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Panel thân nội dung chính chứa hai card bảng dữ liệu */
    private javax.swing.JPanel pnlBody;
    /** Panel bao bọc header tiêu đề trang */
    private javax.swing.JPanel pnlHeaderWrap;
    /** Panel ở giữa xếp chồng 2 bảng dữ liệu */
    private javax.swing.JPanel pnlMid;
    /** Panel thanh công cụ chứa các nút chức năng thao tác */
    private javax.swing.JPanel pnlToolbar;
    // End of variables declaration//GEN-END:variables

    /**
     * Khởi tạo giao diện Quản lý bảo trì mới.
     */
    public QuanLyBaoTriPanel() {
        initComponents();
        customInit();
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     * Khởi tạo các linh kiện giao diện sinh tự động từ GUI Builder.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeaderWrap = new javax.swing.JPanel();
        pnlBody = new javax.swing.JPanel();
        pnlToolbar = new javax.swing.JPanel();
        pnlMid = new javax.swing.JPanel();

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

        pnlMid.setOpaque(false);
        pnlMid.setLayout(new java.awt.GridLayout(2, 1, 0, 12));
        pnlBody.add(pnlMid, java.awt.BorderLayout.CENTER);

        add(pnlBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cấu hình khởi tạo giao diện và các bảng dữ liệu tùy chỉnh.
     */
    private void customInit() {
        // Tạo tiêu đề trang quản lý bảo trì
        pnlHeaderWrap.add(PageUI.createPageHeader("Quản lý bảo trì sân bóng",
                "Xem Bảng tình trạng CSVC sân bóng · Lập phiếu bảo trì · Cập nhật trạng thái · Xem lịch sử"), BorderLayout.CENTER);

        // Khởi tạo Model & JTable Lịch sử bảo trì
        model = new DefaultTableModel(new String[]{
                "Mã phiếu", "Sân", "Nội dung", "Bắt đầu", "Kết thúc", "Trạng thái"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        PageUI.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);

        lblCount = new JLabel();
        lblCount.setFont(UIConstants.FONT_SMALL);
        lblCount.setForeground(UIConstants.TEXT_SECONDARY);

        // Khởi tạo Model & JTable Tình trạng cơ sở vật chất sân bóng
        modelCsvc = new DefaultTableModel(new String[]{
                "Mã sân", "Tên sân", "Loại sân", "Giá/giờ", "Trạng thái"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableCsvc = new JTable(modelCsvc);
        PageUI.styleTable(tableCsvc);
        tableCsvc.getColumnModel().getColumn(0).setPreferredWidth(80);

        // Dựng thanh công cụ và các card chứa bảng
        buildToolbar();
        pnlMid.add(createCsvcCard());
        pnlMid.add(createHistoryCard());

        // Cập nhật dữ liệu khởi tạo
        refreshCsvc();
        reload();
    }

    /**
     * Xây dựng thanh công cụ chứa các nút thao tác phiếu bảo trì.
     */
    private void buildToolbar() {
        pnlToolbar.removeAll();
        pnlToolbar.setLayout(new BorderLayout(0, 0));
        pnlToolbar.setOpaque(false);

        // Nút thao tác phía bên trái thanh công cụ
        JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlLeft.setOpaque(false);

        JButton btnNew = new javax.swing.JButton(" Lập phiếu BT");
        btnNew.setIcon(Utils.IconUtils.getAddIcon(16));
        PageUI.stylePrimaryButton(btnNew);
        btnNew.addActionListener(e -> onCreate());

        JButton btnUpdate = new javax.swing.JButton(" Cập nhật TT");
        btnUpdate.setIcon(Utils.IconUtils.getEditIcon(16));
        PageUI.styleSecondaryButton(btnUpdate);
        btnUpdate.addActionListener(e -> onUpdate());

        JButton btnView = new javax.swing.JButton(" Xem phiếu BT");
        btnView.setIcon(Utils.IconUtils.getOpenIcon(16));
        PageUI.styleSecondaryButton(btnView);
        btnView.addActionListener(e -> viewTicket());

        pnlLeft.add(btnNew);
        pnlLeft.add(btnUpdate);
        pnlLeft.add(btnView);

        pnlToolbar.add(pnlLeft, BorderLayout.WEST);
    }

    /**
     * Xây dựng card chứa bảng tình trạng cơ sở vật chất các sân bóng.
     * 
     * @return JPanel chứa tiêu đề và bảng thông tin CSVC
     */
    private JPanel createCsvcCard() {
        JPanel card = new javax.swing.JPanel();
        card.setLayout(new BorderLayout(0, 8));
        JLabel t = new JLabel("Bảng Tình trạng Cơ sở vật chất sân bóng");
        t.setFont(UIConstants.FONT_SUBTITLE);
        t.setForeground(UIConstants.PRIMARY);
        card.add(t, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(tableCsvc);
        sp.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));
        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    /**
     * Xây dựng card chứa bảng danh sách lịch sử phiếu bảo trì.
     * 
     * @return JPanel chứa tiêu đề, nhãn số lượng và bảng phiếu bảo trì
     */
    private JPanel createHistoryCard() {
        JPanel card = new javax.swing.JPanel();
        card.setLayout(new BorderLayout(0, 8));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel t = new JLabel("Lịch sử & Phiếu bảo trì");
        t.setFont(UIConstants.FONT_SUBTITLE);
        t.setForeground(UIConstants.PRIMARY);
        top.add(t, BorderLayout.WEST);
        top.add(lblCount, BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);
        card.add(new javax.swing.JScrollPane(table), BorderLayout.CENTER);
        return card;
    }

    /**
     * Làm mới dữ liệu hiển thị trên bảng Tình trạng Cơ sở vật chất các sân bóng.
     */
    private void refreshCsvc() {
        if (modelCsvc == null) return;
        modelCsvc.setRowCount(0);

        for (KhuVucSan k : DataStore.get().getKhuVucs()) {
            String ttStr = DataStore.get().getTrangThaiSanHienTai(k);
            modelCsvc.addRow(new Object[]{
                    k.getMaSan(),
                    k.getTenSan(),
                    k.getLoaiSanHienThi(),
                    String.format("%,.0f VNĐ", (double) (k.getGiaThueTheoGio())),
                    ttStr
            });
        }
    }

    /**
     * Nạp lại toàn bộ dữ liệu tình trạng CSVC sân bóng và danh sách phiếu bảo trì từ DataStore.
     */
    public void reload() {
        refreshCsvc();
        model.setRowCount(0);

        List<BaoTri> list = DataStore.get().getBaoTris();
        int c = 0;
        for (BaoTri b : list) {
            model.addRow(new Object[]{
                    b.getMaPhieuBaoTri(), b.getTenSan(), b.getNoiDung(),
                    b.getNgayBatDau(), b.getNgayKetThuc(), b.getTrangThaiHienThi()
            });
            c++;
        }
        lblCount.setText("Tổng: " + c + " phiếu");
    }

    /**
     * Xử lý mở hộp thoại tạo mới một phiếu bảo trì sân bóng.
     */
    private void onCreate() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        BaoTriFormDialog dialog = new BaoTriFormDialog(parent, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed() && dialog.getResult() != null) {
            BaoTri form = dialog.getResult();
            KhuVucSan san = dialog.getSelectedSan();

            // Sinh mã phiếu bảo trì tiếp theo
            String ma = Utils.CodeGen.next("BT", DataStore.get().getBaoTris().stream().map(BaoTri::getMaPhieuBaoTri).toList(), 3);

            BaoTri b = new BaoTri();
            b.setMaPhieuBaoTri(ma);
            b.setMaSan(san != null ? san.getMaSan() : null);
            b.setTenSan(san != null ? san.getTenSan() : "Sân 1");
            b.setNoiDung(form.getNoiDung());
            b.setNgayBatDau(form.getNgayBatDau());
            b.setNgayKetThuc(form.getNgayKetThuc());
            b.setTrangThaiPhieu(form.getTrangThaiPhieu());

            DataStore.get().getBaoTris().add(b);
            if (DataStore.isUseDatabase()) {
                try { new DAO.BaoTriDAO().insert(b); } catch (Exception ignored) {}
            }

            // Đổi trạng thái sân sang Bảo trì nếu có chọn sân cụ thể
            if (san != null) {
                san.setTrangThai("BaoTri");
            }
            reload();
            JOptionPane.showMessageDialog(this, "Đã thêm phiếu bảo trì " + ma + " thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Xử lý mở hộp thoại cập nhật thông tin phiếu bảo trì đang chọn trong bảng.
     */
    private void onUpdate() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu bảo trì để cập nhật.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String ma = (String) model.getValueAt(r, 0);
        BaoTri target = DataStore.get().getBaoTris().stream().filter(b -> ma.equals(b.getMaPhieuBaoTri())).findFirst().orElse(null);
        if (target == null) return;

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        BaoTriFormDialog dialog = new BaoTriFormDialog(parent, target);
        dialog.setVisible(true);
        if (dialog.isConfirmed() && dialog.getResult() != null) {
            BaoTri form = dialog.getResult();
            target.setNoiDung(form.getNoiDung());
            target.setNgayBatDau(form.getNgayBatDau());
            target.setNgayKetThuc(form.getNgayKetThuc());
            target.setTrangThaiPhieu(form.getTrangThaiPhieu());

            KhuVucSan san = DataStore.get().getKhuVucs().stream()
                    .filter(k -> k.getMaSan() != null && k.getMaSan().equalsIgnoreCase(target.getMaSan()))
                    .findFirst().orElse(null);

            // Cập nhật tự động trạng thái sân hoạt động lại nếu hoàn thành hoặc hủy phiếu bảo trì
            if (san != null) {
                if ("HOAN_THANH".equalsIgnoreCase(form.getTrangThaiPhieu()) || "HUY".equalsIgnoreCase(form.getTrangThaiPhieu())) {
                    san.setTrangThai("HOAT_DONG");
                } else if ("DANG_BAO_TRI".equalsIgnoreCase(form.getTrangThaiPhieu()) || "DangXuLy".equalsIgnoreCase(form.getTrangThaiPhieu())) {
                    san.setTrangThai("BAO_TRI");
                }
            }

            reload();
            JOptionPane.showMessageDialog(this, "Đã cập nhật thông tin phiếu " + ma, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Làm mới danh sách phiếu bảo trì.
     */
    private void onHistory() {
        reload();
        JOptionPane.showMessageDialog(this, "Đã làm mới danh sách lịch sử bảo trì.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Thay đổi trực tiếp trạng thái cho phiếu bảo trì đang được chọn trên bảng.
     * 
     * @param status Mã trạng thái mới cần đặt (HOAN_THANH, DANG_BAO_TRI, HUY)
     */
    private void setStatus(String status) {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu bảo trì cần thay đổi trạng thái.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String ma = (String) model.getValueAt(r, 0);
        BaoTri target = DataStore.get().getBaoTris().stream().filter(b -> ma.equals(b.getMaPhieuBaoTri())).findFirst().orElse(null);
        if (target != null) {
            target.setTrangThaiPhieu(status);

            if ("HOAN_THANH".equals(status) || "HUY".equals(status)) {
                KhuVucSan san = DataStore.get().getKhuVucs().stream()
                        .filter(k -> k.getMaSan() != null && k.getMaSan().equals(target.getMaSan()))
                        .findFirst().orElse(null);
                if (san != null && ("BaoTri".equals(san.getTrangThai()) || "BAO_TRI".equals(san.getTrangThai()))) {
                    san.setTrangThai("HOAT_DONG");
                }
            }
            reload();
            JOptionPane.showMessageDialog(this, "Đã chuyển trạng thái phiếu " + ma + " sang: " + target.getTrangThaiHienThi(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Hiển thị hộp thoại thông tin chi tiết của phiếu bảo trì đang được chọn.
     */
    private void viewTicket() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu bảo trì để xem chi tiết.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String ma = (String) model.getValueAt(r, 0);
        BaoTri target = DataStore.get().getBaoTris().stream().filter(b -> ma.equals(b.getMaPhieuBaoTri())).findFirst().orElse(null);
        if (target == null) return;

        String details = "===== CHI TIẾT PHIẾU BẢO TRÌ =====\n\n"
                + "• Mã phiếu     : " + target.getMaPhieuBaoTri() + "\n"
                + "• Sân bảo trì  : " + target.getTenSan() + "\n"
                + "• Nội dung     : " + target.getNoiDung() + "\n"
                + "• Ngày bắt đầu : " + target.getNgayBatDau() + "\n"
                + "• Ngày kết thúc: " + target.getNgayKetThuc() + "\n"
                + "• Trạng thái   : " + target.getTrangThaiHienThi() + "\n";

        JOptionPane.showMessageDialog(this, details, "Chi tiết phiếu bảo trì", JOptionPane.INFORMATION_MESSAGE);
    }
}
