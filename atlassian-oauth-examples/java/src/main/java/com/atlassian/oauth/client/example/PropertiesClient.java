package com.atlassian.oauth.client.example;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

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

public class PropertiesClient {
    public static final String CONSUMER_KEY = "consumer_key";
    public static final String PRIVATE_KEY = "private_key";
    public static final String REQUEST_TOKEN = "request_token";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String SECRET = "secret";
    public static final String JIRA_HOME = "jira_home";


    private final static Map<String, String> DEFAULT_PROPERTY_VALUES = ImmutableMap.<String, String>builder()
            .put(JIRA_HOME, "https:peterkentish.atlassian.net")
            .put(CONSUMER_KEY, "OauthKey")
            .put(PRIVATE_KEY, "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMvH8LCKalC/0DvY1e8Ksh4cchd3xdJraUannv6LzHCSTrZfRveyoAX1eXGUoLLuAszmhkXhKyyuIuLc1AaJvESlFRZHfNq5bBgpQOd8HGe9dSzC3V8mvMokRe9E7PFGFlDxILcLR8Zb/twAIH21DhrhJz3yPh1QXVBjtr/R+ZjNAgMBAAECgYALoEWYHN2B69+aen2CHM8arq7HrfqoTZ58/HUyupEYXxCEkR0AZr2AeYfLNhqQ+slIHWLNu9H1w52T6dti4BrQibBSeiR+Aallar+6T3Rvz+ePBD+qq+n1JJq9P6P7m+SdWnj/v2iJn0jheGMzo07omxAuL6AXARxIRN/NK0s1IQJBAO6ckLeexGS0HOThhYSYFckgaBrpCBzpbus4O9V9ZGV0Eptur5hBhwm6samasLjHwKWVBJrTDv0JvaVqKNBb+rUCQQDaoZdDZDRyM1rE8dSjLqYlZZ3ZIsQ0TQTqUu5/Ktw9ZFT909ZDAle1PGB6N3jfWBdpDFpbKj4aIu9wCTcCIzy5AkEAyEFKC3EJ7mJjJYxIHEHvdr7l4D/W+TzIRE0Lml8EVUkXHK/GWwgTpwyycl9LFak/ezgXh0C/AYqdSShRXJz1SQJBAJYbRAOdFPUjlTqK3vd628/pSMsAN73A85L+hYkCIFx2OnRbsUwom5dvcL34wCB4Fvqk5JSbGDBRtBsz+HSbROECQCano2UgK5kQmIVj8QTGOQYkrNy2at7yTvH9Gx3O1XHiMTYmPbqZD82CbXXtLRaH/6IwwtkPIfWbk08kGB3/mJ8=")
            .build();

    private final String fileUrl;
    private final String propFileName = "config.properties";

    public PropertiesClient() throws Exception {
        fileUrl = "./" + propFileName;
    }

    public Map<String, String> getPropertiesOrDefaults() {
        try {
            Map<String, String> map = toMap(tryGetProperties());
            map.putAll(Maps.difference(map, DEFAULT_PROPERTY_VALUES).entriesOnlyOnRight());
            return map;
        } catch (FileNotFoundException e) {
            tryCreateDefaultFile();
            return new HashMap<>(DEFAULT_PROPERTY_VALUES);
        } catch (IOException e) {
            return new HashMap<>(DEFAULT_PROPERTY_VALUES);
        }
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
        InputStream inputStream = new FileInputStream(new File(fileUrl));
        Properties prop = new Properties();
        prop.load(inputStream);
        return prop;
    }

    public void savePropertiesToFile(Map<String, String> properties) {
        OutputStream outputStream = null;
        File file = new File(fileUrl);

        try {
            outputStream = new FileOutputStream(file);
            Properties p = toProperties(properties);
            p.store(outputStream, null);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            closeQuietly(outputStream);
        }
    }

    public void tryCreateDefaultFile() {
        System.out.println("Creating default properties file: " + propFileName);
        tryCreateFile().ifPresent(file -> savePropertiesToFile(DEFAULT_PROPERTY_VALUES));
    }

    private Optional<File> tryCreateFile() {
        try {
            File file = new File(fileUrl);
            file.createNewFile();
            return Optional.of(file);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            // ignored
        }
    }
}
