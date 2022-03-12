package dev.minealert.platform;

public interface PlatformInfo {


    String getName();

    String getVersion();

    default String convertToString() {
        return String.valueOf(new PlatformRecord("Platform Name: " + getName() + "\n",
                "Platform Version: " + getVersion()));
    }
}
