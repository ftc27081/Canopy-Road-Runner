package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

    @Config
    @TeleOp(name="BioBuzz DriveCode")
    public class OverdriveBioBuzz extends LinearOpMode {

        private DcMotorEx flMotor, frMotor, blMotor, brMotor, outakeMotor, intakeMotor;
        private Servo gateControl;

        public void drive() {
            //get joystick values
            double yPower = 0.7 * gamepad1.left_stick_y;   // forward and back
            double yaw = 0.7 * gamepad1.right_stick_x;  // turning
            double xPower = 0.7 * gamepad1.left_stick_x;   // drive

            //calculate powersf
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

        public void gateControl() {
            if (gamepad2.right_bumper) {
                gateControl.setPosition(0.5);
            }
            if(gamepad2.left_bumper) {
                gateControl.setPosition(1.0);
            }
        }

        public void intake() {
            if (gamepad2.left_bumper) {
                intakeMotor.setPower(0.5);
            }
            if (gamepad2.right_bumper){
                intakeMotor.setPower(0);
            }
        }

        public void outtake() {
            if (gamepad2.a) {
               outakeMotor.setPower(0.5);
            }
            if (gamepad2.y){
                outakeMotor.setPower(0.7);
            }
            if (gamepad2.x){
                outakeMotor.setPower(0);
            }
        }

        public void initialization() {
            // Initialization
            flMotor = hardwareMap.get(DcMotorEx.class, "flMotor");
            frMotor = hardwareMap.get(DcMotorEx.class, "frMotor");
            blMotor = hardwareMap.get(DcMotorEx.class, "blMotor");
            brMotor = hardwareMap.get(DcMotorEx.class, "brMotor");
            outakeMotor = hardwareMap.get(DcMotorEx.class, "outtakeMotor");
            intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");


            gateControl = hardwareMap.get(Servo.class,"artifactGate");
            gateControl.setDirection(Servo.Direction.FORWARD);
            gateControl.setPosition(1.0);

            frMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            brMotor.setDirection(DcMotorSimple.Direction.REVERSE);

            outakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            flMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            frMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            blMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
            brMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

            telemetry = new MultipleTelemetry(telemetry,FtcDashboard.getInstance().getTelemetry());
        }

        public void runOpMode() {
            // init
            initialization();

            // create multiple telemetries and add to dashboard
            telemetry.addData("Status", "Waiting");
            telemetry.update();
            waitForStart();

            while (opModeIsActive()) {
                this.drive();
                this.intake();
                this.gateControl();
                this.outtake();
                telemetry.update();
            }
        }
    }
