package AppJava;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.JDatePicker;
import java.util.Date;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * The AttendanceManagementWindow class represents a window for managing attendance records.
 * It allows the user to view, add, edit, and delete attendance records.
 */
public class AttendanceManagementWindow extends JFrame {

	private JPanel contentPane;
	private JTable table;
	static private DefaultTableModel tableModel = new DefaultTableModel();
	private static DbConn dbConn = new DbConn();
	private static int teacherId;
	private JComboBox<String> subjectComboBox;
	private static JComboBox<String> studentComboBox;
	Map<String, Integer> studentMap = new HashMap<>();
	private JDatePicker datePicker;

	/**
	 * Constructs an AttendanceManagementWindow object.
	 * @param teacherId the ID of the teacher using the window
	 */
	public AttendanceManagementWindow(int teacherId) {
		AttendanceManagementWindow.teacherId = teacherId;
		subjectComboBox = new JComboBox<>();
	}

	/**
	 * Launches the application.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
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

	/**
	 * Creates the AttendanceManagementWindow.
	 * @throws ClassNotFoundException if the database driver class is not found
	 * @throws SQLException if an SQL error occurs
	 */
	public AttendanceManagementWindow() throws ClassNotFoundException, SQLException {
		dbConn = new DbConn();
		dbConn.Connect();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 600);
		setTitle("Attendance Management");

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBackground(new Color(40, 44, 52)); // Set background color
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setBackground(new Color(61, 64, 71)); // Set scroll bar color
		scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = new Color(61, 64, 71); // Set thumb color
			}
		});
		contentPane.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setBackground(new Color(61, 64, 71)); // Set table background color
		table.setForeground(Color.WHITE); // Set table font color
		table.setSelectionBackground(new Color(92, 107, 192)); // Set selection background color in table
		table.setSelectionForeground(Color.WHITE); // Set selection font color in table
		table.getTableHeader().setBackground(new Color(61, 64, 71)); // Set table header background color
		table.getTableHeader().setForeground(Color.WHITE); // Set table header font color
		table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD)); // Set bold font for table header
		table.setGridColor(new Color(128, 128, 128)); // Set color for grid lines between cells
		table.setFont(new Font("Tahoma", Font.PLAIN, 12)); // Set table font

		scrollPane.setViewportView(table);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(40, 44, 52)); // Set background color for button panel
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		JButton addButton = new JButton("Add");
		addButton.setBackground(new Color(92, 107, 192)); // Set button background color
		addButton.setForeground(Color.WHITE); // Set button font color
		addButton.setFont(new Font("Tahoma", Font.PLAIN, 12)); // Set button font
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
		editButton.setBackground(new Color(92, 107, 192));
		editButton.setForeground(Color.WHITE);
		editButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showEditAttendanceDialog();
			}
		});
		buttonPanel.add(editButton);

		JButton deleteButton = new JButton("Delete");
		deleteButton.setBackground(new Color(92, 107, 192));
		deleteButton.setForeground(Color.WHITE);
		deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelectedAttendance();
			}
		});
		buttonPanel.add(deleteButton);

		subjectComboBox = new JComboBox<>();
		subjectComboBox.setBackground(new Color(92, 107, 192));
		subjectComboBox.setForeground(Color.WHITE);
		subjectComboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
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

		JPanel datePanel = new JPanel();
		datePanel.setBackground(new Color(40, 44, 52));
		datePicker = new JDateComponentFactory().createJDatePicker();
		JLabel dateLabel = new JLabel("Date:");
		dateLabel.setForeground(Color.WHITE);
		datePanel.add(dateLabel);
		datePanel.add((Component) datePicker);
		buttonPanel.add(datePanel);

		JButton refreshButton = new JButton("Refresh");
		refreshButton.setBackground(new Color(92, 107, 192));
		refreshButton.setForeground(Color.WHITE);
		refreshButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					subjectComboBox.removeAllItems();
					loadSubjects();
					refreshTableData();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(refreshButton);

		JButton backButton = new JButton("Back to menu");
		backButton.setBackground(new Color(92, 107, 192));
		backButton.setForeground(Color.WHITE);
		backButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				MainWindow mainWindow = null;
				try {
					mainWindow = new MainWindow(LoginUI.id_teacher);
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
				mainWindow.setVisible(true);
			}
		});
		buttonPanel.add(backButton);

		// Initialize table model
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

	/**
	 * Returns the selected date from the date picker.
	 * @return the selected date
	 */
	private Date getSelectedDate() {
		if (datePicker.getModel().getValue() != null) {
			return (Date) ((GregorianCalendar) datePicker.getModel().getValue()).getTime();
		} else {
			return null;
		}
	}

	/**
	 * Deletes the selected attendance record from the table and database.
	 */
	private void deleteSelectedAttendance() {
		// Check if a record is selected
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "No attendance record selected.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Get the ID of the selected record
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

	/**
	 * Displays a dialog to edit the selected attendance record.
	 */
	private void showEditAttendanceDialog() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "No attendance record selected.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int attendanceId = (int) table.getValueAt(selectedRow, 0);
		String teacherName = (String) table.getValueAt(selectedRow, 1);
		String studentName = (String) table.getValueAt(selectedRow, 2);
		String subjectName = (String) table.getValueAt(selectedRow, 3);
		String dateString = (String) table.getValueAt(selectedRow, 4);
		String presenceText = (String) table.getValueAt(selectedRow, 5);
		boolean isPresent = presenceText.equals("Obecny");

		JDialog dialog = new JDialog(this, "Edit Attendance", true);
		dialog.setSize(300, 310);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(this);
		dialog.getContentPane().setBackground(new Color(40, 44, 52)); // Set dialog background color

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setBackground(new Color(40, 44, 52)); // Set main panel background color

		JPanel dataPanel = new JPanel(new GridLayout(5, 2, 10, 10));
		dataPanel.setBackground(new Color(40, 44, 52)); // Set data panel background color

		JLabel teacherLabel = new JLabel("Teacher:");
		teacherLabel.setForeground(Color.WHITE); // Set label font color
		JTextField teacherField = new JTextField(teacherName);
		teacherField.setEditable(false);

		JLabel studentLabel = new JLabel("Student:");
		studentLabel.setForeground(Color.WHITE);
		JTextField studentField = new JTextField(studentName);
		studentField.setEditable(false);

		JLabel subjectLabel = new JLabel("Subject:");
		subjectLabel.setForeground(Color.WHITE);
		JTextField subjectField = new JTextField(subjectName);
		subjectField.setEditable(false);

		JLabel dateLabel = new JLabel("Date:");
		dateLabel.setForeground(Color.WHITE);
		JTextField dateField = new JTextField(dateString);
		dateField.setEditable(false);

		JLabel presenceLabel = new JLabel("Presence:");
		presenceLabel.setForeground(Color.WHITE);
		JComboBox<String> presenceComboBox = new JComboBox<>();
		presenceComboBox.setBackground(new Color(92, 107, 192));
		presenceComboBox.setForeground(Color.WHITE);
		presenceComboBox.addItem("Obecny");
		presenceComboBox.addItem("Nieobecny");
		presenceComboBox.addItem("Spóźnienie");
		presenceComboBox.addItem("Usprawiedliwiono");
		presenceComboBox.setSelectedItem(presenceText);

		dataPanel.add(teacherLabel);
		dataPanel.add(teacherField);
		dataPanel.add(studentLabel);
		dataPanel.add(studentField);
		dataPanel.add(subjectLabel);
		dataPanel.add(subjectField);
		dataPanel.add(dateLabel);
		dataPanel.add(dateField);
		dataPanel.add(presenceLabel);
		dataPanel.add(presenceComboBox);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBackground(new Color(40, 44, 52)); // Set button panel background color

		JButton saveButton = new JButton("Save");
		saveButton.setBackground(new Color(92, 107, 192));
		saveButton.setForeground(Color.WHITE);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String updatedPresence = (String) presenceComboBox.getSelectedItem();

				try {
					Connection connection = dbConn.Connect();
					String query = "UPDATE Attendance SET presence = ? WHERE id = ?";
					PreparedStatement statement = connection.prepareStatement(query);
					int presenceValue = getPresenceValue(updatedPresence);
					statement.setInt(1, presenceValue);
					statement.setInt(2, attendanceId);
					statement.executeUpdate();
					statement.close();
					connection.close();

					table.setValueAt(updatedPresence, selectedRow, 5);
					dialog.dispose();
				} catch (SQLException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(dialog, "Failed to update attendance record.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBackground(new Color(92, 107, 192));
		cancelButton.setForeground(Color.WHITE);
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

	/**
	 * Returns the integer value corresponding to the presence option.
	 * @param presenceText the presence option
	 * @return the integer value representing the presence option
	 */
	private int getPresenceValue(String presenceText) {
		if (presenceText.equals("Obecny")) {
			return 1;
		} else if (presenceText.equals("Nieobecny")) {
			return 0;
		} else if (presenceText.equals("Spóźnienie")) {
			return 2;
		} else if (presenceText.equals("Usprawiedliwiono")) {
			return 3;
		}
		return -1; // Default value for unknown option
	}

	/**
	 * Refreshes the table data based on the selected subject and date.
	 * @throws ClassNotFoundException if the database driver class is not found
	 */
	private void refreshTableData() throws ClassNotFoundException {
		System.out.println("Refreshing table data...");
		try {
			String selectedSubject = (String) subjectComboBox.getSelectedItem();
			Date selectedDate = getSelectedDate();
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

			if (selectedDate != null) {
				query += " AND A.date = ?";
			}
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, selectedSubject);
			statement.setInt(2, teacherId);
			if (selectedDate != null) {
				statement.setDate(3, new java.sql.Date(selectedDate.getTime()));
			}
			ResultSet resultSet = statement.executeQuery();

			tableModel.setRowCount(0);

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String teacherName = resultSet.getString("teacher_name");
				String studentName = resultSet.getString("student_name");
				String subjectName = resultSet.getString("subject_name");
				String date = resultSet.getString("date");
				int presence = resultSet.getInt("presence");

				String presenceText;
				if (presence == 1) {
					presenceText = "Obecny";
				} else if (presence == 0) {
					presenceText = "Nieobecny";
				} else if (presence == 2) {
					presenceText = "Spóźnienie";
				} else if (presence == 3) {
					presenceText = "Usprawiedliwiono";
				} else {
					presenceText = "Nieznany";
				}

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

	/**
	 * Loads the subjects for the teacher and populates the subject combo box.
	 * @throws SQLException if a database access error occurs
	 * @throws ClassNotFoundException if the database driver class is not found
	 */
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
	}

	/**
	 * Shows the dialog to add a new attendance record.
	 * @throws ClassNotFoundException if the database driver class is not found
	 * @throws SQLException if an SQL error occurs
	 */
	private void showAddAttendanceDialog() throws ClassNotFoundException, SQLException {
		JDialog dialog = new JDialog(this, "Add Attendance", true);
		dialog.setSize(300, 310);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(this);
		dialog.getContentPane().setBackground(new Color(40, 44, 52)); // Set dialog background color

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.setBackground(new Color(40, 44, 52)); // Set main panel background color

		JPanel dataPanel = new JPanel(new GridLayout(4, 2, 10, 10));
		dataPanel.setBackground(new Color(40, 44, 52)); // Set data panel background color

		JLabel studentLabel = new JLabel("Student:");
		studentLabel.setForeground(Color.WHITE);
		studentComboBox = new JComboBox<>();
		studentComboBox.setBackground(new Color(92, 107, 192));
		studentComboBox.setForeground(Color.WHITE);
		loadStudents();
		studentComboBox.setSelectedIndex(-1);

		JLabel subjectLabel = new JLabel("Subject:");
		subjectLabel.setForeground(Color.WHITE);
		JTextField subjectField = new JTextField();
		subjectField.setEditable(false);
		subjectField.setText((String) subjectComboBox.getSelectedItem());

		JLabel dateLabel = new JLabel("Date:");
		dateLabel.setForeground(Color.WHITE);
		datePicker = new JDateComponentFactory().createJDatePicker();

		dataPanel.add(studentLabel);
		dataPanel.add(studentComboBox);
		dataPanel.add(subjectLabel);
		dataPanel.add(subjectField);
		dataPanel.add(dateLabel);
		dataPanel.add((Component) datePicker);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBackground(new Color(40, 44, 52)); // Set button panel background color

		JButton addButton = new JButton("Add");
		addButton.setBackground(new Color(92, 107, 192));
		addButton.setForeground(Color.WHITE);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addAttendance();
				dialog.dispose();
			}
		});

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBackground(new Color(92, 107, 192));
		cancelButton.setForeground(Color.WHITE);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		buttonPanel.add(addButton);
		buttonPanel.add(cancelButton);

		mainPanel.add(dataPanel);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(buttonPanel);

		dialog.setContentPane(mainPanel);
		dialog.setVisible(true);
	}

	/**
	 * Adds a new attendance record to the database and table.
	 */
	private void addAttendance() {
		String selectedSubject = (String) subjectComboBox.getSelectedItem();
		String selectedStudent = (String) studentComboBox.getSelectedItem();
		Date selectedDate = getSelectedDate();

		if (selectedSubject == null || selectedStudent == null || selectedDate == null) {
			JOptionPane.showMessageDialog(this, "Please select a subject, student, and date.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			Connection connection = dbConn.Connect();
			String query = "INSERT INTO Attendance (id_teacher, id_student, id_subject, date, presence) "
					+ "VALUES (?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, teacherId);
			statement.setInt(2, studentMap.get(selectedStudent));
			statement.setInt(3, getSubjectId(selectedSubject));
			statement.setDate(4, new java.sql.Date(selectedDate.getTime()));
			statement.setInt(5, 1);
			statement.executeUpdate();
			statement.close();
			connection.close();

			refreshTableData();
			JOptionPane.showMessageDialog(this, "Attendance record added successfully.", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to add attendance record.", "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the ID of the selected subject.
	 * @param subjectName the name of the subject
	 * @return the ID of the subject
	 */
	private int getSubjectId(String subjectName) {
		try {
			Connection connection = dbConn.Connect();
			String query = "SELECT id FROM Subjects WHERE name = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, subjectName);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int subjectId = resultSet.getInt("id");
				resultSet.close();
				statement.close();
				connection.close();
				return subjectId;
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Loads the students for the selected subject and populates the student combo box.
	 */
	private void loadStudents() {
		studentComboBox.removeAllItems();
		try {
			Connection connection = dbConn.Connect();
			String query = "SELECT S.id, CONCAT(S.name, ' ', S.surname) AS student_name "
					+ "FROM Students S " + "JOIN StudentSubject SS ON S.id = SS.id_student "
					+ "JOIN Subjects SB ON SS.id_subject = SB.id "
					+ "WHERE SB.name = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, (String) subjectComboBox.getSelectedItem());
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("student_name");

				studentComboBox.addItem(name);
				studentMap.put(name, id);
			}

			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
