package de.eternalcode.challenges.utils;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class JsonDocument {
    private static final Gson gson = (new GsonBuilder()).serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
    private static final JsonParser parser = new JsonParser();
    private final JsonObject json;

    public JsonDocument() {
        this.json = new JsonObject();
    }

    public JsonDocument(JsonObject object) {
        this.json = object;
    }

    public int size() {
        return this.json.size();
    }

    public JsonDocument remove(String key) {
        this.json.remove(key);
        return this;
    }

    public boolean contains(String key) {
        return this.json.get(key) != null;
    }

    public JsonDocument append(String key, Boolean value) {
        if (key != null && value != null) {
            this.json.addProperty(key, value);
            return this;
        } else {
            return this;
        }
    }

    public JsonDocument append(String key, String value) {
        if (key != null && value != null) {
            this.json.addProperty(key, value);
            return this;
        } else {
            return this;
        }
    }

    public JsonDocument append(String key, Integer value) {
        if (key != null && value != null) {
            this.json.addProperty(key, value);
            return this;
        } else {
            return this;
        }
    }

    public JsonDocument append(JsonObject jsonObject) {
        if (jsonObject == null) {
            return this;
        } else {
            for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                this.json.add((String)entry.getKey(), (JsonElement)entry.getValue());
            }

            return this;
        }
    }

    public int getInt(String key) {
        return !this.contains(key) ? 0 : this.json.get(key).getAsInt();
    }

    public double getDouble(String key) {
        return !this.contains(key) ? (double)0.0F : (double)this.json.get(key).getAsInt();
    }

    public float getFloat(String key) {
        return !this.contains(key) ? 0.0F : this.json.get(key).getAsFloat();
    }

    public byte getByte(String key) {
        return !this.contains(key) ? 0 : this.json.get(key).getAsByte();
    }

    public short getShort(String key) {
        return !this.contains(key) ? 0 : this.json.get(key).getAsShort();
    }

    public long getLong(String key) {
        return !this.contains(key) ? 0L : this.json.get(key).getAsLong();
    }

    public boolean getBoolean(String key) {
        return !this.contains(key) ? false : this.json.get(key).getAsBoolean();
    }

    public String getString(String key) {
        return !this.contains(key) ? null : this.json.get(key).getAsString();
    }

    public Object getObject(String key) {
        return !this.contains(key) ? null : this.json.get(key);
    }

    public List<?> getList(String key) {
        return !this.contains(key) ? null : (List)this.json.get(key);
    }

    public Map<?, ?> getMap(String key) {
        return !this.contains(key) ? null : (Map)this.json.get(key);
    }

    public void removeKey(String key) {
        if (this.contains(key)) {
            this.json.remove(key);
        }
    }

    public <T> T toInstanceOf(Class<T> clazz) {
        return (T)gson.fromJson(this.json, clazz);
    }

    public <T> T toInstanceOf(Type type) {
        return (T)gson.fromJson(this.json, type);
    }

    public void save(String filename) {
        File file = new File(filename);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(this.json, outputStreamWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void save(File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(this.json, outputStreamWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static JsonDocument loadDocument(String file) {
        try {
            return new JsonDocument((JsonObject)parser.parse(new FileReader(new File(file))));
        } catch (Exception var2) {
            return null;
        }
    }

    public static JsonDocument loadDocument(File file) {
        try {
            return new JsonDocument((JsonObject)parser.parse(new FileReader(file)));
        } catch (Exception var2) {
            return null;
        }
    }

    public String toString() {
        return this.json.toString();
    }
}
