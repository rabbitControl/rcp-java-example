package io.github.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.*;

public class NumberParameterExpose {

    public static void exposeByte(final RCPServer rabbit) throws RCPParameterException {

        final Int8Parameter parameter = rabbit.createInt8Parameter("byte number");
        parameter.setValue((byte)-2);
    }

    public static void exposeInt16(final RCPServer rabbit) throws RCPParameterException {

        final Int16Parameter parameter = rabbit.createInt16Parameter("short number");
        parameter.setValue((short)-2);
    }

    public static void exposeInt32(final RCPServer rabbit) throws RCPParameterException {

        final Int32Parameter parameter = rabbit.createInt32Parameter("int number");
        parameter.setValue(-2);
    }

    public static void exposeLong(final RCPServer rabbit) throws RCPParameterException {

        final Int64Parameter parameter = rabbit.createInt64Parameter("long number");
        parameter.setValue(10L);
    }

    public static void exposeSingleFloat(final RCPServer rabbit) throws RCPParameterException, RCPException {

        final GroupParameter group = rabbit.createGroupParameter("float group");

        for (int i = 0; i <1; i++) {

            final Float32Parameter parameter = rabbit.createFloat32Parameter("FLOAT", group);
            parameter.setValue(123.F);
            parameter.getTypeDefinition().setMinimum(0.F);
            parameter.getTypeDefinition().setMaximum(200.F);
        }
    }

    public static void exposeSingleFloat64(final RCPServer rabbit) throws RCPParameterException,
                                                                    RCPException {

        final GroupParameter group = rabbit.createGroupParameter("float group");

        for (int i = 0; i <1; i++) {

            final Float64Parameter parameter = rabbit.createFloat64Parameter("FLOAT64", group);
            parameter.setValue(123.D);
            parameter.getTypeDefinition().setMinimum(0.D);
            parameter.getTypeDefinition().setMaximum(200.D);
        }
    }

    public static void exposeFloatTest(final RCPServer rabbit) throws RCPParameterException {

        Float32Parameter parameter = rabbit.createFloat32Parameter("simple-float");
        parameter.setValue(3.1415F);

        parameter = rabbit.createFloat32Parameter("float min/max");
        parameter.setMinimum(0.F);
        parameter.setMaximum(100.F);
        parameter.setValue(20.F);

        parameter = rabbit.createFloat32Parameter("float min/max/multipleof/unit");
        parameter.setMinimum(0.F);
        parameter.setMaximum(100.F);
        parameter.setValue(20.F);
        parameter.setMultipleof(5.F);
        parameter.setUnit("MM");
    }

    public static void exposeFaultyFloat(final RCPServer rabbit) throws RCPParameterException {

        final Float32Parameter parameter = rabbit.createFloat32Parameter("float 0-0");
        parameter.setMinimum(0.F);
        parameter.setMaximum(0.F);
    }

    public static void exposeOutOfBoundFloat(final RCPServer rabbit) throws RCPParameterException {

        final Float32Parameter parameter = rabbit.createFloat32Parameter("float 0-0");
        parameter.setMinimum(0.F);
        parameter.setMaximum(1.F);
        parameter.setValue(-2.F);
    }


    public static void exposeForCImpl(final RCPServer rabbit) throws RCPParameterException {

        final BooleanParameter bool_parameter = rabbit.createBooleanParameter("bool");

        final Int8Parameter int8_parameter = rabbit.createInt8Parameter("int 8");
        int8_parameter.setValue((byte)8);

        final Int32Parameter int_parameter = rabbit.createInt32Parameter("int 1");
        int_parameter.setMinimum(0);
        int_parameter.setMaximum(100);
        int_parameter.setValue(10);

    }
}
