package teamcode.drive.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;

import teamcode.Components.MainRobot;
import teamcode.Components.Cammy;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class CamWithPid extends LinearOpMode{
    private Cammy camlyn;


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
            sleep(5000);
            //close(); IF WE HAVE START CONE
            //sleep(500);


            if(Cammy.truePath == 1){
                Path1();
                telemetry.addData("P1: ", camlyn.getPosition());
                telemetry.update();
                break;
            }else if(Cammy.truePath == 2){
                Path2();
                telemetry.addData("P2: ", camlyn.getPosition());
                telemetry.update();
                break;
            }else if(Cammy.truePath == 3){
                Path3();
                telemetry.addData("P3: ", camlyn.getPosition());
                telemetry.update();
                break;
            }


        }


        while (!isStarted()) {
            telemetry.addData("ROTATION: ", camlyn.getPosition());
            telemetry.update();
        }

    }

    public void Path1(){
        //TODO: make yellow path here
    }

    public void Path2(){
        //TODO: make blue path here
    }

    public void Path3(){
        //TODO: make magenta path here
    }

}
