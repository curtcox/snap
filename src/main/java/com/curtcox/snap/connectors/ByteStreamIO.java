package com.curtcox.snap.connectors;

import com.curtcox.snap.model.*;

import java.io.*;
import java.util.*;

/**
 * For constructing a StreamIO backed by byte array streams.
 */
public final class ByteStreamIO {

    private final ByteArrayInputStream withPacketsToBeReadFromThisStream;
    private final ByteArrayOutputStream withPacketsThatWereWrittenToThisStream;

    private ByteStreamIO(ByteArrayInputStream toBeReadFromThisStream, ByteArrayOutputStream toBeWrittenHere) {
        this.withPacketsToBeReadFromThisStream = toBeReadFromThisStream;
        this.withPacketsThatWereWrittenToThisStream = toBeWrittenHere;
    }

    static ByteStreamIO with(Packet... packets) {
        return new ByteStreamIO(new ByteArrayInputStream(Bytes.from(packets).value()),new ByteArrayOutputStream());
    }

    static ByteStreamIO debug(Packet... packets) {
        return new ByteStreamIO(new ByteArrayInputStream(Bytes.from(packets).value()),new DebugByteArrayOutputStream());
    }

    StreamIO asStreamIO() {
        return new StreamIO(withPacketsToBeReadFromThisStream, withPacketsThatWereWrittenToThisStream);
    }

    List<Packet> getWrittenTo() throws IOException {
        return InputStreamPacketReader.readWaiting(
                new ByteArrayInputStream(withPacketsThatWereWrittenToThisStream.toByteArray()));
    }

    @Override
    public String toString() {
        return  "Remaining unread : " + withPacketsToBeReadFromThisStream +
                " Previously written :" + withPacketsThatWereWrittenToThisStream;
    }
}
