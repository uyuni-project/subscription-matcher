package com.suse.matcher;

import com.suse.matcher.csv.CSVOutputError;
import com.suse.matcher.csv.CSVOutputSubscription;
import com.suse.matcher.csv.CSVOutputSystem;
import com.suse.matcher.facts.Product;
import com.suse.matcher.facts.Subscription;
import com.suse.matcher.facts.System;
import com.suse.matcher.facts.SystemProduct;
import com.suse.matcher.json.JsonOutput;
import com.suse.matcher.json.JsonOutputError;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Write Reports to disk
 *
 */
public class ReportWriter {

    private static final String JSON_REPORT_FILE = "match_report.json";
    private static final String CSV_SUBSCRIPTION_REPORT_FILE = "subscription_report.csv";
    private static final String CSV_UNMATCHED_SYSTEMS_REPORT_FILE = "unmatched_systems_report.csv";
    private static final String CSV_ERRORS_REPORT_FILE = "error_report.csv";

    private Assignment assignment;
    private String outdir;
    private CSVFormat csvFormat;
    private JsonOutput jsonOutput;

    public ReportWriter(Assignment assignment, String outdir) {
        this.outdir = outdir;
        this.assignment = assignment;
        this.outdir = outdir;
        csvFormat = CSVFormat.EXCEL;
        jsonOutput = FactConverter.convertToOutput(assignment);
    }

    /**
     * @param delimiter set the used CSV delimiter
     */
    public void setDelimiter(char delimiter) {
        csvFormat = csvFormat.withDelimiter(delimiter);
    }

    /**
     * Write the reports to specified output directory
     * @throws IOException
     */
    public void writeReports() throws IOException {
        writeJsonReport();
        writeCSVSubscriptionReport();
        writeCSVSystemReport();
        writeCSVErrorReport();
    }
    /**
     * Write the JSON report
     * @throws FileNotFoundException
     */
    public void writeJsonReport() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(
                new File(outdir, JSON_REPORT_FILE));
        JsonIO io = new JsonIO();
        writer.write(io.toJson(jsonOutput));
        writer.close();
    }

    /**
     * Converts the outputs from CSP solver in CSV format and write it in the directory.
     * @throws IOException
     */
    public void writeCSVSubscriptionReport() throws IOException {
        Stream<Subscription> subscriptions = assignment.getProblemFacts().stream()
                .filter(object -> object instanceof Subscription)
                .map(object -> (Subscription) object);

        Map<Long, CSVOutputSubscription> outsubs = new HashMap<Long, CSVOutputSubscription>();
        subscriptions.forEach(s -> {
            CSVOutputSubscription csvs = new CSVOutputSubscription(
                s.id,
                s.partNumber,
                s.name,
                s.quantity,
                s.startsAt,
                s.expiresAt
            );
            outsubs.put(s.id, csvs);
        });

        // extract facts from assignment by type
        assignment.getMatches().stream()
            .filter(match -> match.confirmed)
            .forEach(m -> {
                if (outsubs.containsKey(m.getSubscriptionId())) {
                    outsubs.get(m.getSubscriptionId()).increaseMatchCount(m.cents / 100);
                } else {
                    // error
                }
            });

        FileWriter fileWriter = null;
        CSVPrinter csvPrinter = null;
        try {
            //initialize FileWriter object
            fileWriter = new FileWriter(new File(outdir, CSV_SUBSCRIPTION_REPORT_FILE));
            //print CSV file header
            csvFormat = csvFormat.withHeader(CSVOutputSubscription.CSV_HEADER);
            //initialize CSVPrinter object
            csvPrinter = new CSVPrinter(fileWriter, csvFormat);

            for(Map.Entry<Long, CSVOutputSubscription> item : outsubs.entrySet()) {
                csvPrinter.printRecord(item.getValue().getCSVRow());
            }
        }
        finally {
            fileWriter.flush();
            fileWriter.close();
            csvPrinter.close();
        }
    }

    public void writeCSVSystemReport() throws IOException {
        Collection<Match> confirmedMatchFacts = assignment.getMatches().stream()
                .filter(match -> match.confirmed)
                .collect(Collectors.toList());

        List<System> systems = assignment.getProblemFacts().stream()
                .filter(object -> object instanceof System)
                .map(object -> (System) object)
                .collect(Collectors.toList());

        List<SystemProduct> systemProductFacts = assignment.getProblemFacts().stream()
                .filter(object -> object instanceof SystemProduct)
                .map(object -> (SystemProduct) object)
                .collect(Collectors.toList());

        List<Product> products = assignment.getProblemFacts().stream()
                .filter(object -> object instanceof Product)
                .map(object -> (Product) object)
                .collect(Collectors.toList());

        // prepare map from (system id, product id) to Match object
        Map<Pair<Long, Long>, Match> matchMap = new HashMap<>();
        for (Match match : confirmedMatchFacts) {
            matchMap.put(new Pair<>(match.systemId, match.productId), match);
        }

        FileWriter fileWriter = null;
        CSVPrinter csvPrinter = null;
        try {
            //initialize FileWriter object
            fileWriter = new FileWriter(new File(outdir, CSV_UNMATCHED_SYSTEMS_REPORT_FILE));
            //print CSV file header
            csvFormat = csvFormat.withHeader(CSVOutputSystem.CSV_HEADER);
            //initialize CSVPrinter object
            csvPrinter = new CSVPrinter(fileWriter, csvFormat);

            // fill output object's system fields
            for (System system : systems) {
                List<String> unmatchedProductNames = systemProductFacts.stream()
                    .filter(sp -> sp.systemId == system.id)
                    .filter(sp -> matchMap.get(new Pair<>(sp.systemId, sp.productId)) == null)
                    .map(sp -> { return products.stream()
                        .filter(p -> p.id == sp.productId)
                        .map(p -> p.name)
                        .findFirst().orElse("Unknown product");
                    })
                    .collect(Collectors.toList());

                if (!unmatchedProductNames.isEmpty()) {
                    CSVOutputSystem csvSystem = new CSVOutputSystem(
                            system.id,
                            system.name,
                            system.cpus,
                            unmatchedProductNames
                        );
                    csvPrinter.printRecords(csvSystem.getCSVRows());
                }
            }
        }
        finally {
            fileWriter.flush();
            fileWriter.close();
            csvPrinter.close();
        }
    }

    public void writeCSVErrorReport() throws IOException {
        FileWriter fileWriter = null;
        CSVPrinter csvPrinter = null;
        try {
            //initialize FileWriter object
            fileWriter = new FileWriter(new File(outdir, CSV_ERRORS_REPORT_FILE));
            //print CSV file header
            csvFormat = csvFormat.withHeader(CSVOutputError.CSV_HEADER);
            //initialize CSVPrinter object
            csvPrinter = new CSVPrinter(fileWriter, csvFormat);

            for (JsonOutputError e : jsonOutput.errors) {
                CSVOutputError csvError = new CSVOutputError(
                    e.type,
                    e.data
                );
                csvPrinter.printRecords(csvError.getCSVRows());
            }
        }
        finally {
            fileWriter.flush();
            fileWriter.close();
            csvPrinter.close();
        }
    }
}
