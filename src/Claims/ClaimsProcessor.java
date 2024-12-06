package Claims;

import java.io.*;
import java.util.*;

public class ClaimsProcessor {
    // HashMap to store claims data by product and origin year
    private Map<String, Map<Integer, Map<Integer, Double>>> ClaimsData;

    // Constructor to initialize ClaimsData map
    public ClaimsProcessor() {
        ClaimsData = new TreeMap<>();
    }

    // Method to read the CSV file
    public void readClaimsData(String inputFilePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            // Skips header line
            br.readLine();

            // Read each line
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String product = values[0].trim();
                int originYear = Integer.parseInt(values[1].trim());
                int developmentYear = Integer.parseInt(values[2].trim());
                double incrementalValue = Double.parseDouble(values[3].trim());

                // Add the data to the claimsData map
                ClaimsData
                        .computeIfAbsent(product, k -> new HashMap<>())
                        .computeIfAbsent(originYear, k -> new HashMap<>())
                        .put(developmentYear, incrementalValue);
            }
        }
    }

    // Process the claims data to calculate cumulative values
    public String processClaims() {
        StringBuilder result = new StringBuilder();

        // Get the earliest origin year and list of all development years
        int earliestYear = ClaimsData.values().stream()
                .flatMap(m -> m.keySet().stream())
                .min(Integer::compareTo)
                .orElse(0);

        // Create a set of all unique development years
        Set<Integer> devYears = new TreeSet<>();
        ClaimsData.values().forEach(m -> m.values().forEach(devYearMap -> devYears.addAll(devYearMap.keySet())));

        // Adds the first line with the earliest year and number of development years
        result.append(earliestYear).append(", ").append(devYears.size()).append("\n");

        // Processes each product and its claims
        for (String product : ClaimsData.keySet()) {
            Map<Integer, Map<Integer, Double>> productData = ClaimsData.get(product);
            StringBuilder productResult = new StringBuilder(product);

            // For each origin year in the product's data
            for (int originYear = earliestYear; originYear <= getMaxOriginYear(); originYear++) {
                double cumulative = 0;

                //  calculates the cumulative sum for each development year
                for (int devYear : devYears) {
                    Double value = productData.getOrDefault(originYear, Collections.emptyMap()).get(devYear);
                    if (value != null && value != 0.0) {  // Only add non-zero values
                        cumulative += value;
                    }

                    // Append cumulative sum, only if it's meaningful (i.e., non-zero or a relevant sum)
                    if (value != null || cumulative != 0.0) {
                        productResult.append(", ").append(Math.round(cumulative * 10.0) / 10.0); // Format rounded to 1 decimal place
                    }
                }
            }

            // Appends the product result to the final result
            result.append(productResult).append("\n");
        }

        return result.toString();
    }

    // Gets the maximum origin year (from the data) to limit iterations
    private int getMaxOriginYear() {
        return ClaimsData.values().stream()
                .flatMap(m -> m.keySet().stream())
                .max(Integer::compareTo)
                .orElse(0);
    }

    // Writes the results to the output file
    public void writeClaimsData(String outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(processClaims());
        }
    }

    public static void main(String[] args) {
        ClaimsProcessor processor = new ClaimsProcessor();
        try {
            processor.readClaimsData("test.csv");
            processor.writeClaimsData("out.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
