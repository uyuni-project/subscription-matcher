package com.suse.matcher;

import com.suse.matcher.csv.CSVOutputSubscription;
import com.suse.matcher.csv.CSVOutputSystem;
import com.suse.matcher.facts.System;
import com.suse.matcher.facts.SystemProduct;
import com.suse.matcher.json.JsonMatch;
import com.suse.matcher.json.JsonSubscription;
import com.suse.matcher.json.JsonSystem;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Optional;
import java.util.TreeMap;
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

    private List<JsonSystem> systems;
    private List<JsonSubscription> subscriptions;
    private List<JsonMatch> pinnedMatches;
    private Assignment assignment;
    private String outdir;

    public ReportWriter(List<JsonSystem> systems, List<JsonSubscription> subscriptions,
            List<JsonMatch> pinnedMatches, Assignment assignment, String outdir) {
        this.systems = systems;
        this.subscriptions = subscriptions;
        this.pinnedMatches = pinnedMatches;
        this.outdir = outdir;
        this.assignment = assignment;
        this.outdir = outdir;
    }

    /**
     * Write the reports to specified output directory
     * @throws IOException
     */
    public void writeReports() throws IOException {
        writeJsonReport();
        writeCSVReports();
    }
    /**
     * Write the JSON report
     * @throws FileNotFoundException
     */
    public void writeJsonReport() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(
                new File(outdir, JSON_REPORT_FILE));
        JsonIO io = new JsonIO();
        writer.write(io.toJson(FactConverter.convertToOutput(assignment)));
        writer.close();
    }

    /**
     * Converts the outputs from CSP solver in CSV format and write it in the directory.
     * @throws IOException
     */
    public void writeCSVReports() throws IOException {
        if (StringUtils.isEmpty(outdir)) {
            throw new IOException("Output directory not specified");
        }
        Map<Long, CSVOutputSubscription> outsubs = new HashMap<Long, CSVOutputSubscription>();
        subscriptions.stream().forEach(s -> {
            CSVOutputSubscription csvs = new CSVOutputSubscription(s);
            outsubs.put(s.id, csvs);
        });
        // extract facts from assignment by type
        assignment.getMatches().stream()
        .filter(match -> match.confirmed)
        .forEach(m -> {
            if (outsubs.containsKey(m.getSubscriptionId())) {
                outsubs.get(m.getSubscriptionId()).consume(m.cents / 100);
            }
            else {
                // error
            }
        });

        FileWriter fileWriter = null;
        CSVPrinter csvPrinter = null;
        CSVFormat  csvFormat = CSVFormat.DEFAULT.withRecordSeparator('\n');
        try {
            //initialize FileWriter object
            fileWriter = new FileWriter(new File(outdir, CSV_SUBSCRIPTION_REPORT_FILE));
            //initialize CSVPrinter object
            csvPrinter = new CSVPrinter(fileWriter, csvFormat);
            //Create CSV file header
            //CSV file header
            Object [] header = {"id","partNumber","systemLimit","consumed","startDate","endDate"};
            csvPrinter.printRecord(header);

            for(Map.Entry<Long, CSVOutputSubscription> item : outsubs.entrySet()) {
                csvPrinter.printRecord(item.getValue().getCSVRow());
            }
        }
        finally {
            fileWriter.flush();
            fileWriter.close();
            csvPrinter.close();
        }

        //########################################################################################
        Collection<Match> confirmedMatchFacts = assignment.getMatches().stream()
                .filter(match -> match.confirmed)
                .collect(Collectors.toList());

        Stream<System> systemFacts = assignment.getProblemFacts().stream()
                .filter(object -> object instanceof System)
                .map(object -> (System) object);

        List<SystemProduct> systemProductFacts = assignment.getProblemFacts().stream()
                .filter(object -> object instanceof SystemProduct)
                .map(object -> (SystemProduct) object)
                .collect(Collectors.toList());

        // prepare map from (system id, product id) to Match object
        Map<Pair<Long, Long>, Match> matchMap = new HashMap<>();
        for (Match match : confirmedMatchFacts) {
            matchMap.put(new Pair<>(match.systemId, match.productId), match);
        }

        // prepare map from system id to set of product ids
        Map<Long, List<Long>> systemMap = systemFacts
                .collect(Collectors.toMap(
                        system -> system.id,
                        system -> Stream.concat(
                                confirmedMatchFacts.stream()
                                    .filter(match -> match.systemId.equals(system.id))
                                    .map(match -> match.productId),
                                systemProductFacts.stream()
                                    .filter(systemProduct -> systemProduct.systemId.equals(system.id))
                                    .map(systemProduct -> systemProduct.productId)
                            ).distinct().sorted().collect(Collectors.toList()),
                        (id1, id2) -> id1,
                        TreeMap::new
                ));

        //initialize FileWriter object
        fileWriter = new FileWriter(new File(outdir, CSV_UNMATCHED_SYSTEMS_REPORT_FILE));
        //initialize CSVPrinter object
        csvPrinter = new CSVPrinter(fileWriter, csvFormat);
        //Create CSV file header
        //CSV file header
        Object [] header = {"id","cpus","unmatched product IDs"};
        csvPrinter.printRecord(header);

        // fill output object's system fields
        for (Long systemId : systemMap.keySet()) {
            Optional<JsonSystem> o = systems.stream().filter(s -> s.id == systemId).findFirst();
            if (! o.isPresent()) {
                continue;
            }
            CSVOutputSystem csvSystem = new CSVOutputSystem(o.get());

            Collection<Long> productIds = systemMap.get(systemId);
            if (productIds != null) {
                for (Long productId : productIds) {

                    Match match = matchMap.get(new Pair<>(systemId, productId));
                    if (match != null) {
                        csvSystem.productIds.remove(productId);
                    }
                }
            }
            if (!csvSystem.productIds.isEmpty()) {
                csvPrinter.printRecord(csvSystem.getCSVRow());
            }
        }
        fileWriter.flush();
        fileWriter.close();
        csvPrinter.close();
    }

}
