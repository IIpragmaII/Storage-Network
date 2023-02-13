package com.lothrazar.storagenetwork.block.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lothrazar.storagenetwork.api.IConnectableLink;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RequestBatch extends HashMap<Item, List<Request>> {
    public List<Request> put(Item item, Request request) {
        if (containsKey(item)) {
            List<Request> matchingList = super.get(item);
            matchingList.add(request);
            return matchingList;
        }
        List<Request> newList = new ArrayList<Request>();
        newList.add(request);
        return super.put(item, newList);
    }

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
