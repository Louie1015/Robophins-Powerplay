package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;


@Autonomous(name = "Signal Sleeve Test")
public class VisionTest extends LinearOpMode {


    private SleeveDetection sleeveDetection;


    OpenCvCamera webcam;

    // Name of the Webcam to be set in the config
    private String webcamName = "Webcam 1";
    //imports a new robot into the file
    Ppbot robot = new Ppbot();
    //motor power constant in our code
    double pwr = 0.35;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //this line COULD be jank, shouldn't be
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        //this is what it used to be:
        //camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
        sleeveDetection = new SleeveDetection();
        webcam.setPipeline(sleeveDetection);
       //webcam.setMillisecondsPermissionTimeout(5000);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()

        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(320,240, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
        waitForStart();
        while(opModeIsActive()) {
            sleep(5000);
            //close(); IF WE HAVE START CONE
            //sleep(500);


            if(SleeveDetection.truePath == 1){
                Path1();
                telemetry.addData("P1: ", sleeveDetection.getPosition());
                telemetry.update();
                break;
            }else if(SleeveDetection.truePath == 2){
                Path2();
                telemetry.addData("P2: ", sleeveDetection.getPosition());
                telemetry.update();
                break;
            }else if(SleeveDetection.truePath == 3){
                Path3();
                telemetry.addData("P3: ", sleeveDetection.getPosition());
                telemetry.update();
                break;
            }


        }


        while (!isStarted()) {
            telemetry.addData("ROTATION: ", sleeveDetection.getPosition());
            telemetry.update();
        }

    }

    public void Path1(){
        moveLeft();
        sleep(1700);
        stopDrivebase();
        sleep(500);
        moveForward();
        sleep(1500);

        stopDrivebase();
    }

    public void Path2(){
        moveForward();
        sleep(2000);
        stopDrivebase();
    }

    public void Path3(){
        moveRight();
        sleep(1400);
        stopDrivebase();
        sleep(500);
        moveForward();
        sleep(1300);
        stopDrivebase();
    }
    public void moveLeft() {
        robot.FLeft.setPower(-pwr);
        robot.FRight.setPower(pwr);
        robot.BLeft.setPower(-pwr*1.2);
        robot.BRight.setPower(-pwr);
    }

    public void moveRight() {
        robot.FLeft.setPower(pwr);
        robot.FRight.setPower(-pwr);
        robot.BLeft.setPower(pwr);
        robot.BRight.setPower(pwr*1.3);
    }

    public void moveForward() {
        robot.FLeft.setPower(pwr);
        robot.FRight.setPower(pwr);
        robot.BLeft.setPower(-pwr);
        robot.BRight.setPower(pwr);
    }

    public void moveBackward() {
        robot.FLeft.setPower(pwr);
        robot.FRight.setPower(pwr);
        robot.BLeft.setPower(pwr);
        robot.BRight.setPower(pwr);
    }

    public void stopDrivebase() {
        robot.FLeft.setPower(0);
        robot.FRight.setPower(0);
        robot.BLeft.setPower(0);
        robot.BRight.setPower(0);
    }

    public void turnLeft() {
        robot.FLeft.setPower(pwr);
        robot.FRight.setPower(-pwr);
        robot.BLeft.setPower(pwr);
        robot.BRight.setPower(-pwr);
    }

    public void turnRight() {
        robot.FLeft.setPower(-pwr);
        robot.FRight.setPower(pwr);
        robot.BLeft.setPower(-pwr);
        robot.BRight.setPower(pwr);
    }
    public void close() {
        robot.Take1.setPosition(0.17);// take 1 closed pos
        robot.Take2.setPosition(0.47);// take 2 closed pos
    }
    public void open() {
        robot.Take1.setPosition(0.08);// take 1 open pos
        robot.Take2.setPosition(0.61);// take 2 open pos
    }
}