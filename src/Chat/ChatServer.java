package Chat;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

//communique avec pb
//Serveur de chat 
public class ChatServer implements Runnable {

    //flux de sortie
    PrintWriter out;
    //flux d'entrée
    BufferedReader in;
    Socket s;
    Scanner keyboard;
    int index;
    String input;
    boolean doRun = true;
    static SecretKey cle;

    public ChatServer(Socket a, int u) {
        s = a;
        keyboard = new Scanner(System.in);
        index = u;
    }

    public void run() {
        File file = new File("cle.txt");
        FileReader fread;
        try {
            //in et out initialisés à partir du socket
            com.sun.org.apache.xml.internal.security.Init.init();
            Cipher cipher = Cipher.getInstance("AES");
            fread = new FileReader(file.getAbsoluteFile());
            BufferedReader bread = new BufferedReader(fread);
            String key = bread.readLine();
            byte[] decodedKey = Base64.decode(key);
            cle = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
            bread.close();
            fread.close();

            try {
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream());

                System.out.println("connexion de " + s.getInetAddress().toString() + " sur le port " + s.getPort());
                String talk = in.readLine();

                while (doRun) {
                    while (talk == null) {
                        talk = in.readLine();
                    }
                    System.out.println("Message encodé : " + talk);
                    cipher.init(Cipher.DECRYPT_MODE, cle);
                    byte[] base64decodedTokenArr;
                    base64decodedTokenArr = Base64.decode(talk.getBytes());
                    byte[] res2 = cipher.doFinal(base64decodedTokenArr);
                    //byte[] res2 = cipher.doFinal(Base64.decode(talk));
                    talk = new String(res2);
                    System.out.println("Message décodé : " + talk);
                    if (talk.compareToIgnoreCase("bye") == 0) {
                        System.out.println("shutting down following remote request");
                        doRun = false;
                    } else {
                        System.out.print("to client#" + index + "> ");
                        input = keyboard.nextLine();
                        //envoi de ce qui est saisie
                        cipher.init(Cipher.ENCRYPT_MODE, cle);
                        byte[] res = cipher.doFinal(input.getBytes("UTF-8"));
                        String res_str = Base64.encode(res);
                        //new String(res);
                        out.println(res_str);
                        out.flush();
                        if (input.compareToIgnoreCase("bye") == 0) {
                            System.out.println("server shutting down");
                            doRun = false;
                        } else {
                            talk = in.readLine();
                        }
                    }
                }
                s.close();
            } catch (Exception e) {
                System.out.println("raaah! what did u forget this time?");
                e.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Base64DecodingException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
