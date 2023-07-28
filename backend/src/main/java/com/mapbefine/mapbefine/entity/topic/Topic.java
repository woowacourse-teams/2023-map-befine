package com.mapbefine.mapbefine.entity.topic;

import static lombok.AccessLevel.PROTECTED;

import com.mapbefine.mapbefine.entity.BaseEntity;
import com.mapbefine.mapbefine.entity.pin.Pin;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Topic extends BaseEntity {

    private static final String DEFAULT_IMAGE_URL = "https://map-befine-official.github.io/favicon.png";
    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final int MAX_NAME_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Lob
    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, length = 2048)
    private String imageUrl = DEFAULT_IMAGE_URL;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.PERSIST)
    private List<Pin> pins = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Publicity publicity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission permission;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean isDeleted = false;

    public Topic(
            String name,
            String description,
            String imageUrl
    ) {
        validateName(name);
        validateDescription(description);

        this.name = name;
        this.description = description;

        if (imageUrl != null) { // TODO : 정팩매 레츠기릿
            this.imageUrl = imageUrl;
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name null");
        }
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름 길이 이상");
        }
    }

    private void validateDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("description null");
        }
        if (description.isBlank() || description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("description 길이 이상");
        }
    }

    public void update(
            String name,
            String description,
            String imageUrl
    ) {
        validateName(name);
        validateDescription(description);
        // TODO : URL 형식 검증 필요?

        this.name = name;
        this.description = description;
        if (imageUrl != null) { // TODO : 생성자와 중복 코드
            this.imageUrl = imageUrl;
        }
    }

    public int countPins() {
        return pins.size();
    }

    public void addPin(Pin pin) {
        pins.add(pin);
    }

    public boolean isPublic() {
        return publicity == Publicity.PUBLIC;
    }

    public boolean isPrivate() {
        return publicity == Publicity.PRIVATE;
    }

}
