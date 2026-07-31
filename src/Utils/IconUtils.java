package Utils;

import javax.swing.ImageIcon;
import java.net.URL;

/**
 * Tiện ích tải icon từ thư mục resources/icons/.
 */
public final class IconUtils {

    private IconUtils() {
    }

    public static ImageIcon getIcon(String name, int size) {
        String path = "/resources/icons/" + name + "_" + size + "x" + size + ".png";
        URL url = IconUtils.class.getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        }
        // Fallback: search available sizes (48, 32, 24, 16) and scale smoothly to requested size
        int[] availableSizes = {48, 32, 24, 16};
        for (int s : availableSizes) {
            URL fallbackUrl = IconUtils.class.getResource("/resources/icons/" + name + "_" + s + "x" + s + ".png");
            if (fallbackUrl != null) {
                ImageIcon original = new ImageIcon(fallbackUrl);
                java.awt.Image scaled = original.getImage().getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        }
        return null;
    }

    public static ImageIcon getAddIcon(int size) {
        return getIcon("add", size);
    }

    public static ImageIcon getEditIcon(int size) {
        return getIcon("edit", size);
    }

    public static ImageIcon getDeleteIcon(int size) {
        return getIcon("delete", size);
    }

    public static ImageIcon getRefreshIcon(int size) {
        return getIcon("refresh", size);
    }

    public static ImageIcon getOpenIcon(int size) {
        return getIcon("open", size);
    }

    public static ImageIcon getBallIcon(int size) {
        return getIcon("ball", size);
    }

    public static ImageIcon getBallBlackIcon(int size) {
        return getIcon("ball", size);
    }

    public static ImageIcon getBallWhiteIcon(int size) {
        return getIcon("ball_white", size);
    }

    public static ImageIcon getCheckIcon(int size) {
        return getIcon("check", size);
    }

    public static ImageIcon getExportIcon(int size) {
        return getIcon("export", size);
    }

    public static ImageIcon getSearchIcon(int size) {
        return getIcon("search", size);
    }

    public static ImageIcon getMaintenanceIcon(int size) {
        return getIcon("maintenance", size);
    }

    public static ImageIcon getStatusIcon(int size) {
        return getIcon("status", size);
    }

    public static ImageIcon getUserIcon(int size) {
        return getIcon("user", size);
    }

    public static ImageIcon getMoneyIcon(int size) {
        return getIcon("money", size);
    }

    public static ImageIcon getCalendarIcon(int size) {
        return getIcon("calendar", size);
    }

    public static ImageIcon getSoccerIcon(int size) {
        return getIcon("ball", size);
    }

    public static ImageIcon getBoxIcon(int size) {
        return getIcon("box", size);
    }

    public static ImageIcon getTagIcon(int size) {
        return getIcon("tag", size);
    }

    public static ImageIcon getDrinkIcon(int size) {
        return getIcon("drink", size);
    }

    public static ImageIcon getReceiptIcon(int size) {
        return getIcon("receipt", size);
    }

    public static ImageIcon getCloseIcon(int size) {
        return getIcon("close", size);
    }

    public static ImageIcon getPinIcon(int size) {
        return getIcon("pin", size);
    }

    public static ImageIcon getWarningIcon(int size) {
        return getIcon("warning", size);
    }

    public static ImageIcon getPlayIcon(int size) {
        return getIcon("play", size);
    }

    public static ImageIcon getArrowRightIcon(int size) {
        return getIcon("arrow_right", size);
    }

    public static ImageIcon getPrevIcon(int size) {
        return getIcon("prev", size);
    }

    public static ImageIcon getNextIcon(int size) {
        return getIcon("next", size);
    }

    public static ImageIcon getFirstIcon(int size) {
        return getIcon("first", size);
    }

    public static ImageIcon getLastIcon(int size) {
        return getIcon("last", size);
    }

    public static ImageIcon getDotIcon(int size) {
        return getIcon("dot", size);
    }
}
