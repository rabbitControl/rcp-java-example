package io.github.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.model.types.Range;

public class NumberParameterExpose {

    public static void exposeLong(final RCPServer rabbit) throws RCPParameterException {

        final Int64Parameter parameter = rabbit.createInt64Parameter("a long number");
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

    public static void exposeRange(final RCPServer rabbit) throws RCPParameterException {

        final RangeParameter<Byte> parameter = rabbit.createRangeParameter("range",
                                                                                Byte.class);

        parameter.setValue(new Range<Byte>((byte)2, (byte)4));
        parameter.getTypeDefinition().setDefault(new Range<Byte>((byte)1, (byte)10));
        parameter.getRangeDefinition().getElementType().setMinimum((byte)0);
        parameter.getRangeDefinition().getElementType().setMaximum((byte)20);
    }

    public static void exposeFaultyFloat(final RCPServer rabbit) throws RCPParameterException {

        final Float32Parameter parameter = rabbit.createFloat32Parameter("float 0-0");
        parameter.setMinimum(0.F);
        parameter.setMaximum(0.F);
    }
}
