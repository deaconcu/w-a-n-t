package com.prosper.want.common.wrapper;

import io.netty.buffer.ByteBuf;

public interface UDPService {
    
    /**
     * 处理UDP数据包
     */
    public int executeData(ByteBuf in);

}
