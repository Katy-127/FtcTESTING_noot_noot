import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name= "Katie's Chassis 3", group= "Linear Op  Mode")

public class KatieChassis3 extends LinearOpMode {

    //declare class members
    private DcMotor rightDriveMotor;
    private DcMotor leftDriveMotor;

    private DcMotor armMotor;


    @Override
    public void runOpMode() {
        initialize();
        waitForStart();

        while(opModeIsActive()){
            driveRobot();
            armMovement();
            telemetry.update();
        }
    }
    public void initialize(){
        telemetry.addData("status", "Initialized");
        telemetry.update();

        //initialize the hardware
        rightDriveMotor = hardwareMap.get(DcMotor.class, "rightDriveMotor");
        leftDriveMotor = hardwareMap.get(DcMotor.class, "leftDriveMotor");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        //testMotor = hardwareMap.get(DcMotor.class, "testMotor");

        //reverse one motor so that they turn the same way
        rightDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        leftDriveMotor.setDirection(DcMotor.Direction.FORWARD);

        armMotor.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(.25);
        telemetry.addData("data", "Encoder is reset");
    }
    public void driveRobot(){
        double leftPower;
        double rightPower;
        double turningModifier = 0.6;
        //y dir motion
        rightPower = -gamepad1.right_stick_y;
        leftPower = -gamepad1.right_stick_y;

        //add in calculations to turn when joystick moves in X dir
        rightPower = rightPower - gamepad1.right_stick_x;
        leftPower = leftPower + gamepad1.right_stick_x;

        //send calculated power to wheels
        rightDriveMotor.setPower(rightPower);
        leftDriveMotor.setPower(leftPower);

        telemetry.addData("Right Wheel Power", + rightPower);
        telemetry.addData("Left Wheel Power", leftPower);
        telemetry.addData("Turning modifier", turningModifier);
    }

    public void armMovement(){
        double armPower;
        double armModifier = 0.1;

        armPower = -gamepad1.left_stick_y;

        //slow down arm with a multiplier
        armPower = armPower * armModifier;

        armMotor.setPower(armPower);

        //show wheel power and turningModifier
        telemetry.addData("left stick y", gamepad1.left_stick_y);
        telemetry.addData("Arm modifier", armModifier);
        //telemetry.addData("Test motor power", testPower);
    }
}
