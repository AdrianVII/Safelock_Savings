package com.aa.safelocksaving.data;

public class AppData {
    private String lastVersion;
    private int lastVersionCode;

    public AppData(){    }

    public AppData(String lastVersion, int lastVersionCode, String url) {
        this.lastVersion = lastVersion;
        this.lastVersionCode = lastVersionCode;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public int getLastVersionCode() {
        return lastVersionCode;
    }

    public void setLastVersionCode(int lastVersionCode) {
        this.lastVersionCode = lastVersionCode;
    }
}
