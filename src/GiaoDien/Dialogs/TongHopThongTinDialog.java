package GiaoDien.Dialogs;

import Model.DatLich;
import Utils.PageUI;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * Dialog hiển thị Bảng Tổng hợp thông tin phiếu đặt lịch sân bóng.
 * Cho phép người dùng kiểm tra lại thông tin, bấm xác nhận hoặc hủy trước khi lưu.
 */
public class TongHopThongTinDialog extends JDialog {

    private final DatLich booking;
    private final List<ChonDichVuDialog.SelectedItem> addons;
    private final double addonTotalCost;
    private boolean confirmed = false;

    public TongHopThongTinDialog(JFrame parent, DatLich booking,
                                List<ChonDichVuDialog.SelectedItem> addons,
                                double addonTotalCost) {
        super(parent, "Bảng tổng hợp thông tin đặt sân", true);
        this.booking = booking;
        this.addons = addons;
        this.addonTotalCost = addonTotalCost;

        initComponents(parent);
    }

    private void initComponents(JFrame parent) {
        setSize(620, 600);
        setResizable(false);
        if (parent != null) setLocationRelativeTo(parent);

        // Header
        JPanel pnlHeader = PageUI.createPageHeader(
                "📋 BẢNG TỔNG HỢP THÔNG TIN PHIẾU ĐẶT LỊCH",
                "Vui lòng kiểm tra lại thông tin phiếu đặt trước khi bấm Xác nhận lưu"
        );
        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // Center Content with ScrollPane
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new javax.swing.BoxLayout(pnlMain, javax.swing.BoxLayout.Y_AXIS));
        pnlMain.setBackground(UIConstants.BG);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Card 1: Thông tin Đặt sân & Khách hàng
        JPanel cardInfo = new JPanel(new GridBagLayout());
        cardInfo.setBackground(Color.WHITE);
        cardInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(UIConstants.BORDER),
                        "1. Thông tin sân bóng & Khách hàng",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        UIConstants.FONT_BOLD,
                        UIConstants.PRIMARY
                ),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        row = addDetailRow(cardInfo, gbc, row, "Sân bóng:", booking.getTenSan() != null ? booking.getTenSan() : "—");
        row = addDetailRow(cardInfo, gbc, row, "Khách hàng:", booking.getTenKhach() + " (SĐT: " + booking.getSoDienThoai() + ")");
        row = addDetailRow(cardInfo, gbc, row, "Ngày đặt sân:", booking.getNgayDat());
        row = addDetailRow(cardInfo, gbc, row, "Khung giờ:", booking.getGioBatDau() + " ➔ " + booking.getGioKetThuc()
                + " (" + String.format("%.1f", calculateHours(booking.getGioBatDau(), booking.getGioKetThuc())) + " giờ)");
        row = addDetailRow(cardInfo, gbc, row, "Tiền sân tạm tính:", String.format("%,.0f VNĐ", booking.getTienSan()));
        if (booking.getGhiChu() != null && !booking.getGhiChu().isBlank()) {
            addDetailRow(cardInfo, gbc, row, "Ghi chú:", booking.getGhiChu());
        }

        pnlMain.add(cardInfo);
        pnlMain.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));

        // Card 2: Danh sách Dịch vụ & Đồ ăn kèm theo
        JPanel cardAddons = new JPanel(new BorderLayout(0, 6));
        cardAddons.setBackground(Color.WHITE);
        cardAddons.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(UIConstants.BORDER),
                        "2. Danh sách Dịch vụ & Đồ ăn kèm theo",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        UIConstants.FONT_BOLD,
                        UIConstants.PRIMARY
                ),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        if (addons != null && !addons.isEmpty()) {
            DefaultTableModel modelAddons = new DefaultTableModel(
                    new String[]{"STT", "Tên dịch vụ / đồ ăn", "Số lượng", "Đơn giá", "Thành tiền"}, 0) {
                @Override
                public boolean isCellEditable(int r, int c) { return false; }
            };

            int stt = 1;
            for (ChonDichVuDialog.SelectedItem item : addons) {
                String donVi = item.getDichVu().getDonVi() != null ? item.getDichVu().getDonVi() : "";
                modelAddons.addRow(new Object[]{
                        stt++,
                        item.getDichVu().getTenDichVu(),
                        item.getSoLuong() + " " + donVi,
                        String.format("%,.0f đ", item.getDichVu().getDonGia()),
                        String.format("%,.0f đ", item.getThanhTien())
                });
            }

            JTable tableAddons = new JTable(modelAddons);
            PageUI.styleTable(tableAddons);
            tableAddons.getColumnModel().getColumn(0).setPreferredWidth(40);
            tableAddons.getColumnModel().getColumn(1).setPreferredWidth(180);
            tableAddons.getColumnModel().getColumn(2).setPreferredWidth(90);
            tableAddons.getColumnModel().getColumn(3).setPreferredWidth(90);
            tableAddons.getColumnModel().getColumn(4).setPreferredWidth(100);

            JScrollPane spAddons = new JScrollPane(tableAddons);
            spAddons.setPreferredSize(new Dimension(540, 120));
            cardAddons.add(spAddons, BorderLayout.CENTER);
        } else {
            JLabel lblEmpty = new JLabel("Chưa chọn dịch vụ hoặc đồ ăn kèm theo nào.", SwingConstants.CENTER);
            lblEmpty.setFont(UIConstants.FONT_NORMAL);
            lblEmpty.setForeground(UIConstants.TEXT_SECONDARY);
            lblEmpty.setPreferredSize(new Dimension(540, 45));
            cardAddons.add(lblEmpty, BorderLayout.CENTER);
        }

        pnlMain.add(cardAddons);
        pnlMain.add(javax.swing.Box.createRigidArea(new Dimension(0, 10)));

        // Card 3: Tổng tiền thanh toán
        double courtPrice = booking.getTienSan();
        double totalCost = courtPrice + addonTotalCost;

        JPanel cardTotal = new JPanel(new GridBagLayout());
        cardTotal.setBackground(UIConstants.SUCCESS_BG);
        cardTotal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.PRIMARY_LIGHT, 1),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        GridBagConstraints gbcT = new GridBagConstraints();
        gbcT.insets = new Insets(2, 6, 2, 6);
        gbcT.anchor = GridBagConstraints.WEST;

        gbcT.gridx = 0; gbcT.gridy = 0; gbcT.weightx = 0.5;
        cardTotal.add(new JLabel("Tiền sân bóng:"), gbcT);
        gbcT.gridx = 1; gbcT.anchor = GridBagConstraints.EAST;
        JLabel lblTienSanVal = new JLabel(String.format("%,.0f VNĐ", courtPrice));
        lblTienSanVal.setFont(UIConstants.FONT_BOLD);
        cardTotal.add(lblTienSanVal, gbcT);

        gbcT.gridx = 0; gbcT.gridy = 1; gbcT.anchor = GridBagConstraints.WEST;
        cardTotal.add(new JLabel("Tiền Dịch vụ & Đồ ăn kèm:"), gbcT);
        gbcT.gridx = 1; gbcT.anchor = GridBagConstraints.EAST;
        JLabel lblTienDVVal = new JLabel(String.format("%,.0f VNĐ", addonTotalCost));
        lblTienDVVal.setFont(UIConstants.FONT_BOLD);
        cardTotal.add(lblTienDVVal, gbcT);

        gbcT.gridx = 0; gbcT.gridy = 2; gbcT.gridwidth = 2; gbcT.fill = GridBagConstraints.HORIZONTAL;
        cardTotal.add(new javax.swing.JSeparator(), gbcT);

        gbcT.gridx = 0; gbcT.gridy = 3; gbcT.gridwidth = 1; gbcT.anchor = GridBagConstraints.WEST;
        JLabel lblTotalLabel = new JLabel("TỔNG CỘNG TẠM TÍNH:");
        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotalLabel.setForeground(UIConstants.PRIMARY);
        cardTotal.add(lblTotalLabel, gbcT);

        gbcT.gridx = 1; gbcT.anchor = GridBagConstraints.EAST;
        JLabel lblTotalVal = new JLabel(String.format("%,.0f VNĐ", totalCost));
        lblTotalVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalVal.setForeground(UIConstants.PRIMARY);
        cardTotal.add(lblTotalVal, gbcT);

        pnlMain.add(cardTotal);

        JScrollPane spMain = new JScrollPane(pnlMain);
        spMain.setBorder(null);
        getContentPane().add(spMain, BorderLayout.CENTER);

        // Footer Panel
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        pnlFooter.setBackground(UIConstants.BG);
        pnlFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.BORDER));

        JButton btnCancel = new JButton("✖ Hủy / Quay lại sửa");
        btnCancel.setFont(UIConstants.FONT_BUTTON);
        btnCancel.setPreferredSize(new Dimension(160, 36));
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton btnConfirm = new JButton("✓ XÁC NHẬN LƯU PHIẾU");
        btnConfirm.setFont(UIConstants.FONT_BUTTON);
        btnConfirm.setBackground(UIConstants.PRIMARY);
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setPreferredSize(new Dimension(190, 36));
        btnConfirm.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnConfirm);

        getContentPane().add(pnlFooter, BorderLayout.SOUTH);
    }

    private int addDetailRow(JPanel card, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BOLD);
        card.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        JLabel val = new JLabel(value);
        val.setFont(UIConstants.FONT_NORMAL);
        card.add(val, gbc);

        return row + 1;
    }

    private double calculateHours(String start, String end) {
        try {
            String[] s = start.split(":");
            String[] e = end.split(":");
            int sMin = Integer.parseInt(s[0]) * 60 + Integer.parseInt(s[1]);
            int eMin = Integer.parseInt(e[0]) * 60 + Integer.parseInt(e[1]);
            return (eMin - sMin) / 60.0;
        } catch (Exception ex) {
            return 1.0;
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
