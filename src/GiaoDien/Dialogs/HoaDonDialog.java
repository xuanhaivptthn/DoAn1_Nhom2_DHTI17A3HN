package GiaoDien.Dialogs;

import Model.DatLich;
import Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Hộp thoại hiển thị và Xuất hóa đơn cho từng đơn đặt sân bóng.
 */
public class HoaDonDialog extends JDialog {

    private final DatLich datLich;
    private final String phuongThucTT;

    public HoaDonDialog(JFrame parent, DatLich datLich) {
        this(parent, datLich, "Tiền mặt");
    }

    public HoaDonDialog(JFrame parent, DatLich datLich, String phuongThucTT) {
        super(parent, "Hóa đơn thanh toán - " + (datLich != null ? datLich.getMaPhieu() : ""), true);
        this.datLich = datLich;
        this.phuongThucTT = phuongThucTT != null ? phuongThucTT : "Tiền mặt";

        initUI(parent);
    }

    private void initUI(JFrame parent) {
        setSize(520, 620);
        if (parent != null) setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel lblTitle = new JLabel("HÓA ĐƠN THANH TOÁN SÂN BÓNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        add(pnlHeader, BorderLayout.NORTH);

        // Center HTML Preview
        JTextPane txtInvoicePreview = new JTextPane();
        txtInvoicePreview.setContentType("text/html");
        txtInvoicePreview.setEditable(false);
        txtInvoicePreview.setText(generateInvoiceHtml());
        txtInvoicePreview.setCaretPosition(0);

        JScrollPane sp = new JScrollPane(txtInvoicePreview);
        sp.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        add(sp, BorderLayout.CENTER);

        // Footer Buttons
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        pnlFooter.setBackground(UIConstants.BG);

        JButton btnExportExcel = new JButton("Xuất file Excel/CSV");
        btnExportExcel.setFont(UIConstants.FONT_BOLD);
        btnExportExcel.addActionListener(e -> onExportCsv());

        JButton btnExportHtml = new JButton("Xuất file HTML");
        btnExportHtml.addActionListener(e -> onExportHtml());

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());

        pnlFooter.add(btnExportExcel);
        pnlFooter.add(btnExportHtml);
        pnlFooter.add(btnClose);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    private String generateInvoiceHtml() {
        if (datLich == null) return "<html><body><h3>Không có dữ liệu hóa đơn</h3></body></html>";

        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String maHd = "HD-" + datLich.getMaPhieu();

        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>")
            .append("body { font-family: 'Segoe UI', sans-serif; margin: 10px; color: #333; }")
            .append("h2 { color: #1E3A8A; margin-bottom: 2px; text-align: center; }")
            .append(".subtitle { text-align: center; color: #666; font-size: 11px; margin-bottom: 15px; }")
            .append(".info-table { width: 100%; border-collapse: collapse; margin-bottom: 15px; }")
            .append(".info-table td { padding: 4px 6px; font-size: 13px; }")
            .append(".items-table { width: 100%; border-collapse: collapse; margin-bottom: 15px; }")
            .append(".items-table th { background-color: #F1F5F9; color: #1E293B; border-bottom: 2px solid #CBD5E1; padding: 6px; text-align: left; font-size: 12px; }")
            .append(".items-table td { border-bottom: 1px solid #E2E8F0; padding: 6px; font-size: 12px; }")
            .append(".total-box { background-color: #EFF6FF; border: 1px solid #BFDBFE; padding: 10px; border-radius: 6px; margin-top: 10px; }")
            .append(".total-row { font-size: 15px; font-weight: bold; color: #1D4ED8; }")
            .append(".footer-text { text-align: center; margin-top: 20px; font-size: 11px; color: #888; italic; }")
            .append("</style></head><body>");

        html.append("<h2>HỆ THỐNG QUẢN LÝ SÂN BÓNG</h2>");
        html.append("<div class='subtitle'>HÓA ĐƠN TỔNG HỢP TIỀN SÂN & DỊCH VỤ</div>");

        html.append("<table class='info-table'>")
            .append("<tr><td><b>Mã hóa đơn:</b> ").append(maHd).append("</td><td><b>Thời gian lập:</b> ").append(nowStr).append("</td></tr>")
            .append("<tr><td><b>Khách hàng:</b> ").append(datLich.getTenKhach()).append("</td><td><b>Số điện thoại:</b> ").append(datLich.getSoDienThoai()).append("</td></tr>")
            .append("<tr><td><b>Sân bóng:</b> ").append(datLich.getTenSan()).append("</td><td><b>Ngày đá:</b> ").append(datLich.getNgayDat()).append("</td></tr>")
            .append("<tr><td><b>Khung giờ:</b> ").append(datLich.getKhungGio()).append("</td><td><b>Hình thức TT:</b> ").append(phuongThucTT).append("</td></tr>")
            .append("<tr><td><b>Nhân viên lập:</b> ").append(datLich.getNhanVienLap() != null ? datLich.getNhanVienLap() : "Admin").append("</td><td><b>Trạng thái:</b> <span style='color:green;'><b>Đã thanh toán</b></span></td></tr>")
            .append("</table>");

        html.append("<h4>Chi tiết khoản thu</h4>");
        html.append("<table class='items-table'>")
            .append("<tr><th>Mục thanh toán</th><th style='text-align:right;'>Số tiền (VNĐ)</th></tr>");

        html.append("<tr><td>Tiền thuê sân bóng (").append(datLich.getKhungGio()).append(")</td>")
            .append("<td style='text-align:right;'>").append(String.format("%,.0f VNĐ", (double) datLich.getTienSan())).append("</td></tr>");

        if (datLich.getDichVuKem() != null && !datLich.getDichVuKem().isBlank()) {
            String[] lines = datLich.getDichVuKem().split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    html.append("<tr><td>• ").append(line.trim()).append("</td><td style='text-align:right;'>—</td></tr>");
                }
            }
            html.append("<tr><td><b>Tổng tiền Dịch vụ & Đồ ăn</b></td><td style='text-align:right;'><b>")
                .append(String.format("%,.0f VNĐ", (double) datLich.getTienDichVu())).append("</b></td></tr>");
        }

        if (datLich.getDatCoc() > 0) {
            html.append("<tr><td>Tiền cọc đã thanh toán trước</td><td style='text-align:right; color:red;'>-")
                .append(String.format("%,.0f VNĐ", (double) datLich.getDatCoc())).append("</td></tr>");
        }

        html.append("</table>");

        html.append("<div class='total-box'>")
            .append("<div class='total-row'>TỔNG THỰC THU: ").append(String.format("%,.0f VNĐ", (double) datLich.getTongTien())).append("</div>")
            .append("</div>");

        html.append("<div class='footer-text'>Cảm ơn quý khách đã sử dụng dịch vụ sân bóng! Hẹn gặp lại quý khách.</div>");
        html.append("</body></html>");

        return html.toString();
    }

    private void onExportCsv() {
        if (datLich == null) return;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Xuất Hóa đơn ra Excel (CSV)");
        chooser.setSelectedFile(new File("HoaDon_" + datLich.getMaPhieu() + ".csv"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File saveFile = chooser.getSelectedFile();
            if (!saveFile.getName().toLowerCase().endsWith(".csv")) {
                saveFile = new File(saveFile.getAbsolutePath() + ".csv");
            }

            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(saveFile), StandardCharsets.UTF_8)) {
                writer.write("\uFEFF"); // UTF-8 BOM
                writer.write("HÓA ĐƠN THANH TOÁN SÂN BÓNG\n");
                writer.write("Mã hóa đơn," + "HD-" + datLich.getMaPhieu() + "\n");
                writer.write("Mã phiếu đặt," + datLich.getMaPhieu() + "\n");
                writer.write("Tên khách hàng," + datLich.getTenKhach() + "\n");
                writer.write("Số điện thoại," + datLich.getSoDienThoai() + "\n");
                writer.write("Sân bóng," + datLich.getTenSan() + "\n");
                writer.write("Ngày đặt," + datLich.getNgayDat() + "\n");
                writer.write("Khung giờ," + datLich.getKhungGio() + "\n");
                writer.write("Hình thức thanh toán," + phuongThucTT + "\n");
                writer.write("Nhân viên lập," + (datLich.getNhanVienLap() != null ? datLich.getNhanVienLap() : "Admin") + "\n\n");

                writer.write("KHOẢN THU,THÀNH TIỀN (VNĐ)\n");
                writer.write(String.format("Tiền thuê sân bóng,\"%,.0f VNĐ\"\n", (double) datLich.getTienSan()));
                writer.write(String.format("Tiền dịch vụ & đồ ăn,\"%,.0f VNĐ\"\n", (double) datLich.getTienDichVu()));
                if (datLich.getDatCoc() > 0) {
                    writer.write(String.format("Tiền cọc đã trả,\"-,.0f VNĐ\"\n", (double) datLich.getDatCoc()));
                }
                writer.write(String.format("TỔNG CỘNG THỰC THU,\"%,.0f VNĐ\"\n", (double) datLich.getTongTien()));

                if (datLich.getDichVuKem() != null && !datLich.getDichVuKem().isBlank()) {
                    writer.write("\nCHI TIẾT ĐỒ ĂN & DỊCH VỤ:\n");
                    writer.write("\"" + datLich.getDichVuKem().replace("\n", " | ") + "\"\n");
                }

                writer.flush();
                JOptionPane.showMessageDialog(this, "Đã xuất Hóa đơn ra Excel (CSV) thành công!\nĐường dẫn: " + saveFile.getAbsolutePath(), "Xuất thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất hóa đơn CSV: " + ex.getMessage(), "Lỗi xuất file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onExportHtml() {
        if (datLich == null) return;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Xuất Hóa đơn ra file HTML");
        chooser.setSelectedFile(new File("HoaDon_" + datLich.getMaPhieu() + ".html"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File saveFile = chooser.getSelectedFile();
            if (!saveFile.getName().toLowerCase().endsWith(".html")) {
                saveFile = new File(saveFile.getAbsolutePath() + ".html");
            }

            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(saveFile), StandardCharsets.UTF_8)) {
                writer.write(generateInvoiceHtml());
                writer.flush();
                JOptionPane.showMessageDialog(this, "Đã xuất Hóa đơn ra file HTML thành công!\nĐường dẫn: " + saveFile.getAbsolutePath(), "Xuất thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất hóa đơn HTML: " + ex.getMessage(), "Lỗi xuất file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
