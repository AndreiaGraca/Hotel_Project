package main;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//separar o código das funções noutras funções mais curtas, mais legíveis.
//atribuindo uma unica responsabilidade a cada função
//não esquecer que auxilia na realização de testes unitários

public class Hotel {
    List<Room> roomList = new ArrayList<>();
    List<Reserva> reservations = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public void setRoomList(List<Room> roomList) {
        if (roomList == null)
            roomList = create_rooms();

        this.roomList = roomList;
    }

    protected List<Room> create_rooms() {
        List<Room> roomList = new ArrayList<>();
        roomList.add(new Room(1, 2, true, true, true));
        roomList.add(new Room(2, 2, true, true, false));
        roomList.add(new Room(3, 2, true, true, false));
        roomList.add(new Room(4, 2, true, true, true));
        roomList.add(new Room(5, 4, true, true, true));
        roomList.add(new Room(6, 4, true, true, true));
        roomList.add(new Room(7, 6, true, true, true));
        roomList.add(new Room(8, 6, true, true, true));
        roomList.add(new Room(9, 8, true, true, true));
        roomList.add(new Room(10, 8, true, true, true));
        return roomList;
    }

    public void ListarQuartos() {
        System.out.println("\n**** Room List ****");
        StringBuilder quartos_ocupados = new StringBuilder();
        int count_occup = 0;
        for (Room a : roomList)
            if (a.isOcupied()) {
                count_occup++;
                quartos_ocupados.append("  ").append(a.getNumber());
            }
        System.out.println("There are : " + count_occup + " rooms occupied.\n");

        if (count_occup > 0) {
            System.out.println("Rooms Occupied:  " + quartos_ocupados);
        }
        for (Room b : roomList)
            System.out.println(b.toString());
    }

    public void Check_In() {
        System.out.println("Client Name: ");
        String client_name = sc.nextLine();
        Reserva reservation = findReservation(client_name);

       if(reservation==null){
           System.out.println("There are no reservations in that name");
           return;
       }
       reservation.setCheck_In_Made(true);
       atualizarReserva(reservation);
       reservation.getQuarto().setOcupied(true);

       System.out.println("\nCheck-In Successful!");
    }

    private Reserva findReservation(String client_name) {
        for (Reserva reserve : reservations) {
            if (reserve.getName().equals(client_name)) {
                return reserve;
            }
        }
        return null;
    }

    public void Check_Out() {
        System.out.println("Room Number: ");
        int roomNumber = sc.nextInt();

        Reserva reservation = findReservation(roomNumber);

        if (reservation == null) {
            System.out.println("There are no reservations for this room. \n");
            return;
        } else if (!reservation.isCheck_In_Made()) {
            System.out.println("Check-In was not done! \n");
            sc.nextLine();
            return;
        }

        if (reservation.isPaid()) {
            processCheckOut(reservation);
        } else {
            handlePayment(reservation);
        }
        invoicing(reservation);
    }

    private void invoicing(Reserva reservation) {
        //createDocument();
        sendEmail(reservation);

    }

    private void sendEmail(Reserva reservation) {
        String emailText= reservation.toString();
        EmailSender.sendEmail(reservation.getEmail(), "Confirmação de Checkout", emailText);
    }

    private void processCheckOut(Reserva reservation) {
        reservation.setCheck_Out_Made(true);
        reservation.getQuarto().setClean(false);
        reservation.getQuarto().setOcupied(false);
        atualizarReserva(reservation);
        System.out.println("\nCheck-Out Sucessfull!");
    }


    private void handlePayment(Reserva reservation) {
        double price=reservation.getTotal_Price();
        sc.nextLine();
        System.out.println("Payment Required! Amount: " + price);
        System.out.println("Paid? (y/n)");
        String paymentResponse = sc.nextLine();

        if (paymentResponse.equalsIgnoreCase("Y")) {
            reservation.setPaid(true);
            processCheckOut(reservation);
            System.out.println("Payment Made!");
        } else {
            reservation.getQuarto().setClean(false);
            reservation.getQuarto().setOcupied(true);
            System.out.println("Payment not made. Room kept as occupied.");
        }
    }

    private Reserva findReservation(int roomNumber) {
        for(Reserva reserve: reservations){
            if(reserve.getQuarto().getNumber()==(roomNumber)){
                return reserve;
            }
        }
        return null;
    }


    private Room_Type solicitarTipoDeQuarto() {
        System.out.println("Tipo de Quarto: ");
        System.out.println("\t SINGLE - 0");
        System.out.println("\t DOUBLE - 1");
        System.out.println("\t COUPLE - 2");
        System.out.println("\t SUITE - 3");

        int escolha = sc.nextInt();
        sc.nextLine(); // Consome o newline

        return switch (escolha) {
            case 1 -> Room_Type.DOUBLE;
            case 2 -> Room_Type.COUPLE;
            case 3 -> Room_Type.SUITE;
            default -> Room_Type.SINGLE;
        };
    }

    private static Room checkRoomAvailability(List<Room> roomList, List<Reserva> reservas, int requiredCapacity, LocalDate check_in, LocalDate check_out) {
        for(Room room: roomList){
            if(room.getCapacity() >= requiredCapacity && !isRoomOccupied(room,reservas,check_in, check_out)){
                return room;
            }
        }
        System.out.println("No Available Rooms Matching the Criteria");
        return null;
    }

    private static boolean isRoomOccupied(Room room, List<Reserva> reservas, LocalDate checkIn, LocalDate checkOut) {
        for (Reserva reserva : reservas) {
            if (reserva.getQuarto().equals(room) && !reserva.isCheck_Out_Made() && !reserva.isCanceled()) {
                if (!(checkOut.isBefore(reserva.getCheck_In()) || checkIn.isAfter(reserva.getCheck_Out()))) {
                    return true;
                }
            }
        }
        return false;
    }


    public void NovaReserva() throws SQLException {
        int quantity = NumberOfPersons();
        LocalDate checkIn = RequestDate("Check-In (dd-MM-yyyy): ");
        LocalDate checkOut = RequestDate("Check-Out (dd-MM-yyyy): ", checkIn);


        Room room = checkRoomAvailability(roomList, reservations, quantity, checkIn, checkOut);

        if (room == null) {
            System.out.println("No rooms Available");
            return;
        }
        System.out.println("Room number " + room.getNumber() + " available");

        if (Confirmation()) {
            reservations = Reservar(room, reservations, quantity, checkIn, checkOut);
        }
    }

    private boolean Confirmation() {
        System.out.println("Do you want to make a reservation? (y/n): ");
        String resposta = sc.next();
        return resposta.equalsIgnoreCase("Y");
    }

    private LocalDate RequestDate(String message) {
        return RequestDate(message, null);
    }

    private LocalDate RequestDate(String mesage, LocalDate dataMinima) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate today = LocalDate.now();
        while (true) {
            try {
                System.out.println(mesage);
                String dataStr = sc.next();
                LocalDate data = LocalDate.parse(dataStr, formatter);

                if (dataMinima != null && !data.isAfter(dataMinima)) {
                    System.out.println("The departure date must be later than the arrival date. Please try again.");
                    continue;
                } else if (dataMinima == null && data.isBefore(today)) {
                    System.out.println("The entry date must be today or later. Please try again.");
                    continue;
                }

                return data;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use the format dd-MM-yyyy");
            }
        }
    }

    private int NumberOfPersons() {
        System.out.println("Number of Persons: ");
        return sc.nextInt();
    }

    public List<Reserva> Reservar(Room reserva, List<Reserva> reservas, int quantidade, LocalDate check_in, LocalDate check_out) throws SQLException {
        String nomeCliente = requestClientName(reservas);
        int nif = requestNif();
        int nights = nightsCalculator(check_in, check_out);
        Room_Type roomType = SelectRoomType(quantidade);
        reserva.setType(roomType);

        int adultos = solicitarNumeroDeAdultos();
        int criancas = solicitarNumeroDeCriancas();
        double precoCriancas = solicitarPrecoCriancas(criancas);

        if (validarQuantidadePessoas(quantidade, adultos, criancas)) {
            System.out.println("Quantidade de pessoas não corresponde!");
            return reservas;
        }

        boolean animaisValid = solicitarPresencaDeAnimais();
        int numeroDeAnimais = 0;
        double precoAnimais = 0;

        if (animaisValid) {
            numeroDeAnimais = solicitarNumeroDeAnimais();
            precoAnimais = solicitarPrecoAnimais();
        }

       // sc.nextLine();  // Limpar o buffer do scanner

        boolean hidromassagem = solicitarHidromassagem();
        double precoHidromassagem = hidromassagem ? 30 : 0;
        reserva.setHydromassage(hidromassagem);

        boolean romantico = solicitarPacoteRomantico();
        double precoRomantico = romantico ? 15 : 0;

        int numeroCamas = solicitarNumeroDeCamas(roomType, adultos);

        double precoPorNoite = calcularPrecoPorNoite(reserva, precoCriancas, precoAnimais, precoHidromassagem, precoRomantico, adultos);
        reserva.setPrice_per_night(precoPorNoite);

        String email=GetEmail();
        Reserva novaReserva = criarReserva(nomeCliente, nif, nights, check_in, check_out, quantidade, roomType, adultos, criancas, precoCriancas, animaisValid, numeroDeAnimais, precoAnimais, precoPorNoite, hidromassagem, romantico, reserva, numeroCamas,email);

        int idReserva = AddBdReservation(novaReserva);
        novaReserva.setNumber(idReserva);
        reservas.add(novaReserva);

        exibirResumoDaReserva(novaReserva);

        return reservas;
    }

    private String GetEmail() {
        String email;

        System.out.println("Client Email: ");
        email=sc.nextLine();

        return email;
    }


    private String requestClientName(List<Reserva> reservas) {
        sc.nextLine();
        String nome;
        while (true) {
            System.out.println("Nome do Cliente: ");
            nome = sc.nextLine();

            if (isNomeDuplicado(reservas, nome)) {
                System.out.println("Nome já reservado. Por favor, escolha outro nome.");
            } else {
                break;
            }
        }
        return nome;
    }

    private int requestNif() {
        System.out.print("Nif: ");
        return sc.nextInt();
    }

    private int nightsCalculator(LocalDate check_in, LocalDate check_out) {
        return (int) ChronoUnit.DAYS.between(check_in, check_out);
    }

    private Room_Type SelectRoomType(int quantidade) {
        if (quantidade == 1) {
            return Room_Type.SINGLE;
        }else{
            return solicitarTipoDeQuarto();
        }
    }

    private int solicitarNumeroDeAdultos() {
        System.out.println("Número de Adultos: ");
        return sc.nextInt();
    }

    private int solicitarNumeroDeCriancas() {
        System.out.println("Número de Crianças: ");
        return sc.nextInt();
    }

    private double solicitarPrecoCriancas(int criancas) {
        if (criancas > 0) {
            System.out.println("Descrição do preço das crianças:");
            return sc.nextDouble();
        }
        return 0;
    }
    private boolean validarQuantidadePessoas(int quantidade, int adultos, int criancas) {
        return adultos + criancas != quantidade;
    }

    private boolean solicitarPresencaDeAnimais() {
        sc.nextLine();
        System.out.println("Animais (s/n): ");
        String resposta = sc.nextLine();
                return resposta.equalsIgnoreCase("S");
    }

    private int solicitarNumeroDeAnimais() {
        System.out.println("Número de Animais: ");
        return sc.nextInt();
    }

    private double solicitarPrecoAnimais() {
        System.out.println("Descrição dos animais - preço");
        double price= sc.nextDouble();
        sc.nextLine();
        return price;
    }

    private boolean solicitarHidromassagem() {
        System.out.println("Hidromassagem? (s/n)");
        String resposta = sc.nextLine();
        return resposta.equalsIgnoreCase("S");
    }

    private boolean solicitarPacoteRomantico() {
        System.out.println("Romântica? (s/n)");
        String resposta = sc.nextLine();
        return resposta.equalsIgnoreCase("S");
    }

    private int solicitarNumeroDeCamas(Room_Type tipoQuarto, int adultos) {
        if (adultos == 2 && tipoQuarto.equals(Room_Type.COUPLE)) {
            return 1;
        }

        System.out.println("Número de Camas:");
        return sc.nextInt();
    }

    private double calcularPrecoPorNoite(Room reserva,double precoCriancas, double precoAnimais, double precoHidromassagem, double precoRomantico, int adultos) {
        double precoAdultosAdicionais = 0;

        if (adultos > 2) {
            precoAdultosAdicionais += 30 * (adultos - 2);
        }

        return reserva.getType().getPrice() + precoCriancas + precoAnimais + precoHidromassagem + precoAdultosAdicionais + precoRomantico;
    }

//    private void atualizarRoomListComPreco(List<main.Room> roomList, main.Room reserva, double precoPorNoite) {
//        for (main.Room room : roomList) {
//            if (room.getNumber() == reserva.getNumber()) {
//                room.setPrice_per_night(precoPorNoite);
//                room.setType(reserva.getType());
//                room.setHydromassage(reserva.isHydromassage());
//            }
//        }
//    }


    private Reserva criarReserva(String nome, int nif, int noites, LocalDate check_in, LocalDate check_out, int quantidade, Room_Type tipoQuarto, int adultos, int criancas, double precoCriancas, boolean animaisValid, int numeroDeAnimais, double precoAnimais, double precoPorNoite, boolean hidromassagem, boolean romantico, Room reserva, int numeroCamas, String email) {
        Reserva novaReserva = new Reserva(nome, nif, noites, check_in, check_out, quantidade, tipoQuarto, adultos, criancas, precoCriancas, animaisValid, numeroDeAnimais, precoAnimais, precoPorNoite, hidromassagem, romantico, reserva, numeroCamas,email);
        novaReserva.setTotal_Price(precoPorNoite * noites);
        return novaReserva;
    }

    private void exibirResumoDaReserva(Reserva reserva) {
        System.out.println("Resumo da Reserva: ");
        System.out.println(reserva);
    }

    private boolean isNomeDuplicado(List<Reserva> reservas, String name) {
        for (Reserva reserva : reservas) {
            if (reserva.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private int AddBdReservation(Reserva reserva) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "andreia";

        String sql = "INSERT INTO reserve (name, nif, email, total_price, nights, check_in, check_out, number_persons, type, " +
                "adults, children, children_description, pets, number_of_pets, pet_description, price_per_night, " +
                "hydromassage, romantic_night, n_room, n_beds, check_in_made, check_out_made, paid, reservation_date, " +
                "occupied, capacity, balcony, clean, canceled) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING reserva_id"; // Assumindo que a coluna id é a chave primária da tabela

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Defina todos os parâmetros do PreparedStatement
            stmt.setString(1, reserva.getName());
            stmt.setInt(2, reserva.getNif());
            stmt.setString(3,reserva.getEmail());
            stmt.setDouble(4, reserva.getTotal_Price());
            stmt.setInt(5, reserva.getNights());
            stmt.setDate(6, java.sql.Date.valueOf(reserva.getCheck_In()));
            stmt.setDate(7, java.sql.Date.valueOf(reserva.getCheck_Out()));
            stmt.setInt(8, reserva.getNumber_Persons());
            stmt.setString(9, String.valueOf(reserva.getType()));
            stmt.setInt(10, reserva.getAdults());
            stmt.setInt(11, reserva.getChildren());
            stmt.setDouble(12, reserva.getChildren_Description());
            stmt.setBoolean(13, reserva.isPets());
            stmt.setInt(14, reserva.getNumber_of_Pets());
            stmt.setDouble(15, reserva.getPet_Description());
            stmt.setDouble(16, reserva.getPrice_per_Night());
            stmt.setBoolean(17, reserva.isHydromassage());
            stmt.setBoolean(18, reserva.isRomantic_Night());
            stmt.setInt(19, reserva.getQuarto().getNumber());
            stmt.setInt(20, reserva.getN_Beds());
            stmt.setBoolean(21, reserva.isCheck_In_Made());
            stmt.setBoolean(22, reserva.isCheck_Out_Made());
            stmt.setBoolean(23, reserva.isPaid());
            stmt.setDate(24, java.sql.Date.valueOf(reserva.getReservation_Date()));
            stmt.setBoolean(25, reserva.getQuarto().isOcupied());
            stmt.setInt(26, reserva.getQuarto().getCapacity());
            stmt.setBoolean(27, reserva.getQuarto().isBalcony());
            stmt.setBoolean(28, reserva.getQuarto().isClean());
            stmt.setBoolean(29, reserva.isCanceled());

            // Execute the query and retrieve the generated key
            try (ResultSet generatedKeys = stmt.executeQuery()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Assuming the ID column is the first column in the result set
                } else {
                    throw new SQLException("Failed to obtain ID for the inserted reservation.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw exception to handle it further up the call stack
        }
    }


    public Room RetornaQuarto(int number) {
        for (Room a : roomList)
            if (a.getNumber() == number)
                return a;

        return null;
    }

    public void ListarReservas() {
        System.out.println(reservations.toString());
    }

    private void exibirMenuDeOpcoes() {
        System.out.println("Alterações: ");
        System.out.println("   0 - Menu");
        System.out.println("   1 - Pagamento");
        System.out.println("   2 - Limpeza");
        System.out.println("   3 - Tipo de Quarto");
        System.out.println("   4 - Informação da reserva");
        System.out.print("Opção: > ");
    }


    public void change_Informations() {
        System.out.println("Número do Quarto: ");
        int roomNumber = sc.nextInt();
        sc.nextLine(); // Consome o newline

        Room room = RetornaQuarto(roomNumber);
        if (room == null) {
            System.out.println("Quarto não encontrado!");
            return;
        }

        Reserva reserva = findReservation(roomNumber);
        if (reserva == null) {
            System.out.println("Reserva não encontrada!");
            return;
        }

        int option;
        do {
            exibirMenuDeOpcoes();
            option = sc.nextInt();
            sc.nextLine(); // Consome o newline

            switch (option) {
                case 1:
                    alterarPagamento(reserva);
                    break;
                case 2:
                    alterarLimpeza(room, reserva);
                    break;
                case 3:
                    alterarTipoDeQuarto(room, reserva);
                    break;
                case 4:
                    exibirResumoDaReserva(reserva);
                    break;
                case 0:
                    System.out.println("Saindo do menu...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (option != 0);
    }

    private void alterarPagamento(Reserva reserva) {
        System.out.println("Pagamento (s/n): ");
        String pagamento = sc.nextLine();
        reserva.setPaid(pagamento.equalsIgnoreCase("S"));
    }

    private void alterarLimpeza(Room room, Reserva reserva) {
        if (reserva.isCheck_Out_Made()) {
            System.out.println("Limpeza (s/n): ");
            String limpeza = sc.nextLine();
            room.setClean(limpeza.equalsIgnoreCase("S"));
        } else {
            System.out.println("Check-Out Não Foi Feito!");
        }
    }

    private void alterarTipoDeQuarto(Room room, Reserva reserva) {
        if (reserva.isCheck_Out_Made()) {
            room.setType(solicitarTipoDeQuarto());
        }else
            System.out.println("Check out nao foi feito!");
    }


    public void FecharDia() {

    }


    public void abrirDia() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "andreia";
        String sql = " SELECT * \n" +
                "FROM reserve \n" +
                "WHERE check_in = CURRENT_DATE \n" +
                "   AND check_out > CURRENT_DATE and canceled=False; ";
        reservations.clear();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reserva reserve = new Reserva();
                reserve.setNumber(rs.getInt("reserva_id"));
                reserve.setName(rs.getString("name"));
                reserve.setEmail(rs.getString("email"));
                reserve.setNif(rs.getInt("nif"));
                reserve.setTotal_Price(rs.getDouble("total_price"));
                reserve.setNights(rs.getInt("nights"));
                reserve.check_In = rs.getDate("check_in").toLocalDate();
                reserve.check_Out = rs.getDate("check_out").toLocalDate();
                reserve.setNumber_Persons(rs.getInt("number_persons"));
                String typeString = rs.getString("type");
                Room_Type type = Room_Type.valueOf(typeString.toUpperCase()); // Convert the text from the database to the enum
                reserve.setType(type);
                reserve.setAdults(rs.getInt("adults"));
                reserve.setChildren(rs.getInt("children"));
                reserve.setChildren_Description(rs.getDouble("children_description"));
                reserve.setPets(rs.getBoolean("pets"));
                reserve.setNumber_of_Pets(rs.getInt("number_of_pets"));
                reserve.setPet_Description(rs.getDouble("pet_description"));
                reserve.setPrice_per_Night(rs.getDouble("price_per_night"));
                reserve.setHydromassage(rs.getBoolean("hydromassage"));
                reserve.setRomantic_Night(rs.getBoolean("romantic_night"));
                reserve.setQuarto(new Room(rs.getInt("n_room"), rs.getInt("capacity"), rs.getBoolean("balcony"), rs.getBoolean("clean"), rs.getBoolean("hydromassage")));
                reserve.setN_Beds(rs.getInt("n_beds"));
                reserve.getQuarto().setType(type);
                reserve.setCheck_In_Made(rs.getBoolean("check_in_made"));
                reserve.setCheck_Out_Made(rs.getBoolean("check_out_made"));
                reserve.setPaid(rs.getBoolean("paid"));
                reserve.reservation_Date=rs.getDate("reservation_date").toLocalDate();
                reserve.setCanceled(rs.getBoolean("canceled"));
                reservations.add(reserve);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelReservation() throws SQLException {
        System.out.println("Nome da Reserva: ");
        String name=requestClientName(reservations);
        LocalDate check_in= RequestDate("");
        LocalDate check_out=RequestDate("");
        Reserva a = searchDB(name,check_in,check_out);
        a.setCanceled(true);
        atualizarReserva(a);
    }


    public void atualizarReserva(Reserva novaReserva) {
        String url = "jdbc:postgresql://localhost:5432/postgres"; // URL do banco
        String user = "postgres"; // Usuário do banco
        String password = "andreia"; // Senha do banco

        // SQL para atualizar a reserva com base no reserva_id
        String sql = "UPDATE reserve SET name = ?, email=?, nif = ?, total_price = ?, nights = ?, check_in = ?, check_out = ?, "
                + "number_persons = ?, type = ?, adults = ?, children = ?, children_description = ?, pets = ?, "
                + "number_of_pets = ?, pet_description = ?, price_per_night = ?, hydromassage = ?, romantic_night = ?, "
                + "n_room = ?, n_beds = ?, check_in_made = ?, check_out_made = ?, paid = ?, reservation_date = ?, "
                + "occupied = ?, capacity = ?, balcony = ?, clean = ?, canceled = ? WHERE reserva_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros do PreparedStatement
            stmt.setString(1, novaReserva.getName());
            stmt.setString(2, novaReserva.getEmail());
            stmt.setInt(3, novaReserva.getNif());
            stmt.setDouble(4, novaReserva.getTotal_Price());
            stmt.setInt(5, novaReserva.getNights());
            stmt.setObject(6, novaReserva.getCheck_In());
            stmt.setObject(7, novaReserva.getCheck_Out());
            stmt.setInt(8, novaReserva.getNumber_Persons());
            stmt.setString(9, String.valueOf(novaReserva.getType()));
            stmt.setInt(10, novaReserva.getAdults());
            stmt.setInt(11, novaReserva.getChildren());
            stmt.setDouble(12, novaReserva.getChildren_Description());
            stmt.setBoolean(13, novaReserva.isPets());
            stmt.setInt(14, novaReserva.getNumber_of_Pets());
            stmt.setDouble(15, novaReserva.getPet_Description());
            stmt.setDouble(16, novaReserva.getPrice_per_Night());
            stmt.setBoolean(17, novaReserva.isHydromassage());
            stmt.setBoolean(18, novaReserva.isRomantic_Night());
            stmt.setInt(19, novaReserva.getQuarto().getNumber());
            stmt.setInt(20, novaReserva.getN_Beds());
            stmt.setBoolean(21, novaReserva.isCheck_In_Made());
            stmt.setBoolean(22, novaReserva.isCheck_Out_Made());
            stmt.setBoolean(23, novaReserva.isPaid());
            stmt.setObject(24, novaReserva.getReservation_Date());
            stmt.setBoolean(25, novaReserva.getQuarto().isOcupied());
            stmt.setInt(26, novaReserva.getQuarto().getCapacity());
            stmt.setBoolean(27, novaReserva.getQuarto().isBalcony());
            stmt.setBoolean(28, novaReserva.getQuarto().isClean());
            stmt.setBoolean(29, novaReserva.isCanceled());
            stmt.setInt(30, novaReserva.getNumber());

            // Executa a atualização
            int rowsUpdated = stmt.executeUpdate();

            // Verifica se a atualização foi bem-sucedida
            if (rowsUpdated > 0) {
                System.out.println("Reserva atualizada com sucesso.");
            } else {
                System.out.println("Nenhuma reserva encontrada com o ID especificado.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    public void cancelReservationBD(String name, LocalDate checkInDate) throws SQLException {
//
//        String url = "jdbc:postgresql://localhost:5432/postgres"; // URL do banco
//        String user = "postgres"; // Usuário do banco
//        String password = "andreia"; // Senha do banco
//
//        // SQL para atualizar o campo 'canceled' para true
//        String sql = "UPDATE reserve SET canceled = TRUE WHERE name = ? AND check_in = ?";
//
//        try (Connection conn = DriverManager.getConnection(url, user, password);
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            // Define os parâmetros do PreparedStatement
//            stmt.setString(1, name);
//            stmt.setDate(2, java.sql.Date.valueOf(checkInDate));
//
//            // Executa a atualização
//            int rowsAffected = stmt.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("main.Reserva cancelada com sucesso!");
//            } else {
//                System.out.println("Nenhuma reserva encontrada com o nome e data de check-in fornecidos.");
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e; // Re-throw exception to handle it further up the call stack
//        }
//        abrirDia();//para dar refresh nos dados da lista dos quartos
//    }

    public Reserva searchReservation() throws SQLException {
        System.out.println("Nome da Reserva: ");
        String name=sc.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("Check-In: ");
        String checkInStr = sc.nextLine();
        LocalDate check_in = LocalDate.parse(checkInStr, formatter);
        System.out.println("Check-Out: ");
        String checkOutStr = sc.nextLine();
        LocalDate check_out = LocalDate.parse(checkOutStr, formatter);

        return searchDB(name,check_in,check_out);
    }

    private Reserva searchDB(String name, LocalDate check_in, LocalDate check_out) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres"; // URL do banco
        String user = "postgres"; // Usuário do banco
        String password = "andreia"; // Senha do banco

        // SQL para selecionar a reserva com base no nome e na data de check-in
        String sql = "SELECT reserva_id, name,email, total_price, nights, check_in, check_out, paid, adults, children, " +
                "number_of_pets, pet_description, price_per_night, hydromassage, romantic_night, " +
                "n_room, n_beds, canceled, occupied, capacity, type, balcony, clean " +
                "FROM reserve WHERE name = ? AND check_in = ? AND check_out=?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros do PreparedStatement
            stmt.setString(1, name);
            stmt.setDate(2, java.sql.Date.valueOf(check_in));
            stmt.setDate(3, java.sql.Date.valueOf(check_out));

            // Executa a consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Recupera os dados
                    int id = rs.getInt("reserva_id");
                    String reservaName = rs.getString("name");
                    String email=rs.getString("email");
                    double totalPrice = rs.getDouble("total_price");
                    int nights = rs.getInt("nights");
                    LocalDate checkIn = rs.getDate("check_in").toLocalDate();
                    LocalDate checkOut = rs.getDate("check_out").toLocalDate();
                    boolean paid = rs.getBoolean("paid");
                    int adults = rs.getInt("adults");
                    int children = rs.getInt("children");
                    int numberOfPets = rs.getInt("number_of_pets");
                    double petDescription = rs.getDouble("pet_description");
                    double pricePerNight = rs.getDouble("price_per_night");
                    boolean hydromassage = rs.getBoolean("hydromassage");
                    boolean romanticNight = rs.getBoolean("romantic_night");
                    int roomNumber = rs.getInt("n_room");
                    int numberOfBeds = rs.getInt("n_beds");
                    boolean canceled = rs.getBoolean("canceled");
                    boolean occupied = rs.getBoolean("occupied");
                    int capacity = rs.getInt("capacity");
                    String type = rs.getString("type");
                    boolean balcony = rs.getBoolean("balcony");
                    boolean clean = rs.getBoolean("clean");

                    // Formata a saída
                    System.out.println("*** Reserve Number: " + id + " ***");
                    System.out.println("    Client Name: " + reservaName);
                    System.out.println("    Email: " + email);
                    System.out.println("    Is Canceled: " + canceled);
                    System.out.println("    Total Price: " + totalPrice);
                    System.out.println("    Nights: " + nights);
                    System.out.println("    Check-In: " + checkIn);
                    System.out.println("    Check-Out: " + checkOut);
                    System.out.println("    Is Paid: " + paid);
                    System.out.println("    Adults: " + adults);
                    System.out.println("    Children: " + children);
                    System.out.println("    Pets: " + numberOfPets + " -- Price: " + petDescription);
                    System.out.println("    Number of Beds: " + numberOfBeds);
                    System.out.println("    Hydromassage: " + hydromassage);
                    System.out.println("    Romantic: " + romanticNight);
                    System.out.println("        Room Number: " + roomNumber);
                    System.out.println("        Occupied: " + (occupied ? "Yes" : "No"));
                    System.out.println("        Capacity: " + capacity);
                    System.out.println("        Room Type: " + type);
                    System.out.println("        Balcony: " + (balcony ? "Yes" : "No"));
                    System.out.println("        Price per Night: " + pricePerNight);
                    System.out.println("        Clean: " + (clean ? "Yes" : "No"));
                    System.out.println("        Hydromassage: " + (hydromassage ? "Yes" : "No"));

                    for (Reserva r: reservations){
                        if(r.name.equals(name) && r.check_In.equals(check_in) && r.check_Out.equals(check_out))
                            return r;
                    }
                } else {
                    System.out.println("Nenhuma reserva encontrada com o nome e data de check-in fornecidos.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw exception to handle it further up the call stack
        }
        return null;
    }

    public void AlterarReservas() throws SQLException {
        Reserva reserva = searchReservation();

        if (!reserva.isCheck_In_Made()) {
            int quantidade = NumberOfPersons();
            LocalDate check_in = RequestDate("Data de Entrada (dd-MM-yyyy):");
            LocalDate check_out = RequestDate("Data de Saída (dd-MM-yyyy):", check_in);
            int noites = nightsCalculator(check_in, check_out);

            Room quarto = checkRoomAvailability(roomList, reservations, quantidade, check_in, check_out);
            if (quarto == null) {
                System.out.println("Nenhum Quarto Disponível");
                return;
            }

            System.out.println("Quarto nº " + quarto.getNumber() + " Livre e Disponível");
            reserva.setRoom(quarto);
            reserva.setNights(noites);

            Room_Type tipoDeQuarto = SelectRoomType(quantidade);
            reserva.setType(tipoDeQuarto);

            int adultos = solicitarNumeroDeAdultos();
            int criancas = solicitarNumeroDeCriancas();
            reserva.setAdults(adultos);
            reserva.setChildren(criancas);

            double precoCriancas = solicitarPrecoCriancas(criancas);
            reserva.setChildren_Description(precoCriancas);

            if (validarQuantidadePessoas(quantidade, adultos, criancas)) {
                System.out.println("Quantidade de pessoas não corresponde!");
                return;
            }

            boolean animais = solicitarPresencaDeAnimais();
            reserva.setPets(animais);

            int numeroAnimais = 0;
            double precoAnimais = 0;
            if (animais) {
                numeroAnimais = solicitarNumeroDeAnimais();
                precoAnimais = solicitarPrecoAnimais();
            }
            reserva.setNumber_of_Pets(numeroAnimais);
            reserva.setPet_Description(precoAnimais);

            boolean hidromassagem = solicitarHidromassagem();
            reserva.setHydromassage(hidromassagem);

            boolean noiteRomantica = solicitarPacoteRomantico();
            reserva.setRomantic_Night(noiteRomantica);

            double precoPorNoite = calcularPrecoPorNoite(quarto, precoCriancas, precoAnimais, hidromassagem ? 30 : 0, noiteRomantica ? 15 : 0, adultos);
            reserva.setPrice_per_Night(precoPorNoite);

            int numeroCamas = solicitarNumeroDeCamas(tipoDeQuarto, adultos);
            reserva.setN_Beds(numeroCamas);
            quarto.setClean(true);

            System.out.println("Reserva com Alterações");
            System.out.println(reserva);

            //atualizarReservaNaLista(reserva, precoPorNoite);
            abrirDia();
        } else {
            System.out.println("Já não é possível alterar a reserva! Check-In efetuado.");
        }
    }


}
