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
import java.io.IOException;
import java.net.URISyntaxException;

public class StravaFirstConfigurationDialog extends JDialog {

    private IdentificationStorage identificationStorage;
    private StravaConnection stravaConnection;
    private static JTextField clientId;
    private static JTextField secret;
    private static JTextField fullUrl;
    private static JTextField file;

    private static StravaFirstConfigurationDialog instance;

    public StravaFirstConfigurationDialog() {
        instance = this;
        setTitle("Initialise Strava tokens");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new MigLayout("","[][grow]", "[]20px[][][]20px[]"));
        JLabel infos = new JLabel("<html>This dialog allows you to initialise the tokens for accessing the Strava API.<br>" +
                "The file is required to store your personal client id and the tokens required for using the Strava API.</html>");
        JLabel labelClientId = new JLabel("Your Client Id");
        JLabel labelSecret = new JLabel("Your Secret");
        JLabel labelUrl = new JLabel("Url");
        JLabel labelFile = new JLabel("File");
        JLabel requestCodeLabel = new JLabel("<html>With your client Id and Secret, you can request a Strava code that will " +
                "grant access to your profile and activities.<br>" +
                "After click the following button, you need to accept the request displayed in the browser.<br>" +
                "Then copy the url from the newly displayed webpage in the Url field.</html>");

        clientId = new JTextField();
        secret = new JTextField();
        fullUrl = new JTextField();
        file = new JTextField();
        JButton requestCode = new JButton(new RequestCodeAction());
        JButton browseFile = new JButton(new BrowseFileAction());
        JButton save = new JButton(new SaveAction());
        add(infos, "span 2, wrap");
        add(labelFile);
        add(file, "grow, split 2");
        add(browseFile, "wrap");
        add(labelClientId);
        add(clientId, "grow, wrap");
        add(labelSecret);
        add(secret, "grow, wrap");
        add(requestCodeLabel, "span 2, wrap");
        add(requestCode, "span 2, center, wrap");
        add(labelUrl);
        add(fullUrl, "grow, wrap");
        add(save, "span 2, center");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public IdentificationStorage getIdentificationStorage() {
        return identificationStorage;
    }

    private static class RequestCodeAction extends AbstractAction {

        public RequestCodeAction() {
            super("Request Strava Code...");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (file.getText() == null || file.getText().isEmpty()) {
                JOptionPane.showMessageDialog(instance, "Error: The file must be filled!");
                return;
            }
            if (clientId.getText() == null || clientId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(instance, "Error: The clientId must be filled!");
                return;
            }
            try {
                Integer.parseInt(clientId.getText());
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(instance, "Error: The clientId must be numeric!");
                return;
            }
            if (secret.getText() == null || secret.getText().isEmpty()) {
                JOptionPane.showMessageDialog(instance, "Error: The secret must be filled!");
                return;
            }
            instance.identificationStorage = new FileIdentificationStorage(new File(file.getText()));
            instance.identificationStorage.setClientId(Integer.parseInt(clientId.getText()));
            instance.identificationStorage.setClientSecret(secret.getText());
            try {
                instance.stravaConnection = new StravaConnection(instance.identificationStorage);
                instance.stravaConnection.requestCode();
            } catch (IOException | URISyntaxException ex) {
                JOptionPane.showMessageDialog(instance, ex.toString());
            }
        }
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
            if (fullUrl.getText() == null || fullUrl.getText().isEmpty()) {
                JOptionPane.showMessageDialog(instance, "Error: The url must be filled!\n" +
                        "Enter the full url from the browser after accepting the request!");
                return;
            }
            try {
            instance.stravaConnection.parseUrl(fullUrl.getText());
            JOptionPane.showMessageDialog(instance, "The file with you identification has been saved.\n" +
                    "You can close this dialog.");
            } catch (RuntimeException re) {
                JOptionPane.showMessageDialog(instance, re.toString());
            }
        }
    }
}
