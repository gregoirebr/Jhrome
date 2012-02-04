
package org.sexydock.tabs.demos;

import java.awt.Window;

import org.sexydock.tabs.ITab;
import org.sexydock.tabs.ITabbedPaneWindow;

public class UndecoratedWindowDemo implements ISexyTabsDemo
{
	@Override
	public void start( )
	{
		UndecoratedTabbedPaneWindowFactory windowFactory = new UndecoratedTabbedPaneWindowFactory( );
		ITabbedPaneWindow tabbedPaneWindow = windowFactory.createWindow( );
		Window window = tabbedPaneWindow.getWindow( );
		
		ITab tab1 = tabbedPaneWindow.getTabbedPane( ).getTabFactory( ).createTab( "Tab 1" );
		tabbedPaneWindow.getTabbedPane( ).addTab( tab1 );
		tabbedPaneWindow.getTabbedPane( ).setSelectedTab( tab1 );
		
		window.setSize( 800 , 600 );
		window.setLocationRelativeTo( null );
		window.setVisible( true );
	}
}