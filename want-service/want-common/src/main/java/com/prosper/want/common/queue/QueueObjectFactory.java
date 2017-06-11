package com.prosper.want.common.queue;

import com.lmax.disruptor.EventFactory;

public class QueueObjectFactory implements EventFactory<QueueObject> {

    public QueueObject newInstance() {
        return new QueueObject();
    }
}
