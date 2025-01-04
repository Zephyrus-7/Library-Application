public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        library.loadData(); //En başta mevcut bulunan veriler yüklenir
        library.mainMenu(); //Programın ana işlem menüsü açılır
        library.saveData(); //Menüden çıkıldığında yapılan değişiklikler kaydedilir.
    }
}
