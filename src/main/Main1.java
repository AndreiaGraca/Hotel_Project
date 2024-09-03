package main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main1 {

    public static void main(String[] args) throws SQLException, IOException {
        Hotel hotel = new Hotel();
        hotel.setRoomList(null);
        int option = 1;
        int option_1 = 1;
        int option_2 = 1;
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println(" - 1 - Abrir dia");
            System.out.println(" - 2 - Listar Quartos");
            System.out.println(" - 3 - Reservas");
            System.out.println(" - 4 - Análise de Contas");
            System.out.println(" - 5 - Fechar Dia");
            System.out.println(" - 6 - Informações dos Quartos");
            System.out.print("Qual a opção que deseja:  > ");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    hotel.abrirDia();
                    break;
                case 2:
                    hotel.ListarQuartos();
                    break;
                case 3:
                    do {
                        System.out.println(" - 0 - Menu");
                        System.out.println(" - 1 - Check-In");
                        System.out.println(" - 2 - Check-Out");
                        System.out.println(" - 3 - Nova Reserva");
                        System.out.println(" - 4 - Cancelar Reserva");
                        System.out.println(" - 5 - Dados Reserva");
                        System.out.println(" - 6 - Lista Reservas");
                        System.out.println(" - 7 - Alterar Reserva");
                        System.out.print("Qual a opção que deseja:  > ");
                        option_1 = sc.nextInt();
                        //sc.nextLine();
                        switch (option_1) {
                            case 0:
                                break;
                            case 1:
                                hotel.Check_In();
                                break;
                            case 2:
                                hotel.Check_Out();
                                break;
                            case 3:
                                hotel.NovaReserva();
                                break;
                            case 4:
                                hotel.cancelReservation();
                                break;
                            case 5:
                                do {
                                    System.out.println(" - 0 - Menu");
                                    System.out.println(" - 1 - Search for a day");
                                    System.out.println(" - 2 - Search for a reservation");
                                    System.out.print("Qual a opção que deseja:  > ");
                                    option_2 = sc.nextInt();
                                    switch (option_1) {
                                        case 0:
                                            break;
                                        case 1:
                                             break;
                                        case 2:
                                            hotel.searchReservation();
                                            break;
                                    }
                                    } while (option_2 != 0) ;
                                break;
                                case 6:
                                            hotel.ListarReservas();
                                            break;
                            case 7:
                                hotel.AlterarReservas();
                                break;
                                    }
                    } while (option_1 != 0);
                    break;
                case 5:
                    hotel.FecharDia();
                    break;
                case 6:
                    hotel.change_Informations();
                    break;
            }
        } while (option != 0);
    }
}
