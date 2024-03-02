package com.webank.wecrosssdk.rpc.methods.response;

import com.webank.wecrosssdk.rpc.common.Block;
import com.webank.wecrosssdk.rpc.methods.Response;

public class BlockResponse extends Response<Block> {

    public BlockResponse() {
        super();
    }

    public Block getBlock() {
        return getData();
    }

    public void setBlock(Block block) {
        setData(block);
    }
}
