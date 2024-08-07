public enum Room_Type {
    SINGLE(45),
    DOUBLE(70),
    COUPLE(70),
    SUITE(100),
    ;

    private final int price;

    Room_Type(int price){
        this.price=price;
    }

    public int getPrice() {
        return price;
    }
}
