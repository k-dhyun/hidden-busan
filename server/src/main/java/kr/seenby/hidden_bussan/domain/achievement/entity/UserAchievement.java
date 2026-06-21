package kr.seenby.hidden_bussan.domain.achievement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import kr.seenby.hidden_bussan.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "user_achievements",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_achievements_user_achievement", columnNames = {"user_id", "achievement_id"})
        },
        indexes = {
                @Index(name = "idx_user_achievements_user_unlocked_at", columnList = "user_id, unlocked_at"),
                @Index(name = "idx_user_achievements_achievement_id", columnList = "achievement_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    @Column(name = "unlocked_at", nullable = false)
    private LocalDateTime unlockedAt;

    private UserAchievement(User user, Achievement achievement, LocalDateTime unlockedAt) {
        this.user = user;
        this.achievement = achievement;
        this.unlockedAt = unlockedAt;
    }

    public static UserAchievement create(User user, Achievement achievement, LocalDateTime unlockedAt) {
        return new UserAchievement(user, achievement, unlockedAt);
    }
}
