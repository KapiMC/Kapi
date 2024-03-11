package me.kyren223.kapi.utility;

import me.kyren223.kapi.KPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class DocumentStore {
    
    private DocumentStore() {
        // Prevent instantiation
    }
    
    private static final HashMap<String, FileConfiguration> documents = new HashMap<>();
    
    private static String getPath(@NotNull String path) {
        path = path.trim();
        
        if (path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be empty");
        }
        
        path = path.replace("\\", "/");
        
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        
        if (path.endsWith("/")) {
            throw new IllegalArgumentException("Must be a file, not a directory");
        }
        
        if (!path.endsWith(".yml")) {
            path += ".yml";
        }
        
        return path;
    }
    
    /**
     * Creates a new document
     * <p></p>
     * If the document already exists, nothing will happen
     * <p>
     * If the document exists as a file but wasn't loaded yet, it will get loaded
     * @param path The path to the document
     */
    public static void createDocument(String path) {
        path = getPath(path);
        if (documentExists(path)) return;
        loadDocument(path);
    }
    
    /**
     * Checks if a document exists
     * @param path The path to the document
     * @return True if the document exists, false otherwise
     */
    public static boolean documentExists(String path) {
        path = getPath(path);
        return documents.containsKey(path);
    }
    
    public static FileConfiguration getDocument(String path) {
        path = getPath(path);
        if (!documentExists(path)) createDocument(path);
        return documents.get(path);
    }
    
    private static FileConfiguration getDocumentFromDisk(String path) {
        File file = new File(KPlugin.get().getDataFolder(), path);
        if (!file.exists()) return null;
        return YamlConfiguration.loadConfiguration(file);
    }
    
    /**
     * Loads the document from disk
     * <p>
     * If the document is already loaded, it will overwrite it
     * <p>
     * If the document doesn't exist, it will create a new empty document
     * @param path Path to the document
     */
    public static void loadDocument(String path) {
        path = getPath(path);
        
        FileConfiguration document = getDocumentFromDisk(path);
        if (document == null) {
            document = new YamlConfiguration();
        }
        
        documents.put(path, document);
    }
    
    /**
     * Loads all documents from the plugin's data folder
     */
    public static void loadDocuments() {
        File[] files = KPlugin.get().getDataFolder().listFiles();
        if (files == null) {
            Log.error("Failed to load documents, no files found");
            return;
        }
        Arrays.stream(files).forEach(file -> {
            if (file.isFile() && file.getName().endsWith(".yml")) {
                loadDocument(file.getName());
            }
        });
        Log.success("Loaded " + documents.size() + " documents successfully");
    }
    
    /**
     * Saves the document to disk
     * @param path The path to the document
     */
    public static void saveDocument(String path) {
        path = getPath(path);
        FileConfiguration document = documents.get(path);
        try {
            document.save(new File(KPlugin.get().getDataFolder(), path));
        } catch (IOException e) {
            Log.error("Failed to save document " + path);
        }
    }
    
    /**
     * Saves all documents to disk
     * See {@link #saveDocument(String)}
     */
    public static void saveDocuments() {
        documents.forEach((path, document) -> saveDocument(path));
        Log.success("Saved " + documents.size() + " documents successfully");
    }
}
