package SearchingUI;

import indexReader.InverseIndex;
import indexReader.Doc2MD5;
import indexReader.MD52Doc;
import indexReader.PageRank;

import javax.swing.*;
import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;

import java.applet.*;
import java.awt.*;

/**
 * Created by soushimei on 2/19/14.
 */
public class SearchICS extends Applet {
    
 // Declare two variables of type "int" (integer).
    int width, height;
  
    // This gets executed when the applet starts.
    public void init() {

       // Store the height and width of the applet for future reference.
//       width = getSize().width;
//       height = getSize().height;
    	width = 500;
    	height = 500;

       // Make the default background color black.
       setBackground( Color.black );
    }
  
    // This gets executed whenever the applet is asked to redraw itself.
    public void paint( Graphics g ) {

       // Set the current drawing color to green.
       g.setColor( Color.green );

       // Draw ten lines using a loop.
       // We declare a temporary variable, i, of type "int".
       // Note that "++i" is simply shorthand for "i=i+1"
       for ( int i = 0; i < 10; ++i ) {

          // The "drawLine" routine requires 4 numbers:
          // the x and y coordinates of the starting point,
          // and the x and y coordinates of the ending point,
          // in that order.  Note that the cartesian plane,
          // in this case, is upside down (as it often is
          // in 2D graphics programming): the origin is at the
          // upper left corner, the x-axis increases to the right,
          // and the y-axis increases downward.
          g.drawLine( width, height, i * width / 10, 0 );
       }
    }


//    public static void main(String[] args) {
//        //Schedule a job for the event-dispatching thread:
//        //creating and showing this application's GUI.
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
////                createAndShowGUI();
//            }
//        });
//    }
}
