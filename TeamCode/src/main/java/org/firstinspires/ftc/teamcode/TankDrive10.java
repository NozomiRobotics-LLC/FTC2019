package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TankDrive10 TeleOp", group = "TankDrive10")

public class TankDrive10 extends LinearOpMode {

    @Override
    public void runOpMode() {


        Robot11319.init(this,hardwareMap,telemetry,gamepad1);

        Robot11319.runTeleOp();

    }

}
