package nl.lolmewn.rug.quakecommon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import nl.lolmewn.rug.quakecommon.net.ServerAddress;

/**
 *
 * @author Lolmewn
 */
public class Settings {
    
    private final String fileName;
    private final Properties properties;
    private boolean newSettings = false;
    
    public Settings(String fileName) throws IOException {
        this.fileName = fileName;
        properties = new Properties();
        load();
    }
    
    private void load() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            exportSettings();
            // no exception occured, file should exist now.
        }
        properties.load(new FileInputStream(file));
    }
    
    private void exportSettings() throws IOException {
        newSettings = true;
        File file = new File(fileName);
        file.createNewFile();
        // Init all streams to auto-close on exit of try
        try (PrintWriter out = new PrintWriter(file);
                InputStream in = this.getClass().getResourceAsStream("/" + fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.println(line); // copy contents of local file to disk
            }
            out.flush();
        }
    }
    
    public Set<ServerAddress> getServers() {
        Set<ServerAddress> servers = new HashSet<>();
        // servers is defined as address,port;address,port... etc
        String stringOfServers = properties.getProperty("servers");
        String[] arrayOfServers = stringOfServers.split(";");
        for (String serverDesc : arrayOfServers) {
            String[] serverInfo = serverDesc.split(",");
            if (serverInfo.length != 2) {
                System.err.println("Server data not in line with definition, ignoring (" + serverDesc + ")...");
                continue;
            }
            String address = serverInfo[0];
            try {
                int port = Integer.parseInt(serverInfo[1]);
                servers.add(new ServerAddress(address, port));
            } catch (NumberFormatException ignored) {
                // port is not a number, report
                System.err.println("Port was not a number for server " + address + ", please check your config.");
                System.err.println("Ignoring server...");
            }
        }
        return servers;
    }
    
    public void saveServers(Set<ServerAddress> list) throws IOException {
        StringBuilder sb = new StringBuilder();
        list.stream().forEach(sa -> {
            sb.append(sa.getAddress()).append(",").append(sa.getPort()).append(";");
        });
        String servers = sb.substring(0, sb.length() - 1); // Remove trailing ;
        this.properties.setProperty("servers", servers);
        this.save();
    }
    
    public boolean isNewSettings() {
        return newSettings;
    }
    
    public synchronized Object setProperty(String key, String value) {
        return properties.setProperty(key, value);
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public void save() throws IOException {
        File file = new File(this.fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileOutputStream out = new FileOutputStream(file)) {
            properties.store(out, "They want me to add a comment, so I did.");
        }
    }
    
}
