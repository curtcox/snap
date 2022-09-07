package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public final class LogViewer implements UIFrame.ComponentFactory {

    final List<Packet> packets = new ArrayList<>();
    final PacketTable packetTable = new PacketTable(packets);
    final JTable table = new JTable(packetTable);

    final JScrollPane scrollPane = new JScrollPane(table);

    public static void main(String... args) {
        UIFrame.main(new LogViewer(),args);
    }

    private boolean add(Packet packet) {
        System.out.println("LogViewer added " + packet);
        boolean added = packets.add(packet);
        table.tableChanged(null);
        return added;
    }

    @Override
    public JScrollPane newComponent(Flags flags, Snap snap) {
        snap.on(packet -> { return add(packet); });
        return scrollPane;
    }
}
