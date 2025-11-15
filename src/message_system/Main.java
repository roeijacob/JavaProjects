package message_system;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Message> messages = new ArrayList<>();

        try {
            messages.add(new BoardMessage("Teacher", "Exam on Sunday", false));
            messages.add(new BoardMessage("Admin", "Building will be closed tomorrow", true));

            EmailMessage email1 = new EmailMessage(
                    "admin@college.com",
                    "Please read the attached file.",
                    false,
                    "Important notice"
            );
            email1.addAttachment(new File("instructions", "pdf"));

            EmailMessage email2 = new EmailMessage(
                    "support@site.com",
                    "Your password was changed successfully.",
                    true,
                    "Password changed"
            );
            email2.addAttachment(new File("security_tips", "txt"));

            messages.add(email1);
            messages.add(email2);

            messages.add(new SmsMessage("Mom", "Call me when you finish class", false, "050-1234567"));
            messages.add(new SmsMessage("Friend", "Wanna play tennis today?", false, "052-7654321"));

        } catch (AttachmentException e) {
            System.out.println("Attachment error while creating initial messages: " + e.getMessage());
        } catch (SmsException e) {
            System.out.println("SMS error while creating initial messages: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error while creating initial messages: " + e.getMessage());
        }

        boolean running = true;

        while (running) {

            System.out.println("\n--- MENU ---");
            System.out.println("1. Add message");
            System.out.println("2. Remove message");
            System.out.println("3. Print all messages");
            System.out.println("4. Show message count");
            System.out.println("5. Print DIGITAL messages only");
            System.out.println("6. Manage Email attachments (extra option)");
            System.out.println("7. Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {

            case 1: 
                System.out.println("Choose type: 1-Board, 2-Email, 3-SMS");
                int type = sc.nextInt();
                sc.nextLine();

                System.out.print("Sender: ");
                String sender = sc.nextLine();

                System.out.print("Content: ");
                String content = sc.nextLine();

                boolean isRead = false; 

                try {
                    if (type == 1) { 
                        messages.add(new BoardMessage(sender, content, isRead));

                    } else if (type == 2) { 
                        System.out.print("Subject: ");
                        String subject = sc.nextLine();

                        EmailMessage email = new EmailMessage(sender, content, isRead, subject);

                        System.out.print("Do you want to add attachments? (y/n): ");
                        String ans = sc.nextLine();

                        while (ans.equalsIgnoreCase("y")) {
                            System.out.print("File name (without type): ");
                            String fName = sc.nextLine();
                            System.out.print("File type (e.g. pdf, jpg): ");
                            String fType = sc.nextLine();

                            File newFile = new File(fName, fType);

                            try {
                                email.addAttachment(newFile);
                                System.out.println("File added.");
                            } catch (AttachmentException e) {
                                System.out.println("Attachment error: " + e.getMessage());
                            }

                            System.out.print("Add another file? (y/n): ");
                            ans = sc.nextLine();
                        }

                        messages.add(email);

                    } else if (type == 3) { 
                        System.out.print("Phone: ");
                        String phone = sc.nextLine();
                        messages.add(new SmsMessage(sender, content, isRead, phone));
                    } else {
                        System.out.println("Unknown type");
                    }
                } catch (SmsException e) {
                    System.out.println("SMS error: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;


            case 2: 
                if (messages.isEmpty()) {
                    System.out.println("No messages.");
                    break;
                }
                System.out.print("Enter index to remove (0 - " + (messages.size()-1) + "): ");
                int idx = sc.nextInt();
                sc.nextLine();
                if (idx >= 0 && idx < messages.size()) {
                    messages.remove(idx);
                    System.out.println("Message removed.");
                } else {
                    System.out.println("Invalid index.");
                }
                break;

            case 3:
                if (messages.isEmpty()) {
                    System.out.println("No messages.");
                } else {
                    for (int i = 0; i < messages.size(); i++) {
                        Message m = messages.get(i);
                        
                        System.out.println("[" + (i + 1) + "] " + m);

                        if (m instanceof EmailMessage) {
                            System.out.println("   " + ((EmailMessage)m).printCommunicationMethod());
                        } 
                        else if (m instanceof SmsMessage) {
                            System.out.println("   " + ((SmsMessage)m).printCommunicationMethod());
                        } 
                    }
                }
                break;



            case 4: 
                System.out.println("Total messages: " + messages.size());
                break;

            case 5:
                for (Message m : messages) {
                    if (m instanceof IDigital) {
                        System.out.println(m);

                        if (m instanceof EmailMessage) {
                            System.out.println("   " + ((EmailMessage)m).printCommunicationMethod());
                        }
                        else if (m instanceof SmsMessage) {
                            System.out.println("   " + ((SmsMessage)m).printCommunicationMethod());
                        }
                    }
                }
                break;


            case 6: 
                manageEmailAttachments(sc, messages);
                break;

            case 7:
                running = false;
                break;

            default:
                System.out.println("Wrong choice.");
            }
        }

        sc.close();
    }

    private static void manageEmailAttachments(Scanner sc, ArrayList<Message> messages) {
        ArrayList<EmailMessage> emails = new ArrayList<>();
        for (Message m : messages) {
            if (m instanceof EmailMessage) {
                emails.add((EmailMessage)m);
            }
        }

        if (emails.isEmpty()) {
            System.out.println("No email messages available.");
            return;
        }

        System.out.println("Email list:");
        for (int i = 0; i < emails.size(); i++) {
            System.out.println("[" + i + "] " + emails.get(i));
        }

        System.out.print("Choose email index: ");
        int eIdx = sc.nextInt();
        sc.nextLine();
        if (eIdx < 0 || eIdx >= emails.size()) {
            System.out.println("Invalid index.");
            return;
        }

        EmailMessage chosen = emails.get(eIdx);

        System.out.println("\n1. Add file");
        System.out.println("2. Remove file");
        System.out.println("3. Show files");
        System.out.print("Choose: ");
        int opt = sc.nextInt();
        sc.nextLine();

        try {
            switch (opt) {
                case 1: 
                    System.out.print("File name (without type): ");
                    String fName = sc.nextLine();
                    System.out.print("File type (e.g. pdf, jpg): ");
                    String fType = sc.nextLine();
                    File newFile = new File(fName, fType);
                    chosen.addAttachment(newFile);
                    System.out.println("File added.");
                    break;

                case 2: 
                    System.out.print("File name (without type): ");
                    String rName = sc.nextLine();
                    System.out.print("File type: ");
                    String rType = sc.nextLine();
                    File target = new File(rName, rType);
                    chosen.removeAttachment(target);
                    System.out.println("File removed (if existed).");
                    break;

                case 3: 
                    System.out.println("Attachments: " + chosen.getAttachments());
                    break;

                default:
                    System.out.println("Wrong option.");
            }
        } catch (AttachmentException e) {
            System.out.println("Attachment error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
