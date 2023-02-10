package com.lothrazar.storagenetwork.block.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lothrazar.storagenetwork.api.IConnectableLink;
import com.lothrazar.storagenetwork.api.IItemStackMatcher;

import net.minecraft.world.item.ItemStack;

public class RequestBatch {
    private Map<IItemStackMatcher, List<Request>> batch = new HashMap<IItemStackMatcher, List<Request>>();

    public void add(IItemStackMatcher matcher, Request request) {
        if (!batch.containsKey(matcher)) {
            batch.put(matcher, new ArrayList<Request>());
        }
        batch.get(matcher).add(request);
    }

    public Set<IItemStackMatcher> getMatchers() {
        return batch.keySet();
    }

    public void extractStacks(IConnectableLink providerStorage, Integer slot, IItemStackMatcher matcher) {
        List<Request> requests = batch.get(matcher);
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
        batch.put(matcher, remainingRequests);
    }

}
