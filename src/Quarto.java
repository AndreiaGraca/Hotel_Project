import java.time.LocalDate;

public class Quarto {
    boolean ocupied;
    int number; //numero do quarto
    int capacity;//numero de pessoas que podem estar
    Room_Type type; //tipo do quarto: casal, duplo
    boolean balcony; //se tem varanda
    double price_per_night; //preço base
    boolean clean;
    boolean hydromassage;

    public Quarto(int number, int capacity, boolean balcony, boolean clean, boolean hydromassage) {
        this.ocupied = false;
        this.number = number;
        this.capacity = capacity;
        this.type = Room_Type.SINGLE;
        this.balcony = balcony;
        this.price_per_night = Room_Type.SINGLE.getPrice();
        this.clean = true;
        this.hydromassage = hydromassage;
    }

    public boolean isOcupied() {
        return ocupied;
    }

    public int getNumber() {
        return number;
    }

    public int getCapacity() {
        return capacity;
    }

    public Room_Type getType() {
        return type;
    }

    public boolean isBalcony() {
        return balcony;
    }

    public double getPrice_per_night() {
        return type.getPrice();
    }

    public boolean isClean() {
        return clean;
    }

    public boolean isHydromassage() {
        return hydromassage;
    }

    public void setOcupied(boolean ocupied) {
        this.ocupied = ocupied;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setType(Room_Type type) {
        this.type = type;
    }

    public void setPrice_per_night(double price_per_night) {
        this.price_per_night = price_per_night;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public void setHydromassage(boolean hydromassage) {
        this.hydromassage = hydromassage;
    }

    public String chooses(boolean a){
        if(a)
            return "Sim";
        else
            return "Não";
    }


    @Override
    public String toString() {
        return "O Quarto Nº " + getNumber() + " está ocupado: " + chooses(isOcupied()) + "\n\tTem a capacidade (pessoas): " + getCapacity()  +
                "\n\tO quarto está: " + getType() + "\n\tVaranda: " + chooses(isBalcony())
                + "\n\tPreço por Noite:" + getPrice_per_night() +
                "\n\tLimpeza: " + chooses(isClean()) +
                "\n\tHidromassagem:" + chooses(isHydromassage()) + "\n";
    }
}

