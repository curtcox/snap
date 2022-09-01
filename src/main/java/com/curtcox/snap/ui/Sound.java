package com.curtcox.snap.ui;

import javax.sound.sampled.*;

final class Sound {

    final SourceDataLine line;
    static final AudioFormat af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, true);

    Sound(SourceDataLine line) {
        this.line = line;
    }

    public static Sound of() throws LineUnavailableException {
        return new Sound(open());
    }

    void play(Note note, int ms) {
        ms = Math.min(ms, Note.SECONDS * 1000);
        int length = Note.SAMPLE_RATE * ms / 1000;
        line.write(note.data(), 0, length);
    }

    static SourceDataLine open() throws LineUnavailableException {
        SourceDataLine line = AudioSystem.getSourceDataLine(af);
        line.open(af, Note.SAMPLE_RATE);
        line.start();
        return line;
    }

    void playNotes() {
        for  (Note n : Note.values()) {
            play(n, 500);
        }
    }

    void close() {
        line.drain();
        line.close();
    }

}
