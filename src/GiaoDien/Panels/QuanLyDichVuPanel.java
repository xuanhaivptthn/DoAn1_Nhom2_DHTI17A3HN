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
 * UC Quản lý dịch vụ trải nghiệm & Tiện ích:
 * Bữa sáng VĐV, Giặt là trang phục thi đấu, Massage hồi phục, Thuê trọng tài, HLV cá nhân...
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class QuanLyDichVuPanel extends javax.swing.JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JLabel lblCount;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlHeaderWrap;
    private javax.swing.JPanel pnlTableCard;
    private javax.swing.JPanel pnlToolbar;
    // End of variables declaration//GEN-END:variables

    public QuanLyDichVuPanel() {
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

        pnlToolbar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 4));
        pnlBody.add(pnlToolbar, java.awt.BorderLayout.NORTH);

        pnlTableCard.setLayout(new java.awt.BorderLayout(0, 8));
        pnlBody.add(pnlTableCard, java.awt.BorderLayout.CENTER);

        add(pnlBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void customInit() {
        pnlHeaderWrap.add(PageUI.createPageHeader("Quản lý dịch vụ & Tiện ích sân bóng",
                "Các gói dịch vụ: Bữa sáng VĐV, Giặt là trang phục, Massage hồi phục, Trọng tài, HLV cá nhân..."), BorderLayout.CENTER);

        model = new DefaultTableModel(
                new String[]{"Mã DV", "Tên dịch vụ", "Loại dịch vụ", "Giá (VNĐ)", "Mô tả"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        PageUI.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(65);

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

    private void buildToolbar() {
        JButton btnBanVatPham = new javax.swing.JButton(" Bán / Cho thuê vật phẩm");
        btnBanVatPham.setIcon(Utils.IconUtils.getOpenIcon(16));
        PageUI.styleSuccessButton(btnBanVatPham);
        btnBanVatPham.addActionListener(e -> onBanVatPham());

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

        JButton btnRefresh = new javax.swing.JButton(" Làm mới dữ liệu");
        btnRefresh.setIcon(Utils.IconUtils.getRefreshIcon(16));
        PageUI.styleSecondaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> {
            reload();
            JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu dịch vụ từ CSDL!", "Làm mới dữ liệu", JOptionPane.INFORMATION_MESSAGE);
        });

        pnlToolbar.add(btnBanVatPham);
        pnlToolbar.add(btnAdd);
        pnlToolbar.add(btnDetail);
        pnlToolbar.add(btnEdit);
        pnlToolbar.add(btnDel);
        pnlToolbar.add(btnRefresh);
    }

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

    public void reload() {
        model.setRowCount(0);
        int n = 0;

        // Bảng Dịch vụ quản lý các gói dịch vụ riêng
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

    private void onBanVatPham() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        BanChoThueVatPhamDialog dialog = new BanChoThueVatPhamDialog(parent);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        reload();
        JOptionPane.showMessageDialog(this,
                "THÀNH CÔNG: BÁN / CHO THUÊ VẬT PHẨM KHO HÀNG\n"
                        + "• Vật phẩm: " + dialog.getSelectedVatPham().getTenHangHoa() + "\n"
                        + "• Hình thức: " + dialog.getHinhThuc() + "\n"
                        + "• Số lượng: " + dialog.getSoLuong() + "\n"
                        + "• Thành tiền: " + String.format("%,.0f VNĐ", dialog.getTongTien()) + "\n"
                        + "• Tồn kho còn lại: " + dialog.getSelectedVatPham().getSoLuongTon(),
                "Kết quả bán / cho thuê", JOptionPane.INFORMATION_MESSAGE);
    }

    private DichVu selected() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        Object val = model.getValueAt(row, 0);
        String ma = val != null ? val.toString() : "";
        return DataStore.get().getDichVus().stream()
                .filter(d -> d.getMaDichVu().equalsIgnoreCase(ma) || String.valueOf(d.getId()).equals(ma))
                .findFirst().orElse(null);
    }

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


    private void onAdd() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        DichVuFormDialog dialog = new DichVuFormDialog(parent, null);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        DichVu form = dialog.getResult();
        int nextId = DataStore.get().getDichVus().stream().mapToInt(DichVu::getId).max().orElse(0) + 1;
        form.setMaDichVu(String.format("DV%03d", nextId));
        DataStore.get().getDichVus().add(form);
        reload();
        JOptionPane.showMessageDialog(this,
                "THÊM DỊCH VỤ THÀNH CÔNG\n"
                        + "• Mã dịch vụ: " + form.getMaDichVu() + "\n"
                        + "• Tên dịch vụ: " + form.getTenDichVu() + "\n"
                        + "• Loại dịch vụ: " + form.getLoaiDichVu() + "\n"
                        + "• Giá: " + String.format("%,.0f VNĐ", (double) (form.getDonGia())),
                "Kết quả dịch vụ", JOptionPane.INFORMATION_MESSAGE);
    }

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
        reload();
        JOptionPane.showMessageDialog(this,
                "CẬP NHẬT DỊCH VỤ THÀNH CÔNG\n• Mã dịch vụ: " + sel.getMaDichVu() + "\n• Tên dịch vụ: " + sel.getTenDichVu(),
                "Kết quả dịch vụ", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onDelete() {
        DichVu sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Chọn dịch vụ để xóa.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Xóa dịch vụ \"" + sel.getTenDichVu() + "\"?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        DataStore.get().getDichVus().remove(sel);
        reload();
        JOptionPane.showMessageDialog(this, "Đã xóa khỏi danh sách dịch vụ.",
                "Kết quả dịch vụ", JOptionPane.INFORMATION_MESSAGE);
    }
}
