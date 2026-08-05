package Test.UI;

import GiaoDien.MainFrame;
import Utils.DataStore;
import Utils.SessionManager;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.timing.Condition;
import org.assertj.swing.timing.Pause;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import javax.swing.JFrame;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * UI Functional Test for the main application shell (MainFrame) and its sub-panels.
 */
@ResourceLock("SessionManager")
public class MainFrameUiTest {
    private FrameFixture window;
    private MainFrame mainFrame;

    @BeforeEach
    public void setUp() {
        // Run fully in-memory mode
        DataStore.setUseDatabase(false);
        DataStore.get().reseed();

        // Authenticate programmatically as Admin (Chủ Sân)
        SessionManager.get().login("admin", "admin123");

        mainFrame = GuiActionRunner.execute(() -> {
            MainFrame frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            return frame;
        });

        window = new FrameFixture(mainFrame);
        window.show(); // Display frame

        // Wait for frame to be fully showing
        Pause.pause(new Condition("Wait for MainFrame to be showing") {
            @Override
            public boolean test() {
                return mainFrame.isShowing();
            }
        }, 5000);
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
        SessionManager.get().logout();
    }

    @Test
    public void testDashboardLoadsAndShowsTitle() {
        // Wait for label to be showing
        Pause.pause(new Condition("Wait for lblUserInfo to be showing") {
            @Override
            public boolean test() {
                try {
                    return window.label("lblUserInfo").target().isShowing();
                } catch (Exception e) {
                    return false;
                }
            }
        }, 3000);

        String userInfoText = window.label("lblUserInfo").text();
        assertThat(userInfoText).contains("admin");
        assertThat(userInfoText).contains("Chủ sân");
    }

    @Test
    public void testNavigationToDatLich() {
        // Programmatically trigger button action on EDT to avoid mouse coordinates scroll/clip issues
        GuiActionRunner.execute(() -> window.button(JButtonMatcher.withText("Quản lý đặt lịch sân")).target().doClick());
        
        // Wait for tableSchedule to be showing
        Pause.pause(new Condition("Wait for tableSchedule to be showing") {
            @Override
            public boolean test() {
                try {
                    return window.table("tableSchedule").target().isShowing();
                } catch (Exception e) {
                    return false;
                }
            }
        }, 5000);

        assertNotNull(window.table("tableSchedule"));
    }

    @Test
    public void testNavigationToKhuVucSan() {
        // Programmatically trigger button action on EDT
        GuiActionRunner.execute(() -> window.button(JButtonMatcher.withText("Quản lý khu vực sân bóng")).target().doClick());
        
        // Wait for tableKhuVuc to be showing
        Pause.pause(new Condition("Wait for tableKhuVuc to be showing") {
            @Override
            public boolean test() {
                try {
                    return window.table("tableKhuVuc").target().isShowing();
                } catch (Exception e) {
                    return false;
                }
            }
        }, 5000);

        JTableFixture table = window.table("tableKhuVuc");
        assertNotNull(table);
        // Expecting 5 default seeded courts in UI table
        assertThat(table.rowCount()).isEqualTo(5);
    }

    @Test
    public void testNavigationToDichVu() {
        // Programmatically trigger button action on EDT
        GuiActionRunner.execute(() -> window.button(JButtonMatcher.withText("Quản lý dịch vụ")).target().doClick());
        
        // Wait for tableDichVu to be showing
        Pause.pause(new Condition("Wait for tableDichVu to be showing") {
            @Override
            public boolean test() {
                try {
                    return window.table("tableDichVu").target().isShowing();
                } catch (Exception e) {
                    return false;
                }
            }
        }, 5000);

        JTableFixture table = window.table("tableDichVu");
        assertNotNull(table);
        // Expecting 4 default seeded services
        assertThat(table.rowCount()).isEqualTo(4);
    }

    @Test
    public void testNavigationToKho() {
        // Programmatically trigger button action on EDT
        GuiActionRunner.execute(() -> window.button(JButtonMatcher.withText("Quản lý kho hàng & vật tư")).target().doClick());
        
        // Wait for tableKho to be showing
        Pause.pause(new Condition("Wait for tableKho to be showing") {
            @Override
            public boolean test() {
                try {
                    return window.table("tableKho").target().isShowing();
                } catch (Exception e) {
                    return false;
                }
            }
        }, 5000);

        JTableFixture table = window.table("tableKho");
        assertNotNull(table);
        // Expecting 6 default seeded warehouse items
        assertThat(table.rowCount()).isEqualTo(6);
    }

    @Test
    public void testNavigationToTaiKhoan() {
        // Programmatically trigger button action on EDT
        GuiActionRunner.execute(() -> window.button(JButtonMatcher.withText("Quản lý tài khoản hệ thống")).target().doClick());
        
        // Wait for tableTaiKhoan to be showing
        Pause.pause(new Condition("Wait for tableTaiKhoan to be showing") {
            @Override
            public boolean test() {
                try {
                    return window.table("tableTaiKhoan").target().isShowing();
                } catch (Exception e) {
                    return false;
                }
            }
        }, 5000);

        JTableFixture table = window.table("tableTaiKhoan");
        assertNotNull(table);
        // Expecting 4 default seeded accounts
        assertThat(table.rowCount()).isEqualTo(4);
    }
}
