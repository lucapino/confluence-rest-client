package com.github.lucapino.confluence.rest.client.example;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Reads properties from a file.
 */
public class PropertiesFileStore {

    private final String configPath;

    private final Map<String, String> defaultValues;

    public PropertiesFileStore(final String configPath, Map<String, String> defaultValues) throws Exception {
        this.configPath = configPath;
        this.defaultValues = defaultValues;
    }

    public Map<String, String> getProperties() {
        try {
            return toMap(tryGetProperties());
        } catch (FileNotFoundException e) {
            tryCreateDefaultFile();
            return new HashMap<>(defaultValues);
        } catch (IOException e) {
            return new HashMap<>(defaultValues);
        }
    }

    public void setProperties(Map<String, String> properties) {
        savePropertiesToFile(properties);
    }

    private Map<String, String> toMap(Properties properties) {
        return properties.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(o -> o.getKey().toString(), t -> t.getValue().toString()));
    }

    private Properties toProperties(Map<String, String> propertiesMap) {
        Properties properties = new Properties();
        propertiesMap.entrySet()
                .stream()
                .forEach(entry -> properties.put(entry.getKey(), entry.getValue()));
        return properties;
    }

    private Properties tryGetProperties() throws IOException {
        InputStream inputStream = new FileInputStream(new File(configPath));
        Properties prop = new Properties();
        prop.load(inputStream);
        return prop;
    }

    public void savePropertiesToFile(Map<String, String> properties) {
        OutputStream outputStream = null;
        File file = new File(configPath);

        try {
            outputStream = new FileOutputStream(file);
            Properties p = toProperties(properties);
            p.store(outputStream, null);
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                // ignored
            }
        }
    }

    public void tryCreateDefaultFile() {
        tryCreateFile().ifPresent(file -> savePropertiesToFile(defaultValues));
    }

    private Optional<File> tryCreateFile() {
        try {
            File file = new File(configPath);
            file.createNewFile();
            return Optional.of(file);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
