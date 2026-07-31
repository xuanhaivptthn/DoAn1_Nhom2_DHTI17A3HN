package GiaoDien.Dialogs;

import Model.PhienLamViec;
import Utils.DataStore;
import Utils.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

/**
 * Xem lịch sử đăng nhập (Model.PhienLamViec) — mở từ màn hình Quản lý tài khoản.
 */
public class LichSuDangNhapDialog extends JDialog {

    public LichSuDangNhapDialog(JFrame parent) {
        super(parent, "Lịch sử đăng nhập", true);
        setSize(920, 480);
        if (parent != null) setLocationRelativeTo(parent);

        DefaultTableModel model = new DefaultTableModel(new String[]{
                "Mã phiên", "Tên đăng nhập", "Họ tên", "Vai trò",
                "Đăng nhập lúc", "Đăng xuất lúc", "Trạng thái", "Địa chỉ IP", "Thiết bị"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        List<PhienLamViec> list = DataStore.get().getPhienHistory();
        for (PhienLamViec p : list) {
            model.addRow(new Object[]{
                    p.getSessionId(), p.getTenDangNhap(), p.getHoTen(), p.getVaiTroHienThi(),
                    p.getThoiGianDangNhap(), p.getThoiGianDangXuat() == null ? "-" : p.getThoiGianDangXuat(),
                    p.getTrangThaiHienThi(), p.getDiaChiIp(), p.getThietBi()
            });
        }

        JTable table = new JTable(model);
        table.setFont(UIConstants.FONT_TABLE);
        table.setRowHeight(28);
        table.getTableHeader().setFont(UIConstants.FONT_TABLE_HEADER);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER, 1));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroll, BorderLayout.CENTER);

        if (list.isEmpty()) {
            table.setPreferredSize(new Dimension(880, 100));
        }
    }
}
