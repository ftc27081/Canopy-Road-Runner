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

    private DcMotorEx flMotor, frMotor, blMotor, brMotor, outakeMotor,frontMotor;
    private CRServo frontServo;
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

        while (gamepad2.right_bumper){
            frontMotor.setPower(0.5);

        }

        while (gamepad2.left_bumper){
            frontMotor.setPower(-0.5);
        }


        frontMotor.setPower(0);
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

    public void frontServo(){

       if (gamepad1.a){
           frontServo.setPower(0.6);

        }

       if (gamepad1.b){
           frontServo.setPower(-0.6);

       }
       if (gamepad1.x){

           frontServo.setPower(0);
       }

    }


    public void gatecontrol(){

        if (gamepad2.dpad_up){
            gateServo.setPosition(90);

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
        frontMotor = hardwareMap.get(DcMotorEx.class,"frontMotor");
        frontServo = hardwareMap.get(CRServo.class,"frontServo");
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
            this.frontServo();



        }
    }
}