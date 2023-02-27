package com.lothrazar.storagenetwork.util;

import com.lothrazar.storagenetwork.api.IConnectableLink;

import net.minecraft.world.item.ItemStack;

public class StackProvider {
    IConnectableLink storage;
    int slot;

    public StackProvider(IConnectableLink storage, int slot) {
        this.storage = storage;
        this.slot = slot;
    }

    public IConnectableLink getStorage() {
        return storage;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack extractOne() {
        return storage.extractFromSlot(slot, 1, false);
    }
}
