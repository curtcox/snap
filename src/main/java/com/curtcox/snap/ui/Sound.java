package com.curtcox.snap.ui;

import javax.sound.sampled.*;

final class Sound {

    static final AudioFormat af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, true);

    void play(Note note) {
        try (SourceDataLine line = open()) {
            playOn(note,line);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private void playOn(Note note,SourceDataLine line) {
        byte[] data = note.data();
        line.write(data, 0, data.length);
        line.drain();
    }

    private static SourceDataLine open() throws LineUnavailableException {
        SourceDataLine line = AudioSystem.getSourceDataLine(af);
        line.open(af, Note.SAMPLE_RATE);
        line.start();
        return line;
    }

    void ping() {
        play(new Note(0,30000));
    }

}
