package com.curtcox.snap.ui;

import com.curtcox.snap.model.Packet.*;
import com.curtcox.snap.model.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public final class LogViewer {

    final JFrame frame;
    final JTable table;
    final Topic topic;
    final Snap snap;

    final List<Packet> packets = new ArrayList<>();

    class PacketTable implements TableModel {

        @Override
        public int getRowCount() {
            return packets.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex==0) return "Topic";
            if (columnIndex==1) return "Message";
            if (columnIndex==2) return "Sender";
            if (columnIndex==3) return "Timestamp";
            if (columnIndex==4) return "Trigger";
            throw new IllegalArgumentException("" + columnIndex);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex==0) return String.class;
            if (columnIndex==1) return String.class;
            if (columnIndex==2) return String.class;
            if (columnIndex==3) return Long.class;
            if (columnIndex==4) return Long.class;
            throw new IllegalArgumentException("" + columnIndex);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Packet packet = packets.get(rowIndex);
            if (columnIndex==0) return packet.topic.toString();
            if (columnIndex==1) return packet.message;
            if (columnIndex==2) return packet.sender.toString();
            if (columnIndex==3) return packet.timestamp;
            if (columnIndex==4) return packet.trigger.toLong();
            throw new IllegalArgumentException("" + columnIndex);
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }

    LogViewer(Topic topic, Snap snap) {
        this.topic = topic;
        frame = new JFrame(topic.toString());
        table = new JTable();
        this.snap = snap;
    }

    public static void main(String... args) {
        EventQueue.invokeLater(() -> main0(args));
    }

    static void main0(String[] args) {
        Flags flags = Flags.from(args);
        Topic topic = flags.topic();
        Snap snap = Snap.newInstance();
        LogViewer button = new LogViewer(topic,snap);
        button.init();
        button.show();
    }

    void init() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JScrollPane(table));
        frame.pack();
        frame.doLayout();
        snap.on(topic, packet -> add(packet));
    }

    private void add(Packet packet) {
        packets.add(packet);
        table.invalidate();
    }

    void show() {
        frame.setVisible(true);
    }
}
