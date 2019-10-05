package io.github.rabbitcontrol;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.Int32Parameter;
import org.rabbitcontrol.rcp.transport.websocket.server.WebsocketServerTransporterNetty;

public class RCPMiniServerExample {

    public static void main(final String[] args) throws RCPException, RCPParameterException {

        final RCPServer rabbitServer = new RCPServer();

        final WebsocketServerTransporterNetty transporter = new WebsocketServerTransporterNetty();

        rabbitServer.addTransporter(transporter);

        transporter.bind(10000);

        // expose parameter
        final Int32Parameter floatParameter = rabbitServer.createInt32Parameter("Int 32");

        // update server (push to clients)
        rabbitServer.update();
    }
}
