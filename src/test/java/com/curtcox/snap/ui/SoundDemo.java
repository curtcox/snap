package com.curtcox.snap.ui;

import javax.sound.sampled.*;

final class SoundDemo {

    public static void main(String[] args) throws LineUnavailableException {
        Sound sound = Sound.of();
        sound.playNotes();
        sound.close();
    }

}