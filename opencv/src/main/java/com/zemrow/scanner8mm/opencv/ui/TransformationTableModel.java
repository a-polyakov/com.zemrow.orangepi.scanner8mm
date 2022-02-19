package com.zemrow.scanner8mm.opencv.ui;

import com.zemrow.scanner8mm.opencv.RunOpenCV;
import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;
import org.opencv.core.Mat;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformationTableModel extends AbstractTableModel implements ChangeListener {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final List<AbstractTransform> data;

    private RunOpenCV transformationFrame;

    public TransformationTableModel(RunOpenCV transformationFrame) {
        this.data = new ArrayList<>();
        this.transformationFrame = transformationFrame;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return data.get(rowIndex).getTitle();
            default:
                return "-";
        }
    }

    public AbstractTransform getTransform(int rowIndex) {
        return data.get(rowIndex);
    }

    public int addTransform(AbstractTransform transform) {
        log.info("addTransform class:" + transform.getClass().getSimpleName());
        transform.setChangeListener(this);
        data.add(transform);
        int rowIndex = data.size() - 1;
        transformChain(rowIndex);
        fireTableRowsInserted(rowIndex, rowIndex);
        return rowIndex;
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        Object source = event.getSource();
        int rowIndex = data.indexOf(source);
        log.info("stateChanged rowIndex:" + rowIndex);
        if (rowIndex >= 0) {
            transformChain(rowIndex);
        }
    }

    private void transformChain(int rowIndex) {
        log.info("transformChain rowIndex:" + rowIndex + ", size:" + data.size());
        //TODO thread
        // panel.setEnabled(false);
        // if (sw == null) {
        //     sw = new SwingWorker<ImageIcon, Object>() {
        //          protected ImageIcon doInBackground() throws Exception {
        //           ...
        //          }
        //          @Override
        //          protected void done() {
        //              panel.setEnabled(true);
        //              sw = null;
        //          }
        //     };
        // sw.execute();

        Mat previous = null;
        if (rowIndex > 0) {
            previous = data.get(rowIndex - 1).getMat();
        }
        for (; rowIndex < data.size(); rowIndex++) {
            try {
                previous = data.get(rowIndex).transform(previous);
            } catch (Exception e) {
                //TODO
                log.severe(e.getMessage());
            }
        }
        // TODO
        transformationFrame.updateImage();
    }

    public void upRow(int lastRow) {
        AbstractTransform temp = data.get(lastRow);
        int firstRow = lastRow - 1;
        data.set(lastRow, data.get(firstRow));
        data.set(firstRow, temp);
        transformChain(firstRow);
        fireTableRowsUpdated(firstRow, lastRow);
    }

    public void downRow(int firstRow) {
        AbstractTransform temp = data.get(firstRow);
        int lastRow = firstRow + 1;
        data.set(firstRow, data.get(lastRow));
        data.set(lastRow, temp);
        transformChain(firstRow);
        fireTableRowsUpdated(firstRow, lastRow);
    }

    public void removeRow(int row) {
        data.remove(row);
        fireTableRowsDeleted(row, row);
    }
}
