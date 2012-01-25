
package org.jhrome;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

@SuppressWarnings( "serial" )
public class JhromeTab extends JComponent
{
	JLabel				label;
	JButton				closeButton;
	CompoundBorder		compoundBorder;
	JhromeTabBorder		outerBorder;
	EmptyBorder			innerBorder;
	Component			content;
	
	boolean				selected;
	boolean				rollover;
	
	float				highlight				= 0f;
	
	float				highlightSpeed			= 0.1f;
	
	javax.swing.Timer	highlightTimer;
	Color				selectedLabelColor		= Color.BLACK;
	Color				unselectedLabelColor	= new Color( 80 , 80 , 80 );
	
	public JhromeTab( String title )
	{
		innerBorder = new EmptyBorder( 5 , 5 , 5 , 0 );
		outerBorder = new JhromeTabBorder( );
		outerBorder.copyAttributes( JhromeTabBorder.UNSELECTED_BORDER );
		compoundBorder = new CompoundBorder( outerBorder , innerBorder );
		setBorder( compoundBorder );
		setLayout( new BorderLayout( ) );
		
		label = new JLabel( title );
		label.setFont( label.getFont( ).deriveFont( Font.PLAIN ) );
		label.setForeground( unselectedLabelColor );
		add( label , BorderLayout.CENTER );
		
		closeButton = new JButton( );
		closeButton.setUI( new BasicButtonUI( ) );
		closeButton.setBorderPainted( false );
		closeButton.setContentAreaFilled( false );
		closeButton.setOpaque( false );
		closeButton.setIcon( JhromeTabCloseButtonIcons.getJhromeNormalIcon( ) );
		closeButton.setRolloverIcon( JhromeTabCloseButtonIcons.getJhromeRolloverIcon( ) );
		closeButton.setPressedIcon( JhromeTabCloseButtonIcons.getJhromePressedIcon( ) );
		closeButton.setPreferredSize( new Dimension( closeButton.getIcon( ).getIconWidth( ) + 1 , closeButton.getIcon( ).getIconHeight( ) + 1 ) );
		add( closeButton , BorderLayout.EAST );
		
		setOpaque( false );
		
		highlightTimer = new javax.swing.Timer( 30 , new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				onHighlightTimerEvent( e );
			}
		} );
		
		JPanel content = new JPanel( );
		content.setOpaque( false );
		content.setLayout( new FlowLayout( ) );
		JLabel contentLabel = new JLabel( label.getText( ) );
		contentLabel.setFont( contentLabel.getFont( ).deriveFont( 72f ) );
		content.add( contentLabel );
		
		this.content = content;
	}
	
	protected void onHighlightTimerEvent( ActionEvent e )
	{
		float targetHighlight = rollover ? 1f : 0f;
		if( highlight != targetHighlight )
		{
			highlight = animate( highlight , targetHighlight );
		}
		else
		{
			highlightTimer.stop( );
		}
		updateBorder( );
		repaint( );
	}
	
	protected void updateBorder( )
	{
		if( selected )
		{
			outerBorder.copyAttributes( JhromeTabBorder.SELECTED_BORDER );
		}
		else
		{
			outerBorder.copyAttributes( JhromeTabBorder.UNSELECTED_ROLLOVER_BORDER );
			outerBorder.interpolateColors( JhromeTabBorder.UNSELECTED_BORDER , JhromeTabBorder.UNSELECTED_ROLLOVER_BORDER , highlight );
		}
	}
	
	protected float animate( float current , float target )
	{
		if( current < target )
		{
			current = Math.min( target , current + highlightSpeed );
		}
		else if( current > target )
		{
			current = Math.max( target , current - highlightSpeed );
		}
		return current;
	}
	
	@Override
	public Dimension getMinimumSize( )
	{
		Dimension min = super.getMinimumSize( );
		if( min != null )
		{
			Insets insets = getInsets( );
			min.width = insets.left + insets.right;
		}
		return min;
	}
	
	@Override
	public void paint( Graphics g )
	{
		Dimension minSize = getMinimumSize( );
		if( minSize != null )
		{
			if( getWidth( ) < minSize.getWidth( ) )
			{
				return;
			}
		}
		super.paint( g );
	}
	
	public Component getRenderer( )
	{
		return this;
	}
	
	public Component getContent( )
	{
		return content;
	}
	
	public JButton getCloseButton( )
	{
		return closeButton;
	}
	
	public boolean isDraggableAt( Point p )
	{
		return isHoverableAt( p ) && !closeButton.contains( SwingUtilities.convertPoint( this , p , closeButton ) );
	}
	
	public boolean isSelectableAt( Point p )
	{
		return isDraggableAt( p );
	}
	
	public boolean isHoverableAt( Point p )
	{
		return outerBorder.contains( p );
	}
	
	public boolean isSelected( )
	{
		return selected;
	}
	
	public void setSelected( boolean selected )
	{
		if( this.selected != selected )
		{
			this.selected = selected;
			label.setForeground( selected ? selectedLabelColor : unselectedLabelColor );
			highlightTimer.start( );
		}
	}
	
	public boolean isRollover( )
	{
		return rollover;
	}
	
	public void setRollover( boolean rollover )
	{
		if( this.rollover != rollover )
		{
			this.rollover = rollover;
			highlightTimer.start( );
		}
	}
}