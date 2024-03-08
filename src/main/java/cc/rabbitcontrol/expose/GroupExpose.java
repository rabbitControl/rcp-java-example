package cc.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.model.types.ImageDefinition;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GroupExpose {

    public static void exposeEmptyGroup(final RCPServer rabbit) throws RCPParameterException
    {
        final GroupParameter group_parameter = rabbit.createGroupParameter("Empty Group");
        group_parameter.setDescription("This is an empty group");
    }

    public static void exposeParameterInGroups1(final RCPServer rabbit) throws RCPParameterException {

        // group 1
        //groupPram1 = rabbit.createGroupParameter("GROUP 1");

        // enumeration
        final EnumParameter enum_parameter = rabbit.createEnumParameter("enum test");
        enum_parameter.getEnumTypeDefinition().addEntry("uno");
        enum_parameter.getEnumTypeDefinition().addEntry("dos");
        enum_parameter.getEnumTypeDefinition().addEntry("tres");
        enum_parameter.getEnumTypeDefinition().addEntry("quattro");
        enum_parameter.getEnumTypeDefinition().setDefault("tres");
        enum_parameter.setValue("dos");

        //RGB color
        final RGBParameter color_parameter = rabbit.createRGBParameter("a color");
        color_parameter.setValue(Color.CYAN);

        // string
        final StringParameter string_parameter = rabbit.createStringParameter("a string");
        string_parameter.setValue("This is a text encoded in utf-8. let's test it:");
        string_parameter.setDescription("description for string");

        final BangParameter bang_parameter = rabbit.createBangParameter("Bang!");
        bang_parameter.setFunction(new Runnable() {

            @Override
            public void run() {

                System.out.println("BANG!");
            }
        });


        // try to get file from resources
        final ClassLoader classLoader = GroupExpose.class.getClassLoader();
        final File        image_file  = new File(classLoader.getResource("ice.jpg").getFile());
        if (image_file.exists()) {

            System.out.println("expose image parameter");

            final ImageParameter image_parameter = rabbit.createImageParameter("image");
            image_parameter.setReadonly(true);
            image_parameter.setImageType(ImageDefinition.ImageType.BMP);

            try {
                final BufferedImage image = ImageIO.read(image_file);
                image_parameter.setValue(image);
            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }
        } else {
            System.out.println("could not find image: ice.jpg");
        }


        // double
        final Float64Parameter double_parameter = rabbit.createFloat64Parameter("a double");
        double_parameter.getTypeDefinition().setMaximum(1000.D);
        double_parameter.getTypeDefinition().setMinimum(0.D);
        double_parameter.setDescription("double description");
        double_parameter.setValue(3.14);

        // float
        final Float32Parameter float_parameter_1 = rabbit.createFloat32Parameter("FLOAT");
        float_parameter_1.setValue(123.F);
        float_parameter_1.getTypeDefinition().setMinimum(100.F);
        float_parameter_1.getTypeDefinition().setMaximum(200.F);

        // float
        final Float32Parameter float_parameter_2 = rabbit.createFloat32Parameter("FLOAT 2");
        float_parameter_2.setValue(33.F);
        //        float_parameter_2.getTypeDefinition().setMinimum(20.F);
        //        float_parameter_2.getTypeDefinition().setMaximum(40.F);

        // int
        final Int32Parameter int32_parameter = rabbit.createInt32Parameter("INT32 LABEL");
        int32_parameter.setValue(333);

        // boolean
        final BooleanParameter bool_parameter = rabbit.createBooleanParameter("toggle button");
        bool_parameter.setValue(true);

        // steal move values to other groups
        rabbit.addParameter(float_parameter_1);
        rabbit.addParameter(float_parameter_2);
    }

    public static void exposeParameterInGroups(final RCPServer rabbit) throws RCPParameterException {

        // group 1
        final GroupParameter group_parameter = rabbit.createGroupParameter("GROUP 1");

        // enumeration
        final EnumParameter enum_parameter = rabbit.createEnumParameter("enum test",
                                                                       group_parameter);
        enum_parameter.getEnumTypeDefinition().addEntry("uno");
        enum_parameter.getEnumTypeDefinition().addEntry("dos");
        enum_parameter.getEnumTypeDefinition().addEntry("tres");
        enum_parameter.getEnumTypeDefinition().addEntry("quattro");
        enum_parameter.getEnumTypeDefinition().setDefault("tres");
        enum_parameter.setValue("dos");

        //RGB color
        final RGBParameter color_parameter = rabbit.createRGBParameter("a color", group_parameter);
        color_parameter.setValue(Color.CYAN);
        final RGBAParameter color_parameter1 = rabbit.createRGBAParameter("a color with alpha",
                                                                         group_parameter);
        color_parameter1.setValue(Color.YELLOW);
        final RGBAParameter color_parameter2 = rabbit.createRGBAParameter("color A");
        color_parameter2.setValue(Color.ORANGE);

        // string
        final StringParameter string_parameter = rabbit.createStringParameter("a string");
        string_parameter.setValue("This is a text encoded in utf-8. let's test it:");
        string_parameter.setDescription("description for string");

        final BangParameter bang_parameter = rabbit.createBangParameter("Bang!");
        bang_parameter.setFunction(new Runnable() {

            @Override
            public void run() {

                System.out.println("BANG!");
            }
        });


        // try to get file from resources
        final ClassLoader classLoader = GroupExpose.class.getClassLoader();
        final File image_file = new File(classLoader.getResource("ice.jpg").getFile());
        if (image_file.exists()) {

            System.out.println("expose image parameter");

            final ImageParameter imageParameter = rabbit.createImageParameter("image");
            imageParameter.setReadonly(true);
            imageParameter.setImageType(ImageDefinition.ImageType.BMP);

            try {
                final BufferedImage image = ImageIO.read(image_file);
                imageParameter.setValue(image);
            }
            catch (final IOException _e) {
                _e.printStackTrace();
            }
        } else {
            System.out.println("could not find image: ice.jpg");
        }


        // double
        final Float64Parameter double_parameter = rabbit.createFloat64Parameter("a double",
                                                                              group_parameter);
        double_parameter.getTypeDefinition().setMaximum(10.D);
        double_parameter.getTypeDefinition().setMinimum(0.D);
        double_parameter.setDescription("double description");
        double_parameter.setValue(3.14);

        // float
        final Float32Parameter float_parameter_1 = rabbit.createFloat32Parameter("FLOAT");
        float_parameter_1.setValue(123.F);
        float_parameter_1.getTypeDefinition().setMinimum(100.F);
        float_parameter_1.getTypeDefinition().setMaximum(200.F);

        // float
        final Float32Parameter flat_parameter_2 = rabbit.createFloat32Parameter("FLOAT 2");
        flat_parameter_2.setValue(33.F);
        flat_parameter_2.getTypeDefinition().setMinimum(20.F);
        flat_parameter_2.getTypeDefinition().setMaximum(40.F);

        // int
        final Int32Parameter int_parameter = rabbit.createInt32Parameter("INT32 LABEL",
                                                                       group_parameter);
        int_parameter.setValue(333);

        // boolean
        final BooleanParameter bool_parameter = rabbit.createBooleanParameter("toggle button",
                                                                            group_parameter);
        bool_parameter.setValue(true);

        // group 2 - as subgroup of group1
        final GroupParameter group_1 = rabbit.createGroupParameter("group 1", group_parameter);

        // group 3 - as subgroup of group1
        final GroupParameter group_2 = rabbit.createGroupParameter("group 2", group_parameter);

        // move parameter to other group
        rabbit.addParameter(group_1, float_parameter_1);
        rabbit.addParameter(group_2, flat_parameter_2);
    }


    public static void exposeParameterChangeParent(final RCPServer rabbit) throws
                                                                                  RCPParameterException
    {
        final GroupParameter group1 = rabbit.createGroupParameter("group1");
        rabbit.createStringParameter("string", group1);

        final GroupParameter group2 = rabbit.createGroupParameter("group2");
        rabbit.createFloat32Parameter("float", group2);

        final BooleanParameter boolParam = rabbit.createBooleanParameter("boolean");

        final int[] state = { 0 };

        Thread t = new Thread(() -> {

            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException _e) {
                _e.printStackTrace();
            }

            for (;;)
            {
                try {
                    Thread.sleep(500);

                    switch (state[0])
                    {
                        case 0:
                            //System.out.println("move parameter to group1");
                            boolParam.setParent(group1);
                            state[0] = 1;
                            break;
                        case 1:
                            //System.out.println("move parameter to group2");
                            boolParam.setParent(group2);
                            state[0] = 2;
                            break;
                        case 2:
                            //System.out.println("move parameter to root");
                            boolParam.setParent(null);
                            state[0] = 0;
                            break;
                    }

                    rabbit.update();
                }
                catch (InterruptedException | RCPException _e) {
                    _e.printStackTrace();
                    break;
                }
            }

        });

        t.start();
    }

    public static void exposeGroupTooLate(final RCPServer rabbit) throws
                                                                           RCPParameterException
    {



    }
}
