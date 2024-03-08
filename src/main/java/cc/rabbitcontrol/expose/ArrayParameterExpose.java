package cc.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.RcpTypes.Datatype;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.ArrayParameter;

public class ArrayParameterExpose {

    public static void exposeArray(final RCPServer rabbit) throws RCPParameterException {

        //--------------------------------------------
        // expose 1 dimensions array of strings with 3 elements
        final ArrayParameter<String[], String> array_parameter_1 = rabbit.createArrayParameter(
                "String Array",
                Datatype.STRING,
                3);
        array_parameter_1.setValue(new String[] {"cc/rabbitcontrol", "string", "array" });

        //--------------------------------------------
        // expose one-dimensional array of doubles with 6 elements
        final ArrayParameter<Double[], Double> array_parameter_2 = rabbit.createArrayParameter(
                "double array",
                Datatype.FLOAT64,
                6);

        array_parameter_2.getArrayDefinition().setDefault(new Double[] { 1.1,
                                                                         2.2,
                                                                         3.3,
                                                                         4.4,
                                                                         5.5,
                                                                         6.6 });
        array_parameter_2.setValue(new Double[] { 10.1, 20.2, 30.3, 40.4, 50.5, 60.6 });

        //--------------------------------------------
        // expose 2-dimensional array of integer: 3, 2
        final ArrayParameter<Integer[][], Integer> array_parameter_3 = rabbit.createArrayParameter(
                "int 2D",
                Datatype.INT32,
                3,
                2);

        array_parameter_3.setValue(new Integer[][] { { 1024, 2048 },
                                                     { 3333, 6666 },
                                                     { 10000, 10001 } });

        //--------------------------------------------
        // expose array with 3 dimensions: 4, 2, 2
        final ArrayParameter<Byte[][][], Byte> array_parameter_4 = rabbit.createArrayParameter(
                "byte-array 3-2-2",
                Datatype.INT8,
                4,
                2,
                2);

        // set default value of element type
        array_parameter_4.getElementType().setDefault((byte)123);

        // set value
        array_parameter_4.setValue(new Byte[][][] { { { 1, 2 }, { 3, 4 } },
                                                    { { 11, 12 }, { 13, 14 } },
                                                    { { 21, 22 }, { 23, 24 } },
                                                    { { 31, 32 }, { 33, 34 } } });

        // change one element in the array
        array_parameter_4.getValue()[0][0][0] = 111;
    }
}
