package teamcode.drive.OpModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;

import teamcode.Components.MainRobot;
import teamcode.Components.Cammy;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous
public class CamWithPidRight extends LinearOpMode{
    private Cammy camlyn;
    public static double firstleftdist = 21.75;
    public static double firstcycleright = 10.5;


    OpenCvCamera webcam;

    // Name of the Webcam to be set in the config
    private String webcamName = "Webcam 1";
    //imports a new robot into the file
    MainRobot mainRobot;
    //motor power constant in our code
    double pwr = 0.35;

    @Override
    public void runOpMode() throws InterruptedException {
        mainRobot = new MainRobot(hardwareMap, telemetry);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //this line COULD be jank, shouldn't be
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        //this is what it used to be:
        //camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, webcamName), cameraMonitorViewId);
        camlyn = new Cammy();
        webcam.setPipeline(camlyn);
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
        int sleeveStage = Cammy.truePath;
        telemetry.addData("Path: ", camlyn.getPosition());
        telemetry.update();
        while(opModeIsActive()) {
            sleep(2000);

            // if you want code to run before we check which sleeve is detected(ex:close grabber on startup), run it here

            if(sleeveStage == 1){
                Path1();
                break;
            }else if(sleeveStage == 2){
                Path2();
                break;
            }else if(sleeveStage == 3){
                Path3();
                break;
            }
            // conversely, if you want to run code after, put it here


        }


        while (!isStarted()) {
            telemetry.addData("ROTATION: ", camlyn.getPosition());
            telemetry.update();
        }

    }
    public Pose2d tempPose;
    public void Path1(){
        drop1();
        //park into left sector (Yellow)
        Trajectory yellowTraj1 = mainRobot.trajectoryBuilder(tempPose)
                .strafeLeft(12)
                .build();
        mainRobot.followTrajectory(yellowTraj1);
    }

    public void Path2(){
        drop1();
        //park into middle sector (Blue)
        Trajectory blueTraj1 = mainRobot.trajectoryBuilder(tempPose)
                .strafeRight(12)
                .build();
        mainRobot.followTrajectory(blueTraj1);
    }

    public void Path3(){
        drop1();
        //park into right sector (Magenta)
        Trajectory magentaTraj1 = mainRobot.trajectoryBuilder(tempPose)
                .strafeRight(35)
                .build();
        mainRobot.followTrajectory(magentaTraj1);
    }
    public void drop1() {
        Pose2d startPose = new Pose2d(-34, 70, Math.toRadians(-90));
        mainRobot.setPoseEstimate(startPose);

        Trajectory firstLeft = mainRobot.trajectoryBuilder(startPose)
                .strafeLeft(firstleftdist)
                .build();

        Trajectory firstForward = mainRobot.trajectoryBuilder(firstLeft.end())
                .forward(46.5)
                .build();

        Trajectory firstRight = mainRobot.trajectoryBuilder(firstForward.end())
                .strafeRight(firstcycleright)
                .build();

        Trajectory creep = mainRobot.trajectoryBuilder(firstRight.end())
                .forward(3)
                .build();

        Trajectory backcreep = mainRobot.trajectoryBuilder(creep.end())
                .back(4)
                .build();
        int step = 0;
        mainRobot.grabber.closeGrabber(); // close on init
        mainRobot.pause(800);
        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();

        mainRobot.slides.setSlidesPower(1.0); // lift so it doesnt fall
        mainRobot.pause(300);
        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();

        mainRobot.slides.setSlidesPower(0.0); // make sure it stops going up
        mainRobot.followTrajectory(firstLeft); // GO LEFT
        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();

        mainRobot.pause(200);
        mainRobot.followTrajectory(firstForward); // GO FORWARD
        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();

        mainRobot.pause(200);
        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();
        mainRobot.followTrajectory(firstRight); // GO RIGHT

        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();
        mainRobot.slides.setSlidesPower(1.0); // VERTICAL SLIDE UP
        mainRobot.pause(2600); // TIME TO GET TO TOP
        mainRobot.followTrajectory(creep); // CREEP FORWARDS
        step += 1;
        telemetry.addData("It works", "NO" + step); telemetry.update();
        mainRobot.grabber.openGrabber(); //DROP CONE
        mainRobot.pause(700);
        mainRobot.followTrajectory(backcreep); // go backwards so you can close
        mainRobot.slides.setSlidesPower(-0.4); // GO DOWN WHILE OPEN
        mainRobot.pause(1800);
        mainRobot.slides.setSlidesPower(-0.7);//MAKE SURE U ACTUALLY GO DOWN
        mainRobot.pause(500);
        /*mainRobot.slides.setSlidesPower(0.1); //  MAKE SURE SPOOL IS TAUGHT
        mainRobot.pause(1000);*/
        mainRobot.grabber.closeGrabber();// CLOSE
        mainRobot.pause(300);
        tempPose = backcreep.end();
    } //this is similar to BlueRight

}
