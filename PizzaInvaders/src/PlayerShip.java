//////////////////////////////////////////////////////////////////////////////
//
//  PlayerShip.java
//
//  Copyright Deep Blue Future Software
//  June 3, 2004 All rights reserved.
//
//////////////////////////////////////////////////////////////////////////////


import javax.microedition.lcdui.*;

import java.io.IOException;

/**
 * Describe class <code>PlayerShip</code> here.
 *
 * @author <a href="mailto:">Greg Buron</a>
 * @version 1.0
 */
public final class PlayerShip
  extends GameObject
{

  // CONSTRUCTOR
  //
  
  /**
   * Creates a new <code>PlayerShip</code> instance.
   *
   * @param screenWidth an <code>int</code> value
   * @param screenHeight an <code>int</code> value
   * @param playerShipImage an <code>Image</code> value
   */
  public PlayerShip( final int screenWidth,
                     final int screenHeight,
                     final Image playerShipImage,
                     final Image playerShieldImage )
  {
    super( 0, 0, 0, 0, screenWidth, screenHeight, playerShipImage );

    this.playerShieldImage = playerShieldImage;
    
    buffer = new StringBuffer();
    fireSpeedFactor = 1;
    speed = 1;
    shieldFrameCount = -1;
    shields = 10;
    
    pt.x = screenWidth / 2;
    pt.y = screenHeight - playerShipImage.getHeight() / 2;

    addBoundingBox(
      new BoundingBox( new Point( -6, -12 ),
                       new Point( 6, 14 ) ) );
  }

  // METHODS
  //

  /**
   * Update the position of the player ship.  ......
   *
   * @param elapsedMillis an <code>int</code> value
   */
  public void update( final int deltaX, final int deltaY, final int elapsedMillis )
  {
    pt.x += ( deltaX * speed );
    pt.y += ( deltaY * speed );

    if( pt.x - image.getWidth()/2 < 0 )
      pt.x = image.getWidth()/2;
    
    if( pt.x + image.getWidth()/2 > screenWidth )
      pt.x = screenWidth - image.getWidth()/2;
    
    timeSinceLastFire += elapsedMillis;    
  }

  /**
   * Resets the time that the last time the player gun is fired.
   *
   */
  public void resetGun()
  {
    timeSinceLastFire = 0;
  }

  /**
   * Returns the recharge status of the player gun (can the player fire).
   *
   * @return a <code>boolean</code> value
   */
  public boolean canFire()
  {
    if( timeSinceLastFire >
        STANDARD_FIRE_TIME_INCREMENT / Math.abs( fireSpeedFactor ) )
      return true;
    else
      return false;
  }

  /**
   * Modify the player score.
   *
   * @param deltaScore an <code>int</code> value
   */
  public void incrementScore( final int deltaScore )
  {
    score += deltaScore;
  }

  /**
   * Increments how fast the player can fire.
   */
  public void incrementFireSpeed()
  {
    if( fireSpeedFactor <= 4 )
      fireSpeedFactor++;      
  }

  /**
   * Speed indicates the movement multiplier for the player's ship.
   */
  public void incrementSpeed()
  {
    if( speed <= 3 )
      speed += 1;
  }

  public void decrementShields()
  {
    shields -= 1;
    shieldFrameCount = 0;
  }

  public void paint( final Graphics g )
  {
    super.paint( g );

    if( shieldFrameCount >= 0 )
    {
      g.drawImage( playerShieldImage, pt.x, pt.y,
                   Graphics.VCENTER | Graphics.HCENTER );
      
      shieldFrameCount++;

      if( shieldFrameCount > MAX_SHIELD_FRAMES )
        shieldFrameCount = -1;
    }
  }
  
  /**
   * Draw the player's score to the screen.
   *
   * @param g a <code>Graphics</code> value
   */
  public void paintScore( final Graphics g )
  {
    int oldColor = g.getColor();
    g.setColor( 0xFF0000 );
    buffer.delete( 0, buffer.length() );
    buffer.append( score );
    String output = buffer.toString();
    g.drawString( output, g.getClipWidth()/2, 0, Graphics.TOP | Graphics.HCENTER );
    g.setColor( oldColor );
  }    
  
  

  // ATTRIBUTES
  //

  private Image playerShieldImage;
  
  private long timeSinceLastFire;
  private int score;
  private StringBuffer buffer;
  private int fireSpeedFactor;
  private int speed;
  private int shieldFrameCount;
  private int shields;


  // STATICS
  //

  private static final long STANDARD_FIRE_TIME_INCREMENT = 1000;

  private static final int MAX_SHIELD_FRAMES = 20;
  
}
