package com.curtcox.snap.ui;

import com.curtcox.snap.model.*;
import com.curtcox.snap.model.Packet.*;

import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;

final class PacketTable implements TableModel {
    final List<Receipt> packets;

    PacketTable(List<Receipt> packets) {
        this.packets = packets;
    }

    @Override
    public int getRowCount() {
        return packets.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex==0) return "Topic";
        if (columnIndex==1) return "Message";
        if (columnIndex==2) return "Sender";
        if (columnIndex==3) return "Timestamp";
        if (columnIndex==4) return "Trigger";
        if (columnIndex==5) return "Received";
        if (columnIndex==6) return "Delta";
        throw new IllegalArgumentException("" + columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex>=0 && columnIndex<7) return String.class;
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

    private Object value(Receipt receipt, int columnIndex) {
        Packet packet = receipt.packet;
        if (columnIndex==0) return packet.topic;
        if (columnIndex==1) return packet.message;
        if (columnIndex==2) return packet.sender;
        if (columnIndex==3) return packet.timestamp;
        if (columnIndex==4) return packet.trigger;
        if (columnIndex==5) return receipt.received;
        if (columnIndex==6) return delta(receipt);
        throw new IllegalArgumentException("" + columnIndex);
    }

    private String delta(Receipt receipt) {
        Timestamp at = receipt.received;
        Packet trigger = trigger(receipt.packet);
        return (at!=null && trigger!=null) ? (at.value - trigger.timestamp.value) + " ms" : "N/A";
    }

    private Packet trigger(Packet packet) {
        for (Receipt t : packets) {
            if (packet.trigger.refersTo(t.packet)) {
                return t.packet;
            }
        }
        return null;
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
