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
    public static double slidepower = 1.0;


    @Override
    public void runOpMode() {
        mainRobot = new MainRobot(hardwareMap, telemetry);
        Pose2d startPose = new Pose2d(-34, 70, Math.toRadians(0));
        mainRobot.setPoseEstimate(startPose);

        Trajectory firstRight = mainRobot.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(startPose.getX()-38, startPose.getY()-3.0, Math.toRadians(-87)))
                .build();

        Trajectory firstForward = mainRobot.trajectoryBuilder(firstRight.end())
                .forward(46.5)
                .build();

        Trajectory firstLeft = mainRobot.trajectoryBuilder(firstForward.end())
                .strafeLeft(14.5)
                .build();

        Trajectory creep = mainRobot.trajectoryBuilder(firstLeft.end())
                .forward(3)
                .build();

        Trajectory backcreep = mainRobot.trajectoryBuilder(creep.end())
                .back(4)
                .build();

        TrajectorySequence turn1 = mainRobot.trajectorySequenceBuilder(backcreep.end())
                .turn(Math.toRadians(90))
                .build();

        Trajectory cycleforward = mainRobot.trajectoryBuilder(turn1.end())
                .forward(30.5 )
                .build();

        Trajectory cyclecreep = mainRobot.trajectoryBuilder(cycleforward.end())
                .forward(3.5)
                .build();

        Trajectory backcycle = mainRobot.trajectoryBuilder(cyclecreep.end())
                .back(33)
                .build();

        TrajectorySequence turn2 = mainRobot.trajectorySequenceBuilder(backcycle.end())
                .turn(Math.toRadians(-90))
                .build();

        Trajectory cyclecreep2 = mainRobot.trajectoryBuilder(turn2.end())
                .forward(3)
                .build();
        Trajectory cyclebackcreep = mainRobot.trajectoryBuilder(creep.end())
                .back(3)
                .build();

        waitForStart();
        /*mainRobot.grabber.closeGrabber();
        mainRobot.grabber.closeGrabber();
        mainRobot.pause(600);
        mainRobot.slides.setSlidesPower(1.0);
        mainRobot.pause(300);
        mainRobot.slides.setSlidesPower(0.0);*/
        mainRobot.slides.setSlidesPower(0.75);
        mainRobot.followTrajectory(firstRight); // GO RIGHT
        mainRobot.slides.setSlidesPower(0.05);
        /*mainRobot.followTrajectory(firstForward); // GO FORWARD
        mainRobot.followTrajectory(firstLeft); // GO LEFT
        mainRobot.slides.setSlidesPower(1.0); // VERTICAL SLIDE UP
        mainRobot.pause(2000); // TIME TO GET TO TOP
        mainRobot.followTrajectory(creep); // CREEP FORWARDS
        mainRobot.grabber.openGrabber(); //DROP CONE
        mainRobot.pause(200);
        mainRobot.followTrajectory(backcreep); // go backwards so you can close
        mainRobot.slides.setSlidesPower(-0.3); // GO DOWN WHILE OPEN
        mainRobot.pause(1400);
        /*mainRobot.slides.setSlidesPower(-0.7);//MAKE SURE U ACTUALLY GO DOWN
        mainRobot.pause(500);
        mainRobot.slides.setSlidesPower(0.1); //  MAKE SURE SPOOL IS TAUGHT
        mainRobot.pause(1000);*/
   //     mainRobot.grabber.closeGrabber();// CLOSE
   //     mainRobot.pause(300);
        /*mainRobot.followTrajectorySequence(turn1); // turn ccw
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
        mainRobot.slides.setSlidesPower(-0.3); // drop
        mainRobot.followTrajectory(cyclebackcreep); // get ready to park*/





    }
}
