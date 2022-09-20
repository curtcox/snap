package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

public final class LogViewer {

    final List<Receipt> packets = new ArrayList<>();
    final PacketTable packetTable = new PacketTable(packets);
    final JTable table = new JTable(packetTable);
    final JScrollPane scrollPane = new JScrollPane(table);

    private LogViewer(Snap snap) {
        snap.on(packet -> {
            EventQueue.invokeLater(() -> add(new Receipt(packet,Timestamp.now())));
            return true;
        });
    }
    public static final UIFrame.ComponentFactory factory = (flags, snap) -> new LogViewer(snap).scrollPane;

    public static void main(String... args) {
        UIFrame.main(factory,args);
    }

    private void add(Receipt packet) {
        packets.add(packet);
        table.tableChanged(null);
    }

}
