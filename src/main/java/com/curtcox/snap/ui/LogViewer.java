package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;

import javax.swing.*;
import java.awt.*;

public final class LogViewer {

    final PacketReceiptList packets = new PacketReceiptList();
    final PacketTable packetTable = new PacketTable(packets);
    final JTable table = new JTable(packetTable);
    final JScrollPane scrollPane = new JScrollPane(table);

    private LogViewer(Snap snap) {
        snap.on(packets);
        snap.on(packet -> {
            EventQueue.invokeLater(() -> table.tableChanged(null));
            return true;
        });
    }
    public static final UIFrame.ComponentFactory factory = (flags, snap) -> new LogViewer(snap).scrollPane;

    public static void main(String... args) {
        UIFrame.main(factory,args);
    }

}
