// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.luna.client.control.IPage;
import com.google.luna.client.control.RunPage;
import com.google.luna.client.rmi.Backend;
import com.google.luna.client.rmi.CrossDomainServerConnection;
import com.google.luna.client.rmi.DirectServerConnection;
import com.google.luna.client.rmi.ServerConnection;
import com.google.luna.client.rmi.Backend.Package;
import com.google.luna.client.test.Es5ConformTestController;
import com.google.luna.client.test.ITestController;
import com.google.luna.client.test.SputnikTestController;
import com.google.luna.client.test.TestPackage;
import com.google.luna.client.ui.ErrorDialog;
import com.google.luna.client.ui.IPageView;
import com.google.luna.client.ui.IUiMessages;
import com.google.luna.client.ui.Toplevel;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;

import java.util.HashMap;
import java.util.Map;

public class Luna implements EntryPoint {

  /**
   * Static flag that determines if client-side devel mode can be
   * enabled by the server.  Set this to false in release mode,
   * otherwise devel code will be included in the generated code.
   */
  private static final boolean enableDevelMode = true;

  private static final Map<String, IPage.IFactory<?>> PAGES = new HashMap<String, IPage.IFactory<?>>() {{
    put("Run", RunPage.getFactory());
  }};

  private static final Map<String, ITestController.Factory> CONTROLLERS = new HashMap<String, ITestController.Factory>() {{
    put("sputnik", SputnikTestController.getFactory());
    put("es5conform", Es5ConformTestController.getFactory());
  }};

  private static final IUiMessages MESSAGES = GWT.create(IUiMessages.class);

  @Override
  public void onModuleLoad() {
    String pageName = getParameters().getPageName();
    IPage.IFactory<?> factory = PAGES.get(pageName);
    IPage<?> page = factory.createPage();
    IPageView view = page.bindView();
    final Toplevel toplevel = new Toplevel(view);
    toplevel.init();
    page.init();
    RootPanel.get().add(toplevel);
  }

  public static void reportError(Throwable error) {
    ErrorDialog dialog = new ErrorDialog(error);
    dialog.setAutoHideEnabled(true);
    dialog.center();
  }

  public static ITestController.Factory getTestControllerFactory(String type) {
    return CONTROLLERS.get(type);
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
