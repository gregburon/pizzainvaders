//////////////////////////////////////////////////////////////////////////////
//
//  BoundingBox.java
//
//  Copyright Deep Blue Future Software
//  June 7, 2004 All rights reserved.
//
//////////////////////////////////////////////////////////////////////////////

import javax.microedition.lcdui.*;


/**
 * <code>BoundingBox</code>
 *
 * @author   Greg Buron
 * @version  $Revision: 1.0 $
 */
public class BoundingBox
{

  // CONSTRUCTORS
  //

  /**
   * Creates a new <code>BoundingBox</code> instance.
   *
   * @param ul a <code>Point</code> value
   * @param lr a <code>Point</code> value
   */
  public BoundingBox( final Point ul, final Point lr )
  {
    this.ul = ul;
    this.lr = lr;
  }

  // METHODS
  //



  public void paint( final Graphics g, final Point center )
  {
    int x = center.x + ul.x;
    int y = center.y + ul.y;
    int width = lr.x - ul.x;
    int height = lr.y - ul.y;

    g.drawRect( x, y, width, height );
  }
  
//   /**
//    * Test if this bounding box intersects with the input parameter bounding
//    * box.
//    *
//    * @param box a <code>BoundingBox</code> value
//    * @return a <code>boolean</code> value
//    */
//   public boolean intersect( final BoundingBox box, final Point center )
//   {

//     return false;
//   }

  // ATTRIBUTES
  //

  /**
   * Upper left hand corner of the bounding box, relative to the center of the
   * image.
   */
  public Point ul;
  
  /**
   * Lower right hand corner of the bounding box, relative to the center of the
   * image.
   */
  public Point lr;
}





















//   /**
//    * Set the bounds of the bounding box.
//    *
//    * @param ul a <code>Point</code> value
//    * @param lr a <code>Point</code> value
//    */
//   public void update( final Point ul, final Point lr )
//   {
//     this.ul = ul;
//     this.lr = lr;
//   }

//   /**
//    * Set the bounds of the bounding box.
//    *
//    * @param x an <code>int</code> value
//    * @param y an <code>int</code> value
//    * @param width an <code>int</code> value
//    * @param height an <code>int</code> value
//    */
//   public void update( final int x, final int y,
//                       final int width, final int height )
//   {
//     this.ul.x = x;
//     this.ul.y = y;
//     this.lr.x = x + width;
//     this.lr.y = y + height;
//   }
