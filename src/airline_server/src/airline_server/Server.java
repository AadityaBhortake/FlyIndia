/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airline_server;

import java.io.*;
import java.net.*;
import java.sql.*;
/**
 *
 * @author Aaditya
 */
public class Server extends Thread{
    private static String user;
    public static ServerSocket ss;
    public static Socket socket;
    public static void startServer(){
        try{
            ss = new ServerSocket(6666);
            System.out.println("Server Started");
            while(true){
                socket = ss.accept();
                new Thread(() -> client_login(socket)).start();
            }
        }catch(IOException e){
        }
    }
    
    public static void closeServer(){
        try{
            if(socket != null){
                socket.close();
                if(ss!= null)
                    ss.close();
            }
        }catch(IOException e){e.printStackTrace();}
        
        
    }
    
    private static void client_login(Socket socket){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
            String uname = "",requestType="";
                
                    
                    while(true){
                            requestType = reader.readLine();
                        
                            switch(requestType){
                                case "LOGIN":
                                    /*
                                    received login info from client
                                    */
                                    uname = reader.readLine();
                                    String pword = reader.readLine();
                                    if(validate_user(uname,pword))
                                        writer.println("SUCCESS");
                                    else
                                        writer.println("FAIL");
                                         
                                    break;
                                    
                                case "BREAK":
                                    return;
                                case "Search":
                                    search_flight(reader,writer);
                                    break;

                                case "confirm":
                                    int rowsaffected=0;
                                    /**
                                     * Receive passenger details from client
                                     */
                                    int n = Integer.parseInt(reader.readLine());
                                    String code = reader.readLine();
                                    try(Connection con = Database.connect()){
                                        /**
                                         * get the user id of current user 
                                         */
                                        PreparedStatement ps = con.prepareStatement("SELECT iduser_login FROM user_login WHERE username=?");
                                        ps.setString(1, uname);
                                        ResultSet rs = ps.executeQuery();
                                        int user_id = 0;
                                        if(rs.next()){
                                            user_id = rs.getInt("iduser_login");
                                        }

                                        while(true){
                                            String name = reader.readLine();
                                            if(name == null || name.equals("END"))
                                                break;
                                            int age = Integer.parseInt(reader.readLine());
                                            String gender = reader.readLine();
                                            String phone = reader.readLine();
                                            String query = "INSERT INTO booking VALUES(?,?,?,?,?,?)";
                                            PreparedStatement pst = con.prepareStatement(query);
                                            pst.setString(1,code);
                                            pst.setInt(2,user_id);
                                            pst.setString(3,name);
                                            pst.setInt(4,age);
                                            pst.setString(5,gender);
                                            pst.setString(6,phone);
                                            if(pst.executeUpdate() > 0)
                                                   rowsaffected++;
                                            else{
                                                writer.println("FAIL");
                                            }
                                        }

                                        if(rowsaffected > 0){
                                            /**
                                             * Update seat_available after booking
                                             */
                                            String q = "UPDATE flight SET seat_available = seat_available-? WHERE flight_code=?";
                                            PreparedStatement st = con.prepareStatement(q);
                                            st.setInt(1, n);
                                            st.setString(2, code);
                                            /**
                                             * if booking successful send msg to client 
                                             */
                                            writer.println("SUCCESS");
                                        }
                                    }
                                    catch(SQLException e){
                                        e.printStackTrace();
                                    }
                                    break;

                                default:
                                    writer.println("FAIL");
                    }
                }
                

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    
    private static boolean validate_user(String uname,String pword){
        try{
            /**
             * Connect to database
             */
            Connection con = Database.connect();
            /*
            Search for login info
            */
            String query = "SELECT * FROM user_login WHERE username=? AND password=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,uname);
            pst.setString(2,pword);
            ResultSet resultSet = pst.executeQuery();
            return resultSet.next();
        }catch(SQLException e){
            System.out.println("Database connection failed");
            return false;
        }
    }
    
    private static void search_flight(BufferedReader reader, PrintWriter writer){
        String src="",dest="",month="",sort="";
        int day = 0,pas=0;
        try{
            src = reader.readLine();
            dest = reader.readLine();
            day = Integer.parseInt(reader.readLine());
            month = reader.readLine();
            pas = Integer.parseInt(reader.readLine());
            sort = reader.readLine();

        }
        catch(IOException e){
            e.printStackTrace();
        }
        try{
            Connection con = Database.connect();
            String query=null;
            if(sort.equals("Time"))
                query = "SELECT * FROM flight WHERE source=? AND destination=? AND day=? AND month=? AND seat_available>=? ORDER BY departure ASC";
            else
                query = "SELECT * FROM flight WHERE source=? AND destination=? AND day=? AND month=? AND seat_available>=? ORDER BY price ASC";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,src);
            ps.setString(2,dest);
            ps.setInt(3,day);
            ps.setString(4,month);
            ps.setInt(5, pas);
            ResultSet rs = ps.executeQuery();
            if(rs!=null){
                try{
                    while(rs.next()){
                        
                        String f_code = rs.getString("flight_code");
                        String f_airline = rs.getString("Airline");
                        String f_dep = rs.getString("departure");
                        String f_arr = rs.getString("arrival");
                        String f_price = String.valueOf(rs.getInt("price"));
                        
                        /*
                        Send flight info to client in search_flight.java
                        */
                        writer.println(f_code);
                        writer.println(f_airline);
                        writer.println(f_dep);
                        writer.println(f_arr);
                        writer.println(f_price);

                    }
                    writer.println("END");
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            else{
                writer.println("N");
            }  
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
}
