package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class UseString extends OpMode {
   @Override
   public void init() {
        String Name = "Saathvik";

        telemetry.addData("Hello",Name);

    }
   @Override
   public void loop(){

   }
}
