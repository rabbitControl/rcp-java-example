package cc.rabbitcontrol.expose;

import org.rabbitcontrol.rcp.RCPServer;
import org.rabbitcontrol.rcp.model.exceptions.RCPParameterException;
import org.rabbitcontrol.rcp.model.parameter.*;
import org.rabbitcontrol.rcp.model.types.*;

public class VectorParameterExpose {

    public static void exposeVectorParameter(final RCPServer rabbit) throws RCPParameterException {

        // vector 2
        final GroupParameter group_vec2 = rabbit.createGroupParameter("Vector 2");

        final Vector2Float32Parameter vec2_f = rabbit.createVector2Float32Parameter("float", group_vec2);
        final Vector2Int32Parameter   vec2_i = rabbit.createVector2Int32Parameter("int", group_vec2);
        final Vector2Int32Parameter vec2_i_mm = rabbit.createVector2Int32Parameter("int min/max",
                                                                                   group_vec2);

        vec2_i_mm.setMinimum(new Vector2<Integer>(0, 0));
        vec2_i_mm.setMaximum(new Vector2<Integer>(10, 10));


        // vector 3
        final GroupParameter group_vec3 = rabbit.createGroupParameter("Vector 3");

        final Vector3Float32Parameter param = rabbit.createVector3Float32Parameter("vector " +
                                                                                   "plain", group_vec3);
        param.setValue(new Vector3<Float>(10.F, 20.F, 30.F));

        final Vector3Float32Parameter param_minmax = rabbit.createVector3Float32Parameter
                ("vector min/max", group_vec3);

        param_minmax.setMinimum(new Vector3<Float>(-1.F, -1.F, -1.F));
        param_minmax.setMaximum(new Vector3<Float>(1.F, 1.F, 1.F));
        param_minmax.setValue(new Vector3<Float>(0.5F, 0.1F, -0.8F));
        param_minmax.setUnit("unit");

        final Vector3Float32Parameter vector_group = rabbit.createVector3Float32Parameter(
                "vector min only",
                group_vec3);

        vector_group.setMinimum(new Vector3<Float>(0.F, 0.F, 0.F));
        vector_group.setValue(new Vector3<Float>(1.F, 2.F, 3.F));

        final Vector3Int32Parameter vec3_i = rabbit.createVector3Int32Parameter("int", group_vec3);
        vec3_i.setMinimum(new Vector3<Integer>(0, 0, 0));
        vec3_i.setMaximum(new Vector3<Integer>(10, 20, 30));


        // vector 4
        final GroupParameter group_vec4 = rabbit.createGroupParameter("Vector 4");

        final Vector4Float32Parameter vec4_f = rabbit.createVector4Float32Parameter("float",
                                                                                    group_vec4);
        final Vector4Int32Parameter vec4_i = rabbit.createVector4Int32Parameter("int", group_vec4);

        final Vector4Float32Parameter vec4_f_mm = rabbit.createVector4Float32Parameter("float " +
                                                                                       "min/max",
                                                                                       group_vec4);

        vec4_f_mm.setMinimum(new Vector4<Float>(0.F, 0.F, 0.F, 0.F));
        vec4_f_mm.setMaximum(new Vector4<Float>(2.F, 2.F, 2.F, 5.F));
    }


    public static void exposeVectorParameterMinMax(final RCPServer rabbit) throws RCPParameterException {

        final GroupParameter group_vec2 = rabbit.createGroupParameter("Vector 2");
        final Vector2Float32Parameter param1 = rabbit.createVector2Float32Parameter
                ("vector min/max", group_vec2);

        param1.setMinimum(new Vector2<Float>(-1.F, -1.F));
        param1.setMaximum(new Vector2<Float>(1.F, 1.F));
        param1.setValue(new Vector2<Float>(1.5F, -10.1F));


        final GroupParameter group_vec3 = rabbit.createGroupParameter("Vector 3");
        final Vector3Float32Parameter param_minmax = rabbit.createVector3Float32Parameter
                ("vector min/max", group_vec3);

        param_minmax.setMinimum(new Vector3<Float>(-1.F, -1.F, -1.F));
        param_minmax.setMaximum(new Vector3<Float>(1.F, 1.F, 1.F));
        param_minmax.setValue(new Vector3<Float>(0.5F, 0.1F, -0.8F));


        final GroupParameter group_vec4 = rabbit.createGroupParameter("Vector 4");
        final Vector4Float32Parameter param3 = rabbit.createVector4Float32Parameter
                ("vector min/max", group_vec4);

        param3.setMinimum(new Vector4<Float>(-1.F, -1.F, -1.F, -1.F));
        param3.setMaximum(new Vector4<Float>(1.F, 1.F, 1.F, 1.F));
        param3.setValue(new Vector4<Float>(-10.5F, 10.1F, -10.8F, 10.F));
    }

    public static void exposeVectorParameterOutOfBounds(final RCPServer rabbit) throws RCPParameterException {

        final GroupParameter group_vec2 = rabbit.createGroupParameter("Vector 2");
        final Vector2Float32Parameter param1 = rabbit.createVector2Float32Parameter
                ("vector min/max", group_vec2);

        param1.setMinimum(new Vector2<Float>(-1.F, -1.F));
        param1.setMaximum(new Vector2<Float>(1.F, 1.F));
        param1.setValue(new Vector2<Float>(1.5F, -10.1F));


        final GroupParameter group_vec3 = rabbit.createGroupParameter("Vector 3");
        final Vector3Float32Parameter param_minmax = rabbit.createVector3Float32Parameter
                ("vector min/max", group_vec3);

        param_minmax.setMinimum(new Vector3<Float>(-1.F, -1.F, -1.F));
        param_minmax.setMaximum(new Vector3<Float>(1.F, 1.F, 1.F));
        param_minmax.setValue(new Vector3<Float>(1.5F, 10.1F, -10.8F));


        final GroupParameter group_vec4 = rabbit.createGroupParameter("Vector 4");
        final Vector4Float32Parameter param3 = rabbit.createVector4Float32Parameter
                ("vector min/max", group_vec4);

        param3.setMinimum(new Vector4<Float>(-1.F, -1.F, -1.F, -1.F));
        param3.setMaximum(new Vector4<Float>(1.F, 1.F, 1.F, 1.F));
        param3.setValue(new Vector4<Float>(-10.5F, 10.1F, -10.8F, 10.F));
    }
}
