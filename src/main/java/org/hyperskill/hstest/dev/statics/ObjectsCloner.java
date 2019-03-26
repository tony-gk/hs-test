package org.hyperskill.hstest.dev.statics;

import com.google.gson.Gson;

import java.awt.*;
import java.util.Scanner;

public class ObjectsCloner {
    private static String serializeObject(Object object) {
        Gson gson = new Gson();
        String serialized = gson.toJson(object);
        return serialized;
    }

    private static Object deserializeObject(String serialized, Class<?> clazz) {
        Gson gson = new Gson();
        Object deserialized = gson.fromJson(serialized, clazz);
        return deserialized;
    }

    public static Object cloneObject(Object obj) {
        // GSON gives StackOverFlow exception serializing Scanner
        // but since user can't use multiple Scanner's in program
        // it's really not necessary to clone Scanner
        // also when testing swing don't need to clone Component objects
        if (obj instanceof Scanner || obj instanceof Component) {
            return obj;
        }
        if (obj == null) {
            return null;
        }
        String serialized = serializeObject(obj);
        Object cloned = deserializeObject(serialized, obj.getClass());
        return cloned;
    }
}
