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
@TeleOp(name="Mechanum DriveCode")
public class MechanumDriveCode extends LinearOpMode {

    private DcMotorEx flMotor, frMotor, blMotor, brMotor, outakeMotor,frontMotor;
    private CRServo rightServo,leftServo;

    private Servo outtakeServo;
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


    public void flowerIntake() {
        // If 'A' is held, run the servos. Otherwise, stop them.
        while (gamepad1.left_trigger>0){
            leftServo.setPower((-1));
            rightServo.setPower(-1);
        }
        while (gamepad1.right_trigger>0){
            leftServo.setPower(1);
            rightServo.setPower(1);
        }
        if (gamepad1.left_trigger==0 && gamepad1.right_trigger==0){
            leftServo.setPower(0);
            rightServo.setPower(0);
        }
    }



    public void outTake() {
        double outakePower;
        double servoAngle;
        servoAngle = 0.4;
        if (gamepad2.b){
            outakePower = 0.6;
            outakeMotor.setPower(outakePower);
        if (gamepad2.x){
            outakePower = 0;
            outakeMotor.setPower(outakePower);
        }
        }
        if (gamepad2.a){
            if (servoAngle==0.4) {
                servoAngle = 0.6;
                outtakeServo.setPosition(servoAngle);

            }
            else{
                servoAngle = 0.4;
                outtakeServo.setPosition(servoAngle);
            }
        }


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
    public void initialization() {
        // Initialization

        flMotor = hardwareMap.get(DcMotorEx.class, "flMotor");
        frMotor = hardwareMap.get(DcMotorEx.class, "frMotor");
        blMotor = hardwareMap.get(DcMotorEx.class, "blMotor");
        brMotor = hardwareMap.get(DcMotorEx.class, "brMotor");
        outakeMotor = hardwareMap.get(DcMotorEx.class, "outakeMotor");
        frontMotor = hardwareMap.get(DcMotorEx.class,"frontMotor");
        rightServo = hardwareMap.get(CRServo.class,"rightServo");
        leftServo = hardwareMap.get(CRServo.class,"leftServo");
        outtakeServo = hardwareMap.get(Servo.class, "outtakeServo");

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
            this.outTake();
            this.flowerIntake();
            this.intake();


        }
    }
}