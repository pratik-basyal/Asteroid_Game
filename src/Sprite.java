import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The Sprite class represents a game object with a position, velocity, rotation, image, and more.
 */
public class Sprite {
    public Vector position;
    public Vector velocity;
    public double rotation; // degrees (angle)
    public Rectangle boundary;
    public ImageView imageView; // each sprite is represented as an image
    public double elapsedTime; // in seconds

    /***********
     * Creates a new Sprite with default values.
     */
    public Sprite() {
        this.position = new Vector();
        this.velocity = new Vector();
        this.rotation = 0;
        this.boundary = new Rectangle();
        imageView = new ImageView();
        elapsedTime = 0;
    }

    /***********
     * Creates a new Sprite with the specified image file.
     * @param imageFileName the file name of the image for the sprite
     */
    protected Sprite(String imageFileName) {
        this(); // calling the default constructor
        this.imageView.setImage(new Image(getClass().getResource(imageFileName).toString()));
    }

    /***********
     * Sets the dimensions of the sprite's image.
     * @param width  the width of the image
     * @param height the height of the image
     */
    public void setImage(double width, double height) {
        // Set fixed width and height for the ImageView
        this.imageView.setFitWidth(width);
        this.imageView.setFitHeight(height);
        this.boundary.setSize(this.imageView.getFitWidth(), this.imageView.getFitHeight());
    }

    /***********
     * Gets the boundary of the sprite.
     * @return the boundary of the sprite
     */
    public Rectangle getBoundary() {
        this.boundary.setPosition(this.position.x, this.position.y);
        return this.boundary;
    }

    /***********
     * Checks if this sprite overlaps with another sprite.
     * @param other the other sprite to check for overlap
     * @return true if the sprites overlap, false otherwise
     */
    public boolean overlaps(Sprite other) {
        return this.getBoundary().overlaps(other.getBoundary());
    }

    /**********
     * Wraps the sprite around the screen if it goes out of bounds.
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    public void wrap(double screenWidth, double screenHeight) {
        // TODO: take into account the center of the object
        double halfWidth = this.imageView.getFitWidth() / 2;
        double halfHeight = this.imageView.getFitHeight() / 2;

        if (this.position.x + halfWidth < 0) this.position.x = screenWidth + halfWidth;

        if (this.position.x > screenWidth + halfWidth) this.position.x = -halfWidth;

        if (this.position.y + halfHeight < 0) this.position.y = screenHeight + halfHeight;
        if (this.position.y > screenHeight + halfHeight) this.position.y = -halfHeight;
    }

    /********
     * Updates the sprite's position and properties.
     * @param deltaTime the time elapsed since the last update
     */
    public void update(double deltaTime) {
        // Update the position according to the velocity
        this.position.add(this.velocity.x * deltaTime, this.velocity.y * deltaTime);
        // Wrap around the screen
        this.wrap(1200, 800);
        // Increase elapsed time for the sprite
        this.elapsedTime += deltaTime;
    }

    /***********
     * Renders the sprite on the given graphics context.
     * @param context the graphics context on which to render the sprite
     */
    public void render(GraphicsContext context) {
        context.save();

        context.translate(this.position.x, this.position.y); // moves to the current location (x, y)

        context.rotate(this.rotation); // rotates the canvas by the sprite's rotation

        context.translate(-this.imageView.getFitWidth() / 2,
                -this.imageView.getFitHeight() / 2); // sprite rotates
        // around its center rather than a corner

        // Render the ImageView instead of the Image
        context.drawImage(this.imageView.getImage(), 0, 0, this.imageView.getFitWidth(),
                this.imageView.getFitHeight());

        context.restore();
    }
}
