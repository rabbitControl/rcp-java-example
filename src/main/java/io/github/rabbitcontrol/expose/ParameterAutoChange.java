package io.github.rabbitcontrol.expose;

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
}
