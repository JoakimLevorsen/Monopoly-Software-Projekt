package monopoly;

import org.javalite.activejdbc.Base;

/**
DatabaseBase:
En klasse til at åbne og lukke forbindelser til databasen.

@author Joakim Levorsen, S185023
*/
public class DatabaseBase {
    public static void openBase() {
        // Følgende er:
        // Driver der skal bruges (MÅ IKKE ÆNDRES)
        // URL til databasen
        // Brugernavn til databasen
        // Kodeord til databasen
        Base.open("com.mysql.cj.jdbc.Driver", "jdbc:mysql://ec2-52-30-211-3.eu-west-1.compute.amazonaws.com/s185023",
                "s185023", "t0MzfHeQBfHIlo8ociaB2");
    }

    public static void openTransaction() {
        Base.openTransaction();
    }

    public static void commitTransaction() {
        Base.commitTransaction();
    }

    public static void rollBackTransaction() {
        Base.rollbackTransaction();
    }

    public static void closeBase() {
        Base.close();
    }
}