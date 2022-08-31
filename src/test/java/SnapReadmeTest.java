import com.curtcox.snap.model.*;
import org.junit.Test;

import java.io.IOException;
import static com.curtcox.snap.model.Packet.*;

import static com.curtcox.snap.model.Clock.tick;
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
        Topic topic = Random.topic();
        SinkReader reader = new SinkReader();
        snap1.ping(topic,reader);
        tick(3);

        Packet packet = reader.read(ANY);

        assertEquals(snap2.whoami(),packet.sender);
    }

    @Test
    public void listening_after_ping_returns_whoami_names() throws IOException {
        Topic topic = Random.topic();
        snap1.ping(topic,new SinkReader());
        tick(3);

        Packet packet = snap1.reader().read(ANY);

        assertEquals(snap2.whoami(),packet.sender);
    }

    @Test
    public void listening_after_ping_returns_response_time() throws IOException {
        Topic topic = Random.topic();

        snap1.ping(topic,new SinkReader());
        tick(3);

        Packet packet = snap1.reader().read(ANY);

        String senderName = snap2.whoami();
        assertEquals(senderName,packet.sender.toString());
        assertContains(packet.message,senderName);
    }

    @Test
    public void light_is_turned_on_when_motion_is_detected() {
        fail();
    }

    @Test
    public void light_turns_off_5_seconds_after_last_motion() {
        fail();
    }

    @Test
    public void ping_lists_all_devices_listening_for_events() {
        fail();
    }

    @Test
    public void ping_lists_all_devices_generating_events() {
        fail();
    }

    @Test
    public void ping_with_topic_only_lists_devices_listening_to_that_topic() {
        fail();
    }

    @Test
    public void ping_with_topic_only_lists_devices_broadcasting_to_that_topic() {
        fail();
    }

    private void assertContains(String text, String substring) {
        assertTrue(text + " does not contain " + substring ,text.contains(substring));
    }

}
