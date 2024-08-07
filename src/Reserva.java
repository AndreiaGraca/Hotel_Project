import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;

public class Reserva {
    int number;
    String name;
    int nif;
    int total_price;
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

    public Reserva(int number, String name, int nif, int nights, LocalDate check_in, LocalDate check_out, int number_persons, Room_Type type, int adults, int children, double children_description, boolean pets, int number_of_pets, double pet_description, double price_per_night, boolean hydromassage, boolean romantic_night, Quarto quarto) {
        this.number = number;
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

    public int getTotal_price() {
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


}
