package com.curtcox.snap.model;

import java.util.*;

/**
 * A packet sink that exposes the packets consumed as receipts.
 */
public final class PacketReceiptList implements Packet.Sink, List<Packet.Receipt> {

    private final List<Packet.Receipt> receipts = new ArrayList<>();

    @Override public boolean add(Packet packet) {
        receipts.add(new Packet.Receipt(packet));
        return true;
    }

    @Override public int size() { return receipts.size(); }
    @Override public boolean isEmpty() { return receipts.isEmpty(); }
    @Override public Iterator<Packet.Receipt> iterator() { return receipts.iterator(); }
    @Override public Packet.Receipt get(int index) { return receipts.get(index); }

    @Override public String toString() { return receipts.toString(); }

    public PacketReceiptList filter(Packet.Filter filter) {
        PacketReceiptList filtered = new PacketReceiptList();
        for (Packet.Receipt receipt : receipts) {
            if (filter.passes(receipt.packet)) {
                filtered.receipts.add(receipt);
            }
        }
        return filtered;
    }

    // Things we don't do, but could if needed.
    @Override public boolean contains(Object o) { throw no(); }
    @Override public Object[] toArray() { throw no(); }
    @Override public <T> T[] toArray(T[] a) { throw no(); }
    @Override public boolean add(Packet.Receipt receipt) { throw no(); }
    @Override public boolean remove(Object o) { throw no(); }
    @Override public boolean containsAll(Collection<?> c) { throw no(); }
    @Override public boolean addAll(Collection<? extends Packet.Receipt> c) { throw no(); }
    @Override public boolean addAll(int index, Collection<? extends Packet.Receipt> c) { throw no(); }
    @Override public boolean removeAll(Collection<?> c) { throw no(); }
    @Override public boolean retainAll(Collection<?> c) { throw no(); }
    @Override public void clear() { throw no(); }
    @Override public Packet.Receipt set(int index, Packet.Receipt element) { throw no(); }
    @Override public void add(int index, Packet.Receipt element) { throw no(); }
    @Override public Packet.Receipt remove(int index) { throw no(); }
    @Override public int indexOf(Object o) { throw no(); }
    @Override public int lastIndexOf(Object o) { throw no(); }
    @Override public ListIterator<Packet.Receipt> listIterator() { throw no(); }
    @Override public ListIterator<Packet.Receipt> listIterator(int index) { throw no(); }
    @Override public List<Packet.Receipt> subList(int fromIndex, int toIndex) { throw no(); }

    private static UnsupportedOperationException no() {
        System.out.println("???");
        return new UnsupportedOperationException("Implement if needed.");
    }
}
