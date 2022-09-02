package com.curtcox.snap.ui;

final class Note {

    private final int n;
    private final byte[] samples;

    static final int SAMPLE_RATE = 32 * 1024; // ~16KHz

    Note(int n, int size) {
        this.n = n;
        samples = new byte[size];
        for (int i = 0; i < samples.length; i++) {
            samples[i] = at(i);
        }
    }

    private double wave(int i) {
        return Math.sin(angle(i));
    }

    private double volume(int i) {
        double x = ((double)(samples.length - i)) / (double) samples.length; //    0 .. 1.0
        double v = Math.abs(x - 0.5d) + 0.5d;                                //  0.5 .. 1.0 .. 0.5
        return Math.pow(v, 6);                                               // shape the curve
    }

    private byte at(int i) {
        return (byte)(wave(i) * volume(i) * 127f);
    }

    private double angle(int i) {
        return i * 2.0 * Math.PI / period();
    }

    private double period() {
        double exp = ((double) n) / 12d;
        double f = 440d * Math.pow(2d, exp);
        return (double) SAMPLE_RATE / f;
    }

    public byte[] data() {
        return samples;
    }
}
