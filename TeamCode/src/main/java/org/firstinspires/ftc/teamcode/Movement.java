package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous(name = "PowerPlayCV", group = "ppcv")
public class Movement extends LinearOpMode {

    DcMotor motorFrontLeft, motorBackLeft, motorFrontRight, motorBackRight;
    Ppbot robot = new Ppbot();
    final double speedScalar = 0.8;
    Movement vision = new Movement();
    int state1 = 0;
    int state2 = 0;
    int state3 = 0;
    double pwr = 0.1;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        telemetry.addData("Say", "Hello");
        telemetry.update();

        waitForStart();
        while(opModeIsActive()){

            if(SleeveDetection.truePath == 1){
                state1 = 1;
            }else if(SleeveDetection.truePath == 2){
                state2 = 1;
            }else if(SleeveDetection.truePath == 3){
                state3 = 1;
            }


            if(state1 == 1){
                Path1();
            }else if(state2 == 2){
                Path2();
            }else if(state3 == 3){
                Path3();
            }
        }

    }

    public void Path1(){
        moveLeft();
        sleep(3500);
        stopDrivebase();
        sleep(500);
        moveForward();
        sleep(3500);
        stopDrivebase();
    }

    public void Path2(){
        moveLeft();
        sleep(3500);
        stopDrivebase();
        sleep(500);
        moveForward();
        sleep(7000);
        stopDrivebase();
        sleep(500);
        moveRight();
        sleep(3500);
        stopDrivebase();
    }

    public void Path3(){
        moveRight();
        sleep(3500);
        stopDrivebase();
        sleep(500);
        moveForward();
        sleep(3500);
        stopDrivebase();
    }

    public void moveRight() {
        motorFrontLeft.setPower(0.15);
        motorFrontRight.setPower(-0.15);
        motorBackLeft.setPower(-0.15);
        motorBackRight.setPower(0.15);
    }

    public void moveLeft() {
        motorFrontLeft.setPower(-pwr);
        motorFrontRight.setPower(pwr);
        motorBackLeft.setPower(pwr);
        motorBackRight.setPower(-pwr);
    }

    public void moveForward() {
        motorFrontLeft.setPower(pwr);
        motorFrontRight.setPower(-pwr);
        motorBackLeft.setPower(-pwr);
        motorBackRight.setPower(pwr);
    }

    public void moveBackward() {
        motorFrontLeft.setPower(-pwr);
        motorFrontRight.setPower(-pwr);
        motorBackLeft.setPower(-pwr);
        motorBackRight.setPower(-pwr);
    }

    public void stopDrivebase() {
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }

    public void turnRight() {
        motorFrontLeft.setPower(pwr);
        motorFrontRight.setPower(-pwr);
        motorBackLeft.setPower(pwr);
        motorBackRight.setPower(-pwr);
    }

    public void turnLeft() {
        motorFrontLeft.setPower(-pwr);
        motorFrontRight.setPower(pwr);
        motorBackLeft.setPower(-pwr);
        motorBackRight.setPower(pwr);
    }

    public void P1() {
    }

    public void P2() {
    }

    public void P3() {
    }

}
