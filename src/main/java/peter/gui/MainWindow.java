package peter.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import peter.Peter;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Peter peter;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/peteruser.jfif"));
    private Image peterImage = new Image(this.getClass().getResourceAsStream("/images/peterdog.jfif"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren().add(
                DialogBox.getPeterDialog("Hello! I'm Peter.\nWhat can I do for you?", peterImage)
        );
    }

    /** Injects the Peter instance */
    public void setPeter(Peter p) {
        peter = p;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = peter.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getPeterDialog(response, peterImage)
        );
        userInput.clear();
        if (input.equals("bye")) {
            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
            delay.setOnFinished(event -> javafx.application.Platform.exit());
            delay.play();
        }
    }
}

