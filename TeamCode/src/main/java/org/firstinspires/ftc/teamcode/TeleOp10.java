package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.actuators.DriveTrain;

@TeleOp(name = "TeleOp10 TeleOp", group = "TeleOp10")

public class TeleOp10 extends LinearOpMode {

    @Override
    public void runOpMode() {


        Robot11319.init(this);

        Robot11319.setChassis(DriveTrain.WheelMode.HYBRID_OMNI_TANK);

        Robot11319.runTeleOp();

    }

}
