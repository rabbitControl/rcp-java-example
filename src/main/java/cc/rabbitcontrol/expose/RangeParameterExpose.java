package cc.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.types.Range;
import org.rabbitcontrol.rcp.model.parameter.RangeParameter;

public class RangeParameterExpose {

    public static void exposeRange(final RCPServer rabbit) throws RCPParameterException {

        final RangeParameter<Byte> parameter = rabbit.createRangeParameter("range",
                                                                           Byte.class);

        parameter.setValue(new Range<Byte>((byte)2, (byte)4));
        parameter.getTypeDefinition().setDefault(new Range<Byte>((byte)1, (byte)10));
        parameter.getRangeDefinition().getElementType().setMinimum((byte)0);
        parameter.getRangeDefinition().getElementType().setMaximum((byte)20);
    }

    public static void exposeRangeOutOfBounds(final RCPServer rabbit) throws RCPParameterException {

        final RangeParameter<Byte> parameter = rabbit.createRangeParameter("range",
                                                                           Byte.class);

        parameter.setValue(new Range<Byte>((byte)2, (byte)40));
        parameter.getTypeDefinition().setDefault(new Range<Byte>((byte)1, (byte)10));
        parameter.getRangeDefinition().getElementType().setMinimum((byte)10);
        parameter.getRangeDefinition().getElementType().setMaximum((byte)20);
    }
}
