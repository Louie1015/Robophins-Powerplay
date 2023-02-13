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

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Map;

import teamcode.Components.Lighting;
import teamcode.Components.MainRobot;
import teamcode.firstinspires.ftc.robotcontroller.external.samples.SampleRevBlinkinLedDriver;

class Ppbot{
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
    Ppbot robot = new Ppbot();
    double x;
    double y;
    double rx;
    double Slidepos = 0.0;
    double Hpos = 0.0;
    final double Armspeed = 0.1;
    final double Slidespeed = 1.0;
    final double Hspeed = 1.0;
    final double rotationScalar = 0.35;
    final double speedScalar = 0.5;
    double drivespeed = 0.2;
    boolean closed = true;

    double pos1 = 0;
    double pos2 = 0;
    boolean wasPressedLastTick = true;
    boolean endOfAuto = false;
    int ticksLeft= 0;
    int stoppedTicks =0;



    @Override

    public void runOpMode(){
        robot.init(hardwareMap);
        telemetry.addData("Say", "Hello");
        telemetry.update();

        waitForStart();

        //while we balling
        while(opModeIsActive()){
            // y = forward/back x = left strafe/right strafe, rx = rotation
            y = -gamepad1.left_stick_y;
            x = gamepad1.left_stick_x;
            rx = gamepad1.right_stick_x;

            //if not turning, do a little driving.
            if (gamepad1.dpad_up)
                y = 0.41;
            if (gamepad1.dpad_down)
                y = -0.41;
            if (gamepad1.dpad_left)
                x = -0.6;
            if (gamepad1.dpad_right)
                x = 0.6;
            if (Math.abs(rx) > 0.03){
                robot.BLeft.setPower(rotationScalar * -rx);
                robot.BRight.setPower(rotationScalar * -rx);
                robot.FLeft.setPower(rotationScalar * rx);
                robot.FRight.setPower(rotationScalar * -rx);
            }
            // if we want to go foward/back do a little motor powering
            else if (Math.abs(y) >= Math.abs(x) && Math.abs(y) > 0.03){
                robot.BLeft.setPower(speedScalar * -y/*0.7*/); //-
                robot.BRight.setPower(speedScalar * y/*0.7*/);
                robot.FLeft.setPower(speedScalar * y/*0.85*/); //-
                robot.FRight.setPower(speedScalar * y); //0

            }
            //if we want to go strafing, set a little moter powerfing for strafing
            else if (Math.abs(x) > (Math.abs(y)) && Math.abs(x) > 0.03){
                robot.BLeft.setPower(speedScalar * x/* (0.7 * 1.4)the 1.4 is for post-new drivebase, for refrence later*/);
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

            Hpos = 0.0;
            if (robot.HorizontalTouch.isPressed() && !wasPressedLastTick) { // stop hslide spool momentum
                Hpos -= 0.05 ;
                wasPressedLastTick = true;
            } else if (!robot.HorizontalTouch.isPressed()) {
                wasPressedLastTick = false;
            }
            if ((gamepad2.y || gamepad1.y) && !(robot.HorizontalTouch.isPressed())) // if limit switch is not pressed
                Hpos += Hspeed;
            else if (gamepad1.a || gamepad2.a)
                Hpos -= Hspeed ;
            Slidepos = 0.0;

            if (gamepad1.left_bumper ||gamepad2.left_bumper || ticksLeft>0) { // press left bumper to initate auto ascend
                Slidepos = 1;
                ticksLeft = 1600; //THIS VALUE IS HOW FAR THE SLIDES WILL GO UP TODO: NEEDS TUNING
                ticksLeft--;
            }
            if (ticksLeft == 0) {
                stoppedTicks = 2000; // HOW MANY TICKS THE SLIDE IS STOPPED AFTERWARDS TODO: NEEDS TUNING
            }
            if (stoppedTicks > 0) {
                endOfAuto = true;
            }
            if (Math.abs(gamepad1.right_trigger) > 0.0 || Math.abs(gamepad2.right_trigger) > 0.0) // uppy
                Slidepos += Slidespeed / 1.1;
            if (gamepad1.right_bumper || gamepad2.right_bumper || endOfAuto) { // stopper
                Slidepos += Slidespeed / 8;
            }
            if (Math.abs(gamepad1.left_trigger) > 0.0 || Math.abs(gamepad2.left_trigger) > 0.0) {// downy
                Slidepos -= Slidespeed / 3;
            }
            //open close :)
            if (gamepad1.x || gamepad2.x) {
                closed = true;
            } else if (gamepad1.b || gamepad2.b) {
                closed = false;
            }
            if (closed) {
                robot.Take1.setPosition(0.21);// take 1 closed pos
                robot.Take2.setPosition(0.41);// take 2 closed pos
            } else {
                robot.Take1.setPosition(0.03);// take 1 open pos
                robot.Take2.setPosition(0.63);// take 2 open pos
            }
            //set power and position for grabby and shit
            if (Slidepos>= 1) { // flooring everything above max motor output to 1
                Slidepos = 1;
            }
            if (Hpos >= 1) {
                Hpos =1;
            }
            robot.Slider1.setPower(Slidepos);
            robot.Slider2.setPower(Slidepos);
            robot.Hslide.setPower(Hpos);

            //led copium


            if (isRed()|| isBlue() || isYellow() || isClose()) {
                robot.LedLight.blinkOrange();
                telemetry.addData("ledOutPut","True");
            } else {
                robot.LedLight.blinkBlue();
                telemetry.addData("ledOutPut","False");
            }



            //telemetry :nerd_emoji:
            telemetry.addData("x","%.2f", x);
            telemetry.addData("y","%.2f", y);
            telemetry.addData("servo1","%.2f", robot.Take1.getPosition());
            telemetry.addData("servo2","%.2f", robot.Take2.getPosition()); // REMEMBER TO CONFIGURE THIS ON PHONE
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