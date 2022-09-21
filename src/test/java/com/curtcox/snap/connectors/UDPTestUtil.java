package com.curtcox.snap.connectors;

import com.curtcox.snap.model.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.curtcox.snap.model.TestClock.tick;

public class UDPTestUtil {

    public static List<Packet> flush(Packet.Reader reader) throws IOException {
        return flush(reader,5);
    }
    public static List<Packet> flush(Packet.Reader reader,int straightNullsNeeded) throws IOException {
        List<Packet> list = new ArrayList<>();
        int straightNulls = 0;
        while (straightNulls<straightNullsNeeded) {
            Packet packet = reader.read(Packet.ANY);
            if (packet==null) {
                straightNulls++;
            } else {
                straightNulls = 0;
                list.add(packet);
            }
            tick();
        }
        return list;
    }

}
