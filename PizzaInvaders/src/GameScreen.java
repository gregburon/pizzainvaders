


import javax.microedition.lcdui.*;
import java.util.*;

import java.util.Vector;


public class GameScreen 
	extends Canvas
	implements Runnable
{

	/**
   * Creates a new <code>GameScreen</code> instance.
   *
   */
  public GameScreen()
	{
		//set up the games random number generator
		generator = new Random( System.currentTimeMillis() );

    scrollSpeed = 1;
    asteroidSpeedFactor = 1;
    
    loadImages();

    // Instantiate the player object.
    playerShip = new PlayerShip( getWidth(), getHeight(),
                                 playerShipImage, playerShieldImage );

    // Initialize the array of player lasers.
    lasers = new Laser[MAX_PLAYER_LASERS];
    for( int i=0; i<MAX_PLAYER_LASERS; ++i )
    {
      lasers[i] = new Laser( getWidth(), getHeight(), laserImage );
      lasers[i].set( 0, 0 );
      lasers[i].setSpeeds( 0, 0 );
      lasers[i].invalidate();
    }

    // Initialize the array of background stars.
    stars = new Point[MAX_STARS];

    xBits = getMaxBits( getWidth() );
    yBits = getMaxBits( getHeight() );
    
    for( int j=0; j<MAX_STARS; ++j )
    {
      int x = Math.abs( generator.nextInt() ) >> ( xBits );
      int y = Math.abs( generator.nextInt() ) >> ( yBits );
      stars[j] = new Point( x, y );
    }


    // Initialize the asteroid objects.
    asteroids = new Asteroid[MAX_ASTEROIDS];

    for( int k=0; k<MAX_ASTEROIDS; ++k )
      asteroids[k] =
        new Asteroid( 0, 0, 0, 0, asteroidImage, 1, getWidth(), getHeight() );
    

    // Initialize the explosion points.
    explosion = new Point[MAX_EXPLOSION_DOTS];

    for( int l=0; l<MAX_EXPLOSION_DOTS; ++l )
      explosion[l] = new Point( -1, -1 );


    // Initialize the goodies.
    laserGoodie =
      new LaserGoodie( 0, 0, 0, 0, getWidth(), getHeight(),
                       xBits, yBits, (long)12345678, laserGoodieImage );

    pointGoodie =
      new PointGoodie( 0, 0, 0, 0, getWidth(), getHeight(),
                       xBits, yBits, (long)54323945, pointGoodieImage );

    speedGoodie =
      new SpeedGoodie( 0, 0, 0, 0, getWidth(), getHeight(),
                       xBits, yBits, (long)923748295, speedGoodieImage );
    
      
    
    
		//create a new Thread on this Runnable and start it immediately
    new Thread( this ).start();		
  }

  private int getMaxBits( final int maxValue )
  {
    int testVal = Integer.MAX_VALUE;
    int counter = 0;

    while( testVal > maxValue )
    {
      testVal /= 2;
      counter++;
    }

    return --counter;    
  }
  

  private boolean loadImages()
  {
		try
		{
			if( !isDoubleBuffered() )
			{
				bufferImage = Image.createImage( getWidth(), getHeight() );
				buffer = bufferImage.getGraphics();
			}

      playerShipImage = Image.createImage( "/player.png" );
      playerShieldImage = Image.createImage( "/playerShield.png" );
      laserImage = Image.createImage( "/laser.png" );
      asteroidImage = Image.createImage( "/asteroid.png" );
      laserGoodieImage = Image.createImage( "/laserGoodie.png" );
      pointGoodieImage = Image.createImage( "/pointsGoodie.png" );
      speedGoodieImage = Image.createImage( "/speedGoodie.png" );
		}
		catch( Exception exception )
		{
			exception.printStackTrace();
      return false;
		}

    return true;
  }
  
	
	/*
	 * run() method defined in the Runnable interface, called by the 
	 * Virtual machine when a Thread is started.
	 */
	public void run()
	{
		while( true )
		{
			// set wanted loop delay
			int loopDelay = LOOP_DELAY_VALUE; 
      
			// get the time at the start of the loop
			long loopStartTime = System.currentTimeMillis();
      
			// call our tick() fucntion which will be our games heartbeat
			tick();
      
			// get time at end of loop
			long loopEndTime  = System.currentTimeMillis();
      
			// caluclate the difference in time from start til end of loop
			int loopTime = (int)(loopEndTime - loopStartTime);
      
			// if the difference is less than what we want
			if( loopTime < loopDelay )
			{
				try
				{
					//then sleep for the time needed to fullfill our wanted rate
					Thread.sleep( loopDelay - loopTime );
				}
				catch( Exception e )
				{
				}
			}
		}
	}
	
	/*
	 * Our games main loop, called at a fixed rate by our game Thread
	 */
	public void tick()
	{
    int deltaX = 0;
    int deltaY = 0;

    if( isLeftDown )  deltaX += -1;
    if( isRightDown ) deltaX +=  1;
    
    playerShip.update( deltaX, deltaY, LOOP_DELAY_VALUE );
    

    for( int a=0; a<MAX_STARS; ++a )
    {
      stars[a].y += scrollSpeed;
      
      if( stars[a].y > getHeight() )
      {
        int x = generator.nextInt();
        int y = generator.nextInt();
        
        stars[a].x = Math.abs( x ) >> xBits;
        stars[a].y = ( Math.abs( y ) >> ( yBits ) ) - getHeight()*2;
      }
    }    

    for( int i=0; i<MAX_PLAYER_LASERS; ++i )
      if( lasers[i].isValid() )
        lasers[i].update();

    if( isFireDown && playerShip.canFire() )
    {
      // Find the first available non-active laser and use it.
      int laserHeight = laserImage.getHeight()*2;
      
      for( int i=0; i<lasers.length; ++i )
      {
        if( !lasers[i].isValid() )
        {       
          // Activate this laser.
          lasers[i].set( playerShip.pt.x, playerShip.pt.y - laserHeight );
          lasers[i].setSpeeds( 0, PLAYER_LASER_SPEED );
          lasers[i].activate();
          
          break;
        }
      }
      
      playerShip.resetGun();
    }

    if( Math.abs( generator.nextInt() ) < Integer.MAX_VALUE / 20 )
    {
      // Try to get another asteroid on the screen.
      for( int i=0; i<MAX_ASTEROIDS; ++i )
      {
        if( !asteroids[i].isValid() )
        {
          asteroids[i].activate();
          int x = generator.nextInt();
          asteroids[i].pt.x = Math.abs( x ) >> xBits;
          asteroids[i].pt.y = -1 * asteroidImage.getHeight()/2;
          asteroids[i].setSpeeds( 0, ASTEROID_SPEED * asteroidSpeedFactor );
          break;
        }
      }
    }

    for( int a=0; a<MAX_ASTEROIDS; ++a )
      if( asteroids[a].isValid() )
        asteroids[a].update();



    // Check for asteroid collisions with stuff.
    for( int b=0; b<MAX_ASTEROIDS; ++b )
    {
      if( asteroids[b].isValid() )
      {
        // Check if asteroid and laser intersect.
        for( int c=0; c<MAX_PLAYER_LASERS; ++c )
        {
          if( lasers[c].isValid() )
          {
            // Check if this laser and asteroid connect.
            if( lasers[c].intersect( asteroids[b] ) )
            {
              destroyAsteroid( asteroids[b] );
              lasers[c].invalidate();
              playerShip.incrementScore( ASTEROID_SCORE );              
            }
          }
        }// end for( lasers )

        // Check for asteroid player intersect.
        if( playerShip.intersect( asteroids[b] ) )
        {
          playerShip.decrementShields();
          destroyAsteroid( asteroids[b] );
        }// end if( playerShip )
      }
    }

    if( laserGoodie.update( playerShip ) )
      pickups++;
    
    if( speedGoodie.update( playerShip ) )
      pickups++;

    if( pickups > 2 )
    {
      asteroidSpeedFactor++;
      pickups = 0;
    }

    pointGoodie.update( playerShip );    
    
		repaint();
		serviceRepaints();
	}

  private void destroyAsteroid( final Asteroid asteroid )
  {
    for( int d=0; d<MAX_EXPLOSION_DOTS; ++d )
    {
      int x = Math.abs( generator.nextInt() ) >> 26;
      int y = Math.abs( generator.nextInt() ) >> 26;
                
      explosion[d].x = asteroid.pt.x + x - 16;
      explosion[d].y = asteroid.pt.y + y - 16;
    }
    
    asteroid.invalidate();
  }
  
	
	/* 
	 * called when the Canvas is to be painted
	 */
	protected void paint( Graphics g )
	{
		Graphics original = g;    

		if( !isDoubleBuffered() )
			g = buffer;

    // Blank the background.
		g.setColor( 0x000000 );
		g.fillRect( 0, 0, this.getWidth(), this.getHeight() );

    
    // Draw the game graphics, in ascending z-order.
    for( int a=0; a<MAX_STARS; ++a )
    {
      g.setColor( generator.nextInt() );
      g.drawRect( stars[a].x, stars[a].y, 1, 1 );
    } 
    
    for( int b=0; b<MAX_ASTEROIDS; ++b )
    {
      if( asteroids[b].pt.y > 0 && asteroids[b].pt.y < getHeight()*2 )
        asteroids[b].paint( g );
    }

    // Paint goodies.
    if( laserGoodie.isValid() )
      laserGoodie.paint( g );

    if( pointGoodie.isValid() )
      pointGoodie.paint( g );

    if( speedGoodie.isValid() )
      speedGoodie.paint( g );

    
    // Draw player ship.
    playerShip.paint( g );

    // Draw active lasers.
    for( int i=0; i<lasers.length; ++i )
      if( lasers[i].isValid() )
        lasers[i].paint( g );

    // Draw the explosion if its valid.
    if( explosion[0].x != -1 )
    {
      for( int i=0; i<MAX_EXPLOSION_DOTS; ++i )
      {
        int color = generator.nextInt() >> 9;
        color &= 0xFF0000;
        g.setColor( color );
        g.drawRect( explosion[i].x, explosion[i].y,
                    EXPLOSION_PARTICLE_SIZE, EXPLOSION_PARTICLE_SIZE );
        explosion[i].x = -1;
        explosion[i].y = -1;
      }
    }
    
    playerShip.paintScore( g );
    
		if( !isDoubleBuffered() )
		{
			//draw the offscreen Image to the original graphics context 
			original.drawImage( bufferImage, 0, 0, Graphics.TOP | Graphics.LEFT );
		}		
	}
	
	/* 
	* called when a key is pressed and this Canvas is the
	* current Displayable 
	*/
	protected void keyPressed( int keyCode )
	{
		//get the game action from the passed keyCode
		int gameAction = getGameAction( keyCode );
    
		switch( gameAction )
		{
			case LEFT:
        isLeftDown = true;
        break;
			case RIGHT:
        isRightDown = true;
        break;
			case GAME_A:
        isFireDown = true;
        break;
		}
	}

	/* 
	* called when a key is released  and this Canvas is the
	* current Displayable 
	*/
	protected void keyReleased( int keyCode )
	{
		//get the game action from the passed keyCode
		int gameAction = getGameAction( keyCode );

		switch( gameAction )
		{
			case LEFT:
        isLeftDown = false;
        break;
			case RIGHT:
        isRightDown = false;
        break;
			case GAME_A:
        isFireDown = false;
        break;
		}    
	}


  // ATTRIBUTES
  //
  
  private Random generator;
	private Graphics buffer;
  private Image bufferImage;
  private Image playerShipImage;
  private Image playerShieldImage;
  private Image laserImage;
  private Image asteroidImage;
  private Image laserGoodieImage;
  private Image pointGoodieImage;
  private Image speedGoodieImage;
  
  private PlayerShip playerShip;
  
  private boolean isLeftDown = false;
  private boolean isRightDown = false;
  private boolean isFireDown = false;

  private Laser[] lasers;
  private Point[] stars;
  private Point[] explosion;
  private Asteroid[] asteroids;
  private LaserGoodie laserGoodie;
  private PointGoodie pointGoodie;
  private SpeedGoodie speedGoodie;

  private int xBits;
  private int yBits;

  private int scrollSpeed;
  private int asteroidSpeedFactor;
  private int pickups;
  
    
  // STATICS
  //

  private static final int MAX_PLAYER_LASERS = 10;

  private static final int MAX_STARS = 20;

  private static final int MAX_ASTEROIDS = 10;

  private static final int PLAYER_LASER_SPEED = -5;

  private static final int ASTEROID_SPEED = 1;

  private static final int LOOP_DELAY_VALUE = 1000 / 33;

  private static final int ASTEROID_SCORE = 10;

  private static final int MAX_EXPLOSION_DOTS = 25;

  private static final int EXPLOSION_PARTICLE_SIZE = 2;
}
