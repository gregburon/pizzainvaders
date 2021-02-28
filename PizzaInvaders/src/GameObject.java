//////////////////////////////////////////////////////////////////////////////
//
//  GameObject.java
//
//  Copyright Deep Blue Future Software
//  June 3, 2004 All rights reserved.
//
//////////////////////////////////////////////////////////////////////////////

import javax.microedition.lcdui.*;

import java.util.Vector;

/**
 * <code>GameObject</code>
 *
 * @author   Greg Buron
 * @version  $Revision: 1.0 $
 */
public abstract class GameObject
{

  // CONSTRUCTORS
  //


  /**
   * Creates a new <code>GameObject</code> instance.
   *
   * @param x an <code>int</code> value for the initial x coordinate of the object.
   * @param y an <code>int</code> value for the initial y coordinate of the object.
   * @param deltaX an <code>int</code> value for the initial x-directional speed of the object.
   * @param deltaY an <code>int</code> value for the initial y-directional speed of the object.
   * @param screenWidth an <code>int</code> value for the width of the game screen.
   * @param screenHeight an <code>int</code> value for the height of the game screen.
   * @param image an <code>Image</code> value for the image that will represent this object.
   */
  public GameObject( final int x,
                     final int y,
                     final int deltaX,
                     final int deltaY,
                     final int screenWidth,
                     final int screenHeight,
                     final Image image )
  {
    pt = new Point( x, y );

    this.deltaX = deltaX;
    this.deltaY = deltaY;
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
    this.image = image;

    boundingBoxes = new Vector();

    drawBoundingBoxes = false;
  }

  // METHODS
  //
  
  /**
   * Paints the object to the screen based on internally containted
   * <code>Image</code> and <code>Point</code> location values.
   *
   * @param g a <code>Graphics</code> value
   */
  public void paint( final Graphics g )
  {
		g.drawImage( image, pt.x, pt.y, Graphics.VCENTER | Graphics.HCENTER );

    if( drawBoundingBoxes )
    {
      int oldColor = g.getColor();
      g.setColor( 0xFF0000 );      
      int numBoxes = boundingBoxes.size();

      for( int i=0; i<numBoxes; ++i )
      {
        BoundingBox box = (BoundingBox)boundingBoxes.elementAt( i );
        box.paint( g, pt );
      }

      g.setColor( oldColor );
    }
  }

  /**
   * Set the flag for drawing all of the game object's bounding boxes.
   *
   * @param enable a <code>boolean</code> value
   */
  public void setBoundingBoxDrawEnable( final boolean enable )
  {
    this.drawBoundingBoxes = enable;
  }

  /**
   * Adds a collision bounding box to this game object, relative to the center
   * of the <code>Image</code> object.  Bounding box values are in local image
   * coordinates.
   *
   * @param box a <code>BoundingBox</code> value
   */
  public void addBoundingBox( final BoundingBox box )
  {
    if( box != null )
      boundingBoxes.addElement( box );
  }

  /**
   * Removes a bounding box from this game object.
   *
   * @param box a <code>BoundingBox</code> value
   */
  public void removeBoundingBox( final BoundingBox box )
  {
    if( box != null )
      boundingBoxes.removeElement( box );
  }

  /**
   * Activate a game object.  An activated game object is renderable if it is
   * on screen and is collidable.
   *
   */
  public void activate()
  {
    valid = true;
  }

  /**
   * Invalidate a game object.  An invalid game object is not rendered and is
   * not collidable.
   */
  public void invalidate()
  {
    pt.x = -10000000;
    pt.y = -10000000;
    valid = false;
  }
  
  /**
   * Return the valid status of the game object.  A valid object indicates that
   * it is renderable and collidable.
   *
   * @return a <code>boolean</code> value
   */
  public boolean isValid()
  {
    return valid;
  }

  /**
   * Set the motion deltas.  Calls to <code>update</code> will use these deltas
   * for future frame advances.
   *
   * @param deltaX an <code>int</code> value
   * @param deltaY an <code>int</code> value
   */
  public void setSpeeds( final int deltaX, final int deltaY )
  {
    this.deltaX = deltaX;
    this.deltaY = deltaY;
  }

  public void update()
  {
    pt.x += deltaX;
    pt.y += deltaY;
  }
  
  /**
   * Tests for an intersection with all of the <code>GameObject</code> 's
   * bounding boxes against the list of input bounding boxes from the input
   * <code>GameObject</code> parameter.
   *
   * @param box a <code>BoundingBox</code> value
   * @return a <code>boolean</code> value
   */
  public boolean intersect( final GameObject rhs )
  {
    int size = boundingBoxes.size();

    for( int i=0; i<size; ++i )
    {
      BoundingBox myBB = (BoundingBox)boundingBoxes.elementAt( i );
      int rhsSizes = rhs.boundingBoxes.size();
      
      for( int j=0; j<rhsSizes; ++j )
      {
        BoundingBox rhsBB = (BoundingBox)rhs.boundingBoxes.elementAt( j );
        
        int p1minX = pt.x + myBB.ul.x;
        int p1minY = pt.y + myBB.ul.y;
        int p1maxX = pt.x + myBB.lr.x;
        int p1maxY = pt.y + myBB.lr.y;
        int p2minX = rhs.pt.x + rhsBB.ul.x;
        int p2minY = rhs.pt.y + rhsBB.ul.y;
        int p2maxX = rhs.pt.x + rhsBB.lr.x;
        int p2maxY = rhs.pt.y + rhsBB.lr.y;

        if( p1minX > p2maxX || p2minX > p1maxX ||
            p1minY > p2maxY || p2minY > p1maxY )
          continue;
        else
          return true;
        
      }
    }
    
    return false;
  }



  
  // ATTRIBUTES
  //

  /** Location of the game object in space. */
  public Point pt;

  /** Image associated with this game object. */
  public Image image;

  public int screenWidth;

  public int screenHeight;
  

  /** List of bounding boxes to use for collision testing. */
  public Vector boundingBoxes;

  /** Debug flag for rendering bounding box. */
  private boolean drawBoundingBoxes;

  private int deltaX;

  private int deltaY;

  /**
   * Flag for the valid status of this game object (to be rendered or simply
   * used as a valid game entity for collision detection, etc.)
   */
  private boolean valid;
}




//         if( ( ( p2maxX > p1minX && p2maxX < p1maxX ) ||
//               ( p2minX > p1minX && p2maxX < p1maxX ) ||
//               ( p2maxX < p1maxX && p2minX > p1minX ) ||
//               ( p2maxX > p1maxX && p2minX < p1minX ) ) &&
            
//             ( ( p2maxY > p1minY && p2maxY < p1maxY ) ||
//               ( p2minY > p1minY && p2maxY < p1maxY ) ||
//               ( p2maxY < p1maxY && p2minY > p1minY ) ||
//               ( p2maxY > p1maxY && p2minY < p1minY ) ) )
//         {
//           return true;
//         }






