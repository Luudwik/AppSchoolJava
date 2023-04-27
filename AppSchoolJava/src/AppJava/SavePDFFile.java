package AppJava;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class SavePDFFile implements ActionListener {
    private JButton saveBtn;
    private JFileChooser fileChooser;
    private JTable table;

    public SavePDFFile(JTable table) {
        this.table = table;
        saveBtn = new JButton("Zapisz jako PDF");
        saveBtn.addActionListener(this);
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", ".pdf"));
        fileChooser.setDialogTitle("Wybierz plik PDF");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            int returnVal = fileChooser.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(file+".pdf"));
                    document.open();

                    // create table with columns based on JTable
                    PdfPTable pdfTable = new PdfPTable(table.getColumnCount());

                    // add column headers
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Paragraph(table.getColumnName(i)));
                        pdfTable.addCell(cell);
                    }

                    // add rows to the table
                    for (int i = 0; i < table.getRowCount(); i++) {
                        for (int j = 0; j < table.getColumnCount(); j++) {
                            PdfPCell cell = new PdfPCell();
                            cell.setPhrase(new Paragraph(table.getValueAt(i, j).toString()));
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
        }
    }
}
