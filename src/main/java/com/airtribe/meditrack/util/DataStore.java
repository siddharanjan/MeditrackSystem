package com.airtribe.meditrack.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// generic wrapper around a map so the service classes aren't all rewriting
// the same save/find/remove code. using LinkedHashMap so "view all" comes
// back in the order stuff was added, not random hash order
public class DataStore<T> {

    private final Map<String, T> records = new LinkedHashMap<>();

    public void save(String id, T item) {
        records.put(id, item);
    }

    public T find(String id) {
        return records.get(id);
    }

    public boolean remove(String id) {
        return records.remove(id) != null;
    }

    public boolean exists(String id) {
        return records.containsKey(id);
    }

    public List<T> findAll() {
        return new ArrayList<>(records.values());
    }

    public int size() {
        return records.size();
    }
}
