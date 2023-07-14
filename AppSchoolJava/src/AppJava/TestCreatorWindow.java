package AppJava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mysql.cj.result.Row;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Iterator;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JSeparator;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.CardLayout;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.BoxLayout;

/**
 * The TestCreatorWindow class represents a GUI window for creating tests.
 */
public class TestCreatorWindow extends JFrame {

    private JPanel contentPane;
    private DbConn dbConn = new DbConn();
    private JTextField wordPolishQuantityTextField;
    private JTextField wordEnglishQuantityTextField;
    private JComboBox<String> categoryBox;
    private DefaultTableModel tableModelPreviewTest = new DefaultTableModel();
    private DefaultTableModel tableModelPreviewKeyTest = new DefaultTableModel();
    private JTable tablePreviewTest;
    private JTable tablePreviewKeyTest;
    private JScrollPane scrollPaneTestPreview;
    private JScrollPane scrollPaneTestKeyPreview;
    private JPanel contentPaneBodyCenter;
    private JPanel contentPaneHeader;
    private JPanel contentPaneBody;
    private JFileChooser fileChooser;

    private JLabel lblEmpty;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TestCreatorWindow frame = new TestCreatorWindow();
                    frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sets the custom style for a JComboBox.
     *
     * @param comboBox the JComboBox to set the style for
     */
    public static void setCustomStyle(JComboBox comboBox) {
        // Set colors
        Color background = new Color(240, 240, 240);
        Color foreground = new Color(51, 51, 51);
        Color selectionBackground = new Color(102, 102, 102);
        Color selectionForeground = Color.WHITE;

        // Set font
        Font font = new Font("Arial", Font.PLAIN, 14);

        // Set border
        Border border = BorderFactory.createLineBorder(new Color(210, 210, 210), 1);

        // Set ComboBox style
        comboBox.setBackground(background);
        comboBox.setForeground(foreground);
        comboBox.setFont(font);
        comboBox.setBorder(border);

        // Set ComboBox style on mouse hover
        comboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                comboBox.setBackground(selectionBackground);
                comboBox.setForeground(selectionForeground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                comboBox.setBackground(background);
                comboBox.setForeground(foreground);
            }
        });
    }

    /**
     * Sets the modern style for a JTable.
     *
     * @param table the JTable to set the style for
     */
    public static void setModernTableStyle(JTable table) {
        // Set font and text size in the table
        table.setFont(new Font("Roboto", Font.PLAIN, 14));

        // Set background color of the table and cells
        table.setBackground(new Color(255, 255, 255));
        table.setForeground(new Color(51, 51, 51));

        // Set color of the grid lines
        table.setGridColor(new Color(224, 224, 224));

        // Set color of the column headers
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Roboto", Font.BOLD, 14));
        header.setForeground(new Color(255, 255, 255));
        header.setBackground(new Color(30, 149, 204));
        Dimension headerSize = header.getPreferredSize();
        headerSize.height = 40;
        header.setPreferredSize(headerSize);

        // Set row spacing and row height
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Set inner cell margins
        TableCellRenderer rendererFromHeader = table.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        renderer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        table.setDefaultRenderer(Object.class, renderer);

        // Change row color on mouse hover
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    table.setSelectionBackground(new Color(150, 150, 150));
                    table.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                table.setSelectionBackground(table.getBackground());
                table.repaint();
            }
        });
    }

    /**
     * Custom button class that extends JButton.
     */
    public class MyButton extends JButton {

        public MyButton(String label) {
            super(label);
            setOpaque(true);
            setForeground(Color.WHITE);
            setBackground(new Color(51, 153, 255)); // blue background

            // Rounded corners
            setBorderPainted(false);
            setFocusPainted(false);
            setMargin(new Insets(10, 20, 10, 20));
            setPreferredSize(new Dimension(200, 50));

            // Add shadow
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            setBorder(BorderFactory.createCompoundBorder(getBorder(),
                    BorderFactory.createEtchedBorder()));

            // Change font and style
	        setFont(new Font("Arial", Font.BOLD, 16));

	        // zmiana koloru na najazdzie myszy
	        addMouseListener(new java.awt.event.MouseAdapter() {
	            public void mouseEntered(java.awt.event.MouseEvent evt) {
	                setBackground(new Color(0, 119, 204));
	            }
	            public void mouseExited(java.awt.event.MouseEvent evt) {
	            	setBackground(new Color(51, 153, 255));
	            }
	        });
	    }
	}
	
	//private class MyTable extends JTable {
       
        // ustawienie stylu
		public void MyTable(JTable table) {
			table.setFont(new Font("Arial", Font.PLAIN, 14));
			table.setRowHeight(30);
			table.setGridColor(new Color(210, 210, 210));
			table.setOpaque(false);
			table.setBackground(Color.WHITE);
			table.setForeground(new Color(51, 51, 51));
			table.setRowSelectionAllowed(false);
			table.setColumnSelectionAllowed(false);
			table.setCellSelectionEnabled(false);
			table.setFocusable(false);
	
	        // zmiana koloru nagłówków kolumn
	        JTableHeader header = table.getTableHeader();
	        header.setBackground(new Color(0, 119, 204)); // niebieskie tło
	        header.setForeground(Color.WHITE);
	        header.setFont(new Font("Arial", Font.BOLD, 14));
	        header.setOpaque(false);
	
	        // dodanie cienia
	        table.setBorder(BorderFactory.createEmptyBorder());
	        table.setBorder(BorderFactory.createCompoundBorder(table.getBorder(),
	                BorderFactory.createEtchedBorder()));
	
	        // zmiana wyglądu kolumn podczas najścia na nie kursorem
	        table.addMouseListener(new MouseListener() {
	            public void mouseEntered(MouseEvent evt) {
	                int row = table.rowAtPoint(evt.getPoint());
	                int col = table.columnAtPoint(evt.getPoint());
	                table.setSelectionBackground(new Color(150,150,150));
	                table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	                if (row >= 0 && col >= 0) {
	                    table.setRowSelectionInterval(row, row);
	                    table.setColumnSelectionInterval(col, col);
	                }
	            }
	
	            public void mouseExited(MouseEvent evt) {
	                table.setCursor(Cursor.getDefaultCursor());
	                table.clearSelection();
	                
	            }

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
	        });
		}
    //}
	

	public void FillCategory() {
		try {
			Connection con = dbConn.Connect();
			PreparedStatement pst = con.prepareStatement("SELECT name FROM FlashcardCategory");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				categoryBox.addItem(name);
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas połączenia z bazą danych:\n" + e.getMessage());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void CreateTest() {

		try {
			Connection con = dbConn.Connect();
			PreparedStatement pst;
			ResultSet rs;
			String category = categoryBox.getSelectedItem().toString();
			
			if (scrollPaneTestPreview != null) {
			    contentPaneBodyCenter.remove(scrollPaneTestPreview);
			    scrollPaneTestPreview = null;
			}

			if (scrollPaneTestKeyPreview != null) {
			    contentPaneBodyCenter.remove(scrollPaneTestKeyPreview);
			    scrollPaneTestKeyPreview = null;
			}
			
			//System.out.println(tableModelPreviewTest.getRowCount());
			if (tableModelPreviewTest.getRowCount() > 0) {
			    for (int i = tableModelPreviewTest.getRowCount() - 1; i > -1; i--) {
			    	tableModelPreviewTest.removeRow(i);
			    }
			}
			if (tableModelPreviewKeyTest.getRowCount() > 0) {
			    for (int i = tableModelPreviewKeyTest.getRowCount() - 1; i > -1; i--) {
			    	tableModelPreviewKeyTest.removeRow(i);
			    }
			}

			pst = con.prepareStatement("SELECT * FROM `Flashcards` JOIN `FlashcardCategory` "
					+ "ON Flashcards.id_category = FlashcardCategory.id WHERE FlashcardCategory.name='" + category
					+ "'");
			rs = pst.executeQuery();

			int rowCounter = 0;
			while (rs.next()) {
				rowCounter++;
			}
			rs = pst.executeQuery();
			int wordPolishQuantity = Integer.parseInt(wordPolishQuantityTextField.getText());
			int wordEnglishQuantity = Integer.parseInt(wordEnglishQuantityTextField.getText());
			int wordCount = wordPolishQuantity + wordEnglishQuantity;
			if ((wordPolishQuantity + wordEnglishQuantity) <= rowCounter) {
				pst = con.prepareStatement("SELECT * FROM `Flashcards` JOIN `FlashcardCategory` "
						+ "ON Flashcards.id_category = FlashcardCategory.id WHERE FlashcardCategory.name='" + category
						+ "' ORDER BY RAND() LIMIT " + wordCount);
				rs = pst.executeQuery();

				
				
				int wordPolishCounter=0;
				int wordEnglishCounter=0;

				while (rs.next()) {
					tableModelPreviewKeyTest.insertRow(0,
							new Object[] { rs.getString("wordPolish"), rs.getString("wordEnglish") });
					if(wordPolishCounter<wordPolishQuantity) {
						tableModelPreviewTest.insertRow(0,  new Object[] {rs.getString("wordPolish")});
						wordPolishCounter++;
					}
					else if(wordEnglishCounter<wordEnglishQuantity) {
						tableModelPreviewTest.insertRow(0,  new Object[] {rs.getString("wordEnglish")});
						wordEnglishCounter++;
					}
					
				}
				if(tablePreviewKeyTest != null) {
					tablePreviewKeyTest.removeAll();
				}
				if(tablePreviewTest != null) {
					tablePreviewTest.removeAll();
				}
				
				
				tablePreviewKeyTest = new JTable(tableModelPreviewKeyTest);
				//tablePreviewKeyTest.setRowHeight(30);
				//MyTable(tablePreviewKeyTest);
				setModernTableStyle(tablePreviewKeyTest);
				
				scrollPaneTestKeyPreview = new JScrollPane(tablePreviewKeyTest);
				contentPaneBodyCenter.add(scrollPaneTestKeyPreview, BorderLayout.EAST);
				
				
				tablePreviewTest = new JTable(tableModelPreviewTest);
				//tablePreviewTest.setRowHeight(30);
				setModernTableStyle(tablePreviewTest);
				scrollPaneTestPreview = new JScrollPane(tablePreviewTest);
				contentPaneBodyCenter.add(scrollPaneTestPreview, BorderLayout.WEST);

			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//tableModelPreviewTest = new DefaultTableModel();
				tableModelPreviewTest.addColumn("Słówko w teście");

//				tableModelPreviewKeyTest = new DefaultTableModel();
				tableModelPreviewKeyTest.addColumn("Słówko po polsku");
				tableModelPreviewKeyTest.addColumn("Słówko po angielsku");

		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height); 
		// Ustawienie zachowania okna na pełny ekran 
		setExtendedState(JFrame.MAXIMIZED_BOTH); 

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		contentPaneHeader = new JPanel();
		contentPaneHeader.setBackground(Color.WHITE);
		contentPane.add(contentPaneHeader, BorderLayout.NORTH);
		contentPaneHeader.setLayout(new BorderLayout(0, 0));

		JPanel contentPaneHeaderUpper = new JPanel();
		contentPaneHeaderUpper.setBackground(new Color(88, 188, 208));
		contentPaneHeader.add(contentPaneHeaderUpper, BorderLayout.NORTH);

		contentPaneHeaderUpper.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("Fiszki");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 34));
		contentPaneHeaderUpper.add(lblNewLabel, BorderLayout.CENTER);

		JLabel lblImage = new JLabel("");
		Image imgFlashcardIcon = new ImageIcon(getClass().getClassLoader().getResource("icon/icon-flashcard.png"))
				.getImage();
		Image scaledImgFlashcardIcon = imgFlashcardIcon.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		lblImage.setIcon(new ImageIcon(scaledImgFlashcardIcon));
		lblImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPaneHeaderUpper.add(lblImage, BorderLayout.WEST);

		JPanel contentPaneHeaderLower = new JPanel();
		contentPaneHeaderLower.setBackground(new Color(150, 150, 150));
		contentPaneHeader.add(contentPaneHeaderLower, BorderLayout.CENTER);
		contentPaneHeaderLower.setLayout(new GridLayout(2, 5, 0, 0));
		contentPaneHeaderLower.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));

		JLabel lblCategory = new JLabel("Kategoria");
		lblCategory.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblCategory.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		contentPaneHeaderLower.add(lblCategory);

		JLabel lblWordEnglishQuantity = new JLabel("Ilość słówek po angielsku");
		lblWordEnglishQuantity.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblWordEnglishQuantity.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		contentPaneHeaderLower.add(lblWordEnglishQuantity);

		JLabel lblWordPolishQuantity = new JLabel("Ilość słówek po polsku");
		lblWordPolishQuantity.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblWordPolishQuantity.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		contentPaneHeaderLower.add(lblWordPolishQuantity);

		lblEmpty = new JLabel(" ");
		contentPaneHeaderLower.add(lblEmpty);

		categoryBox = new JComboBox<String>();
		categoryBox.setBackground(Color.LIGHT_GRAY);
		categoryBox.setForeground(Color.BLACK);
		categoryBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		categoryBox.setFocusable(false);
		//setCustomStyle(categoryBox);
		contentPaneHeaderLower.add(categoryBox);

		wordEnglishQuantityTextField = new JTextField();
		wordEnglishQuantityTextField.setBackground(Color.LIGHT_GRAY);
		contentPaneHeaderLower.add(wordEnglishQuantityTextField);

		wordPolishQuantityTextField = new JTextField();
		wordPolishQuantityTextField.setBackground(Color.LIGHT_GRAY);
		contentPaneHeaderLower.add(wordPolishQuantityTextField);

		JPanel contentPaneHeaderLowerButton = new JPanel();
		contentPaneHeaderLowerButton.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		contentPaneHeaderLowerButton.setBackground(Color.LIGHT_GRAY);
		contentPaneHeaderLower.add(contentPaneHeaderLowerButton);
		contentPaneHeaderLowerButton.setLayout(new GridLayout(1, 1, 5, 5));
		
		

		MyButton btnCreate = new MyButton("Utwórz");
		//btnCreate.setFont(new Font("Tahoma", Font.PLAIN, 18));
		//btnCreate.setBackground(new Color(68, 178, 218));
		//btnCreate.setBackground(new Color(255, 255, 255));
		//btnCreate.setBorder(new RoundedBorder(20));
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateTest();
				revalidate();
				repaint();
				
			}
		});
		btnCreate.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				//btnCreate.setBackground(new Color(48, 158, 188));
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//btnCreate.setBackground(new Color(68, 178, 218));
				
			}
			
		});
		contentPaneHeaderLowerButton.add(btnCreate);
		
		MyButton btnBack = new MyButton("Wróć");
		//btnCreate.setFont(new Font("Tahoma", Font.PLAIN, 18));
		//btnCreate.setBackground(new Color(68, 178, 218));
		//btnCreate.setBackground(new Color(255, 255, 255));
		//btnCreate.setBorder(new RoundedBorder(20));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dispose();
					MainWindow mainWindow = new MainWindow(LoginUI.id_teacher);
					mainWindow.setVisible(true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnBack.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				//btnCreate.setBackground(new Color(48, 158, 188));
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//btnCreate.setBackground(new Color(68, 178, 218));
				
			}
			
		});
		contentPaneHeaderLowerButton.add(btnBack);

		contentPaneBody = new JPanel();
		contentPaneBody.setBackground(new Color(217, 238, 250));
		contentPane.add(contentPaneBody, BorderLayout.CENTER);
		contentPaneBody.setLayout(new BorderLayout(0, 0));
		
		JPanel contentPaneBodyUpper = new JPanel();
		contentPaneBodyUpper.setBackground(new Color(180, 180, 180, 70));
		contentPaneBody.add(contentPaneBodyUpper, BorderLayout.NORTH);
		contentPaneBodyUpper.setLayout(new BorderLayout(0, 0));

		JPanel contentPaneBodyUpperLabels = new JPanel();
		contentPaneBodyUpperLabels.setBackground(new Color(180, 180, 180, 70));
		contentPaneBodyUpper.add(contentPaneBodyUpperLabels, BorderLayout.SOUTH);
		contentPaneBodyUpperLabels.setLayout(new BorderLayout(0, 0));
		
		JPanel contentPaneBodyUpperPdfButtons = new JPanel();
		contentPaneBodyUpperPdfButtons.setBackground(new Color(120, 120, 120, 70));
		contentPaneBodyUpper.add(contentPaneBodyUpperPdfButtons, BorderLayout.NORTH);
		contentPaneBodyUpperPdfButtons.setLayout(new BoxLayout(contentPaneBodyUpperPdfButtons, BoxLayout.X_AXIS));
		
		
		MyButton btnCreatePdfTestKey = new MyButton("Generuj plik PDF z odpowiedziami");
		contentPaneBodyUpperPdfButtons.add(btnCreatePdfTestKey);
		btnCreatePdfTestKey.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	fileChooser = new JFileChooser();
		        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", ".pdf"));
		        fileChooser.setDialogTitle("Wybierz plik PDF");
		    	
		        if (e.getSource() == btnCreatePdfTestKey) {
		            int returnVal = fileChooser.showSaveDialog(null);
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                File file = fileChooser.getSelectedFile();
		                try {
		                    Document document = new Document();
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setInitialLeading(12.5f);
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setPdfVersion(PdfWriter.VERSION_1_7);
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setViewerPreferences(PdfWriter.PageModeUseOutlines);
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setViewerPreferences(PdfWriter.PageLayoutTwoColumnLeft);
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setFullCompression();
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setStrictImageSequence(true);
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setLinearPageMode();
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setLanguage("pl");
		                    PdfWriter.getInstance(document, new FileOutputStream(file + ".pdf"));
		                    document.open();

		                    // create table with columns based on JTable
		                    PdfPTable pdfTable = new PdfPTable(tablePreviewKeyTest.getColumnCount());

		                    // add column headers
		                    for (int i = 0; i < tablePreviewKeyTest.getColumnCount(); i++) {
		                        PdfPCell cell = new PdfPCell();
		                        cell.setPhrase(new Paragraph(tablePreviewKeyTest.getColumnName(i)));
		                        pdfTable.addCell(cell);
		                    }

		                    // add rows to the table
		                    for (int i = 0; i < tablePreviewKeyTest.getRowCount(); i++) {
		                        for (int j = 0; j < tablePreviewKeyTest.getColumnCount(); j++) {
		                            PdfPCell cell = new PdfPCell();
		                            cell.setPhrase(new Paragraph(tablePreviewKeyTest.getValueAt(i, j).toString()));
		                            pdfTable.addCell(cell);
		                        }
		                    }

		                    document.add(pdfTable);
		                    document.close();
		                    JOptionPane.showMessageDialog(null, "Plik PDF został zapisany pomyślnie!");
		                } catch (IOException | DocumentException ex) {
		                    JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas zapisywania pliku PDF: " + ex.getMessage());
		                }
		            }
		        };
		    }
			
		});
		

		
		MyButton btnCreatePdfTest = new MyButton("Generuj plik PDF z testem");
		contentPaneBodyUpperPdfButtons.add(btnCreatePdfTest);
		btnCreatePdfTest.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	fileChooser = new JFileChooser();
		        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", ".pdf"));
		        fileChooser.setDialogTitle("Wybierz plik PDF");
		    	
		        if (e.getSource() == btnCreatePdfTest) {
		            int returnVal = fileChooser.showSaveDialog(null);
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                File file = fileChooser.getSelectedFile();
		                try {
		                    Document document = new Document();
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setInitialLeading(12.5f);
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setPdfVersion(PdfWriter.VERSION_1_7);
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setViewerPreferences(PdfWriter.PageModeUseOutlines);
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setViewerPreferences(PdfWriter.PageLayoutTwoColumnLeft);
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setFullCompression();
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setStrictImageSequence(true);
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setLinearPageMode();
		                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf")).setLanguage("pl");
		                    PdfWriter.getInstance(document, new FileOutputStream(file + ".pdf"));
		                    document.open();

		                    // create table with columns based on JTable
		                    PdfPTable pdfTable = new PdfPTable(tablePreviewTest.getColumnCount());

		                    // add column headers
		                    for (int i = 0; i < tablePreviewTest.getColumnCount(); i++) {
		                        PdfPCell cell = new PdfPCell();
		                        cell.setPhrase(new Paragraph(tablePreviewTest.getColumnName(i)));
		                        pdfTable.addCell(cell);
		                    }

		                    // add rows to the table
		                    for (int i = 0; i < tablePreviewTest.getRowCount(); i++) {
		                        for (int j = 0; j < tablePreviewTest.getColumnCount(); j++) {
		                            PdfPCell cell = new PdfPCell();
		                            cell.setPhrase(new Paragraph(tablePreviewTest.getValueAt(i, j).toString()));
		                            pdfTable.addCell(cell);
		                        }
		                    }

		                    document.add(pdfTable);
		                    document.close();
		                    JOptionPane.showMessageDialog(null, "Plik PDF został zapisany pomyślnie!");
		                } catch (IOException | DocumentException ex) {
		                    JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas zapisywania pliku PDF: " + ex.getMessage());
		                }
		            }
		        };
		    }
			
		});
		

		JLabel lblTestPreview = new JLabel("Podgląd testu");
		lblTestPreview.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTestPreview.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 330));
		contentPaneBodyUpperLabels.add(lblTestPreview, BorderLayout.EAST);

		JLabel lblTestKeyPreview = new JLabel("Podgląd klucza odpowiedzi");
		lblTestKeyPreview.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTestKeyPreview.setBorder(BorderFactory.createEmptyBorder(10, 270, 10, 0));
		contentPaneBodyUpperLabels.add(lblTestKeyPreview, BorderLayout.WEST);
		

		contentPaneBodyCenter = new JPanel();
		contentPaneBodyCenter.setBackground(new Color(217, 238, 250));
		contentPaneBody.add(contentPaneBodyCenter, BorderLayout.CENTER);
		contentPaneBodyCenter.setLayout(new BoxLayout(contentPaneBodyCenter, BoxLayout.X_AXIS));

		scrollPaneTestPreview = new JScrollPane();
		contentPaneBodyCenter.add(scrollPaneTestPreview);

	}
	
	public TestCreatorWindow() {
		initialize();
		FillCategory();
		
	}
}
