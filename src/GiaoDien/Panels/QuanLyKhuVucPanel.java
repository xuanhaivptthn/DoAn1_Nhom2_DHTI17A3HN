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
 * Quản lý khu vực sân bóng: cập nhật thông tin các sân.
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class QuanLyKhuVucPanel extends javax.swing.JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JLabel lblCount;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlHeaderWrap;
    private javax.swing.JPanel pnlTableCard;
    private javax.swing.JPanel pnlToolbar;
    // End of variables declaration//GEN-END:variables

    public QuanLyKhuVucPanel() {
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

    private void customInit() {
        pnlHeaderWrap.add(PageUI.createPageHeader("Quản lý khu vực sân bóng",
                "Quản lý khu vực sân bóng - Kết quả cập nhật khu vực"), BorderLayout.CENTER);

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

        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlRight.setOpaque(false);

        JLabel lblQuickNote = new JLabel("Chuyển nhanh trạng thái:");
        lblQuickNote.setFont(UIConstants.FONT_SMALL);
        lblQuickNote.setForeground(UIConstants.TEXT_SECONDARY);

        JButton btnQuickReady = new javax.swing.JButton(" Sẵn sàng");
        btnQuickReady.setIcon(Utils.IconUtils.getCheckIcon(16));
        PageUI.styleSuccessButton(btnQuickReady);
        btnQuickReady.addActionListener(e -> onQuickSetStatus("SanSang"));

        JButton btnQuickMaint = new javax.swing.JButton(" Bảo trì");
        btnQuickMaint.setIcon(Utils.IconUtils.getWarningIcon(16));
        PageUI.styleDangerButton(btnQuickMaint);
        btnQuickMaint.addActionListener(e -> onQuickSetStatus("BaoTri"));

        pnlRight.add(lblQuickNote);
        pnlRight.add(btnQuickReady);
        pnlRight.add(btnQuickMaint);

        pnlToolbar.add(pnlLeft, BorderLayout.WEST);
        pnlToolbar.add(pnlRight, BorderLayout.EAST);
    }

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

    private KhuVucSan selected() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        String maSan = (String) model.getValueAt(row, 0);
        return DataStore.get().getKhuVucs().stream().filter(k -> maSan.equals(k.getMaSan())).findFirst().orElse(null);
    }

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

        boolean isSwitchingToBaoTri = !wasBaoTri && "BaoTri".equalsIgnoreCase(form.getTrangThai());

        if (wasBaoTri && "SanSang".equalsIgnoreCase(form.getTrangThai())) {
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

        if (isSwitchingToBaoTri) {
            promptCreateMaintenanceTicket(sel);
        }
    }

    private void onDelete() {
        KhuVucSan sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Chọn một khu vực để xóa.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Xóa sân " + sel.getMaSan() + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        DataStore.get().getKhuVucs().remove(sel);
        if (DataStore.isUseDatabase()) {
            try { new DAO.KhuVucSanDAO().delete(sel.getMaSan()); } catch (Exception ignored) {}
        }
        reload();

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parent instanceof GiaoDien.MainFrame mf) {
            mf.refreshDataPanels();
        }

        JOptionPane.showMessageDialog(this, "Đã xóa khu vực.", "Kết quả cập nhật khu vực", JOptionPane.INFORMATION_MESSAGE);
    }

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

    private void onQuickSetStatus(String targetStatus) {
        KhuVucSan sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sân bóng trong bảng để chuyển nhanh trạng thái.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean wasBaoTri = DataStore.get().isSanBaoTri(sel);

        if (wasBaoTri && "SanSang".equalsIgnoreCase(targetStatus)) {
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

        String label = "SanSang".equalsIgnoreCase(targetStatus) ? "Sẵn sàng" : "Bảo trì";
        JOptionPane.showMessageDialog(this,
                "Đã chuyển nhanh trạng thái sân " + sel.getMaSan() + " (" + sel.getTenSan() + ") sang: " + label,
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        if (!wasBaoTri && "BaoTri".equalsIgnoreCase(targetStatus)) {
            promptCreateMaintenanceTicket(sel);
        }
    }
}
