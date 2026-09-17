package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name="Basic Drive")
public class TwoWheelDrive extends LinearOpMode {

//check in testing
   private DcMotor frontRight;
   private DcMotor frontLeft;
   private DcMotor arm;

   // Arm preset positions (Adjust these values based on testing)
   private final int ARM_SPOT_1 = 0;
   private final int ARM_SPOT_2 =1500;
   private boolean manualMode = true;

   public void runOpMode(){
       initialization();
       telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
       telemetry.addData("Status", "Waiting");
       telemetry.update();
       waitForStart();

       while (opModeIsActive()) {
           this.drive();
           this.arm();
       }

   }

   public void arm(){
       double manualArmPower = -gamepad1.left_stick_y;

       // Manual override: if stick is moved, switch to manual mode
       if (Math.abs(manualArmPower) > 0.05) {
           manualMode = true;
           arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
           arm.setPower(manualArmPower);
       } else if (gamepad1.y) {
           manualMode = false;
           arm.setTargetPosition(ARM_SPOT_2);
           arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           arm.setPower(1.0); // Maximum speed to "i" or move to preset
       } else if (gamepad1.a) {
           manualMode = false;
           arm.setTargetPosition(ARM_SPOT_1);
           arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           arm.setPower(1.0); // Maximum speed to move back
       }

       // If in manual mode and no input, hold position
       if (manualMode && Math.abs(manualArmPower) <= 0.05) {
           arm.setPower(0);
       }
   }



    public void initialization() {
        // Initialization
        frontLeft = hardwareMap.get(DcMotorEx.class, "flMotor");
        frontRight = hardwareMap.get(DcMotorEx.class, "frMotor");
        arm = hardwareMap.get(DcMotorEx.class,"Arm");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Arm setup
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void drive(){
        double turn = gamepad1.right_stick_x;
        double drive = gamepad1.right_stick_y; // Flipped sign to fix Up/Down inversion

        double leftPower  = drive + turn;
        double rightPower = drive - turn;

        double maxPower = Math.max(Math.abs(leftPower), Math.abs(rightPower));

        if (maxPower > 1.0) {
            leftPower  /= maxPower;
            rightPower /= maxPower;
        }

        frontLeft.setPower(leftPower);
        frontRight.setPower(rightPower);

    }




}