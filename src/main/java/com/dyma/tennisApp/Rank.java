package com.dyma.tennisApp;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record Rank(
        @Positive(message = "need to be positive") int position,
        @PositiveOrZero(message = "need to be positive or 0") int points) {
}
