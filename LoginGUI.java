import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.*;
/**
 * Clase LoginGUI que representa una interfaz gráfica de usuario (GUI) para iniciar sesión o registrar nuevos usuarios.
 * Extiende JFrame y permite a los usuarios ingresar credenciales para acceder a la tienda o registrarse.
 */
public class LoginGUI extends JFrame {
    /** Campo de texto para ingresar el nombre de usuario. */
    private JTextField campoUsuario;

    /** Campo de contraseña para ingresar la contraseña del usuario. */
    private JPasswordField campoContraseña;

    /**Botón para inciair sesión. */
    /** Botón para registrar un nuevo usuario. */
    private JButton botonIniciarSesion, botonRegistrar;

    /** HashMap que almacena los usuarios registrados con su nombre de usuario y contraseña. */
    private HashMap<String, String> usuariosRegistrados;

    /**
     * Constructor de LoginGUI que inicializa la interfaz gráfica y carga los usuarios desde el archivo.
     */
    public LoginGUI() {
        usuariosRegistrados = cargarUsuarios();

        setTitle("Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new GridLayout(3, 2));

        panelCentro.add(new JLabel("Usuario:"));
        campoUsuario = new JTextField();
        panelCentro.add(campoUsuario);

        panelCentro.add(new JLabel("Contraseña:"));
        campoContraseña = new JPasswordField();
        panelCentro.add(campoContraseña);

        add(panelCentro, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        botonIniciarSesion = new JButton("Iniciar Sesión");
        botonRegistrar = new JButton("Registrar Usuario");

        panelInferior.add(botonIniciarSesion);
        panelInferior.add(botonRegistrar);

        add(panelInferior, BorderLayout.SOUTH);

        botonIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = campoUsuario.getText();
                String contraseña = new String(campoContraseña.getPassword());

                if (usuariosRegistrados.containsKey(usuario) && usuariosRegistrados.get(usuario).equals(contraseña)) {
                    JOptionPane.showMessageDialog(null, "Exito al Iniciar Sesión");
                    abrirTienda(usuario);
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.");
                }
            }
        });

        botonRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevoUsuario = campoUsuario.getText();
                String nuevaContraseña = new String(campoContraseña.getPassword());

                if (nuevoUsuario.isEmpty() || nuevaContraseña.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un nombre de usuario y una contraseña.");
                } else if (usuariosRegistrados.containsKey(nuevoUsuario)) {
                    JOptionPane.showMessageDialog(null, "El nombre de usuario ya está registrado.");
                } else {
                    usuariosRegistrados.put(nuevoUsuario, nuevaContraseña);
                    guardarUsuarios();
                    JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.");
                }
            }
        });
    }
    /**
     * Método que abre la ventana de la tienda para el usuario autenticado.
     * 
     * @param nombreUsuario El nombre del usuario autenticado.
     */
    private void abrirTienda(String nombreUsuario) {
        this.dispose();
        SwingUtilities.invokeLater(() -> new TiendaGUI(nombreUsuario).setVisible(true));
    }
    /**
     * Método que carga los usuarios registrados desde un archivo de texto.
     * 
     * @return Un HashMap con los usuarios y sus contraseñas.
     */
    private HashMap<String, String> cargarUsuarios() {
        HashMap<String, String> usuarios = new HashMap<>();
        try (BufferedReader lector = new BufferedReader(new FileReader("usuarios.txt"))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.split(":");
                if (partes.length == 2) {
                    usuarios.put(partes[0], partes[1]);
                }
            }
        } catch (IOException e) {
        }
        return usuarios;
    }
    /**
     * Método que guarda los usuarios registrados en un archivo de texto.
     */
    private void guardarUsuarios() {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("usuarios.txt"))) {
            for (String usuario : usuariosRegistrados.keySet()) {
                escritor.write(usuario + ":" + usuariosRegistrados.get(usuario));
                escritor.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los usuarios: " + e.getMessage());
        }
    }
    /**
     * Método main que ejecuta la aplicación de Login.
     * 
     * @param args Los argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}
/**
 * Clase TiendaGUI que representa la interfaz gráfica de la tienda.
 * Permite a los usuarios seleccionar productos, agregar al carrito y generar facturas.
 */
class TiendaGUI extends JFrame {
    /** Nombre del usuario autenticado. */
    private String nombreUsuario;
    /** Lista de productos disponibles en la tienda. */
    private String[] productos = {"Coca Cola", "Cerveza Corona", "Jugo Natural", "Ace", "Red Bull","Papas Fritas",
    "Yogurt","Leche Natural","Pilfrut","Mantequilla","Jabon","Shampoo","Aceite","Pepsi","Maruchan"};
    /** Precios correspondientes a los productos. */
    private double[] precios = {15.0, 10.0, 7.0, 6.0, 20.0, 3.00, 4.00, 6.00, 1.00, 8.00, 5.00, 30.0, 12.0, 12.00, 10.0 };
    /** Stock disponible para cada producto. */
    private int[] stock = {10, 20, 15, 30, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
    /** Rutas de las imágenes asociadas a los productos. */
    private String[] imagenes = {"C:\\Users\\andyz\\Desktop\\productos\\coca.jpeg",
        "C:\\Users\\andyz\\Desktop\\productos\\corona.jpeg",   
        "C:\\Users\\andyz\\Desktop\\productos\\jugo.jpeg",   
        "C:\\Users\\andyz\\Desktop\\productos\\ace.jpg",
        "C:\\Users\\andyz\\Desktop\\productos\\redbull.jpg",
        "C:\\Users\\andyz\\Desktop\\productos\\papas.jfif" ,
        "C:\\Users\\andyz\\Desktop\\productos\\yogurt.jfif" ,
        "C:\\Users\\andyz\\Desktop\\productos\\leche.jfif" ,
        "C:\\Users\\andyz\\Desktop\\productos\\pilfrut.jfif" ,
        "C:\\Users\\andyz\\Desktop\\productos\\mantequilla.jfif" ,
        "C:\\Users\\andyz\\Desktop\\productos\\jabon.jfif" ,
        "C:\\Users\\andyz\\Desktop\\productos\\shampo.jfif" ,
        "C:\\Users\\andyz\\Desktop\\productos\\aceite.jfif" ,
        "C:\\Users\\andyz\\Desktop\\productos\\pepsi.jfif" ,
        "C:\\Users\\andyz\\Desktop\\productos\\maru.jfif" ,
        };
    /** ComboBox para seleccionar un producto. */
    private JComboBox<Producto> comboProductos;
    /** Campo de texto para ingresar la cantidad del producto a comprar. */
    private JTextField campoCantidad;
    /** Área de texto que muestra los productos en el carrito de compras. */
    private JTextArea areaCarrito;
    /** Etiqueta que muestra el total de la compra. */
    private JLabel etiquetaTotal;
    /** Etiqueta que muestra la imagen del producto seleccionado. */
    private JLabel etiquetaImagen;
    /** Total acumulado de la compra. */
    private double total = 0.0;
    /** Botón para generar la factura de la compra. */
    /** Botón para ver facturas anteriores del usuario. */
    private JButton botonGenerarFactura, botonVerFacturas;
    
     /**
     * Constructor de TiendaGUI que inicializa la interfaz gráfica y los componentes de la tienda.
     * 
     * @param nombreUsuario El nombre del usuario autenticado.
     */
    public TiendaGUI(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        cargarStock();

        setTitle("Tienda - Usuario: " + nombreUsuario);
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout());

        comboProductos = new JComboBox<>();
        comboProductos.setRenderer(new ProductoRenderer());
        cargarProductos();

        campoCantidad = new JTextField(5);

        panelSuperior.add(new JLabel("Producto:"));
        panelSuperior.add(comboProductos);
        panelSuperior.add(new JLabel("Cantidad:"));
        panelSuperior.add(campoCantidad);

        JButton botonAgregar = new JButton("Agregar al carrito");
        panelSuperior.add(botonAgregar);

        add(panelSuperior, BorderLayout.NORTH);

        areaCarrito = new JTextArea(10, 40);
        areaCarrito.setEditable(false);
        JScrollPane scrollCarrito = new JScrollPane(areaCarrito);
        add(scrollCarrito, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout());

        etiquetaTotal = new JLabel("Total: $0.0");
        botonGenerarFactura = new JButton("Generar Factura");
        botonVerFacturas = new JButton("Ver Facturas Anteriores");

        panelInferior.add(etiquetaTotal);
        panelInferior.add(botonGenerarFactura);
        panelInferior.add(botonVerFacturas);

        add(panelInferior, BorderLayout.SOUTH);

        etiquetaImagen = new JLabel();
        add(etiquetaImagen, BorderLayout.EAST);

        botonAgregar.addActionListener(e -> agregarProductoAlCarrito());
        botonGenerarFactura.addActionListener(e -> generarFactura());
        botonVerFacturas.addActionListener(e -> verFacturasAnteriores());

        comboProductos.addActionListener(e -> cambiarImagenProducto());

        cambiarImagenProducto();
    }
     /**
     * Método que carga los productos en el ComboBox con sus precios y stock.
     */
    private void cargarProductos() {
        DefaultComboBoxModel<Producto> modelo = new DefaultComboBoxModel<>();
        for (int i = 0; i < productos.length; i++) {
            modelo.addElement(new Producto(productos[i], precios[i], stock[i]));
        }
        comboProductos.setModel(modelo);
    }
    /**
     * Método que agrega un producto al carrito de compras.
     */
    private void agregarProductoAlCarrito() {
        Producto productoSeleccionado = (Producto) comboProductos.getSelectedItem();
        if (productoSeleccionado == null) return;

        int cantidad;
        try {
            cantidad = Integer.parseInt(campoCantidad.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese una cantidad válida.");
            return;
        }

        int indiceProducto = comboProductos.getSelectedIndex();

        if (cantidad > 0 && cantidad <= stock[indiceProducto]) {
            double precioProducto = precios[indiceProducto] * cantidad;
            total += precioProducto;
            stock[indiceProducto] -= cantidad;

            areaCarrito.append(productoSeleccionado.getNombre() + " x " + cantidad + " = $" + precioProducto + "\n");
            etiquetaTotal.setText("Total: $" + String.format("%.2f", total));

            actualizarProductosConStock();
            guardarStock();  // Guardar el stock actualizado en el archivo
        } else {
            JOptionPane.showMessageDialog(null, "Stock insuficiente.");
        }
    }
    /**
     * Método que cambia la imagen del producto seleccionado.
     */
    private void cambiarImagenProducto() {
        Producto productoSeleccionado = (Producto) comboProductos.getSelectedItem();
        if (productoSeleccionado == null) return;

        int indiceProducto = comboProductos.getSelectedIndex();
        ImageIcon icono = new ImageIcon(imagenes[indiceProducto]);
        Image imagen = icono.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        etiquetaImagen.setIcon(new ImageIcon(imagen));
    }
    /**
     * Método que genera la factura de la compra y la guarda en un archivo de texto.
     */
    private void generarFactura() {
        if (total == 0) {
            JOptionPane.showMessageDialog(null, "No hay productos en el carrito.");
            return;
        }

        String nombreArchivo = nombreUsuario + "_facturas.txt";
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreArchivo, true))) {
            escritor.write("Factura de " + nombreUsuario + "\n");
            escritor.write("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            escritor.write("====================================\n");
            escritor.write(areaCarrito.getText());
            escritor.write("====================================\n");
            escritor.write("Total: $" + String.format("%.2f", total) + "\n");
            escritor.write("----------------------------------\n");
            JOptionPane.showMessageDialog(null, "Factura generada y guardada en: " + nombreArchivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al generar la factura: " + e.getMessage());
        }

        areaCarrito.setText("");
        total = 0.0;
        etiquetaTotal.setText("Total: $0.0");
    }
    /**
     * Método que muestra las facturas anteriores del usuario.
     */
    private void verFacturasAnteriores() {
        String nombreArchivo = nombreUsuario + "_facturas.txt";
        try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {
            JTextArea areaFacturas = new JTextArea();
            areaFacturas.read(lector, null);
            areaFacturas.setEditable(false);

            JOptionPane.showMessageDialog(null, new JScrollPane(areaFacturas), "Facturas Anteriores", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No hay facturas anteriores.");
        }
    }
     /**
     * Método que actualiza el stock de productos desde un archivo.
     */
    private void actualizarProductosConStock() {
        DefaultComboBoxModel<Producto> modelo = new DefaultComboBoxModel<>();
        for (int i = 0; i < productos.length; i++) {
            modelo.addElement(new Producto(productos[i], precios[i], stock[i]));
        }
        comboProductos.setModel(modelo);
    }
    /**
     * Método que carga el stock de productos desde un archivo.
     */
    private void cargarStock() {
        try (Scanner lector = new Scanner(new File("stock.txt"))) {
            for (int i = 0; i < stock.length; i++) {
                if (lector.hasNextLine()) {
                    stock[i] = Integer.parseInt(lector.nextLine());
                }
            }
        } catch (IOException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo cargar el stock guardado. Usando valores predeterminados.");
        }
    }
    /**
     * Método que guarda el stock de productos en un archivo.
     */
    private void guardarStock() {
        try (FileWriter escritor = new FileWriter("stock.txt")) {
            for (int i = 0; i < stock.length; i++) {
                escritor.write(stock[i] + "\n");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar el stock: " + ex.getMessage());
        }
    }
    /**
     * Método main que ejecuta la aplicación de la tienda.
     * 
     * @param args Los argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}
/**
 * Clase Producto que representa un producto en la tienda.
 */
class Producto {
    /** Nombre del producto. */
    private String nombre;
    /** Precio del producto. */
    private double precio;
    /** Stock disponible del producto. */
    private int stock;

    /**
     * Constructor de Producto.
     * 
     * @param nombre El nombre del producto.
     * @param precio El precio del producto.
     * @param stock El stock disponible del producto.
     */
    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }
   /**
     * Obtiene el nombre del producto.
     * 
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * Obtiene el precio del producto.
     * 
     * @return El precio del producto.
     */

    public double getPrecio() {
        return precio;
    }
    /**
     * Obtiene el stock disponible del producto.
     * 
     * @return El stock del producto.
     */
    public int getStock() {
        return stock;
    }
     /**
     * Devuelve una representación en cadena del producto.
     * 
     * @return El nombre del producto.
     */
    @Override
    public String toString() {
        return nombre;
    }
}
/**
 * Clase ProductoRenderer que extiende DefaultListCellRenderer para personalizar la visualización de productos en el ComboBox.
 */
class ProductoRenderer extends DefaultListCellRenderer {
    /**
     * Renderiza el componente visual de cada producto en el ComboBox.
     * 
     * @param list La lista de productos.
     * @param value El valor del producto.
     * @param index El índice del producto.
     * @param isSelected Si el producto está seleccionado.
     * @param cellHasFocus Si la celda tiene foco.
     * @return El componente visual representando el producto.
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Producto producto = (Producto) value;
        String texto = String.format("%s - $%.2f - Stock: %d", producto.getNombre(), producto.getPrecio(), producto.getStock());
        Component componente = super.getListCellRendererComponent(list, texto, index, isSelected, cellHasFocus);
        return componente;
    }
}
