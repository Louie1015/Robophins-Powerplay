package teamcode.drive.OpModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
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
public class CamWithPidLeft extends LinearOpMode{
    private Cammy camlyn;
    public static double firstrightdist = 20.75;
    public static double firstcycleleft = 15.5;


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
            mainRobot.pause(1200);
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
                .strafeLeft(37.5)
                .build();
        mainRobot.slides.setSlidesPower(0.0);
        mainRobot.followTrajectory(yellowTraj1);

    }

    public void Path2(){
        drop1();
        //park into middle sector (Blue)
        Trajectory blueTraj1 = mainRobot.trajectoryBuilder(tempPose)
                .strafeLeft(14)
                .build();
        mainRobot.slides.setSlidesPower(0.0);
        mainRobot.followTrajectory(blueTraj1);
    }

    public void Path3(){
        drop1();
        //park into right sector (Magenta)
        Trajectory magentaTraj1 = mainRobot.trajectoryBuilder(tempPose)
                .strafeRight(14)
                .build();
        mainRobot.slides.setSlidesPower(0.0);
        mainRobot.followTrajectory(magentaTraj1);
    }
    public void drop1() {
        Pose2d startPose = new Pose2d(-34, 70, Math.toRadians(-90));
        mainRobot.setPoseEstimate(startPose);
        Trajectory ff1 = mainRobot.trajectoryBuilder(startPose)
                //.forward(1)
                .lineToLinearHeading(new Pose2d(startPose.getX()-26.5, startPose.getY()-2.5, Math.toRadians(-90)))
                .build();
        startPose = ff1.end();

        /*Trajectory firstRight = mainRobot.trajectoryBuilder(ff1.end())
                .strafeRight(26.5)
                .build();*/

        Trajectory firstForward = mainRobot.trajectoryBuilder(startPose)
                .forward(41)
                .build();
        startPose = firstForward.end();

        Trajectory firstLeft = mainRobot.trajectoryBuilder(firstForward.end())
                //.strafeLeft(14.5)
                .lineToLinearHeading(new Pose2d(startPose.getX()+14.25, startPose.getY()-4.25, Math.toRadians(-90)))
                .build();
        startPose = firstLeft.end();
        /*Trajectory creep = mainRobot.trajectoryBuilder(firstLeft.end())
                .forward(3.5)
                .build();
        startPose = creep.end();*/
        Trajectory backcreep = mainRobot.trajectoryBuilder(startPose)
                //.back(3)
                .lineToLinearHeading(new Pose2d(startPose.getX()+12.25, startPose.getY()+4, Math.toRadians(-90)))
                .build();
        /*Trajectory backstrafe = mainRobot.trajectoryBuilder(backcreep.end())
                .strafeLeft(10)
                .build();*/

        TrajectorySequence turn1 = mainRobot.trajectorySequenceBuilder(backcreep.end())
                .turn(Math.toRadians(90))
                .build();

        Trajectory cycleforward = mainRobot.trajectoryBuilder(turn1.end())
                .forward(23.5)
                .build();
        startPose = cycleforward.end();
        /*Trajectory cyclecreep = mainRobot.trajectoryBuilder(cycleforward.end())
                .forward(2.75)
                .build();
        startPose = cyclecreep.end();*/
        Trajectory backcycle = mainRobot.trajectoryBuilder(startPose)
                //.back(32.5)
                .lineToLinearHeading(new Pose2d(startPose.getX()-39, startPose.getY()-3.5, Math.toRadians(-87)))
                .build();
        startPose=(new Pose2d(backcycle.end().getX(),backcycle.end().getY(), Math.toRadians(-90)));

        /*TrajectorySequence turn2 = mainRobot.trajectorySequenceBuilder(backcycle.end())
                .turn(Math.toRadians(-90))
                .build();*/

        /*Trajectory cyclecreep2 = mainRobot.trajectoryBuilder(startPose)
                .forward(2)
                .build();*/
        //3RD CONE COMMENT THIS OUT IF IT DOESNT WORK
        /*Trajectory backcreep2 = mainRobot.trajectoryBuilder(startPose)
                //.back(3)
                .lineToLinearHeading(new Pose2d(startPose.getX()+11.5, startPose.getY()+3.75, Math.toRadians(-90)))
                .build();
        startPose = backcreep2.end();
        TrajectorySequence turnThirdCone = mainRobot.trajectorySequenceBuilder(startPose)
                .turn(Math.toRadians(90))
                .build();
        Trajectory cycleforwardThirdCone = mainRobot.trajectoryBuilder(turnThirdCone.end())
                .forward(24)
                .build();
        startPose = cycleforwardThirdCone.end();
        Trajectory cyclecreepThirdCone = mainRobot.trajectoryBuilder(cycleforwardThirdCone.end())
                .forward(2.75)
                .build();
        startPose = cyclecreep.end();
        Trajectory backcycleThirdCone = mainRobot.trajectoryBuilder(startPose)
                //.back(32.5)
                .lineToLinearHeading(new Pose2d(startPose.getX()-38, startPose.getY()-3.0, Math.toRadians(-87)))
                .build();
        startPose=(new Pose2d(backcycleThirdCone.end().getX(),backcycleThirdCone.end().getY(), Math.toRadians(-90)));*/


        Trajectory cyclebackcreep = mainRobot.trajectoryBuilder(startPose)
                .back(2)
                .build();
        mainRobot.followTrajectory(ff1);
        //mainRobot.followTrajectory(firstRight); // GO RIGHT
        mainRobot.followTrajectory(firstForward); // GO FORWARD
        //mainRobot.followTrajectory(firstLeft); // GO LEFT
        mainRobot.slides.setSlidesPower(1.0); // VERTICAL SLIDE UP
        mainRobot.pause(600);
        mainRobot.followTrajectory(firstLeft); // GO LEFT AND CREEP
        mainRobot.pause(300); // TIME TO GET TO TOP
        mainRobot.slides.setSlidesPower(1.0/6.0);
        //mainRobot.followTrajectory(creep); // CREEP FORWARDS
        mainRobot.grabber.openGrabber(); //DROP CONE
        mainRobot.pause(200);
        mainRobot.slides.setSlidesPower(-1.0); // GO DOWN WHILE OPEN
        mainRobot.followTrajectory(backcreep); // go backwards so you can close
        mainRobot.pause(200);
        mainRobot.slides.setSlidesPower(0.0); // STOP

        //mainRobot.followTrajectory(backstrafe);
        //mainRobot.pause(400);
        /*mainRobot.slides.setSlidesPower(-0.7);//MAKE SURE U ACTUALLY GO DOWN
        mainRobot.pause(500);
        mainRobot.slides.setSlidesPower(0.1); //  MAKE SURE SPOOL IS TAUGHT
        mainRobot.pause(1000);*/
        //     mainRobot.grabber.closeGrabber();// CLOSE
        //     mainRobot.pause(300);
        mainRobot.followTrajectorySequence(turn1); // turn ccw
        mainRobot.slides.setSlidesPower(1.0); // slide up
        mainRobot.pause(200);
        mainRobot.slides.setSlidesPower(0.05); // hold up
        mainRobot.followTrajectory(cycleforward); // go to stack
        //mainRobot.followTrajectory(cyclecreep);// go forward while open
        mainRobot.grabber.closeGrabber();// grab it
        mainRobot.pause(400);
        mainRobot.slides.setSlidesPower(1.0); // take it off
        mainRobot.pause(400);
        mainRobot.slides.setSlidesPower(0.75); //up while drifting
        mainRobot.followTrajectory(backcycle); //go back to pole
        //mainRobot.followTrajectorySequence(turn2); //turn
        //mainRobot.slides.setSlidesPower(1.0);// lift
        //mainRobot.pause(1400);
        mainRobot.slides.setSlidesPower(0.05);
        //mainRobot.followTrajectory(cyclecreep2); //creep
        mainRobot.grabber.openGrabber(); // drop cone
        mainRobot.pause(200);
        /*3RD CONE CODE
        mainRobot.slides.setSlidesPower(-1.0); // GO DOWN WHILE OPEN
        mainRobot.followTrajectory(backcreep2);

        mainRobot.followTrajectorySequence(turnThirdCone);
        mainRobot.slides.setSlidesPower(1.0); // slide up
        mainRobot.pause(220);
        mainRobot.slides.setSlidesPower(0.05); // hold up
        mainRobot.followTrajectory(cycleforwardThirdCone); // go to stack
        //mainRobot.followTrajectory(cyclecreepThirdCone);// go forward while open
        mainRobot.grabber.closeGrabber();// grab it
        mainRobot.pause(400);
        mainRobot.slides.setSlidesPower(1.0); // take it off
        mainRobot.pause(400);
        mainRobot.slides.setSlidesPower(0.75); //up while drifting
        mainRobot.followTrajectory(backcycleThirdCone); //go back to pole
        mainRobot.slides.setSlidesPower(0.05);
        mainRobot.grabber.openGrabber(); // drop cone
        mainRobot.pause(200);





        //3RD CONE CODE END*/
        mainRobot.slides.setSlidesPower(-1.0); // drop
        mainRobot.followTrajectory(cyclebackcreep); // get ready to park
        tempPose = cyclebackcreep.end();
    }

}

