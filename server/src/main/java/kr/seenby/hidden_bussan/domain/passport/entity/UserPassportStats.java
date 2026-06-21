package kr.seenby.hidden_bussan.domain.passport.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.seenby.hidden_bussan.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(name = "user_passport_stats")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPassportStats {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "stamp_count", nullable = false)
    private int stampCount;

    @Column(name = "unlocked_achievement_count", nullable = false)
    private int unlockedAchievementCount;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    private UserPassportStats(User user, int stampCount, int unlockedAchievementCount) {
        this.user = user;
        this.stampCount = stampCount;
        this.unlockedAchievementCount = unlockedAchievementCount;
    }

    public static UserPassportStats create(User user, int stampCount, int unlockedAchievementCount) {
        return new UserPassportStats(user, stampCount, unlockedAchievementCount);
    }

    public void updateCounts(int stampCount, int unlockedAchievementCount) {
        this.stampCount = stampCount;
        this.unlockedAchievementCount = unlockedAchievementCount;
    }
}
