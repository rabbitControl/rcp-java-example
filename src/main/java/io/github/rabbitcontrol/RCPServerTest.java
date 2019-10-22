package io.github.rabbitcontrol;

import io.github.rabbitcontrol.expose.*;
import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.RCPCommands.Init;
import org.rabbitcontrol.rcp.model.RCPCommands.Update;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.transport.websocket.server.WebsocketServerTransporterNetty;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * start with
 * ./gradlew run -Dexec.args="-c exposeSingleFloat"
 */
public class RCPServerTest implements Update, Init {

    public static final int DEFAULT_PORT = 10000;

    //------------------------------------------------------------
    //
    public static void main(final String[] args) {

        String expose_method_name = null;

        int port = DEFAULT_PORT;

        for (int i = 0; i < args.length; i++) {

            final String arg = args[i];

            if ("--config".equals(arg) || "-c".equals(arg)) {
                i++;
                if (i <args.length) {
                    System.out.println("setting expose method name to: " + args[i]);
                    expose_method_name = args[i];
                }

            } else if ("-m".equals(arg)) {

                System.out.println("available method names:");

                final Method[] methods = RCPServerTest.class.getDeclaredMethods();

                for (final Method method : methods) {
                    final Class<?>[] ex_types = method.getExceptionTypes();
                    if (!method.getName().startsWith("_") &&
                        Arrays.toString(ex_types).contains
                            ("RCPParameterException"))
                    {
                        System.out.println(method.getName());
                    }
                }

                System.out.println("\nuse --config, -c <method-name> to call the method on start");

                return;

            } else if ("-h".equals(arg) || "--help".equals(arg)) {

                // print help
                System.out.println("valid arguments: ");
                System.out.println();
                System.out.println("--help, -h\t\t\tshow this help");
                System.out.println("-m\t\t\t\tprints a list with available method-names");
                System.out.println("--config, -c <method-name>\tmethod to call on start to expose" +
                                   " " +
                                   "parameters");
                System.out.println("-p <port>\t\t\tthe port to bind to");

                return;

            } else if ("-p".equals(arg)) {
                i++;

                if (i <args.length) {
                    port = Integer.parseInt(args[i]);
                    System.out.println("setting port: " + port);
                }
            }
        }

        try {
            new RCPServerTest(expose_method_name, port);
        }
        catch (final RCPException _e) {
            _e.printStackTrace();
        }
        catch (RCPParameterException _e) {
            _e.printStackTrace();
        }

    }

    //------------------------------------------------------------
    //
    private final RCPServer rabbit;

    //------------------------------------------------------------
    //
    public RCPServerTest(final String exposeMethodName, final int port) throws
                                                                        RCPException,
                                                                        RCPParameterException {

        final WebsocketServerTransporterNetty transporter = new WebsocketServerTransporterNetty();

        // create rabbit
        rabbit = new RCPServer(transporter);
        rabbit.setApplicationId("java-test-server");

        rabbit.setUpdateListener(this);
        rabbit.setInitListener(this);

        transporter.bind(port);

        //------------------------------------------------------------
        //------------------------------------------------------------
        // expose parameters
        if ((exposeMethodName != null) && !exposeMethodName.isEmpty()) {

            try {
                final Method config_method = RCPServerTest.class.getDeclaredMethod(exposeMethodName);

                System.out.println("calling method: " + config_method.getName());
                config_method.invoke(this);
            }
            catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException _e) {
                System.err.println(_e.getMessage());
                System.exit(1);
            }
        } else {
            System.out.println("no expose method defined. using exposeParameterInGroups");
            exposeParameterInGroups();
        }

        rabbit.update();
    }


    private void exposeVectorParameter() throws RCPParameterException {
        VectorParameterExpose.exposeVectorParameter(rabbit);
    }

    private void exposeFaultyParameter() throws RCPParameterException {
        NumberParameterExpose.exposeFaultyFloat(rabbit);
    }

    private void exposeImageParameter() throws RCPParameterException {
        ImageParameterExpose.exposeImageParameter(rabbit);
    }

    private void exposeBooleanReadonly() throws RCPParameterException {
        BoolParameterExpose.exposeBooleanReadonly(rabbit);
    }

    private void exposeRange() throws RCPParameterException {
        NumberParameterExpose.exposeRange(rabbit);
    }

    private void exposeArray() throws RCPParameterException {
        ArrayParameterExpose.exposeArray(rabbit);
    }

    private void exposeWithWidget() throws RCPParameterException {
        WidgetExpose.exposeWithWidget(rabbit);
    }

    private void exposeGroupWithCustomWidget() throws RCPParameterException {
        WidgetExpose.exposeGroupWithCustomWidget(rabbit);
    }

    private void exposeLong() throws RCPParameterException {
        NumberParameterExpose.exposeLong(rabbit);
    }

    private void exposeSingleFloat() throws RCPParameterException, RCPException {
        NumberParameterExpose.exposeSingleFloat(rabbit);
    }

    private void exposeFloatTest() throws RCPParameterException {
        NumberParameterExpose.exposeFloatTest(rabbit);
    }

    private void exposeParameterInGroups1() throws RCPParameterException {
        GroupExpose.exposeParameterInGroups1(rabbit);
    }

    private void exposeParameterInGroups() throws RCPParameterException {
        GroupExpose.exposeParameterInGroups(rabbit);
    }

    //------------------------------------------------------------
    // interface Update
    @Override
    public void parameterUpdated(final IParameter param) {
        System.out.println("---- parameter updated:");
        param.dump();
    }

    // interface Init
    @Override
    public void init() {
    }
}
