package GiaoDien.Dialogs;

import Model.DichVu;
import Utils.DataStore;
import Utils.PageUI;
import Utils.SimpleDocListener;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog tìm kiếm chọn nhanh và nhập thêm số lượng cho vật tư/hàng hóa có sẵn trong Kho.
 * Hỗ trợ lọc tìm kiếm tức thì theo Tên, Mã, Nhà cung cấp và nhập số lượng cộng thêm vào tồn kho.
 */
public class ChonNhapKhoDialog extends JDialog {

    private JTextField txtSearch;
    private JTable tableKho;
    private DefaultTableModel modelKho;
    private JLabel lblSelectedItem;
    private JTextField txtSoLuongNhap;

    private List<DichVu> filteredList = new ArrayList<>();
    private DichVu selectedVatPham;
    private int soLuongNhap = 0;
    private boolean confirmed = false;

    public ChonNhapKhoDialog(JFrame parent, DichVu preSelected) {
        super(parent, "Nhập thêm số lượng vật tư kho hàng", true);
        this.selectedVatPham = preSelected;
        initComponents(parent);
    }

    private void initComponents(JFrame parent) {
        setSize(760, 530);
        setResizable(false);
        if (parent != null) setLocationRelativeTo(parent);

        setLayout(new BorderLayout());

        // Header Panel
        JPanel pnlHeader = PageUI.createPageHeader(
                "Nhập thêm số lượng vật tư kho",
                "Tìm kiếm nhanh mặt hàng kho và nhập số lượng cộng thêm vào tồn kho hiện tại"
        );
        add(pnlHeader, BorderLayout.NORTH);

        // Center Content Panel
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBackground(UIConstants.BG);
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(12, 16, 8, 16));

        // 1. Search Bar Panel
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Tìm mặt hàng:");
        lblSearch.setIcon(Utils.IconUtils.getSearchIcon(16));
        lblSearch.setFont(UIConstants.FONT_BOLD);
        pnlSearch.add(lblSearch);

        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(230, 34));
        txtSearch.setFont(UIConstants.FONT_NORMAL);
        txtSearch.setToolTipText("Nhập mã hàng, tên hàng hoặc nhà cung cấp để tìm kiếm...");
        txtSearch.getDocument().addDocumentListener(new SimpleDocListener(this::reloadTable));
        pnlSearch.add(txtSearch);

        JLabel lblHint = new JLabel("(Gõ tên hoặc mã hàng để tìm kiếm nhanh)");
        lblHint.setFont(UIConstants.FONT_SMALL);
        lblHint.setForeground(UIConstants.TEXT_SECONDARY);
        pnlSearch.add(lblHint);

        pnlCenter.add(pnlSearch, BorderLayout.NORTH);

        // 2. Table List Panel
        String[] headers = {"Mã HH", "Tên mặt hàng kho", "Đơn giá", "Tồn kho", "Đơn vị", "Nhà cung cấp"};
        modelKho = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableKho = new JTable(modelKho);
        tableKho.setRowHeight(32);
        tableKho.setFont(UIConstants.FONT_NORMAL);
        tableKho.getTableHeader().setFont(UIConstants.FONT_BOLD);
        tableKho.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tableKho.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableKho.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tableKho.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        tableKho.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onRowSelected();
            }
        });

        JScrollPane scrollTable = new JScrollPane(tableKho);
        scrollTable.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER));
        pnlCenter.add(scrollTable, BorderLayout.CENTER);

        // 3. Bottom Input Card
        JPanel pnlInputCard = new JPanel(new GridBagLayout());
        pnlInputCard.setBackground(Color.WHITE);
        pnlInputCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.BORDER),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblSelectedItem = new JLabel("Mặt hàng đã chọn: (Vui lòng chọn 1 mặt hàng từ danh sách trên)");
        lblSelectedItem.setFont(UIConstants.FONT_BOLD);
        lblSelectedItem.setForeground(UIConstants.PRIMARY);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        pnlInputCard.add(lblSelectedItem, gbc);

        gbc.gridwidth = 1;

        JLabel lblQty = new JLabel("Số lượng nhập thêm *:");
        lblQty.setFont(UIConstants.FONT_BOLD);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        pnlInputCard.add(lblQty, gbc);

        txtSoLuongNhap = new JTextField(10);
        txtSoLuongNhap.setText("10");
        txtSoLuongNhap.setFont(UIConstants.FONT_BOLD);
        txtSoLuongNhap.setPreferredSize(new Dimension(140, 34));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        pnlInputCard.add(txtSoLuongNhap, gbc);

        pnlCenter.add(pnlInputCard, BorderLayout.SOUTH);

        add(pnlCenter, BorderLayout.CENTER);

        // Footer Action Panel
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pnlFooter.setBackground(UIConstants.BG);

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setFont(UIConstants.FONT_BUTTON);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnConfirm = new JButton("Xác nhận nhập kho");
        btnConfirm.setIcon(Utils.IconUtils.getCheckIcon(16));
        btnConfirm.setFont(UIConstants.FONT_BUTTON);
        btnConfirm.setBackground(UIConstants.PRIMARY);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.addActionListener(e -> onConfirm());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnConfirm);
        add(pnlFooter, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnConfirm);

        reloadTable();
    }

    private void reloadTable() {
        modelKho.setRowCount(0);
        filteredList.clear();

        String keyword = txtSearch != null ? txtSearch.getText().trim().toLowerCase() : "";
        List<DichVu> allKho = DataStore.get().getKhoItems();

        int selectedRowIndex = -1;
        int index = 0;

        for (DichVu item : allKho) {
            String maStr = "HH" + item.getMaHangHoa();
            boolean match = keyword.isEmpty()
                    || maStr.toLowerCase().contains(keyword)
                    || (item.getTenHangHoa() != null && item.getTenHangHoa().toLowerCase().contains(keyword))
                    || (item.getNhaCungCap() != null && item.getNhaCungCap().toLowerCase().contains(keyword));

            if (match) {
                filteredList.add(item);
                modelKho.addRow(new Object[]{
                        "HH" + item.getMaHangHoa(),
                        item.getTenHangHoa(),
                        String.format("%,.0f VNĐ", (double) item.getDonGia()),
                        item.getSoLuongTon(),
                        item.getDonVi(),
                        item.getNhaCungCap()
                });

                if (selectedVatPham != null && item.getId() == selectedVatPham.getId()) {
                    selectedRowIndex = index;
                }
                index++;
            }
        }

        if (selectedRowIndex >= 0 && selectedRowIndex < tableKho.getRowCount()) {
            tableKho.setRowSelectionInterval(selectedRowIndex, selectedRowIndex);
        } else if (tableKho.getRowCount() > 0) {
            tableKho.setRowSelectionInterval(0, 0);
        } else {
            selectedVatPham = null;
            lblSelectedItem.setText("Không tìm thấy mặt hàng phù hợp với từ khóa '" + keyword + "'");
            lblSelectedItem.setForeground(UIConstants.DANGER);
        }
    }

    private void onRowSelected() {
        int r = tableKho.getSelectedRow();
        if (r >= 0 && r < filteredList.size()) {
            selectedVatPham = filteredList.get(r);
            lblSelectedItem.setText("Mặt hàng đã chọn: " + selectedVatPham.getTenHangHoa()
                    + " (Mã: HH" + selectedVatPham.getMaHangHoa() + " · Tồn hiện tại: " + selectedVatPham.getSoLuongTon() + " " + selectedVatPham.getDonVi() + ")");
            lblSelectedItem.setForeground(UIConstants.PRIMARY);
        }
    }

    private void onConfirm() {
        if (selectedVatPham == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 mặt hàng trong bảng danh sách.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String slStr = txtSoLuongNhap.getText().trim();
        try {
            soLuongNhap = Integer.parseInt(slStr);
            if (soLuongNhap <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng nhập thêm phải là số nguyên dương lớn hơn 0.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            txtSoLuongNhap.requestFocus();
            return;
        }

        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() { return confirmed; }
    public DichVu getSelectedVatPham() { return selectedVatPham; }
    public int getSoLuongNhap() { return soLuongNhap; }
}
