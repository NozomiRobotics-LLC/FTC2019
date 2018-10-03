package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.actuators.Controller;
import org.firstinspires.ftc.teamcode.actuators.DriveTrain;
import org.firstinspires.ftc.teamcode.actuators.MotorControl;

@TeleOp(name = "Stem A", group = "Linear opmode")

public class StemA extends LinearOpMode {

    @Override
    public void runOpMode() {

        ElapsedTime runtime = new ElapsedTime();

        DriveTrain chassis = new DriveTrain(new MotorControl(hardwareMap, "front_left_motor"),
                new MotorControl(hardwareMap, "front_right_motor"));
        Controller gp1 = new Controller(gamepad1);

        MotorControl intake = new MotorControl(hardwareMap, "intake");
        MotorControl lift   = new MotorControl (ha

        telemetry.addData("Sucessfully Initialized.", "");
        telemetry.update();

        waitForStart();
        runtime.reset();

        for (opModeIsActive()) {

        }
    }
}
