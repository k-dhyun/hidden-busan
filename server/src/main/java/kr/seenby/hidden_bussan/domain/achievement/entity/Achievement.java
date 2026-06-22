package kr.seenby.hidden_bussan.domain.achievement.entity;

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
        name = "achievements",
        indexes = {
                @Index(name = "idx_achievements_condition_type", columnList = "condition_type")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Achievement extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false, length = 50)
    private String icon;

    @Column(name = "condition_type", nullable = false, length = 30)
    private String conditionType;

    @Column(name = "condition_value", nullable = false, length = 255)
    private String conditionValue;

    @Column(name = "condition_total")
    private Integer conditionTotal;

    private Achievement(
            String title,
            String description,
            String icon,
            String conditionType,
            String conditionValue,
            Integer conditionTotal
    ) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
        this.conditionTotal = conditionTotal;
    }

    public static Achievement create(
            String title,
            String description,
            String icon,
            String conditionType,
            String conditionValue,
            Integer conditionTotal
    ) {
        return new Achievement(title, description, icon, conditionType, conditionValue, conditionTotal);
    }

    public void update(
            String description,
            String icon,
            String conditionType,
            String conditionValue,
            Integer conditionTotal
    ) {
        this.description = description;
        this.icon = icon;
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
        this.conditionTotal = conditionTotal;
    }
}
