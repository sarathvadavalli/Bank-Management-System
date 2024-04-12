package bankproject;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String args[]) throws IOException
    {
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
        Scanner sc1 = new Scanner(System.in);
        String name = "";
        String pass_code;
        int ch;

        while (true) {
            System.out.println(
                    "\n ->|| Welcome to InBank ||<- \n");
            System.out.println("1)Create Account");
            System.out.println("2)Login Account");

            try {
                System.out.print("\n Enter Input:");
                ch = sc1.nextInt();
                if (ch == -1) {
                    System.out.println("Exited Successfully!\n\n Thank You :)");
                    break;
                }

                switch (ch) {
                    case 1:
                        try {
                            System.out.print("Enter Unique UserName:");
                            name = sc.readLine();
                            System.out.print("Enter New Password:");
                            pass_code = sc.readLine();

                            if (bankManagement.createAccount(name, pass_code)) {
                                System.out.println("MSG : Account Created Successfully!\n");
                            }
                            else {
                                System.out.println("ERR : Account Creation Failed!\n");
                            }
                        }
                        catch (Exception e) {
                            System.out.println(" ERR : Enter Valid Data::Insertion Failed!\n");
                        }
                        break;

                    case 2:
                        try {
                            System.out.print("Enter UserName:");
                            name = sc.readLine();
                            System.out.print("Enter Password:");
                            pass_code = sc.readLine();

                            if (bankManagement.loginAccount(name, pass_code)) {
                                System.out.println("\n.......THANK YOU FOR USING OUR SERVICES........");
                            }
                            else {
                                System.out.println("ERR : login Failed!\n");
                            }
                        }
                        catch (Exception e) {
                            System.out.println("ERR : Enter Valid Data::Login Failed!\n");
                        }

                        break;

                    default:
                        System.out.println("Invalid Entry!\n");
                }
            }
            catch (Exception e) {
                System.out.println("Enter Valid Entry!");
            }
        }
        sc.close();
    }
}
