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
 * Hộp thoại (JDialog) hiển thị và xuất hóa đơn chi tiết cho đơn đặt sân bóng.
 * <p>
 * Dialog hỗ trợ xem trước hóa đơn dưới dạng HTML trình bày chuyên nghiệp,
 * hỗ trợ tính tổng tiền sân bóng, tiền dịch vụ, tiền hàng kho, tiền đặt cọc và tiền thực thu,
 * đồng thời cung cấp tính năng xuất hóa đơn ra file CSV (Excel) hoặc HTML.
 * </p>
 */
public class HoaDonDialog extends JDialog {

    /** Đối tượng phiếu đặt lịch cần lập và xem hóa đơn */
    private final DatLich datLich;

    /** Phương thức thanh toán (ví dụ: Tiền mặt, Chuyển khoản, Thẻ) */
    private final String phuongThucTT;

    /**
     * Khởi tạo dialog hóa đơn mặc định với hình thức thanh toán Tiền mặt.
     *
     * @param parent  Cửa sổ cha (JFrame)
     * @param datLich Đối tượng phiếu đặt lịch
     */
    public HoaDonDialog(JFrame parent, DatLich datLich) {
        this(parent, datLich, "Tiền mặt");
    }

    /**
     * Khởi tạo dialog hóa đơn với hình thức thanh toán tùy chọn.
     *
     * @param parent       Cửa sổ cha (JFrame)
     * @param datLich      Đối tượng phiếu đặt lịch
     * @param phuongThucTT Chuỗi hình thức thanh toán
     */
    public HoaDonDialog(JFrame parent, DatLich datLich, String phuongThucTT) {
        super(parent, "Hóa đơn thanh toán - " + (datLich != null ? datLich.getMaLichDat() : ""), true);
        this.datLich = datLich;
        this.phuongThucTT = phuongThucTT != null ? phuongThucTT : "Tiền mặt";

        // Đồng bộ/Lưu vết hóa đơn thanh toán vào DataStore
        if (datLich != null) {
            Utils.DataStore.get().saveOrUpdateHoaDonForBooking(datLich, this.phuongThucTT);
        }

        // Khởi tạo các thành phần giao diện
        initUI(parent);
    }

    /**
     * Khởi tạo các thành phần giao diện xem trước hóa đơn và các nút bấm xuất file.
     *
     * @param parent Cửa sổ cha để căn giữa vị trí dialog
     */
    private void initUI(JFrame parent) {
        setSize(520, 620);
        if (parent != null) setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Header trên cùng hiển thị tên hóa đơn
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel lblTitle = new JLabel("HÓA ĐƠN THANH TOÁN SÂN BÓNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        add(pnlHeader, BorderLayout.NORTH);

        // Khung hiển thị nội dung mẫu hóa đơn bằng JTextPane định dạng HTML
        JTextPane txtInvoicePreview = new JTextPane();
        txtInvoicePreview.setContentType("text/html");
        txtInvoicePreview.setEditable(false);
        txtInvoicePreview.setText(generateInvoiceHtml());
        txtInvoicePreview.setCaretPosition(0);

        JScrollPane sp = new JScrollPane(txtInvoicePreview);
        sp.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));
        add(sp, BorderLayout.CENTER);

        // Thanh công cụ nút bấm ở chân dialog
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        pnlFooter.setBackground(UIConstants.BG);

        // Nút xuất hóa đơn ra file Excel (CSV)
        JButton btnExportExcel = new JButton("Xuất file Excel/CSV");
        btnExportExcel.setFont(UIConstants.FONT_BOLD);
        btnExportExcel.addActionListener(e -> onExportCsv());

        // Nút xuất hóa đơn ra file HTML
        JButton btnExportHtml = new JButton("Xuất file HTML");
        btnExportHtml.addActionListener(e -> onExportHtml());

        // Nút Đóng dialog
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());

        pnlFooter.add(btnExportExcel);
        pnlFooter.add(btnExportHtml);
        pnlFooter.add(btnClose);

        add(pnlFooter, BorderLayout.SOUTH);
    }

    /**
     * Sinh mã HTML tổng hợp toàn bộ thông tin chi tiết hóa đơn thanh toán.
     *
     * @return Chuỗi mã HTML của hóa đơn
     */
    private String generateInvoiceHtml() {
        if (datLich == null) return "<html><body><h3>Không có dữ liệu hóa đơn</h3></body></html>";

        Model.HoaDon hd = Utils.DataStore.get().saveOrUpdateHoaDonForBooking(datLich, phuongThucTT);
        String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String maHd = hd != null ? hd.getMaHoaDon() : "HD-" + datLich.getMaLichDat();

        // Tính toán các khoản thu
        double tienSan = datLich.getTienSan();
        double tienDichVu = datLich.getTongTienDichVuOnly();
        double tienKho = datLich.getTongTienKhoOnly();
        double tongCong = tienSan + tienDichVu + tienKho - datLich.getDatCoc();

        String dvDetails = (hd != null && hd.getDichVuKem() != null && !hd.getDichVuKem().isBlank())
                ? hd.getDichVuKem() : datLich.getDichVuKem();

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

        // Bảng thông tin hóa đơn và khách hàng
        html.append("<table class='info-table'>")
            .append("<tr><td><b>Mã hóa đơn:</b> ").append(maHd).append("</td><td><b>Thời gian lập:</b> ").append(nowStr).append("</td></tr>")
            .append("<tr><td><b>Khách hàng:</b> ").append(datLich.getTenKhach()).append("</td><td><b>Số điện thoại:</b> ").append(datLich.getSoDienThoaiKhach()).append("</td></tr>")
            .append("<tr><td><b>Sân bóng:</b> ").append(datLich.getTenSan()).append("</td><td><b>Ngày đá:</b> ").append(datLich.getNgayDat()).append("</td></tr>")
            .append("<tr><td><b>Khung giờ:</b> ").append(datLich.getKhungGio()).append("</td><td><b>Hình thức TT:</b> ").append(phuongThucTT).append("</td></tr>")
            .append("<tr><td><b>Nhân viên lập:</b> ").append(datLich.getMaTaiKhoan() != null ? datLich.getMaTaiKhoan() : "Admin").append("</td><td><b>Trạng thái:</b> <span style='color:green;'><b>Đã thanh toán</b></span></td></tr>")
            .append("</table>");

        // Bảng chi tiết các khoản thu
        html.append("<h4>Chi tiết khoản thu</h4>");
        html.append("<table class='items-table'>")
            .append("<tr><th>Mục thanh toán</th><th style='text-align:right;'>Số tiền (VNĐ)</th></tr>");

        // Tiền thuê sân bóng
        html.append("<tr><td>Tiền thuê sân bóng (").append(datLich.getKhungGio()).append(")</td>")
            .append("<td style='text-align:right;'>").append(String.format("%,.0f VNĐ", tienSan)).append("</td></tr>");

        // Tiền dịch vụ
        if (tienDichVu > 0) {
            html.append("<tr><td>Tiền dịch vụ đi kèm</td><td style='text-align:right;'>")
                .append(String.format("%,.0f VNĐ", tienDichVu)).append("</td></tr>");
        }

        // Tiền hàng kho / đồ ăn
        if (tienKho > 0) {
            html.append("<tr><td>Tiền sản phẩm kho / đồ ăn</td><td style='text-align:right;'>")
                .append(String.format("%,.0f VNĐ", tienKho)).append("</td></tr>");
        }

        // Chi tiết danh sách các món dịch vụ đi kèm
        if (dvDetails != null && !dvDetails.isBlank()) {
            String[] lines = dvDetails.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    html.append("<tr><td colspan='2' style='font-size:11px; color:#555;'>• ").append(line.trim()).append("</td></tr>");
                }
            }
        }

        // Tiền cọc trừ bớt
        if (datLich.getDatCoc() > 0) {
            html.append("<tr><td>Tiền cọc đã thanh toán trước</td><td style='text-align:right; color:red;'>-")
                .append(String.format("%,.0f VNĐ", datLich.getDatCoc())).append("</td></tr>");
        }

        html.append("</table>");

        // Khung tổng số tiền thực thu
        html.append("<div class='total-box'>")
            .append("<div class='total-row'>TỔNG THỰC THU: ").append(String.format("%,.0f VNĐ", tongCong)).append("</div>")
            .append("</div>");

        html.append("<div class='footer-text'>Cảm ơn quý khách đã sử dụng dịch vụ sân bóng! Hẹn gặp lại quý khách.</div>");
        html.append("</body></html>");

        return html.toString();
    }

    /**
     * Xử lý xuất nội dung hóa đơn ra định dạng tập tin Excel (CSV) mã hóa UTF-8 với BOM.
     */
    private void onExportCsv() {
        if (datLich == null) return;
        Model.HoaDon hd = Utils.DataStore.get().saveOrUpdateHoaDonForBooking(datLich, phuongThucTT);
        String maHd = hd != null ? hd.getMaHoaDon() : "HD-" + datLich.getMaLichDat();
        String dvDetails = (hd != null && hd.getDichVuKem() != null && !hd.getDichVuKem().isBlank())
                ? hd.getDichVuKem() : datLich.getDichVuKem();

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Xuất Hóa đơn ra Excel (CSV)");
        chooser.setSelectedFile(new File("HoaDon_" + datLich.getMaLichDat() + ".csv"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File saveFile = chooser.getSelectedFile();
            String cleanName = Utils.CodeGen.removeDiacritics(saveFile.getName());
            saveFile = new File(saveFile.getParent(), cleanName);
            if (!saveFile.getName().toLowerCase().endsWith(".csv")) {
                saveFile = new File(saveFile.getAbsolutePath() + ".csv");
            }

            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(saveFile), StandardCharsets.UTF_8)) {
                // Ghi ký tự UTF-8 BOM để Excel đọc đúng tiếng Việt
                writer.write("\uFEFF");
                writer.write("HÓA ĐƠN THANH TOÁN SÂN BÓNG\n");
                writer.write("Mã hóa đơn," + maHd + "\n");
                writer.write("Mã phiếu đặt," + datLich.getMaLichDat() + "\n");
                writer.write("Tên khách hàng," + datLich.getTenKhach() + "\n");
                writer.write("Số điện thoại," + datLich.getSoDienThoaiKhach() + "\n");
                writer.write("Sân bóng," + datLich.getTenSan() + "\n");
                writer.write("Ngày đặt," + datLich.getNgayDat() + "\n");
                writer.write("Khung giờ," + datLich.getKhungGio() + "\n");
                writer.write("Hình thức thanh toán," + phuongThucTT + "\n");
                writer.write("Nhân viên lập," + (datLich.getMaTaiKhoan() != null ? datLich.getMaTaiKhoan() : "Admin") + "\n\n");

                double tienSan = datLich.getTienSan();
                double tienDichVu = datLich.getTongTienDichVuOnly();
                double tienKho = datLich.getTongTienKhoOnly();
                double tongCong = tienSan + tienDichVu + tienKho - datLich.getDatCoc();

                writer.write("KHOẢN THU,THÀNH TIỀN (VNĐ)\n");
                writer.write(String.format("Tiền thuê sân bóng,\"%,.0f VNĐ\"\n", tienSan));
                writer.write(String.format("Tiền dịch vụ đi kèm,\"%,.0f VNĐ\"\n", tienDichVu));
                writer.write(String.format("Tiền sản phẩm kho / đồ ăn,\"%,.0f VNĐ\"\n", tienKho));
                if (datLich.getDatCoc() > 0) {
                    writer.write(String.format("Tiền cọc đã trả,\"-,.0f VNĐ\"\n", datLich.getDatCoc()));
                }
                writer.write(String.format("TỔNG CỘNG THỰC THU,\"%,.0f VNĐ\"\n", tongCong));

                if (dvDetails != null && !dvDetails.isBlank()) {
                    writer.write("\nCHI TIẾT ĐỒ ĂN & DỊCH VỤ:\n");
                    writer.write("\"" + dvDetails.replace("\n", " | ") + "\"\n");
                }

                writer.flush();
                JOptionPane.showMessageDialog(this, "Đã xuất Hóa đơn ra Excel (CSV) thành công!\nĐường dẫn: " + saveFile.getAbsolutePath(), "Xuất thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất hóa đơn CSV: " + ex.getMessage(), "Lỗi xuất file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Xử lý xuất hóa đơn ra file HTML riêng độc lập để in hoặc lưu trữ.
     */
    private void onExportHtml() {
        if (datLich == null) return;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Xuất Hóa đơn ra file HTML");
        chooser.setSelectedFile(new File("HoaDon_" + datLich.getMaLichDat() + ".html"));

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
