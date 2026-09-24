package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Test Auton", group = "Autonomous")
public class TestAuton extends LinearOpMode {

    @Override
    public void runOpMode() {
        // 1. Define the robot's starting position and orientation (X, Y, Heading)
        Pose2d initialPose = new Pose2d(63, 63, Math.toRadians(270));

        // 2. Initialize MecanumDrive with the hardware map and starting pose
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        // 3. Build your path using the Action Builder
        Action mainTrajectory = drive.actionBuilder(initialPose)
                .lineToY(50) // Drives straight to Y = 30
                .turn(Math.toRadians(-90)) // Turns 90 degrees
                .lineToX(50)
              //  .splineTo(new Vector2d(40, 30), Math.toRadians(0)) // Smooth curve to coordinates
                .build();

        // 4. Initialization complete, wait for the user to press PLAY
        telemetry.addLine("Initialized and ready to run!");
        telemetry.update();
        waitForStart();

        if (isStopRequested()) return;

        // 5. Execute the trajectory blockingly
        Actions.runBlocking(mainTrajectory);
    }
}