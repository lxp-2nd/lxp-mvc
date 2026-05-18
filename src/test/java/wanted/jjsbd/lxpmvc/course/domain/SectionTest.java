package wanted.jjsbd.lxpmvc.course.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wanted.jjsbd.lxpmvc.member.domain.Member;

import static org.assertj.core.api.Assertions.*;

class SectionTest {

    private Course createCourse() {
        return Course.createCourse(new Member(), "테스트 강의", "설명");
    }

    // ── createSection ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("유효한 인자로 섹션을 생성하면 필드가 올바르게 설정된다")
    void createSection_validArgs_setsFields() {
        Course course = createCourse();

        Section section = Section.createSection(course, "1장. 기초", 1);

        assertThat(section.getCourse()).isSameAs(course);
        assertThat(section.getTitle()).isEqualTo("1장. 기초");
        assertThat(section.getSequence()).isEqualTo(1);
    }

    @Test
    @DisplayName("sequence가 1인 최솟값으로 섹션 생성에 성공한다")
    void createSection_sequenceOne_succeeds() {
        Section section = Section.createSection(createCourse(), "섹션 제목", 1);

        assertThat(section.getSequence()).isEqualTo(1);
    }

    @Test
    @DisplayName("course가 null이면 IllegalArgumentException이 발생한다")
    void createSection_nullCourse_throwsException() {
        assertThatThrownBy(() -> Section.createSection(null, "섹션 제목", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("소속된 강의 정보는 필수입니다.");
    }

    @Test
    @DisplayName("title이 null이면 IllegalArgumentException이 발생한다")
    void createSection_nullTitle_throwsException() {
        assertThatThrownBy(() -> Section.createSection(createCourse(), null, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("섹션 제목은 필수입니다.");
    }

    @Test
    @DisplayName("title이 빈 문자열이면 IllegalArgumentException이 발생한다")
    void createSection_emptyTitle_throwsException() {
        assertThatThrownBy(() -> Section.createSection(createCourse(), "", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("섹션 제목은 필수입니다.");
    }

    @Test
    @DisplayName("title이 공백만 있는 문자열이면 IllegalArgumentException이 발생한다")
    void createSection_blankTitle_throwsException() {
        assertThatThrownBy(() -> Section.createSection(createCourse(), "   ", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("섹션 제목은 필수입니다.");
    }

    @Test
    @DisplayName("sequence가 null이면 IllegalArgumentException이 발생한다")
    void createSection_nullSequence_throwsException() {
        assertThatThrownBy(() -> Section.createSection(createCourse(), "섹션 제목", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("순서는 1 이상의 유효한 값이어야 합니다.");
    }

    @Test
    @DisplayName("sequence가 0이면 IllegalArgumentException이 발생한다")
    void createSection_zeroSequence_throwsException() {
        assertThatThrownBy(() -> Section.createSection(createCourse(), "섹션 제목", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("순서는 1 이상의 유효한 값이어야 합니다.");
    }

    @Test
    @DisplayName("sequence가 음수이면 IllegalArgumentException이 발생한다")
    void createSection_negativeSequence_throwsException() {
        assertThatThrownBy(() -> Section.createSection(createCourse(), "섹션 제목", -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("순서는 1 이상의 유효한 값이어야 합니다.");
    }

    // ── updateSection ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("유효한 인자로 섹션 정보를 수정하면 필드가 업데이트된다")
    void updateSection_validArgs_updatesFields() {
        Section section = Section.createSection(createCourse(), "원래 제목", 1);

        section.updateSection("새 제목", 2);

        assertThat(section.getTitle()).isEqualTo("새 제목");
        assertThat(section.getSequence()).isEqualTo(2);
    }

    @Test
    @DisplayName("updateSection 시 title이 null이면 IllegalArgumentException이 발생한다")
    void updateSection_nullTitle_throwsException() {
        Section section = Section.createSection(createCourse(), "원래 제목", 1);

        assertThatThrownBy(() -> section.updateSection(null, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("섹션 제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("updateSection 시 title이 빈 문자열이면 IllegalArgumentException이 발생한다")
    void updateSection_emptyTitle_throwsException() {
        Section section = Section.createSection(createCourse(), "원래 제목", 1);

        assertThatThrownBy(() -> section.updateSection("", 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("섹션 제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("updateSection 시 title이 공백만 있는 문자열이면 IllegalArgumentException이 발생한다")
    void updateSection_blankTitle_throwsException() {
        Section section = Section.createSection(createCourse(), "원래 제목", 1);

        assertThatThrownBy(() -> section.updateSection("   ", 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("섹션 제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("updateSection 시 sequence가 null이면 IllegalArgumentException이 발생한다")
    void updateSection_nullSequence_throwsException() {
        Section section = Section.createSection(createCourse(), "원래 제목", 1);

        assertThatThrownBy(() -> section.updateSection("새 제목", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("순서는 1 이상의 유효한 값이어야 합니다.");
    }

    @Test
    @DisplayName("updateSection 시 sequence가 0이면 IllegalArgumentException이 발생한다")
    void updateSection_zeroSequence_throwsException() {
        Section section = Section.createSection(createCourse(), "원래 제목", 1);

        assertThatThrownBy(() -> section.updateSection("새 제목", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("순서는 1 이상의 유효한 값이어야 합니다.");
    }

    @Test
    @DisplayName("updateSection 시 sequence가 음수이면 IllegalArgumentException이 발생한다")
    void updateSection_negativeSequence_throwsException() {
        Section section = Section.createSection(createCourse(), "원래 제목", 1);

        assertThatThrownBy(() -> section.updateSection("새 제목", -5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("순서는 1 이상의 유효한 값이어야 합니다.");
    }

    @Test
    @DisplayName("섹션 생성 후 ID는 null이다 (DB 저장 전)")
    void createSection_idIsNullBeforePersist() {
        Section section = Section.createSection(createCourse(), "섹션 제목", 1);

        assertThat(section.getId()).isNull();
    }
}
