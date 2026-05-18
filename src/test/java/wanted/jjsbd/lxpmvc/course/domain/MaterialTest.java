package wanted.jjsbd.lxpmvc.course.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wanted.jjsbd.lxpmvc.member.domain.Member;

import static org.assertj.core.api.Assertions.*;

class MaterialTest {

    private Section createSection() {
        Course course = Course.createCourse(new Member(), "테스트 강의", "설명");
        return Section.createSection(course, "테스트 섹션", 1);
    }

    // ── createMaterial ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("유효한 인자로 강의자료를 생성하면 필드가 올바르게 설정된다 (VIDEO)")
    void createMaterial_validArgs_video_setsFields() {
        Section section = createSection();

        Material material = Material.createMaterial(section, "1강. 개요", MaterialType.VIDEO, "https://example.com/video.mp4", 1);

        assertThat(material.getSection()).isSameAs(section);
        assertThat(material.getTitle()).isEqualTo("1강. 개요");
        assertThat(material.getMaterialType()).isEqualTo(MaterialType.VIDEO);
        assertThat(material.getContentUrl()).isEqualTo("https://example.com/video.mp4");
        assertThat(material.getSequence()).isEqualTo(1);
    }

    @Test
    @DisplayName("유효한 인자로 강의자료를 생성하면 필드가 올바르게 설정된다 (DOCUMENT)")
    void createMaterial_validArgs_document_setsFields() {
        Section section = createSection();

        Material material = Material.createMaterial(section, "강의 자료", MaterialType.DOCUMENT, "https://example.com/doc.pdf", 2);

        assertThat(material.getMaterialType()).isEqualTo(MaterialType.DOCUMENT);
        assertThat(material.getSequence()).isEqualTo(2);
    }

    @Test
    @DisplayName("contentUrl이 null이어도 강의자료 생성에 성공한다")
    void createMaterial_nullContentUrl_succeeds() {
        Section section = createSection();

        Material material = Material.createMaterial(section, "1강. 개요", MaterialType.VIDEO, null, 1);

        assertThat(material.getContentUrl()).isNull();
    }

    @Test
    @DisplayName("section이 null이면 IllegalArgumentException이 발생한다")
    void createMaterial_nullSection_throwsException() {
        assertThatThrownBy(() ->
                Material.createMaterial(null, "1강. 개요", MaterialType.VIDEO, "https://example.com/video.mp4", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("소속된 섹션 정보는 필수입니다.");
    }

    @Test
    @DisplayName("title이 null이면 IllegalArgumentException이 발생한다")
    void createMaterial_nullTitle_throwsException() {
        assertThatThrownBy(() ->
                Material.createMaterial(createSection(), null, MaterialType.VIDEO, "https://example.com/video.mp4", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의자료 제목은 필수입니다.");
    }

    @Test
    @DisplayName("title이 빈 문자열이면 IllegalArgumentException이 발생한다")
    void createMaterial_emptyTitle_throwsException() {
        assertThatThrownBy(() ->
                Material.createMaterial(createSection(), "", MaterialType.VIDEO, "https://example.com/video.mp4", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의자료 제목은 필수입니다.");
    }

    @Test
    @DisplayName("title이 공백만 있는 문자열이면 IllegalArgumentException이 발생한다")
    void createMaterial_blankTitle_throwsException() {
        assertThatThrownBy(() ->
                Material.createMaterial(createSection(), "   ", MaterialType.VIDEO, "https://example.com/video.mp4", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의자료 제목은 필수입니다.");
    }

    @Test
    @DisplayName("materialType이 null이면 IllegalArgumentException이 발생한다")
    void createMaterial_nullMaterialType_throwsException() {
        assertThatThrownBy(() ->
                Material.createMaterial(createSection(), "1강. 개요", null, "https://example.com/video.mp4", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자료 유형(VIDEO/DOCUMENT)은 필수입니다.");
    }

    @Test
    @DisplayName("sequence가 null이면 IllegalArgumentException이 발생한다")
    void createMaterial_nullSequence_throwsException() {
        assertThatThrownBy(() ->
                Material.createMaterial(createSection(), "1강. 개요", MaterialType.VIDEO, "https://example.com/video.mp4", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("순서는 1 이상의 유효한 값이어야 합니다.");
    }

    @Test
    @DisplayName("sequence가 0이면 IllegalArgumentException이 발생한다")
    void createMaterial_zeroSequence_throwsException() {
        assertThatThrownBy(() ->
                Material.createMaterial(createSection(), "1강. 개요", MaterialType.VIDEO, "https://example.com/video.mp4", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("순서는 1 이상의 유효한 값이어야 합니다.");
    }

    @Test
    @DisplayName("sequence가 음수이면 IllegalArgumentException이 발생한다")
    void createMaterial_negativeSequence_throwsException() {
        assertThatThrownBy(() ->
                Material.createMaterial(createSection(), "1강. 개요", MaterialType.VIDEO, "https://example.com/video.mp4", -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("순서는 1 이상의 유효한 값이어야 합니다.");
    }

    // ── updateMaterial ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("유효한 인자로 강의자료를 수정하면 필드가 업데이트된다")
    void updateMaterial_validArgs_updatesFields() {
        Material material = Material.createMaterial(createSection(), "원래 제목", MaterialType.VIDEO, "https://example.com/old.mp4", 1);

        material.updateMaterial("새 제목", MaterialType.DOCUMENT, "https://example.com/new.pdf", 2);

        assertThat(material.getTitle()).isEqualTo("새 제목");
        assertThat(material.getMaterialType()).isEqualTo(MaterialType.DOCUMENT);
        assertThat(material.getContentUrl()).isEqualTo("https://example.com/new.pdf");
        assertThat(material.getSequence()).isEqualTo(2);
    }

    @Test
    @DisplayName("updateMaterial 시 contentUrl을 null로 변경할 수 있다")
    void updateMaterial_nullContentUrl_updatesFieldToNull() {
        Material material = Material.createMaterial(createSection(), "원래 제목", MaterialType.VIDEO, "https://example.com/video.mp4", 1);

        material.updateMaterial("새 제목", MaterialType.VIDEO, null, 1);

        assertThat(material.getContentUrl()).isNull();
    }

    @Test
    @DisplayName("updateMaterial 시 title이 null이면 IllegalArgumentException이 발생한다")
    void updateMaterial_nullTitle_throwsException() {
        Material material = Material.createMaterial(createSection(), "원래 제목", MaterialType.VIDEO, null, 1);

        assertThatThrownBy(() -> material.updateMaterial(null, MaterialType.VIDEO, null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의자료 제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("updateMaterial 시 title이 빈 문자열이면 IllegalArgumentException이 발생한다")
    void updateMaterial_emptyTitle_throwsException() {
        Material material = Material.createMaterial(createSection(), "원래 제목", MaterialType.VIDEO, null, 1);

        assertThatThrownBy(() -> material.updateMaterial("", MaterialType.VIDEO, null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의자료 제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("updateMaterial 시 title이 공백만 있는 문자열이면 IllegalArgumentException이 발생한다")
    void updateMaterial_blankTitle_throwsException() {
        Material material = Material.createMaterial(createSection(), "원래 제목", MaterialType.VIDEO, null, 1);

        assertThatThrownBy(() -> material.updateMaterial("  ", MaterialType.VIDEO, null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의자료 제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("updateMaterial 시 materialType이 null이면 IllegalArgumentException이 발생한다")
    void updateMaterial_nullMaterialType_throwsException() {
        Material material = Material.createMaterial(createSection(), "원래 제목", MaterialType.VIDEO, null, 1);

        assertThatThrownBy(() -> material.updateMaterial("새 제목", null, null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("자료 유형은 필수입니다.");
    }

    @Test
    @DisplayName("updateMaterial 시 sequence가 null이면 IllegalArgumentException이 발생한다")
    void updateMaterial_nullSequence_throwsException() {
        Material material = Material.createMaterial(createSection(), "원래 제목", MaterialType.VIDEO, null, 1);

        assertThatThrownBy(() -> material.updateMaterial("새 제목", MaterialType.VIDEO, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("순서는 1 이상의 유효한 값이어야 합니다.");
    }

    @Test
    @DisplayName("updateMaterial 시 sequence가 0이면 IllegalArgumentException이 발생한다")
    void updateMaterial_zeroSequence_throwsException() {
        Material material = Material.createMaterial(createSection(), "원래 제목", MaterialType.VIDEO, null, 1);

        assertThatThrownBy(() -> material.updateMaterial("새 제목", MaterialType.VIDEO, null, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("순서는 1 이상의 유효한 값이어야 합니다.");
    }

    @Test
    @DisplayName("updateMaterial 시 sequence가 음수이면 IllegalArgumentException이 발생한다")
    void updateMaterial_negativeSequence_throwsException() {
        Material material = Material.createMaterial(createSection(), "원래 제목", MaterialType.VIDEO, null, 1);

        assertThatThrownBy(() -> material.updateMaterial("새 제목", MaterialType.VIDEO, null, -3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("순서는 1 이상의 유효한 값이어야 합니다.");
    }

    @Test
    @DisplayName("강의자료 생성 후 ID는 null이다 (DB 저장 전)")
    void createMaterial_idIsNullBeforePersist() {
        Material material = Material.createMaterial(createSection(), "1강. 개요", MaterialType.VIDEO, null, 1);

        assertThat(material.getId()).isNull();
    }
}