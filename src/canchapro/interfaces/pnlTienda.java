/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package canchapro.interfaces;
import ModuloTienda.ConfiguracionTienda;
import ModuloTienda.DetalleVenta;
import ModuloTienda.GestionProductos;
import ModuloTienda.GestionVentas;
import ModuloTienda.Producto;
import ModuloTienda.Venta;
import canchapro.codigo.GestionReservas;
import canchapro.codigo.Reserva;
import canchapro.codigo.ServicioAdicional;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Padilla
 */
public class pnlTienda extends javax.swing.JPanel {
private GestionProductos gestionProductos;
private GestionVentas gestionVentas;
private GestionReservas gestionReservas;
private Reserva[] reservasActivasCombo = new Reserva[200];
private int cantReservasCombo = 0;
private DetalleVenta[] carrito = new DetalleVenta[20];
private int cantCarrito = 0;
private ConfiguracionTienda configuracion;

    /**
     * Creates new form pnlTienda
     */
    public pnlTienda(GestionProductos gp, GestionVentas gv, GestionReservas gr, ConfiguracionTienda config) {
    initComponents();
    this.gestionProductos = gp;
    this.gestionVentas = gv;
    this.gestionReservas = gr;
    this.configuracion = config;
    
    tblProductos.setDefaultEditor(Object.class, null);
    tblCarrito.setDefaultEditor(Object.class, null);
    txtTotalVenta.setEditable(false);

    cmbReservasEnCurso.setEnabled(false);
    rbtnAnadirReserva.addActionListener(e -> {
        boolean seleccionado = rbtnAnadirReserva.isSelected();
        cmbReservasEnCurso.setEnabled(seleccionado);
        cmbMetodoVenta.setEnabled(!seleccionado);
    });

    refrescarProductos();
    refrescarCombo();
    refrescarComboReservas();
    refrescarCarrito();
    
    limpiarProducto();
    }

    //ESCOGER RESERVAS QUE NO ESTEN CANCELADAS
    private void refrescarComboReservas() {
    cmbReservasEnCurso.removeAllItems();
    cantReservasCombo = 0;
    gestionReservas.actualizarEstados();

    for (int i = 0; i < gestionReservas.getCantReservas(); i++) {
        canchapro.codigo.Reserva r = gestionReservas.getReserva()[i];
        String est = r.getEstado();
       
        if (!est.equals(canchapro.codigo.Reserva.CANCELADA) 
                && !est.equals(canchapro.codigo.Reserva.FINALIZADA)) {
            if (cantReservasCombo < reservasActivasCombo.length) {
                reservasActivasCombo[cantReservasCombo] = r;
                cantReservasCombo++;
                cmbReservasEnCurso.addItem(r.getCodigo() + " - " + r.getCliente().getNombre() + " (" + r.getCancha().getNombre() + ")");
            }
        }
    }
}
    
    
    
    
    
    //PRODUCTOS
    private void refrescarProductos() {
    DefaultTableModel modelo = (DefaultTableModel) tblProductos.getModel();
    modelo.setRowCount(0);
    for (int i = 0; i < gestionProductos.getCantProductos(); i++) {
        Producto p = gestionProductos.getProducto()[i];
        String alerta = "";
        if (p.tieneStockBajo()) {
            alerta = "STOCK BAJO";
        }
        modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getCategoria(), p.getPrecio(), p.getExistencia(), p.getStockMinimo(), p.getEstado(), alerta});
    }
}

private void refrescarCombo() {
    cmbProductoVenta.removeAllItems();
    for (int i = 0; i < gestionProductos.getCantProductos(); i++) {
        Producto p = gestionProductos.getProducto()[i];
        cmbProductoVenta.addItem(p.getNombre() + " - Q" + p.getPrecio() + " (existencia: " + p.getExistencia() + ")");
    }
}

private void limpiarProducto() {
    txtCodigo.setText(gestionProductos.generarCodigo());

    txtNombre.setText("");
    txtPrecio.setText("");
    txtExistencia.setText("");
    txtStockMinimo.setText("");
    cmbCategoria.setSelectedIndex(0);
    cmbEstadoProducto.setSelectedIndex(0);
}

private Producto leerProducto() {
    String codigo = txtCodigo.getText().trim();
    String nombre = txtNombre.getText().trim();

    if (codigo.isEmpty() || nombre.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Complete código y nombre");
        return null;
    }
    if (codigo.contains(",") || nombre.contains(",")) {
        JOptionPane.showMessageDialog(this, "No use comas en los campos");
        return null;
    }
    try {
        double precio = Double.parseDouble(txtPrecio.getText().trim());
        int existencia = Integer.parseInt(txtExistencia.getText().trim());
        int minimo = Integer.parseInt(txtStockMinimo.getText().trim());
        return new Producto(codigo, nombre, cmbCategoria.getSelectedItem().toString(), precio, existencia, minimo, cmbEstadoProducto.getSelectedItem().toString());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Precio, existencia y stock mínimo deben ser números");
        return null;
    }
}


private void tblProductosMouseClicked(java.awt.event.MouseEvent evt) {
    int fila = tblProductos.getSelectedRow();
    if (fila >= 0) {
        Producto p = gestionProductos.buscarPorCodigo(tblProductos.getValueAt(fila, 0).toString());
        if (p != null) {
            txtCodigo.setText(p.getCodigo());
            txtNombre.setText(p.getNombre());
            txtPrecio.setText(String.valueOf(p.getPrecio()));
            txtExistencia.setText(String.valueOf(p.getExistencia()));
            txtStockMinimo.setText(String.valueOf(p.getStockMinimo()));
            cmbCategoria.setSelectedItem(p.getCategoria());
            cmbEstadoProducto.setSelectedItem(p.getEstado());
        }
    }
}


    
    
    // VENTA

private void refrescarCarrito() {
    DefaultTableModel modelo = (DefaultTableModel) tblCarrito.getModel();
    modelo.setRowCount(0);
    double total = 0;
    for (int i = 0; i < cantCarrito; i++) {
        DetalleVenta d = carrito[i];
        modelo.addRow(new Object[]{d.getProducto().getNombre(), d.getCantidad(), d.getPrecioUnitario(), d.calcularSubtotal()});
        total = total + d.calcularSubtotal();
    }
    txtTotalVenta.setText(String.format("%.2f", total));
}






    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Venta = new javax.swing.JTabbedPane();
        pnlVentas = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        cmbProductoVenta = new javax.swing.JComboBox<>();
        spnCantidad = new javax.swing.JSpinner();
        btnQuitar = new javax.swing.JButton();
        btnConfirmarVenta = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCarrito = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        cmbMetodoVenta = new javax.swing.JComboBox<>();
        lblTotalVendido = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtTotalVenta = new javax.swing.JTextField();
        btnAnadir = new javax.swing.JButton();
        cmbReservasEnCurso = new javax.swing.JComboBox<>();
        rbtnAnadirReserva = new javax.swing.JRadioButton();
        pnlProductos = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtStockMinimo = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        txtExistencia = new javax.swing.JTextField();
        cmbCategoria = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbEstadoProducto = new javax.swing.JComboBox<>();
        btnLimpiarProducto = new javax.swing.JButton();
        btnAgregarProducto = new javax.swing.JButton();
        btnActualizarProducto = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        lblDatosHorario1 = new javax.swing.JLabel();
        lblReportesTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 204));
        setMaximumSize(new java.awt.Dimension(832, 652));
        setMinimumSize(new java.awt.Dimension(832, 652));
        setPreferredSize(new java.awt.Dimension(832, 652));
        setLayout(null);

        Venta.setBackground(new java.awt.Color(255, 255, 255));
        Venta.setForeground(new java.awt.Color(0, 0, 0));
        Venta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        pnlVentas.setBackground(new java.awt.Color(255, 255, 204));
        pnlVentas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        pnlVentas.setLayout(null);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Producto:");
        pnlVentas.add(jLabel12);
        jLabel12.setBounds(10, 20, 60, 16);

        cmbProductoVenta.setBackground(new java.awt.Color(255, 255, 255));
        cmbProductoVenta.setForeground(new java.awt.Color(0, 0, 0));
        cmbProductoVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlVentas.add(cmbProductoVenta);
        cmbProductoVenta.setBounds(80, 20, 250, 22);

        spnCantidad.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));
        pnlVentas.add(spnCantidad);
        spnCantidad.setBounds(420, 20, 67, 22);

        btnQuitar.setBackground(new java.awt.Color(204, 0, 0));
        btnQuitar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuitar.setForeground(new java.awt.Color(255, 255, 255));
        btnQuitar.setText("Quitar");
        btnQuitar.addActionListener(this::btnQuitarActionPerformed);
        pnlVentas.add(btnQuitar);
        btnQuitar.setBounds(550, 20, 90, 27);

        btnConfirmarVenta.setBackground(new java.awt.Color(0, 102, 0));
        btnConfirmarVenta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnConfirmarVenta.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmarVenta.setText("Confirmar Venta");
        btnConfirmarVenta.addActionListener(this::btnConfirmarVentaActionPerformed);
        pnlVentas.add(btnConfirmarVenta);
        btnConfirmarVenta.setBounds(580, 450, 170, 27);

        tblCarrito.setBackground(new java.awt.Color(255, 255, 255));
        tblCarrito.setForeground(new java.awt.Color(0, 0, 0));
        tblCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Producto", "Cantidad", "Precio Unitario", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblCarrito);

        pnlVentas.add(jScrollPane2);
        jScrollPane2.setBounds(30, 70, 720, 310);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Cantidad:");
        pnlVentas.add(jLabel15);
        jLabel15.setBounds(350, 20, 60, 16);

        cmbMetodoVenta.setBackground(new java.awt.Color(255, 255, 255));
        cmbMetodoVenta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbMetodoVenta.setForeground(new java.awt.Color(0, 0, 0));
        cmbMetodoVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Efectivo", "Transferencia", "Tarjeta" }));
        pnlVentas.add(cmbMetodoVenta);
        cmbMetodoVenta.setBounds(430, 450, 110, 22);

        lblTotalVendido.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTotalVendido.setForeground(new java.awt.Color(0, 0, 0));
        lblTotalVendido.setText("Total: ");
        pnlVentas.add(lblTotalVendido);
        lblTotalVendido.setBounds(30, 440, 110, 40);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("Método de Pago:");
        pnlVentas.add(jLabel17);
        jLabel17.setBounds(300, 450, 130, 20);

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("Reserva:");
        pnlVentas.add(jLabel18);
        jLabel18.setBounds(180, 400, 80, 20);

        txtTotalVenta.setEditable(false);
        txtTotalVenta.setBackground(new java.awt.Color(255, 255, 255));
        txtTotalVenta.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtTotalVenta.setForeground(new java.awt.Color(0, 0, 0));
        pnlVentas.add(txtTotalVenta);
        txtTotalVenta.setBounds(110, 440, 170, 31);

        btnAnadir.setBackground(new java.awt.Color(0, 102, 0));
        btnAnadir.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAnadir.setForeground(new java.awt.Color(255, 255, 255));
        btnAnadir.setText("Añadir");
        btnAnadir.addActionListener(this::btnAnadirActionPerformed);
        pnlVentas.add(btnAnadir);
        btnAnadir.setBounds(670, 20, 90, 27);

        cmbReservasEnCurso.setBackground(new java.awt.Color(255, 255, 255));
        cmbReservasEnCurso.setForeground(new java.awt.Color(0, 0, 0));
        cmbReservasEnCurso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlVentas.add(cmbReservasEnCurso);
        cmbReservasEnCurso.setBounds(260, 400, 280, 22);

        rbtnAnadirReserva.setForeground(new java.awt.Color(0, 0, 0));
        rbtnAnadirReserva.setText("Añadir a Reserva");
        pnlVentas.add(rbtnAnadirReserva);
        rbtnAnadirReserva.setBounds(40, 400, 130, 21);

        Venta.addTab("Venta", pnlVentas);

        pnlProductos.setBackground(new java.awt.Color(204, 204, 255));
        pnlProductos.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Estado:");
        pnlProductos.add(jLabel2);
        jLabel2.setBounds(250, 90, 60, 16);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Nombre:");
        pnlProductos.add(jLabel3);
        jLabel3.setBounds(160, 50, 60, 16);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Precio:");
        pnlProductos.add(jLabel4);
        jLabel4.setBounds(460, 50, 60, 16);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Existencia:");
        pnlProductos.add(jLabel5);
        jLabel5.setBounds(610, 50, 60, 16);

        txtNombre.setBackground(new java.awt.Color(255, 255, 255));
        txtNombre.setForeground(new java.awt.Color(0, 0, 0));
        pnlProductos.add(txtNombre);
        txtNombre.setBounds(220, 50, 220, 22);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Stock Minimo:");
        pnlProductos.add(jLabel6);
        jLabel6.setBounds(450, 100, 80, 16);

        txtCodigo.setBackground(new java.awt.Color(255, 255, 255));
        txtCodigo.setForeground(new java.awt.Color(0, 0, 0));
        pnlProductos.add(txtCodigo);
        txtCodigo.setBounds(60, 50, 80, 22);

        txtStockMinimo.setBackground(new java.awt.Color(255, 255, 255));
        txtStockMinimo.setForeground(new java.awt.Color(0, 0, 0));
        pnlProductos.add(txtStockMinimo);
        txtStockMinimo.setBounds(540, 90, 71, 22);

        txtPrecio.setBackground(new java.awt.Color(255, 255, 255));
        txtPrecio.setForeground(new java.awt.Color(0, 0, 0));
        pnlProductos.add(txtPrecio);
        txtPrecio.setBounds(510, 50, 90, 22);

        txtExistencia.setBackground(new java.awt.Color(255, 255, 255));
        txtExistencia.setForeground(new java.awt.Color(0, 0, 0));
        pnlProductos.add(txtExistencia);
        txtExistencia.setBounds(680, 50, 71, 22);

        cmbCategoria.setBackground(new java.awt.Color(255, 255, 255));
        cmbCategoria.setForeground(new java.awt.Color(0, 0, 0));
        cmbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bebidas", "Snacks", "Golosinas", "Alimentos", "Otros" }));
        pnlProductos.add(cmbCategoria);
        cmbCategoria.setBounds(80, 90, 130, 22);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Codigo:");
        pnlProductos.add(jLabel7);
        jLabel7.setBounds(10, 50, 60, 16);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Categoría:");
        pnlProductos.add(jLabel8);
        jLabel8.setBounds(10, 90, 60, 16);

        cmbEstadoProducto.setBackground(new java.awt.Color(255, 255, 255));
        cmbEstadoProducto.setForeground(new java.awt.Color(0, 0, 0));
        cmbEstadoProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "inactivo", "activo" }));
        pnlProductos.add(cmbEstadoProducto);
        cmbEstadoProducto.setBounds(310, 90, 110, 22);

        btnLimpiarProducto.setBackground(new java.awt.Color(153, 0, 153));
        btnLimpiarProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLimpiarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarProducto.setText("Limpiar");
        btnLimpiarProducto.addActionListener(this::btnLimpiarProductoActionPerformed);
        pnlProductos.add(btnLimpiarProducto);
        btnLimpiarProducto.setBounds(380, 140, 100, 30);

        btnAgregarProducto.setBackground(new java.awt.Color(0, 102, 0));
        btnAgregarProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAgregarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarProducto.setText("Agregar");
        btnAgregarProducto.addActionListener(this::btnAgregarProductoActionPerformed);
        pnlProductos.add(btnAgregarProducto);
        btnAgregarProducto.setBounds(650, 140, 100, 30);

        btnActualizarProducto.setBackground(new java.awt.Color(255, 153, 51));
        btnActualizarProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnActualizarProducto.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarProducto.setText("Actualizar");
        btnActualizarProducto.addActionListener(this::btnActualizarProductoActionPerformed);
        pnlProductos.add(btnActualizarProducto);
        btnActualizarProducto.setBounds(510, 140, 100, 30);

        tblProductos.setBackground(new java.awt.Color(255, 255, 255));
        tblProductos.setForeground(new java.awt.Color(0, 0, 0));
        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Categoría", "Precio", "Existencias", "Stock Minimo", "Estado", "Alerta"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblProductos);

        pnlProductos.add(jScrollPane1);
        jScrollPane1.setBounds(20, 190, 740, 290);

        lblDatosHorario1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        lblDatosHorario1.setForeground(new java.awt.Color(51, 51, 51));
        lblDatosHorario1.setText("Seleccione una fila para editar los datos del producto. O Ingrese los dados para agregarlo al inventario.");
        pnlProductos.add(lblDatosHorario1);
        lblDatosHorario1.setBounds(80, 10, 620, 20);

        Venta.addTab("Productos", pnlProductos);

        add(Venta);
        Venta.setBounds(20, 90, 790, 540);

        lblReportesTitulo.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 48)); // NOI18N
        lblReportesTitulo.setForeground(new java.awt.Color(0, 51, 0));
        lblReportesTitulo.setText("Tienda");
        add(lblReportesTitulo);
        lblReportesTitulo.setBounds(20, 20, 170, 62);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconoTienda.png"))); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(720, 10, 100, 100);

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Perpetua Titling MT", 1, 38)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("10");
        add(jLabel9);
        jLabel9.setBounds(680, 30, 60, 70);

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Perpetua Titling MT", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Shop");
        add(jLabel10);
        jLabel10.setBounds(640, 60, 80, 60);

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Perpetua Titling MT", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("La");
        add(jLabel11);
        jLabel11.setBounds(640, 40, 50, 50);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
    Producto p = leerProducto();
    if (p != null) {
        String mensaje = gestionProductos.agregarProducto(p);
        JOptionPane.showMessageDialog(this, mensaje);
        if (mensaje.equals("Producto agregado correctamente")) {
            limpiarProducto();
            refrescarProductos();
            refrescarCombo();
        }
    }
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnActualizarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarProductoActionPerformed
           Producto nuevo = leerProducto();
    if (nuevo != null) {
        Producto actual = gestionProductos.buscarPorCodigo(nuevo.getCodigo());
        String validacion = gestionProductos.validar(nuevo);
        if (actual == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para actualizarlo");
        } else if (!validacion.equals("OK")) {
            JOptionPane.showMessageDialog(this, validacion);
        } else {
            actual.setNombre(nuevo.getNombre());
            actual.setCategoria(nuevo.getCategoria());
            actual.setPrecio(nuevo.getPrecio());
            actual.setExistencia(nuevo.getExistencia());
            actual.setStockMinimo(nuevo.getStockMinimo());
            actual.setEstado(nuevo.getEstado());
            JOptionPane.showMessageDialog(this, "Producto actualizado correctamente");
            limpiarProducto();
            refrescarProductos();
            refrescarCombo();
        }
    }
    }//GEN-LAST:event_btnActualizarProductoActionPerformed

    private void btnLimpiarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarProductoActionPerformed
          limpiarProducto();
    }//GEN-LAST:event_btnLimpiarProductoActionPerformed

    private void btnAnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnadirActionPerformed
           int indice = cmbProductoVenta.getSelectedIndex();
    int cantidad = (Integer) spnCantidad.getValue();

    if (indice < 0) {
        JOptionPane.showMessageDialog(this, "No hay productos registrados");
    } else {
        Producto p = gestionProductos.getProducto()[indice];
        int posicion = -1;
        for (int i = 0; i < cantCarrito; i++) {
            if (carrito[i].getProducto() == p) {
                posicion = i;
            }
        }

        if (posicion >= 0) {
            carrito[posicion] = new DetalleVenta(p, carrito[posicion].getCantidad() + cantidad, p.getPrecio());
        } else if (cantCarrito >= carrito.length) {
            JOptionPane.showMessageDialog(this, "El carrito está lleno");
        } else {
            carrito[cantCarrito] = new DetalleVenta(p, cantidad, p.getPrecio());
            cantCarrito++;
        }
        refrescarCarrito();
    }
    }//GEN-LAST:event_btnAnadirActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        int fila = tblCarrito.getSelectedRow();
    if (fila < 0 || fila >= cantCarrito) {
        JOptionPane.showMessageDialog(this, "Seleccione en el carrito el producto a quitar");
    } else {
        for (int i = fila; i < cantCarrito - 1; i++) {
            carrito[i] = carrito[i + 1];
        }
        carrito[cantCarrito - 1] = null;
        cantCarrito--;
        refrescarCarrito();
    }
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void btnConfirmarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarVentaActionPerformed
if (cantCarrito == 0) {
        JOptionPane.showMessageDialog(this, "Agregue productos al carrito");
        return;
    }

    boolean anadirAReserva = rbtnAnadirReserva.isSelected();
    canchapro.codigo.Reserva reservaDestino = null;
    String codigoReserva = "";

    if (anadirAReserva) {
        int idxReserva = cmbReservasEnCurso.getSelectedIndex();
        if (idxReserva < 0 || idxReserva >= cantReservasCombo) {
            JOptionPane.showMessageDialog(this, "No hay reservas activas seleccionadas para cargar el consumo");
            return;
        }
        reservaDestino = reservasActivasCombo[idxReserva];
        codigoReserva = reservaDestino.getCodigo();
    }

    String metodo = anadirAReserva 
            ? "cargo_reserva" 
            : cmbMetodoVenta.getSelectedItem().toString().toLowerCase();

    Venta v = new Venta(gestionVentas.generarNumero(), LocalDateTime.now(), codigoReserva, metodo);
    for (int i = 0; i < cantCarrito; i++) {
        v.agregarDetalle(carrito[i]);
    }

    String mensaje = gestionVentas.registrarVenta(v);
    if (mensaje.equals("Venta registrada correctamente")) {
        double totalVenta = v.calcularTotal();

        // Si se marcó "Añadir a Reserva", sumarlo al saldo pendiente de la reserva
        if (anadirAReserva && reservaDestino != null) {
            canchapro.codigo.ServicioAdicional cargoTienda = new canchapro.codigo.ServicioAdicional(
                    v.getNumero(),
                    "Consumo Tienda " + v.getNumero(),
                    totalVenta
            );
            boolean agregado = reservaDestino.agregarServicio(cargoTienda);
            if (!agregado) {
                JOptionPane.showMessageDialog(this, "Venta registrada, pero la reserva alcanzó el límite de servicios adicionales.");
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Venta registrada y cargada a la reserva " + codigoReserva 
                        + "\nMonto añadido al saldo pendiente: Q" + String.format("%.2f", totalVenta));
            }
        } else {
            JOptionPane.showMessageDialog(this, mensaje + "\nTotal: Q" + String.format("%.2f", totalVenta));
        }

        // Limpiar carrito y controles
        for (int i = 0; i < carrito.length; i++) {
            carrito[i] = null;
        }
        cantCarrito = 0;
        rbtnAnadirReserva.setSelected(false);
        cmbReservasEnCurso.setEnabled(false);
        cmbMetodoVenta.setEnabled(true);

        refrescarProductos();
        refrescarCombo();
        refrescarComboReservas();
        refrescarCarrito();
        
        
           //Guardar info para el desglose
    StringBuilder desc = new StringBuilder("Tienda (" + v.getNumero() + "): ");
for (int i = 0; i < v.getCantidadDetalles(); i++) {
    DetalleVenta d = v.getDetalles()[i];
    desc.append(d.getCantidad()).append("x ").append(d.getProducto().getNombre());
    if (i < v.getCantidadDetalles() - 1) {
        desc.append(", ");
    }
}

ServicioAdicional cargoTienda = new ServicioAdicional(
        v.getNumero(),
        desc.toString(),
        totalVenta
);
reservaDestino.agregarServicio(cargoTienda);
      
    } else {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    
 
    }//GEN-LAST:event_btnConfirmarVentaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Venta;
    private javax.swing.JButton btnActualizarProducto;
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnAnadir;
    private javax.swing.JButton btnConfirmarVenta;
    private javax.swing.JButton btnLimpiarProducto;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JComboBox<String> cmbEstadoProducto;
    private javax.swing.JComboBox<String> cmbMetodoVenta;
    private javax.swing.JComboBox<String> cmbProductoVenta;
    private javax.swing.JComboBox<String> cmbReservasEnCurso;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDatosHorario1;
    private javax.swing.JLabel lblReportesTitulo;
    private javax.swing.JLabel lblTotalVendido;
    private javax.swing.JPanel pnlProductos;
    private javax.swing.JPanel pnlVentas;
    private javax.swing.JRadioButton rbtnAnadirReserva;
    private javax.swing.JSpinner spnCantidad;
    private javax.swing.JTable tblCarrito;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtExistencia;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtStockMinimo;
    private javax.swing.JTextField txtTotalVenta;
    // End of variables declaration//GEN-END:variables
}
