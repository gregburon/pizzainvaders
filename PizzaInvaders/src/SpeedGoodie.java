//////////////////////////////////////////////////////////////////////////////
//
//  SpeedGoodie.java
//
//  Copyright Deep Blue Future Software
//  June 10, 2004 All rights reserved.
//
//////////////////////////////////////////////////////////////////////////////

import javax.microedition.lcdui.*;


public class SpeedGoodie
  extends Goodie
{

  // CONSTRUCTORS
  //

  public SpeedGoodie( final int x,
                      final int y,
                      final int deltaX,
                      final int deltaY,
                      final int screenWidth,
                      final int screenHeight,
                      final int xBits,
                      final int yBits,
                      final long seed, 
                      final Image image )
  {
    super( x, y, deltaX, deltaY, screenWidth, screenHeight, xBits, yBits, seed, image );

    addBoundingBox(
      new BoundingBox( new Point( -1*image.getWidth()/2,
                                  -1*image.getHeight()/2 ),
                       new Point( image.getWidth()/2,
                                  image.getHeight()/2 ) ) );            
  }

  // METHODS
  //
  public boolean update( final PlayerShip playerShip )
  {
    boolean retVal = super.update( playerShip );

    if( pt.y - image.getHeight()/2 > screenHeight )
      invalidate();

    return retVal;
  }

  /**
   * 
   *
   * @param player a <code>PlayerShip</code> value
   */
  public void applyEffectsToPlayer( final PlayerShip playerShip )
  {
    System.err.println( "=== applying speed goodie..." );
    playerShip.incrementSpeed();
  }
}
