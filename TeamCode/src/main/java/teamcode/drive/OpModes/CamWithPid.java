package teamcode.drive.OpModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;

import teamcode.Components.MainRobot;
import teamcode.Components.Cammy;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class CamWithPid extends LinearOpMode{
    private Cammy camlyn;
    public static double firstrightdist = 25;
    public static double firstcycleleft = 35;
    public static double slidepower = 1.0;


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

        while(opModeIsActive()) {
            sleep(2000);

            // if you want code to run before we check which sleeve is detected(ex:close grabber on startup), run it here


            if(Cammy.truePath == 1){
                Path1();
                telemetry.addData("P1: ", camlyn.getPosition());
                telemetry.update();
                break;
            }else if(Cammy.truePath == 2){
                Path2();
                telemetry.addData("P2: ", camlyn.getPosition());
                telemetry.update();
                break;
            }else if(Cammy.truePath == 3){
                Path3();
                telemetry.addData("P3: ", camlyn.getPosition());
                telemetry.update();
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
                .strafeLeft(20)
                .build();
        mainRobot.followTrajectory(yellowTraj1);
    }

    public void Path2(){
        drop1();
        //park into middle sector (Blue)
        //should already be in it, not much needs to be done but might need some small adjustments
    }

    public void Path3(){
        drop1();
        //park into right sector (Magenta)
        Trajectory magentaTraj1 = mainRobot.trajectoryBuilder(tempPose)
                .strafeRight(20)
                .build();
        mainRobot.followTrajectory(magentaTraj1);
    }
    public void drop1() {
        Pose2d startPose = new Pose2d(-34, 70, Math.toRadians(-90));
        mainRobot.setPoseEstimate(startPose);

        Trajectory firstRight = mainRobot.trajectoryBuilder(startPose)
                .strafeRight(firstrightdist)
                .build();

        Trajectory firstForward = mainRobot.trajectoryBuilder(firstRight.end())
                .forward(50)
                .build();

        Trajectory firstLeft = mainRobot.trajectoryBuilder(firstForward.end())
                .strafeLeft(firstcycleleft)
                .build();

        Trajectory creep = mainRobot.trajectoryBuilder(firstLeft.end())
                .forward(4)
                .build();

        Trajectory backcreep = mainRobot.trajectoryBuilder(creep.end())
                .back(5)
                .build();

        int step = 0;
        mainRobot.grabber.closeGrabber();
        mainRobot.pause(500);
        mainRobot.followTrajectory(firstRight); // GO RIGHT
        mainRobot.pause(200);
        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();
        mainRobot.followTrajectory(firstForward); // GO FORWARD
        mainRobot.pause(200);
        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();
        mainRobot.followTrajectory(firstLeft); // GO LEFT
        step += 1;
        telemetry.addData("step", ":#" + step); telemetry.update();
        mainRobot.slides.setSlidesPower(1.0); // VERTICAL SLIDE UP
        mainRobot.pause(2750); // TIME TO GET TO TOP
        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();
        mainRobot.followTrajectory(creep); // CREEP FORWARDS
        step += 1;
        telemetry.addData("step", ":#" + step); telemetry.update();
        mainRobot.grabber.openGrabber(); //DROP CONE
        mainRobot.pause(500);
        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();
        mainRobot.slides.setSlidesPower(-0.3); // GO DOWN WHILE OPEN
        mainRobot.pause(1800);
        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();
        mainRobot.slides.setSlidesPower(0.1); //  MAKE SURE SPOOL IS TAUGHT
        mainRobot.pause(1000);
        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();
        mainRobot.followTrajectory(backcreep); // go backwards so you can close
        mainRobot.grabber.closeGrabber();// CLOSE
        mainRobot.pause(500);
        step += 1;
        telemetry.addData("step",":#" + step); telemetry.update();
        tempPose = backcreep.end();
    } //this is similar to BlueRight

}
