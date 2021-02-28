//////////////////////////////////////////////////////////////////////////////
//
//  Laser.java
//
//  Copyright Deep Blue Future Software
//  June 3, 2004 All rights reserved.
//
//////////////////////////////////////////////////////////////////////////////



import javax.microedition.lcdui.*;



public final class Laser
  extends GameObject
{
  // CONSTRUCTOR
  //
  
  public Laser( final int screenWidth,
                final int screenHeight,
                final Image image )
  {
    super( 0, 0, 0, 0, screenWidth, screenHeight, image );

    addBoundingBox(
      new BoundingBox( new Point( -1*image.getWidth()/2,
                                  -1*image.getHeight()/2 ),
                       new Point( image.getWidth()/2,
                                  image.getHeight()/2 ) ) );       
  }

  // METHODS
  //
  
  public void set( final int x, final int y )
  {
    pt.x = x;
    pt.y = y;
  }

  public void update()
  {
    super.update();

    if( pt.x < 0 || pt.x > screenWidth ||
        pt.y < 0 || pt.y > screenHeight )
      invalidate();
  }
}
