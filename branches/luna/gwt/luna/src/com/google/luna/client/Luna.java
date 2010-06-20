// Copyright 2010 the Sputnik authors.  All rights reserved.
// This code is governed by the BSD license found in the LICENSE file.

package com.google.luna.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.luna.client.control.AboutPage;
import com.google.luna.client.control.ComparePage;
import com.google.luna.client.control.IPage;
import com.google.luna.client.control.ManagePage;
import com.google.luna.client.logic.RunPagePresenter;
import com.google.luna.client.logic.ToplevelPresenter;
import com.google.luna.client.rmi.Backend;
import com.google.luna.client.rmi.CrossDomainServerConnection;
import com.google.luna.client.rmi.DirectServerConnection;
import com.google.luna.client.rmi.ServerConnection;
import com.google.luna.client.rmi.Backend.Package;
import com.google.luna.client.test.Es5ConformTestCase;
import com.google.luna.client.test.SputnikTestCase;
import com.google.luna.client.test.data.ITestCase;
import com.google.luna.client.test.data.ITestPackage;
import com.google.luna.client.test.data.TestPackageImpl;
import com.google.luna.client.utils.BrowserCookieJar;
import com.google.luna.client.utils.Cookie;
import com.google.luna.client.utils.Promise;
import com.google.luna.client.utils.Thunk;
import com.google.luna.client.widget.ErrorDialog;
import com.google.luna.client.widget.IPageView;
import com.google.luna.client.widget.IUiMessages;
import com.google.luna.client.widget.ResultEntry;
import com.google.luna.client.widget.RunView;
import com.google.luna.client.widget.TestControlPanelWidget;
import com.google.luna.client.widget.ToplevelWidget;

import java.util.HashMap;
import java.util.Map;

/**
 * Entry point for the Luna app.  Also provides access to app-global
 * state such as app parameters, current user information, etc.
 *
 *
 */
public class Luna implements EntryPoint {

  /**
   * Static flag that determines if client-side devel mode can be
   * enabled by the server.  Set this to false in release mode,
   * otherwise devel code will be included in the generated code.
   */
  private static final boolean enableDevelMode = true;

  private static final Map<String, IPage.IFactory<?>> kPages = new HashMap<String, IPage.IFactory<?>>() {{
    put("About", AboutPage.getFactory());
    put("Run", RunPagePresenter.getFactory());
    put("Compare", ComparePage.getFactory());
    put("Manage", ManagePage.getFactory());
  }};

  private static final Map<String, ITestCase.IFactory> kTestCaseFactories = new HashMap<String, ITestCase.IFactory>() {{
    put("sputnik", SputnikTestCase.getFactory());
    put("es5conform", Es5ConformTestCase.getFactory());
  }};

  private static final IUiMessages kMessages = GWT.create(IUiMessages.class);

  private static Node workspace;
  private static ToplevelPresenter toplevel;

  @Override
  public void onModuleLoad() {
    ensureCssInjected();
    String pageName = getParameters().getPageName();
    IPage.IFactory<?> factory = kPages.get(pageName);
    IPage<?> page = factory.createPage();
    IPageView view = page.bindView();
    toplevel = new ToplevelPresenter();
    ToplevelWidget widget = ToplevelWidget.create(toplevel, view);
    toplevel.init();
    page.init();
    RootPanel.get().add(widget);
  }

  public static ToplevelPresenter getToplevel() {
    return toplevel;
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
    ToplevelWidget.getResources().css().ensureInjected();
    TestControlPanelWidget.getResources().css().ensureInjected();
    RunView.getResources().css().ensureInjected();
    ResultEntry.getResources().css().ensureInjected();
  }

  public static void reportError(Throwable error) {
    ErrorDialog dialog = new ErrorDialog(error);
    dialog.setAutoHideEnabled(true);
    dialog.center();
  }

  /**
   * Returns the test case factory for a given test type identifier.
   */
  public static ITestCase.IFactory getTestCaseFactory(String type) {
    return kTestCaseFactories.get(type);
  }

  public static IUiMessages getMessages() {
    return kMessages;
  }

  /**
   * Returns the app parameters.
   */
  public static native Parameters getParameters() /*-{
    return top.kParameters;
  }-*/;

  /**
   * Looks up a request path in the set of pre-populated server
   * requests.
   */
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

  private static Boolean devModeUiCache = null;
  public static boolean showDevModeUi() {
    if (devModeUiCache == null) {
      devModeUiCache = "true".equals(Location.getParameter("devModeUi"));
    }
    return devModeUiCache;
  }

  private static final Cookie.Factory rootCookieFactory =
    new BrowserCookieJar().factory().child("luna");
  public static Cookie.Factory getRootCookieFactory() {
    return rootCookieFactory;
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

  private static Promise<ITestPackage> activePackage;
  public static Promise<ITestPackage> getActivePackage() {
    if (activePackage == null) {
      activePackage = new Promise<ITestPackage>();
      getBackend().getActivePackage().onValue(new Thunk<Backend.Package>() {
        @Override
        public void onValue(Package data) {
          activePackage.setValue(new TestPackageImpl(data));
        }
      });
    }
    return activePackage;
  }

}
