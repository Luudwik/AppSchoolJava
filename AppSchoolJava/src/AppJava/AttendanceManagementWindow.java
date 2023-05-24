package AppJava;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.JDatePicker;

public class AttendanceManagementWindow extends JFrame {

	private JPanel contentPane;
	private JTable table;
	static private DefaultTableModel tableModel = new DefaultTableModel();
	private static DbConn dbConn = new DbConn();
	private static int teacherId;
	private JComboBox<String> subjectComboBox;
	private static JComboBox<String> studentComboBox;
	Map<String, Integer> studentMap = new HashMap<>();

	public AttendanceManagementWindow(int teacherId) {
		AttendanceManagementWindow.teacherId = teacherId;
		subjectComboBox = new JComboBox<>();
	}

	public static void main(String[] args) {
		//int teacherId = 0;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AttendanceManagementWindow frame = new AttendanceManagementWindow(teacherId);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AttendanceManagementWindow() throws ClassNotFoundException, SQLException {
		dbConn = new DbConn();
		dbConn.Connect();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setTitle("Attendance Management");

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		scrollPane.setViewportView(table);

		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					subjectComboBox.removeAllItems();
					loadSubjects();
					refreshTableData();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(refreshButton);

		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					showAddAttendanceDialog();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(addButton);

		JButton editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showEditAttendanceDialog();
			}
		});
		buttonPanel.add(editButton);

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelectedAttendance();
			}
		});
		buttonPanel.add(deleteButton);

		subjectComboBox = new JComboBox<>();
		subjectComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					refreshTableData();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		buttonPanel.add(subjectComboBox);

		JButton backButton = new JButton("Back to menu");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				MainWindow mainWindow = null;
				try {
					mainWindow = new MainWindow();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mainWindow.setVisible(true);
			}
		});
		buttonPanel.add(backButton);

		// Inicjalizacja modelu tabeli
		tableModel = new DefaultTableModel();
		tableModel.addColumn("ID");
		tableModel.addColumn("Teacher");
		tableModel.addColumn("Student");
		tableModel.addColumn("Subject");
		tableModel.addColumn("Date");
		tableModel.addColumn("Presence");
		table.setModel(tableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		table.getColumnModel().getColumn(0).setMinWidth(50);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.setDefaultRenderer(Object.class, centerRenderer);

		refreshTableData();
		loadSubjects();
	}

	private void deleteSelectedAttendance() {
		// Sprawdź, czy został wybrany rekord do usunięcia
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "No attendance record selected.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Pobierz ID wybranego rekordu
		int attendanceId = (int) table.getValueAt(selectedRow, 0);

		int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this attendance record?",
				"Confirm Deletion", JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			try {
				Connection connection = dbConn.Connect();
				String query = "DELETE FROM Attendance WHERE id = ?";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, attendanceId);
				statement.executeUpdate();
				statement.close();
				connection.close();

				tableModel.removeRow(selectedRow);
				// JOptionPane.showMessageDialog(this, "Attendance record deleted
				// successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Failed to delete attendance record.", "Error",
						JOptionPane.ERROR_MESSAGE);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void showEditAttendanceDialog() {
		// Sprawdź, czy został wybrany rekord do edycji
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "No attendance record selected.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Pobierz dane z wybranego rekordu
		int attendanceId = (int) table.getValueAt(selectedRow, 0);
		String teacherName = (String) table.getValueAt(selectedRow, 1);
		String studentName = (String) table.getValueAt(selectedRow, 2);
		String subjectName = (String) table.getValueAt(selectedRow, 3);
		String dateString = (String) table.getValueAt(selectedRow, 4);
		String presenceText = (String) table.getValueAt(selectedRow, 5);
		boolean isPresent = presenceText.equals("Obecny");

		// Tworzenie okna dialogowego
		JDialog dialog = new JDialog(this, "Edit Attendance", true);
		dialog.setSize(300, 310);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(this);

		// Panel główny
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		// Panel danych rekordu
		JPanel dataPanel = new JPanel(new GridLayout(5, 2, 10, 10));

		// Tworzenie elementów formularza
		JLabel teacherLabel = new JLabel("Teacher:");
		JTextField teacherField = new JTextField(teacherName);
		teacherField.setEditable(false);

		JLabel studentLabel = new JLabel("Student:");
		JTextField studentField = new JTextField(studentName);
		studentField.setEditable(false);

		JLabel subjectLabel = new JLabel("Subject:");
		JTextField subjectField = new JTextField(subjectName);
		subjectField.setEditable(false);

		JLabel dateLabel = new JLabel("Date:");
		JTextField dateField = new JTextField(dateString);
		dateField.setEditable(false);

		JLabel presenceLabel = new JLabel("Presence:");
		JCheckBox presenceCheckBox = new JCheckBox("Present", isPresent);

		// Dodawanie elementów do panelu danych rekordu
		dataPanel.add(teacherLabel);
		dataPanel.add(teacherField);
		dataPanel.add(studentLabel);
		dataPanel.add(studentField);
		dataPanel.add(subjectLabel);
		dataPanel.add(subjectField);
		dataPanel.add(dateLabel);
		dataPanel.add(dateField);
		dataPanel.add(presenceLabel);
		dataPanel.add(presenceCheckBox);

		// Panel przycisków
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		// Przycisk zapisu zmian
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Pobierz zmienione dane z formularza
				boolean updatedPresence = presenceCheckBox.isSelected();

				try {
					Connection connection = dbConn.Connect();
					String query = "UPDATE Attendance SET presence = ? WHERE id = ?";
					PreparedStatement statement = connection.prepareStatement(query);
					statement.setInt(1, updatedPresence ? 1 : 0);
					statement.setInt(2, attendanceId);
					statement.executeUpdate();
					statement.close();
					connection.close();

					table.setValueAt(updatedPresence ? "Obecny" : "Nieobecny", selectedRow, 5);
					// JOptionPane.showMessageDialog(dialog, "Attendance record updated
					// successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
					dialog.dispose();

				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(dialog, "Failed to update attendance record.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(dataPanel);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(buttonPanel);

		dialog.setContentPane(mainPanel);
		dialog.setVisible(true);
	}

	private void refreshTableData() throws ClassNotFoundException {
		System.out.println("Refreshing table data...");
		try {
			String selectedSubject = (String) subjectComboBox.getSelectedItem();
			System.out.println("Selected Subject: " + selectedSubject);
			if (selectedSubject == null) {
				tableModel.setRowCount(0);
				return;
			}

			Connection connection = dbConn.Connect();
			String query = "SELECT A.id, CONCAT(T.name, ' ', T.surname) AS teacher_name, "
					+ "CONCAT(S.name, ' ', S.surname) AS student_name, SB.name AS subject_name, "
					+ "A.date, A.presence " + "FROM Attendance A " + "JOIN Teachers T ON A.id_teacher = T.id "
					+ "JOIN Students S ON A.id_student = S.id " + "JOIN Subjects SB ON A.id_subject = SB.id "
					+ "WHERE SB.name = ? AND A.id_teacher = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, selectedSubject);
			statement.setInt(2, teacherId);
			ResultSet resultSet = statement.executeQuery();

			tableModel.setRowCount(0);

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String teacherName = resultSet.getString("teacher_name");
				String studentName = resultSet.getString("student_name");
				String subjectName = resultSet.getString("subject_name");
				String date = resultSet.getString("date");
				int presence = resultSet.getInt("presence");

				String presenceText = presence == 1 ? "Obecny" : "Nieobecny";

				Vector<Object> row = new Vector<>();
				row.add(id);
				row.add(teacherName);
				row.add(studentName);
				row.add(subjectName);
				row.add(date);
				row.add(presenceText);
				tableModel.addRow(row);
			}

			resultSet.close();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadSubjects() throws SQLException, ClassNotFoundException {
		subjectComboBox.removeAllItems();
		System.out.println("Loading subjects...");

		Connection connection = dbConn.Connect();
		String query = "SELECT S.id, S.name " + "FROM Subjects S " + "JOIN TeacherSubject TS ON S.id = TS.id_subject "
				+ "WHERE TS.id_teacher = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, teacherId);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			subjectComboBox.addItem(name);
		}

		resultSet.close();
		statement.close();
		connection.close();

		refreshTableData();
	}

	private void showAddAttendanceDialog() throws ClassNotFoundException, SQLException {

		studentComboBox = new JComboBox<>();
		subjectComboBox = new JComboBox<>();
		subjectComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					refreshTableData();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		JComboBox<String> presenceComboBox = new JComboBox<>();
		JDatePicker datePicker = new JDateComponentFactory().createJDatePicker();

		loadStudents();
		loadSubjects();

		presenceComboBox.addItem("Obecny");
		presenceComboBox.addItem("Nieobecny");

		datePicker.setShowYearButtons(true);

		JPanel panel = new JPanel(new BorderLayout(10, 10));

		JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
		inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		inputPanel.add(new JLabel("Student:"));
		inputPanel.add(studentComboBox);

		inputPanel.add(new JLabel("Subject:"));
		inputPanel.add(subjectComboBox);

		inputPanel.add(new JLabel("Date:"));
		inputPanel.add((Component) datePicker);

		inputPanel.add(new JLabel("Presence:"));
		inputPanel.add(presenceComboBox);

		panel.add(inputPanel, BorderLayout.CENTER);

		int option = JOptionPane.showConfirmDialog(null, panel, "Add Attendance", JOptionPane.OK_CANCEL_OPTION);

		if (option == JOptionPane.OK_OPTION) {
			String selectedStudent = (String) studentComboBox.getSelectedItem();
			String subjectName = (String) subjectComboBox.getSelectedItem();
			java.util.Date selectedDate = ((java.util.GregorianCalendar) datePicker.getModel().getValue()).getTime();
			String presenceText = (String) presenceComboBox.getSelectedItem();
			int presence = presenceText.equals("Obecny") ? 1 : 0;

			java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

			try {
				Connection connection = dbConn.Connect();
				String query = "INSERT INTO Attendance (id_teacher, id_student, id_subject, date, presence) "
						+ "VALUES (?, ?, (SELECT id FROM Subjects WHERE name = ?), ?, ?)";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setInt(1, teacherId);
				statement.setInt(2, studentMap.get(selectedStudent)); // Użyj mapy do pobrania ID studenta
				statement.setString(3, subjectName);
				statement.setDate(4, sqlDate);
				statement.setInt(5, presence);
				statement.executeUpdate();

				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			studentComboBox.setSelectedItem(selectedStudent);
			
			refreshTableData();
		}
	}

	private Map<String, Integer> loadStudents() throws SQLException, ClassNotFoundException {
		studentComboBox.removeAllItems();

		Connection connection = dbConn.Connect();
		String query = "SELECT id, CONCAT(S.name, ' ', S.surname) AS student_name FROM Students S";
		PreparedStatement statement = connection.prepareStatement(query);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {
			int studentId = resultSet.getInt("id");
			String studentName = resultSet.getString("student_name");
			studentComboBox.addItem(studentName);
			studentMap.put(studentName, studentId);
		}

		resultSet.close();
		statement.close();
		connection.close();

		return studentMap;
	}
}