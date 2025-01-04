import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Library {
    private static final String BOOKS_FILE = "books.json"; //Gerekli olan books ve readers dosyaları ve
    private static final String READERS_FILE = "readers.json"; // onlara bağlı ArrayList yapıları tanımlanıyor.
    private List<Book> books = new ArrayList<>();
    private List<Reader> readers = new ArrayList<>();

    //Program ilk çalıştığında mevcut dosyaları okuyup bunları belleğe alır.
    public void loadData() {
        books = loadFromFile(BOOKS_FILE, new TypeToken<List<Book>>() {});
        readers = loadFromFile(READERS_FILE, new TypeToken<List<Reader>>() {});
    }

    //Programdan çıkış yapıldığında bellekte yapılan değişikliklerin dosyalara kaydedilmesi.
    public void saveData() {
        saveToFile(BOOKS_FILE, books);
        saveToFile(READERS_FILE, readers);
    }

    //Programın çalıştırıldığı ana metot, burada istenilen diğer metotlara gidilmesi sağlanıyor.
    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Borrow a Book");
            System.out.println("2. Return a Book");
            System.out.println("3. Manage Readers");
            System.out.println("4. Manage Books");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    borrowBookMenu();
                    break;
                case "2":
                    returnBookMenu();
                    break;
                case "3":
                    manageReadersMenu();
                    break;
                case "4":
                    manageBooksMenu();
                    break;
                case "5":
                    System.out.println("Exiting program. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    //Kitap ödünç alınması istendiğine gerçekleştirilen metot.
    private void borrowBookMenu() {
        Scanner scanner = new Scanner(System.in);
        List<Book> availableBooks = new ArrayList<>();

        System.out.println("\nAvailable Books:");
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (!book.isBorrowed()) { //Kitapların ödünç alınıp alınmadığına göre if şartı yardımıyla yazdırır.
                availableBooks.add(book);
                System.out.println((availableBooks.size()) + ". " + book);
            }
        }

        if (availableBooks.isEmpty()) {
            System.out.println("No books available for borrowing.");
            return;
        }

        //Kullanıcıdan alınan integer ile kitap seçimi yapılır.
        //0 ya da geçersiz bir seçim yapılırsa ana menüye döner.
        System.out.print("Select a book to borrow (or 0 to return to main menu): ");
        int bookChoice = Integer.parseInt(scanner.nextLine()) - 1;
        if (bookChoice < 0 || bookChoice >= availableBooks.size()) {
            System.out.println("Returning to main menu.");
            return;
        }
        Book selectedBook = availableBooks.get(bookChoice);

        //Sistemde kayıtlı okurlar varsa bunlar listelenir
        System.out.println("\nRegistered Readers:");
        for (int i = 0; i < readers.size(); i++) {
            System.out.println((i + 1) + ". " + readers.get(i));
        }

        //Kullanıcı dilerse bu adımda yeni okur ekleyebilir. Ya da listelenmiş okurlardan birini seçebilir.
        System.out.println("N. Add new reader and borrow");
        System.out.print("Select a reader: ");
        String readerChoice = scanner.nextLine();

        Reader reader;
        if (readerChoice.equalsIgnoreCase("N")) {
            reader = addNewReader();
        } else {
            int readerIndex = Integer.parseInt(readerChoice) - 1;
            if (readerIndex < 0 || readerIndex >= readers.size()) {
                System.out.println("Invalid choice. Returning to main menu.");
                return;
            }
            reader = readers.get(readerIndex);
        }

        //Son adımda onay istenir. Onaylama yapılırsa seçilen kitap ödünç alınmış olur.
        System.out.println("You are about to lend '" + selectedBook.getTitle() + "' to " + reader.getName() + ". Confirm? (Y/N)");
        if (scanner.nextLine().equalsIgnoreCase("Y")) {
            selectedBook.borrow(reader);
            System.out.println("Book borrowed successfully!");
        } else {
            System.out.println("Operation canceled.");
        }
    }

    //Kullanıcı ödünç alınan kitapları geri teslim etmek istediğinde bu metotu kullanmış olur.
    private void returnBookMenu() {
        Scanner scanner = new Scanner(System.in);

        //Ödünç alınmış kitaplar varsa listelenir, yoksa olmadığı bildirilir.
        System.out.println("\nBorrowed Books:");
        List<Book> borrowedBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isBorrowed()) {
                borrowedBooks.add(book);
                System.out.println((borrowedBooks.size()) + ". " + book);
            }
        }

        if (borrowedBooks.isEmpty()) {
            System.out.println("No books are currently borrowed.");
            return;
        }

        //Kullanıcının seçimi 0 veya geçersiz ise ana menüye döner.
        System.out.print("Select a book to return (or 0 to return to main menu): ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice < 0 || choice >= borrowedBooks.size()) {
            System.out.println("Returning to main menu.");
            return;
        }

        //Bir kitap seçildiyse onay istenir ve sonucunda kitap müsait duruma geçer.
        Book selectedBook = borrowedBooks.get(choice);
        System.out.println("You are about to return '" + selectedBook.getTitle() + "'. Confirm? (Y/N)");
        if (scanner.nextLine().equalsIgnoreCase("Y")) {
            selectedBook.returnBook();
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Operation canceled.");
        }
    }

    //Kullanıcı bu metot yardımıyla kayıtlı okurları yönetebilir.
    private void manageReadersMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nManage Readers:");
            System.out.println("1. List Readers");
            System.out.println("2. Add Reader");
            System.out.println("3. Delete Reader");
            System.out.println("4. Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    listReaders();
                    break;
                case "2":
                    addNewReader();
                    break;
                case "3":
                    deleteReader();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    //Bu metotla ise kayıtlı kitap yönetimi yapılır.
    private void manageBooksMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nManage Books:");
            System.out.println("1. List Books");
            System.out.println("2. Add Book");
            System.out.println("3. Delete Book");
            System.out.println("4. Back to Main Menu");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    listBooks();
                    break;
                case "2":
                    addNewBook();
                    break;
                case "3":
                    deleteBook();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    //Okurları for döngüsü ile alt alta yazdırır.
    private void listReaders(){
        System.out.println("\nReaders:");
        for (int i = 0; i < readers.size(); i++) {
            System.out.println((i + 1) + ". " + readers.get(i));
        }
    }

    //Kitapları for döngüsü ile alt alta yazdırır.
    private void listBooks() {
        System.out.println("\nBooks:");
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }
    }

    //Yeni okur ekler
    private Reader addNewReader() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Reader's Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Reader's Email: ");
        String email = scanner.nextLine();

        Reader newReader = new Reader(name, email);

        if (readers.contains(newReader)) { //Eğer okur zaten kayıtlıysa bunu bildirir, ikinci kez kayıt yapmaz.
            System.out.println("Reader already registered.");
            return null;
        } else {
            readers.add(newReader);
            System.out.println("New reader added successfully!");
            return newReader;
        }
    }

    //Kayıtlı okurları silmeye yarar
    private void deleteReader() {
        Scanner scanner = new Scanner(System.in);

        listReaders();

        System.out.print("Select a reader to delete (or 0 to return to main menu): ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice < 0 || choice >= readers.size()) {
            System.out.println("Returning to main menu.");
            return;
        }

        readers.remove(choice);
        System.out.println("Reader deleted successfully!");
    }

    //Yeni kitap eklemeyi sağlar.
    private void addNewBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Book Author: ");
        String author = scanner.nextLine();

        Book newBook = new Book(title, author);
        if (books.contains(newBook)){ //Aynı ada sahip kitap varsa bunu bildirir, ikinci kez ekleme yapmaz.
            System.out.println("Book already registered.");
        } else {
            books.add(newBook);
            System.out.println("New book added successfully!");
        }
    }

    //Kayıtlı kitapları silmeyi sağlar.
    private void deleteBook() {
        Scanner scanner = new Scanner(System.in);

        listBooks();

        System.out.print("Select a book to delete (or 0 to return to main menu): ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice < 0 || choice >= books.size()) {
            System.out.println("Returning to main menu.");
            return;
        }

        Book selectedBook = books.get(choice);
        System.out.println("You are about to delete '" + selectedBook.getTitle() + "'. Confirm? (Y/N)");
        if (scanner.nextLine().equalsIgnoreCase("Y")) {
            books.remove(choice);
            System.out.println("Book deleted successfully!");
        } else {
            System.out.println("Operation canceled.");
        }
    }

    //JSON dosyalarının okunma ve yazma işlemleri için kullandığım gson kütüphanesini içeren kodlar.
    private <T> List<T> loadFromFile(String filename, TypeToken<List<T>> typeToken) {
        try (FileReader reader = new FileReader(filename)) {
            return new Gson().fromJson(reader, typeToken.getType());
        } catch (IOException e) {
            System.out.println("Error loading data from " + filename + ". Returning empty list.");
            return new ArrayList<>();
        }
    }

    private <T> void saveToFile(String filename, List<T> data) {
        try (Writer writer = new FileWriter(filename)) {
            new Gson().toJson(data, writer);
        } catch (IOException e) {
            System.out.println("Error saving data to " + filename + ".");
        }
    }
}
