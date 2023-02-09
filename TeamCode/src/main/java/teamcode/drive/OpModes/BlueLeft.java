package teamcode.drive.OpModes;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import teamcode.Components.MainRobot;
import teamcode.drive.DriveConstants;
import teamcode.drive.SampleMecanumDrive;
import teamcode.trajectorysequence.TrajectorySequence;

@Config
@Autonomous
public class BlueLeft extends LinearOpMode{

    MainRobot mainRobot;
    public static double firstrightdist = 20.75;
    public static double firstcycleleft = 15.5;
    public static double slidepower = 1.0;


    @Override
    public void runOpMode() {
        mainRobot = new MainRobot(hardwareMap, telemetry);
        Pose2d startPose = new Pose2d(-34, 70, Math.toRadians(-90));
        mainRobot.setPoseEstimate(startPose);

        Trajectory firstRight = mainRobot.trajectoryBuilder(startPose)
                .strafeRight(firstrightdist)
                .build();

        Trajectory firstForward = mainRobot.trajectoryBuilder(firstRight.end())
                .forward(46.5)
                .build();

        Trajectory firstLeft = mainRobot.trajectoryBuilder(firstForward.end())
                .strafeLeft(firstcycleleft)
                .build();

        Trajectory creep = mainRobot.trajectoryBuilder(firstLeft.end())
                .forward(4)
                .build();

        Trajectory backcreep = mainRobot.trajectoryBuilder(creep.end())
                .back(4)
                .build();
        TrajectorySequence strafeLeft1 = mainRobot.trajectorySequenceBuilder(backcreep.end())
                .strafeLeft(15)
                .build();
        TrajectorySequence turn1 = mainRobot.trajectorySequenceBuilder(strafeLeft1.end())
                .turn(Math.toRadians(90))
                .build();

        Trajectory cycleforward = mainRobot.trajectoryBuilder(turn1.end())
                .forward(18)
                .build();

        Trajectory cyclecreep = mainRobot.trajectoryBuilder(cycleforward.end())
                .forward(2)
                .build();

        Trajectory backcycle = mainRobot.trajectoryBuilder(cyclecreep.end())
                .back(33.5)
                .build();

        TrajectorySequence turn2 = mainRobot.trajectorySequenceBuilder(backcycle.end())
                .turn(Math.toRadians(-90))
                .build();

        Trajectory cyclecreep2 = mainRobot.trajectoryBuilder(turn2.end())
                .forward(4)
                .build();

        waitForStart();
        mainRobot.grabber.closeGrabber();
        mainRobot.pause(800);
        mainRobot.slides.setSlidesPower(1.0);
        mainRobot.pause(300);
        mainRobot.slides.setSlidesPower(0.0);
        mainRobot.followTrajectory(firstRight); // GO RIGHT
        mainRobot.pause(200);
        mainRobot.followTrajectory(firstForward); // GO FORWARD
        mainRobot.pause(200);

        mainRobot.followTrajectory(firstLeft); // GO LEFT

        mainRobot.slides.setSlidesPower(1.0); // VERTICAL SLIDE UP
        mainRobot.pause(2600); // TIME TO GET TO TOP
        mainRobot.followTrajectory(creep); // CREEP FORWARDS

        mainRobot.pause(2000);
        mainRobot.grabber.openGrabber(); //DROP CONE
        mainRobot.pause(1000);
        mainRobot.followTrajectory(backcreep); // go backwards so you can close
        mainRobot.slides.setSlidesPower(-0.3); // GO DOWN WHILE OPEN
        mainRobot.pause(1800);
        /*mainRobot.slides.setSlidesPower(-0.7);//MAKE SURE U ACTUALLY GO DOWN
        mainRobot.pause(500);
        mainRobot.slides.setSlidesPower(0.1); //  MAKE SURE SPOOL IS TAUGHT
        mainRobot.pause(1000);*/
   //     mainRobot.grabber.closeGrabber();// CLOSE
   //     mainRobot.pause(300);
        mainRobot.followTrajectorySequence(strafeLeft1); // left 1 tile
        mainRobot.pause(200);
        mainRobot.followTrajectorySequence(turn1); // turn ccw
        mainRobot.pause(200);
        mainRobot.followTrajectory(cycleforward); // go to stack
        mainRobot.pause(200);
        mainRobot.slides.setSlidesPower(1.0); // slide up
        mainRobot.pause(300);
        mainRobot.slides.setSlidesPower(0.05); // hold up
        mainRobot.followTrajectory(cyclecreep);// go forward while open
        mainRobot.grabber.closeGrabber();// grab it
        mainRobot.pause(500);
        mainRobot.slides.setSlidesPower(1.0); // take it off
        mainRobot.pause(300);
        mainRobot.slides.setSlidesPower(0.05); //hold
        mainRobot.followTrajectory(backcycle); //go back to pole
        mainRobot.pause(200);
        mainRobot.followTrajectorySequence(turn2); //turn
        mainRobot.pause(200);
        mainRobot.slides.setSlidesPower(1.0);// lift
        mainRobot.pause(2600);
        mainRobot.followTrajectory(cyclecreep2); //creep
        mainRobot.pause(2000);
        mainRobot.grabber.openGrabber(); // drop cone
        mainRobot.pause(600);
        mainRobot.slides.setSlidesPower(-0.3); // drop
        mainRobot.pause(1800);





    }
}
