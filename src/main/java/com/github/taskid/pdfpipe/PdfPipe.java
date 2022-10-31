package com.github.taskid.pdfpipe;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class PdfPipe {

    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption(Option.builder("i").required()
            .longOpt("input")
            .argName("files")
            .hasArgs()
            .desc("Input files, e.g: -i file1.pdf file2.pdf").build());

        options.addOption(Option.builder("s")
            .longOpt("structure")
            .hasArgs()
            .desc("Document/page structure.\n===== EXAMPLE 1 =====\n-s 1 2\nAll pages of first file, all pages of second file. This is the default behaviour.\n===== EXAMPLE 2 =====\n-s 2:1,3 1:3 3:1-4\nOnly first and third page of second file, only third page of first file, pages 1-4 of third file.\n===== EXAMPLE 3 =====\n-s 1:3,2,4 2 2 3:1,1\nThird, second and fourth page of first file, two times all pages of second file, two times the first page of the third file.\n===== EXAMPLE 4 =====\n-s 2:5 1:5-1,7\nFifth page of second file, pages 5-1 (reverse order) and page 7 of first file.").build());

        options.addOption(Option.builder("o").required()
            .longOpt("output")
            .argName("path")
            .numberOfArgs(1)
            .desc("Output file, e.g: -o output.pdf").build());

        options.addOption(Option.builder("f")
            .longOpt("force")
            .numberOfArgs(0)
            .desc("Force creating file, even if output file already exists").build());

        options.addOption(Option.builder("t")
            .longOpt("title")
            .argName("title")
            .numberOfArgs(1)
            .desc("Title of the PDF file (not file name)").build());

        options.addOption(Option.builder("a")
            .longOpt("author")
            .argName("author")
            .numberOfArgs(1)
            .desc("Author of the PDF file").build());

        if (args.length == 0 || (args.length == 1 && args[0].contains("help"))) {
            if (System.console() == null) {
                JOptionPane.showMessageDialog(null, "This is a command line tool.\nOpen a CMD/Shell window and execute it with\njava -jar <filename.jar>");
                return;
            }
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.setOptionComparator(null);
            helpFormatter.printHelp("java -jar FILENAME.JAR", options, true);
            return;
        }

        CommandLine line;
        try {
            line = parser.parse(options, args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        File output = new File(line.getOptionValue("output"));
        if (output.exists() && !line.hasOption("force")) {
            System.out.println(output.getAbsolutePath() + " already exists. Use -f to force writing file.");
            return;
        }
        if (output.isDirectory()) {
            System.out.println(output.getAbsolutePath() + " is a directory, must be a file.");
            return;
        }

        String[] inputOptionValues = line.getOptionValues("input");

        File[] inputFiles = new File[inputOptionValues.length];
        for (int i = 0; i < inputOptionValues.length; i++) {
            File file = new File(inputOptionValues[i]);
            if (!file.exists() || file.isDirectory() || !file.getName().endsWith(".pdf")) {
                System.out.println("Invalid input file: " + inputOptionValues[i]);
                return;
            }
            inputFiles[i] = file;
        }

        PDDocument[] inputDocuments = new PDDocument[inputFiles.length];
        for (int i = 0; i < inputFiles.length; i++) {
            try {
                inputDocuments[i] = PDDocument.load(inputFiles[i]);
            } catch (InvalidPasswordException e) {
                System.out.println("Cannot load " + inputFiles[i].getName() + ": Protected files are unsupported");
            } catch (Exception e) {
                System.out.println("Cannot load " + inputFiles[i].getName() + ": " + e.getMessage());
                return;
            }
        }

        PDDocument outputDocument = new PDDocument();
        PDDocumentInformation information = new PDDocumentInformation();
        information.setTitle(line.getOptionValue("title", ""));
        information.setAuthor(line.getOptionValue("author", ""));
        information.setCreationDate(Calendar.getInstance(TimeZone.getDefault()));
        information.setModificationDate(Calendar.getInstance(TimeZone.getDefault()));
        outputDocument.setDocumentInformation(information);

        if (line.hasOption("structure")) {
            for (String s : line.getOptionValues("structure")) {
                String id;
                String[] pageIds;
                if (s.contains(":")) {
                    String[] split = s.split(":");
                    if (split.length != 2) {
                        System.out.println("Invalid data argument: " + s);
                        return;
                    }
                    id = split[0];
                    pageIds = split[1].split(",");
                } else {
                    id = s;
                    pageIds = null;
                }

                PDDocument document;
                try {
                    document = inputDocuments[Integer.parseInt(id) - 1];
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    System.out.println("Invalid document index: " + s);
                    return;
                }

                if (pageIds == null) {
                    for (PDPage page : document.getPages()) {
                        outputDocument.addPage(page);
                    }
                } else {
                    for (String pageId : pageIds) {
                        try {
                            if (!pageId.contains("-")) {
                                outputDocument.addPage(document.getPage(Integer.parseInt(pageId) - 1));
                            } else {
                                String[] split = pageId.split("-");
                                if (split.length != 2) {
                                    System.out.println("Invalid range: " + pageId);
                                    return;
                                }
                                int fromPageId = Integer.parseInt(split[0]);
                                int toPageId = Integer.parseInt(split[1]);

                                if (fromPageId > toPageId) {
                                    for (int i = fromPageId; i >= toPageId; i--) {
                                        outputDocument.addPage(document.getPage(i-1));
                                    }
                                } else {
                                    for (int i = fromPageId; i <= toPageId; i++) {
                                        outputDocument.addPage(document.getPage(i-1));
                                    }
                                }
                            }
                        } catch (IndexOutOfBoundsException | NumberFormatException e) {
                            System.out.println("Invalid page index: " + pageId);
                            return;
                        }
                    }
                }
            }
        } else {
            for (PDDocument document : inputDocuments) {
                for (PDPage page : document.getPages()) {
                    outputDocument.addPage(page);
                }
            }
        }

        try {
            outputDocument.save(output);
            System.out.println("Saved output file to: " + output.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error saving output file: " + e.getMessage());
        }

        for (PDDocument input : inputDocuments) {
            try {
                input.close();
            } catch (IOException e) {
                System.out.println("Couldn't close input file: " + e.getMessage());
            }
        }
        try {
            outputDocument.close();
        } catch (IOException e) {
            System.out.println("Couldn't close output file: " + e.getMessage());
        }
    }

}