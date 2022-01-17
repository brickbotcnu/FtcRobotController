package ro.brickbot.cnu.teamcode;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

import com.qualcomm.robotcore.util.Range;


import ro.brickbot.cnu.teamcode.hardware.Robot;
import ro.brickbot.cnu.teamcode.teleoperare.TeleDrive;
import ro.brickbot.cnu.teamcode.utils.Constants;

@TeleOp(name = "TeleOp", group = "CNU")

@SuppressWarnings("All")
public class TeleOp15996 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        //Hardware + pasez mapare
        Robot robot = new Robot(hardwareMap);

        //Variabile pentru miscare
        double xDrive;
        double yDrive;
        double turn;
        int valEncoderBrat;

        //Status element in cupa + LED
        boolean element = false;
        boolean led_status = false;

        boolean goldFound = false;      // Sound file present flags
        boolean silverFound = false;

        boolean tipat = false;
        boolean amTipat = false;

        //Constanta pentru corectie brat
        int corectie = 0;

        //Mesaj frumos si asteptare pentru start
        telemetry.addData("=>", "Press play to start!");
        telemetry.update();

        int silverSoundID = hardwareMap.appContext.getResources().getIdentifier("culcat", "raw", hardwareMap.appContext.getPackageName());

        if (silverSoundID != 0)
            silverFound = SoundPlayer.getInstance().preload(hardwareMap.appContext, silverSoundID);

        waitForStart();

        while(!isStopRequested()) {

            if(gamepad1.a) robot.led.setState(true);


            element = robot.hasElementInIntake();
            tipat = robot.hasElementInIntake();


            if(element && (tipat=robot.hasElementInIntake()) && !amTipat) {
                    SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, silverSoundID);
            }

            amTipat = tipat;

            /*if(element && !gamepad2.a && !gamepad2.b && !gamepad2.y) {
                valEncoderBrat=Constants.BRAT_LEVEL_1+corectie;
                valEncoderBrat= Range.clip(valEncoderBrat, Constants.BRAT_LEVEL_MIN,Constants.BRAT_LEVEL_MAX);
                robot.bratEncoder(valEncoderBrat);
            }

*/


            if(gamepad1.left_trigger > 0) {
                yDrive = -gamepad1.left_stick_y * Constants.SLOW_DRIVE_SPEED;
                xDrive = gamepad1.left_stick_x * Constants.SLOW_DRIVE_SPEED;
                turn = -gamepad1.right_stick_x * Constants.SLOW_TURN_SPEED;
            } else {
                yDrive = -gamepad1.left_stick_y * Constants.FULL_DRIVE_SPEED;
                xDrive = gamepad1.left_stick_x * Constants.FULL_DRIVE_SPEED;
                turn = -gamepad1.right_stick_x * Constants.FULL_TURN_SPEED;
            }

            double[] motorPowers = TeleDrive.mecanumDrive(xDrive, yDrive, turn);
            robot.setMotorPower(motorPowers);

            if (gamepad2.dpad_up) {
                corectie+=1;
            }
            if (gamepad2.dpad_down) {
                corectie-=1;
            }
            corectie=Range.clip(corectie,-100,100);

            //Telemetrie
            telemetry.addData("Encoder brat ", robot.brat.getCurrentPosition());
            telemetry.addData("Are element: ", robot.hasElementInIntake());
            telemetry.addData("Corectie: ", corectie);

            telemetry.update();




            if (gamepad2.a) {
                // Brat Level 1;
                valEncoderBrat=Constants.BRAT_LEVEL_1+corectie;
                valEncoderBrat= Range.clip(valEncoderBrat, Constants.BRAT_LEVEL_MIN,Constants.BRAT_LEVEL_MAX);
                robot.bratEncoder(valEncoderBrat);
            }
            if (gamepad2.b) {
                // Brat Level 2;
                valEncoderBrat=Constants.BRAT_LEVEL_2+corectie;
                valEncoderBrat= Range.clip(valEncoderBrat,Constants.BRAT_LEVEL_MIN,Constants.BRAT_LEVEL_MAX);
                robot.bratEncoder(valEncoderBrat);
            }
            if (gamepad2.y) {
                // Brat Level 3;
                valEncoderBrat=Constants.BRAT_LEVEL_3;
                valEncoderBrat= Range.clip(valEncoderBrat,Constants.BRAT_LEVEL_MIN,Constants.BRAT_LEVEL_MAX);
                robot.bratEncoder(valEncoderBrat);
            }
            if (gamepad2.x) {
                // Brat Level 0;
                corectie=0;
                valEncoderBrat=Constants.BRAT_LEVEL_MIN+corectie;
                valEncoderBrat= Range.clip(valEncoderBrat,Constants.BRAT_LEVEL_MIN,Constants.BRAT_LEVEL_MAX);
                robot.bratEncoder(valEncoderBrat);
            }
            if (gamepad2.dpad_right) {
                robot.carusel.setDirection(CRServo.Direction.FORWARD);
                robot.carusel.setPower(Constants.SERVO_SPEED);
            }else if (gamepad2.dpad_left) {
                robot.carusel.setDirection(CRServo.Direction.REVERSE);
                robot.carusel.setPower(Constants.SERVO_SPEED);
            }else {
                robot.carusel.setPower(0);
            }
            if (gamepad2.left_trigger > 0) {
                robot.intake.setPower(gamepad2.left_trigger);
            } else if (gamepad2.right_trigger > 0) {
                robot.intake.setPower(-gamepad2.right_trigger);
            } else {
                robot.intake.setPower(0);
            }

        }
    }
}