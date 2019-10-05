package io.github.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.ImageParameter;
import org.rabbitcontrol.rcp.model.types.ImageDefinition.ImageType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageParameterExpose {

    public static void exposeImageParameter(final RCPServer rabbit) throws RCPParameterException {

        // try to get file from resources

        final ClassLoader classLoader = ImageParameterExpose.class.getClassLoader();
        final File        image_file  = new File(classLoader.getResource("ice.jpg").getFile());
        if (image_file.exists()) {

            final ImageParameter parameter = rabbit.createImageParameter("image");
            parameter.setReadonly(true);
            parameter.setImageType(ImageType.BMP);

            try {
                final BufferedImage image = ImageIO.read(image_file);
                parameter.setValue(image);
            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }
        }
        else {
            System.out.println("could not find image: ice.jpg");
        }
    }
}
