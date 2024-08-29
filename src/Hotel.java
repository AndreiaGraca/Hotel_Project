import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        String quartos_ocupados = "";
        int count_occup = 0;
        for (Room a : roomList)
            if (a.isOcupied()) {
                count_occup++;
                quartos_ocupados = quartos_ocupados + "  " + a.getNumber();
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
        boolean find = false;
        for (Reserva a : reservations) {
            if (a.getName().equals(client_name)) {
                find = true;
                a.setCheck_In_Made(true);
                a.getQuarto().setOcupied(true);
                System.out.println("Quarto Nº: " + a.getQuarto().getNumber());
            }
        }

        if (!find)
            System.out.println("\nNão existe nenhuma reserva com esse nome! ");
        else
            System.out.println("\nCheck-In Realizado com Sucesso!");
    }

    public void Check_Out() {
        boolean find = false;
        double price;

        System.out.println("Qual o nº do quarto: ");
        int room = sc.nextInt();
        sc.nextLine();
        for (Reserva a : reservations) {
            if (a.getQuarto().getNumber() == room && a.isCheck_In_Made()) {
                if (a.isPaid()) {
                    a.setCheck_Out_Made(true);
                    a.getQuarto().setClean(false);
                    a.getQuarto().setOcupied(false);
                } else {
                    price = a.getTotal_Price();
                    System.out.println("Pagamento Necessário!  Valor: " + price);
                    System.out.println("Pago? (s/n)");
                    String pagamento = sc.nextLine();
                    if (pagamento.toUpperCase().equals("S")) {
                        a.setCheck_Out_Made(true);
                        a.getQuarto().setClean(false);
                        a.getQuarto().setOcupied(false);
                        System.out.println("Pagamento Realizado!");
                    } else {
                        a.setCheck_Out_Made(false);
                        a.getQuarto().setClean(true);
                        a.getQuarto().setOcupied(true);
                    }
                }
                find = true;
            } else if (!a.isCheck_In_Made()) {
                System.out.println("Check-In Não Realizado!");
                return;
            }
        }

        if (!find)
            System.out.println("\nNão existe nenhuma reserva para esse quarto! ");
        else {
            System.out.println("\nCheck-Out Realizado com Sucesso!");
        }
    }

    private static Room Verifica_Disponibilidade(List<Room> roomList, List<Reserva> reservas, int quantidade, LocalDate check_in, LocalDate check_out) {
        int count_occup = 0;

        for (Room a : roomList)
            if (a.isOcupied())
                count_occup++;

        if (count_occup == roomList.size()) {
            System.out.println("Hotel Lotado!");
            return null;
        } else {
            for (Room room : roomList) {
                if (room.getCapacity() >= quantidade) {
                    boolean disponivel = true;
                    for (Reserva r : reservas) {
                        if (r.getQuarto().equals(room) && r.isCheck_Out_Made() != true && r.isCanceled()!=true) {
                            if (!(check_out.isBefore(r.getCheck_In()) || check_in.isAfter(r.getCheck_Out()))) {
                                disponivel = false;
                                break;
                            }
                        }
                    }
                    if (disponivel) {
                        return room;
                    }
                }
            }
            return null;
        }
    }

    public void NovaReserva() throws SQLException {
        String reservation;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        System.out.println("Quantidade de Pessoas: ");
        int quantidade = sc.nextInt();
        sc.nextLine();
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


        Room a = Verifica_Disponibilidade(roomList, reservations, quantidade, check_in, check_out);
        if (a == null)
            System.out.println("Nenhum Quarto Disponível");
        else {
            System.out.println("Quarto nº " + a.getNumber() + "  Livre e Disponível");
            String name = "";

            //Quarto reserva=RetornaQuarto(a.getNumber());
            System.out.println("Deseja Realizar a Reserva? (s/n)");
            reservation = sc.nextLine();
            if (reservation.toUpperCase().equals("S")) {
                reservations = Reservar(a, reservations, quantidade, check_in, check_out);
            } else
                return;
        }
    }

    public List<Reserva> Reservar(Room reserva, List<Reserva> reservas, int quantidade, LocalDate check_in, LocalDate check_out) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String name;
        while (true) {
            System.out.print("Nome do Cliente: ");
            name = sc.nextLine();

            if (isNomeDuplicado(reservas, name)) {
                System.out.println("Nome já reservado. Por favor, escolha outro nome.");
            } else {
                break;
            }
        }

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

        if(adultos + criancas!= quantidade) {
            System.out.println("Quantidade de pessoas não corresponde! ");
            return reservas;
        }

        sc.nextLine();
        System.out.println("Animais (s/n): ");
        String animais = sc.nextLine();

        boolean animais_valid;
        if (animais.toUpperCase().equals("S"))
            animais_valid = true;
        else
            animais_valid = false;

        int number_of_pets;
        double price_animals;

        if (animais_valid) {
            System.out.println("Número de Animais: ");
            number_of_pets = sc.nextInt();
            System.out.println("Descrição dos animais - preço");
            price_animals = sc.nextDouble();
        } else {
            number_of_pets = 0;
            price_animals = 0;
        }
        //sc.nextLine();
        System.out.println("Hidromassagem? (s/n)");
        String hidromassagem = sc.nextLine();

        double hidromassagem_val;
        boolean hidromassagem_bol;
        if (hidromassagem.toUpperCase().equals("S")) {
            hidromassagem_bol = true;
            reserva.setHydromassage(true);
            hidromassagem_val = 30;
        } else {
            hidromassagem_bol = false;
            hidromassagem_val = 0;
            reserva.setHydromassage(false);
        }

        System.out.println("Romatica? (s/n)");
        String romantica = sc.nextLine();

        double romantica_val;
        boolean romantica_bol;
        if (romantica.toUpperCase().equals("S")) {
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

        double price_per_night = ((reserva.type.getPrice() + criancas_description + price_animals + hidromassagem_val + criancas_description + price_adultos_mais + romantica_val));

        reserva.setPrice_per_night(price_per_night);
        for (Room a : roomList) {
            if (a.getNumber() == reserva.getNumber()) {
                a.setPrice_per_night(price_per_night);
                a.setType(reserva.getType());
                a.setHydromassage(reserva.isHydromassage());
                // Continue com outras propriedades necessárias
            }
        }

        //reserva.setOcupied(true);
        reserva.setType(rm);
        reserva.setClean(false);
        int number= reserva.getNumber();
        Reserva l = new Reserva(name, nif, noites, check_in, check_out, quantidade, rm, adultos, criancas, criancas_description, animais_valid, number_of_pets, price_animals, price_per_night, hidromassagem_bol, romantica_bol, reserva, n_camas);
        l.setTotal_Price((price_per_night * noites));

        int b=AddBdReservation(l);
        l.setNumber(b);
        reservas.add(l);

        System.out.println("Resumo da Reserva: ");
        System.out.println(l);

       return reservas;
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

        String SQL = "INSERT INTO reservas(name, nif, total_price, nights, check_in, check_out, number_persons, type, adults, " +
                "children, children_description, pets, number_of_pets, pet_description, price_per_night, hydromassage, " +
                "romantic_night, n_quarto, n_camas, check_in_made, check_out_made, pago, reserva_feita_em,occupied, capacity, balcony, clean) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ? , ? , ? )";

        String sql = "INSERT INTO reserve (name, nif, total_price, nights, check_in, check_out, number_persons, type, " +
                "adults, children, children_description, pets, number_of_pets, pet_description, price_per_night, " +
                "hydromassage, romantic_night, n_room, n_beds, check_in_made, check_out_made, paid, reservation_date, " +
                "occupied, capacity, balcony, clean, canceled) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING reserva_id"; // Assumindo que a coluna id é a chave primária da tabela

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Defina todos os parâmetros do PreparedStatement
            stmt.setString(1, reserva.getName());
            stmt.setInt(2, reserva.getNif());
            stmt.setDouble(3, reserva.getTotal_Price());
            stmt.setInt(4, reserva.getNights());
            stmt.setDate(5, java.sql.Date.valueOf(reserva.getCheck_In()));
            stmt.setDate(6, java.sql.Date.valueOf(reserva.getCheck_Out()));
            stmt.setInt(7, reserva.getNumber_Persons());
            stmt.setString(8, String.valueOf(reserva.getType()));
            stmt.setInt(9, reserva.getAdults());
            stmt.setInt(10, reserva.getChildren());
            stmt.setDouble(11, reserva.getChildren_Description());
            stmt.setBoolean(12, reserva.isPets());
            stmt.setInt(13, reserva.getNumber_of_Pets());
            stmt.setDouble(14, reserva.getPet_Description());
            stmt.setDouble(15, reserva.getPrice_per_Night());
            stmt.setBoolean(16, reserva.isHydromassage());
            stmt.setBoolean(17, reserva.isRomantic_Night());
            stmt.setInt(18, reserva.getQuarto().getNumber());
            stmt.setInt(19, reserva.getN_Beds());
            stmt.setBoolean(20, reserva.isCheck_In_Made());
            stmt.setBoolean(21, reserva.isCheck_Out_Made());
            stmt.setBoolean(22, reserva.isPaid());
            stmt.setDate(23, java.sql.Date.valueOf(reserva.getReservation_Date()));
            stmt.setBoolean(24, reserva.getQuarto().isOcupied());
            stmt.setInt(25, reserva.getQuarto().getCapacity());
            stmt.setBoolean(26, reserva.getQuarto().isBalcony());
            stmt.setBoolean(27, reserva.getQuarto().isClean());
            stmt.setBoolean(28, reserva.isCanceled());

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

    public boolean chooses(String a) {
        if (a.toUpperCase().equals('S'))
            return true;
        else
            return false;
    }

    public void change_Informations() {
        Room_Type rm;
        int option = 7;
        String pagamento, limpeza, tipo_quarto;
        System.out.println("Número do Quarto: ");
        int roomNumber = sc.nextInt();
        Room a = RetornaQuarto(roomNumber);
        sc.nextLine();
        for (Reserva b : reservations) {
            if (b.getQuarto().equals(a)) {
                do {
                    System.out.println("Alterações: ");
                    System.out.println("   0 - Menu");
                    System.out.println("   1 - Pagamento ");
                    System.out.println("   2 - Limpeza");
                    System.out.println("   3 - Tipo de Quarto");

                    System.out.println("Option: >");
                    option = sc.nextInt();
                    sc.nextLine();
                    switch ((option)) {
                        case 1:
                            System.out.println("Pagamento (s/n)");
                            pagamento = sc.nextLine();

                            if (pagamento.toUpperCase().equals("S"))
                                b.setPaid(true);
                            else
                                b.setPaid(false);
                            break;
                        case 2:
                            if (b.isCheck_Out_Made()) {
                                System.out.println("Limpeza: (s/n):");
                                limpeza = sc.nextLine();
                                if (limpeza.toUpperCase().equals("S"))
                                    a.setClean(true);
                                else
                                    a.setClean(false);
                            } else System.out.println("Check_Out Não Foi Feito! ");
                            break;
                        case 3:
                            if (b.isCheck_Out_Made()) {
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

                } while (option != 0);

            }
        }
    }

    public void FecharDia() {
        String url = "jdbc:postgresql://localhost:5432/postgres"; // URL do banco
        String user = "postgres"; // Usuário do banco
        String password = "andreia"; // Senha do banco

        String SQL = "INSERT INTO reservas(number, name, nif, total_price, nights, check_in, check_out, number_persons, type, adults, " +
                "children, children_description, pets, number_of_pets, pet_description, price_per_night, hydromassage, " +
                "romantic_night, n_quarto, n_camas, check_in_made, check_out_made, pago, reserva_feita_em,occupied, capacity, balcony, clean) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ? , ? , ? )";


        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            // Itera sobre o ArrayList de reservas
            for (Reserva reserva : reservations) {
                try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
 // Incrementa o número da reserva a cada inserção
                    pstmt.setString(2, reserva.name);
                    pstmt.setInt(3, reserva.nif);
                    pstmt.setDouble(4, reserva.total_Price);
                    pstmt.setInt(5, reserva.nights);
                    pstmt.setDate(6, java.sql.Date.valueOf(reserva.check_In));
                    pstmt.setDate(7, java.sql.Date.valueOf(reserva.check_Out));
                    pstmt.setInt(8, reserva.number_Persons);
                    pstmt.setString(9, String.valueOf(reserva.getType()));
                    pstmt.setInt(10, reserva.adults);
                    pstmt.setInt(11, reserva.children);
                    pstmt.setDouble(12, reserva.children_Description);
                    pstmt.setBoolean(13, reserva.pets);
                    pstmt.setInt(14, reserva.number_of_Pets);
                    pstmt.setDouble(15, reserva.pet_Description);
                    pstmt.setDouble(16, reserva.price_per_Night);
                    pstmt.setBoolean(17, reserva.hydromassage);
                    pstmt.setBoolean(18, reserva.romantic_Night);
                    pstmt.setInt(19, (int) (reserva.getQuarto().getNumber()));
                    pstmt.setBoolean(25, reserva.getQuarto().isOcupied()); // Se o quarto está ocupado
                    pstmt.setInt(26, reserva.getQuarto().getCapacity()); // Capacidade do quarto
                    pstmt.setBoolean(27, reserva.getQuarto().isBalcony()); // Se tem varanda
                    pstmt.setBoolean(28, reserva.getQuarto().isClean()); // Se está limpo
                    pstmt.setInt(20, reserva.n_Beds);
                    pstmt.setBoolean(21, reserva.check_In_Made);
                    pstmt.setBoolean(22, reserva.check_Out_Made);
                    pstmt.setBoolean(23, reserva.paid);
                    pstmt.setDate(24, java.sql.Date.valueOf(reserva.reservation_Date));

                    pstmt.executeUpdate();
                } catch (SQLException ex) {
                    System.out.println("Erro ao inserir reserva: " + reserva.name + " - " + ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar ao banco de dados: " + ex.getMessage());
        }
    }


    public List<Reserva> abrirDia() throws SQLException {
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
                reserve.setNif(rs.getInt("nif"));
                reserve.setTotal_Price(rs.getDouble("total_price"));
                reserve.setNights(rs.getInt("nights"));
                reserve.check_In = rs.getDate("check_in").toLocalDate();
                reserve.check_Out = rs.getDate("check_out").toLocalDate();
                reserve.setNumber_Persons(rs.getInt("number_persons"));
                String typeString = rs.getString("type");
                Room_Type type = Room_Type.valueOf(typeString.toUpperCase()); // Converte o texto do banco para o enum
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

        return reservations;
    }

    public void cancelReservation() throws SQLException {
        System.out.println("Nome da Reserva: ");
        String name=sc.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("Check-In: ");
        String checkInStr = sc.nextLine();
        LocalDate check_in = LocalDate.parse(checkInStr, formatter);

        cancelReservationBD(name,check_in);

    }

    public void cancelReservationBD(String name, LocalDate checkInDate) throws SQLException {

        String url = "jdbc:postgresql://localhost:5432/postgres"; // URL do banco
        String user = "postgres"; // Usuário do banco
        String password = "andreia"; // Senha do banco

        // SQL para atualizar o campo 'canceled' para true
        String sql = "UPDATE reserve SET canceled = TRUE WHERE name = ? AND check_in = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Define os parâmetros do PreparedStatement
            stmt.setString(1, name);
            stmt.setDate(2, java.sql.Date.valueOf(checkInDate));

            // Executa a atualização
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reserva cancelada com sucesso!");
            } else {
                System.out.println("Nenhuma reserva encontrada com o nome e data de check-in fornecidos.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw exception to handle it further up the call stack
        }
        abrirDia();//para dar refresh nos dados da lista dos quartos
    }

    public void searchReservation() throws SQLException {
        System.out.println("Nome da Reserva: ");
        String name=sc.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("Check-In: ");
        String checkInStr = sc.nextLine();
        LocalDate check_in = LocalDate.parse(checkInStr, formatter);
        System.out.println("Check-Out: ");
        String checkOutStr = sc.nextLine();
        LocalDate check_out = LocalDate.parse(checkOutStr, formatter);

        searchDB(name,check_in,check_out);
    }

    private void searchDB(String name, LocalDate check_in, LocalDate check_out) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres"; // URL do banco
        String user = "postgres"; // Usuário do banco
        String password = "andreia"; // Senha do banco

        // SQL para selecionar a reserva com base no nome e na data de check-in
        String sql = "SELECT id, name, total_price, nights, check_in, check_out, paid, adults, children, " +
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
                    int id = rs.getInt("id");
                    String reservaName = rs.getString("name");
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
                } else {
                    System.out.println("Nenhuma reserva encontrada com o nome e data de check-in fornecidos.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw exception to handle it further up the call stack
        }
    }
    }
