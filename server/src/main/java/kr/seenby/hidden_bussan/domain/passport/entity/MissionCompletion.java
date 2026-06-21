package kr.seenby.hidden_bussan.domain.passport.entity;

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
import java.time.LocalDate;
import kr.seenby.hidden_bussan.domain.achievement.entity.Achievement;
import kr.seenby.hidden_bussan.domain.mission.entity.Mission;
import kr.seenby.hidden_bussan.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "mission_completions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_mission_completions_user_mission", columnNames = {"user_id", "mission_id"})
        },
        indexes = {
                @Index(name = "idx_mission_completions_user_completed_at", columnList = "user_id, completed_at"),
                @Index(name = "idx_mission_completions_achievement_id", columnList = "achievement_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id")
    private Achievement achievement;

    @Column(name = "photo_url", nullable = false, length = 500)
    private String photoUrl;

    @Column(name = "stamp_emoji", nullable = false, length = 20)
    private String stampEmoji;

    @Column(nullable = false, length = 255)
    private String memo;

    @Column(name = "completed_at", nullable = false)
    private LocalDate completedAt;

    private MissionCompletion(
            User user,
            Mission mission,
            Achievement achievement,
            String photoUrl,
            String stampEmoji,
            String memo,
            LocalDate completedAt
    ) {
        this.user = user;
        this.mission = mission;
        this.achievement = achievement;
        this.photoUrl = photoUrl;
        this.stampEmoji = stampEmoji;
        this.memo = memo;
        this.completedAt = completedAt;
    }

    public static MissionCompletion create(
            User user,
            Mission mission,
            Achievement achievement,
            String photoUrl,
            String stampEmoji,
            String memo,
            LocalDate completedAt
    ) {
        return new MissionCompletion(user, mission, achievement, photoUrl, stampEmoji, memo, completedAt);
    }
}
