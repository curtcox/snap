package com.curtcox.snap.model;

import com.curtcox.snap.model.Packet.*;

import java.util.*;

public final class FakeNetwork implements Network {

    final List<IO> ios = new ArrayList<>();

    @Override
    public void add(IO io) {
        ios.add(io);
    }
}
