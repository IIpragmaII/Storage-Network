package com.lothrazar.storagenetwork.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StackProviderBatch extends Batch<StackProvider> {
    public ItemStack extractOne(Item item) {
        List<StackProvider> availableStacks = get(item);
        List<StackProvider> availableWithoutEmpty = new ArrayList<StackProvider>(availableStacks);
        ItemStack extractedStack = ItemStack.EMPTY;
        for (StackProvider stackProvider : availableStacks) {
            extractedStack = stackProvider.extractOne();
            if (!extractedStack.isEmpty()) {
                break;
            }
            availableWithoutEmpty.remove(stackProvider);
        }
        put(item, availableWithoutEmpty);
        return extractedStack;
    }
}
