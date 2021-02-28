


import javax.microedition.lcdui.*;


public abstract class Enemy
  extends GameObject
{

  public Enemy( final int x,
                final int y,
                final int deltaX,
                final int deltaY,
                final int screenWidth,
                final int screenHeight,
                final Image image,
                final int health )
  {
    super( x, y, deltaX, deltaY, screenWidth, screenHeight, image );
    this.health = health;
  }

  private int health;
}
