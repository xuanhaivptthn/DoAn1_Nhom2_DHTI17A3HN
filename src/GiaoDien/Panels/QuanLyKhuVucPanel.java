package GiaoDien.Panels;

import GiaoDien.Dialogs.*;
import GiaoDien.TaiKhoanTableModel;
import Utils.PageUI;

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
                "Yêu cầu: Quản lý khu vực sân bóng  →  Phản hồi: Kết quả cập nhật khu vực"), BorderLayout.CENTER);

        model = new DefaultTableModel(
                new String[]{"ID", "Mã sân", "Tên sân", "Loại", "Giá/giờ", "Trạng thái"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        PageUI.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(45);
        lblCount = new JLabel();
        lblCount.setFont(UIConstants.FONT_SMALL);
        lblCount.setForeground(UIConstants.TEXT_SECONDARY);

        buildToolbar();
        buildTableCard();
        reload();
    }

    private void buildToolbar() {
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

        JButton btnStatus = new javax.swing.JButton(" Đổi trạng thái");
        btnStatus.setIcon(Utils.IconUtils.getRefreshIcon(16));
        PageUI.styleSuccessButton(btnStatus);
        btnStatus.addActionListener(e -> onChangeStatus());

        pnlToolbar.add(btnAdd);
        pnlToolbar.add(btnEdit);
        pnlToolbar.add(btnDelete);
        pnlToolbar.add(btnStatus);
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
            model.addRow(new Object[]{
                    k.getId(), k.getMaSan(), k.getTenSan(), k.getLoaiSanHienThi(),
                    String.format("%,.0f VNĐ", (double) (k.getGiaThueTheoGio())), k.getTrangThaiHienThi()
            });
        }
        lblCount.setText(list.size() + " khu vực");
    }

    private KhuVucSan selected() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int id = (Integer) model.getValueAt(row, 0);
        return DataStore.get().getKhuVucs().stream().filter(k -> k.getId() == id).findFirst().orElse(null);
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
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        KhuVucFormDialog dialog = new KhuVucFormDialog(parent, sel);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        KhuVucSan form = dialog.getResult();
        sel.setMaSan(form.getMaSan());
        sel.setTenSan(form.getTenSan());
        sel.setLoaiSan(form.getLoaiSan());
        sel.setGiaThueTheoGio(form.getGiaThueTheoGio());
        sel.setTrangThai(form.getTrangThai());
        if (DataStore.isUseDatabase()) {
            try { new DAO.KhuVucSanDAO().update(sel); } catch (Exception ignored) {}
        }
        reload();
        JOptionPane.showMessageDialog(this, "Đã cập nhật khu vực \"" + sel.getMaSan() + "\".",
                "Kết quả cập nhật khu vực", JOptionPane.INFORMATION_MESSAGE);
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
        JOptionPane.showMessageDialog(this, "Đã xóa khu vực.", "Kết quả cập nhật khu vực", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onChangeStatus() {
        KhuVucSan sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Chọn một khu vực.");
            return;
        }
        String[] opts = {"Sẵn sàng", "Đang thuê", "Bảo trì"};
        String pick = (String) JOptionPane.showInputDialog(this, "Trạng thái mới cho " + sel.getMaSan(),
                "Đổi trạng thái", JOptionPane.QUESTION_MESSAGE, null, opts, sel.getTrangThaiHienThi());
        if (pick == null) return;
        sel.setTrangThai(switch (pick) {
            case "Đang thuê" -> "DangThue";
            case "Bảo trì" -> "BaoTri";
            default -> "SanSang";
        });
        if (DataStore.isUseDatabase()) {
            try { new DAO.KhuVucSanDAO().update(sel); } catch (Exception ignored) {}
        }
        reload();
        JOptionPane.showMessageDialog(this,
                "Kết quả: " + sel.getMaSan() + " → " + sel.getTrangThaiHienThi(),
                "Kết quả cập nhật khu vực", JOptionPane.INFORMATION_MESSAGE);
    }
}
