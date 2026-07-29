package GiaoDien.Panels;

import Model.DatLich;
import Model.DichVu;
import Model.KhuVucSan;
import Utils.DataStore;
import Utils.PageUI;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Quản lý kinh doanh & Báo cáo tài chính chuyên nghiệp dạng Bảng (JTable).
 * Thống kê doanh thu sân bóng, doanh thu dịch vụ/đồ ăn, báo cáo chi tiết & Xuất Excel (CSV).
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class QuanLyKinhDoanhPanel extends javax.swing.JPanel {

    private JPanel cardDoanhThu;
    private JPanel cardTienSan;
    private JPanel cardTienDichVu;
    private JPanel cardTB;

    private DefaultTableModel modelBySan;
    private DefaultTableModel modelSummaryTable;
    private DefaultTableModel modelByDichVu;
    private JComboBox<String> cboFilter;
    private JComboBox<String> cboTimeRange;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlBody;
    private javax.swing.JPanel pnlFilterBar;
    private javax.swing.JPanel pnlHeaderWrap;
    private javax.swing.JPanel pnlMid;
    private javax.swing.JPanel pnlStats;
    private javax.swing.JPanel pnlTop;
    // End of variables declaration//GEN-END:variables

    public QuanLyKinhDoanhPanel() {
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
        pnlTop = new javax.swing.JPanel();
        pnlFilterBar = new javax.swing.JPanel();
        pnlStats = new javax.swing.JPanel();
        pnlMid = new javax.swing.JPanel();

        setBackground(UIConstants.BG);
        setLayout(new java.awt.BorderLayout());

        pnlHeaderWrap.setOpaque(false);
        pnlHeaderWrap.setLayout(new java.awt.BorderLayout());
        add(pnlHeaderWrap, java.awt.BorderLayout.NORTH);

        pnlBody.setBackground(UIConstants.BG);
        pnlBody.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlBody.setLayout(new java.awt.BorderLayout(0, 14));

        pnlTop.setOpaque(false);
        pnlTop.setLayout(new java.awt.BorderLayout(0, 12));

        pnlFilterBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 4));
        pnlTop.add(pnlFilterBar, java.awt.BorderLayout.NORTH);

        pnlStats.setOpaque(false);
        pnlStats.setLayout(new java.awt.GridLayout(1, 4, 14, 0));
        pnlTop.add(pnlStats, java.awt.BorderLayout.CENTER);

        pnlBody.add(pnlTop, java.awt.BorderLayout.NORTH);

        pnlMid.setOpaque(false);
        pnlMid.setLayout(new java.awt.GridLayout(1, 2, 14, 0));
        pnlBody.add(pnlMid, java.awt.BorderLayout.CENTER);

        add(pnlBody, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void customInit() {
        pnlHeaderWrap.add(PageUI.createPageHeader("Báo cáo Tài chính & Doanh thu",
                "Tổng hợp doanh thu sân bóng, doanh thu dịch vụ/đồ ăn & Bảng phân tích kinh doanh"), BorderLayout.CENTER);

        // 4 Sleek KPI Metric Cards
        cardDoanhThu = PageUI.createStatCard("TỔNG DOANH THU", "0 đ", UIConstants.PRIMARY);
        cardTienSan = PageUI.createStatCard("DOANH THU TIỀN SÂN", "0 đ", new ColorBlue());
        cardTienDichVu = PageUI.createStatCard("DOANH THU DV & ĐỒ ĂN", "0 đ", UIConstants.SUCCESS);
        cardTB = PageUI.createStatCard("TRUNG BÌNH / PHIẾU", "0 đ", UIConstants.WARNING);

        pnlStats.add(cardDoanhThu);
        pnlStats.add(cardTienSan);
        pnlStats.add(cardTienDichVu);
        pnlStats.add(cardTB);

        // Filter Controls
        LocalDate now = LocalDate.now();
        String todayStr = now.toString();
        String currentMonthStr = String.format("%04d-%02d", now.getYear(), now.getMonthValue());
        String currentYearStr = String.valueOf(now.getYear());

        cboTimeRange = new JComboBox<>(new String[]{
                "Tất cả thời gian",
                "Theo Ngày (" + todayStr + ")",
                "Theo Tháng (" + currentMonthStr + ")",
                "Theo Năm (" + currentYearStr + ")",
                "Chọn Ngày cụ thể..."
        });
        cboTimeRange.setSelectedIndex(0);
        cboTimeRange.addActionListener(e -> {
            if (cboTimeRange.getSelectedIndex() == 4) {
                javax.swing.JFrame parent = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);
                GiaoDien.Dialogs.ChonNgayDialog dialog = new GiaoDien.Dialogs.ChonNgayDialog(parent, selectedDateFilter);
                dialog.setVisible(true);
                if (dialog.isConfirmed() && dialog.getSelectedDate() != null) {
                    selectedDateFilter = dialog.getSelectedDate();
                }
            }
            refresh();
        });

        cboFilter = new JComboBox<>(new String[]{
                "Tất cả trạng thái", "Chỉ phiếu Hoàn thành", "Hoàn thành + Đã xác nhận"
        });
        cboFilter.setSelectedIndex(2);
        cboFilter.addActionListener(e -> refresh());

        pnlFilterBar.add(new javax.swing.JLabel("Thời gian:"));
        cboTimeRange.setPreferredSize(new Dimension(210, 32));
        pnlFilterBar.add(cboTimeRange);

        pnlFilterBar.add(new javax.swing.JLabel("Trạng thái:"));
        cboFilter.setPreferredSize(new Dimension(190, 32));
        pnlFilterBar.add(cboFilter);

        JButton btnReport = new javax.swing.JButton("Tổng hợp báo cáo");
        PageUI.stylePrimaryButton(btnReport);
        btnReport.addActionListener(e -> onTongHopBaoCao());
        pnlFilterBar.add(btnReport);

        JButton btnRefresh = new javax.swing.JButton("↻ Làm mới dữ liệu");
        PageUI.styleSecondaryButton(btnRefresh);
        btnRefresh.addActionListener(e -> {
            refresh();
            JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu báo cáo kinh doanh từ CSDL!", "Làm mới dữ liệu", JOptionPane.INFORMATION_MESSAGE);
        });
        pnlFilterBar.add(btnRefresh);

        JButton btnExport = new javax.swing.JButton("💾 Xuất Excel (CSV)");
        PageUI.styleSuccessButton(btnExport);
        btnExport.addActionListener(e -> onExportExcel());
        pnlFilterBar.add(btnExport);

        // Left Panel: Doanh thu theo Sân
        JPanel left = new javax.swing.JPanel(new BorderLayout(0, 8));
        JLabel lt = new JLabel("Bảng Doanh thu theo Khu vực sân bóng");
        lt.setFont(UIConstants.FONT_SUBTITLE);
        lt.setForeground(UIConstants.PRIMARY);
        left.add(lt, BorderLayout.NORTH);

        modelBySan = new DefaultTableModel(
                new String[]{"Mã sân", "Tên sân bóng", "Số lượt", "Tiền sân", "Tiền DV", "Tổng doanh thu"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableSan = new JTable(modelBySan);
        PageUI.styleTable(tableSan);
        tableSan.getColumnModel().getColumn(0).setMaxWidth(60);
        left.add(new javax.swing.JScrollPane(tableSan), BorderLayout.CENTER);

        // Right Panel: Tabbed Detailed Reports (Bảng Báo cáo Tổng hợp & Bảng Doanh thu dịch vụ)
        JPanel right = new javax.swing.JPanel(new BorderLayout(0, 8));
        JLabel rt = new JLabel("Bảng Báo cáo & Phân tích Tài chính Chi tiết");
        rt.setFont(UIConstants.FONT_SUBTITLE);
        rt.setForeground(UIConstants.PRIMARY);
        right.add(rt, BorderLayout.NORTH);

        JTabbedPane tabReports = new JTabbedPane();

        // Tab 1: BẢNG BÁO CÁO TỔNG HỢP (JTable)
        modelSummaryTable = new DefaultTableModel(
                new String[]{"Chỉ số tài chính / Báo cáo", "Giá trị thực tế"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableSummary = new JTable(modelSummaryTable);
        PageUI.styleTable(tableSummary);
        tableSummary.getColumnModel().getColumn(0).setPreferredWidth(220);
        tableSummary.getColumnModel().getColumn(1).setPreferredWidth(180);

        JScrollPane spSummary = new JScrollPane(tableSummary);
        tabReports.addTab("Báo cáo Tổng hợp", spSummary);

        // Tab 2: Bảng chi tiết doanh thu Dịch vụ & Đồ ăn
        modelByDichVu = new DefaultTableModel(
                new String[]{"ID", "Tên Dịch vụ / Đồ ăn", "Đơn giá", "Đơn vị", "Loại", "Doanh thu ước tính"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableDichVu = new JTable(modelByDichVu);
        PageUI.styleTable(tableDichVu);
        tableDichVu.getColumnModel().getColumn(0).setMaxWidth(50);
        tabReports.addTab("Chi tiết DV & Đồ ăn", new JScrollPane(tableDichVu));

        right.add(tabReports, BorderLayout.CENTER);

        pnlMid.add(left);
        pnlMid.add(right);

        refresh();
    }

    private LocalDate selectedDateFilter = LocalDate.now();

    private String getTimePeriodLabel() {
        int timeMode = cboTimeRange == null ? 0 : cboTimeRange.getSelectedIndex();
        LocalDate now = LocalDate.now();
        return switch (timeMode) {
            case 1 -> "Theo Ngày (" + selectedDateFilter.toString() + ")";
            case 2 -> "Theo Tháng (" + String.format("%02d/%04d", now.getMonthValue(), now.getYear()) + ")";
            case 3 -> "Theo Năm (" + now.getYear() + ")";
            case 4 -> "Theo Ngày chọn (" + selectedDateFilter.toString() + ")";
            default -> "Tất cả thời gian";
        };
    }

    private void onTongHopBaoCao() {
        refresh();
        JOptionPane.showMessageDialog(this,
                "ĐÃ TỔNG HỢP BÁO CÁO KINH DOANH & TÀI CHÍNH THÀNH CÔNG!\n\n"
                        + "• Kỳ báo cáo: " + getTimePeriodLabel() + "\n"
                        + "• Dữ liệu doanh thu sân bóng, doanh thu dịch vụ/đồ ăn đã được cập nhật vào Bảng.\n"
                        + "• Vui lòng xem chi tiết tại Bảng Doanh thu theo sân và Tab Báo cáo tổng hợp.",
                "Tổng hợp báo cáo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onExportExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Xuất báo cáo kinh doanh ra Excel (CSV)");
        String fileNamePeriod = getTimePeriodLabel().replaceAll("[^a-zA-Z0-9_-]", "_");
        fileChooser.setSelectedFile(new File("BaoCaoDoanhThu_" + fileNamePeriod + ".csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) return;

        File fileToSave = fileChooser.getSelectedFile();
        if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
            fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileToSave), StandardCharsets.UTF_8)) {
            writer.write("\uFEFF"); // UTF-8 BOM for Microsoft Excel compatibility

            List<DatLich> all = DataStore.get().getDatLichs();
            int mode = cboFilter == null ? 2 : cboFilter.getSelectedIndex();
            int timeMode = cboTimeRange == null ? 0 : cboTimeRange.getSelectedIndex();
            LocalDate now = LocalDate.now();
            String todayStr = selectedDateFilter.toString();
            String monthPrefix = String.format("%04d-%02d", now.getYear(), now.getMonthValue());
            String yearPrefix = String.valueOf(now.getYear());

            List<DatLich> revenueList = all.stream().filter(d -> {
                String dNgay = d.getNgayDat() != null ? d.getNgayDat().trim() : "";
                if (timeMode == 1 && !todayStr.equalsIgnoreCase(dNgay)) return false;
                if (timeMode == 2 && !dNgay.startsWith(monthPrefix)) return false;
                if (timeMode == 3 && !dNgay.startsWith(yearPrefix)) return false;
                if (timeMode == 4 && !selectedDateFilter.toString().equalsIgnoreCase(dNgay)) return false;

                if (mode == 1) return "HoanThanh".equals(d.getTrangThai());
                if (mode == 2) return "HoanThanh".equals(d.getTrangThai()) || "DaXacNhan".equals(d.getTrangThai());
                return !"DaHuy".equals(d.getTrangThai()) && !"ChoXacNhan".equals(d.getTrangThai());
            }).collect(Collectors.toList());

            double total = revenueList.stream().mapToDouble(DatLich::getTongTien).sum();
            double tienSanTotal = revenueList.stream().mapToDouble(DatLich::getTienSan).sum();
            double tienDvTotal = revenueList.stream().mapToDouble(DatLich::getTienDichVu).sum();
            long done = revenueList.stream().filter(d -> "HoanThanh".equals(d.getTrangThai())).count();
            long cancel = all.stream().filter(d -> "DaHuy".equals(d.getTrangThai())).count();
            double avg = revenueList.isEmpty() ? 0 : total / revenueList.size();

            writer.write("BÁO CÁO TỔNG HỢP KINH DOANH & TÀI CHÍNH SÂN BÓNG\n");
            writer.write("Kỳ báo cáo," + getTimePeriodLabel() + "\n");
            writer.write("Ngày xuất file," + LocalDate.now().toString() + "\n\n");

            writer.write("1. TỔNG QUAN TÀI CHÍNH KỲ BÁO CÁO\n");
            writer.write("Chỉ số tài chính,Giá trị (VNĐ)\n");
            writer.write("Số phiếu trong kỳ báo cáo," + revenueList.size() + "\n");
            writer.write("Tổng doanh thu,\"" + String.format("%,.0f VNĐ", (double) (total)) + "\"\n");
            writer.write("Doanh thu tiền sân,\"" + String.format("%,.0f VNĐ", (double) (tienSanTotal)) + "\"\n");
            writer.write("Doanh thu Dịch vụ & Đồ ăn,\"" + String.format("%,.0f VNĐ", (double) (tienDvTotal)) + "\"\n");
            writer.write("Số phiếu hoàn thành," + done + "\n");
            writer.write("Trung bình / phiếu DT,\"" + String.format("%,.0f VNĐ", (double) (avg)) + "\"\n\n");

            writer.write("2. PHÂN TÍCH DOANH THU THEO SÂN BÓNG\n");
            writer.write("Tên sân bóng,Số lượt đặt,Doanh thu tiền sân,Doanh thu dịch vụ,Tổng doanh thu\n");

            Map<String, double[]> bySan = new LinkedHashMap<>();
            for (DatLich d : revenueList) {
                bySan.computeIfAbsent(d.getTenSan(), k -> new double[3]);
                bySan.get(d.getTenSan())[0] += 1;
                bySan.get(d.getTenSan())[1] += d.getTienSan();
                bySan.get(d.getTenSan())[2] += d.getTienDichVu();
            }

            for (Map.Entry<String, double[]> e : bySan.entrySet()) {
                double subtotal = e.getValue()[1] + e.getValue()[2];
                writer.write(String.format("\"%s\",%d,\"%s\",\"%s\",\"%s\"\n",
                        e.getKey(), (int) e.getValue()[0],
                        String.format("%,.0f VNĐ", (double) (e.getValue()[1])),
                        String.format("%,.0f VNĐ", (double) (e.getValue()[2])),
                        String.format("%,.0f VNĐ", (double) (subtotal))));
            }

            writer.write("\n3. CHI TIẾT PHIẾU ĐẶT SÂN TÍNH DOANH THU\n");
            writer.write("Mã phiếu,Sân bóng,Khách hàng,Số điện thoại,Ngày đặt,Khung giờ,Tiền sân,Tiền dịch vụ,Tổng tiền,Đồ kèm,Trạng thái\n");
            for (DatLich d : revenueList) {
                writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                        d.getMaPhieu(), d.getTenSan(), d.getTenKhach(), d.getSoDienThoai(),
                        d.getNgayDat(), d.getKhungGio(),
                        String.format("%,.0f VNĐ", (double) (d.getTienSan())),
                        String.format("%,.0f VNĐ", (double) (d.getTienDichVu())),
                        String.format("%,.0f VNĐ", (double) (d.getTongTien())),
                        d.getDichVuKem() != null ? d.getDichVuKem().replace("\"", "'").replace("\n", " | ") : "",
                        d.getTrangThaiHienThi()));
            }

            writer.flush();
            JOptionPane.showMessageDialog(this,
                    "ĐÃ XUẤT BÁO CÁO RA FILE EXCEL (CSV) THÀNH CÔNG!\n\n• Kỳ báo cáo: " + getTimePeriodLabel() + "\n• Đường dẫn file: " + fileToSave.getAbsolutePath(),
                    "Xuất Excel thành công", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi xuất file Excel (CSV): " + ex.getMessage(),
                    "Lỗi xuất file", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class ColorBlue extends java.awt.Color {
        ColorBlue() { super(25, 118, 210); }
    }

    public void refresh() {
        List<DatLich> all = DataStore.get().getDatLichs();
        int mode = cboFilter == null ? 2 : cboFilter.getSelectedIndex();
        int timeMode = cboTimeRange == null ? 0 : cboTimeRange.getSelectedIndex();
        LocalDate now = LocalDate.now();
        String todayStr = selectedDateFilter.toString();
        String monthPrefix = String.format("%04d-%02d", now.getYear(), now.getMonthValue());
        String yearPrefix = String.valueOf(now.getYear());

        List<DatLich> revenueList = all.stream().filter(d -> {
            String dNgay = d.getNgayDat() != null ? d.getNgayDat().trim() : "";
            if (timeMode == 1 && !todayStr.equalsIgnoreCase(dNgay)) return false;
            if (timeMode == 2 && !dNgay.startsWith(monthPrefix)) return false;
            if (timeMode == 3 && !dNgay.startsWith(yearPrefix)) return false;
            if (timeMode == 4 && !selectedDateFilter.toString().equalsIgnoreCase(dNgay)) return false;

            if (mode == 1) return "HoanThanh".equals(d.getTrangThai());
            if (mode == 2) return "HoanThanh".equals(d.getTrangThai()) || "DaXacNhan".equals(d.getTrangThai());
            return !"DaHuy".equals(d.getTrangThai()) && !"ChoXacNhan".equals(d.getTrangThai());
        }).collect(Collectors.toList());

        double total = revenueList.stream().mapToDouble(DatLich::getTongTien).sum();
        double tienSanTotal = revenueList.stream().mapToDouble(DatLich::getTienSan).sum();
        double tienDvTotal = revenueList.stream().mapToDouble(DatLich::getTienDichVu).sum();

        long done = all.stream().filter(d -> "HoanThanh".equals(d.getTrangThai())).count();
        long cancel = all.stream().filter(d -> "DaHuy".equals(d.getTrangThai())).count();
        double avg = revenueList.isEmpty() ? 0 : total / revenueList.size();

        PageUI.updateStatCard(cardDoanhThu, String.format("%,.0f VNĐ", (double) (total)));
        PageUI.updateStatCard(cardTienSan, String.format("%,.0f VNĐ", (double) (tienSanTotal)));
        PageUI.updateStatCard(cardTienDichVu, String.format("%,.0f VNĐ", (double) (tienDvTotal)));
        PageUI.updateStatCard(cardTB, String.format("%,.0f VNĐ", (double) (avg)));

        // 1. Fill Doanh Thu Theo Sân Table
        Map<String, double[]> bySan = new LinkedHashMap<>();
        for (DatLich d : revenueList) {
            bySan.computeIfAbsent(d.getTenSan(), k -> new double[3]);
            bySan.get(d.getTenSan())[0] += 1;
            bySan.get(d.getTenSan())[1] += d.getTienSan();
            bySan.get(d.getTenSan())[2] += d.getTienDichVu();
        }

        if (modelBySan != null) {
            modelBySan.setRowCount(0);
            for (KhuVucSan k : DataStore.get().getKhuVucs()) {
                double[] val = bySan.getOrDefault(k.getTenSan(), new double[3]);
                double subtotal = val[1] + val[2];
                modelBySan.addRow(new Object[]{
                        k.getMaSan(),
                        k.getTenSan(),
                        (int) val[0],
                        String.format("%,.0f VNĐ", (double) (val[1])),
                        String.format("%,.0f VNĐ", (double) (val[2])),
                        String.format("%,.0f VNĐ", (double) (subtotal))
                });
            }
        }

        // 2. Fill Báo Cáo Tổng Hợp JTable (WITHOUT GHI CHÚ VÀ TỶ TRỌNG)
        if (modelSummaryTable != null) {
            modelSummaryTable.setRowCount(0);

            modelSummaryTable.addRow(new Object[]{
                    "TỔNG DOANH THU KINH DOANH",
                    String.format("%,.0f VNĐ", (double) (total))
            });
            modelSummaryTable.addRow(new Object[]{
                    "Doanh thu tiền thuê sân bóng",
                    String.format("%,.0f VNĐ", (double) (tienSanTotal))
            });
            modelSummaryTable.addRow(new Object[]{
                    "Doanh thu dịch vụ & đồ ăn",
                    String.format("%,.0f VNĐ", (double) (tienDvTotal))
            });
            modelSummaryTable.addRow(new Object[]{
                    "Tổng số phiếu đặt lịch",
                    all.size() + " phiếu"
            });
            modelSummaryTable.addRow(new Object[]{
                    "Số phiếu hoàn thành",
                    done + " phiếu"
            });
            modelSummaryTable.addRow(new Object[]{
                    "Số phiếu đã hủy",
                    cancel + " phiếu"
            });
            modelSummaryTable.addRow(new Object[]{
                    "Giá trị trung bình / phiếu",
                    String.format("%,.0f VNĐ", (double) (avg))
            });
            modelSummaryTable.addRow(new Object[]{
                    "Mặt hàng kho & Dịch vụ",
                    (DataStore.get().getDichVus().size() + DataStore.get().getKhoItems().size()) + " loại"
            });
        }

        // 3. Fill Doanh Thu Chi Tiết Dịch Vụ JTable
        if (modelByDichVu != null) {
            modelByDichVu.setRowCount(0);

            // Dịch vụ trải nghiệm
            for (DichVu dv : DataStore.get().getDichVus()) {
                double estRev = dv.getDonGia() * (100 - dv.getSoLuongTon());
                modelByDichVu.addRow(new Object[]{
                        dv.getId(), dv.getTenDichVu(),
                        String.format("%,.0f VNĐ", (double) dv.getDonGia()),
                        dv.getDonVi(), "Dịch vụ",
                        String.format("%,.0f VNĐ", (double) Math.max(dv.getDonGia(), estRev))
                });
            }

            // Mặt hàng Kho (Đồ ăn, Nước, Bóng...)
            for (DichVu kho : DataStore.get().getKhoItems()) {
                double estRev = kho.getDonGia() * (150 - kho.getSoLuongTon());
                modelByDichVu.addRow(new Object[]{
                        kho.getId(), kho.getTenDichVu(),
                        String.format("%,.0f VNĐ", (double) kho.getDonGia()),
                        kho.getDonVi(), "Đồ ăn / Kho",
                        String.format("%,.0f VNĐ", (double) Math.max(kho.getDonGia(), estRev))
                });
            }
        }
    }
}
