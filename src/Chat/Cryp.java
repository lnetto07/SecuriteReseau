package Chat;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.util.Base64.*;

public class Cryp {

    /**
     * @param args
     */
    //encode les méssage avec AES
    final String encryptedValue = "I saw the real you";
    String secKey = "ubutru";

    public static void main(String[] args) {

        String encryptedVal = null;
        String valueEnc = "aazzaa";
        //valueEnc chaine à chiffrer avec AES

        try {
            //Générateur de clé AES    	
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            //init
            generator.init(128);
            //création de la clé
            SecretKey key = generator.generateKey();
            //choix de la méthode= suit l'algorithme AES
            Cipher cipher = Cipher.getInstance("AES");
            //init, chiffrer ou déchiffrer, ici on va chiffrer param 1= Cipher.Encrypt_mode
            //param 2 clé à utiliser
            //On a le crptosystème
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //le passage dans cipher.dofinal est le résultat du chiffrement
            //valueEnc est ici chiffré et transformer en tableau d'octets
            byte[] res = cipher.doFinal(valueEnc.getBytes());
            //res est le cryptogramme, pour l'affichage on le transforme en string
            String res_str = Base64.encode(res);//new String(res);
            //pour déchiffrer DECRYPT_MODE	 
            cipher.init(Cipher.DECRYPT_MODE, key);
            
            //résultat du déchiffrement	               
            byte[] res2 = cipher.doFinal(Base64.decode(res_str));
            //byte[] res2 = cipher.doFinal(res_str.getBytes("utf-8"));
            String res_str2 = new String(res2);
            //affichage valeur de départ , valeur chiffrée et déchiffrée
            System.out.println("source:" + valueEnc);
            System.out.println("enc:" + res_str);
            System.out.println("dec:" + res_str2);

            //chiffrement déchiffrement ok
            //Maintenant il faut rendre la clé inerte                    
            //passage en tableau de bit
            byte[] enck = key.getEncoded();
            System.out.println(Base64.encode(enck));

            //encoded résultat d"un chiffrement préalable       
            String encoded = "r1peJOWYRRod8IibmrYoPA==";
            //clé en clair        
            String key_str = "+WHQtDsr9LJQ05/2MHZkQQ==";
            //transformation de key_str en clé AES
            byte[] kb = Base64.decode(key_str.getBytes());
            SecretKeySpec ksp = new SecretKeySpec(kb, "AES");
            //cipher a partir de la clé 
            Cipher cipher2 = Cipher.getInstance("AES");
            cipher2.init(Cipher.DECRYPT_MODE, ksp);
            byte[] res3 = cipher2.doFinal(Base64.decode(encoded));
            String res_str3 = new String(res3);
            System.out.println("obtained: " + res_str3);

        } catch (Exception ex) {
            System.out.println("The Exception is=" + ex);
        }

    }

}
