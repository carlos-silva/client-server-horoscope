/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utrgvhoroscope;

import java.io.*;
import java.net.*;
import java.util.*;

public class UTRGVHoroscopeClient {

    private static InetAddress host;
    private static final int PORT = 1234;
    private static DatagramSocket dgramSocket;
    private static DatagramPacket inPacket, outPacket;
    private static byte[] buffer;
    private static String CRLF = "\r\n";
    // Purchase order Information
    private static String PO = "";

    public static void main(String[] args) {
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println("Host ID not found!");
            System.exit(1);
        }
        run();
    }

    private static void run() {
        try {
            dgramSocket = new DatagramSocket();        //Step 1.

            //Set up stream for keyboard entry...
            BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));

            String response = "";
            String temp;
            int limit = 0;
          
            System.out.println("Enter your ID, Name and birth date [xx/xx/xxxx] or ***CLOSE*** to exit");
            do {
                
                temp =userEntry.readLine();
                limit++;
                if (!temp.equals("***CLOSE***") )
                {
                    
                        outPacket = new DatagramPacket(
                                temp.getBytes(),
                                temp.length(),
                                host, PORT);       
                        dgramSocket.send(outPacket);    
                        
                     
                }
                else
                    break;
            } while ( limit < 3);
            
             buffer = new byte[2048];          
                        inPacket = new DatagramPacket(
                                buffer, buffer.length);     
                        dgramSocket.receive(inPacket);    
                        response = new String(inPacket.getData(),
                                0, inPacket.getLength());    
                        System.out.println(
                                "\nSERVER> " + response);

        }
        
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("\n* Closing connection... *");
            dgramSocket.close();                    //Step 8.
        }
    }
}

     

    