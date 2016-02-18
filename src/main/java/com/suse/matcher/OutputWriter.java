package com.suse.matcher;

import com.suse.matcher.csv.CSVOutputMessage;
import com.suse.matcher.csv.CSVOutputSubscription;
import com.suse.matcher.csv.CSVOutputUnmatchedProduct;
import com.suse.matcher.facts.Message;
import com.suse.matcher.facts.Product;
import com.suse.matcher.facts.Subscription;
import com.suse.matcher.facts.System;
import com.suse.matcher.facts.SystemProduct;
import com.suse.matcher.solver.Assignment;
import com.suse.matcher.solver.Match;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Writes output (to disk or standard output).
 */
public class OutputWriter {

    // filenames
    private static final String JSON_INPUT_FILE = "input.json";
    private static final String JSON_OUTPUT_FILE = "output.json";
    private static final String CSV_SUBSCRIPTION_REPORT_FILE = "subscription_report.csv";
    private static final String CSV_UNMATCHED_PRODUCT_REPORT_FILE = "unmatched_product_report.csv";
    private static final String CSV_MESSAGE_REPORT_FILE = "message_report.csv";

    /** The output directory. */
    private String outputDirectory;

    /** The CSV format. */
    private CSVFormat csvFormat;

    /**
     * Instantiates a new writer.
     *
     * @param outputDirectoryIn an output directory path. If empty, current directory is used
     * as default
     * @param delimiter an optional CSV delimiter. If empty, comma is used as default
     */
    public OutputWriter(Optional<String> outputDirectoryIn, Optional<Character> delimiter) {
        outputDirectory = outputDirectoryIn.orElse(".");
        csvFormat = CSVFormat.EXCEL;
        if (delimiter.isPresent()) {
            csvFormat = csvFormat.withDelimiter(delimiter.get());
        }
    }

    /**
     * Write the output files to the specified directory.
     *
     * @param assignment output from {@link Matcher}
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void writeOutput(Assignment assignment) throws IOException {
        writeJsonOutput(assignment);
        writeCSVSubscriptionReport(assignment);
        writeCSVUnmatchedProductReport(assignment);
        writeCSVMessageReport(assignment);
    }

    /**
     * Writes the raw input file in JSON format.
     *
     * @param input the input
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void writeJsonInput(String input) throws IOException {
        FileUtils.write(new File(outputDirectory, JSON_INPUT_FILE), input);
    }

    /**
     * Writes the raw output file in JSON format.
     *
     * @param assignment output from {@link Matcher}
     * @throws FileNotFoundException if the output directory was not found
     */
    public void writeJsonOutput(Assignment assignment) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(new File(outputDirectory, JSON_OUTPUT_FILE))) {
            JsonIO io = new JsonIO();
            writer.write(io.toJson(FactConverter.convertToOutput(assignment)));
            writer.close();
        }
    }

    /**
     * Writes the CSV subscription report.
     *
     * @param assignment output from {@link Matcher}
     * @throws IOException if an I/O error occurs
     */
    public void writeCSVSubscriptionReport(Assignment assignment) throws IOException {
        Stream<Subscription> subscriptions = assignment.getProblemFactStream(Subscription.class)
            .filter(s -> s.policy != null)
            .filter(s -> s.startDate != null && s.endDate != null)
            .filter(s -> s.quantity != null && s.quantity > 0);

        Map<Long, CSVOutputSubscription> outsubs = new TreeMap<Long, CSVOutputSubscription>();
        subscriptions.forEach(s -> {
            CSVOutputSubscription csvs = new CSVOutputSubscription(
                s.partNumber,
                s.name,
                s.policy.toString(),
                s.quantity,
                s.startDate,
                s.endDate
            );
            outsubs.put(s.id, csvs);
        });

        // compute cents by subscription id
        Map<Long, Integer> matchedCents = new HashMap<>();
        FactConverter.getMatches(assignment, true)
            .forEach(m -> matchedCents.merge(m.getSubscriptionId(), m.getCents(), Math::addExact));

        // update output
        matchedCents.forEach((subscriptionId, cents) -> {
            if (outsubs.containsKey(subscriptionId)) {
                // convert from cents to count
                // we want the partial matches (e.g. only 20 cents of a
                // subscription is used) to be counted as an used subscription
                // see http://www.cs.nott.ac.uk/~psarb2/G51MPC/slides/NumberLogic.pdf
                outsubs.get(subscriptionId).setMatched((cents + 100 - 1) / 100);
            }
            else {
                // error
            }
        });

        // prepare header
        csvFormat = csvFormat.withHeader(CSVOutputSubscription.CSV_HEADER);

        // write CSV file
        try (FileWriter writer = new FileWriter(new File(outputDirectory, CSV_SUBSCRIPTION_REPORT_FILE));
            CSVPrinter printer = new CSVPrinter(writer, csvFormat)) {
            for (Map.Entry<Long, CSVOutputSubscription> item : outsubs.entrySet()) {
                printer.printRecord(item.getValue().getCSVRow());
            }
        }
    }

    /**
     * Writes the CSV report of unmatched products and corresponding systems.
     *
     * @param assignment output from {@link Matcher}
     * @throws IOException if an I/O error occurs
     */
    public void writeCSVUnmatchedProductReport(Assignment assignment) throws IOException {
        Collection<Match> confirmedMatchFacts = FactConverter.getMatches(assignment, true);

        List<System> systems = assignment.getProblemFactStream(System.class)
                .sorted((a, b) -> a.id.compareTo(b.id))
                .collect(Collectors.toList());

        Collection<SystemProduct> systemProducts = assignment.getProblemFacts(SystemProduct.class);
        Collection<Product> products = assignment.getProblemFacts(Product.class);

        // prepare map from (system id, product id) to Match object
        Map<Pair<Long, Long>, Match> matchMap = new HashMap<>();
        for (Match match : confirmedMatchFacts) {
            matchMap.put(new Pair<>(match.systemId, match.productId), match);
        }

        // prepare header
        csvFormat = csvFormat.withHeader(CSVOutputUnmatchedProduct.CSV_HEADER);

        // write CSV file
        try (FileWriter writer = new FileWriter(new File(outputDirectory, CSV_UNMATCHED_PRODUCT_REPORT_FILE));
             CSVPrinter printer = new CSVPrinter(writer, csvFormat)) {
            // create map of product id -> set of systems ids with this product and filter out successful matches
            Map<Long, Set<Long>> unmatchedProductSystems = systemProducts.stream()
                    .filter(sp -> matchMap.get(new Pair<>(sp.systemId, sp.productId)) == null)
                    .collect(groupingBy(
                            SystemProduct::getProductId,
                            mapping(SystemProduct::getSystemId, toSet())));

            List<CSVOutputUnmatchedProduct> unmatchedProductsCsvs = unmatchedProductSystems.entrySet().stream()
                    .map(e -> new CSVOutputUnmatchedProduct(
                            productNameById(products, e.getKey()),
                            e.getValue().stream().map(sid -> systemById(systems, sid)).collect(toList())))
                    .collect(toList());

            // cant use java 8 forEach as printer throws a checked exception
            for (CSVOutputUnmatchedProduct csv : unmatchedProductsCsvs) {
                Collections.sort(csv.getUnmatchedSystems(), ((s1, s2) -> s1.name.compareTo(s2.name)));
                printer.printRecords(csv.getCSVRows());
            }
        }
    }

    private System systemById(Collection<System> systems, Long systemId) {
        return systems.stream()
                .filter(s -> systemId.equals(s.getId()))
                .findFirst()
                .get();
    }

    private String productNameById(Collection<Product> products, Long productId) {
        return products.stream()
                .filter(p -> p.id.equals(productId))
                .map(p -> p.name)
                .findFirst()
                .orElse("Unknown product (" + productId + ")");
    }

    /**
     * Writes the CSV message report.
     *
     * @param assignment output from {@link Matcher}
     * @throws IOException if an I/O error occurs
     */
    public void writeCSVMessageReport(Assignment assignment) throws IOException {
        // prepare header
        csvFormat = csvFormat.withHeader(CSVOutputMessage.CSV_HEADER);

        // write CSV file
        try (FileWriter writer = new FileWriter(new File(outputDirectory, CSV_MESSAGE_REPORT_FILE));
                CSVPrinter printer = new CSVPrinter(writer, csvFormat)) {

            List<Message> messages = assignment.getProblemFactStream(Message.class)
                .filter(m -> m.severity != Message.Level.DEBUG)
                .sorted()
                .collect(Collectors.toList());

            for (Message message: messages) {
                CSVOutputMessage csvMessage = new CSVOutputMessage(message.type, message.data);
                printer.printRecords(csvMessage.getCSVRows());
            }
        }
    }
}
