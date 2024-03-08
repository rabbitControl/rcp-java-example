package io.github.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.Widget;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.GroupParameter;
import org.rabbitcontrol.rcp.model.parameter.StringParameter;
import org.rabbitcontrol.rcp.model.widgets.*;

import java.util.UUID;

public class WidgetExpose {

    public static void exposeWithWidget(final RCPServer rabbit) throws RCPParameterException
    {
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

    public static void exposeGroupWithCustomWidget(final RCPServer rabbit) throws RCPParameterException
    {
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


    public static void exposeGroupAsTabs(final RCPServer rabbit) throws RCPParameterException
    {

        final GroupParameter tab1 = rabbit.createGroupParameter("long_nam_tab1");
        tab1.setWidget(new TabsWidget());

        // add some params
        rabbit.createBooleanParameter("bool", tab1);
        rabbit.createFloat32Parameter("float", tab1);

        final GroupParameter g1 = rabbit.createGroupParameter("group1", tab1);
        g1.setWidget(new TabsWidget());

        final GroupParameter sub1 = rabbit.createGroupParameter("sub1", g1);
        final GroupParameter sub2 = rabbit.createGroupParameter("sub2", g1);

        rabbit.createStringParameter("sub1 string", sub1);
        rabbit.createStringParameter("sub2 string", sub2);

        rabbit.createBooleanParameter("b1", g1);
        rabbit.createBooleanParameter("b2", g1);

        final GroupParameter g2 = rabbit.createGroupParameter("group2", tab1);
        final GroupParameter g2sub1 = rabbit.createGroupParameter("g2_sub1", g2);
        rabbit.createBooleanParameter("b6", g2sub1);
        rabbit.createBooleanParameter("b7", g2sub1);


        final GroupParameter g3 = rabbit.createGroupParameter("group3", tab1);



        rabbit.createBooleanParameter("b2", g2);
        rabbit.createBooleanParameter("b3", g3);


        final GroupParameter tab2 = rabbit.createGroupParameter("long_nam_tab2");
        rabbit.createBooleanParameter("tab2 bool", tab2);

        final GroupParameter tab3 = rabbit.createGroupParameter("long_nam_tab3");
        rabbit.createStringParameter("tab3 string", tab3);


        rabbit.createStringParameter("root string");
        rabbit.createStringParameter("root string2");

        final GroupParameter tab4 = rabbit.createGroupParameter("long_nam_tab4");
        rabbit.createStringParameter("tab4-string", tab4);

        final GroupParameter tab5 = rabbit.createGroupParameter("long_nam_tab5");
        rabbit.createBooleanParameter("tab5 bool", tab5);

        final GroupParameter tab6 = rabbit.createGroupParameter("long_nam_tab6");
        rabbit.createBooleanParameter("tab6 bool", tab6);
    }

    public static void exposeOneGroupAsTabs(final RCPServer rabbit) throws RCPParameterException
    {
        rabbit.createBangParameter("TEST");

        final GroupParameter maingroup = rabbit.createGroupParameter("maingroup");
        maingroup.setWidget(new TabsWidget());


        final GroupParameter group1 = rabbit.createGroupParameter("group1", maingroup);

        final StringParameter s1 = rabbit.createStringParameter("string1", group1);
        s1.setStringValue("string 1");


        final GroupParameter group2 = rabbit.createGroupParameter("group2", maingroup);

        final StringParameter s2 = rabbit.createStringParameter("string2", group2);
        s1.setStringValue("string 2");


        final GroupParameter group3 = rabbit.createGroupParameter("group3", maingroup);

        final StringParameter s3 = rabbit.createStringParameter("string3", group3);
        s1.setStringValue("string 3");

    }
}
