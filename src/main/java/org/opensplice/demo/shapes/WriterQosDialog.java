package org.opensplice.demo.shapes;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.omg.dds.pub.DataWriterQos;

public class WriterQosDialog extends JDialog implements ActionListener {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;
    private JPanel dialogPanel = null;
    private ButtonGroup bgReliability = null;
    private ButtonGroup bgOwnership = null;

    private DataWriterQos qos;

    public WriterQosDialog(Frame owner) {
        super(owner, true);
        initialize();
        pack();
        setLocationRelativeTo(null);
    }

    protected void initialize() {
        setContentPane(getDialogPanel());
    }

    protected JPanel getDialogPanel() {
        if (dialogPanel == null) {
            dialogPanel = new JPanel();
            dialogPanel.setLayout(new GridBagLayout());
            Insets insets = new Insets(3, 3, 3, 3);
            JLabel lbl = new JLabel("Reliability QoS");
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD).deriveFont(19.0F));
            dialogPanel.add(lbl, getGbcR(0, 0, 2, false, insets));
            bgReliability = new ButtonGroup();
            dialogPanel
                    .add(getRadioButton("Reliable", "Reliable", bgReliability,
                            false), getGbcL(0, 1, 1, false, insets));
            dialogPanel.add(
                    getRadioButton("Best Effort", "BestEffort", bgReliability,
                            true), getGbcL(1, 1, 1, false, insets));
            lbl = new JLabel("Ownersip QoS");
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            dialogPanel.add(lbl, getGbcL(0, 2, 2, false, insets));
            bgOwnership = new ButtonGroup();
            dialogPanel
                    .add(
                            getRadioButton("Exclusive", "Exclusive",
                                    bgOwnership, false),
                            getGbcR(0, 3, 1, false, insets));
            dialogPanel.add(
                    getRadioButton("Shared", "Shared", bgOwnership, true),
                    getGbcL(1, 3, 1, false, insets));
            dialogPanel.add(getButton("Cancel", "Cancel", KeyEvent.VK_C),
                    getGbc(0, 9, 1, true, insets));
            dialogPanel.add(getButton("OK", "OK", KeyEvent.VK_O),
                    getGbc(1, 9, 1, true, insets));
        }
        return dialogPanel;
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

    private GridBagConstraints getGbcR(int x, int y, int width, boolean fill,
            Insets insets) {
        GridBagConstraints gbc = getGbc(x, y, width, fill, insets);
        gbc.anchor = GridBagConstraints.LINE_END;
        return gbc;
    }

    private GridBagConstraints getGbcL(int x, int y, int width, boolean fill,
            Insets insets) {
        GridBagConstraints gbc = getGbc(x, y, width, fill, insets);
        gbc.anchor = GridBagConstraints.LINE_START;
        return gbc;
    }

    private JButton getButton(String caption, String command, int mnemonic) {
        JButton button = new JButton(caption);
        if (mnemonic != 0) {
            button.setMnemonic(mnemonic);
        }
        button.setActionCommand(command);
        button.addActionListener(this);
        return button;
    }

    private JRadioButton getRadioButton(String caption, String command,
            ButtonGroup group, boolean selected) {
        JRadioButton radioButton = new JRadioButton(caption);
        radioButton.setActionCommand(command);
        radioButton.setSelected(selected);
        group.add(radioButton);
        radioButton.addActionListener(this);
        return radioButton;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "OK") {
            setVisible(false);
        } else if (e.getActionCommand() == "Cancel") {
            qos = null;
            setVisible(false);
        } else if (e.getActionCommand() == "Reliable") {

        }
    }

    public DataWriterQos getQoS(DataWriterQos theQos) {
        qos = theQos;
        setVisible(true);
        return qos;
    }
}
