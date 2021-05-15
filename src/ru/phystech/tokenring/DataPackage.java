package ru.phystech.tokenring;

public class DataPackage {

    private final String data;
    public long time;

    public DataPackage(String data) {
        this.data = data;
    }

    public String getData(){
        return data;
    }
}

