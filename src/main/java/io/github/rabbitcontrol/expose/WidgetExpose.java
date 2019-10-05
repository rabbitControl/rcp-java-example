package io.github.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.Widget;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;
import org.rabbitcontrol.rcp.model.parameter.StringParameter;
import org.rabbitcontrol.rcp.model.widgets.CustomWidget;
import org.rabbitcontrol.rcp.model.widgets.TextboxWidget;

import java.util.UUID;

public class WidgetExpose {

    public static void exposeWithWidget(final RCPServer rabbit) throws RCPParameterException {

        // string
        final StringParameter parameter = rabbit.createStringParameter("string with widget");
        parameter.setValue("This is a text encoded in utf-8. let's test it:");

        parameter.setDescription("test description 2");
        parameter.setUserdata("some user data?".getBytes());

        parameter.setLanguageLabel("eng", "another label");
        parameter.setLanguageLabel("deu", "deutsches label");
        parameter.setLanguageLabel("fra", "une label francaise");

        parameter.setLanguageDescription("eng", "english description");
        parameter.setLanguageDescription("deu", "deutsche beschreibung");
        parameter.setLanguageDescription("fra", "escription francoise");

        parameter.getTypeDefinition().setDefault("defaultstring");

        final Widget str_widget = new TextboxWidget();
        parameter.setWidget(str_widget);
    }

    public static void exposeGroupWithCustomWidget(final RCPServer rabbit) throws RCPParameterException {

        // setup custom widget
        final CustomWidget custom_widget = new CustomWidget();
        custom_widget.setUuid(UUID.fromString("69babd06-72d9-11e8-adc0-fa7ae01bbebc"));
        custom_widget.setConfig("testconfig".getBytes());

        // group 1
        final GroupParameter group = rabbit.createGroupParameter("GROuP");
        group.setWidget(custom_widget);

        // add parameter to group
        rabbit.createInt8Parameter("int 1", group);
        rabbit.createInt8Parameter("int 2", group);
        rabbit.createInt8Parameter("int 3", group);
    }
}
