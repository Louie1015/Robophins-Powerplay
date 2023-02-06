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

    private DcMotor slider1;
    private DcMotor slider2;

    public Slides (HardwareMap hardwareMap, Telemetry telemetry) {
        slider1 = hardwareMap.get (DcMotor.class, "Slider1");
        slider1.setDirection(DcMotorSimple.Direction.REVERSE);
        slider2 = hardwareMap.get (DcMotor.class, "Slider2");
        slider1.setDirection(DcMotorSimple.Direction.FORWARD);
        slider1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slider2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setSlidesPower (double power) {
        double finalPower = power;
        slider1.setPower(finalPower);
        slider2.setPower(finalPower);
    }

}
