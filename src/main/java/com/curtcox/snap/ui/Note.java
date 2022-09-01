package com.curtcox.snap.ui;

enum Note {

    A4, A4$, B4, C4, C4$, D4, D4$, E4, F4, F4$, G4, G4$, A5;
    static final int SAMPLE_RATE = 16 * 1024; // ~16KHz
    static final int SECONDS = 2;
    private final byte[] sin = new byte[SECONDS * SAMPLE_RATE];

    Note() {
        int n = this.ordinal();
        double exp = ((double) n) / 12d;
        double f = 440d * Math.pow(2d, exp);
        for (int i = 0; i < sin.length; i++) {
            double period = (double)SAMPLE_RATE / f;
            double angle = 2.0 * Math.PI * i / period;
            sin[i] = (byte)(Math.sin(angle) * 127f);
        }
    }

    public byte[] data() {
        return sin;
    }
}
