package cc.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.EnumParameter;
import org.rabbitcontrol.rcp.model.widgets.RadiobuttonWidget;

public class EnumParameterExpose {

    public static void exposeEnum(final RCPServer rabbit) throws RCPParameterException {

        final EnumParameter parameter = rabbit.createEnumParameter("enum");
        parameter.getEnumTypeDefinition().addEntry("uno");
        parameter.getEnumTypeDefinition().addEntry("dos");
        parameter.getEnumTypeDefinition().addEntry("tres");

        parameter.setValue("dos");

        parameter.setWidget(new RadiobuttonWidget());
    }


    public static void exposeDynamicEnums(final RCPServer rabbit) throws RCPParameterException {

        final EnumParameter setting_param = rabbit.createEnumParameter("Settings");
        setting_param.getEnumTypeDefinition().addEntry("uno");
        setting_param.getEnumTypeDefinition().addEntry("dos");
        setting_param.setValue("uno");

        final EnumParameter enum_1 = rabbit.createEnumParameter("Enum 1");
        enum_1.getEnumTypeDefinition().addEntry("a");
        enum_1.getEnumTypeDefinition().addEntry("b");
        enum_1.getEnumTypeDefinition().addEntry("c");
        enum_1.setValue("b");

        setting_param.addValueUpdateListener(_parameter -> {

            System.out.println("setting: " + _parameter.getStringValue());

            if (_parameter.getStringValue().compareTo("dos") == 0) {
                System.out.println("removing abc");
                enum_1.getEnumTypeDefinition().removeEntry("a");
                enum_1.getEnumTypeDefinition().removeEntry("b");
                enum_1.getEnumTypeDefinition().removeEntry("c");

                enum_1.getEnumTypeDefinition().addEntry("x");
                enum_1.getEnumTypeDefinition().addEntry("y");
                enum_1.getEnumTypeDefinition().addEntry("z");
                enum_1.setValue("z");
            }
            else if (_parameter.getStringValue().compareTo("uno") == 0) {
                System.out.println("removing xyz");
                enum_1.getEnumTypeDefinition().removeEntry("x");
                enum_1.getEnumTypeDefinition().removeEntry("y");
                enum_1.getEnumTypeDefinition().removeEntry("z");

                enum_1.getEnumTypeDefinition().addEntry("a");
                enum_1.getEnumTypeDefinition().addEntry("b");
                enum_1.getEnumTypeDefinition().addEntry("c");
                enum_1.setValue("b");
            }

            try {
                rabbit.update();
            } catch (RCPException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });


    }
}
