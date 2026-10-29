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

    private double targetVelocity = 0.0;
    private final double FLYWHEEL_TARGET_RPM_TICKS = 1800.0;

    // PIDF Coefficients (tune these)
    private double kP = 0.005;
    private double kI = 0.000;
    private double kD = 0.0001;
    private double kF = 0.00055; // Approx 1.0 / maxTicksPerSec

    private double lastError = 0.0;
    private double integralSum = 0.0;
    private long lastTimeNanos = 0;
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





    public void intake(){

        while (gamepad2.right_bumper){
            frontMotor.setPower(0.5);

        }
        while (gamepad2.left_bumper){
            frontMotor.setPower(-0.5);
        }
        frontMotor.setPower(0);
    }

    public void wheelvolocity () {
        // 1. Handle Gamepad 2 Controls
        if (gamepad2.a) {
            targetVelocity = FLYWHEEL_TARGET_RPM_TICKS;
        } else if (gamepad2.x) {
            targetVelocity = 0.0;
        }

        // 2. Shut off power immediately if shooter is toggled off
        if (targetVelocity == 0.0) {
            outakeMotor.setPower(0.0);
            lastError = 0.0;
            integralSum = 0.0;
            return;
        }

        // 3. Compute elapsed time (dt)
        long currentTimeNanos = System.nanoTime();
        if (lastTimeNanos == 0) {
            lastTimeNanos = currentTimeNanos;
            return;
        }
        double dt = (currentTimeNanos - lastTimeNanos) / 1e9;
        lastTimeNanos = currentTimeNanos;

        if (dt <= 0.0) return;

        // 4. Calculate error & PIDF terms
        double currentVelocity = outakeMotor.getVelocity();
        double error = targetVelocity - currentVelocity;

        integralSum += error * dt;
        double derivative = (error - lastError) / dt;
        lastError = error;

        double pTerm = kP * error;
        double iTerm = kI * integralSum;
        double dTerm = kD * derivative;
        double fTerm = kF * targetVelocity;

        double power = pTerm + iTerm + dTerm + fTerm;

        // Clamp motor output between -1.0 and 1.0
        power = Math.max(-1.0, Math.min(1.0, power));
        outakeMotor.setPower(power);
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
            this.flowerIntake();
            this.intake();
            this.wheelvolocity();


        }
    }
}