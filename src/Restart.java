import javafx.scene.control.Button;

/********
 * The Restart class represents a restart button for the game.
 */
public class Restart {
    private Button restartButton;

    public Restart() {
        restartButton = new Button("Restart Game");
        restartButton.setStyle("-fx-font-size: 20px;");
    }

    /*******
     * Gets the restart button.
     * @return the restart button
     */
    public Button getRestartButton() {
        return restartButton;
    }
}
