package kr.seenby.hidden_bussan.domain.place.dto;

import kr.seenby.hidden_bussan.domain.place.entity.Place;

public record ContentResponse(
        String id,
        String name,
        String region,
        String category,
        String difficulty,
        String description,
        String address,
        String image,
        String duration,
        String bestTime
) {

    public static ContentResponse from(Place place) {
        return new ContentResponse(
                String.valueOf(place.getId()),
                place.getName(),
                place.getRegion(),
                place.getCategory(),
                place.getDifficulty(),
                place.getDescription(),
                place.getAddress(),
                place.getImageUrl(),
                place.getDuration(),
                place.getBestTime()
        );
    }
}
