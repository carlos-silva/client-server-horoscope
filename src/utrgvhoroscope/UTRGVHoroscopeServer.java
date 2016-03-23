/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utrgvhoroscope;

/**
 *
 * @author csilva
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UTRGVHoroscopeServer {

    private static final int PORT = 1234;
    private static DatagramSocket dgramSocket;
    private static DatagramPacket inPacket, outPacket;
    private static byte[] buffer;

    public static void main(String[] args)
    {
        System.out.println("Opening port...\n");
        try {
            dgramSocket = new DatagramSocket(PORT);//Step 1.
        } catch (SocketException e) {
            System.out.println("Unable to attach to port!");
            System.exit(1);
        }
        horoscope();
    }

    private static void horoscope() {
        try {
            String messageIn, messageOut;
            int numMessages = 0;
            
            
            Scanner s = new Scanner(new File("files/id.txt"));
            ArrayList<String> idList = new ArrayList<String>();
            while (s.hasNext()){
                idList.add(s.next());
            }
            s.close();
            
               String id = "";
                String name = "";
                String date = "";
                String month = "";
                String day = "";
                String zodiac = "";
                String horoscope_message = "";
                
            do {
                buffer = new byte[2048];         
                inPacket = new DatagramPacket(
                        buffer, buffer.length); 
                dgramSocket.receive(inPacket);    

                InetAddress clientAddress
                        = inPacket.getAddress();    
                int clientPort
                        = inPacket.getPort();        

                messageIn = new String(inPacket.getData(), 0,
                        inPacket.getLength());    
                
               

                System.out.println("Message received.");
                numMessages++;
               
                
                if (numMessages == 1){ id = messageIn; }
                if (numMessages == 2){ name = messageIn; }
                if (numMessages == 3){ date = messageIn; }
                
                
                
                if (date != ""){
                
                month = date.substring(0, 2);
                day   = date.substring(3, 5);
                zodiac = zodiac(month, day);
                horoscope_message = horoscope_selector(zodiac);
                
                if (!idList.contains(id)){
                    messageOut = ("ID " + id + " was not found\n");
                    outPacket = new DatagramPacket(
                        messageOut.getBytes(),
                        messageOut.length(),
                        clientAddress,
                        clientPort);       
                dgramSocket.send(outPacket);
                    dgramSocket.close();
                } else {
                
                
                
                messageOut = ("Hello " + name + "\n"
                        + "ID " + id + " was validated\n"
                        + "You were born on: " + date + "\n"
                        + "You are a[n] " + zodiac + "\n"
                        + "You Horoscope for the day is : \n" + horoscope_message +"\n"
                        );
                
             
                
                
                outPacket = new DatagramPacket(
                        messageOut.getBytes(),
                        messageOut.length(),
                        clientAddress,
                        clientPort);     
                dgramSocket.send(outPacket);
                
                
                
                }
               //Step 8.
            }} while (true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally //If exception thrown, close connection.
        {
            System.out.println("\n* Closing connection... *");
            dgramSocket.close();             
        }
    }
    
    private static String zodiac(String month, String day){
        
         String z;
         int M = Integer.parseInt(month);
         int D = Integer.parseInt(day);
        if      ((M == 12 && D >= 22 && D <= 31) || (M ==  1 && D >= 1 && D <= 19))
            z = "Capricorn";
        else if ((M ==  1 && D >= 20 && D <= 31) || (M ==  2 && D >= 1 && D <= 17))
            z ="Aquarius";
        else if ((M ==  2 && D >= 18 && D <= 29) || (M ==  3 && D >= 1 && D <= 19))
            z ="Pisces";
        else if ((M ==  3 && D >= 20 && D <= 31) || (M ==  4 && D >= 1 && D <= 19))
            z ="Aries";
        else if ((M ==  4 && D >= 20 && D <= 30) || (M ==  5 && D >= 1 && D <= 20))
            z ="Taurus";
        else if ((M ==  5 && D >= 21 && D <= 31) || (M ==  6 && D >= 1 && D <= 20))
            z ="Gemini";
        else if ((M ==  6 && D >= 21 && D <= 30) || (M ==  7 && D >= 1 && D <= 22))
            z ="Cancer";
        else if ((M ==  7 && D >= 23 && D <= 31) || (M ==  8 && D >= 1 && D <= 22))
            z ="Leo";
        else if ((M ==  8 && D >= 23 && D <= 31) || (M ==  9 && D >= 1 && D <= 22))
            z ="Virgo";
        else if ((M ==  9 && D >= 23 && D <= 30) || (M == 10 && D >= 1 && D <= 22))
            z ="Libra";
        else if ((M == 10 && D >= 23 && D <= 31) || (M == 11 && D >= 1 && D <= 21))
            z ="Scorpio";
        else if ((M == 11 && D >= 22 && D <= 30) || (M == 12 && D >= 1 && D <= 21))
            z ="Sagittarius";
        else
            z = "Illegal date";
        return z;
    }
    
    private static String horoscope_selector (String zodiac) throws FileNotFoundException, IOException{
        
             int random =  (int) (Math.random()*(6-1)+1);
             String file = "files/" + zodiac +"/" + zodiac + "_0" + random + ".txt";
             StringBuilder stringBuilder = new StringBuilder();
             BufferedReader br = new BufferedReader(new FileReader(file));
             String fileContent;
             String line = null;
             while ((line = br.readLine()) != null) {
                 stringBuilder.append(line + "\n");
             }
 
             fileContent = stringBuilder.toString();
             return fileContent;
        
    }
}

         