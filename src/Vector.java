public class Vector {
    public double x;
    public double y;

    /*********
     * Creates a new Vector with initial values (0,0).
     */
    public Vector() {
        this.set(0, 0);
    }

    /*********
     * Creates a new Vector with the specified initial values.
     * @param x the initial x-coordinate
     * @param y the initial y-coordinate
     */
    public Vector(double x, double y) {
        set(x, y);
    }

    /*********
     * Sets the x and y coordinates of the vector.
     * @param x the new x-coordinate
     * @param y the new y-coordinate
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /********
     * Adds the given values to the x and y coordinates of the vector.
     * @param x the value to add to the x-coordinate
     * @param y the value to add to the y-coordinate
     */
    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    /*********
     * Multiplies the vector's components by the given value.
     * @param m the value by which to multiply the components
     */
    public void multiply(double m) {
        this.x *= m;
        this.y *= m;
    }

    /*********
     * Calculates and returns the length (magnitude) of the vector using trigonometry.
     * @return the length of the vector
     */
    public double getLength() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**********
     * Sets the length of the vector while preserving its direction.
     * @param len the new length for the vector
     */
    public void setLength(double len) {
        double currentLength = this.getLength();

        if (currentLength == 0) {
            this.set(len, 0);
        } else {
            this.multiply(1 / currentLength);
            this.multiply(len);
        }
    }

    /*********
     * Calculates and returns the angle of the vector in degrees.
     * @return the angle of the vector in degrees
     */
    public double getAngle() {
        return Math.toDegrees(Math.atan2(this.y, this.x));
    }

    /********
     * Sets the angle of the vector given an angle in degrees.
     * @param angleDegrees the new angle for the vector in degrees
     */
    public void setAngle(double angleDegrees) {
        double len = this.getLength();
        double angleRadians = Math.toRadians(angleDegrees);
        this.x = len * Math.cos(angleRadians);
        this.y = len * Math.sin(angleRadians);
    }
}
