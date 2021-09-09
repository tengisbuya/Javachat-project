import java.io.*;
import java.net.*;
import java.sql.*;
class UDPServer {
    public static void main(String args[]) throws Exception
    {
        //Prepare the variables for SQL connection
        Connection conn;
        String url;
        String driver;

        // Enter credentials for connection
        conn = null;
        url = "jdbc:mysql://localhost:3306/javachat?useSSL=false";
        driver = "com.mysql.jdbc.Driver";

        // Connecting to database...
        Class.forName(driver).getDeclaredConstructor().newInstance();
        conn = DriverManager.getConnection(url,"root","admin");

        // Connecting to UDP port 9876
        DatagramSocket serverSocket = new
                DatagramSocket(9876);
        byte[] receiveData = new byte[1024];
        byte[] sendData = "false".getBytes();

        while(true)
        {
            // Prepare some variables for db
            PreparedStatement p;
            ResultSet rs;
            String sql;

            // Check for incoming packets
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            // Get arguments from response
            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received packet: " + message);
            String[] arguments = message.split("\\?");
            String action = arguments[0];
            if (action.equals("login") && arguments.length == 3) {
                String username = arguments[1];
                String password = arguments[2];

                // Use preparedStatement to avoid SQL injection, if this query returns a record
                // then credentials are correct and we should login the user, but if it is empty,
                // the credentials are wrong and we should display error message.
                sql = "Select " + "* from user " + "where name = ? " + "and password = ?";

                // statement-ийг тохируулан үүсгэж байна
                p = conn.prepareStatement(sql);
                p.setString(1, username);
                p.setString(2, password);

                // асуулгыг ажилууллаа
                rs = p.executeQuery();

                if (rs.next() == false) {
                    // If login credentials are wrong, display error
                    System.out.println("Login failed, wrong credentials");
                    sendData = "false".getBytes();
                } else {
                    System.out.println("yes sda orchloo");
                    // If login is correct, store information and redirect user to friends page.
                    sendData = (rs.getString("pk_id") + "?" + rs.getString("name") + "?"
                            + rs.getString("firstname") + "?" + rs.getString("lastname")).getBytes();
                }
            }
            else if (action.equals("reg")) {
                String username = arguments[1];

                // Use preparedStatement to avoid SQL injection, if this query returns a record
                // then credentials are correct and we should login the user, but if it is empty,
                // the credentials are wrong and we should display error message.
                sql = "Select " + "* from user " + "where name = ? ";

                // statement-ийг тохируулан үүсгэж байна
                p = conn.prepareStatement(sql);
                p.setString(1, username);

                // асуулгыг ажилууллаа
                rs = p.executeQuery();

                // Handle query results
                if (rs.next() == false){
                    String firstname = arguments[2];
                    String lastname = arguments[3];
                    String password = arguments[4];
                    if (username.trim().length() > 0 && firstname.trim().length() > 0
                        && lastname.trim().length() > 0 && password.trim().length() > 0)
                    {
                        // Get highest id, and set id of new record to be highest id + 1
                        sql = "SELECT pk_id FROM user order by pk_id DESC LIMIT 1";
                        rs = p.executeQuery(sql);
                        int max_id;
                        if (rs.next() == false){
                            max_id = 0;
                        } else {
                            max_id = Integer.parseInt(rs.getString("pk_id"));
                        }
                        sql = "Insert into user(pk_id, name, password, firstname, lastname) " + "values (?, ?, ?, ?, ?)";
                        p = conn.prepareStatement(sql);
                        p.setInt(1, max_id+1);
                        p.setString(2, username);
                        p.setString(3, password);
                        p.setString(4, firstname);
                        p.setString(5, lastname);
                        Boolean works = p.execute();
                        System.out.println(works);
                        sendData = new String("success").getBytes();
                    }
                } else {
                    System.out.println("Username taken");
                    sendData = new String("false").getBytes();
                }
            }
            else if (action.equals("friends")){
                // Sends a packet of users information,
                // split by ?? between users
                // and by ? between columns
                sql = "SELECT * FROM user where pk_id != ?";
                p = conn.prepareStatement(sql);
                p.setInt(1, Integer.parseInt(arguments[1]));
                rs = p.executeQuery();
                String sendString = "";
                if (rs.next() == false){
                    System.out.println("There are no users");
                    sendData = "false".getBytes();
                } else {
                    do {
                        String str = rs.getInt("pk_id") + "?" + rs.getString("name");
                        sendString += str + "??";
                    } while (rs.next() == true);
                    sendString = sendString.substring(0, sendString.length() - 2);
                    sendData = sendString.getBytes();
                }
            }

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
            sendData = "false".getBytes();
        }
    }
}