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
public class BlueLeft extends LinearOpMode{

    MainRobot mainRobot;
    public static double firstrightdist = 22;
    public static double firstcycleleft = 36;
    public static double slidepower = 1.0;

    @Override
    public void runOpMode() {
        mainRobot = new MainRobot(hardwareMap, telemetry);
        Pose2d startPose = new Pose2d(-70, -36, Math.toRadians(0));
        mainRobot.setPoseEstimate(startPose);

        Trajectory firstRight = mainRobot.trajectoryBuilder(startPose)
                .strafeRight(firstrightdist)
                .build();

        Trajectory firstForward = mainRobot.trajectoryBuilder(firstRight.end())
                .forward(55)
                .build();

        Trajectory firstLeft = mainRobot.trajectoryBuilder(firstForward.end())
                .strafeLeft(firstcycleleft)
                .build();

        Trajectory creep = mainRobot.trajectoryBuilder(firstLeft.end())
                .splineTo(new Vector2d(-10, -20), 0,
                        SampleMecanumDrive.getVelocityConstraint(3, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(3))
                .build();

        waitForStart();

        mainRobot.followTrajectory(firstRight);
        mainRobot.pause(200);
        mainRobot.followTrajectory(firstForward);
        mainRobot.pause(200);
        mainRobot.followTrajectory(firstLeft);
        mainRobot.slides.setSlidesPower(1.0);
        mainRobot.followTrajectory(creep);
        mainRobot.grabber.openGrabber();
        mainRobot.slides.setSlidesPower(0.0);
        mainRobot.grabber.closeGrabber();

    }
}
