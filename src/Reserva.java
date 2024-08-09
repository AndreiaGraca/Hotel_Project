import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;

public class Reserva {
    private static int number = 1;
    String name;
    int nif;
    double total_price;
    int nights;
    LocalDate check_in;
    LocalDate check_out;
    int number_persons;
    Room_Type type;
    int adults;
    int children;
    double children_description; //idade das crianças
    boolean pets; //se trazem animais
    int number_of_pets;
    double pet_description; //peso do cão/gato
    double price_per_night; //preço por noite do quarto
    boolean hydromassage; //se é com hidromassagem
    boolean romantic_night; // se é noite romantica;
    Quarto quarto;
    int n_camas;
    boolean check_in_made, check_out_made;

    public Reserva(String name, int nif, int nights, LocalDate check_in, LocalDate check_out, int number_persons, Room_Type type, int adults, int children, double children_description, boolean pets, int number_of_pets, double pet_description, double price_per_night, boolean hydromassage, boolean romantic_night, Quarto quarto, int n_camas) {
        this.number = number++;
        this.name = name;
        this.nif = nif;
        this.nights = nights;
        this.check_in = check_in;
        this.check_out = check_out;
        this.number_persons = number_persons;
        this.type = type;
        this.adults = adults;
        this.children = children;
        this.children_description = children_description;
        this.pets = pets;
        this.number_of_pets = number_of_pets;
        this.pet_description = pet_description;
        this.price_per_night = price_per_night;
        this.hydromassage = hydromassage;
        this.romantic_night = romantic_night;
        this.quarto = quarto;
        this.n_camas = n_camas;
        this.check_in_made=false;
        this.check_out_made=false;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getNif() {
        return nif;
    }

    public double getTotal_price() {
        return total_price;
    }

    public int getNights() {
        return nights;
    }

    public LocalDate getCheck_in() {
        return check_in;
    }

    public LocalDate getCheck_out() {
        return check_out;
    }

    public int getNumber_persons() {
        return number_persons;
    }

    public Room_Type getType() {
        return type;
    }

    public int getAdults() {
        return adults;
    }

    public int getChildren() {
        return children;
    }

    public double getChildren_description() {
        return children_description;
    }

    public boolean isPets() {
        return pets;
    }

    public int getNumber_of_pets() {
        return number_of_pets;
    }

    public double getPet_description() {
        return pet_description;
    }

    public double getPrice_per_night() {
        return price_per_night;
    }

    public boolean isHydromassage() {
        return hydromassage;
    }

    public boolean isRomantic_night() {
        return romantic_night;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public int getN_camas() {
        return n_camas;
    }


    public void setCheck_out_made(boolean check_out_made) {
        this.check_out_made = check_out_made;
    }

    public void setCheck_in_made(boolean check_in_made) {
        this.check_in_made = check_in_made;
    }

    public boolean isCheck_in_made() {
        return check_in_made;
    }

    public boolean isCheck_out_made() {
        return check_out_made;
    }

    @Override
    public String toString() {
return "\n***\nReserva nº " + getNumber() + "\nNome do Cliente: " + getName() + "\nPagamento Total: " + getTotal_price() +
        "\nNoites: " + getNights() + "\nEntrada: " + getCheck_in() + "\nSaída:" + getCheck_out() +
        "\nAdultos: " + getAdults() + "\n Children: " + getChildren() + "\nNº camas: " + getN_camas() +
        "\n" + quarto.toString() + "";
    }
}
