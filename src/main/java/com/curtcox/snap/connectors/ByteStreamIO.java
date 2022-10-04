package com.curtcox.snap.connectors;

import com.curtcox.snap.model.*;

import java.io.*;
import java.util.*;

/**
 * For constructing a StreamIO backed by byte array streams.
 */
public final class ByteStreamIO {

    private final ByteArrayInputStream withPacketsToBeReadFromThisStream;
    private final ByteArrayOutputStream withPacketsThatWereWrittenToThisStream = new ByteArrayOutputStream();

    private ByteStreamIO(ByteArrayInputStream toBeReadFromThisStream) {
        this.withPacketsToBeReadFromThisStream = toBeReadFromThisStream;
    }

    static ByteStreamIO with(Packet... packets) {
        return new ByteStreamIO(new ByteArrayInputStream(Bytes.from(packets).value()));
    }

    StreamIO asStreamIO() {
        return new StreamIO(withPacketsToBeReadFromThisStream, withPacketsThatWereWrittenToThisStream);
    }

    List<Packet> getWrittenTo() throws IOException {
        return InputStreamPacketReader.readWaiting(
                new ByteArrayInputStream(withPacketsThatWereWrittenToThisStream.toByteArray()));
    }
}
