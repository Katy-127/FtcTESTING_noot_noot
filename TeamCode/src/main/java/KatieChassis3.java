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
    private int targetPosition = 0;
    private final int targetSpeed = 1;
    private final int topClamp = 1400;
    private final int bottomClamp = 50;


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
        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(0.25);
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
        //// check to see that add'l movement will not violate the clamp; if the
        // new movement doesn't violate clamp, then do the new movement
        if (gamepad1.dpad_up && targetPosition + targetSpeed < topClamp){
            targetPosition += targetSpeed;
            // define movement so that if movement would take you outside
            // the clamp, movement stops at clamp
        } else if(gamepad1.dpad_up){
            targetPosition = topClamp;
        }

        if (gamepad1.dpad_down && targetPosition - targetSpeed > bottomClamp){
            targetPosition -= targetSpeed;
            // define movement so that if movement would take you outside
            // the clamp, movement stops at clamp
        } else if(gamepad1.dpad_down){
            targetPosition = bottomClamp;
        }
        armMotor.setTargetPosition(targetPosition);
    }
}
