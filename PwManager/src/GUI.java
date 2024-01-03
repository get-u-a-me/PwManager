import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {

    private final Manager manager;
    private JPanel comboPanel;
    private JPanel tablePanel;
    private JTable table;


    public GUI(Manager manager){
        this.manager = manager;
        // Set the basics for the JFrame
        setTitle("Passwort-Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the buttons
        JButton addButton = new JButton("Hinzufügen");
        JButton editButton = new JButton("Bearbeiten");
        JButton deleteButton = new JButton("Löschen");
        JButton searchButton = new JButton("Suche");

        // Add buttons to the JFrame (South)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add comboPanel to main frame
        add(comboPanel = createCombo(), BorderLayout.NORTH);

        // Add tablePanel to main frame
        add(tablePanel = createTable(null));

        // Load custom window adapter
        addWindowListener(new CustomWindowAdapter(manager));


        // Add Function to AddButton
        addButton.addActionListener(e -> {
            // User Input for Account creation
            String platform = askForInput("Auf welcher Platform befindet sich der Account");
            String name = askForInput("Bitte gib den Nutzernamen ein");
            String password = askForInput("Bitte gib das Passwort ein");
            String email = askForInput("Bitte gib die email ein");

            // Account creation
            manager.add(new Account(platform, name, password, email));

            // reload the frame
            reload();
        });

        //Add function to edit Button
        editButton.addActionListener(e -> {
            //get selected Field
            int selectedRow = table.getSelectedRow();
            int selectedColumn = table.getSelectedColumn();

            if (selectedRow != -1) {
                var selectedaccount = new Account((String) table.getValueAt(selectedRow, 0), (String) table.getValueAt(selectedRow, 1), (String) table.getValueAt(selectedRow, 2), (String) table.getValueAt(selectedRow, 3));
                String newValue = askForInput("Bitte gib einen neuen wert ein");

                // Edit account based on given information
                manager.edit(selectedaccount, selectedColumn, newValue);
                // reload the frame
                reload();
            }
        });

        //Add function to deleteButton
        deleteButton.addActionListener(e -> {
            //select data
            int selectedRow = table.getSelectedRow();

            if(selectedRow != -1){
                var selected = new Account((String) table.getValueAt(selectedRow, 0), (String) table.getValueAt(selectedRow, 1), (String) table.getValueAt(selectedRow, 2), (String) table.getValueAt(selectedRow, 3));

                // remove account
                manager.remove(selected);

                // reload the frame
                reload();
            }
        });

        //SearchButton listener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = askForInput("Nach welcher Platform suchen");
                for(String value : manager.getPlatformList()){
                    if(value.equalsIgnoreCase(input)){
                        if(value != null){
                            remove(tablePanel);
                            add(tablePanel = createTable(value));
                        } else{
                            add(tablePanel = createTable(null));
                        }

                        revalidate();
                        repaint();
                    }
                }
            }
        });
        // Set the GUI visible
        setVisible(true);
    }



    // creating platform menu
    private JPanel createCombo(){
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement("Platform auswählen"); // Setze den Standardtext
        for(String a : manager.getPlatformList()){
            comboBoxModel.addElement(a);
        }
        JComboBox<String> comboBox = new JComboBox<>(comboBoxModel);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());
        panel.add(comboBox);

        comboBox.addActionListener(e -> {
            if(tablePanel != null){
                remove(tablePanel);
            }

            if(comboBox.getSelectedItem() != null){
                add(tablePanel = createTable(comboBox.getSelectedItem().toString()));
            } else{
                add(tablePanel = createTable(null));
            }

            revalidate();
            repaint();
        });

        return panel;
    }

    // creating the JTable based on Platform selection
    private JPanel createTable(String a){
        String[] column = {"Platform", "Name", "Passwort", "E-Mail"}; //Creating Columns for Table
        DefaultTableModel tableModel = new DefaultTableModel(column, 0){ //creating tableModel
            @Override
            public boolean isCellEditable(int row, int column){ //setting editable to false so cant enter random data
                return false;
            }
        };


        if(a == null || a.equalsIgnoreCase("Platform auswählen")){ //show only Accounts for selected Platform
            for(Account value : manager.getList()){
                tableModel.addRow(value.getDataArray());
            }
        } else{ //show all if none is selected
            for(Account value : manager.getList()){
                if(value.platform().equalsIgnoreCase(a)){
                    tableModel.addRow(value.getDataArray());
                }
            }
        }

        //table creation and configure

            var table = new JTable(tableModel);
            table.setColumnSelectionAllowed(true);
            table.setRowSelectionAllowed(true);
            table.setCellSelectionEnabled(false);
            table.getTableHeader().setReorderingAllowed(false);
            table.getTableHeader().setResizingAllowed(false);


        //set column width
            TableColumn column0 = table.getColumnModel().getColumn(0);
            column0.setPreferredWidth(10);
            TableColumn column1 = table.getColumnModel().getColumn(1);
            column1.setPreferredWidth(20);
            TableColumn column2 = table.getColumnModel().getColumn(2);
            column2.setPreferredWidth(20);
            TableColumn column3 = table.getColumnModel().getColumn(3);
            column3.setPreferredWidth(100);



        //add mouse Listener, so you can select data in the table and copy to clipboard
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // only copy while double-click
                    int selectedRow = table.getSelectedRow();
                    int selectedColumn = table.getSelectedColumn();

                    if (selectedRow != -1 && selectedColumn != -1) {
                        Object selectedValue = table.getValueAt(selectedRow, selectedColumn);

                        if (selectedValue != null) {
                            String selectedText = selectedValue.toString();

                            // copy text to clipboard
                            StringSelection stringSelection = new StringSelection(selectedText);
                            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
                        }
                    }
                }
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn columne = table.getColumnModel().getColumn(i);
            columne.setCellRenderer(centerRenderer);
        } //center text in field

        this.table = table;

        JScrollPane scrollPane = new JScrollPane(table);
        //update table and add it to panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    //Input function to ask for User input
    public String askForInput(String text){
        String input = "";
        while (input.trim().isEmpty()){
            input = JOptionPane.showInputDialog(text);
        }

        return input;
    }

    //function for Updating JFrame
    public void reload(){
        if(tablePanel != null){
            remove(tablePanel);
        }
        if(comboPanel != null){
            remove(comboPanel);
        }

        add(tablePanel = createTable(null));
        add(comboPanel = createCombo(), BorderLayout.NORTH);

        revalidate();
        repaint();
    }
}
