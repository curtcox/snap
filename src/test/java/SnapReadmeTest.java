import com.curtcox.snap.model.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

// Direct tests for things in the Readme.
public class SnapReadmeTest {

    final Packet.Network network = Snap.newNetwork(Packet.Network.Type.memory);

    final Snap snap1 = Snap.on(network);
    final Snap snap2 = Snap.on(network);

//    snap [ send | listen | listen-ping |listen-all | ping | whoami | interactive | awt | swing ]

    @Test
    public void whoami_returns_the_identity_with_machine_and_user_name() {
        String identity = snap1.whoami();
        assertTrue(identity.contains(snap1.user()));
        assertTrue(identity.contains(snap1.host()));
        assertEquals(snap1.user() + "@" + snap1.host(),identity);
    }

    @Test
    public void whoami_returns_the_name_given() {
        String expected = Random.random("name");
        snap1.setName(expected);
        String actual = snap1.whoami();
        assertEquals(expected,actual);
    }

    @Test
    public void ping_returns_whoami_names() throws IOException {
        snap1.ping();
        Packet.Reader reader = snap1.listen();

        reader.read();
    }
}
