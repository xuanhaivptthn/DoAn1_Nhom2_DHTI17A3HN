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
 * <p>
 * Cho phép lọc danh sách thông tin khách hàng dựa theo tên hoặc số điện thoại.
 * Hỗ trợ chọn nhanh bằng cách nhấp đúp chuột vào bảng hoặc nhấn nút 'Chọn khách hàng'.
 * </p>
 *
 * @author Nhóm 2 - DHTI17A3HN
 * @version 1.0
 */
public class ChonKhachHangDialog extends JDialog {

    /** Ô nhập liệu từ khóa tìm kiếm tên hoặc SĐT khách hàng */
    private JTextField txtSearch;

    /** Bảng hiển thị danh sách khách hàng */
    private JTable tableKhach;

    /** Model điều khiển dữ liệu cho bảng khách hàng */
    private DefaultTableModel modelKhach;

    /** Danh sách lưu tạm các khách hàng sau khi lọc theo từ khóa */
    private final List<KhachHang> displayList = new ArrayList<>();

    /** Đối tượng khách hàng được chọn */
    private KhachHang selectedCustomer = null;

    /** Cờ đánh dấu đã xác nhận chọn thành công */
    private boolean confirmed = false;

    /**
     * Khởi tạo thoại chọn khách hàng.
     *
     * @param parent Cửa sổ cha (JFrame)
     */
    public ChonKhachHangDialog(JFrame parent) {
        super(parent, "Danh sách khách hàng quen", true);
        initComponents(parent);
    }

    /**
     * Xây dựng và kết nối các thành phần giao diện người dùng dialog.
     *
     * @param parent Cửa sổ cha
     */
    private void initComponents(JFrame parent) {
        setSize(650, 480);
        setResizable(false);
        if (parent != null) setLocationRelativeTo(parent);

        // ── 1. Header Panel ──────────────────────────────────────────────────
        JPanel pnlHeader = PageUI.createPageHeader("Chọn khách hàng quen", "Tìm kiếm theo Tên hoặc Số điện thoại để chọn nhanh");
        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // ── 2. Main Center Panel ─────────────────────────────────────────────
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Thanh tìm kiếm nhanh
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setIcon(Utils.IconUtils.getSearchIcon(16));
        lblSearch.setFont(UIConstants.FONT_BOLD);
        pnlSearch.add(lblSearch);

        txtSearch = new JTextField(24);
        txtSearch.setPreferredSize(new Dimension(280, 34));
        txtSearch.setFont(UIConstants.FONT_NORMAL);
        txtSearch.setToolTipText("Nhập tên hoặc số điện thoại để tìm kiếm...");
        txtSearch.getDocument().addDocumentListener(new SimpleDocListener(this::filterCustomers));
        pnlSearch.add(txtSearch);

        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        // ── 3. Table Bảng thông tin khách hàng ──────────────────────────────
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

        // Bắt sự kiện nhấp đúp chuột để chọn
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

        // ── 4. Footer Panel ──────────────────────────────────────────────────
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pnlFooter.setBackground(UIConstants.BG);
        pnlFooter.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER));

        JButton btnCancel = new JButton("Hủy");
        Utils.PageUI.styleSecondaryButton(btnCancel);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnSelect = new JButton(" Chọn khách hàng");
        btnSelect.setIcon(Utils.IconUtils.getCheckIcon(16));
        Utils.PageUI.stylePrimaryButton(btnSelect);
        btnSelect.addActionListener(e -> onSelect());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnSelect);

        getContentPane().add(pnlFooter, BorderLayout.SOUTH);

        // Tiến hành nạp dữ liệu ban đầu vào bảng
        filterCustomers();
    }

    /**
     * Lọc danh sách khách hàng từ DataStore theo từ khóa tìm kiếm (họ tên hoặc số điện thoại).
     */
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

        // Tự động chọn dòng đầu tiên nếu có dữ liệu
        if (modelKhach.getRowCount() > 0) {
            tableKhach.setRowSelectionInterval(0, 0);
        }
    }

    /**
     * Lấy khách hàng đang chọn từ dòng của bảng và hoàn tất dialog.
     */
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

    /**
     * Kiểm tra dialog có được xác nhận chọn khách hàng hay không.
     *
     * @return true nếu đã chọn khách hàng thành công
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Lấy đối tượng khách hàng được chọn.
     *
     * @return Đối tượng KhachHang
     */
    public KhachHang getSelectedCustomer() {
        return selectedCustomer;
    }
}
