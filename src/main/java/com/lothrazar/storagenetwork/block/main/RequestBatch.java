package com.lothrazar.storagenetwork.block.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lothrazar.storagenetwork.api.IConnectable;
import com.lothrazar.storagenetwork.api.IConnectableLink;
import com.lothrazar.storagenetwork.api.IItemStackMatcher;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class RequestBatch {
    private Map<CompoundTag, List<IConnectable>> batch = new HashMap<CompoundTag, List<IConnectable>>();

    public void add(CompoundTag tag, IConnectable connectable) {
        if (!batch.containsKey(tag)) {
            batch.put(tag, new ArrayList<IConnectable>());
        }
        batch.get(tag).add(connectable);
    }

    public Set<CompoundTag> getMatchers() {
        return batch.keySet();
    }

    public void extractStacks(IConnectableLink providerStorage, Integer slot, CompoundTag tag) {
        List<IConnectable> connectables = batch.get(tag);
        List<IConnectable> remainingConnectables = new ArrayList<IConnectable>();
        for (IConnectable connectable : connectables) {
            ItemStack stack = providerStorage.extractFromSlot(slot, connectable.getCount());
            if (!connectable.insertStack(stack)) {
                remainingConnectables.add(connectable);
            }
            if (stack.isEmpty()) {
                return;
            }
        }
        batch.put(tag, remainingConnectables);
    }

}
