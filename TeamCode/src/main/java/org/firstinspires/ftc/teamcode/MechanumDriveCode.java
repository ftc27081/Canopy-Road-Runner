package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp(name="Mechanum DriveCode")
public class MechanumDriveCode extends LinearOpMode {

    private DcMotorEx flMotor, frMotor, blMotor, brMotor, outakeMotor,frontMotor;
    private CRServo gate,rightServo,leftServo;


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


    public void intake() {
        // If 'A' is held, run the servos. Otherwise, stop them.
        if (gamepad1.a) {
            rightServo.setPower(-1);
            leftServo.setPower(-1);
        }


        if (gamepad1.b){
            leftServo.setPower(1);
            rightServo.setPower(1);
        }

        if (gamepad1.x){

            leftServo.setPower(0);
            rightServo.setPower(0);
        }
    }



    public void wheelVelocity() {
        if (gamepad2.a){
            outakeMotor.setPower(0.45);


        }
        if (gamepad2.b){

            outakeMotor.setPower(0.6);

        }
        if (gamepad2.x){

            outakeMotor.setPower(-0.2);
            sleep(1000);
            outakeMotor.setPower(0);
        }


    }
    public void gateControl(){

        if (gamepad2.right_bumper){
            gate.setPower(-0.5);

        }
        if (gamepad2.left_bumper){
            gate.setPower(0);
        }
    }

    public void frontWheels(){

        if (gamepad2.dpad_down){
            frontMotor.setPower(0.5);

        }
        if (gamepad2.dpad_up){
            frontMotor.setPower(0);
        }
        if (gamepad2.dpad_left){
            frontMotor.setPower(-0.5);
        }
    }
    public void initialization() {
        // Initialization

        flMotor = hardwareMap.get(DcMotorEx.class, "flMotor");
        frMotor = hardwareMap.get(DcMotorEx.class, "frMotor");
        blMotor = hardwareMap.get(DcMotorEx.class, "blMotor");
        brMotor = hardwareMap.get(DcMotorEx.class, "brMotor");
        outakeMotor = hardwareMap.get(DcMotorEx.class, "outakeMotor");
        frontMotor = hardwareMap.get(DcMotorEx.class,"frontMotor");
        gate = hardwareMap.get(CRServo.class,"Gate");
        rightServo = hardwareMap.get(CRServo.class,"rightServo");
        leftServo = hardwareMap.get(CRServo.class,"leftServo");

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
            this.wheelVelocity();
            this.frontWheels();
            this.intake();
            this.gateControl();



        }
    }
}