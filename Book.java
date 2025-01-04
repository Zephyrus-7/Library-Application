import java.util.Objects;

//Bu class'ta kitapların sahip olduğu değişkenler ve getter-setter metotları yer alır.
public class Book {
    private String title;
    private String author;
    private boolean isBorrowed;
    private Reader borrowedBy;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
        this.borrowedBy = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void borrow(Reader reader) {
        this.isBorrowed = true;
        this.borrowedBy = reader;
    }

    public void returnBook() {
        this.isBorrowed = false;
        this.borrowedBy = null;
    }

    public Reader getBorrowedBy() {
        return borrowedBy;
    }

    //Override girdileri isim karşılaştırmalarının yapılabilmesi için yazılmıştır.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same object reference
        if (obj == null || getClass() != obj.getClass()) return false; // Null or different class
        Book book = (Book) obj;
        return title.equalsIgnoreCase(book.title); // Compare title only (case-insensitive)
    }

    @Override
    public int hashCode() {
        return Objects.hash(title.toLowerCase()); // Hash only title (case-insensitive)
    }

    @Override
    public String toString() {
        if (isBorrowed) {
            return title + " by " + author + " (Borrowed by: " + borrowedBy.getName() + ")";
        }
        return title + " by " + author + " (Available)";
    }
}