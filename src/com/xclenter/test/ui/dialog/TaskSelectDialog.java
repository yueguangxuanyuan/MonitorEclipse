package com.xclenter.test.ui.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.xclenter.test.model.TaskModel;

public abstract class TaskSelectDialog extends TitleAreaDialog {
	List<TaskModel> tasks;

	public TaskSelectDialog(Shell parentShell) {
		super(parentShell);
	}

	public TaskSelectDialog(Shell parentShell, List<TaskModel> tasks) {
		super(parentShell);
		this.tasks = tasks;
	}

	private Table taskTable;

	@Override
	public void create() {
		// TODO Auto-generated method stub
		super.create();
		setTitle("Download");
		setMessage("choose a task");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(1, true);
		GridData grid = new GridData(SWT.FILL, SWT.FILL, true, true);
		container.setLayoutData(grid);
		container.setLayout(layout);

		createTaskTable(container);
		return area;
	}

	private void createTaskTable(Composite container) {
		// combine label with combo
		Composite cont = new Composite(container, SWT.NONE);
		cont.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(1, false);
		GridData grid = new GridData(SWT.FILL, SWT.FILL, true, true);
		cont.setLayoutData(grid);
		cont.setLayout(layout);

		// create label
		Label lbSubject = new Label(cont, SWT.NONE);
		lbSubject.setText("please choose an exam");

		GridData dataSubject = new GridData();
		dataSubject.grabExcessHorizontalSpace = true;
		dataSubject.horizontalAlignment = GridData.FILL;

		taskTable = new Table(cont, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL
				| SWT.H_SCROLL|SWT.FULL_SELECTION);
		taskTable.setLayoutData(dataSubject);

		setTableContents(taskTable, tasks);

//		taskTable.addSelectionListener(new SelectionListener() {
//
//			@Override
//			public void widgetDefaultSelected(SelectionEvent event) {
//				TableItem tableItem = (TableItem) event.item;
//				TaskModel selectedTask = new TaskModel(tableItem.getText(0),
//						tableItem.getText(1), tableItem.getText(2), tableItem
//								.getText(3), false);
//				doAfterSelect(selectedTask);
//			}
//
//			@Override
//			public void widgetSelected(SelectionEvent event) {
//				// TODO Auto-generated method stub
//
//			}
//
//		});
	}
	
	protected abstract void doAfterSelect(TaskModel tablemodel);
	
	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		TableItem[] items = taskTable.getSelection();
		if(items != null && items.length == 1){
			TableItem tableItem = items[0];
			TaskModel selectedTask = new TaskModel(tableItem.getText(0),
					tableItem.getText(1), tableItem.getText(2), tableItem
							.getText(3), false);
			doAfterSelect(selectedTask);
			super.okPressed();
		}else{
			MessageBox messageBox = new MessageBox(this.getShell(),
					SWT.ICON_INFORMATION);
			messageBox
					.setMessage("please select one item");
			messageBox.open();
		}
	}

	public void setTableContents(Table table, List<TaskModel> data) {
		table.removeAll();

		table.setHeaderVisible(true);

		TableColumn idColumn = new TableColumn(table, SWT.NONE);
		idColumn.setText("id");
		TableColumn nameColumn = new TableColumn(table, SWT.NONE);
		nameColumn.setText("name");
		TableColumn start_timeColumn = new TableColumn(table, SWT.NONE);
		start_timeColumn.setText("begin_time");
		TableColumn end_timeColumn = new TableColumn(table, SWT.NONE);
		end_timeColumn.setText("end_time");

		for (TaskModel item : data) {
			TableItem rowItem = new TableItem(table, SWT.NONE);
			rowItem.setText(0, item.getId());
			rowItem.setText(1, item.getName());
			rowItem.setText(2, item.getBegin_time());
			rowItem.setText(3, item.getEnd_time());
		}

		final TableColumn[] columns = table.getColumns();
		for (TableColumn column : columns) {
			column.pack();
		}
	}

}
