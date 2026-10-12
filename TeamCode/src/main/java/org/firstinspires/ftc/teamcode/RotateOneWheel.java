package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
@Config
@TeleOp(name="Rotate One Wheel")
public class RotateOneWheel extends LinearOpMode {

    DcMotorEx flMotor;
    double flPower = 0.0;
    public void initialization() {
        flMotor = hardwareMap.get(DcMotorEx.class, "flMotor");
    }

    public void runOpMode() {
        initialization();

        waitForStart();

        while ( opModeIsActive()){
            if (flPower != 0.5){
                flPower = 0.5;
                flMotor.setPower(flPower);
            }
        }

    }


}
