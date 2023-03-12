package org.jstrava;

import net.miginfocom.swing.MigLayout;
import org.jstrava.user.FileIdentificationStorage;
import org.jstrava.user.IdentificationStorage;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.io.File;

public class StravaConfigurationDialog extends JDialog {

    private IdentificationStorage identificationStorage;
    private static JTextField clientId;
    private static JTextField accessToken;
    private static JTextField refreshToken;
    private static JTextField file;

    private static StravaConfigurationDialog instance;

    public StravaConfigurationDialog() {
        instance = this;
        setTitle("Set up Strava tokens");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new MigLayout("","[][grow]", "[]20px[][][][]"));
        JLabel infos = new JLabel("<html>This dialog allows you to setup the tokens for accessing the Strava API.<br>" +
                "The file is required to store your personal client id and the tokens required for using the Strava API.</html>");
        JLabel labelClientId = new JLabel("Your Client Id");
        JLabel labelAccessToken = new JLabel("Your Access token");
        JLabel labelRefreshToken = new JLabel("Your Refresh token");
        JLabel labelFile = new JLabel("File");
        clientId = new JTextField();
        accessToken = new JTextField();
        refreshToken = new JTextField();
        file = new JTextField();
        JButton browseFile = new JButton(new BrowseFileAction());
        JButton save = new JButton(new SaveAction());
        add(infos, "span 2, wrap");
        add(labelFile);
        add(file, "grow, split 2");
        add(browseFile, "wrap");
        add(labelClientId);
        add(clientId, "grow, wrap");
        add(labelAccessToken);
        add(accessToken, "grow, wrap");
        add(labelRefreshToken);
        add(refreshToken, "grow, wrap");
        add(save, "span 2, center");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public IdentificationStorage getIdentificationStorage() {
        return identificationStorage;
    }

    private static class BrowseFileAction extends AbstractAction {

        public BrowseFileAction() {
            super("...");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(instance)) {
                file.setText(fileChooser.getSelectedFile().toString());
            }
        }
    }

    private static class SaveAction extends AbstractAction {

        public SaveAction() {
            super("Validate and Save data");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (file.getText() == null || file.getText().isEmpty()) {
                JOptionPane.showMessageDialog(instance, "Error: The file must be filled!");
                return;
            }
            if (clientId.getText() == null || clientId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(instance, "Error: The client Id must be filled!");
                return;
            }
            try {
                Integer.parseInt(clientId.getText());
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(instance, "Error: The client Id must be numeric!");
                return;
            }
            if (accessToken.getText() == null || accessToken.getText().isEmpty()) {
                JOptionPane.showMessageDialog(instance, "Error: The access token must be filled!");
                return;
            }
            if (refreshToken.getText() == null || refreshToken.getText().isEmpty()) {
                JOptionPane.showMessageDialog(instance, "Error: The refresh token must be filled!");
                return;
            }
            instance.identificationStorage = new FileIdentificationStorage(new File(file.getText()));
            instance.identificationStorage.setClientId(Integer.parseInt(clientId.getText()));
            instance.identificationStorage.setAccessToken(accessToken.getText());
            instance.identificationStorage.setRefreshToken(refreshToken.getText());
            instance.identificationStorage.setClientSecret("XXX");
            instance.identificationStorage.save();
        }
    }
}
