package teamcode.drive.OpModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;

import teamcode.Components.MainRobot;
import teamcode.Components.Cammy;
import teamcode.trajectorysequence.TrajectorySequence;

import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous
public class CamWithPidRight extends LinearOpMode{
    private Cammy camlyn;



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
            mainRobot.lighting.blinkBlack();
            mainRobot.grabber.closeGrabber();
            mainRobot.pause(1000);
            mainRobot.slides.setSlidesPower(1.0);
            mainRobot.pause(100);
            mainRobot.slides.setSlidesPower(0.0);
            int sleeveStage = Cammy.truePath;
            telemetry.addData("Path: ", camlyn.getPosition());
            telemetry.update();
            // if you want code to run before we check which sleeve is detected(ex:close grabber on startup), run it here

            if(sleeveStage == 1){
                mainRobot.lighting.blinkYellow();
                Path1();
                break;
            }else if(sleeveStage == 2){
                mainRobot.lighting.blinkCyan();
                Path2();
                break;
            }else if(sleeveStage == 3){
                mainRobot.lighting.blinkMagenta();
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
                .strafeLeft(13)
                .build();
        mainRobot.slides.setSlidesPower(0.0);
        mainRobot.followTrajectory(yellowTraj1);
    }

    public void Path2(){
        drop1();
        //park into middle sector (Blue)
        Trajectory blueTraj1 = mainRobot.trajectoryBuilder(tempPose)
                .strafeRight(13)
                .build();
        mainRobot.slides.setSlidesPower(0.0);
        mainRobot.followTrajectory(blueTraj1);
    }

    public void Path3(){
        drop1();
        //park into right sector (Magenta)
        Trajectory magentaTraj1 = mainRobot.trajectoryBuilder(tempPose)
                .strafeRight(47)
                .build();
        mainRobot.slides.setSlidesPower(0.0);
        mainRobot.followTrajectory(magentaTraj1);
    }
    public void drop1() {
        Pose2d startPose = new Pose2d(-34, 70, Math.toRadians(-90));
        mainRobot.setPoseEstimate(startPose);
        Trajectory ff1 = mainRobot.trajectoryBuilder(startPose)
                .forward(2)
                .build();
        Trajectory firstLeft = mainRobot.trajectoryBuilder(ff1.end())
                .strafeLeft(21.5)
                .build();

        Trajectory firstForward = mainRobot.trajectoryBuilder(firstLeft.end())
                .forward(44.5)
                .build();

        Trajectory firstRight = mainRobot.trajectoryBuilder(firstForward.end())
                .strafeRight(10.5)
                .build();

        Trajectory creep = mainRobot.trajectoryBuilder(firstRight.end())
                .forward(3.25)
                .build();

        Trajectory backcreep = mainRobot.trajectoryBuilder(creep.end())
                .back(3.25)
                .build();

        TrajectorySequence turn1 = mainRobot.trajectorySequenceBuilder(backcreep.end())
                .turn(Math.toRadians(-90))
                .build();

        Trajectory cycleforward = mainRobot.trajectoryBuilder(turn1.end())
                .forward(27.25)
                .build();

        Trajectory cyclecreep = mainRobot.trajectoryBuilder(cycleforward.end())
                .forward(4)
                .build();

        Trajectory backcycle = mainRobot.trajectoryBuilder(cyclecreep.end())
                .back(32.7)
                .build();

        TrajectorySequence turn2 = mainRobot.trajectorySequenceBuilder(backcycle.end())
                .turn(Math.toRadians(90))
                .build();

        Trajectory cyclecreep2 = mainRobot.trajectoryBuilder(turn2.end())
                .forward(3)
                .build();
        Trajectory cyclebackcreep = mainRobot.trajectoryBuilder(creep.end())
                .back(3)
                .build();


        mainRobot.followTrajectory(ff1);
        mainRobot.followTrajectory(firstLeft); // GO RIGHT
        mainRobot.followTrajectory(firstForward); // GO FORWARD
        mainRobot.followTrajectory(firstRight); // GO LEFT
        mainRobot.slides.setSlidesPower(1.0); // VERTICAL SLIDE UP
        mainRobot.pause(2000); // TIME TO GET TO TOP
        mainRobot.followTrajectory(creep); // CREEP FORWARDS
        mainRobot.grabber.openGrabber(); //DROP CONE
        mainRobot.pause(200);
        mainRobot.followTrajectory(backcreep); // go backwards so you can close
        mainRobot.slides.setSlidesPower(-1.0); // GO DOWN WHILE OPEN
        mainRobot.pause(700);
        /*mainRobot.slides.setSlidesPower(-0.7);//MAKE SURE U ACTUALLY GO DOWN
        mainRobot.pause(500);
        mainRobot.slides.setSlidesPower(0.1); //  MAKE SURE SPOOL IS TAUGHT
        mainRobot.pause(1000);*/
        //     mainRobot.grabber.closeGrabber();// CLOSE
        //     mainRobot.pause(300);

        // CYCLE CODE
        mainRobot.followTrajectorySequence(turn1); // turn ccw
        mainRobot.slides.setSlidesPower(1.0); // slide up
        mainRobot.pause(220);
        mainRobot.slides.setSlidesPower(0.05); // hold up
        mainRobot.followTrajectory(cycleforward); // go to stack
        mainRobot.followTrajectory(cyclecreep);// go forward while open
        mainRobot.grabber.closeGrabber();// grab it
        mainRobot.pause(200);
        mainRobot.slides.setSlidesPower(1.0); // take it off
        mainRobot.pause(400);
        mainRobot.slides.setSlidesPower(0.05); //hold
        mainRobot.followTrajectory(backcycle); //go back to pole
        mainRobot.followTrajectorySequence(turn2); //turn
        mainRobot.slides.setSlidesPower(1.0);// lift
        mainRobot.pause(2000);
        mainRobot.slides.setSlidesPower(0.05);
        mainRobot.followTrajectory(cyclecreep2); //creep
        mainRobot.grabber.openGrabber(); // drop cone
        mainRobot.pause(200);
        mainRobot.slides.setSlidesPower(-1.0); // drop
        mainRobot.followTrajectory(cyclebackcreep); // get ready to park
        tempPose = cyclebackcreep.end();
    }

}
