package GiaoDien.Dialogs;

import GiaoDien.Panels.*;

import Model.KhuVucSan;
import Utils.DataStore;
import Utils.UIConstants;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;

/**
 * Hộp thoại (JDialog) hỗ trợ kiểm tra tính khả dụng của sân bóng theo ngày và khung giờ chọn.
 * <p>
 * Dialog hỗ trợ lọc danh sách sân bóng không bảo trì, chọn ngày qua bộ chọn lịch
 * và cảnh báo nếu sân chọn đang thuộc lịch bảo trì thiết bị/cơ sở vật chất.
 * </p>
 * Tương thích Apache NetBeans GUI Builder Drag & Drop.
 */
public class KiemTraSanDialog extends JDialog {

    /** Combobox lựa chọn sân bóng cần kiểm tra */
    private JComboBox<KhuVucSan> cboSan;

    /** Ô nhập/hiển thị ngày kiểm tra */
    private JTextField txtNgay;

    /** Ô nhập giờ bắt đầu (HH:mm) */
    private JTextField txtGioBatDau;

    /** Ô nhập giờ kết thúc (HH:mm) */
    private JTextField txtGioKetThuc;

    /** Sân bóng được lựa chọn sau khi người dùng bấm Kiểm tra */
    private KhuVucSan selectedSan;

    /** Ngày kiểm tra đã chọn */
    private String ngay;

    /** Giờ bắt đầu đã chọn */
    private String gioBatDau;

    /** Giờ kết thúc đã chọn */
    private String gioKetThuc;

    /** Cờ xác nhận đã bấm kiểm tra thành công */
    private boolean confirmed;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    /** Nhãn tiêu đề header dialog */
    private javax.swing.JLabel lblHeaderTitle;
    /** Panel bọc phần nội dung trung tâm */
    private javax.swing.JPanel pnlCenterWrap;
    /** Panel footer chứa các nút hành động */
    private javax.swing.JPanel pnlFooter;
    /** Panel card form GridBagLayout */
    private javax.swing.JPanel pnlFormCard;
    /** Panel header trên cùng */
    private javax.swing.JPanel pnlHeader;
    // End of variables declaration//GEN-END:variables

    /**
     * Khởi tạo dialog kiểm tra sân mặc định.
     */
    public KiemTraSanDialog() {
        this(null);
    }

    /**
     * Khởi tạo dialog kiểm tra sân với cửa sổ cha chỉ định.
     *
     * @param parent Cửa sổ cha (JFrame)
     */
    public KiemTraSanDialog(JFrame parent) {
        super(parent, "Kiểm tra sân khả dụng", true);

        // Khởi tạo thành phần GUI NetBeans
        initComponents();
        // Cấu hình bố cục chi tiết
        customInit(parent);
    }

    /**
     * NetBeans GUI Builder generated code initialization.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblHeaderTitle = new javax.swing.JLabel();
        pnlCenterWrap = new javax.swing.JPanel();
        pnlFormCard = new javax.swing.JPanel();
        pnlFooter = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Kiểm tra sân khả dụng");
        setResizable(false);

        pnlHeader.setBackground(UIConstants.PRIMARY);
        pnlHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 20, 16, 20));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblHeaderTitle.setFont(UIConstants.FONT_TITLE);
        lblHeaderTitle.setForeground(java.awt.Color.WHITE);
        lblHeaderTitle.setText("Kiểm tra sân khả dụng");
        pnlHeader.add(lblHeaderTitle, java.awt.BorderLayout.WEST);

        getContentPane().add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlCenterWrap.setBackground(UIConstants.BG);
        pnlCenterWrap.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 8, 16));
        pnlCenterWrap.setLayout(new java.awt.BorderLayout());

        pnlFormCard.setLayout(new java.awt.GridBagLayout());
        pnlCenterWrap.add(pnlFormCard, java.awt.BorderLayout.CENTER);

        getContentPane().add(pnlCenterWrap, java.awt.BorderLayout.CENTER);

        pnlFooter.setBackground(UIConstants.BG);
        pnlFooter.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 16, 12, 16));
        pnlFooter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 12));
        getContentPane().add(pnlFooter, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Khởi tạo giao diện nhập liệu chi tiết và nạp danh sách sân bóng không bảo trì.
     *
     * @param parent Cửa sổ cha dùng để căn giữa dialog
     */
    private void customInit(JFrame parent) {
        setSize(450, 380);
        if (parent != null) setLocationRelativeTo(parent);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tải danh sách các sân bóng hoạt động bình thường
        List<KhuVucSan> sans = DataStore.get().getKhuVucsKhongBaoTri();
        cboSan = new JComboBox<>(sans.toArray(new KhuVucSan[0]));
        styleCombo(cboSan);

        int row = 0;
        row = addField(pnlFormCard, gbc, row, "Khu vực sân *", cboSan);

        // Ô chọn ngày kiểm tra tích hợp bộ chọn lịch
        txtNgay = new javax.swing.JTextField();
        txtNgay.setText(java.time.LocalDate.now().toString());
        JPanel pnlNgay = createDatePickerPanel(txtNgay, (JFrame) getOwner());
        row = addField(pnlFormCard, gbc, row, "Ngày kiểm tra *", pnlNgay);

        // Khung nhập giờ bắt đầu
        txtGioBatDau = new javax.swing.JTextField(16);
        txtGioBatDau.setText("18:00");
        row = addField(pnlFormCard, gbc, row, "Giờ bắt đầu *", txtGioBatDau);

        // Khung nhập giờ kết thúc
        txtGioKetThuc = new javax.swing.JTextField(16);
        txtGioKetThuc.setText("19:00");
        addField(pnlFormCard, gbc, row, "Giờ kết thúc *", txtGioKetThuc);

        // Nút Đóng
        JButton btnCancel = new javax.swing.JButton("Đóng");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        // Nút Kiểm tra
        JButton btnCheck = new javax.swing.JButton("Kiểm tra");
        btnCheck.addActionListener(e -> onCheck());

        pnlFooter.add(btnCancel);
        pnlFooter.add(btnCheck);

        getRootPane().setDefaultButton(btnCheck);
    }

    /**
     * Thêm hàng gồm nhãn và trường dữ liệu vào form GridBagLayout.
     *
     * @param form  Panel form
     * @param gbc   Cấu hình GridBagConstraints
     * @param row   Chỉ số hàng
     * @param label Tiêu đề nhãn
     * @param field Thành phần nhập liệu
     * @return Chỉ số hàng tiếp theo
     */
    private int addField(JPanel form, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.38;
        gbc.gridwidth = 1;
        form.add(new javax.swing.JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.62;
        field.setPreferredSize(new Dimension(220, 36));
        form.add(field, gbc);
        return row + 1;
    }

    /**
     * Áp dụng kiểu phông và màu cho combobox.
     *
     * @param combo Combobox cần định dạng
     */
    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UIConstants.FONT_NORMAL);
        combo.setBackground(Color.WHITE);
        combo.setForeground(UIConstants.TEXT_PRIMARY);
    }

    /**
     * Kiểm tra tính hợp lệ dữ liệu và trạng thái bảo trì của sân bóng đã chọn.
     */
    private void onCheck() {
        selectedSan = (KhuVucSan) cboSan.getSelectedItem();
        ngay = txtNgay.getText().trim();
        gioBatDau = txtGioBatDau.getText().trim();
        gioKetThuc = txtGioKetThuc.getText().trim();

        // 1. Vui lòng điền đầy đủ các ô dữ liệu
        if (selectedSan == null || ngay.isEmpty() || gioBatDau.isEmpty() || gioKetThuc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Kiểm tra nếu sân chọn đang bị bảo trì vào ngày đã nhập
        if (DataStore.get().isSanBaoTriVoiNgay(selectedSan, ngay)) {
            JOptionPane.showMessageDialog(this,
                    "[!] KHÔNG THỂ ĐẶT SÂN ĐANG BẢO TRÌ!\n\n"
                            + "Sân " + selectedSan.getTenSan() + " đang trong thời gian bảo trì cơ sở vật chất.\n"
                            + "Vui lòng chọn ngày khác hoặc chọn sân khác!",
                    "Cảnh báo bảo trì", JOptionPane.WARNING_MESSAGE);
            return;
        }

        confirmed = true;
        dispose();
    }

    /**
     * Trả về kết quả người dùng đã bấm Kiểm tra thành công hay chưa.
     *
     * @return {@code true} nếu thành công, {@code false} nếu chưa
     */
    public boolean isConfirmed() { return confirmed; }

    /**
     * Lấy sân bóng được chọn.
     *
     * @return Đối tượng {@link KhuVucSan}
     */
    public KhuVucSan getSelectedSan() { return selectedSan; }

    /**
     * Lấy ngày kiểm tra.
     *
     * @return Chuỗi ngày (YYYY-MM-DD)
     */
    public String getNgay() { return ngay; }

    /**
     * Lấy giờ bắt đầu.
     *
     * @return Chuỗi giờ (HH:mm)
     */
    public String getGioBatDau() { return gioBatDau; }

    /**
     * Lấy giờ kết thúc.
     *
     * @return Chuỗi giờ (HH:mm)
     */
    public String getGioKetThuc() { return gioKetThuc; }

    /**
     * Tạo panel chứa ô nhập ngày cùng nút icon mở bộ chọn lịch.
     *
     * @param txtField Ô JTextField nhập ngày
     * @param parent   Cửa sổ cha (JFrame)
     * @return Panel chứa giao diện bộ chọn ngày
     */
    private JPanel createDatePickerPanel(JTextField txtField, JFrame parent) {
        JPanel pnl = new JPanel(new BorderLayout(4, 0));
        pnl.setOpaque(false);
        txtField.setPreferredSize(new Dimension(190, 36));
        txtField.setFont(UIConstants.FONT_NORMAL);

        JButton btnPicker = new JButton();
        btnPicker.setIcon(Utils.IconUtils.getCalendarIcon(16));
        btnPicker.setPreferredSize(new Dimension(44, 36));
        btnPicker.addActionListener(e -> openDatePickerFor(txtField, parent));

        txtField.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                openDatePickerFor(txtField, parent);
            }
        });

        pnl.add(txtField, BorderLayout.CENTER);
        pnl.add(btnPicker, BorderLayout.EAST);
        return pnl;
    }

    /**
     * Mở hộp thoại chọn ngày dạng lịch cho ô text được chỉ định.
     *
     * @param txtField Ô JTextField cần cập nhật ngày
     * @param parent   Cửa sổ cha (JFrame)
     */
    private void openDatePickerFor(JTextField txtField, JFrame parent) {
        java.time.LocalDate initDate = java.time.LocalDate.now();
        try {
            if (!txtField.getText().isBlank()) {
                initDate = java.time.LocalDate.parse(txtField.getText().trim());
            }
        } catch (Exception ignored) {}

        ChonNgayDialog dialog = new ChonNgayDialog(parent, initDate);
        dialog.setVisible(true);
        if (dialog.isConfirmed() && dialog.getSelectedDate() != null) {
            txtField.setText(dialog.getSelectedDate().toString());
        }
    }
}
