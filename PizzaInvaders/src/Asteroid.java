


import javax.microedition.lcdui.*;


public class Asteroid
  extends Enemy
{
  // CONSTRUCTOR
  //
  
  public Asteroid( final int x,
                   final int y,
                   final int deltaX,
                   final int deltaY,
                   final Image image,
                   final int health,
                   final int screenWidth,
                   final int screenHeight )
  {
    super( x, y, deltaX, deltaY, screenWidth, screenHeight, image, health );

    addBoundingBox(
      new BoundingBox( new Point( -1*image.getWidth()/2,
                                  -1*image.getHeight()/2 ),
                       new Point( image.getWidth()/2,
                                  image.getHeight()/2 ) ) );                               
  }
  
  public void update()
  {
    super.update();
    
    if( pt.y - image.getHeight()/2 > screenHeight )
      invalidate();
  }
}

