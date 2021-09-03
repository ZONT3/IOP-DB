package ru.zont.iopdb.data;

import android.util.Log;

import java.util.HashMap;

public class DataEntity {
    private final HashMap<String, Object> map = new HashMap<>();

    DataEntity() {

    }

    public <T> T getValue(String key) {
        return getValue(key, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, T orElse) {
        T res;
        try {
            res = (T) map.get(key);
        } catch (Exception e) {
            Log.e("DataEntityCast", "Exception while casting entity: " + e.getLocalizedMessage());
            res = null;
        }
        return res != null ? res : orElse;
    }

    void setValue(String key, Object value) {
        map.put(key, value);
    }

    public int getID() {
        return getValue("id", -1);
    }

    public String getType() {
        return getValue("type", "unknown");
    }

    public String getName() {
        return getValue("name", "???");
    }

    public int getRarity() {
        return getValue("rarity", -1);
    }

    public String getThumbnail() {
        return getValue("img_thumb");
    }
}
