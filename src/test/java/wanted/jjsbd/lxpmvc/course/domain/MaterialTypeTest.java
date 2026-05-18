package wanted.jjsbd.lxpmvc.course.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MaterialTypeTest {

    @Test
    @DisplayName("MaterialType에 VIDEO 값이 존재한다")
    void materialType_videoValueExists() {
        assertThat(MaterialType.VIDEO).isNotNull();
    }

    @Test
    @DisplayName("MaterialType에 DOCUMENT 값이 존재한다")
    void materialType_documentValueExists() {
        assertThat(MaterialType.DOCUMENT).isNotNull();
    }

    @Test
    @DisplayName("MaterialType은 정확히 VIDEO와 DOCUMENT 두 가지 값을 가진다")
    void materialType_hasExactlyTwoValues() {
        MaterialType[] values = MaterialType.values();

        assertThat(values).hasSize(2);
        assertThat(values).containsExactlyInAnyOrder(MaterialType.VIDEO, MaterialType.DOCUMENT);
    }

    @Test
    @DisplayName("문자열 'VIDEO'로 MaterialType.VIDEO를 조회할 수 있다")
    void materialType_valueOfVideo_returnsVideo() {
        assertThat(MaterialType.valueOf("VIDEO")).isEqualTo(MaterialType.VIDEO);
    }

    @Test
    @DisplayName("문자열 'DOCUMENT'로 MaterialType.DOCUMENT를 조회할 수 있다")
    void materialType_valueOfDocument_returnsDocument() {
        assertThat(MaterialType.valueOf("DOCUMENT")).isEqualTo(MaterialType.DOCUMENT);
    }

    @Test
    @DisplayName("존재하지 않는 문자열로 조회하면 IllegalArgumentException이 발생한다")
    void materialType_valueOfUnknown_throwsException() {
        assertThatThrownBy(() -> MaterialType.valueOf("AUDIO"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}