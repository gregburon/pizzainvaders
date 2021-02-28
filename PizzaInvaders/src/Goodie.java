


import javax.microedition.lcdui.*;


import java.util.*;

public abstract class Goodie
  extends GameObject
{

  public Goodie( final int x,
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
    super( x, y, deltaX, deltaY, screenWidth, screenHeight, image );

    this.xBits = xBits;
    this.yBits = yBits;

    invalidate();

    generator = new Random();
    generator.setSeed( seed );
  }

  private long generateSeed()
  {
    return ( (long)generator.nextInt() << 8 ) & (long)generator.nextInt();
  }
  
  public void invalidate()
  {
    super.invalidate();

    if( generator != null )
      generator.setSeed( generateSeed() );
  }
  
  public boolean update( final PlayerShip playerShip )
  {
    if( !isValid() )
    {
      int chance = Math.abs( generator.nextInt() ) >> GOODIE_SHIFT_BITS;
      int maxValue = 0x000001 << ( 32 - GOODIE_SHIFT_BITS - 1 );
      
      if( chance >= ( maxValue - 1 )  )
      {
        activate();
        int x = Math.abs( generator.nextInt() ) >> xBits;
        int width = image.getWidth()/2;
        
        if( x < width )
          x = width;
        else if( x > screenWidth - width )
          x = screenWidth - width;
        
        pt.x = x;
        pt.y = -1 * image.getHeight()/2;
        setSpeeds( 0, 1 );
      }
    }
    else
    {
      super.update();

      if( intersect( playerShip ) )
      {
        // Collide with player ship...
        applyEffectsToPlayer( playerShip );
        invalidate();

        // Return true if the goodie was picked up.
        return true;
      }
    }

    return false;
  }
    
  public abstract void applyEffectsToPlayer( final PlayerShip player );


  // ATTRIBUTES
  //

  private Random generator;
  
  private int xBits;
  private int yBits;


  // STATICS
  //

  /**
   * The shift bits indicate how likely a goodie will appear when that goodie
   * is invalid.  A higher shift bits will indicate less numbers of a 32 bit
   * integer for the number of chances that a goodie will show up, so therefore
   * the max number ( which activate the goodie ) will have more of a chance of
   * activating the goodie.  Use higher numbers for more frequent goodies,
   * lower numbers for less frequent goodies.
   */
  private static final int GOODIE_SHIFT_BITS = 20;
}
