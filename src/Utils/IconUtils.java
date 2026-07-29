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
}
