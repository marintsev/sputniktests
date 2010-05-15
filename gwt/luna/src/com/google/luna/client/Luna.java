// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.luna.client.control.AboutPage;
import com.google.luna.client.control.IPage;
import com.google.luna.client.control.RunPage;
import com.google.luna.client.rmi.Backend;
import com.google.luna.client.rmi.CrossDomainServerConnection;
import com.google.luna.client.rmi.DirectServerConnection;
import com.google.luna.client.rmi.ServerConnection;
import com.google.luna.client.rmi.Backend.Package;
import com.google.luna.client.test.Es5ConformTestCase;
import com.google.luna.client.test.ITestCase;
import com.google.luna.client.test.SputnikTestCase;
import com.google.luna.client.test.TestPackage;
import com.google.luna.client.ui.ErrorDialog;
import com.google.luna.client.ui.IPageView;
import com.google.luna.client.ui.IUiMessages;
import com.google.luna.client.ui.ResultEntry;
import com.google.luna.client.ui.RunView;
import com.google.luna.client.ui.TestControlPanel;
import com.google.luna.client.ui.Toplevel;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

public class Luna implements EntryPoint {

  /**
   * Static flag that determines if client-side devel mode can be
   * enabled by the server.  Set this to false in release mode,
   * otherwise devel code will be included in the generated code.
   */
  private static final boolean enableDevelMode = true;

  private static final Map<String, IPage.IFactory<?>> PAGES = new HashMap<String, IPage.IFactory<?>>() {{
    put("About", AboutPage.getFactory());
    put("Run", RunPage.getFactory());
  }};

  private static final Map<String, ITestCase.IFactory> TEST_CASE_FACTORIES = new HashMap<String, ITestCase.IFactory>() {{
    put("sputnik", SputnikTestCase.getFactory());
    put("es5conform", Es5ConformTestCase.getFactory());
  }};

  private static final IUiMessages MESSAGES = GWT.create(IUiMessages.class);

  private static Node workspace;

  @Override
  public void onModuleLoad() {
  	ensureCssInjected();
    String pageName = getParameters().getPageName();
    IPage.IFactory<?> factory = PAGES.get(pageName);
    IPage<?> page = factory.createPage();
    IPageView view = page.bindView();
    final Toplevel toplevel = Toplevel.create(view);
    toplevel.init();
    page.init();
    RootPanel.get().add(toplevel);
  }

  public static Node getWorkspace() {
  	assert workspace != null;
  	return workspace;
  }

  public static void setWorkspace(Node value) {
  	assert workspace == null;
  	workspace = value;
  }

  private void ensureCssInjected() {
  	Toplevel.getResources().css().ensureInjected();
  	TestControlPanel.getResources().css().ensureInjected();
  	RunView.getResources().css().ensureInjected();
  	ResultEntry.getResources().css().ensureInjected();
  }

  public static void reportError(Throwable error) {
    ErrorDialog dialog = new ErrorDialog(error);
    dialog.setAutoHideEnabled(true);
    dialog.center();
  }

  public static ITestCase.IFactory getTestCaseFactory(String type) {
    return TEST_CASE_FACTORIES.get(type);
  }

  public static IUiMessages getMessages() {
    return MESSAGES;
  }

  public static native Parameters getParameters() /*-{
    return top.kParameters;
	}-*/;

  public static native Object getCannedRequest(String path) /*-{
	  return top.kCannedRequests[path];
	}-*/;

  private static ServerConnection.IFactory getServerConnectionFactory() {
    String dataPath = getParameters().getDataPath();
    if (enableDevelMode && getParameters().isClientSideDevel()) {
      return CrossDomainServerConnection.getFactory(dataPath);
    } else {
      return DirectServerConnection.getFactory(dataPath);
    }
  }

  public static boolean isDevelMode() {
    Parameters params = getParameters();
    return enableDevelMode || params.isServerSideDevel() || params.isClientSideDevel();
  }

  private static Backend backend;
  public static Backend getBackend() {
    if (backend == null)
      backend = new Backend(getServerConnectionFactory());
    return backend;
  }

  private static Promise<TestPackage> activePackage;
  public static Promise<TestPackage> getActivePackage() {
    if (activePackage == null) {
      activePackage = new Promise<TestPackage>();
      getBackend().getActivePackage().onValue(new Thunk<Backend.Package>() {
        @Override
        public void onValue(Package data) {
          activePackage.setValue(new TestPackage(data));
        }
      });
    }
    return activePackage;
  }

}
