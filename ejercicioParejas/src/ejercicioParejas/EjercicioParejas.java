package ejercicioParejas;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import ejercicioParejas.ConnectionSingleton;

import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

public class EjercicioParejas {

	private JFrame frmCrudAsignaturas;
	private JTable table;
	private JTextField textFieldCod;
	private JTextField textFieldNom;
	private JTextField textFieldHoras;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EjercicioParejas window = new EjercicioParejas();
					window.frmCrudAsignaturas.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EjercicioParejas() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmCrudAsignaturas = new JFrame();
		frmCrudAsignaturas.setTitle("CRUD ASIGNATURAS");
		frmCrudAsignaturas.setBounds(100, 100, 807, 489);
		frmCrudAsignaturas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCrudAsignaturas.getContentPane().setLayout(null);

		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("CodAsignatura");
		model.addColumn("Nombre");
		model.addColumn("Horas");

		try {
			Connection con = ConnectionSingleton.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Asignaturas");
			while (rs.next()) {
				Object[] row = new Object[3];
				row[0] = rs.getInt("codAsignatura");
				row[1] = rs.getString("nombre");
				row[2] = rs.getInt("horas");
				model.addRow(row);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.getErrorCode();
			e.printStackTrace();
		}

		JTable table = new JTable(model);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();

				textFieldCod.setText(model.getValueAt(index, 0).toString());
				textFieldNom.setText(model.getValueAt(index, 1).toString());
				textFieldHoras.setText(model.getValueAt(index, 2).toString());
			}
		});
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(51, 47, 397, 188);

		frmCrudAsignaturas.getContentPane().add(scrollPane);

		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement stmt = con
							.prepareStatement("UPDATE Asignaturas SET nombre=?,horas=? WHERE codAsignatura=?");

					stmt.setString(1, textFieldNom.getText());
					stmt.setInt(2, Integer.valueOf(textFieldHoras.getText()));
					stmt.setInt(3, Integer.valueOf(textFieldCod.getText()));
					int rowsUpdate = stmt.executeUpdate();
					stmt.close();

				} catch (SQLException e) {
					System.err.println(e.getMessage());
					e.getErrorCode();
					e.printStackTrace();
				}
				
				try {
					model.setRowCount(0);

					Connection con = ConnectionSingleton.getConnection();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM Asignaturas");

					while (rs.next()) {
						Object[] row = new Object[3];
						row[0] = rs.getInt("codAsignatura");
						row[1] = rs.getString("nombre");
						row[2] = rs.getInt("horas");
						model.addRow(row);
					}
				} catch (SQLException e) {
					System.err.println(e.getMessage());
					e.getErrorCode();
					e.printStackTrace();
				}
			}
		});
		btnActualizar.setBounds(443, 314, 117, 25);
		frmCrudAsignaturas.getContentPane().add(btnActualizar);

		JLabel lblCod = new JLabel("Cod:");
		lblCod.setBounds(51, 295, 70, 15);
		frmCrudAsignaturas.getContentPane().add(lblCod);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(51, 319, 70, 15);
		frmCrudAsignaturas.getContentPane().add(lblNombre);

		JLabel lblHoras = new JLabel("Horas:");
		lblHoras.setBounds(51, 346, 70, 15);
		frmCrudAsignaturas.getContentPane().add(lblHoras);

		textFieldCod = new JTextField();
		textFieldCod.setEditable(false);
		textFieldCod.setEnabled(false);
		textFieldCod.setBounds(128, 293, 114, 19);
		frmCrudAsignaturas.getContentPane().add(textFieldCod);
		textFieldCod.setColumns(10);

		textFieldNom = new JTextField();
		textFieldNom.setBounds(128, 317, 114, 19);
		frmCrudAsignaturas.getContentPane().add(textFieldNom);
		textFieldNom.setColumns(10);

		textFieldHoras = new JTextField();
		textFieldHoras.setBounds(128, 344, 114, 19);
		frmCrudAsignaturas.getContentPane().add(textFieldHoras);
		textFieldHoras.setColumns(10);

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement stmt = con
							.prepareStatement("INSERT INTO Asignaturas (nombre,horas) VALUES (?,?)");

					stmt.setString(1, textFieldNom.getText());
					stmt.setInt(2, Integer.valueOf(textFieldHoras.getText()));

					int rowsInserted = stmt.executeUpdate();
					stmt.close();

				} catch (SQLException e) {
					System.err.println(e.getMessage());
					e.getErrorCode();
					e.printStackTrace();
				}

				try {
					model.setRowCount(0);

					Connection con = ConnectionSingleton.getConnection();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM Asignaturas");

					while (rs.next()) {
						Object[] row = new Object[3];
						row[0] = rs.getInt("codAsignatura");
						row[1] = rs.getString("nombre");
						row[2] = rs.getInt("horas");
						model.addRow(row);
					}
				} catch (SQLException e) {
					System.err.println(e.getMessage());
					e.getErrorCode();
					e.printStackTrace();
				}
			}
		});
		btnGuardar.setBounds(443, 277, 117, 25);
		frmCrudAsignaturas.getContentPane().add(btnGuardar);

		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement stmt = con.prepareStatement("DELETE FROM Asignaturas WHERE codAsignatura=?");
					stmt.setInt(1, Integer.valueOf(textFieldCod.getText()));
					int rowsDeleted = stmt.executeUpdate();
					stmt.close();

				} catch (SQLException e) {
					System.err.println(e.getMessage());
					e.getErrorCode();
					e.printStackTrace();
				}
				try {
					model.setRowCount(0);

					Connection con = ConnectionSingleton.getConnection();
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM Asignaturas");

					while (rs.next()) {
						Object[] row = new Object[3];
						row[0] = rs.getInt("codAsignatura");
						row[1] = rs.getString("nombre");
						row[2] = rs.getInt("horas");
						model.addRow(row);
					}
				} catch (SQLException e) {
					System.err.println(e.getMessage());
					e.getErrorCode();
					e.printStackTrace();
				}

			}
		});
		btnBorrar.setBounds(443, 351, 117, 25);
		frmCrudAsignaturas.getContentPane().add(btnBorrar);
	}
}
