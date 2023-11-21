package es.deusto.ingenieria.sd.rest.client;

import javax.swing.SwingUtilities;

import es.deusto.ingenieria.sd.rest.client.controller.ClientController;
import es.deusto.ingenieria.sd.rest.client.gui.ClientGUI;

public class BasicRestClient {	

    public static void main(String[] args) {
        try {
        	SwingUtilities.invokeLater(() -> new ClientGUI(new ClientController(args[0])));
        } catch (Exception e) {
            System.out.format("Error: %s", e.getMessage());
        }
    }
}