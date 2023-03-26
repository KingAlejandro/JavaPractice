import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Phonebook phonebook = new Phonebook();
        PhonebookGUI phonebookGUI = new PhonebookGUI(phonebook);
        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println(
                    "Select an option: n1. Add a contact, n2. Remove a contact, n3. Update a contact, n4. Search, n5. Display all contacts, n6. Import/Export, n7. Quit");
            choice = scanner.nextInt();
            scanner.nextLine(); // to consume the leftover newline character

            switch (choice) {
                case 1:
                    System.out.println("Enter a name to add to the phonebook:");
                    String name = scanner.nextLine();

                    System.out.println("Enter a phone number for " + name + ":");
                    String phoneNumber = scanner.nextLine();

                    Contact contact  = new Contact(name, phoneNumber);
                    phonebook.addEntry(contact);

                    System.out.println(name + " has been added to the phonebook.");
                    break;
                case 2:
                    System.out.println("Enter a name to remove from the phonebook:");
                    name = scanner.nextLine();
                    
                    phonebook.deleteEntry(name);

                    System.out.println(name + " has been removed from the phonebook.");
                    break;
                case 3:
                    System.out.println("Enter a name to update in the phonebook:");
                    name = scanner.nextLine();

                    System.out.println("Enter a new phone number for " + name + ":");
                    phoneNumber = scanner.nextLine();

                    phonebook.updateEntry(name, phoneNumber);

                    System.out.println(name + "'s phone number has been updated in the phonebook.");
                    break;
                case 4:
                    System.out.println(
                            "Select a search method: n1. Search by exact name, n2. Search by exact phone number, n3. Search by part of a name or number");
                    int searchChoice = scanner.nextInt();
                    scanner.nextLine(); // to consume the leftover newline character

                    switch (searchChoice) {
                        case 1:
                            System.out.println("Enter a name to search for:");
                            name = scanner.nextLine();

                            phoneNumber = phonebook.getPhoneNumber(name);
                            if (phoneNumber != null) {
                                System.out.println(phoneNumber);
                            }
                            break;
                        case 2:
                            System.out.println("Enter a phone number to search for:");
                            phoneNumber = scanner.nextLine();

                            name = phonebook.getName(phoneNumber);
                            if (name != null) {
                                System.out.println(name);
                            }
                            break;
                        case 3:
                            System.out.println("Enter a part of a name or part of a phone number to search for:");
                            String query = scanner.nextLine();

                            ArrayList<Contact> contacts = phonebook.searchContacts(query);
                            if (contacts != null) {
                                for (Contact c : contacts) {
                                    c.printContact();
                                }
                            }
                            break;
                        default:
                            System.out.println("Invalid search method.");
                            break;
                    }
                    break;
                case 5:
                    ArrayList<Contact> contacts = phonebook.displayAllContacts();
                    if (contacts != null) {
                        for (Contact c : contacts) {
                            c.printContact();
                        }
                    }
                    break;
                case 6:
                    System.out.println(
                            "n1. Import, n2. Export");
                    int ExportImportChoice = scanner.nextInt();
                    scanner.nextLine(); // to consume the leftover newline character

                    String filepath;
                    switch (ExportImportChoice) {
                        case 1:
                            System.out.println("Enter file path:");
                            filepath = scanner.nextLine();

                            phonebook.importFromCsv(filepath);
                            break;
                        case 2:
                            System.out.println("Enter file path:");
                            filepath = scanner.nextLine();

                            phonebook.exportToCsv(filepath);
                            break;
                        default:
                            System.out.println("Invalid");
                            break;
                    }
                    break;
                case 7:
                    System.out.println("Exiting phonebook...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}
