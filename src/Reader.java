import java.util.Objects;

////Bu class'ta okurların sahip olduğu değişkenler ve getter-setter metotları yer alır.
public class Reader {
    private String name;
    private String email;

    public Reader(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //Override girdileri isim karşılaştırmalarının yapılabilmesi için yazılmıştır.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same object reference
        if (obj == null || getClass() != obj.getClass()) return false; // Null or different class
        Reader reader = (Reader) obj;
        return name.equalsIgnoreCase(reader.name); // Compare name only
    }

    @Override
    public int hashCode() {
        return Objects.hash(name); // Hash only name
    }

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}
