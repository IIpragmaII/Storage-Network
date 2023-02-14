package com.lothrazar.storagenetwork.block.main;

import java.util.ArrayList;
import java.util.List;

import com.lothrazar.storagenetwork.api.IConnectableLink;
import com.lothrazar.storagenetwork.util.ItemMap;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RequestBatch extends ItemMap<Request> {
    public void extractStacks(IConnectableLink providerStorage, Integer slot, Item item) {
        List<Request> requests = get(item);
        List<Request> remainingRequests = new ArrayList<Request>();
        for (Request request : requests) {
            ItemStack stack = providerStorage.extractFromSlot(slot, request.getCount());
            if (!request.insertStack(stack)) {
                remainingRequests.add(request);
            }
            if (stack.isEmpty()) {
                return;
            }
        }
        put(item, remainingRequests);
    }
}
