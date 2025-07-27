import java.io.*;
import java.util.*;

/**
 * This class provides the driver program for TransplantGraph, offering a
 * menu-driven interface to list, add, remove, and sort donors and recipients.
 */
public class TransplantDriver {
    public static final String DONOR_FILE     = "donors.txt";
    public static final String RECIPIENT_FILE = "recipients.txt";
    private static TransplantGraph graph;
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Main method which runs the transplant matching driver program.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        loadGraph();
        boolean running = true;

        while (running) {
            printMainMenu();
            String option = scanner.nextLine().toUpperCase();
            System.out.println();
            switch (option) {
                case "LR": graph.printAllRecipients(); break;
                case "LO": graph.printAllDonors(); break;
                case "AO": addDonor(); break;
                case "AR": addRecipient(); break;
                case "RO": removeDonor(); break;
                case "RR": removeRecipient(); break;
                case "SR": sortSubmenu(graph.getRecipients(), true); break;
                case "SO": sortSubmenu(graph.getDonors(), false); break;
                case "Q":  running = false; saveGraph(); break;
                default:   System.out.println("Invalid option."); break;
            }
        }
        System.out.println("Program terminating normally...");
    }

    /**
     * It loads the TransplantGraph from a saved file, or from txt files if absent.
     */
    private static void loadGraph() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("transplant.obj"))) {
            graph = (TransplantGraph) ois.readObject();
            System.out.println("Loading data from transplant.obj...");
        } catch (Exception e) {
            System.out.println("transplant.obj not found. Creating new TransplantGraph object...");
            try {
                System.out.println("Loading data from '" + DONOR_FILE + "'...");
                System.out.println("Loading data from '" + RECIPIENT_FILE + "'...");
                graph = TransplantGraph.buildFromFiles(DONOR_FILE, RECIPIENT_FILE);
            } catch (IOException io) {
                System.out.println("Error loading files: " + io.getMessage());
            }
        }
    }

    /**
     * It saves the current TransplantGraph to a file.
     */
    private static void saveGraph() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("transplant.obj"))) {
            oos.writeObject(graph);
            System.out.println("Writing data to transplant.obj...");
        } catch (IOException io) {
            System.out.println("Error saving data: " + io.getMessage());
        }
    }

    /**
     * It prints the main menu options for user interaction.
     */
    private static void printMainMenu() {
        System.out.println();
        System.out.println("Menu:");
        System.out.println("    (LR) - List all recipients");
        System.out.println("    (LO) - List all donors");
        System.out.println("    (AO) - Add new donor");
        System.out.println("    (AR) - Add new recipient");
        System.out.println("    (RO) - Remove donor");
        System.out.println("    (RR) - Remove recipient");
        System.out.println("    (SR) - Sort recipients");
        System.out.println("    (SO) - Sort donors");
        System.out.println("    (Q) - Quit\n");
        System.out.print("Please select an option: ");
    }

    /**
     * It prompts for donor details, adds a new donor, and confirms addition.
     */
    private static void addDonor() {
        System.out.print("Please enter the organ donor name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Please enter the blood type of " + name + ": ");
        String bt = scanner.nextLine().trim();
        System.out.print("Please enter the age of " + name + ": ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid age. Donor not added.");
            return;
        }
        System.out.print("Please enter the organs " + name + " is donating: ");
        String organ = scanner.nextLine().trim();
        Patient donor = new Patient(0, name, age, organ, new BloodType(bt), true);
        graph.addDonor(donor);
        System.out.println("The organ donor, " + name +
                ", has been added to the donor list with ID " + donor.getID() + ".");
    }

    /**
     * It prompts for recipient details, adds a new recipient, and confirms addition.
     */
    private static void addRecipient() {
        System.out.print("Please enter new recipient's name: ");
        String name = scanner.nextLine();
        System.out.print("Please enter the recipient's blood type: ");
        String bt = scanner.nextLine();
        System.out.print("Please enter the recipient's age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Please enter the organ needed: ");
        String organ = scanner.nextLine();
        Patient rec = new Patient(0, name, age, organ, new BloodType(bt), false);
        graph.addRecipient(rec);
        System.out.println("The organ recipient, " + name + ", has been added to the recipient list with ID " +
                rec.getID() + ".");
    }

    /**
     * It prompts for a donor name, removes that donor if present, and confirms removal.
     */
    private static void removeDonor() {
        System.out.print("Please enter the name of the organ donor to remove: ");
        String name = scanner.nextLine();
        boolean exists = graph.getDonors().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(name));
        graph.removeDonor(name);
        if (exists)
            System.out.println(name + " was removed from the organ donor list.");
    }

    /**
     * It prompts for a recipient name, removes that recipient if present, and confirms removal.
     */
    private static void removeRecipient() {
        System.out.print("Please enter the name of the recipient to remove: ");
        String name = scanner.nextLine().trim();
        boolean exists = graph.getRecipients().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(name));
        graph.removeRecipient(name);
        if (exists)
            System.out.println(name + " was removed from the organ transplant waitlist.");
    }

    /**
     * It displays a sorting submenu, sorts the given list by selected criteria,
     * and prints the sorted results.
     *
     * @param list the list of patients to sort
     * @param isRecipient true if sorting recipients, false for donors
     */
    private static void sortSubmenu(List<Patient> list, boolean isRecipient) {
        while (true) {
            System.out.println("   (I) Sort by ID");
            System.out.println("   (N) Sort by Number of " + (isRecipient ? "Donors" : "Recipients"));
            System.out.println("   (B) Sort by Blood Type");
            System.out.println("   (O) Sort by Organ " + (isRecipient ? "Needed" : "Donated"));
            System.out.println("   (Q) Back to Main Menu");
            System.out.print("\nPlease select an option: ");
            String opt = scanner.nextLine().toUpperCase();
            System.out.println();
            if (opt.equals("Q")) {
                System.out.println("Returning to main menu.");
                break;
            }
            List<Patient> temp = new ArrayList<>(list);
            switch (opt) {
                case "I": Collections.sort(temp); break;
                case "N": Collections.sort(temp, new NumConnectionsComparator(graph)); break;
                case "B": Collections.sort(temp, new BloodTypeComparator()); break;
                case "O": Collections.sort(temp, new OrganComparator()); break;
                default:    System.out.println("Invalid option."); continue;
            }
            if (isRecipient) {
                printRecipients(temp);
                System.out.println();
            } else {
                printDonors(temp);
                System.out.println();
            }
        }
    }

    /**
     * It prints a formatted table of recipients with their compatible donor IDs.
     *
     * @param list the list of recipients to print
     */
    private static void printRecipients(List<Patient> list) {
        System.out.printf("Index | Recipient Name     | Age | Organ Needed  | Blood Type | Donor IDs%n");
        System.out.println("==========================================================================");
        for (Patient p : list) {
            int j = p.getID();
            System.out.printf("   %d  | %-18s | %2d  | %-13s |     %-6s | ",
                    j, p.getName(), p.getAge(), p.getOrgan(), p.getBloodType().getType());
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < graph.getDonors().size(); i++) {
                if (graph.isConnected(i, j)) ids.add(i);
            }
            for (int k = 0; k < ids.size(); k++) {
                System.out.print(ids.get(k));
                if (k < ids.size() - 1) System.out.print(", ");
            }
            System.out.println();
        }
    }

    /**
     * It prints a formatted table of donors with their compatible recipient IDs.
     *
     * @param list the list of donors to print
     */
    private static void printDonors(List<Patient> list) {
        System.out.printf("Index | Donor Name         | Age | Organ Donated | Blood Type | Recipient IDs%n");
        System.out.println("=============================================================================");
        for (Patient p : list) {
            int i = p.getID();
            System.out.printf("   %d  | %-18s | %2d  | %-13s |     %-6s | ",
                    i, p.getName(), p.getAge(), p.getOrgan(), p.getBloodType().getType());
            List<Integer> ids = new ArrayList<>();
            for (int j = 0; j < graph.getRecipients().size(); j++) {
                if (graph.isConnected(i, j)) ids.add(j);
            }
            for (int k = 0; k < ids.size(); k++) {
                System.out.print(ids.get(k));
                if (k < ids.size() - 1) System.out.print(", ");
            }
            System.out.println();
        }
    }
}
