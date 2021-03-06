package com.redhat.ceylon.common;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    
    /**
     * Create a temporary directory
     * @param prefix The prefix to use for the directory name
     * @return a File pointing to the new directory
     * @throws RuntimeException if the directory could not be created
     */
    public static File makeTempDir(String prefix){
        try {
            File dir = File.createTempFile(prefix, "");
            if(!dir.delete() || !dir.mkdirs()) {
                throw new RuntimeException("Failed to create tmp dir: "+dir);
            }
            return dir;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Delete a file or directory
     * @param f The file or directory to be deleted
     * @throws RuntimeException if the file or directory could not be deleted
     */
    public static void delete(File f){
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        }
        if (!f.delete()) {
            throw new RuntimeException("Failed to delete file: " + f.getPath());
        }
    }
    
    /**
     * The OS-specific directory where global application data can be stored.
     * As given by the {@code ceylon.config.dir} system property or the default
     * OS-dependent directory if the property doesn't exist. (/etc/ceylon on
     * Unix-like systems and %ALLUSERSPROFILE%/ceylon on WIndows)
     */
    public static File getSystemConfigDir() throws IOException {
        File configDir = null;
        String ceylonConfigDir = System.getProperty("ceylon.config.dir");
        if (ceylonConfigDir != null) {
            configDir = new File(ceylonConfigDir);
        } else {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.startsWith("windows")) {
                String appDir = System.getenv("ALLUSERSPROFILE");
                if (appDir != null) {
                    configDir = new File(appDir, "ceylon");
                }
            } else if (os.startsWith("mac")) {
                configDir = new File("/etc/ceylon");
            } else {
                // Assume a "regular" unix OS
                configDir = new File("/etc/ceylon");
            }
        }
        return configDir;
    }

    /**
     * The installation directory. As given by the {@code ceylon.home}
     * system property
     */
    public static File getInstallDir() {
        String ceylonHome = System.getProperty("ceylon.home");
        if (ceylonHome != null) {
            return new File(ceylonHome);
        } else {
            return null;
        }
    }

    /**
     * The default user directory, that is {@code ~/.ceylon}.
     */
    public static File getDefaultUserDir() {
        String userHome = System.getProperty("user.home");
        return new File(userHome, ".ceylon");
    }

    /**
     * The effective user directory, checking the {@code ceylon.user.dir}
     * system property then defaulting to {@link getDefaultUserDir}.
     */
    public static File getUserDir() {
        String ceylonUserDir = System.getProperty("ceylon.user.dir");
        if (ceylonUserDir != null) {
            return new File(ceylonUserDir);
        } else {
            return getDefaultUserDir();
        }
    }
    
}
