package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class LogViewer {

    final List<Packet> packets = new ArrayList<>();
    final PacketTable packetTable = new PacketTable(packets);
    final JTable table = new JTable(packetTable);
    final JScrollPane scrollPane = new JScrollPane(table);

    private LogViewer(Snap snap) {
        snap.on(packet -> {
            EventQueue.invokeLater(() -> add(packet));
            return true;
        });
    }
    public static final UIFrame.ComponentFactory factory = new UIFrame.ComponentFactory() {
        @Override
        public JComponent newComponent(Flags flags, Snap snap) {
            return new LogViewer(snap).scrollPane;
        }
    };

    public static void main(String... args) {
        UIFrame.main(factory,args);
    }

    private void add(Packet packet) {
        packets.add(packet);
        table.tableChanged(null);
    }

}
