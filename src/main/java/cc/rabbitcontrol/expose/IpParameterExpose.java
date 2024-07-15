package cc.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.IPv4Parameter;

public class IpParameterExpose {

    public static void exposeIpV4Parameter(final RCPServer rabbit) throws RCPParameterException {
        final IPv4Parameter parameter = rabbit.createIPv4Parameter("IP");
    }
}
