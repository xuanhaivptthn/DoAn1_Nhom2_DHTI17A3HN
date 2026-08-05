package Test.UI;

import GiaoDien.Panels.LoginPanel;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.timing.Condition;
import org.assertj.swing.timing.Pause;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JFrame;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.parallel.ResourceLock;

/**
 * UI Functional Test for LoginPanel using AssertJ Swing and JUnit 5.
 */
@ResourceLock("SessionManager")
public class LoginPanelTest {
    private FrameFixture window;
    private JFrame frame;

    @BeforeEach
    public void setUp() {
        // Set up the system to not use database for in-memory testing
        Utils.DataStore.setUseDatabase(false);

        frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame("Test Login");
            f.setContentPane(new LoginPanel(v -> {
                // login success callback
            }));
            f.pack();
            return f;
        });
        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
        
        // Select "Dữ liệu mẫu (DataStore / In-Memory)" to run test fully in-memory
        window.comboBox("cboDataSource").selectItem(1);
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }

    @Test
    public void testEmptyLoginShowsError() {
        window.textBox("txtUser").setText("");
        window.textBox("txtPass").setText("");
        window.button("btnLogin").click();
        
        Pause.pause(new Condition("Wait for empty username error") {
            @Override
            public boolean test() {
                String text = window.label("lblError").text();
                return text != null && !text.isBlank();
            }
        }, 2000);
        
        String errorText = window.label("lblError").text();
        assertThat(errorText).contains("Vui lòng nhập tên đăng nhập.");
    }

    @Test
    public void testEmptyPasswordShowsError() {
        window.textBox("txtUser").setText("admin");
        window.textBox("txtPass").setText("");
        window.button("btnLogin").click();
        
        Pause.pause(new Condition("Wait for empty password error") {
            @Override
            public boolean test() {
                String text = window.label("lblError").text();
                return text != null && !text.isBlank();
            }
        }, 2000);
        
        String errorText = window.label("lblError").text();
        assertThat(errorText).contains("Vui lòng nhập mật khẩu.");
    }

    @Test
    public void testInvalidLoginShowsError() {
        window.textBox("txtUser").setText("invalid_user");
        window.textBox("txtPass").setText("wrong_password");
        window.button("btnLogin").click();
        
        Pause.pause(new Condition("Wait for invalid login error") {
            @Override
            public boolean test() {
                String text = window.label("lblError").text();
                return text != null && !text.isBlank();
            }
        }, 2000);
        
        String errorText = window.label("lblError").text();
        assertThat(errorText).contains("Tên đăng nhập hoặc mật khẩu không đúng.");
    }
}
