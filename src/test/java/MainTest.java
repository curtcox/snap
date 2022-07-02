import com.curtcox.snap.Main;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void main_version_returns_without_exception() {
        Main.main(new String[] {"-version"});
    }

    @Test
    public void version_returns_version_info() {
        String actual = Main.exec("-version");
        assertEquals("Snap version 0.0.7",actual);
    }

}
