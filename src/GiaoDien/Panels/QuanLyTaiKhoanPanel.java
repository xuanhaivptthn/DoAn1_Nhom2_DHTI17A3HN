package GiaoDien.Panels;

import GiaoDien.Dialogs.*;
import Utils.PageUI;

import Model.TaiKhoan;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel giao diện quản lý tài khoản – dùng trong đồ án quản lý sân bóng.
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class QuanLyTaiKhoanPanel extends javax.swing.JPanel {

    private TaiKhoanTableModel tableModel;
    private JTable table;
    private JTextField txtSearch;
    private JComboBox<String> cboVaiTro;
    private JComboBox<String> cboTrangThai;
    private JLabel lblCount;
    private boolean embedded;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlHeaderWrap;
    private javax.swing.JPanel pnlTableCard;
    private javax.swing.JPanel pnlToolbar;
    // End of variables declaration//GEN-END:variables

    public QuanLyTaiKhoanPanel() {
        this(false);
    }

    public QuanLyTaiKhoanPanel(boolean embedded) {
        this.embedded = embedded;
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
        pnlBody.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 8, 20));
        pnlBody.setLayout(new java.awt.BorderLayout(0, 12));

        pnlToolbar.setLayout(new java.awt.GridBagLayout());
        pnlBody.add(pnlToolbar, java.awt.BorderLayout.NORTH);

        pnlTableCard.setLayout(new java.awt.BorderLayout(0, 8));
        pnlBody.add(pnlTableCard, java.awt.BorderLayout.CENTER);

        add(pnlBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void customInit() {
        tableModel = new TaiKhoanTableModel();
        table = createTable();
        txtSearch = new javax.swing.JTextField(22);
        cboVaiTro = new JComboBox<>(new String[]{"Tất cả", "Quản trị viên", "Nhân viên"});
        cboTrangThai = new JComboBox<>(new String[]{"Tất cả", "Hoạt động", "Đã khoá"});
        lblCount = new JLabel("0 tài khoản");

        buildUI();
        loadFromStore();
        applyFilter();
    }

    private void buildUI() {
        pnlHeaderWrap.removeAll();
        pnlHeaderWrap.add(PageUI.createPageHeader("Quản lý tài khoản hệ thống",
                "Phân quyền người dùng & quản lý danh sách tài khoản truy cập hệ thống sân bóng"), BorderLayout.CENTER);

        buildToolbar();
        buildTableCard();
    }

    private void buildToolbar() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblSearch = new javax.swing.JLabel("Tìm kiếm:");
        lblSearch.setIcon(Utils.IconUtils.getSearchIcon(16));
        pnlToolbar.add(lblSearch, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtSearch.setPreferredSize(new Dimension(220, 34));
        setPlaceholder(txtSearch, "Tên đăng nhập, họ tên, SĐT, email...");
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { applyFilter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { applyFilter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });
        pnlToolbar.add(txtSearch, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        pnlToolbar.add(new javax.swing.JLabel("Vai trò:"), gbc);

        gbc.gridx = 3;
        styleCombo(cboVaiTro);
        cboVaiTro.setPreferredSize(new Dimension(140, 34));
        cboVaiTro.addActionListener(e -> applyFilter());
        pnlToolbar.add(cboVaiTro, gbc);

        gbc.gridx = 4;
        pnlToolbar.add(new javax.swing.JLabel("Trạng thái:"), gbc);

        gbc.gridx = 5;
        styleCombo(cboTrangThai);
        cboTrangThai.setPreferredSize(new Dimension(120, 34));
        cboTrangThai.addActionListener(e -> applyFilter());
        pnlToolbar.add(cboTrangThai, gbc);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);

        JButton btnAdd = new javax.swing.JButton(" Thêm mới");
        btnAdd.setIcon(Utils.IconUtils.getAddIcon(16));
        PageUI.stylePrimaryButton(btnAdd);
        btnAdd.setPreferredSize(new Dimension(130, 36));
        btnAdd.addActionListener(e -> onAdd());

        JButton btnEdit = new javax.swing.JButton(" Sửa");
        btnEdit.setIcon(Utils.IconUtils.getEditIcon(16));
        PageUI.styleSecondaryButton(btnEdit);
        btnEdit.setPreferredSize(new Dimension(100, 36));
        btnEdit.addActionListener(e -> onEdit());

        JButton btnDelete = new javax.swing.JButton(" Xóa");
        btnDelete.setIcon(Utils.IconUtils.getDeleteIcon(16));
        PageUI.styleDangerButton(btnDelete);
        btnDelete.setPreferredSize(new Dimension(100, 36));
        btnDelete.addActionListener(e -> onDelete());

        JButton btnLock = new javax.swing.JButton("Khóa / Mở");
        PageUI.styleSuccessButton(btnLock);
        btnLock.setPreferredSize(new Dimension(130, 36));
        btnLock.addActionListener(e -> onToggleLock());

        JButton btnRefresh = new javax.swing.JButton(" Làm mới");
        btnRefresh.setIcon(Utils.IconUtils.getRefreshIcon(16));
        PageUI.styleSecondaryButton(btnRefresh);
        btnRefresh.setPreferredSize(new Dimension(120, 36));
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            cboVaiTro.setSelectedIndex(0);
            cboTrangThai.setSelectedIndex(0);
            applyFilter();
        });

        actions.add(btnAdd);
        actions.add(btnEdit);
        actions.add(btnDelete);
        actions.add(btnLock);
        actions.add(btnRefresh);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 6, 4, 6);
        pnlToolbar.add(actions, gbc);
    }

    private void buildTableCard() {
        pnlTableCard.setBackground(Color.WHITE);
        pnlTableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER, 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lbl = new JLabel("Danh sách tài khoản");
        lbl.setFont(UIConstants.FONT_SUBTITLE);
        lbl.setForeground(UIConstants.PRIMARY);
        lblCount.setFont(UIConstants.FONT_SMALL);
        lblCount.setForeground(UIConstants.TEXT_SECONDARY);
        top.add(lbl, BorderLayout.WEST);
        top.add(lblCount, BorderLayout.EAST);
        pnlTableCard.add(top, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(0, 360));
        pnlTableCard.add(scroll, BorderLayout.CENTER);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && table.rowAtPoint(e.getPoint()) >= 0) {
                    onEdit();
                }
            }
        });
    }

    private JTable createTable() {
        JTable t = new JTable(tableModel);
        PageUI.styleTable(t);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setFillsViewportHeight(true);
        t.setAutoCreateRowSorter(true);

        t.getColumnModel().getColumn(0).setPreferredWidth(45);  // STT
        t.getColumnModel().getColumn(1).setPreferredWidth(140); // Tên đăng nhập
        t.getColumnModel().getColumn(2).setPreferredWidth(160); // Họ và tên
        t.getColumnModel().getColumn(3).setPreferredWidth(120); // Số điện thoại
        t.getColumnModel().getColumn(4).setPreferredWidth(120); // Vai trò
        t.getColumnModel().getColumn(5).setPreferredWidth(110); // Trạng thái

        return t;
    }

    private void onAdd() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        TaiKhoanFormDialog dialog = new TaiKhoanFormDialog(
                parent,
                null,
                username -> tableModel.existsUsername(username, null)
        );
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            TaiKhoan tk = dialog.getResult();
            tk.setMaTaiKhoan(tableModel.nextMaTaiKhoan());
            tableModel.addTaiKhoan(tk);
            DataStore.get().getTaiKhoans().add(tk);
            if (DataStore.isUseDatabase()) {
                try { new DAO.TaiKhoanDAO().insert(tk); } catch (Exception ignored) {}
            }
            saveProfile(tk, dialog.getHoTen(), dialog.getSoDienThoai(), dialog.getDiaChi());
            applyFilter();
            JOptionPane.showMessageDialog(this,
                    "Đã thêm tài khoản \"" + tk.getTenDangNhap() + "\" thành công!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onEdit() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một tài khoản để sửa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        TaiKhoan selected = tableModel.getAt(modelRow);
        if (selected == null) return;

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        TaiKhoanFormDialog dialog = new TaiKhoanFormDialog(
                parent,
                selected,
                username -> tableModel.existsUsername(username, selected.getMaTaiKhoan())
        );
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            TaiKhoan updated = dialog.getResult();
            tableModel.updateTaiKhoan(updated);
            syncStoreFromModel();
            if (DataStore.isUseDatabase()) {
                try { new DAO.TaiKhoanDAO().update(updated); } catch (Exception ignored) {}
            }
            saveProfile(updated, dialog.getHoTen(), dialog.getSoDienThoai(), dialog.getDiaChi());
            applyFilter();
            JOptionPane.showMessageDialog(this,
                    "Đã cập nhật tài khoản \"" + updated.getTenDangNhap() + "\"!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveProfile(TaiKhoan tk, String hoTen, String sdt, String diaChi) {
        if (tk == null || hoTen == null || hoTen.isBlank()) return;
        if (tk.isChuSan() || tk.isAdmin()) {
            DataStore.get().saveOrUpdateChuSan(tk.getMaTaiKhoan(), hoTen, sdt);
        } else if (tk.isNhanVien()) {
            DataStore.get().saveOrUpdateNhanVien(tk.getMaTaiKhoan(), hoTen, sdt, diaChi);
        }
    }

    private void onDelete() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một tài khoản để xóa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        TaiKhoan selected = tableModel.getAt(modelRow);
        if (selected == null) return;

        if ("admin".equalsIgnoreCase(selected.getTenDangNhap())) {
            JOptionPane.showMessageDialog(this,
                    "Không thể xóa tài khoản quản trị viên mặc định!",
                    "Cảnh báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa tài khoản:\n\n"
                        + "  • Tên đăng nhập: " + selected.getTenDangNhap() + "\n\n"
                        + "Thao tác này không thể hoàn tác.",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeTaiKhoan(selected.getMaTaiKhoan());
            DataStore.get().getTaiKhoans().removeIf(tk -> tk.getMaTaiKhoan().equals(selected.getMaTaiKhoan()));
            if (DataStore.isUseDatabase()) {
                try { new DAO.TaiKhoanDAO().delete(selected.getMaTaiKhoan()); } catch (Exception ignored) {}
            }
            applyFilter();
            JOptionPane.showMessageDialog(this,
                    "Đã xóa tài khoản \"" + selected.getTenDangNhap() + "\".",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onToggleLock() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một tài khoản để khóa/mở khóa.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        TaiKhoan selected = tableModel.getAt(modelRow);
        if (selected == null) return;

        if ("admin".equalsIgnoreCase(selected.getTenDangNhap())) {
            JOptionPane.showMessageDialog(this,
                    "Không thể khóa tài khoản quản trị viên mặc định!",
                    "Cảnh báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isLocked = "KHOA".equalsIgnoreCase(selected.getTrangThai());
        String action = isLocked ? "mở khóa" : "khóa";
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn " + action + " tài khoản \"" + selected.getTenDangNhap() + "\"?",
                "Xác nhận " + action,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            selected.setTrangThai(isLocked ? "HOAT_DONG" : "KHOA");
            tableModel.updateTaiKhoan(selected);
            syncStoreFromModel();
            if (DataStore.isUseDatabase()) {
                try { new DAO.TaiKhoanDAO().update(selected); } catch (Exception ignored) {}
            }
            applyFilter();
            if (viewRow < table.getRowCount()) {
                table.setRowSelectionInterval(viewRow, viewRow);
            }
        }
    }

    private void applyFilter() {
        String keyword = txtSearch.getText();
        if ("Tên đăng nhập, họ tên, SĐT, email...".equals(keyword)) {
            keyword = "";
        }
        String vaiTro = (String) cboVaiTro.getSelectedItem();
        String trangThai = (String) cboTrangThai.getSelectedItem();
        tableModel.filter(keyword, vaiTro, trangThai);
        if (lblCount != null) {
            lblCount.setText(tableModel.getRowCount() + " / " + tableModel.getAllData().size() + " tài khoản");
        }
    }

    private void loadFromStore() {
        tableModel.setData(new ArrayList<>(DataStore.get().getTaiKhoans()));
    }

    private void syncStoreFromModel() {
        List<TaiKhoan> store = DataStore.get().getTaiKhoans();
        store.clear();
        store.addAll(tableModel.getAllData());
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    private void setPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(UIConstants.TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }
}
