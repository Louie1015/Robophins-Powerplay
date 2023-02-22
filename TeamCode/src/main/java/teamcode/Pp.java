package teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Light;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import teamcode.trajectorysequence.TrajectorySequence;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Map;

import teamcode.Components.Lighting;
import teamcode.Components.MainRobot;
import teamcode.firstinspires.ftc.robotcontroller.external.samples.SampleRevBlinkinLedDriver;

class Ppbot{ //WE ARE NOT USING PPBOT
    public DcMotor BLeft = null;
    public DcMotor BRight = null;
    public DcMotor FLeft = null;
    public DcMotor FRight = null;
    public DcMotor Slider1 = null;
    public DcMotor Slider2 = null;
    public DcMotor Hslide = null;
    public Servo Take1 = null;
    public Servo Take2 = null;

    public ColorSensor KTsensor = null;
    public DistanceSensor BMsensor = null;
    public TouchSensor HorizontalTouch = null;
    HardwareMap map = null;
    public Lighting LedLight;


    public void init(HardwareMap maps) {
        map = maps;
        BLeft = maps.dcMotor.get("bl");
        BRight = maps.dcMotor.get("br");
        FLeft = maps.dcMotor.get("fl");
        FRight = maps.dcMotor.get("fr");
        Hslide = maps.dcMotor.get("hs");
        Take1 = maps.servo.get("grabber");
        Take2 = maps.servo.get("grabber2");
        Slider1 = maps.dcMotor.get("slider1");
        Slider2 = maps.dcMotor.get("slider2");
        KTsensor = maps.get(ColorSensor.class, "Color");
        BMsensor = maps.get(DistanceSensor.class, "Color");
        HorizontalTouch = map.get(TouchSensor.class, "Touch");
        LedLight = new Lighting(map);

        LedLight.blinkBlue();

        BLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        BRight.setDirection(DcMotorSimple.Direction.REVERSE);
        FLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        FRight.setDirection(DcMotorSimple.Direction.REVERSE);
        Hslide.setDirection(DcMotorSimple.Direction.FORWARD);
        Slider1.setDirection(DcMotorSimple.Direction.REVERSE);
        Slider2.setDirection(DcMotorSimple.Direction.FORWARD);

        BLeft.setPower(0.0);
        BRight.setPower(0.0);
        FLeft.setPower(0.0);
        FRight.setPower(0.0);
        Slider1.setPower(0.0);
        Slider2.setPower(0.0);
        Hslide.setPower(0.0);

        BLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Slider1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Slider2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hslide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


}

@TeleOp (name = "PowerPlaybot", group = "pp")

public class Pp extends LinearOpMode{
    MainRobot robot;
    double x;
    double y;
    double rx;
    double Slidepos = 0.0;
    double Hpos = 0.0;
    final double Armspeed = 0.1;
    final double Slidespeed = 1.0;
    final double Hspeed = 1.0;
    final double rotationScalar = 0.35;
    final double speedScalar = 0.75;
    double drivespeed = 0.2;
    boolean closed = true;

    double pos1 = 0;
    double pos2 = 0;
    boolean wasPressedLastTick = true;
    boolean autosliding = false;
    boolean nextPhase = false;
    boolean holdie_cow = false;
    int ticksLeft= 0;
    int ticksLeft2 = 0;



    @Override

    public void runOpMode(){
        robot = new MainRobot(hardwareMap, telemetry);
        double sH = 0;
        int cringeHoldButton =0;
        //robot.BLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        telemetry.addData("Say", "Hello");
        telemetry.update();
        //pid for auto
        waitForStart();

        //while we balling
        while(opModeIsActive()){
            /*Pose2d startPose = robot.getPoseEstimate(); UNCOMMENT IF U WANT PID BACK, BUT U WILL HAVE MORE INPUT LAG AND SLOWER TICKRATE
            Trajectory ff1 = robot.trajectoryBuilder(startPose, Math.toRadians(sH))
                    .back(13)
                    .build();
            Trajectory bb1 = robot.trajectoryBuilder(startPose, Math.toRadians(sH))
                    .forward(13)
                    .build();*/
            // y = forward/back x = left strafe/right strafe, rx = rotation
            y = -gamepad1.left_stick_y;
            x = gamepad1.left_stick_x;
            rx = gamepad1.right_stick_x;
            rx += gamepad2.right_stick_x / 2;
            // if l/r dpad, do a little rotating
            /*if (gamepad1.dpad_left) {
                rx=-1;
            } else if (gamepad1.dpad_right) {
                rx=1;
            }*/

            //if not turning, do a little driving.
            /*if (Math.abs(gamepad1.right_stick_y) + Math.abs(gamepad1.right_stick_x) > 0.03){
                if (Math.abs(gamepad1.right_stick_y) > Math.abs(gamepad1.right_stick_x)) {
                    y = gamepad1.right_stick_y * -0.41;
                } else {
                    x = gamepad1.right_stick_x * 0.6;
                }
            }*/
            if (gamepad1.dpad_up) {
                y=0.41;
            } else if (gamepad1.dpad_down) {
                y=-0.41;
            } else if (gamepad1.dpad_left) {
                x=-0.6 * 0.666;
            } else if (gamepad1.dpad_right) {
                x=0.6 * 0.666;
            }
            if (Math.abs(rx) > 0.03){
                robot.BLeft.setPower(rotationScalar * rx);
                robot.BRight.setPower(rotationScalar * -rx);
                robot.FLeft.setPower(rotationScalar * rx);
                robot.FRight.setPower(rotationScalar * -rx);
            }
            // if we want to go foward/back AND WE ARE NOT ALREADY AUTOING do a little motor powering
            else if (Math.abs(y) >= Math.abs(x) && Math.abs(y) > 0.03 && !autosliding) {
                robot.BLeft.setPower(speedScalar * y/*0.7*/ * 0.666); //-
                robot.BRight.setPower(speedScalar * y/*0.7*/ * 0.666);
                robot.FLeft.setPower(speedScalar * y/*0.85*/ * 0.666); //-
                robot.FRight.setPower(speedScalar * y * 0.666); //0
            }
            //if we want to go strafing, set a little moter powerfing for strafing
            else if (Math.abs(x) > (Math.abs(y)) && Math.abs(x) > 0.03){
                robot.BLeft.setPower(speedScalar * -x/* (0.7 * 1.4)the 1.4 is for post-new drivebase, for refrence later*/);
                robot.BRight.setPower(speedScalar * x /*(0.7 * 1.4)*/);
                robot.FLeft.setPower(speedScalar * x * 0.9 /*0.85*/);
                robot.FRight.setPower(speedScalar * 0.9 * -x);
                //if we dont want to move make sure we dont move
            } else {
                robot.BLeft.setPower(0);
                robot.BRight.setPower(0);
                robot.FLeft.setPower(0);
                robot.FRight.setPower(0);
            }
            //uppy downy. <- NOT DOWNY madge <- YES DOWNY WE HAVE RETRACTION NOW
            //impulse pog
            Hpos = 0.0;
            if (robot.HorizontalTouch.isPressed() && !wasPressedLastTick) { // stop hslide spool momentum
                Hpos += 0.10 ;
                wasPressedLastTick = true;
            } else if (!robot.HorizontalTouch.isPressed()) {
                wasPressedLastTick = false;
            }
            if ((gamepad2.y || gamepad1.y) && !(robot.HorizontalTouch.isPressed())) // if limit switch is not pressed
                Hpos -= Hspeed;
            else if (gamepad1.a || gamepad2.a)
                Hpos += Hspeed ;
            Slidepos = 0.0;

            /*cringeHoldButton--; UNCOMMENT FOR PID BACK
            if (gamepad2.dpad_up && cringeHoldButton <=0) { //goes forward to pick up cone
                robot.followTrajectory(bb1);
                cringeHoldButton = 10;
            }

            if (gamepad2.dpad_down && cringeHoldButton <= 0) { //goes backwards  to place cone
                robot.followTrajectory(ff1);
                cringeHoldButton = 10;
            }*/


            if (Math.abs(gamepad1.right_trigger) > 0.0 || Math.abs(gamepad2.right_trigger) > 0.0 ) // uppy
                Slidepos += Slidespeed / 1.1;
            if (gamepad1.right_bumper || gamepad2.right_bumper ) { // stopper
                Slidepos += Slidespeed / 8;
            }
            if (Math.abs(gamepad1.left_trigger) > 0.0 || Math.abs(gamepad2.left_trigger) > 0.0) {// downy
                Slidepos -= Slidespeed / 1.5;
            }
            if (gamepad1.left_bumper || gamepad2.left_bumper) {
                Slidepos -= Slidespeed / 6;
            }
            //open close :)
            if (gamepad1.x || gamepad2.x) {
                closed = true;
            } else if (gamepad1.b || gamepad2.b) {
                closed = false;
            }
            if (closed) {
                robot.grabber.Take1.setPosition(0.21);// take 1 closed pos
                robot.grabber.Take2.setPosition(0.41);// take 2 closed pos
            } else {
                robot.grabber.Take1.setPosition(0.03);// take 1 open pos
                robot.grabber.Take2.setPosition(0.63);// take 2 open pos
            }
            //set power and position for grabby and shit
            if (Slidepos>= 1) { // flooring everything above max motor output to 1
                Slidepos = 1;
            }
            if (Hpos >= 1) {
                Hpos =1;
            }
            robot.slides.slider1.setPower(Slidepos);
            robot.slides.slider2.setPower(Slidepos);
            robot.slides.Hslide.setPower(Hpos);

            //led copium


            if (isRed()|| isBlue() || isYellow() || isClose()) {
                robot.lighting.blinkOrange();
                telemetry.addData("ledOutPut","True");
            } else {
                robot.lighting.blinkBlue();
                telemetry.addData("ledOutPut","False");
            }



            //telemetry :nerd_emoji:
            telemetry.addData("x","%.2f", x);
            telemetry.addData("y","%.2f", y);
            telemetry.addData("servo1","%.2f", robot.grabber.Take1.getPosition());
            telemetry.addData("servo2","%.2f", robot.grabber.Take2.getPosition()); // REMEMBER TO CONFIGURE THIS ON PHONE
            //color sensor telemetry NOT ACTIVATED
            telemetry.addData("Red", robot.KTsensor.red());
            telemetry.addData("Green", robot.KTsensor.green());
            telemetry.addData("Blue", robot.KTsensor.blue());
            //distance sensor telemetry
            telemetry.addData("Distance (cm)", "%.02f", robot.BMsensor.getDistance(DistanceUnit.CM));
            //limit switch telemetry
            if (true) {
                telemetry.addData("LimitSwitch", robot.HorizontalTouch.getValue());
            } else {
                telemetry.addData("LimitSwitch", " is not pressed");
            }

            telemetry.update();

            sleep(50);
        }
    }
    public void moveRight() {
        x = 0.3;
    }

    public void moveLeft() {
        x = -0.3;
    }

    public void moveForward() {
        y = 0.2;
    }

    public void moveBackward() {
        y = -0.2;
    }

    public boolean isRed() {
        int red = robot.KTsensor.red();
        int blue = robot.KTsensor.blue();
        int green = robot.KTsensor.green();
        return (red > 25 && red - blue >3);
    }
    public boolean isBlue() {
        int red = robot.KTsensor.red();
        int blue = robot.KTsensor.blue();
        int green = robot.KTsensor.green();
        return (blue > 25 && blue - red >3);
    }public boolean isYellow() {
        int red = robot.KTsensor.red();
        int blue = robot.KTsensor.blue();
        int green = robot.KTsensor.green();
        return (blue + red >= 50 || (blue+red)/2 > green);
    }

    public boolean isClose() {
        return 100 >= robot.BMsensor.getDistance(DistanceUnit.CM);
    }
}