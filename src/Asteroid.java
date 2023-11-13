/***********
 * PRATIK BASYAL
 * CS 351
 */

/**********
 * Description : This is a java implementation of an Asteroid Game.
 * Rules and Logics are described in the doc.
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class Asteroid extends Application {
    protected BorderPane root;
    protected int score;
    protected Canvas canvas;
    protected GraphicsContext context;
    protected Scene startScene;
    protected Scene gameScene;

    protected Sprite spaceship;
    private Restart restart;

    protected ArrayList<Sprite> bulletsList;
    protected ArrayList<Sprite> asteroidsList;
    protected ArrayList<Sprite> explosionList;
    String playerName = "";

    protected boolean gameOver = false;

    protected AnimationTimer gameLoop;
    protected VBox restartButtonContainer;

    public static void main(String[] args) {
        launch(args);
    }


    /********
     * Start method.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Asteroids");

        // Background music for home page
        Media bgMusic = new Media(getClass().getResource("audio/background.mp3").toURI().toString());
        MediaPlayer bgMusicPlayer = new MediaPlayer(bgMusic);

        // Background music for space while playing
        Media spaceSound = new Media(getClass().getResource("audio/spaceb.mp3").toURI().toString());
        MediaPlayer space = new MediaPlayer(spaceSound);

        bgMusicPlayer.play();

        // Video file
        Media media = new Media(getClass().getResource("Home.mp4").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Set the MediaView to the full size of the window
        mediaView.setFitWidth(1200);
        mediaView.setFitHeight(800);
        mediaView.setPreserveRatio(false); // Prevent maintaining aspect ratio

        // Set the video to loop indefinitely
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // Play the video
        mediaPlayer.play();

        // Create the start screen layout
        StackPane startLayout = new StackPane();
        VBox startContent = new VBox(20);
        startContent.setAlignment(Pos.CENTER);

        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter Your Name");
        nameInput.setAlignment(Pos.CENTER); // Center the text field

        Button startGameButton = new Button("Start Game");
        startGameButton.setStyle("-fx-font-size: 20px"); // Set the button's font size

        startContent.getChildren().addAll(nameInput, startGameButton);
        startLayout.getChildren().addAll(mediaView, startContent);

        startScene = new Scene(startLayout, 1200, 800);
        primaryStage.setScene(startScene);

        root = new BorderPane();
        canvas = new Canvas(1200, 800);
        context = canvas.getGraphicsContext2D();
        root.setCenter(canvas);
        gameScene = new Scene(root);

        startGameButton.setOnAction(event -> {
            playerName = nameInput.getText();
            primaryStage.setScene(gameScene);
            startGame(gameScene);
            bgMusicPlayer.stop();
            space.setCycleCount(MediaPlayer.INDEFINITE);
            space.play();
        });

        // Start your game and create an instance of the Restart class
        restart = new Restart();

        restart.getRestartButton().setOnAction(event -> {
            if (gameOver) {
                resetGame(); // Call the resetGame method to restart the game
                gameOver = false;
                restartButtonContainer.setVisible(false);
            }
        });

        // Create a VBox to hold the restart button and center it
        restartButtonContainer = new VBox(restart.getRestartButton());
        restartButtonContainer.setAlignment(Pos.CENTER);
        restartButtonContainer.setVisible(false); // Initially hide the restart button

        // Create a StackPane to overlay the game and restart button
        StackPane gameAndRestartLayout = new StackPane(canvas, restartButtonContainer);
        root.setCenter(gameAndRestartLayout);

        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    /*********
     * This function helps starting the game and setting everything up
     * @param scene
     */
    protected void startGame(Scene scene) {
        // This is to handle continuous inputs (as long as the key is pressed)
        ArrayList<String> keyPressedList = new ArrayList<>();
        // Handle discrete inputs (once per key press)
        ArrayList<String> keyPressedOnceList = new ArrayList<>();

        scene.setOnKeyPressed((KeyEvent event) -> {
            String keyName = event.getCode().toString();
            if (!keyPressedList.contains(keyName)) {
                keyPressedList.add(keyName);
                keyPressedOnceList.add(keyName);
            } else {
                keyPressedList.remove(keyName);
                keyPressedOnceList.remove(keyName);
            }
        });

        Sprite backgroundImage = new Sprite("images/galaxy-2.jpg");
        backgroundImage.position.set(600, 400);
        backgroundImage.setImage(canvas.getWidth(), canvas.getHeight());
        backgroundImage.render(context);

        spaceship = new Sprite("images/spaceship2.png");
        spaceship.position.set(900, 700);
        spaceship.setImage(100, 100);
        spaceship.velocity.set(100, 0);
        spaceship.render(context);

        bulletsList = new ArrayList<>();
        asteroidsList = new ArrayList<>();
        explosionList = new ArrayList<>();

        int asteroidsCount = 20;
        for (int n = 0; n < asteroidsCount; n++) {
            Sprite asteroid = new Sprite("images/asteroid.png");
            asteroid.setImage(50, 50);
            double x = 500 * Math.random() + 300; // 300 - 800
            double y = 400 * Math.random() + 100; // 100 - 500
            asteroid.position.set(x, y);
            double angle = 360 * Math.random();
            asteroid.velocity.setLength(50);
            asteroid.velocity.setAngle(angle);
            asteroidsList.add(asteroid);
        }

        score = 0;
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long nanoTime) {
                // Process user input
                if (keyPressedList.contains("LEFT")) {
                    spaceship.rotation -= 2;
                }
                if (keyPressedList.contains("RIGHT")) {
                    spaceship.rotation += 2;
                }

                if (keyPressedList.contains("UP")) {
                    if (score < 50) spaceship.velocity.setLength(100);
                    if (score >= 50 && score < 100) spaceship.velocity.setLength(200);
                    if (score >= 100 && score <= 150) spaceship.velocity.setLength(300);
                    if (score > 150) spaceship.velocity.setLength(400);
                    spaceship.velocity.setAngle(spaceship.rotation);
                } else { // Not pressing UP
                    spaceship.velocity.setLength(0);
                }
                if (keyPressedOnceList.contains("SPACE")) {
                    double spaceshipCenterX = spaceship.position.x + spaceship.imageView.getFitWidth() / 2;
                    double spaceshipCenterY = spaceship.position.y + spaceship.imageView.getFitHeight() / 2;
                    Sprite bullet = new Sprite("images/laserBullet.png");
                    bullet.setImage(100, 100);
                    bullet.position.set(spaceshipCenterX, spaceshipCenterY);
                    bullet.velocity.setLength(600);
                    bullet.velocity.setAngle(spaceship.rotation);
                    bulletsList.add(bullet);

                    //getClass().getResource("audio/laser.mp3").toURI().toString());
                    Media laserSound = new Media(Objects.requireNonNull(getClass().getResource("audio/laser.mp3")).toExternalForm());
                    MediaPlayer laser = new MediaPlayer(laserSound);
                    laser.play();
                }
                // After processing user input, clear the justPressedOnce list
                keyPressedOnceList.clear();
                spaceship.update(1 / 60.0);

                for (Sprite asteroid : asteroidsList) {
                    asteroid.update(1 / 60.0);
                }

                // Update bullets; destroy them after 2 seconds
                for (int i = 0; i < bulletsList.size(); i++) {
                    Sprite bullet = bulletsList.get(i);
                    bullet.update(1 / 60.0);
                    if (bullet.elapsedTime > 1) {
                        bulletsList.remove(bullet);
                    }
                }

                for (int i = 0; i < explosionList.size(); i++) {
                    Sprite explode = explosionList.get(i);
                    explode.update(1 / 60.0);
                    if (explode.elapsedTime > 0.5) {
                        explosionList.remove(explode);
                    }
                }

                // When bullets overlap asteroids
                for (int bulletNum = 0; bulletNum < bulletsList.size(); bulletNum++) {
                    Sprite bullet = bulletsList.get(bulletNum);
                    for (int asteroidNum = 0; asteroidNum < asteroidsList.size(); asteroidNum++) {
                        Sprite asteroid = asteroidsList.get(asteroidNum);
                        if (bullet.overlaps(asteroid)) {
                            // Explosion sound
                            Media explode1 = new Media(Objects.requireNonNull(getClass().getResource("audio/asteroid.mp3")).toExternalForm());
                            MediaPlayer explosion = new MediaPlayer(explode1);
                            explosion.play();
                            Sprite explode = new Sprite("images/ex.png");
                            explode.position.set(asteroid.position.x, asteroid.position.y);
                            explode.setImage(100, 100);
                            explosionList.add(explode);
                            asteroidsList.remove(asteroid);
                            bulletsList.remove(bullet);
                            score += 10;
                            for (Sprite asteroid1 : asteroidsList) {
                                if (score < 50) {
                                    asteroid1.velocity.setLength(90);
                                    asteroid1.setImage(50, 50);
                                }
                                if (score >= 50 && score < 100) {
                                    asteroid1.velocity.setLength(110);
                                    asteroid1.setImage(70, 70);
                                }
                                if (score >= 100 && score < 150) {
                                    asteroid1.velocity.setLength(150);
                                    asteroid1.setImage(150, 150);
                                }
                                if (score >= 150) {
                                    asteroid1.velocity.setLength(300);
                                    asteroid1.setImage(150, 150);
                                }
                            }
                        }
                    }
                }

                // When spaceship overlaps an asteroid
                for (int asteroidNum = 0; asteroidNum < asteroidsList.size(); asteroidNum++) {
                    Sprite asteroid = asteroidsList.get(asteroidNum);
                    if (asteroid.overlaps(spaceship)) {
                        Sprite explode = new Sprite("images/ex.png");
                        explode.position.set(spaceship.position.x, spaceship.position.y);
                        explode.setImage(150, 150);
                        explosionList.add(explode);
                        gameOver = true;
                    }
                }

                backgroundImage.render(context);
                spaceship.render(context);

                for (Sprite bullet : bulletsList) {
                    bullet.render(context);
                }

                for (Sprite asteroid : asteroidsList) {
                    asteroid.render(context);
                }

                for (Sprite explode : explosionList) {
                    explode.render(context);
                }

                if(score == 200) gameOver = true;
                // If the game is over, display "Game Over" and a restart button
                if (gameOver) {
                    context.setFill(Color.AQUA);
                    context.setFont(new Font("Arial Black", 64));

                    if(score == 200) context.fillText("Game Over \n YOU WIN!!!!", 400, 500);
                    else {
                        context.fillText("Game Over\nYOU LOST :(", 400, 500);
                    }

                    // Show the restart button
                    restartButtonContainer.setVisible(true);

                    Media endGame = new Media(Objects.requireNonNull(getClass().getResource("audio/gameOver.mp3")).toExternalForm());
                    MediaPlayer ended = new MediaPlayer(endGame);
                    ended.play();

                    gameLoop.stop();
                } else {
                    context.setFill(Color.WHITE);
                    context.setStroke(Color.GREEN);
                    context.setFont(new Font("Arial Black", 48));
                    context.setLineWidth(3);
                    String text = playerName.toUpperCase() + " : " + score;
                    int textX = 800;
                    int textY = 80;
                    context.fillText(text, textX, textY);
                    context.strokeText(text, textX, textY);
                }
            }
        };
        gameLoop.start();
    }


    /*********
     * This function resets the game
     */
    protected void resetGame() {
        // Reset game variables
        score = 0;
        spaceship.position.set(900, 700);
        spaceship.velocity.setLength(0);

        // Clear existing bullets and asteroids
        bulletsList.clear();
        asteroidsList.clear();

        // Create new asteroids
        for (int n = 0; n < 20; n++) {
            Sprite asteroid = new Sprite("images/asteroid.png");
            asteroid.setImage(50, 50);
            double x = 500 * Math.random() + 300; // 300 - 800
            double y = 400 * Math.random() + 100; // 100 - 500
            asteroid.position.set(x, y);
            double angle = 360 * Math.random();
            asteroid.velocity.setLength(50);
            asteroid.velocity.setAngle(angle);
            asteroidsList.add(asteroid);
        }

        // Start a new game loop
        gameLoop.start();
    }
}