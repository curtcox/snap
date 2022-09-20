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
        if (columnIndex>=0 && columnIndex<5) return String.class;
        throw new IllegalArgumentException("" + columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return value(packets.get(rowIndex),columnIndex).toString();
    }

    private Object value(Packet packet, int columnIndex) {
        if (columnIndex==0) return packet.topic;
        if (columnIndex==1) return packet.message;
        if (columnIndex==2) return packet.sender;
        if (columnIndex==3) return packet.timestamp;
        if (columnIndex==4) return packet.trigger;
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
