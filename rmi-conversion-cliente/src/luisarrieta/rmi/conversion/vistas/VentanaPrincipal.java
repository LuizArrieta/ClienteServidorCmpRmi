package luisarrieta.rmi.conversion.vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import luisarrieta.rmi.conversion.lib.DatosCmp;
import luisarrieta.rmi.conversion.lib.IConversionRemota;

/** @author LUIS CAMILO ARRIETA MURIEL */
public class VentanaPrincipal extends JFrame {

    private static final String NOMBRE_SERVICIO = "ConversionCmp";

    private Registry registro;
    private IConversionRemota conversionRemota;

    private final JTextField campoIPServidor = new JTextField("127.0.0.1");
    private final JTextField campoPuertoServidor = new JTextField("9008");
    private final JButton btnIniciar = new JButton("Conectar");
    private final JLabel txtEstado = new JLabel("\u25CF DESCONECTADO");

    private final JTextField campoMetros = new JTextField();
    private final JButton btnConvertir = new JButton("Convertir a pies");
    private final JLabel txtResultado = new JLabel("--");
    private final JLabel txtMensaje = new JLabel(" ");

    private final JTextArea areaLog = new JTextArea();
    private final JButton btnLimpiarLog = new JButton("Limpiar log");
    private final SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

    public VentanaPrincipal() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Cliente RMI - Conversion de Metros a Pies");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 720);
        setLocationRelativeTo(null);

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(new EmptyBorder(12, 12, 12, 12));

        panelContenido.add(crearPanelConexion());
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(crearPanelConversion());
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(crearPanelLog());

        setLayout(new BorderLayout());
        add(panelContenido, BorderLayout.CENTER);
    }

    private JPanel crearPanelConexion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Conexion con el servidor"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        panel.add(new JLabel("Host del servidor:"), c);
        c.gridx = 1; c.weightx = 1;
        panel.add(campoIPServidor, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        panel.add(new JLabel("Puerto del servidor:"), c);
        c.gridx = 1; c.weightx = 1;
        panel.add(campoPuertoServidor, c);

        txtEstado.setForeground(Color.RED);
        txtEstado.setFont(txtEstado.getFont().deriveFont(Font.BOLD));
        c.gridx = 0; c.gridy = 2; c.weightx = 0;
        panel.add(txtEstado, c);
        btnIniciar.addActionListener(this::btnIniciarActionPerformed);
        c.gridx = 1; c.weightx = 1;
        panel.add(btnIniciar, c);

        return panel;
    }

    private JPanel crearPanelConversion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Datos de la conversion"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        panel.add(new JLabel("Longitud en metros:"), c);
        c.gridx = 1; c.weightx = 1;
        panel.add(campoMetros, c);

        JLabel lblPies = new JLabel("Pies:");
        lblPies.setFont(lblPies.getFont().deriveFont(Font.BOLD));
        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        panel.add(lblPies, c);
        txtResultado.setForeground(new Color(0, 128, 0));
        txtResultado.setFont(txtResultado.getFont().deriveFont(Font.BOLD, 14f));
        c.gridx = 1; c.weightx = 1;
        panel.add(txtResultado, c);

        btnConvertir.addActionListener(this::btnConvertirActionPerformed);
        c.gridx = 1; c.gridy = 2; c.weightx = 1;
        panel.add(btnConvertir, c);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 2; c.weightx = 1;
        panel.add(txtMensaje, c);

        return panel;
    }

    private JPanel crearPanelLog() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(new TitledBorder("Log"));

        areaLog.setEditable(false);
        areaLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setPreferredSize(new java.awt.Dimension(600, 260));

        btnLimpiarLog.addActionListener(e -> areaLog.setText(""));
        JPanel panelBoton = new JPanel(new BorderLayout());
        panelBoton.add(btnLimpiarLog, BorderLayout.CENTER);

        panel.add(panelBoton, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void agregarLog(String mensaje) {
        String hora = formatoHora.format(new Date());
        SwingUtilities.invokeLater(() -> {
            areaLog.append("[" + hora + "] " + mensaje + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (btnIniciar.getText().equalsIgnoreCase("Conectar")) {
                int puerto = Integer.parseInt(campoPuertoServidor.getText());
                String ipServidor = campoIPServidor.getText();

                registro = LocateRegistry.getRegistry(ipServidor, puerto);
                conversionRemota = (IConversionRemota) registro.lookup(NOMBRE_SERVICIO);

                btnIniciar.setText("Desconectar");
                txtEstado.setText("\u25CF CONECTADO");
                txtEstado.setForeground(new Color(0, 128, 0));
                agregarLog("Conectado al servidor " + ipServidor + ":" + puerto);
            } else if (btnIniciar.getText().equalsIgnoreCase("Desconectar")) {
                conversionRemota = null;
                registro = null;
                btnIniciar.setText("Conectar");
                txtEstado.setText("\u25CF DESCONECTADO");
                txtEstado.setForeground(Color.RED);
                agregarLog("Desconectado del servidor");
            }
        } catch (Exception ex) {
            agregarLog("ERROR AL CONECTAR: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR AL CONECTAR: " + ex.getMessage());
        }
    }

    private void btnConvertirActionPerformed(java.awt.event.ActionEvent evt) {
        if (conversionRemota == null) {
            JOptionPane.showMessageDialog(this, "Primero debe conectarse al servidor.");
            return;
        }
        float metros = Float.parseFloat(campoMetros.getText());
        agregarLog("Enviando " + metros + " metros al servidor...");
        Thread hilo = new Thread() {
            @Override
            public void run() {
                try {
                    DatosCmp datos = new DatosCmp();
                    datos.setMetros(metros);
                    datos = conversionRemota.convertir(datos);
                    agregarLog("Respuesta recibida: " + datos.getPies() + " pies");
                    txtResultado.setText(datos.getPies() + "");
                    txtMensaje.setText(datos.getInterpretacion());
                } catch (RemoteException ex) {
                    agregarLog("ERROR con el servidor: " + ex.getMessage());
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "ERROR con el servidor " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        };
        hilo.start();
    }
}
