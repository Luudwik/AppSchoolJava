package AppJava;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

		try {
			loadSubjects();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// int teacherId = 0;
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
					refreshTableData();
				} catch (ClassNotFoundException e1) {
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
				// showEditAttendanceDialog();
			}
		});
		buttonPanel.add(editButton);

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// deleteSelectedAttendance();
			}
		});
		buttonPanel.add(deleteButton);

		subjectComboBox = new JComboBox<>();
		subjectComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					refreshTableData();
					revalidate();
					repaint();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		buttonPanel.add(subjectComboBox);

		// Inicjalizacja modelu tabeli
		tableModel = new DefaultTableModel();
		tableModel.addColumn("ID");
		tableModel.addColumn("Teacher");
		tableModel.addColumn("Student");
		tableModel.addColumn("Subject");
		tableModel.addColumn("Date");
		tableModel.addColumn("Presence");
		table.setModel(tableModel);

		refreshTableData();
		loadSubjects();
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

		// Po załadowaniu przedmiotów odświeżamy dane w tabeli
		refreshTableData();
	}

	private void showAddAttendanceDialog() throws ClassNotFoundException, SQLException {

		studentComboBox = new JComboBox<>();
		subjectComboBox = new JComboBox<>();
		subjectComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					refreshTableData(); // Po zmianie przedmiotu odświeżamy dane w tabeli
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
			subjectComboBox.removeAllItems();
			refreshTableData();
			loadSubjects();
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