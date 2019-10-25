package io.github.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.RGBAParameter;
import org.rabbitcontrol.rcp.model.parameter.RGBParameter;

import java.awt.*;

public class ColorParameterExpose {

    public static void exposeColorParameters(final RCPServer rabbit) throws RCPParameterException {

        // try to get file from resources
        final RGBParameter rgb_param = rabbit.createRGBParameter("RGB");
        rgb_param.setValue(Color.RED);

        final RGBAParameter rgba_param = rabbit.createRGBAParameter("RGBA");
        rgba_param.setValue(Color.GREEN);
    }
}
