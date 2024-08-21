package Servidor;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class IntServidor extends javax.swing.JFrame {

    private List<Socket> clientSockets;
    private final int PORT = 5000;

    public IntServidor() {
        initComponents();

        iniciarServidor.addActionListener(e -> iniciarServidor());
        setTitle("Servidor TCP");
        setLocationRelativeTo(null);
        jTextAreaMs.setEditable(false);
        clientSockets = new ArrayList<>();
    }

    private void iniciarServidor() {
        new Thread(() -> {
            try (ServerSocket servidorSocket = new ServerSocket(PORT)) {
                jTextAreaMs.append("Servidor esperando conexiones en el puerto " + PORT + "\n");

                while (true) {
                    try {
                        Socket socket = servidorSocket.accept();
                        clientSockets.add(socket);
                        jTextAreaMs.append("Cliente conectado: " + socket.getInetAddress().getHostName() + "\n");
                        new ClientHandler(socket).start();
                    } catch (IOException e) {
                        jTextAreaMs.append("Error al aceptar la conexión: " + e.getMessage() + "\n");
                    }
                }
            } catch (IOException e) {
                jTextAreaMs.append("Error al iniciar el servidor: " + e.getMessage() + "\n");
            }
        }).start();
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private DataInputStream dis;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                dis = new DataInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    receiveFile();
                }
            } catch (IOException e) {
                jTextAreaMs.append("Error al procesar datos del cliente: " + e.getMessage() + "\n");
            } finally {
                try {
                    clientSocket.close();
                    clientSockets.remove(clientSocket);
                } catch (IOException e) {
                    jTextAreaMs.append("Error al cerrar la conexión con el cliente: " + e.getMessage() + "\n");
                }
            }
        }

        private void receiveFile() throws IOException {
            String fileName = dis.readUTF();
            long fileSize = dis.readLong();
            File file = new File("C:\\Users\\RC\\Documents\\" + fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                long remaining = fileSize;
                while (remaining > 0 && (bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, remaining))) > 0) {
                    fos.write(buffer, 0, bytesRead);
                    remaining -= bytesRead;
                }
            }
            jTextAreaMs.append("Archivo " + fileName + " recibido y guardado en " + file.getAbsolutePath() + "\n");
            broadcastFile(file);
        }

        private void broadcastFile(File file) throws IOException {
            synchronized (clientSockets) {
                for (Socket socket : clientSockets) {
                    if (socket != clientSocket) {
                        sendFile(socket, file);
                    }
                }
            }
        }

        private void sendFile(Socket socket, File file) throws IOException {
            try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                 FileInputStream fis = new FileInputStream(file)) {
                dos.writeUTF(file.getName());
                dos.writeLong(file.length());
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }
            }
        }
    }


        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        iniciarServidor = new javax.swing.JButton();
        titulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaMs = new javax.swing.JTextArea();
        jLabelImagen = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        iniciarServidor.setText("INICIAR SERVIDOR");
        iniciarServidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniciarServidorActionPerformed(evt);
            }
        });

        titulo.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        titulo.setForeground(new java.awt.Color(0, 0, 255));
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("SERVIDOR PRINCIPAL");

        jTextAreaMs.setColumns(20);
        jTextAreaMs.setRows(5);
        jScrollPane1.setViewportView(jTextAreaMs);

        jLabelImagen.setIcon(new javax.swing.ImageIcon("C:\\Users\\RC\\Downloads\\Imagenes netbeans\\5960320.png")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(titulo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                        .addComponent(iniciarServidor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabelImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(iniciarServidor, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 172, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void iniciarServidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciarServidorActionPerformed

    }//GEN-LAST:event_iniciarServidorActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IntServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IntServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IntServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IntServidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IntServidor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton iniciarServidor;
    private javax.swing.JLabel jLabelImagen;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaMs;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
//JButton iniciar servidor = Boton para iniciar el servidor
//JTextArea jTextAreaMs = Area de texto para mostrar los mensajes del servidor
}

