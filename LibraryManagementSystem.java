import java.io.*;
import java.util.*;

// =========================
//      BOOK CLASS
// =========================
class Book implements Comparable<Book> {
    int bookId;
    String title;
    String author;
    String category;
    boolean isIssued;

    public Book(int bookId, String title, String author, String category, boolean isIssued) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isIssued = isIssued;
    }

    public void displayBookDetails() {
        System.out.println("\n--- Book Details ---");
        System.out.println("Book ID: " + bookId);
        System.out.println("Title  : " + title);
        System.out.println("Author : " + author);
        System.out.println("Category: " + category);
        System.out.println("Issued : " + (isIssued ? "Yes" : "No"));
    }

    public void markAsIssued() {
        isIssued = true;
    }

    public void markAsReturned() {
        isIssued = false;
    }

    // Comparable → Sort by title
    @Override
    public int compareTo(Book b) {
        return this.title.compareToIgnoreCase(b.title);
    }

    @Override
    public String toString() {
        return bookId + "," + title + "," + author + "," + category + "," + isIssued;
    }
}

// =========================
//      MEMBER CLASS
// =========================
class Member {
    int memberId;
    String name;
    String email;
    List<Integer> issuedBooks = new ArrayList<>();

    public Member(int memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }

    public void displayMemberDetails() {
        System.out.println("\n--- Member Details ---");
        System.out.println("Member ID: " + memberId);
        System.out.println("Name      : " + name);
        System.out.println("Email     : " + email);
        System.out.println("Issued Books: " + issuedBooks);
    }

    public void addIssuedBook(int bookId) {
        issuedBooks.add(bookId);
    }

    public void returnIssuedBook(int bookId) {
        issuedBooks.remove(Integer.valueOf(bookId));
    }

    @Override
    public String toString() {
        return memberId + "," + name + "," + email + "," + issuedBooks.toString();
    }
}

// =========================
//      LIBRARY MANAGER
// =========================
public class LibraryManagementSystem {

    Map<Integer, Book> books = new HashMap<>();
    Map<Integer, Member> members = new HashMap<>();
    Set<String> categorySet = new HashSet<>();
    Scanner sc = new Scanner(System.in);

    File booksFile = new File("books.txt");
    File membersFile = new File("members.txt");

    // Constructor loads existing data
    public LibraryManagementSystem() {
        loadFromFile();
    }

    // =====================
    //   ADD BOOK
    // =====================
    public void addBook() {
        try {
            System.out.print("Enter Book Title: ");
            sc.nextLine();
            String title = sc.nextLine();

            System.out.print("Enter Author: ");
            String author = sc.nextLine();

            System.out.print("Enter Category: ");
            String category = sc.nextLine();

            int bookId = books.size() + 101;

            Book b = new Book(bookId, title, author, category, false);
            books.put(bookId, b);
            categorySet.add(category);

            saveToFile();

            System.out.println("Book added successfully with ID: " + bookId);

        } catch (Exception e) {
            System.out.println("Error adding book.");
        }
    }

    // =====================
    //   ADD MEMBER
    // =====================
    public void addMember() {
        try {
            System.out.print("Enter Member Name: ");
            sc.nextLine();
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            int memberId = members.size() + 1001;

            Member m = new Member(memberId, name, email);
            members.put(memberId, m);

            saveToFile();

            System.out.println("Member added successfully with ID: " + memberId);

        } catch (Exception e) {
            System.out.println("Error adding member.");
        }
    }

    // =====================
    //   ISSUE BOOK
    // =====================
    public void issueBook() {
        try {
            System.out.print("Enter Book ID: ");
            int bookId = sc.nextInt();

            System.out.print("Enter Member ID: ");
            int memberId = sc.nextInt();

            if (!books.containsKey(bookId)) {
                System.out.println("Book not found.");
                return;
            }

            if (!members.containsKey(memberId)) {
                System.out.println("Member not found.");
                return;
            }

            Book b = books.get(bookId);

            if (b.isIssued) {
                System.out.println("Book already issued.");
                return;
            }

            b.markAsIssued();
            members.get(memberId).addIssuedBook(bookId);

            saveToFile();

            System.out.println("Book issued successfully.");

        } catch (Exception e) {
            System.out.println("Error issuing book.");
        }
    }

    // =====================
    //   RETURN BOOK
    // =====================
    public void returnBook() {
        try {
            System.out.print("Enter Book ID: ");
            int bookId = sc.nextInt();

            System.out.print("Enter Member ID: ");
            int memberId = sc.nextInt();

            if (!books.containsKey(bookId) || !members.containsKey(memberId)) {
                System.out.println("Invalid details.");
                return;
            }

            Book b = books.get(bookId);
            Member m = members.get(memberId);

            if (!b.isIssued) {
                System.out.println("Book is not issued.");
                return;
            }

            b.markAsReturned();
            m.returnIssuedBook(bookId);

            saveToFile();

            System.out.println("Book returned successfully.");

        } catch (Exception e) {
            System.out.println("Error returning book.");
        }
    }

    // =====================
    //   SEARCH BOOKS
    // =====================
    public void searchBooks() {
        sc.nextLine();
        System.out.print("Search by (title/author/category): ");
        String keyword = sc.nextLine().toLowerCase();

        for (Book b : books.values()) {
            if (b.title.toLowerCase().contains(keyword) ||
                b.author.toLowerCase().contains(keyword) ||
                b.category.toLowerCase().contains(keyword)) {
                b.displayBookDetails();
            }
        }
    }

    // =====================
    //   SORT BOOKS
    // =====================
    public void sortBooks() {
        List<Book> sorted = new ArrayList<>(books.values());

        System.out.println("\nSort by: 1) Title  2) Author");
        int ch = sc.nextInt();

        if (ch == 1) {
            Collections.sort(sorted);
        } else if (ch == 2) {
            sorted.sort(Comparator.comparing(b -> b.author.toLowerCase()));
        }

        for (Book b : sorted) {
            b.displayBookDetails();
        }
    }

    // =====================
    //   SAVE TO FILE
    // =====================
    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(booksFile))) {
            for (Book b : books.values()) {
                bw.write(b.toString());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error saving books.");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(membersFile))) {
            for (Member m : members.values()) {
                bw.write(m.toString());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error saving members.");
        }
    }

    // =====================
    //   LOAD FROM FILE
    // =====================
    public void loadFromFile() {
        try {
            if (!booksFile.exists()) booksFile.createNewFile();
            if (!membersFile.exists()) membersFile.createNewFile();

            BufferedReader br = new BufferedReader(new FileReader(booksFile));
            String line;

            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                Book b = new Book(
                    Integer.parseInt(arr[0]),
                    arr[1], arr[2], arr[3],
                    Boolean.parseBoolean(arr[4])
                );
                books.put(b.bookId, b);
                categorySet.add(arr[3]);
            }
            br.close();

            br = new BufferedReader(new FileReader(membersFile));
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                Member m = new Member(
                    Integer.parseInt(arr[0]),
                    arr[1], arr[2]
                );
                members.put(m.memberId, m);
            }
            br.close();

        } catch (Exception e) {
            System.out.println("Error loading files.");
        }
    }

    // =====================
    //   MAIN MENU
    // =====================
    public void menu() {
        int ch = 0;

        while (ch != 7) {
            System.out.println("\n===== City Library Digital Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. Add Member");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Books");
            System.out.println("6. Sort Books");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            ch = sc.nextInt();

            switch (ch) {
                case 1: addBook(); break;
                case 2: addMember(); break;
                case 3: issueBook(); break;
                case 4: returnBook(); break;
                case 5: searchBooks(); break;
                case 6: sortBooks(); break;
                case 7: saveToFile(); System.out.println("Exiting..."); break;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // =====================
    //        MAIN
    // =====================
    public static void main(String[] args) {
        LibraryManagementSystem sys = new LibraryManagementSystem();
        sys.menu();
    }
}
