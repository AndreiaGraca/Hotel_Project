import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Hotel {
    List<Quarto> roomList = new ArrayList<>();
    List<Reserva> reservas= new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public void setRoomList(List<Quarto> roomList) {
        this.roomList = roomList;
    }

    public List<Quarto> create_rooms() {
        List<Quarto> roomList = new ArrayList<>();
        roomList.add(new Quarto(1,2,true,true,true));
        roomList.add(new Quarto(2,2,true,true,false));
        //roomList.add(new Quarto(3,2,true,true,false));
        //roomList.add(new Quarto(4,2,true,true,true));
        //roomList.add(new Quarto(5,4,true,true,true));
        //roomList.add(new Quarto(6,4,true,true,true));
        //roomList.add(new Quarto(7,6,true,true,true));
        //roomList.add(new Quarto(8,6,true,true,true));
        //roomList.add(new Quarto(9,8,true,true,true));
        //roomList.add(new Quarto(10,8,true,true,true));
        return roomList;
    }

    public void ListarQuartos() {
        System.out.println("\n**** Lista de Quartos ****");
        String quartos_ocupados="";
        int count_occup=0;
        for(Quarto a:roomList)
            if(a.isOcupied()) {
                count_occup++;
                quartos_ocupados= quartos_ocupados+ "  " + a.getNumber();
            }
        System.out.println("Neste Momento estão: " + count_occup + " quartos ocupados.\n");

        if(count_occup>0){
            System.out.println("Quartos Ocupados:  " + quartos_ocupados );
        }
        for(Quarto b:roomList)
                System.out.println(b.toString());
    }

    public void Check_In() {
        sc.nextLine();
        System.out.println("Qual o nome do Cliente: ");
        String client_name=sc.nextLine();
        boolean find=false;
        for(Reserva a:reservas){
            if(a.getName().equals(client_name)){
                find=true;
                a.setCheck_in_made(true);
                a.getQuarto().setOcupied(true);
                System.out.println("Quarto Nº: " + a.getQuarto().getNumber());
            }
        }

        if(!find)
            System.out.println("\nNão existe nenhuma reserva com esse nome! ");
        else
            System.out.println("\nCheck-In Realizado com Sucesso!");
    }

    public void Check_Out() {
        boolean find=false;

        System.out.println("Qual o nº do quarto: ");
        int room=sc.nextInt();

        for(Reserva a:reservas){
            if(a.getQuarto().getNumber()==room && a.isCheck_in_made()==true){
                a.setCheck_out_made(true);
                a.getQuarto().setClean(false);
                a.getQuarto().setOcupied(false);
                find=true;
            }
            else if(a.check_out_made==false){
                System.out.println("Check-In Não Realizado!");
                return;
            }
        }

        if(!find)
            System.out.println("\nNão existe nenhuma reserva para esse quarto! ");
        else
            System.out.println("\nCheck-Out Realizado com Sucesso!");
    }

    private static Quarto Verifica_Disponibilidade(List<Quarto> roomList, List<Reserva> reservas, int quantidade, LocalDate check_in, LocalDate check_out) {
        int count_occup=0;

        for(Quarto a:roomList)
            if(a.isOcupied())
                count_occup++;

        if(count_occup==roomList.size()) {
            System.out.println("Hotel Lotado!");
            return null;
        }
        else {

            for (Quarto quarto : roomList) {
                if (quarto.getCapacity() >= quantidade) {
                    boolean disponivel = true;
                    for (Reserva r : reservas) {
                        if (r.getQuarto().equals(quarto)) {
                            if (!(check_out.isBefore(r.getCheck_in()) || check_in.isAfter(r.getCheck_out()))) {
                                disponivel = false;
                                break;
                            }
                        }
                    }
                    if (disponivel) {
                        return quarto;


                    }

                }}
            return null;
        }
    }

    public void NovaReserva() {
        String reservation;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        System.out.println("Quantidade de Pessoas: ");
        int quantidade = sc.nextInt();
        String s1= sc.nextLine();
        LocalDate check_in = null;
        LocalDate check_out = null;
        LocalDate today = LocalDate.now();
        while (true) {
            try {
                System.out.println("Data de Entrada (dd-MM-yyyy): ");
                String checkInStr = sc.nextLine();
                check_in = LocalDate.parse(checkInStr, formatter);

                if (check_in.isBefore(today)) {
                    System.out.println("A data de entrada deve ser hoje ou posterior. Tente novamente.");
                    continue;
                }

                System.out.println("Data de Saída (dd-MM-yyyy): ");
                String checkOutStr = sc.nextLine();
                check_out = LocalDate.parse(checkOutStr, formatter);

                if (!check_out.isAfter(check_in)) {
                    System.out.println("A data de saída deve ser posterior à data de entrada. Tente novamente.");
                    continue;
                }

                break; // Se as datas forem válidas, sai do loop


            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Por favor, use o formato dd-MM-yyyy.");
            }
        }


        Quarto a = Verifica_Disponibilidade(roomList,reservas, quantidade, check_in,check_out);
        if (a == null)
            System.out.println("Nenhum Quarto Disponível");
        else {
            System.out.println("Quarto nº " + a.getNumber() + "  Livre e Disponível");
            String name="";

            Quarto reserva=RetornaQuarto(a.getNumber());
            System.out.println("Deseja Realizar a Reserva? (s/n)");
            reservation= sc.nextLine();
            if(reservation.toUpperCase().equals("S")) {
                reservas= Reservar(reserva, reservas, quantidade, check_in,check_out);

                System.out.println("RESERVAS\n\n\n");
                System.out.println( reservas.toString());

            }
            else
                return;
        }
    }

    public static List<Reserva> Reservar(Quarto reserva, List<Reserva> reservas, int quantidade, LocalDate check_in, LocalDate check_out) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        System.out.println("Nome do Cliente: ");
        String nome = sc.nextLine();
        System.out.println("Nif: ");
        int nif = sc.nextInt();
        sc.nextLine(); //consumir

        int noites = (int) ChronoUnit.DAYS.between(check_in, check_out);
        Room_Type rm;

        if (quantidade == 1) {
            rm = Room_Type.SINGLE;
            reserva.setType(Room_Type.SINGLE);
        } else {
            System.out.println("Tipo de Quarto: ");
            System.out.println("\t SINGLE - 0");
            System.out.println("\t DUPLO - 1");
            System.out.println("\t CASAL - 2");
            System.out.println("\t SUITE - 3");

            int choice = sc.nextInt();

            if (choice == 1) {
                rm = Room_Type.DOUBLE;
                reserva.setType(Room_Type.DOUBLE);
            } else if (choice == 2) {
                rm = Room_Type.COUPLE;
                reserva.setType(Room_Type.COUPLE);
            } else if (choice == 3) {
                rm = Room_Type.SUITE;
                reserva.setType(Room_Type.SUITE);
            } else {
                rm = Room_Type.SINGLE;
                reserva.setType(Room_Type.SINGLE);
            }
        }
        System.out.println("Número de Adultos: ");
        int adultos = sc.nextInt();

        System.out.println("Número de Crianças: ");
        int criancas = sc.nextInt();

        double criancas_description;
        if (criancas > 0) {
            System.out.println("Descricao do preço das crianças:");
            criancas_description = sc.nextDouble();
        } else
            criancas_description = 0;

        sc.nextLine();
        System.out.println("Animais (s/n): ");
        String animais = sc.nextLine();

        boolean animais_valid;
        if (animais.equals('s'))
            animais_valid = true;
        else
            animais_valid = false;

        int number_of_pets;
        double price_animals;
        if (animais_valid) {
            System.out.println("Numero de Animais: ");
            number_of_pets = sc.nextInt();
            System.out.println("Descrição dos animais - preço");
            price_animals = sc.nextDouble();
        } else {
            number_of_pets = 0;
            price_animals = 0;
        }

        System.out.println("Hidromassagem? (s/n)");
        String hidromassagem = sc.nextLine();

        double hidromassagem_val;
        boolean hidromassagem_bol;
        if (hidromassagem.equals("s")) {
            hidromassagem_bol = true;
            hidromassagem_val = 30;
        } else {
            hidromassagem_bol = false;
            hidromassagem_val = 0;
        }

        System.out.println("Romatica? (s/n)");
        String romantica = sc.nextLine();

        double romantica_val;
        boolean romantica_bol;
        if (romantica.equals("s")) {
            romantica_bol = true;
            romantica_val = 15;
        } else {
            romantica_bol = false;
            romantica_val = 0;
        }

        int price_adultos_mais = 0;

        if (adultos > 2) {
            int adultos_a_mais = adultos - 2;
            price_adultos_mais += 30 * adultos_a_mais;
        }

        int n_camas;

        if (adultos == 2 && rm.equals(Room_Type.COUPLE))
            n_camas = 1;
        else {
            System.out.println("Número de Camas:");
            n_camas = sc.nextInt();
        }

        double price_per_night = ((reserva.getPrice_per_night() + criancas_description + price_animals + hidromassagem_val + criancas_description + price_adultos_mais));
        reserva.setPrice_per_night(price_per_night);
        System.out.println("-----> " + price_per_night);
        //reserva.setOcupied(true);
        reserva.setType(rm);
        reserva.setClean(false);
        Reserva l = new Reserva(nome, nif, noites, check_in, check_out, quantidade, rm, adultos, criancas, criancas_description, animais_valid, number_of_pets, price_animals, price_per_night, hidromassagem_bol, romantica_bol, reserva, n_camas);
        l.setTotal_price((price_per_night * noites));
        reservas.add(l);
        return reservas;
    }

    public  Quarto RetornaQuarto(int number) {
        for (Quarto a : roomList)
            if(a.getNumber()==number)
                return a;

        return null;
    }

    public void ListarReservas() {
        System.out.println(reservas.toString());
    }

    public boolean chooses(String a){
        if(a.toUpperCase().equals('S'))
            return true;
        else
            return false;
    }

    public void change_Informations() {
        Room_Type rm;

        System.out.println("Número do Quarto: ");
        int roomNumber=sc.nextInt();
        Quarto a= RetornaQuarto(roomNumber);
        sc.nextLine();
        for(Reserva b: reservas){
            if(b.getQuarto().equals(a)){
                if(b.isCheck_out_made()){
                    System.out.println("Limpeza: (s/n):");
                    String option=sc.nextLine();
                    a.setClean(chooses(option));

                    System.out.println("Tipo de Quarto: ");
                    System.out.println("\t SINGLE - 0");
                    System.out.println("\t DUPLO - 1");
                    System.out.println("\t CASAL - 2");
                    System.out.println("\t SUITE - 3");

                    int choice = sc.nextInt();

                    if (choice == 1) {
                        rm = Room_Type.DOUBLE;
                        a.setType(Room_Type.DOUBLE);
                    } else if (choice == 2) {
                        rm = Room_Type.COUPLE;
                        a.setType(Room_Type.COUPLE);
                    } else if (choice == 3) {
                        rm = Room_Type.SUITE;
                        a.setType(Room_Type.SUITE);
                    } else {
                        rm = Room_Type.SINGLE;
                        a.setType(Room_Type.SINGLE);
                    }
                }
            }
        }
    }
}
