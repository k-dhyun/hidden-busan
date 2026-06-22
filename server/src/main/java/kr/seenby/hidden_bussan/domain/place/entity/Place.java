package kr.seenby.hidden_bussan.domain.place.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import kr.seenby.hidden_bussan.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "places",
        indexes = {
                @Index(name = "idx_places_region", columnList = "region"),
                @Index(name = "idx_places_category", columnList = "category"),
                @Index(name = "idx_places_difficulty", columnList = "difficulty")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String region;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    private String category;

    @Column(nullable = false, length = 20)
    private String difficulty;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false, length = 50)
    private String duration;

    @Column(name = "best_time", nullable = false, length = 100)
    private String bestTime;

    private Place(
            String region,
            String name,
            String category,
            String difficulty,
            String description,
            String address,
            String imageUrl,
            String duration,
            String bestTime
    ) {
        this.region = region;
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.description = description;
        this.address = address;
        this.imageUrl = imageUrl;
        this.duration = duration;
        this.bestTime = bestTime;
    }

    public static Place create(
            String region,
            String name,
            String category,
            String difficulty,
            String description,
            String address,
            String imageUrl,
            String duration,
            String bestTime
    ) {
        return new Place(region, name, category, difficulty, description, address, imageUrl, duration, bestTime);
    }

    public void update(
            String region,
            String name,
            String category,
            String difficulty,
            String description,
            String address,
            String imageUrl,
            String duration,
            String bestTime
    ) {
        this.region = region;
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.description = description;
        this.address = address;
        this.imageUrl = imageUrl;
        this.duration = duration;
        this.bestTime = bestTime;
    }
}
