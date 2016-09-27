package com.xclenter.test.ui.dialog;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LoginDialog extends TitleAreaDialog {

	  private Text txtUsername;
	  private Text txtPassword;

	  private String username;
	  private String password;

	  public LoginDialog(Shell parentShell) {
	    super(parentShell);
	  }

	  @Override
	  public void create() {
	    super.create();
	    setTitle("login");
	    setMessage("please enter your account and password", IMessageProvider.INFORMATION);
	  }

	  @Override
	  protected Control createDialogArea(Composite parent) {
	    Composite area = (Composite) super.createDialogArea(parent);
	    Composite container = new Composite(area, SWT.NONE);
	    container.setLayoutData(new GridData(GridData.FILL_BOTH));
	    GridLayout layout = new GridLayout(1, false);
	    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    container.setLayout(layout);

	    createUsername(container);
	    createPassword(container);

	    return area;
	  }

	  private void createUsername(Composite container) {
	    Label lbtusername = new Label(container, SWT.NONE);
	    lbtusername.setText("account");

	    GridData datausername = new GridData();
	    datausername.grabExcessHorizontalSpace = true;
	    datausername.horizontalAlignment = GridData.FILL;

	    txtUsername = new Text(container, SWT.BORDER);
	    txtUsername.setLayoutData(datausername);
	  }

	 private void createPassword(Composite container) {
	    Label lbtpassword = new Label(container, SWT.NONE);
	    lbtpassword.setText("password");

	    GridData datapassword = new GridData();
	    datapassword.grabExcessHorizontalSpace = true;
	    datapassword.horizontalAlignment = GridData.FILL;
	    txtPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
	    txtPassword.setLayoutData(datapassword);
	  }

	  @Override
	  protected boolean isResizable() {
	    return true;
	  }

	  // save content of the Text fields because they get disposed
	  // as soon as the Dialog closes
	  protected void saveInput() {
	    username = txtUsername.getText();
	    password = txtPassword.getText();

	  }

	  @Override
	  protected void okPressed() {
	    saveInput();
	    super.okPressed();
	  }

	  public String getUsername() {
	    return username;
	  }

	  public String getPassword() {
	    return password;
	  }
}
