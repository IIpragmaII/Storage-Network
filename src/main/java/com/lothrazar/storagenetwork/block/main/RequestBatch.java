package com.lothrazar.storagenetwork.block.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import com.lothrazar.storagenetwork.api.IConnectableLink;
import com.lothrazar.storagenetwork.api.IItemStackMatcher;

public class RequestBatch {
    private Map<IItemStackMatcher, List<BiConsumer<IConnectableLink, Integer>>> batch = new HashMap<IItemStackMatcher, List<BiConsumer<IConnectableLink, Integer>>>();

    public void add(IItemStackMatcher matcher, BiConsumer<IConnectableLink, Integer> extractor) {
        if (!batch.containsKey(matcher)) {
            batch.put(matcher, new ArrayList<BiConsumer<IConnectableLink, Integer>>());
        }
        batch.get(matcher).add(extractor);
    }

    public Set<IItemStackMatcher> getMatchers() {
        return batch.keySet();
    }

    public boolean extractStacks(IConnectableLink providerStorage, Integer slot, IItemStackMatcher matcher) {
        List<BiConsumer<IConnectableLink, Integer>> extractors = batch.get(matcher);
        for (BiConsumer<IConnectableLink, Integer> extractor : extractors) {
            extractor.accept(providerStorage, slot);
        }
        return true;
    }

}
