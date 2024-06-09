
@TeleOp(name= "Katie's Chassis 2", group= "Linear Op  Mode")

public class KatieChassis2 extends LinearOpMode {

    //declare class members
    private DcMotor rightDriveMotor= null;
    private DcMotor leftDriveMotor = null;


    @Override
    public void runOpMode() {
        telemetry.addData("status", "Initialized");
        telemetry.update();

        //initialize the hardware
        rightDriveMotor = hardwareMap.get(DcMotor.class, "rightDrive");
        leftDriveMotor = hardwareMap.get(DcMotor.class, "leftDrive");

        //reverse one motor so that they turn the same way
        rightDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        leftDriveMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive()){

        }


    }



}
