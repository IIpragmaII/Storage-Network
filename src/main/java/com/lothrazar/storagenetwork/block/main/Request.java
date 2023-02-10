package com.lothrazar.storagenetwork.block.main;

import com.lothrazar.storagenetwork.api.IConnectableItemAutoIO;

import net.minecraft.world.item.ItemStack;

public class Request {
    private Integer count;
    private IConnectableItemAutoIO storage;

    public Request(IConnectableItemAutoIO storage) {
        this.count = storage.getTransferRate();
        this.storage = storage;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public Boolean insertStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        ItemStack insertedStack = storage.insertStack(stack, false);
        // Determine the amount of items moved in the stack
        int movedItems = stack.getCount() - insertedStack.getCount();
        if (movedItems <= 0) {
            return false;
        }
        stack.setCount(movedItems);
        if (stack.isEmpty()) {
            return false;
        }
        return true;
    }
}
