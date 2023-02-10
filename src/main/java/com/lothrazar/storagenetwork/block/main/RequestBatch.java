package com.lothrazar.storagenetwork.block.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import com.lothrazar.storagenetwork.api.IConnectableLink;
import com.lothrazar.storagenetwork.api.IItemStackMatcher;

public class RequestBatch {
    private Map<IItemStackMatcher, List<BiFunction<IConnectableLink, Integer, Boolean>>> batch = new HashMap<IItemStackMatcher, List<BiFunction<IConnectableLink, Integer, Boolean>>>();

    public void add(IItemStackMatcher matcher, BiFunction<IConnectableLink, Integer, Boolean> extractor) {
        if (!batch.containsKey(matcher)) {
            batch.put(matcher, new ArrayList<BiFunction<IConnectableLink, Integer, Boolean>>());
        }
        batch.get(matcher).add(extractor);
    }

    public Set<IItemStackMatcher> getMatchers() {
        return batch.keySet();
    }

    public boolean extractStacks(IConnectableLink providerStorage, Integer slot, IItemStackMatcher matcher) {
        List<BiFunction<IConnectableLink, Integer, Boolean>> extractors = batch.get(matcher);
        List<BiFunction<IConnectableLink, Integer, Boolean>> remainingExtractors = new ArrayList<BiFunction<IConnectableLink, Integer, Boolean>>();
        for (BiFunction<IConnectableLink, Integer, Boolean> extractor : extractors) {
            if (!extractor.apply(providerStorage, slot)) {
                remainingExtractors.add(extractor);
            }
        }
        batch.put(matcher, remainingExtractors);
        return true;
    }

}
