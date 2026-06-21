package kr.seenby.hidden_bussan.domain.share.entity;

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
import kr.seenby.hidden_bussan.domain.user.entity.User;
import kr.seenby.hidden_bussan.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "collection_shares",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_collection_shares_share_token", columnNames = "share_token")
        },
        indexes = {
                @Index(name = "idx_collection_shares_user_id", columnList = "user_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectionShare extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "share_token", nullable = false, length = 100)
    private String shareToken;

    @Column(name = "share_url", nullable = false, length = 500)
    private String shareUrl;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    private CollectionShare(User user, String shareToken, String shareUrl, boolean isPublic) {
        this.user = user;
        this.shareToken = shareToken;
        this.shareUrl = shareUrl;
        this.isPublic = isPublic;
    }

    public static CollectionShare create(User user, String shareToken, String shareUrl, boolean isPublic) {
        return new CollectionShare(user, shareToken, shareUrl, isPublic);
    }
}
