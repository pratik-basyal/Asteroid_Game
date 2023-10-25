public class Rectangle {
    // (x,y) represents the top-left corner of the Rectangle
    double x;
    double y;
    double width;
    double height;

    /********
     * Creates a new Rectangle with default values for position and size.
     */
    public Rectangle() {
        this.setPosition(this.x, this.y);
        this.setSize(this.width, this.height);
    }

    /********
     * Sets the position (top-left corner) of the Rectangle.
     * @param x the x-coordinate of the top-left corner
     * @param y the y-coordinate of the top-left corner
     */
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /********
     * Sets the size (width and height) of the Rectangle.
     * @param w the width of the Rectangle
     * @param h the height of the Rectangle
     */
    public void setSize(double w, double h) {
        this.width = w;
        this.height = h;
    }

    /*********
     * Checks if this Rectangle overlaps with another Rectangle.
     * @param other the other Rectangle to check for overlap
     * @return true if there is an overlap, false otherwise
     */
    public boolean overlaps(Rectangle other) {
        // Cases where there is no overlap
        boolean noOverlap = this.x + this.width < other.x || // No overlap to the left
                this.x > other.x + other.width || // No overlap to the right
                this.y + this.height < other.y || // No overlap above
                this.y > other.y + other.height;   // No overlap below
        return !noOverlap;
    }
}
