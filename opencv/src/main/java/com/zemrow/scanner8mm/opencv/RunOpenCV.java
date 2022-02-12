package com.zemrow.scanner8mm.opencv;

import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;
import com.zemrow.scanner8mm.opencv.transformation.TransformEnum;
import com.zemrow.scanner8mm.opencv.transformation.open.TransformOpen;
import com.zemrow.scanner8mm.opencv.ui.TransformEditPanel;
import com.zemrow.scanner8mm.opencv.ui.TransformationTableModel;
import nu.pattern.OpenCV;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/**
 * TODO
 * TODO добавить возможность результат трансформации сохранить как Java код
 * TODO удаление строчки
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class RunOpenCV extends JFrame implements ListSelectionListener, ActionListener {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final JLabel image;
    private final JTable table;
    private final TransformationTableModel tableModel;
    private final JComboBox<TransformEnum> comboBox;
    private final JButton addButton;
    private final TransformEditPanel transformEditPanel;
    private final JLabel statusLabel;

    public RunOpenCV() throws HeadlessException {
        image = new JLabel();
        add(new JScrollPane(image), BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(2, 1));
        add(right, BorderLayout.EAST);

        JPanel rightTop = new JPanel(new BorderLayout());
        right.add(rightTop);

        tableModel = new TransformationTableModel(this);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(this);
        final JScrollPane tableScroll = new JScrollPane(table);
        rightTop.add(tableScroll, BorderLayout.CENTER);

        JPanel addPanel = new JPanel(new BorderLayout());
        rightTop.add(addPanel, BorderLayout.SOUTH);

        comboBox = new JComboBox(TransformEnum.values());
        addPanel.add(comboBox, BorderLayout.CENTER);
        addButton = new JButton("+");
        addButton.addActionListener(this);
        addPanel.add(addButton, BorderLayout.EAST);

        transformEditPanel = new TransformEditPanel();
        right.add(transformEditPanel);

        statusLabel = new JLabel("Status");
        statusLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusLabel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 480));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (event.getValueIsAdjusting())
            return;
        log.info("valueChanged");
        final AbstractTransform transform = updateImage();
        transformEditPanel.setTransform(transform);
    }

    public AbstractTransform updateImage() {
        AbstractTransform transform = null;
        int rowIndex = table.getSelectedRow();
        log.info("updateImage rowIndex:"+rowIndex);
        if (rowIndex >= 0) {
            transform = tableModel.getTransform(rowIndex);
            ImageIcon imageIcon = transform.getImage();
            if (imageIcon != null) {
                image.setIcon(imageIcon);
            }
        }
        return transform;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        log.info("actionPerformed");
        Object source = event.getSource();
        if (addButton == source) {
            TransformEnum select = (TransformEnum) comboBox.getSelectedItem();
            // TODO insert after select row
            int rowIndex = tableModel.addTransform(select.newInstance());
            table.setRowSelectionInterval(rowIndex, rowIndex);
        }
    }

    public static void main(String[] args) {
        OpenCV.loadShared();

        RunOpenCV transformationFrame = new RunOpenCV();
        transformationFrame.tableModel.addTransform(new TransformOpen());
        transformationFrame.table.setRowSelectionInterval(0, 0);
    }
}
