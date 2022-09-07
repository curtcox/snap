package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;

import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;

final class PacketTable implements TableModel {
    final List<Packet> packets;

    PacketTable(List<Packet> packets) {
        this.packets = packets;
    }

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
