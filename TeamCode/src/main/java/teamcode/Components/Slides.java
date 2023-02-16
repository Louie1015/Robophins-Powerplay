package teamcode.Components;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Slides {

    public DcMotor slider1, slider2, Hslide;

    public Slides (HardwareMap hardwareMap, Telemetry telemetry) {
        slider1 = hardwareMap.get (DcMotor.class, "slider1");
        slider1.setDirection(DcMotorSimple.Direction.REVERSE);
        slider2 = hardwareMap.get (DcMotor.class, "slider2");
        slider2.setDirection(DcMotorSimple.Direction.FORWARD);
        slider1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slider2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //HS
        Hslide = hardwareMap.get(DcMotor.class, "hs");
    }

    public void setSlidesPower (double power) {
        slider1.setPower(power);
        slider2.setPower(power);
    }

    public void goToTopJunction(MainRobot MR) {
        slider1.setPower(1.0);
        slider2.setPower(1.0);
        MR.pause(1600);
        slider1.setPower(0.05);
        slider2.setPower(0.05);
    }

}
