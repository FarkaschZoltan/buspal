package hu.farkasch.buspalbackend.datastructures;

public class KeyDataPairs {
    public String key;
    public String data;

    public KeyDataPairs(String key, String data) {
        this.key = key;
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public String getData() {
        return data;
    }
}
