package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.actuators.DriveTrain;
import org.firstinspires.ftc.teamcode.actuators.Motor;
import org.firstinspires.ftc.teamcode.actuators.XboxController;

public class Robot11319 {

    static LinearOpMode opMode;
    static HardwareMap hwMap;
    static Telemetry telemetry;
    static DriveTrain driveTrain;
    static XboxController gp1,gp2;
    static boolean isSecondGamepadUsed = false;

    public static void
    init(LinearOpMode linearOpMode, HardwareMap hardwareMap, Telemetry dsTelemetry, Gamepad gamepad1) {

        opMode = linearOpMode;
        hwMap  = hardwareMap;
        telemetry = dsTelemetry;

        driveTrain = new DriveTrain(new Motor(hardwareMap,"front_left"),
                                    new Motor(hardwareMap,"front_right"),
                                    new Motor(hardwareMap, "rear_left"),
                                    new Motor(hardwareMap,"rear_right"));
        gp1 = new XboxController(gamepad1);

        driveTrain.setWheelMode(DriveTrain.WheelMode.MECANUM_WHEEL);
    }

    public static void setChassis(int mode) {
        driveTrain.setWheelMode(mode);
    }

    public static void
    useSecondController(Gamepad gamepad2) {
        isSecondGamepadUsed = true;
        gp2 = new XboxController(gamepad2);
    }

    public static boolean
    runTeleOp() {

        if(!isSecondGamepadUsed) {
            gp2 = gp1;
        }

        opMode.waitForStart();

        while(opMode.opModeIsActive()) {
            teleopPeriodic();
        }
        telemetry.addData("Status", "Match/Practice Ended");
        telemetry.update();
        return true;
    }

    public static void teleopPeriodic() {
        if(gp1.isKeysChanged(XboxController.jLeftY,XboxController.jLeftX,XboxController.RT,XboxController.LT)) {
            double xSpeed = gp1.getValue(XboxController.jLeftX);
            double ySpeed = - gp1.getValue(XboxController.jLeftY);
            double rotation = - (gp1.getValue(XboxController.RT) - gp1.getValue(XboxController.LT));
            driveTrain.drive(xSpeed,ySpeed,rotation);
        }
    }

}
