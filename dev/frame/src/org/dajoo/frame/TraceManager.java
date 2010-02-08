package org.dajoo.frame;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TraceManager {

    protected static final int MAXTRACENUM = 10;
    protected Queue<Trace> traceQueue = new ConcurrentLinkedQueue<Trace>();

    public void addTrace(Trace t) {
        if(!traceQueue.contains(t)) traceQueue.add(t);
        if(traceQueue.size()>MAXTRACENUM) traceQueue.remove();
    }

    public Trace[] getTraces() {
        Trace[] traces = new Trace[traceQueue.size()];
        traces = traceQueue.toArray(traces);
        return traces;
    }
}
