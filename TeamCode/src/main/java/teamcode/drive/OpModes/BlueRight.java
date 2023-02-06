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

@Config
@Autonomous
public class BlueRight extends LinearOpMode{

    MainRobot mainRobot;
    public static double firstrightdist = 25;
    public static double firstcycleleft = 35;
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
                .forward(60)
                .build();

        Trajectory firstLeft = mainRobot.trajectoryBuilder(firstForward.end())
                .strafeLeft(firstcycleleft)
                .build();

        Trajectory creep = mainRobot.trajectoryBuilder(firstLeft.end())
                .forward(2)
   //                     SampleMecanumDrive.getVelocityConstraint(3, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
 //                       SampleMecanumDrive.getAccelerationConstraint(1))
                .build();

        waitForStart();
        int step = 0;
        mainRobot.followTrajectory(firstRight);
        mainRobot.pause(200);
        mainRobot.followTrajectory(firstForward);
        mainRobot.pause(200);
        step += 1;
        telemetry.addData("It works","YES" + step); telemetry.update();
        mainRobot.followTrajectory(firstLeft);
//        mainRobot.slides.setSlidesPower(1.0);
        step += 1;
        telemetry.addData("It works", "MAYBE" + step); telemetry.update();

        mainRobot.slides.setSlidesPower(1.0);
        mainRobot.pause(2750);
        mainRobot.followTrajectory(creep);
        step += 1;
        telemetry.addData("It works", "NO" + step); telemetry.update();
        mainRobot.grabber.openGrabber();
        mainRobot.pause(1000);
        mainRobot.slides.setSlidesPower(-0.3);
        mainRobot.pause(1800);
        mainRobot.slides.setSlidesPower(0.1);
        mainRobot.pause(1000);
        mainRobot.grabber.closeGrabber();
        mainRobot.pause(300);


    }
}
