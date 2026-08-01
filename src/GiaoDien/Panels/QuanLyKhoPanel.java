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
 * UC Quản lý kho:
 * Xem · Thêm · Cập nhật · Nhập kho · Xuất kho · Kiểm tra tồn kho hàng hóa (Nước suối, lưới, găng tay,...).
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class QuanLyKhoPanel extends javax.swing.JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JLabel lblCount;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlHeaderWrap;
    private javax.swing.JPanel pnlTableCard;
    private javax.swing.JPanel pnlToolbar;
    // End of variables declaration//GEN-END:variables

    public QuanLyKhoPanel() {
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
        pnlHeaderWrap.add(PageUI.createPageHeader("Quản lý kho hàng & Vật tư",
                "Quản lý vật phẩm: Nước suối, lưới bóng, găng tay, bóng thi đấu, trang thiết bị..."), BorderLayout.CENTER);

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
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);

        lblCount = new JLabel();
        lblCount.setFont(UIConstants.FONT_SMALL);
        lblCount.setForeground(UIConstants.TEXT_SECONDARY);

        buildToolbar();
        buildTableCard();

        reload();
    }

    private void buildToolbar() {
        pnlToolbar.setLayout(new BorderLayout());
        pnlToolbar.setOpaque(false);

        // Bên TRÁI: Thêm mới mặt hàng / Thêm mặt hàng đã có / Sửa / Xóa
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

        JButton btnEdit = new javax.swing.JButton(" Sửa");
        btnEdit.setIcon(Utils.IconUtils.getEditIcon(16));
        PageUI.styleSecondaryButton(btnEdit);
        btnEdit.addActionListener(e -> onEdit());

        JButton btnDel = new javax.swing.JButton(" Xóa");
        btnDel.setIcon(Utils.IconUtils.getDeleteIcon(16));
        PageUI.styleDangerButton(btnDel);
        btnDel.addActionListener(e -> onDelete());

        pnlLeft.add(btnNhap);
        pnlLeft.add(btnKiemTra);
        pnlLeft.add(btnEdit);
        pnlLeft.add(btnDel);

        // Bên PHẢI: Xuất kho | Làm mới dữ liệu
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlRight.setOpaque(false);

        JButton btnXuat = new javax.swing.JButton(" Xuất kho");
        btnXuat.setIcon(Utils.IconUtils.getExportIcon(16));
        PageUI.styleSecondaryButton(btnXuat);
        btnXuat.addActionListener(e -> onXuatKho());

        JButton btnRefresh = new javax.swing.JButton(" Làm mới dữ liệu");
        btnRefresh.setIcon(Utils.IconUtils.getRefreshIcon(16));
        PageUI.styleSecondaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> {
            reload();
            JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu kho hàng từ CSDL!", "Làm mới dữ liệu", JOptionPane.INFORMATION_MESSAGE);
        });

        pnlRight.add(btnXuat);
        pnlRight.add(btnRefresh);

        pnlToolbar.add(pnlLeft, BorderLayout.WEST);
        pnlToolbar.add(pnlRight, BorderLayout.EAST);
    }

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

    private void onAddMoi() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        KhoFormDialog dialog = new KhoFormDialog(parent, null);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        DichVu form = dialog.getResult();
        int nextId = DataStore.get().getKhoItems().stream().mapToInt(DichVu::getId).max().orElse(100) + 1;
        form.setMaDichVu(String.format("HH%03d", nextId));
        DataStore.get().getKhoItems().add(form);
        reload();

        JOptionPane.showMessageDialog(this,
                "THÊM MẶT HÀNG MỚI THÀNH CÔNG\n"
                        + "• " + form.getTenDichVu() + "\n"
                        + "• Giá: " + String.format("%,.0f VNĐ", (double) form.getDonGia()) + "\n"
                        + "• Số lượng: " + form.getSoLuongTon(),
                "Kết quả cập nhật kho", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onAddDaCo() {
        List<DichVu> list = DataStore.get().getKhoItems();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kho hiện tại chưa có mặt hàng nào. Vui lòng sử dụng 'Thêm mới mặt hàng'.");
            return;
        }

        DichVu sel = selected();
        if (sel == null) {
            sel = (DichVu) JOptionPane.showInputDialog(this,
                    "Chọn mặt hàng đã có để thêm số lượng:",
                    "Thêm mặt hàng đã có", JOptionPane.QUESTION_MESSAGE,
                    null, list.toArray(), list.get(0));
        }
        if (sel == null) return;

        String input = JOptionPane.showInputDialog(this,
                "Mặt hàng: " + sel.getTenDichVu()
                        + "\nSố lượng hiện tại: " + sel.getSoLuongTon() + " " + sel.getDonVi()
                        + "\nNhập số lượng muốn thêm vào kho:",
                "Thêm số lượng — " + sel.getTenDichVu(), JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.isBlank()) return;

        int sl;
        try {
            sl = Integer.parseInt(input.trim());
            if (sl <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ.");
            return;
        }

        sel.nhapKho(sl);
        reload();
        JOptionPane.showMessageDialog(this,
                "CẬP NHẬT THÀNH CÔNG\n• " + sel.getTenDichVu()
                        + "\n• Đã cộng thêm: +" + sl + " " + sel.getDonVi()
                        + "\n• Tổng số lượng hiện tại: " + sel.getSoLuongTon() + " " + sel.getDonVi(),
                "Kết quả cập nhật kho", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onEdit() {
        DichVu sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Chọn mặt hàng để cập nhật.");
            return;
        }
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        KhoFormDialog dialog = new KhoFormDialog(parent, sel);
        dialog.setVisible(true);
        if (!dialog.isConfirmed()) return;

        DichVu form = dialog.getResult();
        sel.setTenDichVu(form.getTenDichVu());
        sel.setMoTa(form.getMoTa());
        sel.setDonGia(form.getDonGia());
        sel.setSoLuongTon(form.getSoLuongTon());
        reload();
        JOptionPane.showMessageDialog(this,
                "CẬP NHẬT KHO — ĐÃ LƯU THÔNG TIN\n• " + sel.getTenDichVu(),
                "Kết quả cập nhật kho", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onDelete() {
        DichVu sel = selected();
        if (sel == null) {
            JOptionPane.showMessageDialog(this, "Chọn mặt hàng để xóa.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Xóa \"" + sel.getTenDichVu() + "\" khỏi kho?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        DataStore.get().getKhoItems().remove(sel);
        reload();
        JOptionPane.showMessageDialog(this, "Đã xóa khỏi kho.",
                "Kết quả cập nhật kho", JOptionPane.INFORMATION_MESSAGE);
    }
}
