package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.actuators.DriveTrain;

@TeleOp(name = "TeleOp 9",group = "Linear opmode")

public class TeleOp9 extends LinearOpMode {

    @Override
    public void runOpMode() {
        Robot11540.init(this);
        Robot11540.setChassis(DriveTrain.WheelMode.NORMAL);
        Robot11540.runTeleOp();
    }
}
