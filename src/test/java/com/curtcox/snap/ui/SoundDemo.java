package com.curtcox.snap.ui;

final class SoundDemo {

    public static void main(String[] args) {
        System.out.println("starting");
        Sound sound = new Sound();
        for (int i=0; i<100; i++) {
            System.out.println(""+i);
            sound.ping(i);

        }
        System.out.println("closed");
    }

}