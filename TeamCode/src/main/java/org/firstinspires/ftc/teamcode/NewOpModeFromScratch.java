package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "NewOpModeFromScratch", group = "Linear OpModes")
public class NewOpModeFromScratch extends LinearOpMode {
    private double turningSensitivity = .75;
    private int armSpeed = 4;
    private int armMotorTopClamp = 1400;
    private int armMotorBottomClamp = 0;
    private double gripStrength = .18;
    private int armTargetPosition = 0;
    private double idleTimer = 0;
    private int animationStatus = 0;
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor rightDriveMotor;
    public DcMotor leftDriveMotor;
    public DcMotor armMotor;
    public Servo rightGripperMotor;
    public Servo leftGripperMotor;
    public Servo eyeballMotor;
    public NormalizedColorSensor colorSensor;

    @Override
    public void runOpMode(){
        // Dc motors init
        rightDriveMotor = hardwareMap.get(DcMotor.class, "rightDriveMotor");
        leftDriveMotor = hardwareMap.get(DcMotor.class, "leftDriveMotor");
        armMotor = hardwareMap.get(DcMotor.class, "armDriveMotor");

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armMotor.setPower(1);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //Servo init
        rightGripperMotor = hardwareMap.get(Servo.class, "rightGripperMotor");
        leftGripperMotor = hardwareMap.get(Servo.class, "leftGripperMotor");
        eyeballMotor = hardwareMap.get(Servo.class, "eyeballMotor");

        rightGripperMotor.setPosition(0);
        leftGripperMotor.setPosition(1);
        eyeballMotor.setPosition(.5);
        //color sensor
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");

        waitForStart();
        runtime.reset();

        while(opModeIsActive()){
            runDriveTrain();
            handleArmMovement();
            handleGripperGrippage();
            sensorStuff();
            idleAnimation();
            telemetry.update();
        }
    }
    private void runDriveTrain(){
        double stickX = gamepad1.right_stick_x;
        double stickY = -gamepad1.left_stick_y;

        double rightMotorSpeed = stickY;
        double leftMotorSpeed = stickY;

        rightMotorSpeed -= stickX * turningSensitivity;
        leftMotorSpeed += stickX * turningSensitivity;

        if (gamepad1.right_trigger > .2){
            rightDriveMotor.setPower(-rightMotorSpeed * .25);
            leftDriveMotor.setPower(leftMotorSpeed * .25);
        }else{
            rightDriveMotor.setPower(-rightMotorSpeed);
            leftDriveMotor.setPower(leftMotorSpeed);
        }
    }

    private void handleArmMovement(){
        if(gamepad1.dpad_up && armTargetPosition + armSpeed < armMotorTopClamp){
            armTargetPosition += armSpeed;
        } else if(gamepad1.dpad_up){
            armTargetPosition = armMotorTopClamp;
        }

        if(gamepad1.dpad_down && armTargetPosition - armSpeed > armMotorBottomClamp){
            armTargetPosition -= armSpeed;
        } else if(gamepad1.dpad_down){
            armTargetPosition = armMotorBottomClamp;
        }
        telemetry.addData("arm target position", armTargetPosition);
        telemetry.addData("true arm position", armMotor.getCurrentPosition());
        armMotor.setTargetPosition(armTargetPosition);
    }

    private void handleGripperGrippage(){
        if(gamepad1.left_bumper){
            rightGripperMotor.setPosition(gripStrength);
            leftGripperMotor.setPosition(1 - gripStrength);
        } else{
            rightGripperMotor.setPosition(0);
            leftGripperMotor.setPosition(1);
        }
        telemetry.addData("right trigger", gamepad1.right_trigger);
    }

    private void sensorStuff(){
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        float[] colorsHSV = new float[3];
        Color.colorToHSV(colors.toColor(), colorsHSV);
        telemetry.addData("color", colorsHSV);
    }

    private void idleAnimation(){
        if (animationStatus == 0 && idleTimer - runtime.seconds() < -15){
            eyeballMotor.setPosition(.6);
            animationStatus = 1;
            idleTimer = runtime.seconds();
        }else if(animationStatus == 1 && idleTimer - runtime.seconds() < -3){
            eyeballMotor.setPosition(.4);
            animationStatus = 2;
            idleTimer = runtime.seconds();
        }else if(animationStatus == 2 && idleTimer - runtime.seconds() < -3) {
            eyeballMotor.setPosition(.5);
            animationStatus = 0;
            idleTimer = runtime.seconds();
        }
    }
}
