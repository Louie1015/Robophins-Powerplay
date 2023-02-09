package teamcode.Components;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ThreadPool;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import java.util.concurrent.ExecutorService;
import teamcode.Components.Grabber;
import teamcode.Components.Slides;
import teamcode.Components.Lighting;

import teamcode.drive.SampleMecanumDrive;

@Config
public class MainRobot extends SampleMecanumDrive{

    public Slides slides;
    public Grabber grabber;
    public Lighting lighting;

    public MainRobot (HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);
        slides = new Slides(hardwareMap, telemetry);
        grabber = new Grabber(hardwareMap,telemetry);
        lighting = new Lighting(hardwareMap, telemetry);

    }

    public void pause(long time) {
        long initTime = System.currentTimeMillis();
        while(initTime + time > System.currentTimeMillis() && !Thread.currentThread().isInterrupted()) { }
    }

}
