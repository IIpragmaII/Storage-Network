package com.lothrazar.storagenetwork.util;

import java.util.List;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StackProviderBatch extends Batch<StackProvider> {
    public ItemStack extractOne(Item item) {
        List<StackProvider> availableStacks = get(item);
        for (StackProvider stackProvider : availableStacks) {
            ItemStack extractedStack = stackProvider.extractOne();
            // TODO: We should remove the StackProvider if it returns an empty stack.
            if (!extractedStack.isEmpty()) {
                return extractedStack;
            }
        }
        return ItemStack.EMPTY;
    }
}
