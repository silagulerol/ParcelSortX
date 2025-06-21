package fabric; // Or whatever package your main application is in

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConfigLoader {

    private static Properties properties;
    private static final String CONFIG_FILE_NAME = "config.properties";

    static {
        properties = new Properties();
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (input == null) {
                System.out.println("Warning: " + CONFIG_FILE_NAME + " not found in classpath. Trying direct file system access.");
                try (InputStream fileInput = new FileInputStream(CONFIG_FILE_NAME)) {
                    properties.load(fileInput);
                } catch (IOException e) {
                    System.err.println("Error: Could not load " + CONFIG_FILE_NAME + " from classpath or file system.");
                    e.printStackTrace();
                }
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            System.err.println("Error loading configuration file: " + CONFIG_FILE_NAME);
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                System.err.println("Warning: Property '" + key + "' is not a valid integer. Using default value: " + defaultValue);
            }
        }
        return defaultValue;
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value.trim());
        }
        return defaultValue;
    }

    /**
     * Gets a property value as a String array, splitting by a delimiter.
     *
     * @param key The key of the property.
     * @param delimiter The delimiter to use for splitting the string (e.g., ",").
     * @return A String array of the values, or an empty array if the property is not found or empty.
     */
    public static String[] getStringArrayProperty(String key, String delimiter) {
        String value = getProperty(key);
        if (value != null && !value.trim().isEmpty()) {
            // Trim each individual element to remove leading/trailing spaces
            return Arrays.stream(value.split(delimiter))
                         .map(String::trim)
                         .filter(s -> !s.isEmpty()) // Remove empty strings if delimiter causes them
                         .toArray(String[]::new);
        }
        return new String[0]; // Return an empty array if property not found or empty
    }

    /**
     * Gets a property value as a List of Strings, splitting by a delimiter.
     *
     * @param key The key of the property.
     * @param delimiter The delimiter to use for splitting the string (e.g., ",").
     * @return A List of Strings of the values, or an empty list if the property is not found or empty.
     */
    public static List<String> getStringListProperty(String key, String delimiter) {
        String value = getProperty(key);
        if (value != null && !value.trim().isEmpty()) {
            return Arrays.stream(value.split(delimiter))
                         .map(String::trim)
                         .filter(s -> !s.isEmpty())
                         .collect(Collectors.toList());
        }
        return Arrays.asList(); // Return an empty list
    }

    /**
     * Gets a property value as an int array, splitting by a delimiter.
     *
     * @param key The key of the property.
     * @param delimiter The delimiter to use for splitting the string (e.g., ",").
     * @return An int array of the values, or an empty array if the property is not found or empty.
     */
    public static int[] getIntArrayProperty(String key, String delimiter) {
        String value = getProperty(key);
        if (value != null && !value.trim().isEmpty()) {
            return Arrays.stream(value.split(delimiter))
                         .map(String::trim)
                         .filter(s -> !s.isEmpty())
                         .mapToInt(s -> {
                             try {
                                 return Integer.parseInt(s);
                             } catch (NumberFormatException e) {
                                 System.err.println("Warning: Element '" + s + "' in property '" + key + "' is not a valid integer. Skipping this element.");
                                 return -1; // Or throw an exception, or some other error handling
                             }
                         })
                         .filter(i -> i != -1) // Filter out invalid integers (based on our error handling)
                         .toArray();
        }
        return new int[0]; // Return an empty array
    }
    
    

    public static double getDoubleProperty(String key, double defaultValue) {
    	String value = getProperty(key);
    	if (value != null) {
    		try {
    			return Double.parseDouble(value.trim());
    		} catch (NumberFormatException e) {
    			System.err.println("Warning: Property '" + key + "' is not a valid integer. Using default value: " + defaultValue);
    		}
    	}
    	return defaultValue;
    }
    
}