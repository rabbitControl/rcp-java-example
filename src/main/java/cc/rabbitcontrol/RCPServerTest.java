package cc.rabbitcontrol;

import cc.rabbitcontrol.expose.*;
import org.rabbitcontrol.rcp.RCP;
import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.RCPCommands.Init;
import org.rabbitcontrol.rcp.model.RCPCommands.Update;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.transport.ServerTransporter;
import org.rabbitcontrol.rcp.transport.tcp.server.TcpServerTransporterNetty;
import org.rabbitcontrol.rcp.transport.websocket.server.RabbitHoleWsServerTransporterNetty;
import org.rabbitcontrol.rcp.transport.websocket.server.WebsocketServerTransporterNetty;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * start with
 * ./gradlew run -Dexec.args="-c exposeSingleFloat"
 */
public class RCPServerTest implements Update, Init {

    public static final int DEFAULT_PORT = 10000;

    enum TransportType {
        udp, tcp, web
    }

    final static List<TransportType> transporterTypes = new ArrayList<>();

    //------------------------------------------------------------
    //
    public static void main(final String[] args) {

        System.out.println("using library version: " + RCP.getLibraryVersion());
        System.out.println("implementing rcp version: " + RCP.getRcpVersion());

        String expose_method_name = null;
        String rabbithole_url = null;

        int port = DEFAULT_PORT;

        for (int i = 0; i < args.length; i++) {

            final String arg = args[i];

            if ("--config".equals(arg) || "-c".equals(arg)) {
                i++;
                if (i <args.length) {
                    System.out.println("setting expose method name to: " + args[i]);
                    expose_method_name = args[i];
                }

            } else if ("-m".equals(arg) || "--list".equals(arg) || "-l".equals(arg)) {

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
                System.out.println("--help, -h\t\t\t\tshow this help");
                System.out.println("-m, -l, --list\t\t\t\tprints a list with available " +
                                   "method-names");
                System.out.println("--config, -c <method-name>\t\tmethod to call on start to expose" +
                                   " " +
                                   "parameters");
                System.out.println("-p <port>\t\t\t\tthe port to bind to");
                System.out.println("-t \t\t\t\t\tuse tcp transporter (websocket otherwise)");
                System.out.println("-u \t\t\t\t\tuse udp transporter (websocket otherwise)");
                System.out.println("--rabbithole, -r <rabbithole uri> \trabbithole " +
                                   "uri containing a valid key");

                return;

            } else if ("-p".equals(arg)) {
                i++;

                if (i <args.length) {
                    port = Integer.parseInt(args[i]);
                    System.out.println("setting port: " + port);
                }
            } else if ("-w".equals(arg)) {
                transporterTypes.add(RCPServerTest.TransportType.web);
            } else if ("-t".equals(arg)) {
                transporterTypes.add(RCPServerTest.TransportType.tcp);
            } else if ("-u".equals(arg)) {
                transporterTypes.add(RCPServerTest.TransportType.udp);
            }
            else if ("--rabbithole".equals(arg) || "-r".equals(arg)) {
                i++;
                if (i <args.length) {
                    rabbithole_url = args[i];
                }
            }
        }

        try {
            new RCPServerTest(expose_method_name, port, rabbithole_url);
        }
        catch (final RCPException | RCPParameterException _e) {
            _e.printStackTrace();
        }

    }

    //------------------------------------------------------------
    //
    private RCPServer rabbit = null;

    //------------------------------------------------------------
    //
    public RCPServerTest(final String exposeMethodName,
                         int port,
                         final String rabbitholeUrl) throws RCPException,
                                                     RCPParameterException
    {
//        RCP.doDebgLogging = true;

        // create rabbit
        rabbit = new RCPServer();

        // add transporter
        // default to websocket
        if (transporterTypes.isEmpty())
        {
            transporterTypes.add(TransportType.web);
        }

        for (TransportType tt : transporterTypes)
        {
            ServerTransporter transporter = null;

            if (tt == TransportType.web)
            {
                System.out.println("adding websocket transporter on port: " + port);
                transporter = new WebsocketServerTransporterNetty();
            }
            else if (tt == TransportType.tcp)
            {
                System.out.println("adding tcp transporter on port: " + port);
                transporter = new TcpServerTransporterNetty();
            }
            else if (tt == TransportType.udp)
            {
                System.out.println("not adding udp transporter on port: " + port);
                continue;
            }

            rabbit.addTransporter(transporter);
            transporter.bind(port);
            port++;
        }

        rabbit.setApplicationId("java-test-server");

        rabbit.setUpdateListener(this);
        rabbit.setInitListener(this);

        // rabbithole transporter
        if ((rabbitholeUrl != null) && !rabbitholeUrl.isEmpty())
        {
            try {
                System.out.println("create rabbithole transporter");

                final URI url = new URI(rabbitholeUrl);

                RabbitHoleWsServerTransporterNetty rhlTransporter =
                        new RabbitHoleWsServerTransporterNetty(url);

                rabbit.addTransporter(rhlTransporter);
                rhlTransporter.bind(0);

//                rhlTransporter.addListener((_bytes, _serverTransporter, _o) -> {
//                    System.out.println("rhl received: " + ParserTest.bytesToHex(_bytes));
//                });
            }
            catch (URISyntaxException _e) {
                _e.printStackTrace();
            }
        }

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
            System.out.println("no expose method defined - using exposeParameterInGroups");
            exposeParameterInGroups();
        }

        rabbit.update();
    }


    private void exposeFaultyParameter() throws RCPParameterException {
        NumberParameterExpose.exposeFaultyFloat(rabbit);
    }

    private void exposeImageParameter() throws RCPParameterException {
        ImageParameterExpose.exposeImageParameter(rabbit);
    }

    private void exposeManyImageParameter() throws RCPParameterException {
        ImageParameterExpose.exposeManyImageParameter(rabbit);
    }

    private void exposeBooleanReadonly() throws RCPParameterException {
        BoolParameterExpose.exposeBooleanReadonly(rabbit);
    }

    private void exposeRange() throws RCPParameterException {
        RangeParameterExpose.exposeRange(rabbit);
    }

    private void exposeRangeOutOfBounds() throws RCPParameterException {
        RangeParameterExpose.exposeRangeOutOfBounds(rabbit);
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

    private void exposeSingleFloat64() throws RCPParameterException, RCPException {
        NumberParameterExpose.exposeSingleFloat64(rabbit);
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

    private void exposeColorParameters() throws RCPParameterException {
        ColorParameterExpose.exposeColorParameters(rabbit);
    }

    private void exposeOrdered() throws RCPParameterException {
        OrderedParameterExpose.exposeOrdered(rabbit);
    }

    private void exposeOrderedGroups() throws RCPParameterException {
        OrderedParameterExpose.exposeOrderedinGroups(rabbit);
    }

    private void exposeIntParameterRandom() throws RCPParameterException {
        ParameterAutoChange.exposeIntParameterRandom(rabbit);
    }

    private void exposeOutOfBoundFloat() throws RCPParameterException {
        NumberParameterExpose.exposeOutOfBoundFloat(rabbit);
    }

    //--------------------------------------------
    // vector parameters
    private void exposeVectorParameter() throws RCPParameterException {
        VectorParameterExpose.exposeVectorParameter(rabbit);
    }

    private void exposeVectorParameterOutOfBounds() throws RCPParameterException {
        VectorParameterExpose.exposeVectorParameterOutOfBounds(rabbit);
    }

    private void exposeVectorParameterMinMax() throws RCPParameterException {
        VectorParameterExpose.exposeVectorParameterMinMax(rabbit);
    }

    //--------------------------------------------
    //
    private void exposeByte() throws RCPParameterException {
        NumberParameterExpose.exposeByte(rabbit);
    }

    private void exposeInt16() throws RCPParameterException {
        NumberParameterExpose.exposeInt16(rabbit);
    }

    private void exposeInt32() throws RCPParameterException {
        NumberParameterExpose.exposeInt32(rabbit);
    }

    private void exposeForCImpl() throws RCPParameterException {

        System.out.println("export for C");
        NumberParameterExpose.exposeForCImpl(rabbit);
    }

    private void exposeForCImplAutoChange() throws RCPParameterException
    {
        System.out.println("export for C");
        ParameterAutoChange.exposeForCImpl(rabbit);
    }

    private void exposeParameterChangeLabel() throws RCPParameterException
    {
        ParameterAutoChange.exposeParameterChangeLabel(rabbit);
    }

    private void exposeParameterChangeMinMaxVal() throws RCPParameterException
    {
        ParameterAutoChange.exposeParameterChangeMinMaxVal(rabbit);
    }


    private void exposeEmptyGroup() throws RCPParameterException {
        GroupExpose.exposeEmptyGroup(rabbit);
    }



    private void exposeGroupAsTabs() throws RCPParameterException
    {
        WidgetExpose.exposeGroupAsTabs(rabbit);
    }

    private void exposeParameterChangeParent() throws RCPParameterException
    {
        GroupExpose.exposeParameterChangeParent(rabbit);
    }

    private void exposeEnum() throws RCPParameterException
    {
        EnumParameterExpose.exposeEnum(rabbit);
    }

    private void exposeDynamicEnums() throws RCPParameterException
    {
        EnumParameterExpose.exposeDynamicEnums(rabbit);
    }

    private void exposeOneGroupAsTabs() throws RCPParameterException
    {
        WidgetExpose.exposeOneGroupAsTabs(rabbit);
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
