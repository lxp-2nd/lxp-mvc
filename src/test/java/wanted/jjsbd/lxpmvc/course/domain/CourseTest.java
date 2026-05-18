package wanted.jjsbd.lxpmvc.course.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wanted.jjsbd.lxpmvc.member.domain.Member;

import static org.assertj.core.api.Assertions.*;

class CourseTest {

    private Member createMember() {
        return new Member();
    }

    // ── createCourse ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("유효한 인자로 강의를 생성하면 필드가 올바르게 설정된다")
    void createCourse_validArgs_setsFields() {
        Member instructor = createMember();

        Course course = Course.createCourse(instructor, "스프링 입문", "스프링 부트 강의입니다.");

        assertThat(course.getInstructor()).isSameAs(instructor);
        assertThat(course.getTitle()).isEqualTo("스프링 입문");
        assertThat(course.getDescription()).isEqualTo("스프링 부트 강의입니다.");
    }

    @Test
    @DisplayName("설명(description)이 null이어도 강의 생성에 성공한다")
    void createCourse_nullDescription_succeeds() {
        Member instructor = createMember();

        Course course = Course.createCourse(instructor, "스프링 입문", null);

        assertThat(course.getTitle()).isEqualTo("스프링 입문");
        assertThat(course.getDescription()).isNull();
    }

    @Test
    @DisplayName("instructor가 null이면 IllegalArgumentException이 발생한다")
    void createCourse_nullInstructor_throwsException() {
        assertThatThrownBy(() -> Course.createCourse(null, "스프링 입문", "설명"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강사 정보는 필수입니다.");
    }

    @Test
    @DisplayName("title이 null이면 IllegalArgumentException이 발생한다")
    void createCourse_nullTitle_throwsException() {
        Member instructor = createMember();

        assertThatThrownBy(() -> Course.createCourse(instructor, null, "설명"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의 제목은 필수입니다.");
    }

    @Test
    @DisplayName("title이 빈 문자열이면 IllegalArgumentException이 발생한다")
    void createCourse_emptyTitle_throwsException() {
        Member instructor = createMember();

        assertThatThrownBy(() -> Course.createCourse(instructor, "", "설명"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의 제목은 필수입니다.");
    }

    @Test
    @DisplayName("title이 공백만 있는 문자열이면 IllegalArgumentException이 발생한다")
    void createCourse_blankTitle_throwsException() {
        Member instructor = createMember();

        assertThatThrownBy(() -> Course.createCourse(instructor, "   ", "설명"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의 제목은 필수입니다.");
    }

    // ── updateInfo ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("유효한 인자로 강의 정보를 수정하면 필드가 업데이트된다")
    void updateInfo_validArgs_updatesFields() {
        Course course = Course.createCourse(createMember(), "원래 제목", "원래 설명");

        course.updateInfo("새 제목", "새 설명");

        assertThat(course.getTitle()).isEqualTo("새 제목");
        assertThat(course.getDescription()).isEqualTo("새 설명");
    }

    @Test
    @DisplayName("updateInfo 시 description을 null로 변경할 수 있다")
    void updateInfo_nullDescription_updatesFieldToNull() {
        Course course = Course.createCourse(createMember(), "원래 제목", "원래 설명");

        course.updateInfo("새 제목", null);

        assertThat(course.getDescription()).isNull();
    }

    @Test
    @DisplayName("updateInfo 시 title이 null이면 IllegalArgumentException이 발생한다")
    void updateInfo_nullTitle_throwsException() {
        Course course = Course.createCourse(createMember(), "원래 제목", "원래 설명");

        assertThatThrownBy(() -> course.updateInfo(null, "새 설명"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의 제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("updateInfo 시 title이 빈 문자열이면 IllegalArgumentException이 발생한다")
    void updateInfo_emptyTitle_throwsException() {
        Course course = Course.createCourse(createMember(), "원래 제목", "원래 설명");

        assertThatThrownBy(() -> course.updateInfo("", "새 설명"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의 제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("updateInfo 시 title이 공백만 있는 문자열이면 IllegalArgumentException이 발생한다")
    void updateInfo_blankTitle_throwsException() {
        Course course = Course.createCourse(createMember(), "원래 제목", "원래 설명");

        assertThatThrownBy(() -> course.updateInfo("   ", "새 설명"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강의 제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("강의 생성 후 ID는 null이다 (DB 저장 전)")
    void createCourse_idIsNullBeforePersist() {
        Course course = Course.createCourse(createMember(), "제목", "설명");

        assertThat(course.getId()).isNull();
    }
}