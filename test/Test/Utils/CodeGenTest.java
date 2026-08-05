package Test.Utils;

import Utils.CodeGen;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeGenTest {

    @Test
    public void testNextCodeGenerationEmptyList() {
        List<String> existing = Arrays.asList();
        String nextCode = CodeGen.next("SAN", existing, 3);
        assertEquals("SAN001", nextCode);
    }

    @Test
    public void testNextCodeGenerationIncrement() {
        List<String> existing = Arrays.asList("SAN001", "SAN002", "SAN005");
        String nextCode = CodeGen.next("SAN", existing, 3);
        assertEquals("SAN006", nextCode);
    }

    @Test
    public void testNextCodeGenerationDifferentWidth() {
        List<String> existing = Arrays.asList("TK01", "TK02");
        String nextCode = CodeGen.next("TK", existing, 2);
        assertEquals("TK03", nextCode);
    }

    @Test
    public void testRemoveDiacritics() {
        String input = "Tất cả thời gian - Sân cỏ nhân tạo";
        String expected = "Tat ca thoi gian - San co nhan tao";
        assertEquals(expected, CodeGen.removeDiacritics(input));
    }

    @Test
    public void testRemoveDiacriticsWithD() {
        String input = "Huấn luyện viên Độc Lập - bóng đá";
        String expected = "Huan luyen vien Doc Lap - bong da";
        assertEquals(expected, CodeGen.removeDiacritics(input));
    }
}
