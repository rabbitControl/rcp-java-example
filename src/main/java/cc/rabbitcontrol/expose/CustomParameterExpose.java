package cc.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.CustomParameter;

import java.nio.ByteBuffer;
import java.util.UUID;

public class CustomParameterExpose {

    public static void exposeCustom(final RCPServer rabbit) throws RCPParameterException {

        final CustomParameter custom = rabbit.createCustomParameter("custom", 4);

        ByteBuffer bb = ByteBuffer.wrap(new byte[4]);
        bb.putInt(3);
        custom.setValue(bb.array());

        custom.getCustomDefinition().setUuid(UUID.fromString("6C0A938B-05A3-481D-B968-78380E9B450C"));

        byte[] conf = {5, 6, 7};
        custom.getCustomDefinition().setConfig(conf);



        final CustomParameter custom2 = rabbit.createCustomParameter("custom 2", 4);

        bb = ByteBuffer.wrap(new byte[4]);
        bb.putInt(10);
        custom2.setValue(bb.array());

        custom2.getCustomDefinition().setUuid(UUID.fromString("081CE188-DCFF-47DE-98A4-755533AA50DF"));

        byte[] conf2 = {5, 6, 7, 8, 9, 10};
        custom2.getCustomDefinition().setConfig(conf2);
    }
}
