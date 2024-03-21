package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HostelDetailsHandler {
    protected HashMap<String, String> hostelDetails;

    public HostelDetailsHandler() {
        hostelDetails = new HashMap<>();
        try {
            readFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromFile() throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("hostelInfo.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(", ");
                if (details.length >= 2) {
                    hostelDetails.put(details[0], details[1]);
                } else {
                    // If line does not contain enough data, log a warning or simply skip it
                    System.out.println("Warning: Invalid format in the file. Skipping line: " + line);
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public void writeToFile(String hostelName, String address) throws IOException {
        BufferedWriter writer = null;
        try {
            if (hostelDetails.containsKey(hostelName)) {
                throw new IllegalArgumentException("Hostel name already exists.");
            } else if (hostelDetails.containsValue(address)) {
                throw new IllegalArgumentException("Address already exists for another hostel.");
            } else {
                writer = new BufferedWriter(new FileWriter("hostelInfo.txt", true));
                writer.write(hostelName.replace(",", " ") + ", " + address.replace(",", " ") + "\n"); 
                hostelDetails.put(hostelName, address);
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


    public HashMap<String, String> getHostelDetails() {
        return hostelDetails;
    }

    public Set<String> getHostelNames() {
        return new HashSet<>(hostelDetails.keySet());
    }

    public String getHostelAddress(String hostelName) {
        return hostelDetails.getOrDefault(hostelName, "Hostel not found.");
    }
}