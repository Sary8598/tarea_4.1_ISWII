package com.uapa.view;

import com.uapa.controller.InvoiceController;
import com.uapa.model.Invoice;
import com.uapa.model.InvoiceItem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class InvoiceListView extends JFrame {
    private final InvoiceController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    public InvoiceListView(InvoiceController controller) {
        this.controller = controller;

        // Set Nimbus Look and Feel if available.
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                // Fallback: do nothing.
            }
        }

        setTitle("Listado de Facturas");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with a BorderLayout and a custom background.
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        setContentPane(mainPanel);

        // Header panel with a title label.
        JLabel headerLabel = new JLabel("Listado de Facturas", SwingConstants.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(60, 63, 65));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Table panel.
        tableModel = new DefaultTableModel(
                new Object[] { "ID Factura", "Fecha", "Total", "Producto", "Cantidad", "Precio" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));

        // Customize the table header.
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(220, 220, 220));
        header.setForeground(new Color(60, 63, 65));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer panel with the refresh button.
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(245, 245, 245));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton refreshButton = new JButton("Refrescar");
        refreshButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        refreshButton.setBackground(new Color(100, 150, 255));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadInvoices());
        footerPanel.add(refreshButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        loadInvoices();
    }

    private void loadInvoices() {
        List<Invoice> invoices = controller.getAllInvoices();
        tableModel.setRowCount(0);
        for (Invoice invoice : invoices) {
            // If the invoice has no items, add an empty row for item details.
            if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
                tableModel.addRow(new Object[] {
                        invoice.getId(),
                        invoice.getDate().toString(),
                        invoice.getTotal(),
                        "", "", ""
                });
            } else {
                // For each item, add a row with both invoice and item details.
                for (InvoiceItem item : invoice.getItems()) {
                    tableModel.addRow(new Object[] {
                            invoice.getId(),
                            invoice.getDate().toString(),
                            item.getPrice() * item.getQuantity(),
                            item.getProduct(),
                            item.getQuantity(),
                            item.getPrice()
                    });
                }
            }
        }
    }
}
