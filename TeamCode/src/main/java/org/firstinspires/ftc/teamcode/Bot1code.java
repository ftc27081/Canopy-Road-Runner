package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp(name="Bot 1 code")
public class Bot1code extends LinearOpMode {

    private DcMotorEx flMotor, frMotor, blMotor, brMotor, outakeMotor,intakeMotor;
    private CRServo intakeFeeder;
    private Servo gateServo;

    public void drive() {
        //get joystick values
        double yPower = 0.7 * gamepad1.left_stick_y;   // forward and back
        double yaw = 0.7 * gamepad1.right_stick_x;  // turning
        double xPower = 0.7 * gamepad1.left_stick_x;   // drive

        double frPower = yPower + xPower + yaw;
        double flPower = yPower - xPower - yaw;
        double brPower = yPower - xPower + yaw;
        double blPower = yPower + xPower - yaw;

        double maxPower = Math.max(Math.max(Math.abs(flPower), Math.abs(frPower)), Math.max(Math.abs(blPower), Math.abs(brPower)));
        if (maxPower > 1.0) {
            flPower /= maxPower;
            blPower /= maxPower;
            frPower /= maxPower;
            brPower /= maxPower;
        }
        flMotor.setPower(flPower);
        frMotor.setPower(frPower);
        blMotor.setPower(blPower);
        brMotor.setPower(brPower);
    }
    public void intake(){
        if (gamepad2.right_bumper){
            intakeMotor.setPower(1);
        }
        if (gamepad2.left_bumper){
            intakeMotor.setPower(-1);
        }
        if (gamepad2.y) {
            intakeMotor.setPower(0);
        }
    }
    public void outtake(){
        if (gamepad2.a){
            outakeMotor.setPower(0.5);
        }
        if (gamepad2.b){
            outakeMotor.setPower(0.6);
        }
        if (gamepad2.x){
            outakeMotor.setPower(-0.3);
            sleep(500);
            outakeMotor.setPower(0);
        }
    }

    public void intakeFeeder(){
       if (gamepad2.right_trigger>0){
           intakeFeeder.setPower(0.6);
        }
       if (gamepad2.left_trigger>0){
           intakeFeeder.setPower(-0.6);
       }
       if (gamepad2.right_stick_button){
           intakeFeeder.setPower(0);
       }
    }

    public void gatecontrol(){
        if (gamepad2.dpad_up){
            gateServo.setPosition(1);
        }
        if (gamepad2.dpad_down){
            gateServo.setPosition(0);
        }
    }

    public void initialization() {
        // Initialization

        flMotor = hardwareMap.get(DcMotorEx.class, "flMotor");
        frMotor = hardwareMap.get(DcMotorEx.class, "frMotor");
        blMotor = hardwareMap.get(DcMotorEx.class, "blMotor");
        brMotor = hardwareMap.get(DcMotorEx.class, "brMotor");
        outakeMotor = hardwareMap.get(DcMotorEx.class, "outakeMotor");
        intakeMotor = hardwareMap.get(DcMotorEx.class,"frontMotor");
        intakeFeeder = hardwareMap.get(CRServo.class,"frontServo");
        gateServo = hardwareMap.get(Servo.class,"gateServo");

        frMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        brMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        flMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        blMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        brMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }




    public void runOpMode() {

        initialization();

        telemetry.addData("Status", "Waiting");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            this.drive();
            this.intake();
            this.outtake();
            this.gatecontrol();
            this.intakeFeeder();
        }
    }
}