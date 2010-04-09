package com.google.client.ui;
import com.google.gwt.i18n.client.Messages;
public interface SputnikMessages extends Messages {

	@DefaultMessage("Sputnik")
	String title();

	@DefaultMessage("JavaScript Conformance - version {0}")
	String subtitle(String version);

	@DefaultMessage("About")
	String aboutLabel();

	@DefaultMessage("Run")
	String runLabel();

	@DefaultMessage("Compare")
	String compareLabel();

}
