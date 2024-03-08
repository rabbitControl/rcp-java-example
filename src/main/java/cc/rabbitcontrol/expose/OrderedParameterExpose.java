package cc.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.*;

public class OrderedParameterExpose {

    public static void exposeOrdered(final RCPServer rabbit) throws RCPParameterException {

        final StringParameter parameter = rabbit.createStringParameter("first - order 10");
        parameter.setOrder(10);

        final StringParameter parameter2 = rabbit.createStringParameter("2nd - order 0");

        final StringParameter parameter3 = rabbit.createStringParameter("3rd - order -10");
        parameter3.setOrder(-10);

        final StringParameter parameter4 = rabbit.createStringParameter("4th - order 5");
        parameter4.setOrder(5);

        BangParameter btn = rabbit.createBangParameter("reorder #1");
        btn.setOrder(100);
        btn.setFunction(() -> {
            parameter.setOrder(-100);
            try {
                rabbit.update();
            }
            catch (RCPException _e) {
                _e.printStackTrace();
            }
        });

        btn = rabbit.createBangParameter("reorder #2");
        btn.setOrder(101);
        btn.setFunction(() -> {
            parameter.setOrder(10);
            try {
                rabbit.update();
            }
            catch (RCPException _e) {
                _e.printStackTrace();
            }
        });
    }


    public static void exposeOrderedinGroups(final RCPServer rabbit) throws RCPParameterException {

        final GroupParameter group1 = rabbit.createGroupParameter("group #1");
        group1.setOrder(10);

        final StringParameter parameter = rabbit.createStringParameter("first - order 10", group1);
        parameter.setOrder(10);

        final StringParameter parameter2 = rabbit.createStringParameter("2nd - order 0", group1);


        final GroupParameter group2= rabbit.createGroupParameter("group #2");
        group2.setOrder(-10);

        final StringParameter parameter3 = rabbit.createStringParameter("3rd - order -10", group2);
        parameter3.setOrder(-10);

        final StringParameter parameter4 = rabbit.createStringParameter("4th - order 5", group2);
        parameter4.setOrder(5);

        BangParameter btn = rabbit.createBangParameter("reorder #1");
        btn.setOrder(100);
        btn.setFunction(() -> {
            parameter.setOrder(-100);
            group1.setOrder(-100);
            try {
                rabbit.update();
            }
            catch (RCPException _e) {
                _e.printStackTrace();
            }
        });

        btn = rabbit.createBangParameter("reorder #2");
        btn.setOrder(101);
        btn.setFunction(() -> {
            parameter.setOrder(10);
            group1.setOrder(10);
            try {
                rabbit.update();
            }
            catch (RCPException _e) {
                _e.printStackTrace();
            }
        });
    }
}
