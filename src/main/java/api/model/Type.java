package api.model;

import java.util.HashMap;
import java.util.Map;

public enum Type {
    LECTURE('L'),
    PRACTICAL('P'),
    TUTORIAL('T'),
    SEMINAR('S'),
    UNKNOWN('U');

    private static final Map<Character, Type> characterTypeMap = new HashMap<>();

    static {
        for (Type type : Type.values()) {
            characterTypeMap.put(type.type, type);
        }
    }

    private final Character type;

    Type(Character type) {
        this.type = Character.toUpperCase(type);
    }

    public static Type getInstanceFromTypeValue(Character type) {
        return characterTypeMap.get(Character.toUpperCase(type));
    }
}