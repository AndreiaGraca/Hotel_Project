package main;

import java.time.LocalDate;

public class Reserva {
    int number;
    String name;
    int nif;
    double total_Price;
    int nights;
    LocalDate check_In;
    LocalDate check_Out;
    int number_Persons;
    Room_Type type;
    int adults;
    int children;
    double children_Description; //idade das crianças
    boolean pets; //se trazem animais
    int number_of_Pets;
    double pet_Description; //peso do cão/gato
    double price_per_Night; //preço por noite do quarto
    boolean hydromassage; //se é com hidromassagem
    boolean romantic_Night; // se é noite romantica;
    Room room;
    int n_Beds;
    boolean check_In_Made, check_Out_Made;
    boolean paid;
    LocalDate reservation_Date;
    boolean canceled;
    String email;

    public Reserva(String name, int nif, int nights, LocalDate check_in, LocalDate check_out, int number_Persons, Room_Type type, int adults, int children, double children_description, boolean pets, int number_of_pets, double pet_description, double price_per_night, boolean hydromassage, boolean romantic_night, Room room, int n_camas, String email) {
        this.name = name;
        this.nif = nif;
        this.nights = nights;
        this.check_In = check_in;
        this.check_Out = check_out;
        this.number_Persons = number_Persons;
        this.type = type;
        this.adults = adults;
        this.children = children;
        this.children_Description = children_description;
        this.pets = pets;
        this.number_of_Pets = number_of_pets;
        this.pet_Description = pet_description;
        this.price_per_Night = price_per_night;
        this.hydromassage = hydromassage;
        this.romantic_Night = romantic_night;
        this.room = room;
        this.n_Beds = n_camas;
        this.check_In_Made =false;
        this.check_Out_Made =false;
        this.paid =false;
        this.reservation_Date = LocalDate.now();
        this.canceled=false;
        this.email=email;
    }

    public Reserva() {

    }


    public String getName() {
        return name;
    }

    public int getNif() {
        return nif;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public double getTotal_Price() {
        return total_Price;
    }

    public int getNights() {
        return nights;
    }

    public LocalDate getCheck_In() {
        return check_In;
    }

    public LocalDate getCheck_Out() {
        return check_Out;
    }

    public LocalDate getReservation_Date() {
        return reservation_Date;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public int getNumber_Persons() {
        return number_Persons;
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

    public double getChildren_Description() {
        return children_Description;
    }

    public boolean isPets() {
        return pets;
    }

    public int getNumber_of_Pets() {
        return number_of_Pets;
    }

    public double getPet_Description() {
        return pet_Description;
    }

    public double getPrice_per_Night() {
        return price_per_Night;
    }

    public boolean isHydromassage() {
        return hydromassage;
    }

    public boolean isRomantic_Night() {
        return romantic_Night;
    }

    public Room getQuarto() {
        return room;
    }

    public void setTotal_Price(double total_Price) {
        this.total_Price = total_Price;
    }

    public int getN_Beds() {
        return n_Beds;
    }


    public void setCheck_Out_Made(boolean check_Out_Made) {
        this.check_Out_Made = check_Out_Made;
    }

    public void setCheck_In_Made(boolean check_In_Made) {
        this.check_In_Made = check_In_Made;
    }

    public boolean isCheck_In_Made() {
        return check_In_Made;
    }

    public boolean isCheck_Out_Made() {
        return check_Out_Made;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNif(int nif) {
        this.nif = nif;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public void setCheck_In(LocalDate check_In) {
        this.check_In = check_In;
    }

    public void setCheck_Out(LocalDate check_Out) {
        this.check_Out = check_Out;
    }

    public void setNumber_Persons(int number_Persons) {
        this.number_Persons = number_Persons;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public void setChildren_Description(double children_Description) {
        this.children_Description = children_Description;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public void setNumber_of_Pets(int number_of_Pets) {
        this.number_of_Pets = number_of_Pets;
    }

    public void setPet_Description(double pet_Description) {
        this.pet_Description = pet_Description;
    }

    public void setPrice_per_Night(double price_per_Night) {
        this.price_per_Night = price_per_Night;
    }

    public void setHydromassage(boolean hydromassage) {
        this.hydromassage = hydromassage;
    }

    public void setRomantic_Night(boolean romantic_Night) {
        this.romantic_Night = romantic_Night;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setN_Beds(int n_Beds) {
        this.n_Beds = n_Beds;
    }

    public void setReservation_Date(LocalDate reservation_Date) {
        this.reservation_Date = reservation_Date;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public int getNumber() {
        return number;
    }



    @Override
    public String toString() {
        return "\n*** Reserve Number: " + getNumber() + " ***" +"\n\tNClient Name: " + getName() + "\n\tIs Canceled: " + isCanceled() + "\n\tTotal Price: " + getTotal_Price() + "\n\tPrice per Night: " + getPrice_per_Night() +
                "\n\tNights: " + getNights() + "\n\tCheck-In: " + getCheck_In() + "\n\tCheck-Out:" + getCheck_Out() + "\n\tIs Paid: " + isPaid() +
                "\n\tAdults: " + getAdults() + "\n\tChildren: " + getChildren() + "\n\tPets: " + getNumber_of_Pets() +  " -- Price: " + getPet_Description()+
                "\n\tNNumber of Beds: " + getN_Beds() + "\n\tHydromassage: " + isHydromassage() + "\n\tRomantic: " + isRomantic_Night() +
                "\n\t\t" + room.toString() + "\n\n" + "Check-In: " + isCheck_In_Made() + "\nCheck-Out: " + isCheck_Out_Made() + "\n";
    }

    public void setQuarto(Room quarto) {
        room=quarto;
    }

    public void setType(Room_Type type) {
        this.type=type;
    }
}
