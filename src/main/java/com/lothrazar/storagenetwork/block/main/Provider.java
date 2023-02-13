package com.lothrazar.storagenetwork.block.main;

import com.lothrazar.storagenetwork.api.IConnectableLink;

public class Provider {
    IConnectableLink storage;
    int slot;

    public Provider(IConnectableLink storage, int slot) {
        this.storage = storage;
        this.slot = slot;
    }
}
