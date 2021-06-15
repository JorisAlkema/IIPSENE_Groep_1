package Model;

import java.util.Objects;

public class TrainCard {
    private String color;

    // Allow Firebase to create a new instance of the object with an empty constructor,
    // which will be filled in using reflection.
    public TrainCard() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof TrainCard) {
            TrainCard trainCard = (TrainCard) o;
            return this.color.equals(trainCard.color);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }

    public TrainCard(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
