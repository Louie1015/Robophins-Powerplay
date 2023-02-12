package teamcode.Components;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Grabber {
    Servo grabber1;
    Servo grabber2;

    public static double openPos1 = 0.03;
    public static double openPos2 = 0.63;
    public static double closePos1 = 0.20;
    public static double closePos2 = 0.42;

    public Grabber (HardwareMap hardwareMap, Telemetry telemetry) {
        grabber1 = hardwareMap.get(Servo.class, "grabber");
        grabber2 = hardwareMap.get(Servo.class, "grabber2");

    }

    public void closeGrabber() {
        grabber1.setPosition(closePos1);
        grabber2.setPosition(closePos2);
    }

    public void openGrabber() {
        grabber1.setPosition(openPos1);
        grabber2.setPosition(openPos2);
    }
}
