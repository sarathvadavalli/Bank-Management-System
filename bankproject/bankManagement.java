package bankproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class bankManagement {
    private static final int NULL = 0;

    static Connection con = connection.getConnection();
    static String sql = "";
    static int prev;

    static Random rand = new Random();
    static int max = 591022400,min = 521022400;

    public static boolean createAccount(String name, String passCode) // create account function
    {
        try {
            // validation
            if (name == "" || passCode == "") {
                System.out.println("All Field Required!");
                return false;
            }
            // query
            int ac = rand.nextInt(max - min + 1) + min;
            Statement st = con.createStatement();
            sql = " INSERT INTO banking(uname,balance,pass_code,ac_no) values('"
                    + name + "',500,'" + passCode + "', " + ac + ") ";

            // Execution
            if (st.executeUpdate(sql) == 1) {
                System.out.println(name
                        + ", Now You Login!");

                sql = " INSERT INTO transaction(uname,type,balance) values('" + name + "', 'Initial', 500) ";
                st.executeUpdate(sql);

                return true;
            }
        }
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username already exists!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
   
    public static boolean loginAccount(String name, String passCode) // login method
    {
        try {
            // validation
            if (name == "" || passCode == "") {
                System.out.println("All Field Required!");
                return false;
            }

            // query
            sql = " select * from banking where uname='"+ name + "' and pass_code= '"+ passCode + "' ";

            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            int senderAc = 0;

            // Execution
            BufferedReader sc = new BufferedReader(
                    new InputStreamReader(System.in));

            if (rs.next()) {
                int ch = 5;
                int amt = 0;
                senderAc = rs.getInt(4);

                while (true) {
                    try {
                        String userName = rs.getString(1);
                        int bal = rs.getInt(2);
                        System.out.println(
                                "\nHello, " + userName);
                        System.out.println(
                                "----------------");
                        System.out.println("\n1) Deposit Money");
                        System.out.println("2) Withdraw Money");
                        System.out.println("3) View Balance");
                        System.out.println("4) Previous Transaction");
                        System.out.println("5) Transaction History");
                        System.out.println("6) Logout");

                        System.out.print("Enter Choice:");
                        ch = Integer.parseInt(sc.readLine());
                        if(ch == 6){
                            System.out.println("\nLogged out!");
                            break;
                        }

                        switch (ch) {
                            case 1:
                                System.out.print("Enter Amount:");
                                amt = Integer.parseInt(sc.readLine());

                                if (bankManagement.deposit(userName, senderAc, amt)) {
                                    System.out.println(
                                            "MSG : Money deposited Successfully!\n");
                                } else {
                                    System.out.println("ERR : Failed!\n");
                                }
                                break;

                            case 2:
                                System.out.print("Enter Amount:");
                                amt = Integer.parseInt(sc.readLine());

                                if (bankManagement.withDrawMoney(userName, senderAc, amt)) {
                                    System.out.println(
                                            "MSG : Money Withdrawn Successfully!\n");
                                } else {
                                    System.out.println("ERR : Failed!\n");
                                }
                                break;

                            case 3:
                                bankManagement.getBalance(senderAc, 1);
                                break;

                            case 4:
                                bankManagement.previous(userName, senderAc, bal);
                                break;

                            case 5:
                                bankManagement.history(userName);
                                break;
                            default:
                                System.out.println("Err : Enter Valid input!\n");
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                return false;
            }
        }
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username Not Available!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public static int getBalance(int acNo, int ch)
    {
        try {
            // query
            sql = "select * from banking where ac_no="
                    + acNo;
            PreparedStatement st
                     = con.prepareStatement(sql);

            ResultSet rs = st.executeQuery(sql);

            // Execution
            int bal=0;
            if (rs.next()) {
                bal = rs.getInt("balance");
                if(ch == 0)
                    return bal;

                System.out.println(
                        "\n-----------------------------------------------------------");
                System.out.printf("%12s %10s %10s\n",
                        "Account No", "Name",
                        "Balance");

                System.out.printf("%12d %10s %10d.00\n",
                        rs.getInt("ac_no"),
                        rs.getString("uname"),
                        bal);
            }
            System.out.println(
                    "-----------------------------------------------------------\n");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean deposit(String name, int sender_ac, int amount) throws SQLException
    {
        try {
            con.setAutoCommit(false);
            Statement st = con.createStatement();
            con.setSavepoint();

            sql = "update banking set balance=balance+"
                    + amount + " where ac_no=" + sender_ac;

            if (st.executeUpdate(sql) == 1) {
                System.out.println("Amount Credited!");
                prev = amount;
            }

            int bal = getBalance(sender_ac,0);
            sql = " INSERT INTO transaction(uname,type,amount,balance) values('" + name + "', 'Deposit', " + amount + ", " + bal + ") ";

            if (st.executeUpdate(sql) == 1)
                System.out.println("\nTransaction recorded!");

            con.commit();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            con.rollback();
        }

        return false;
    }

    public static boolean withDrawMoney(String name, int sender_ac, int amount) throws SQLException
    {
        try {
            con.setAutoCommit(false);
            sql = "select * from banking where ac_no="
                    + sender_ac;
            PreparedStatement ps
                    = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getInt("balance") < amount) {
                    System.out.println(
                            "Insufficient Balance!");
                    return false;
                }
            }

            Statement st = con.createStatement();
            con.setSavepoint();

            sql = "update banking set balance=balance-"
                    + amount + " where ac_no=" + sender_ac;

            if (st.executeUpdate(sql) == 1) {
                System.out.println("Amount Debited!");
                prev = -amount;
            }

            int bal = getBalance(sender_ac,0);
            sql = " INSERT INTO transaction(uname,type,amount,balance) values('" + name + "', 'Withdraw', " + amount + ", " + bal + ") ";

            if (st.executeUpdate(sql) == 1)
                System.out.println("\nTransaction recorded!");

            con.commit();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            con.rollback();
        }

        return false;
    }
    public static void previous(String name,int acNo,int bal)
    {
        System.out.println("\n-------------------------------------------------------");
        System.out.println("User Name: "+name);
        System.out.println("Account No: "+acNo);
        if(prev > 0) {
            System.out.println("Deposited: " + prev);
        }
        else if(prev < 0) {
            System.out.println("Withdrawn: " +Math.abs(prev));
        }
        else {
            System.out.println("No Transaction Occured");
        }
        System.out.println("Balance: "+(bal+prev));
        System.out.println("-------------------------------------------------------\n");
    }

    public static void history(String name)
    {
        try {
            sql = "select * from transaction where uname='" + name + "'";

            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            System.out.println("\n-------------------------------------------------------");
            System.out.printf("%10s %12s %12s\n",
                    "Type", "Amount",
                    "Balance");
            while(rs.next()){
               String type = rs.getString("type");
               int am = rs.getInt("amount");
               int bal = rs.getInt("balance");

                System.out.printf("%12s %10d %10d.00\n",
                        type, am, bal);
            }
            System.out.println("-------------------------------------------------------\n");
        }
        catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username Not Available!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}