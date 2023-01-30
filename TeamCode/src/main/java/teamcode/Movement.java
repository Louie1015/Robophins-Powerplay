package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import teamcode.VisionTest;
import org.openftc.easyopencv.OpenCvCamera;


@Autonomous(name = "PowerPlayCV", group = "ppcv")
public class Movement extends LinearOpMode {

    private VisionTest sleeveDetection;
    private OpenCvCamera camera;
    private String webcamName = "Webcam 1";
    //DcMotor motorFrontLeft, motorBackLeft, motorFrontRight, motorBackRight;
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

        while(opModeIsActive()) {

          /*  if(SleeveDetection.truePath == 1){
                state1 = 1;
                //break;
            }else if(SleeveDetection.truePath == 2){
                state2 = 1;
                //break;
            }else if(SleeveDetection.truePath == 3){
                state3 = 1;
                //break;
            }
            if(state1 == 1){
                Path1();
            }else if(state2 == 1){
                Path2();
            }else if(state3 == 1){
                Path3();
            }
        */
        }
        waitForStart();

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
        robot.FLeft.setPower(0.15);
        robot.FRight.setPower(-0.15);
        robot.BLeft.setPower(-0.15);
        robot.BRight.setPower(0.15);
    }

    public void moveLeft() {
        robot.FLeft.setPower(-pwr);
        robot.FRight.setPower(pwr);
        robot.BLeft.setPower(pwr);
        robot.BRight.setPower(-pwr);
    }

    public void moveForward() {
        robot.FLeft.setPower(pwr);
        robot.FRight.setPower(-pwr);
        robot.BLeft.setPower(-pwr);
        robot.BRight.setPower(pwr);
    }

    public void moveBackward() {
        robot.FLeft.setPower(-pwr);
        robot.FRight.setPower(-pwr);
        robot.BLeft.setPower(-pwr);
        robot.BRight.setPower(-pwr);
    }

    public void stopDrivebase() {
        robot.FLeft.setPower(0);
        robot.FRight.setPower(0);
        robot.BLeft.setPower(0);
        robot.BRight.setPower(0);
    }

    public void turnRight() {
        robot.FLeft.setPower(pwr);
        robot.FRight.setPower(-pwr);
        robot.BLeft.setPower(pwr);
        robot.BRight.setPower(-pwr);
    }

    public void turnLeft() {
        robot.FLeft.setPower(-pwr);
        robot.FRight.setPower(pwr);
        robot.BLeft.setPower(-pwr);
        robot.BRight.setPower(pwr);
    }

}