import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class ConnectDialog extends JDialog {
    private GuiDisplayer parent;
    private JTextField fldPortName = new JTextField(10);
    private JLabel lblPornName = new JLabel("Nazwa portu");
    private JLabel lblBoundRate = new JLabel("Pr�dko�� transmisji");

    private JButton connectBtn = createConnectBtn();
    private Container contentPane;
    private SpringLayout layout;

    public ConnectDialog(JFrame frame) {
        super(frame);
        parent = (GuiDisplayer) frame;
        setLocationRelativeTo(null);
        setLocation(100, 100);
        setSize(250, 250);

        prepareGui();
        setVisible(true);
    }

    private void prepareGui() {
        contentPane = getContentPane();
        layout = new SpringLayout();
        setLayoutConstraints();

        contentPane.setLayout(layout);
        contentPane.setSize(100, 100);
        contentPane.add(lblPornName);

        contentPane.add(fldPortName);
        contentPane.add(connectBtn);

    }

    private void setLayoutConstraints() {
        layout.putConstraint(SpringLayout.WEST, lblPornName, 5, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, lblPornName, 5, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.EAST, fldPortName, 5, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, fldPortName, 5, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, connectBtn, 80, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, connectBtn, 35, SpringLayout.NORTH, contentPane);

    }

    private JButton createConnectBtn() {
        JButton button = new JButton("Po��cz");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                parent.getService().connect(fldPortName.getText());
            }
        });
        return button;
    }

}
