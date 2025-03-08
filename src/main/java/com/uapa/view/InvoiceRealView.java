package com.uapa.view;

import com.uapa.controller.InvoiceController;
import com.uapa.model.InvoiceItem;
import com.uapa.observer.InvoiceGUIObserver;
import com.uapa.observer.InvoiceObserver;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceRealView extends JFrame {
    private final InvoiceController controller;
    private final JTextField productField;
    private final JTextField quantityField;
    private final JTextField priceField;
    private final JTextField dateField;
    private final JTextField totalField;
    private final JButton addItemButton;
    private final JButton createInvoiceButton;
    private final JButton viewInvoicesButton;
    private final JButton removeItemButton;
    private final JTextArea itemsArea;
    private final List<InvoiceItem> items;
    private final InvoiceObserver guiObserver;

    public InvoiceRealView(InvoiceController controller) {
        this.controller = controller;
        this.items = new ArrayList<>();

        // Apply Nimbus Look and Feel if available.
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        setTitle("Generar Factura Real");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(650, 600);
        setLayout(new BorderLayout(10, 10));

        // Header panel with title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(60, 63, 65));
        JLabel headerLabel = new JLabel("Generar Factura Real");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        headerPanel.add(headerLabel);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(headerPanel, BorderLayout.NORTH);

        // Main input panel for invoice data
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 245, 245));
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.PLAIN, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);

        // Row 0: Producto
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel productLabel = new JLabel("Producto:");
        productLabel.setFont(labelFont);
        inputPanel.add(productLabel, gbc);
        gbc.gridx = 1;
        productField = new JTextField(15);
        productField.setFont(fieldFont);
        inputPanel.add(productField, gbc);

        // Row 1: Cantidad
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel quantityLabel = new JLabel("Cantidad:");
        quantityLabel.setFont(labelFont);
        inputPanel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        quantityField = new JTextField(15);
        quantityField.setFont(fieldFont);
        inputPanel.add(quantityField, gbc);

        // Row 2: Precio
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel priceLabel = new JLabel("Precio:");
        priceLabel.setFont(labelFont);
        inputPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        priceField = new JTextField(15);
        priceField.setFont(fieldFont);
        inputPanel.add(priceField, gbc);

        // Row 3: Fecha (yyyy-MM-dd)
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel dateLabel = new JLabel("Fecha (yyyy-MM-dd):");
        dateLabel.setFont(labelFont);
        inputPanel.add(dateLabel, gbc);
        gbc.gridx = 1;
        dateField = new JTextField(15);
        dateField.setFont(fieldFont);
        dateField.setText(LocalDate.now().toString());
        inputPanel.add(dateField, gbc);

        // Row 4: Total (deshabilitado)
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel totalLabel = new JLabel("Total:");
        totalLabel.setFont(labelFont);
        inputPanel.add(totalLabel, gbc);
        gbc.gridx = 1;
        totalField = new JTextField(15);
        totalField.setFont(fieldFont);
        totalField.setEditable(false);
        totalField.setText("0.0");
        inputPanel.add(totalField, gbc);

        // Row 5: Botones para Agregar Producto y Crear Factura
        gbc.gridx = 0;
        gbc.gridy = 5;
        addItemButton = new JButton("Agregar Producto");
        styleButton(addItemButton);
        inputPanel.add(addItemButton, gbc);
        gbc.gridx = 1;
        createInvoiceButton = new JButton("Crear Factura");
        styleButton(createInvoiceButton);
        inputPanel.add(createInvoiceButton, gbc);

        // Row 6: Botones para Visualizar Facturas y Eliminar Ítem
        gbc.gridx = 0;
        gbc.gridy = 6;
        viewInvoicesButton = new JButton("Visualizar Facturas");
        styleButton(viewInvoicesButton);
        inputPanel.add(viewInvoicesButton, gbc);
        gbc.gridx = 1;
        removeItemButton = new JButton("Eliminar Ítem");
        styleButton(removeItemButton);
        inputPanel.add(removeItemButton, gbc);

        add(inputPanel, BorderLayout.CENTER);

        // Log panel for added items
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBackground(new Color(245, 245, 245));
        logPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        JLabel logLabel = new JLabel("Ítems Agregados:");
        logLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        logLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        logPanel.add(logLabel, BorderLayout.NORTH);
        itemsArea = new JTextArea();
        itemsArea.setEditable(false);
        itemsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        itemsArea.setBackground(new Color(230, 230, 230));
        itemsArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(itemsArea);
        scrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        logPanel.add(scrollPane, BorderLayout.CENTER);
        add(logPanel, BorderLayout.SOUTH);

        // DocumentListener para actualizar el total mientras se ingresa cantidad o
        // precio.
        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTotalPreview();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTotalPreview();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTotalPreview();
            }
        };
        quantityField.getDocument().addDocumentListener(dl);
        priceField.getDocument().addDocumentListener(dl);

        // Configuración de listeners para botones.
        addItemButton.addActionListener(this::handleAddItem);
        createInvoiceButton.addActionListener(this::handleCreateInvoice);
        viewInvoicesButton.addActionListener(e -> {
            InvoiceListView listView = new InvoiceListView(controller);
            listView.setVisible(true);
        });
        removeItemButton.addActionListener(this::handleRemoveItem);

        // Inicializar el observador para el área de log.
        this.guiObserver = new InvoiceGUIObserver(itemsArea);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(100, 150, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void updateItemsArea() {
        itemsArea.setText("");
        double total = 0.0;
        for (InvoiceItem item : items) {
            itemsArea.append(String.format("Producto: %s, Cantidad: %d, Precio: %.2f%n",
                    item.getProduct(), item.getQuantity(), item.getPrice()));
            total += item.getQuantity() * item.getPrice();
        }
        totalField.setText(String.valueOf(total));
    }

    private void updateTotalPreview() {
        double total = 0.0;
        for (InvoiceItem item : items) {
            total += item.getQuantity() * item.getPrice();
        }
        try {
            int qty = Integer.parseInt(quantityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            total += qty * price;
        } catch (NumberFormatException e) {
            // Ignore if values are invalid.
        }
        totalField.setText(String.valueOf(total));
    }

    private void handleAddItem(ActionEvent e) {
        String product = productField.getText().trim();
        if (product.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del producto.");
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            return;
        }
        double price;
        try {
            price = Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio inválido.");
            return;
        }
        InvoiceItem item = InvoiceItem.builder()
                .product(product)
                .quantity(quantity)
                .price(price)
                .build();
        items.add(item);
        updateItemsArea();
        // Clear input fields (except date and total).
        productField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }

    private void handleCreateInvoice(ActionEvent e) {
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Agregue al menos un ítem.");
            return;
        }
        try {
            LocalDate.parse(dateField.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Use el formato yyyy-MM-dd.");
            return;
        }
        controller.generateRealInvoice(items);
        JOptionPane.showMessageDialog(this, "Factura creada con éxito.");
        items.clear();
        updateItemsArea();
    }

    private void handleRemoveItem(ActionEvent e) {
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay ítems para eliminar.");
            return;
        }
        StringBuilder sb = new StringBuilder("Ítems disponibles:\n");
        for (int i = 0; i < items.size(); i++) {
            InvoiceItem item = items.get(i);
            sb.append(String.format("%d: Producto: %s, Cantidad: %d, Precio: %.2f%n",
                    i, item.getProduct(), item.getQuantity(), item.getPrice()));
        }
        String input = JOptionPane.showInputDialog(this, sb.toString() + "\nIngrese el índice del ítem a eliminar:");
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un índice válido.");
            return;
        }
        try {
            int index = Integer.parseInt(input.trim());
            if (index < 0 || index >= items.size()) {
                JOptionPane.showMessageDialog(this, "Índice fuera de rango.");
                return;
            }
            items.remove(index);
            updateItemsArea();
            JOptionPane.showMessageDialog(this, "Ítem eliminado con éxito.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Índice inválido.");
        }
    }

    // Getter para el observador que actualiza el área de log.
    public InvoiceObserver getLogAreaObserver() {
        return guiObserver;
    }
}
