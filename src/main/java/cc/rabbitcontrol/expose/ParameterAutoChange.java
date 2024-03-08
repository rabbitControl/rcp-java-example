package cc.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.*;

public class ParameterAutoChange {

    public static void exposeIntParameterRandom(final RCPServer rabbit) throws RCPParameterException {

        final Int32Parameter parameter = rabbit.createInt32Parameter("number");

        Thread t = new Thread(() -> {

            while (!Thread.interrupted())
            {
                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException _e) {
                    break;
                }

                int i = (int)(Math.random()*100);
                System.out.println("setting: " + i);
                parameter.setValue(i);
                try {
                    rabbit.update();
                }
                catch (RCPException _e) {
                    _e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public static void exposeParameterChangeLabel(final RCPServer rabbit) throws RCPParameterException {

        final GroupParameter group = rabbit.createGroupParameter("group 1");

        final Float32Parameter parameter = rabbit.createFloatParameter("number", group);

        Thread t = new Thread(() -> {

            while (!Thread.interrupted())
            {
                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException _e) {
                    break;
                }

                int rnd = (int)(Math.random()*100);

                // set datatype
                parameter.setDefault((float)rnd);
                parameter.setMin(rnd-1);
                parameter.setMax(rnd+1);
                parameter.setUnit("unit " + rnd);

                // set parameter
                parameter.setValue((float)rnd);
                parameter.setLabel("number " + rnd);
                parameter.setDescription("desc " + rnd);

                byte[] userdata = new byte[99];
                for (int i=0; i<userdata.length; i++)
                {
                    userdata[i] = (byte)i;
                }
                parameter.setUserdata(userdata);

                try {
                    rabbit.update();
                }
                catch (RCPException _e) {
                    _e.printStackTrace();
                }
            }
        });
        t.start();

    }

    public static void exposeParameterChangeMinMaxVal(final RCPServer rabbit) throws RCPParameterException {

        final Int8Parameter parameter = rabbit.createInt8Parameter("number");

        Thread t = new Thread(() -> {

            while (!Thread.interrupted())
            {
                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException _e) {
                    break;
                }

                int rnd = (int)(Math.random()*50);
                int rnd2 = rnd + 20;

                // set datatype
                parameter.getTypeDefinition().setMinimum((byte)rnd);
                parameter.getTypeDefinition().setMaximum((byte)rnd2);

                // set parameter
                parameter.setValue((byte)(rnd + 10));

                try {
                    rabbit.update();
                }
                catch (RCPException _e) {
                    _e.printStackTrace();
                }
            }
        });
        t.start();

    }

    public static void exposeForCImpl(final RCPServer rabbit) throws RCPParameterException {

        final BooleanParameter bool_parameter = rabbit.createBooleanParameter("bool");

        final Int8Parameter int8_parameter = rabbit.createInt8Parameter("int 8");
        int8_parameter.setValue((byte)8);

        final Int32Parameter int_parameter = rabbit.createInt32Parameter("int 1");
        int_parameter.setMinimum(0);
        int_parameter.setMaximum(100);
        int_parameter.setValue(10);


        Thread t = new Thread(() -> {

            while (!Thread.interrupted())
            {
                try {
                    Thread.sleep(5000);
                }
                catch (InterruptedException _e) {
                    break;
                }

                byte i = (byte)(Math.random()*100);
                System.out.println("setting: " + i);
                int8_parameter.setValue(i);
                try {
                    rabbit.update();
                }
                catch (RCPException _e) {
                    _e.printStackTrace();
                }
            }
        });
        t.start();

    }
}
