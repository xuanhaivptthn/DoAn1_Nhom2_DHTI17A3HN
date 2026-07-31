package GiaoDien.Dialogs;

import Model.KhachHang;
import Utils.DataStore;
import Utils.PageUI;
import Utils.SimpleDocListener;
import Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog chọn khách hàng quen với khung tìm kiếm thời gian thực.
 * Thiết kế đồng bộ chuẩn UIConstants.
 */
public class ChonKhachHangDialog extends JDialog {

    private JTextField txtSearch;
    private JTable tableKhach;
    private DefaultTableModel modelKhach;
    private final List<KhachHang> displayList = new ArrayList<>();

    private KhachHang selectedCustomer = null;
    private boolean confirmed = false;

    public ChonKhachHangDialog(JFrame parent) {
        super(parent, "Danh sách khách hàng quen", true);
        initComponents(parent);
    }

    private void initComponents(JFrame parent) {
        setSize(650, 480);
        setResizable(false);
        if (parent != null) setLocationRelativeTo(parent);

        // Header Panel
        JPanel pnlHeader = PageUI.createPageHeader("🔍 Chọn khách hàng quen", "Tìm kiếm theo Tên hoặc Số điện thoại để chọn nhanh");
        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // Main Center Panel
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Search Bar Panel
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("🔍 Tìm kiếm:");
        lblSearch.setFont(UIConstants.FONT_BOLD);
        pnlSearch.add(lblSearch);

        txtSearch = new JTextField(24);
        txtSearch.setPreferredSize(new Dimension(280, 34));
        txtSearch.setFont(UIConstants.FONT_NORMAL);
        txtSearch.setToolTipText("Nhập tên hoặc số điện thoại để tìm kiếm...");
        txtSearch.getDocument().addDocumentListener(new SimpleDocListener(this::filterCustomers));
        pnlSearch.add(txtSearch);

        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        // Table
        modelKhach = new DefaultTableModel(
                new String[]{"Mã KH", "Họ và tên", "Số điện thoại"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableKhach = new JTable(modelKhach);
        PageUI.styleTable(tableKhach);
        tableKhach.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableKhach.getColumnModel().getColumn(0).setPreferredWidth(90);
        tableKhach.getColumnModel().getColumn(1).setPreferredWidth(250);
        tableKhach.getColumnModel().getColumn(2).setPreferredWidth(180);

        tableKhach.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tableKhach.getSelectedRow() >= 0) {
                    onSelect();
                }
            }
        });

        pnlCenter.add(new JScrollPane(tableKhach), BorderLayout.CENTER);
        getContentPane().add(pnlCenter, BorderLayout.CENTER);

        // Footer Panel
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pnlFooter.setBackground(UIConstants.BG);
        pnlFooter.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER));

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFont(UIConstants.FONT_BUTTON);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnSelect = new JButton("✓ Chọn khách hàng");
        btnSelect.setFont(UIConstants.FONT_BUTTON);
        btnSelect.setBackground(UIConstants.PRIMARY);
        btnSelect.setForeground(java.awt.Color.WHITE);
        btnSelect.addActionListener(e -> onSelect());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSelect);

        getContentPane().add(pnlFooter, BorderLayout.SOUTH);

        // Load data
        filterCustomers();
    }

    private void filterCustomers() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        displayList.clear();
        modelKhach.setRowCount(0);

        List<KhachHang> all = DataStore.get().getKhachHangs();
        for (KhachHang kh : all) {
            boolean matchName = kh.getTenKhachHang() != null && kh.getTenKhachHang().toLowerCase().contains(keyword);
            boolean matchPhone = kh.getSoDienThoai() != null && kh.getSoDienThoai().toLowerCase().contains(keyword);
            if (keyword.isEmpty() || matchName || matchPhone) {
                displayList.add(kh);
                modelKhach.addRow(new Object[]{
                        kh.getMaKhachHang(),
                        kh.getTenKhachHang(),
                        kh.getSoDienThoai()
                });
            }
        }

        if (modelKhach.getRowCount() > 0) {
            tableKhach.setRowSelectionInterval(0, 0);
        }
    }

    private void onSelect() {
        int selectedRow = tableKhach.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < displayList.size()) {
            selectedCustomer = displayList.get(selectedRow);
            confirmed = true;
            dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một khách hàng từ danh sách!",
                    "Thông báo", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public KhachHang getSelectedCustomer() {
        return selectedCustomer;
    }
}
