package ro.brickbot.cnu.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import ro.brickbot.cnu.teamcode.utils.Constants;

@SuppressWarnings("All")
public class Robot {
    //Telemetria face Romania
    public Telemetry telemetry;

    //DcMotors aici. 4 motoare roti, 1 brat, 1 intake
    public static DcMotor[] motors = new DcMotor[4];
    public static DcMotor brat = null;
    public static DcMotor intake = null;

    //Servo aici
    public static CRServo carusel = null;

    //LED-ul aici
    public static DigitalChannel led;

    static {
        led = null;
    }

    //Senzor Distanta
    NormalizedColorSensor sensor = null;

    public Robot(HardwareMap hardwareMap) {

        //Mapare motoare
        motors[0] = hardwareMap.get(DcMotor.class, "frontLeft");
        motors[1] = hardwareMap.get(DcMotor.class, "backLeft");
        motors[2] = hardwareMap.get(DcMotor.class, "backRight");
        motors[3] = hardwareMap.get(DcMotor.class, "frontRight");

        //Mapare brat
        brat = hardwareMap.get(DcMotor.class, "motorBrat");
        intake = hardwareMap.get(DcMotor.class, "motorIntake");

        //CRServo
        carusel = hardwareMap.get(CRServo.class, "motorRoata");

        //Color Sensor
        sensor = hardwareMap.get(NormalizedColorSensor.class, "senzor");

        //LED
        led = hardwareMap.get(DigitalChannel.class, "led");
        led.setMode(DigitalChannel.Mode.OUTPUT);

        //Setarea modului de rulare pentru motoare
        for(int i = 0; i < 4; i++) {
            motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motors[i].setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        //Setarea directiei motoarelor -> motoare stanga = forward; motoare dreapta = reverse
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[1].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[2].setDirection(DcMotorSimple.Direction.FORWARD);
        motors[3].setDirection(DcMotorSimple.Direction.FORWARD);

        //Brat directie
        brat.setDirection(DcMotorSimple.Direction.REVERSE);

        brat.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        brat.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        brat.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //Resetare motoare roti

        //LED
        led.setState(false);

        resetMotors();
    }

    //Resetarea encoder-elor
    public void resetMotors() {
        for(int i = 0; i < 4; i++) {
            motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motors[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    //Setarea puterii motoarelor
    public void setMotorPower(double[] speeds) {
        if(speeds.length == motors.length) {
            for(int i = 0; i < motors.length; i++) {
                motors[i].setPower(speeds[i]);
            }
        }
    }

    public void bratEncoder(int tpr) {
        brat.setTargetPosition(tpr);
        brat.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brat.setPower(Constants.BRAT_SPEED);
    }
    public boolean hasElementInIntake(){
        return ((DistanceSensor) sensor).getDistance(DistanceUnit.CM) < Constants.MIN_DIST;
    }

}
