package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.model.*;

import javax.swing.*;
import java.util.List;

public final class LogViewer extends TopicFrame {

    final JTable table;
    final List<Packet> packets;

    LogViewer(Topic topic, Snap snap) {
        super(topic,new JScrollPane(new JTable(new PacketTable())),snap);
        table = (JTable) ((JScrollPane) component).getViewport().getView();
        packets = ((PacketTable) table.getModel()).packets;
    }

    public static void main(String... args) {
        TopicFrame.main((flags, snap) -> new LogViewer(flags.topic(),snap));
    }

    @Override
    void customInit() {
        snap.on(packet -> { return add(packet); });
    }

    private boolean add(Packet packet) {
        System.out.println("Added " + packet);
        boolean added = packets.add(packet);
        table.tableChanged(null);
        return added;
    }

}
