package io.github.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.BooleanParameter;

public class BoolParameterExpose {

    public static void exposeBooleanReadonly(final RCPServer rabbit) throws RCPParameterException {

        final BooleanParameter parameter = rabbit.createBooleanParameter("io/github/rabbitcontrol");
        parameter.setReadonly(true);
    }
}
