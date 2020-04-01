package Chat;


import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
//import java.net.UnknownHostException;

//Serveur-bureau d'accueil, initialise la connexion
//Doit être lancé en premier
//une fois la connexon établit le serveur démarre le chat
//Possibilité de plusieurs chat en parallèle(5 max)
public class PC {

    static ServerSocket server;
    static int clientID = 0;
    static SecretKey cle;

    public static void main(String ard[]) {

        try {
            File file = new File("cle.txt");
            System.out.println("Création du fichier et de la clé");
            //generation de la clé
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            SecretKey cle = generator.generateKey();
            //passage en string 
            String cle_str;
            byte[] encoded = cle.getEncoded();
            cle_str = Base64.encode(encoded);
            FileWriter fwrite = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bwrite = new BufferedWriter(fwrite);
            bwrite.write(cle_str);
            bwrite.close();
            fwrite.close();
            System.out.println("Clé : " + cle_str);
            server = new ServerSocket(4444, 5);//5 connexions clientes au plus
            go();
        } catch (Exception e) {
        }
    }

    public static void go() {

        try {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true)//
                    {
                        try {
                            Socket client = server.accept();
                            // Faire tourner le socket qui s'occupe de ce client dans son propre thread et revenir en attente de la prochaine connexion
                            // Le chat avec l'entit� connect�e est encapsul� par une instance de ChatServer
                            Thread tAccueil = new Thread(new ChatServer(client, clientID));
                            tAccueil.start();
                            clientID++;
                        } catch (Exception e) {
                        }
                    }
                }
            });
            t.start();

        } catch (Exception i) {
            System.out.println("Impossible d'�couter sur le port 4444: serait-il occup�?");
            i.printStackTrace();
        }
    }
}
