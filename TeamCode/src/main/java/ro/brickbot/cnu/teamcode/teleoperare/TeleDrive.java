package ro.brickbot.cnu.teamcode.teleoperare;

@SuppressWarnings("All")
public class TeleDrive {

    //Drive-ul in sine
    public static double[] mecanumDrive(double x, double y, double turn) {
        double[] wheelSpeeds = {0, 0, 0, 0};

        //Calcul matematic :)
        wheelSpeeds[0] = x + y - turn;
        wheelSpeeds[1] = -x + y - turn;
        wheelSpeeds[2] = x + y + turn;
        wheelSpeeds[3] = -x + y + turn;

        //Normalizare viteze
        wheelSpeeds = normalize(wheelSpeeds);

        //Returnez array-ul
        return wheelSpeeds;
    }

    public static double[] normalize(double[] speeds) {
        double max = 0;

        //Vedem maximul
        for(int i = 0; i < 4; i++) {
            if(Math.abs(speeds[i]) > max) {
                max = Math.abs((speeds[i]));
            }
        }

        //Impartim viteza la maxim in cazul in care max > 1
        if(max > 1) {
            for(int i = 0; i < 4; i++) {
                speeds[i] /= max;
            }
        }

        //Returnez array-ul
        return speeds;
    }

}
