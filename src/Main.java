import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int option=1;
        int option_1=1;
        List<Quarto> roomList = new ArrayList<>();
        List<Reserva> reservas= new ArrayList<>();

        roomList= create_rooms();

        do{
            System.out.println(" - 1 - Abrir dia");
            System.out.println(" - 2 - Listar Quartos");
            System.out.println(" - 3 - Reservas");
            System.out.println(" - 4 - Análise de Contas");
            System.out.println(" - 5 - Fechar Dia");
            System.out.print("Qual a opção que deseja:  > ");
            option=sc.nextInt();

            switch (option) {
                case 1:
                    return;
                case 2:
                    ListarQuartos(roomList);
            }
            if (option==3) {
                do {
                    System.out.println(" - 0 - Menu");
                    System.out.println(" - 1 - Check-In");
                    System.out.println(" - 2 - Check-Out");
                    System.out.println(" - 3 - Nova Reserva");
                    System.out.println(" - 4 - Cancelar Reserva");
                    System.out.println(" - 5 - Dados Reserva");
                    System.out.println(" - 6 - Lista Reservas");
                    System.out.print("Qual a opção que deseja:  > ");
                    option_1 = sc.nextInt();

                    switch (option_1) {
                        case 0:
                            break;
                        case 1:
                            return;
                        case 2:
                            return;
                        case 3:
                            NovaReserva(roomList, reservas);
                            break;
                        case 6:
                            System.out.println("Hey");
                            reservas.toString();
                            break;
                    }

                } while (option_1 != 0);
            }
        }while(option!=0);
   }

    private static Quarto Verifica_Disponibilidade(List<Quarto> roomList, List<Reserva> reservas) {
        int count_occup=0;
        int quantidade=0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for(Quarto a:roomList)
            if(a.isOcupied())
                count_occup++;

        if(count_occup==roomList.size()) {
            System.out.println("Hotel Lotado!");
            return null;
        }
        else {
            System.out.println("Quantidade de Pessoas: ");
            quantidade = sc.nextInt();
            String s1= sc.nextLine();
            System.out.println("Data de Entrada (dd-MM-yyyy): ");
            String checkInStr = sc.nextLine();
            LocalDate check_in = LocalDate.parse(checkInStr, formatter);

            System.out.println("Data de Saída (dd-MM-yyyy): ");
            String checkOutStr = sc.nextLine();
            LocalDate check_out = LocalDate.parse(checkOutStr, formatter);


            for (Quarto quarto : roomList) {
                if (!quarto.isOcupied() && quarto.getCapacity() >= quantidade ) {
                    boolean disponivel=true;
                    for(Reserva r: reservas){
                        if(r.getQuarto().equals(quarto)){
                            if (!(check_out.isBefore(r.getCheck_in()) || check_in.isAfter(r.getCheck_out()))) {
                                disponivel = false;
                                break;
                            }
                        }
                    }
                    if(disponivel){
                        return quarto;
                    }else
                        return null;
                }
            }

        }
        return null;
    }

    private static void NovaReserva(List<Quarto> roomList, List<Reserva> reservas) {
        String reservation;
        Quarto a = Verifica_Disponibilidade(roomList,reservas);
        if (a == null)
            System.out.println("Nenhum Quarto Disponível");
        else {
            System.out.println("Quarto nº " + a.getNumber() + "  Livre e Disponível");
            String name="";

            Quarto reserva=RetornaQuarto(roomList,a.getNumber());
            sc.nextLine(); // Consume newline
            System.out.println("Deseja Realizar a Reserva? (s/n)");
            reservation= sc.nextLine();
            if(reservation.toUpperCase().equals("S")) {
               reservas= Reservar(reserva, reservas);
            }
            else
                return;
        }
    }

    private static List<Reserva> Reservar(Quarto reserva, List<Reserva> reservas) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date_in_1, date_out_1;

        System.out.println("Numero da Reserva: ");
        int reserva_n = sc.nextInt();
        System.out.println("Nome do Cliente: ");
        String nome = sc.nextLine();
        System.out.println("Nif: ");
        int nif = sc.nextInt();
        System.out.println("Número de Noites:");
        int noites = sc.nextInt();
        System.out.println("Data de Check-In (dd-mm-yy): ");
        String date_in = sc.nextLine();
        System.out.println("Data de Check-Out (dd-mm-yy): ");
        String date_out = sc.nextLine();

        try {
            date_in_1 = LocalDate.parse(date_in, formatter);
            System.out.println("Parsed date: " + date_in_1);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format");
            return reservas;
        }
        try {
            date_out_1 = LocalDate.parse(date_out, formatter);
            System.out.println("Parsed date: " + date_out_1);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format");
            return reservas;
        }
        System.out.println("Numero de Pessoas: ");
        int number_of_persons = sc.nextInt();

        System.out.println("Tipo de Quarto: ");
        System.out.println("/t SINGLE - 0");
        System.out.println("/t DUPLO - 1");
        System.out.println("/t CASAL - 2");
        System.out.println("/t SUITE - 3");

        int choice = sc.nextInt();
        Room_Type rm;
        if (choice == 1)
            rm = Room_Type.DOUBLE;
        else if (choice == 2)
            rm = Room_Type.COUPLE;
        else if (choice == 3)
            rm = Room_Type.SUITE;
        else
            rm = Room_Type.SINGLE;

        System.out.println("Numero de Adultos: ");
        int adultos = sc.nextInt();

        System.out.println("Número de Crianças: ");
        int criancas = sc.nextInt();

        double criancas_description;
        if (criancas > 0) {
            System.out.println("Descricao do preço das crianças:");
            criancas_description = sc.nextDouble();
        } else
            criancas_description = 0;

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
        String hidromassagem= sc.nextLine();

        double hidromassagem_val;
        boolean hidromassagem_bol;
        if(hidromassagem.equals("s")) {
            hidromassagem_bol = true;
            hidromassagem_val=30;
        }else{
            hidromassagem_bol = false;
            hidromassagem_val=0;
        }

        System.out.println("Romatica? (s/n)");
        String romantica= sc.nextLine();

        double romantica_val;
        boolean romantica_bol;
        if(romantica.equals("s")) {
            romantica_bol = true;
            romantica_val=15;
        }else{
            romantica_bol = false;
            romantica_val=0;
        }
        double price_per_night= ((reserva.getPrice_per_night()+criancas_description+price_animals + hidromassagem_val)*noites);



        reservas.add(new Reserva(1, nome, nif, noites, date_in_1, date_out_1, number_of_persons, rm, adultos, criancas, criancas_description, animais_valid, number_of_pets,price_animals,price_per_night,hidromassagem_bol,romantica_bol,reserva));
        reserva.setOcupied(true);
        reserva.setType(rm);
        reserva.setClean(false);
        return reservas;
        }

    private static Quarto RetornaQuarto(List<Quarto> roomList, int number) {
        for (Quarto a : roomList)
            if(a.getNumber()==number)
                return a;

        return null;
    }

    private static void ListarQuartos(List<Quarto> roomList) {
        System.out.println("**** Lista de Quartos ****");

        int count_occup=0;

        for(Quarto a:roomList)
            if(a.isOcupied())
                count_occup++;
        System.out.println("Neste Momento estão: " + count_occup + " quartos ocupados. ");

        for(Quarto b:roomList)
            System.out.println(b.toString());
    }

    private static List<Quarto> create_rooms() {
        List<Quarto> roomList = new ArrayList<>();
        roomList.add(new Quarto(1,2,true,true,true));
        roomList.add(new Quarto(2,2,true,true,false));
        roomList.add(new Quarto(3,2,true,true,false));
        roomList.add(new Quarto(4,2,true,true,true));
        roomList.add(new Quarto(5,4,true,true,true));
        roomList.add(new Quarto(6,4,true,true,true));
        roomList.add(new Quarto(7,6,true,true,true));
        roomList.add(new Quarto(8,6,true,true,true));
        roomList.add(new Quarto(9,8,true,true,true));
        roomList.add(new Quarto(10,8,true,true,true));
        return roomList;
    }
}