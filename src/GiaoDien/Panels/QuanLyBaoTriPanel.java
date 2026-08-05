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
 * UC Quản lý bảo trì:
 * Đăng nhập · Xem tình trạng CSVC dạng Bảng (JTable) · Cập nhật trạng thái BT
 * · Xem lịch sử bảo trì · Lập phiếu bảo trì
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class QuanLyBaoTriPanel extends javax.swing.JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JLabel lblCount;

    // Table Tình trạng cơ sở vật chất (JTable)
    private DefaultTableModel modelCsvc;
    private JTable tableCsvc;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlHeaderWrap;
    private javax.swing.JPanel pnlMid;
    private javax.swing.JPanel pnlToolbar;
    // End of variables declaration//GEN-END:variables

    public QuanLyBaoTriPanel() {
        initComponents();
        customInit();
    }

    /**
     * NetBeans GUI Builder generated code initialization.
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

    private void customInit() {
        pnlHeaderWrap.add(PageUI.createPageHeader("Quản lý bảo trì sân bóng",
                "Xem Bảng tình trạng CSVC sân bóng · Lập phiếu bảo trì · Cập nhật trạng thái · Xem lịch sử"), BorderLayout.CENTER);

        // Model Lịch sử bảo trì
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

        // Model Bảng Tình trạng cơ sở vật chất (JTable)
        modelCsvc = new DefaultTableModel(new String[]{
                "Mã sân", "Tên sân", "Loại sân", "Giá/giờ", "Trạng thái"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableCsvc = new JTable(modelCsvc);
        PageUI.styleTable(tableCsvc);
        tableCsvc.getColumnModel().getColumn(0).setPreferredWidth(80);

        buildToolbar();
        pnlMid.add(createCsvcCard());
        pnlMid.add(createHistoryCard());

        refreshCsvc();
        reload();
    }

    private void buildToolbar() {
        pnlToolbar.removeAll();
        pnlToolbar.setLayout(new BorderLayout(0, 0));
        pnlToolbar.setOpaque(false);

        // Bên TRÁI: Thao tác lập/sửa/xem phiếu bảo trì
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

    private void onCreate() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        BaoTriFormDialog dialog = new BaoTriFormDialog(parent, null);
        dialog.setVisible(true);
        if (dialog.isConfirmed() && dialog.getResult() != null) {
            BaoTri form = dialog.getResult();
            KhuVucSan san = dialog.getSelectedSan();

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

            if (san != null) {
                san.setTrangThai("BaoTri");
            }
            reload();
            JOptionPane.showMessageDialog(this, "Đã thêm phiếu bảo trì " + ma + " thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

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

    private void onHistory() {
        reload();
        JOptionPane.showMessageDialog(this, "Đã làm mới danh sách lịch sử bảo trì.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

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
