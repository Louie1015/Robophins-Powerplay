package teamcode.Components;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@Config
public class Lighting {

    RevBlinkinLedDriver ledDriver;

    public Lighting(HardwareMap hardwareMap) {

        ledDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

    }

    public void blinkOrange() { ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.HOT_PINK); }
    public void blinkBlue() { ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE); }
    public void blinkYellow() { ledDriver.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW); }

}

