package io.github.rabbitcontrol;

import org.rabbitcontrol.rcp.RCPClient;
import org.rabbitcontrol.rcp.model.RCPCommands.*;
import org.rabbitcontrol.rcp.model.exceptions.RCPException;
import org.rabbitcontrol.rcp.model.interfaces.IParameter;
import org.rabbitcontrol.rcp.model.parameter.NumberParameter;
import org.rabbitcontrol.rcp.model.parameter.StringParameter;
import org.rabbitcontrol.rcp.transport.websocket.client.WebsocketClientTransporter;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class RCPClientTest extends JFrame implements Add, Remove, Update {

    public static final String HOST = "localhost";

    public static final int PORT = 10000;

    //------------------------------------------------------------
    //
    public static void main(final String[] args) throws RCPException {

        new RCPClientTest();
    }

    //------------------------------------------------------------
    //
    private final RCPClient rcp;

    ArrayList<IParameter> allParams = new ArrayList<>();

    final GridLayout layout = new GridLayout();

    Map<Short, Component> componentMap = new HashMap<>();

    int rowcount;

    int count;

    //------------------------------------------------------------
    //
    public RCPClientTest() throws RCPException {

        setupFrame();

        // create serializer and transporter
        final WebsocketClientTransporter transporter = new WebsocketClientTransporter();


        // create rcp client
        rcp = new RCPClient(transporter);
        rcp.setUpdateListener(this);
        rcp.setAddListener(this);
        rcp.setRemoveListener(this);

        transporter.connect(HOST, PORT);

        // init
        rcp.initialize();
    }

    private void setupFrame() {

        setLayout(layout);

        pack();
        setSize(800, 48);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void addComponent(final Component _component) {

        add(_component);
        rowcount++;
        layout.setRows(rowcount);

        setSize(getWidth(), 48 + (48 * rowcount));

        getContentPane().repaint();
    }

    private void removeComponent(final Component _component) {
        remove(_component);
        if (--rowcount < 0) {
            rowcount = 0;
        }
        layout.setRows(rowcount);
    }


    public void updateValue() {

        final Map<Short, IParameter> cache = rcp.getValueCache();

        if (!cache.isEmpty()) {

            final Object[] objs = cache.keySet().toArray();

            final IParameter parameter = cache.get(objs[0]);

            if (parameter instanceof StringParameter) {

                final StringParameter stringParam = (StringParameter)parameter;

                if (stringParam.getValue() == null) {
                    stringParam.setValue("-");
                } else {
                    stringParam.setValue(stringParam.getValue() + "-");
                }
            }
            else if (parameter instanceof NumberParameter) {
                ((NumberParameter)parameter).setValue(count++);
            }
        }
        else {
            System.err.println("no values");
        }
    }

    //------------------------------------------------------------
    //
    @Override
    public void parameterAdded(final IParameter _parameter) {

        System.out.println("adding parameter: " + _parameter.getLabel());

        if (!allParams.contains(_parameter)) {
            allParams.add(_parameter);
            _parameter.dump();

            final JPanel panel = new JPanel(new BorderLayout());

            //--------------------
            // set label
            final JLabel label  = new JLabel(_parameter.getLabel());
            final Border border = label.getBorder();
            Border       margin = new EmptyBorder(0, 0, 0, 4);
            label.setBorder(new CompoundBorder(border, margin));
            panel.add(label, BorderLayout.WEST);

            //--------------------
            // add text-field
            final JTextField text_field = new JTextField(_parameter.getStringValue());

            text_field.setBorder(BorderFactory.createCompoundBorder(
                    text_field.getBorder(),
                    BorderFactory.createEmptyBorder(0, 4, 0, 4)));

            text_field.addActionListener(e -> {
                _parameter.setStringValue(text_field.getText());
                rcp.update();
            });

            panel.add(text_field, BorderLayout.CENTER);

            //--------------------
            // add panel
            SwingUtilities.invokeLater(() -> addComponent(panel));

            componentMap.put(_parameter.getId(), panel);

            _parameter.addValueUpdateListener(_parameter1 -> text_field.setText(_parameter1.getStringValue()));

        }
    }

    @Override
    public void parameterUpdated(final IParameter _value) {

        System.out.println("client: updated: " + _value.getId());
        _value.dump();
    }

    @Override
    public void parameterRemoved(final IParameter _value) {

        System.out.println("client: removed: " + _value.getId());
        _value.dump();
    }
}
