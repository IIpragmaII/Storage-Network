package com.lothrazar.storagenetwork.block.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lothrazar.storagenetwork.api.IConnectableLink;
import com.lothrazar.storagenetwork.api.IItemStackMatcher;

import net.minecraft.world.item.ItemStack;

public class RequestBatch extends HashMap<IItemStackMatcher, List<Request>> {
    public IItemStackMatcher getMatchingKey(IItemStackMatcher matcher) {
        for (IItemStackMatcher mapKey : keySet()) {
            if (mapKey.match(matcher)) {
                return mapKey;
            }
        }
        return null;
    }

    public List<Request> put(IItemStackMatcher matcher, List<Request> requests) {
        IItemStackMatcher matchingKey = getMatchingKey(matcher);
        if (matchingKey != null) {
            return super.put(matchingKey, requests);
        }
        return super.put(matcher, requests);
    }

    public List<Request> put(IItemStackMatcher matcher, Request request) {
        IItemStackMatcher matchingKey = getMatchingKey(matcher);
        if (matchingKey != null) {
            List<Request> matchingList = super.get(matchingKey);
            matchingList.add(request);
            return matchingList;
        }
        List<Request> newList = new ArrayList<Request>();
        newList.add(request);
        return super.put(matcher, newList);
    }

    public List<Request> get(IItemStackMatcher matcher) {
        IItemStackMatcher matchingKey = getMatchingKey(matcher);
        if (matchingKey != null) {
            return super.get(matchingKey);
        }
        return null;
    }

    public void extractStacks(IConnectableLink providerStorage, Integer slot, IItemStackMatcher matcher) {
        List<Request> requests = get(matcher);
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
        put(matcher, remainingRequests);
    }
}
