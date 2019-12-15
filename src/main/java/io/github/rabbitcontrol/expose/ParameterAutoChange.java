package io.github.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.Int32Parameter;

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
}
