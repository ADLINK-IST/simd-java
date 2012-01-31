package org.opensplice.demo.shapes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.omg.dds.core.Duration;
import org.omg.dds.core.event.DataAvailableEvent;
import org.omg.dds.core.policy.DestinationOrder;
import org.omg.dds.core.policy.LatencyBudget;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.pub.DataWriter;
import org.omg.dds.pub.DataWriterQos;
import org.omg.dds.pub.Publisher;
import org.omg.dds.runtime.Bootstrap;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.SimpleDataReaderListener;
import org.omg.dds.sub.DataReaderListener;
import org.omg.dds.sub.DataReaderQos;
import org.omg.dds.sub.InstanceState;
import org.omg.dds.sub.Sample;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.topic.Topic;

import org.opensplice.demo.ShapeType;

public class JavaShapesMainWindow extends JFrame implements ActionListener {

    /** The default serialVersionUID */
	private static final long serialVersionUID = 1L;
	private DrawCanvas canvas = null;
    private JPanel mainPanel = null;
    private JPanel leftPanel = null;
    private JPanel rightPanel = null;
    private JPanel publishPanel = null;
    private JPanel subscribePanel = null;
    private ReaderQosDialog dlgReaderQoS = null;
    private WriterQosDialog dlgWriterQoS = null;

    private String subscribeShape = "Circle";
    private String publishShape = "Circle";
    private String publishColor = "RED";
    private int publishSpeed = 10;
    private int publishSize = 50;

    private DomainParticipant dp = null;
    private Subscriber sub = null;
    private Publisher pub = null;
    private DataReaderQos rdrqos = null;
    private DataWriterQos wtrqos = null;

    private Topic<ShapeType> topicCircle = null;
    private Topic<ShapeType> topicTriangle = null;
    private Topic<ShapeType> topicSquare = null;
    private DataReaderListener<ShapeType> circleListener = null;
    private DataReaderListener<ShapeType> squareListener = null;
    private DataReaderListener<ShapeType> triangleListener = null;

    private List<DataReader<ShapeType>> readers = null;
    private List<DataWriter<ShapeType>> writers = null;
    private List<WriterThread> threads = null;

    public JavaShapesMainWindow() {
        super("JavaShapes");
        initialize();
        initializeDds();
        pack();
        setVisible(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            
            public void windowClosing(final java.awt.event.WindowEvent e) {
                tryTerminate();
            }
        });
    }

    private void initializeDds() {
        dp = Bootstrap.runtime().getParticipantFactory().createParticipant();
        sub = dp.createSubscriber();
        pub = dp.createPublisher();
        DataReaderQos drQos = sub.getDefaultDataReaderQos();
        rdrqos = drQos.with(
                DestinationOrder.SourceTimestamp(),
                new LatencyBudget(new Duration(0, 300000)));
        // new Duration(300, TimeUnit.MICROSECONDS)));
        wtrqos = pub.getDefaultDataWriterQos().with(
                DestinationOrder.SourceTimestamp(),
                new LatencyBudget(new Duration(0, 300000)));

        topicCircle = dp.createTopic("Circle", ShapeType.class);
        topicTriangle = dp.createTopic("Triangle", ShapeType.class);
        topicSquare = dp.createTopic("Square", ShapeType.class);
        circleListener = new MyListener("Circle");
        squareListener = new MyListener("Square");
        triangleListener = new MyListener("Triangle");
        readers = new ArrayList<DataReader<ShapeType>>();
        writers = new ArrayList<DataWriter<ShapeType>>();
        threads = new ArrayList<JavaShapesMainWindow.WriterThread>();
        pub.enable();
        sub.enable();
    }

    private void tryTerminate() {
        Iterator<JavaShapesMainWindow.WriterThread> it = threads.iterator();
        while (it.hasNext()) {
            it.next().interrupt();
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }
        if (dp != null) {
            //dp.close();
        }
        System.exit(0);
    }

    private void initialize() {
        setContentPane(getMainPanel());
    }

    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel();
            mainPanel.setPreferredSize(new Dimension(600, 415));
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(getLeftPanel(), BorderLayout.WEST);
            mainPanel.add(getRightPanel(), BorderLayout.CENTER);
        }
        return mainPanel;
    }

    private JPanel getLeftPanel() {
        if (leftPanel == null) {
            leftPanel = new JPanel();
            leftPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(6, 6, 3, 6);
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            leftPanel.add(getPublishPanel(), gbc);
            gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(3, 6, 7, 8);
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            leftPanel.add(getSubscribePanel(), gbc);
        }
        return leftPanel;
    }

    private JPanel getRightPanel() {
        if (rightPanel == null) {
            rightPanel = new JPanel();
            rightPanel.setLayout(new BorderLayout());
            rightPanel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 6));
            JPanel borderPanel = new JPanel();
            borderPanel.setLayout(new BorderLayout());
            borderPanel.setBorder(BorderFactory.createEtchedBorder());
            borderPanel.add(getCanvas(), BorderLayout.CENTER);
            rightPanel.add(borderPanel, BorderLayout.CENTER);

        }
        return rightPanel;
    }

    private DrawCanvas getCanvas() {
        if (canvas == null) {
            canvas = new DrawCanvas();
            canvas.setPreferredSize(new Dimension(400, 400));
        }
        return canvas;
    }

    private JPanel getPublishPanel() {
        if (publishPanel == null) {
            publishPanel = new JPanel();
            publishPanel.setBorder(BorderFactory
                    .createEtchedBorder(EtchedBorder.LOWERED));
            publishPanel.setLayout(new GridBagLayout());
            Insets insets = new Insets(3, 3, 3, 3);
            publishPanel.add(new JLabel("Shape"),
                    getGbc(0, 0, 1, false, insets));
            publishPanel.add(getShapeCombobox("PublishShape"),
                    getGbc(1, 0, 1, true, insets));
            publishPanel.add(new JLabel("Color"),
                    getGbc(0, 1, 1, false, insets));
            publishPanel.add(getColorCombobox("PublishColor"),
                    getGbc(1, 1, 1, true, insets));
            publishPanel.add(new JLabel("Speed"),
                    getGbc(0, 2, 1, false, insets));
            publishPanel.add(getSpeedSlider(),
                    getGbc(1, 2, 1, true, insets));
            publishPanel.add(new JLabel("Size"),
                    getGbc(0, 3, 1, false, insets));
            publishPanel.add(getSizeSlider(),
                    getGbc(1, 3, 1, true, insets));
            publishPanel.add(getButton("QoS", "PublishQoS"),
                    getGbc(0, 4, 2, true, insets));
            publishPanel.add(getButton("Publish", "Publish"),
                    getGbc(0, 5, 2, true, insets));
        }
        return publishPanel;
    }

    private GridBagConstraints getGbc(int x, int y, int width, boolean fill,
            Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        if (fill) {
            gbc.fill = GridBagConstraints.HORIZONTAL;
        }
        if (insets != null) {
            gbc.insets = insets;
        }
        return gbc;
    }

    private JButton getButton(String caption, String command) {
        JButton button = new JButton(caption);
        button.setActionCommand(command);
        button.addActionListener(this);
        return button;
    }

    private ReaderQosDialog getReaderQoSDialog() {
        if (dlgReaderQoS == null) {
            dlgReaderQoS = new ReaderQosDialog(this);
        }
        return dlgReaderQoS;
    }

    private WriterQosDialog getWriterQoSDialog() {
        if (dlgWriterQoS == null) {
            dlgWriterQoS = new WriterQosDialog(this);
        }
        return dlgWriterQoS;
    }

    private JPanel getSubscribePanel() {
        if (subscribePanel == null) {
            subscribePanel = new JPanel();
            subscribePanel.setBorder(BorderFactory
                    .createEtchedBorder(EtchedBorder.LOWERED));
            subscribePanel.setLayout(new GridBagLayout());
            Insets insets = new Insets(3, 3, 3, 3);
            subscribePanel.add(new JLabel("Shape"),
                    getGbc(0, 0, 1, false, insets));
            subscribePanel.add(getShapeCombobox("SubscribeShape"),
                    getGbc(1, 0, 1, true, insets));
            subscribePanel.add(getButton("QoS", "SubscribeQoS"),
                    getGbc(0, 1, 2, true, insets));
            subscribePanel.add(getButton("Filter", "SubscribeFilter"),
                    getGbc(0, 2, 2, true, insets));
            subscribePanel.add(getButton("Subscribe", "Subscribe"),
                    getGbc(0, 3, 2, true, insets));
        }
        return subscribePanel;
    }

    public JComboBox getShapeCombobox(String command) {
        JComboBox shapeCombobox = new JComboBox(new String[] { "Circle",
                "Triangle", "Square" });
        shapeCombobox.setActionCommand(command);
        shapeCombobox.setSelectedIndex(0);
        shapeCombobox.addActionListener(this);
        return shapeCombobox;
    }

    private JSlider getSpeedSlider() {
        final JSlider slider = new JSlider(1, 50, publishSpeed);
        slider.setPreferredSize(new Dimension(50, 20));
        slider.addChangeListener(new ChangeListener() {
            
            public void stateChanged(ChangeEvent e) {
                if (!slider.getValueIsAdjusting()) {
                    publishSpeed = slider.getValue();
                }
            }
        });
        return slider;
    }

    private JSlider getSizeSlider() {
        final JSlider slider = new JSlider(10, 100, publishSize);
        slider.setPreferredSize(new Dimension(50, 20));
        slider.addChangeListener(new ChangeListener() {
            
            public void stateChanged(ChangeEvent e) {
                if (!slider.getValueIsAdjusting()) {
                    publishSize = slider.getValue();
                }
            }
        });
        return slider;
    }

    public JComboBox getColorCombobox(String command) {
        JComboBox colorCombobox = new JComboBox(new String[] { "red",
                "green", "blue", "yellow", "orange", "gray", "cyan" });
        colorCombobox.setActionCommand(command);
        colorCombobox.setSelectedIndex(0);
        colorCombobox.addActionListener(this);
        return colorCombobox;
    }

    String getSourceValue(Object source) {
        if (source instanceof JComboBox) {
            JComboBox cmb = (JComboBox) source;
            return "" + cmb.getSelectedItem();
        }
        if (source instanceof JRadioButton) {
            JRadioButton btn = (JRadioButton) source;
            return source.toString();
        }
        return "";
    }

    
    public void actionPerformed(ActionEvent e) {
        if ("PublishQoS".equals(e.getActionCommand())) {
            DataWriterQos newqos = getWriterQoSDialog().getQoS(wtrqos);
            if (newqos != null) {
                wtrqos = newqos;
            }
        } else if ("SubscribeQoS".equals(e.getActionCommand())) {
            DataReaderQos newqos = getReaderQoSDialog().getQoS(rdrqos);
            if (newqos != null) {
                rdrqos = newqos;
            }
        } else if ("SubscribeShape".equals(e.getActionCommand())) {
            subscribeShape = getSourceValue(e.getSource());
        } else if ("PublishColor".equals(e.getActionCommand())) {
            publishColor = getSourceValue(e.getSource());
        } else if ("PublishShape".equals(e.getActionCommand())) {
            publishShape = getSourceValue(e.getSource());
        } else if ("Subscribe".equals(e.getActionCommand())) {
            subscribe();
        } else if ("Publish".equals(e.getActionCommand())) {
            publish();
        }
    }

    private void subscribe() {
        Topic<ShapeType> topic = topicSquare;
        DataReaderListener<ShapeType> listener = squareListener;
        if ("Circle".equals(subscribeShape)) {
            topic = topicCircle;
            listener = circleListener;
        } else if ("Triangle".equals(subscribeShape)) {
            topic = topicTriangle;
            listener = triangleListener;
        }
        DataReader<ShapeType> dr = sub.createDataReader(
                topic,
                rdrqos,
                listener,
                null /* all status changes */);
        readers.add(dr);
        try {
            dr.waitForHistoricalData(10, TimeUnit.SECONDS);
        } catch (TimeoutException tx) {
            tx.printStackTrace();
        }
    }

    private void publish() {
        Topic<ShapeType> topic = topicSquare;
        if ("Circle".equals(publishShape)) {
            topic = topicCircle;
        } else if ("Triangle".equals(publishShape)) {
            topic = topicTriangle;
        }
        DataWriter<ShapeType> dw = pub.createDataWriter(
                topic,
                wtrqos,
                null,
                null /* all status changes */);
        writers.add(dw);
        dw.enable();
        WriterThread thrd = new WriterThread(dw, publishSpeed, publishSize,
                publishColor, publishShape);
        threads.add(thrd);
        thrd.start();
    }

    private class WriterThread extends Thread {

        private final DataWriter<ShapeType> dw;
        private final int delay;
        private String theShape;
        private ShapeType shape;
        private Random random;
        private int dx;
        private int dy;
        private int max;

        public WriterThread(DataWriter<ShapeType> writer, int speed,
                int size, String color, String strShape) {
            dw = writer;
            theShape = strShape;
            delay = 1000 / speed;
            shape = new ShapeType();
            shape.color = color;
            random = new Random();
            shape.shapesize = size;
            shape.x = random.nextInt(350 - size);
            shape.y = random.nextInt(350 - size);
            dx = speed;
            dy = speed;
            max = 400 - size;
        }

        
        public void run() {
            while (!isInterrupted()) {
                try {
                    dw.write(shape);
                    ShapeData.getInstance().putShape(theShape,
                                shape.color, shape);

                    shape.x += dx;
                    shape.y += dy;
                    if (shape.x > max) {
                        shape.x = max + max - shape.x;
                        dx = -dx;
                    } else if (shape.x < 0) {
                        shape.x = -shape.x;
                        dx = -dx;
                    }
                    if (shape.y > max) {
                        shape.y = max + max - shape.y;
                        dy = -dy;
                    } else if (shape.y < 0) {
                        shape.y = -shape.y;
                        dy = -dy;
                    }
                } catch (TimeoutException e) {

                }
                try {
                    sleep(delay);
                } catch (InterruptedException e) {

                }
            }
            try {
                dw.dispose(null, shape);
            } catch (TimeoutException e) {

            }
            dw.close();
        }

    }

    private class MyListener extends SimpleDataReaderListener<ShapeType> {
        private final String shape;

        public MyListener(String theShape) {
            shape = theShape;
        }

        
        public void onDataAvailable(DataAvailableEvent<ShapeType> status) {
            DataReader<ShapeType> dr = status.getSource();
            Sample.Iterator<ShapeType> it = dr.take();
            while (it.hasNext()) {
                Sample<ShapeType> smp = it.next();
                boolean alive = smp.getInstanceState().equals(
                        InstanceState.ALIVE);
                ShapeType shapeData = smp.getData();
                if (shapeData != null) {
                    if (alive) {
                        ShapeData.getInstance().putShape(shape,
                                shapeData.color, shapeData);
                    } else {
                        ShapeData.getInstance().removeShape(shape,
                                shapeData.color);
                    }
                }
            }
            it.returnLoan();
            it = null;
        }
    }

}
