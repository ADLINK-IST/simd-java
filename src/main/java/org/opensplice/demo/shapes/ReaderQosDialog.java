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

import org.omg.dds.sub.DataReaderQos;

public class ReaderQosDialog extends JDialog implements ActionListener {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;
    private JPanel dialogPanel = null;
    private ButtonGroup bgReliability = null;
    private ButtonGroup bgOwnership = null;

    private DataReaderQos qos;

    public ReaderQosDialog(Frame owner) {
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
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(3, 3, 3, 3);
            JLabel lbl = new JLabel("Reliability QoS");
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD).deriveFont(19.0F));
            dialogPanel.add(lbl, gbc);
            bgReliability = new ButtonGroup();
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.weightx = 1.0;
            gbc.insets = new Insets(3, 3, 3, 3);
            dialogPanel
                    .add(getRadioButton("Reliable", "Reliable", bgReliability,
                            false), gbc);
            gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.weightx = 1.0;
            gbc.insets = new Insets(3, 3, 3, 3);
            dialogPanel.add(
                    getRadioButton("Best Effort", "BestEffort", bgReliability,
                            true), gbc);
            gbc = new GridBagConstraints();
            gbc.gridwidth = 2;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(3, 3, 3, 3);
            lbl = new JLabel("Ownersip QoS");
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            dialogPanel.add(lbl, gbc);
            bgOwnership = new ButtonGroup();
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(3, 3, 3, 3);
            dialogPanel
                    .add(getRadioButton("Exclusive", "Exclusive", bgOwnership,
                            false), gbc);
            gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(3, 3, 3, 3);
            dialogPanel.add(
                    getRadioButton("Shared", "Shared", bgOwnership, true), gbc);
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 9;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(3, 3, 3, 3);
            dialogPanel.add(getButton("Cancel", "Cancel", KeyEvent.VK_C), gbc);
            gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = 9;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(3, 3, 3, 3);
            dialogPanel.add(getButton("OK", "OK", KeyEvent.VK_O), gbc);
        }
        return dialogPanel;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "OK") {
            setVisible(false);
        } else if (e.getActionCommand() == "Cancel") {
            qos = null;
            setVisible(false);
        } else if (e.getActionCommand() == "Reliable") {

        }
    }

    public DataReaderQos getQoS(DataReaderQos theQos) {
        qos = theQos;
        setVisible(true);
        return qos;
    }
}
