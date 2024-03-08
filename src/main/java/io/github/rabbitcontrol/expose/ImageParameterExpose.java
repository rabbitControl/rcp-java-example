package io.github.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.model.types.ImageDefinition.ImageType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

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


    private static final ArrayList<ImageParameter> images = new ArrayList<>();
    private static int reloadCount = 0;


    private static void clearImages(final RCPServer rabbit) throws RCPException {
        System.out.println("remove images: " + images.size());
        for (ImageParameter p : images) {
            rabbit.remove(p);
            rabbit.update();
        }
        images.clear();

    }
    private static void loadMany(final RCPServer rabbit, BufferedImage image, String prefix) throws
                                                                                             RCPParameterException,
                                                                                             RCPException {
        clearImages(rabbit);

        int amount = 50;
        for (int i = 0; i < amount; i++) {
            final ImageParameter parameter = rabbit.createImageParameter(prefix + "_image " + i);
            parameter.setReadonly(true);
            parameter.setImageType(ImageType.JPEG);
            parameter.setValue(image);
            parameter.setOrder(amount-i);

            images.add(parameter);

            rabbit.update();
        }
    }

    public static void exposeManyImageParameter(final RCPServer rabbit) throws RCPParameterException {

        // try to get file from resources

        final ClassLoader classLoader = ImageParameterExpose.class.getClassLoader();
        final File        image_file  = new File(classLoader.getResource("ice.jpg").getFile());
        if (image_file.exists()) {

            try {
                final BufferedImage image = ImageIO.read(image_file);

                loadMany(rabbit, image, "" + reloadCount);

                BangParameter reload = rabbit.createBangParameter("Reload");
                reload.setFunction(() -> {
                    try {
                        reloadCount++;
                        System.out.println("reload images");
                        loadMany(rabbit, image, "" + reloadCount);
                    }
                    catch (RCPParameterException | RCPException _e) {
                        _e.printStackTrace();
                    }
                });

                BangParameter clear = rabbit.createBangParameter("Clear");
                clear.setFunction(() -> {

                    try {
                        clearImages(rabbit);
                    }
                    catch (RCPException _e) {
                        _e.printStackTrace();
                    }

                });
            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }
            catch (RCPException _e) {
                throw new RuntimeException(_e);
            }
        }
        else {
            System.out.println("could not find image: ice.jpg");

            final StringParameter no_image = rabbit.createStringParameter("no image");
            no_image.setReadonly(true);
            no_image.setValue("No Image");
        }
    }
}
