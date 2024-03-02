package me.kyren223.kapi.data;

import java.util.Map;

public interface ConfigSerializable {
    void serialize(Config config);
    void deserialize(Config config);
}
