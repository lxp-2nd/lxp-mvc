package wanted.jjsbd.lxpmvc.common.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wanted.jjsbd.lxpmvc.course.domain.Course;
import wanted.jjsbd.lxpmvc.member.domain.Member;

import static org.assertj.core.api.Assertions.*;

class BaseTimeEntityTest {

    /**
     * BaseTimeEntity는 추상 클래스이므로 테스트용 구체 서브클래스를 사용합니다.
     * Course는 실제로 BaseTimeEntity를 상속하므로 대표로 활용합니다.
     */

    @Test
    @DisplayName("BaseTimeEntity를 상속한 Course는 getCreatedAt() 메서드를 가진다")
    void baseTimeEntity_subclass_hasGetCreatedAt() {
        Course course = Course.createCourse(new Member(), "테스트 강의", "설명");

        // DB 저장 전이므로 createdAt은 null이어야 한다
        assertThat(course.getCreatedAt()).isNull();
    }

    @Test
    @DisplayName("BaseTimeEntity를 상속한 엔티티는 생성 직후 createdAt이 null이다")
    void baseTimeEntity_createdAt_isNullBeforePersistence() {
        Course course = Course.createCourse(new Member(), "강의 제목", null);

        assertThat(course.getCreatedAt()).isNull();
    }

    @Test
    @DisplayName("BaseTimeEntity 자체가 추상 클래스임을 확인한다")
    void baseTimeEntity_isAbstractClass() {
        assertThat(java.lang.reflect.Modifier.isAbstract(BaseTimeEntity.class.getModifiers())).isTrue();
    }

    @Test
    @DisplayName("BaseTimeEntity는 @MappedSuperclass 어노테이션을 가진다")
    void baseTimeEntity_hasMappedSuperclassAnnotation() {
        assertThat(BaseTimeEntity.class.isAnnotationPresent(MappedSuperclass.class)).isTrue();
    }

    @Test
    @DisplayName("BaseTimeEntity는 @EntityListeners 어노테이션을 가진다")
    void baseTimeEntity_hasEntityListenersAnnotation() {
        assertThat(BaseTimeEntity.class.isAnnotationPresent(EntityListeners.class)).isTrue();
    }

    @Test
    @DisplayName("createdAt 필드는 updatable=false 설정을 가진다")
    void baseTimeEntity_createdAt_fieldIsNotUpdatable() throws NoSuchFieldException {
        java.lang.reflect.Field createdAtField = BaseTimeEntity.class.getDeclaredField("createdAt");
        Column columnAnnotation = createdAtField.getAnnotation(Column.class);

        assertThat(columnAnnotation).isNotNull();
        assertThat(columnAnnotation.updatable()).isFalse();
        assertThat(columnAnnotation.nullable()).isFalse();
    }

    @Test
    @DisplayName("createdAt 필드는 @CreatedDate 어노테이션을 가진다")
    void baseTimeEntity_createdAt_hasCreatedDateAnnotation() throws NoSuchFieldException {
        java.lang.reflect.Field createdAtField = BaseTimeEntity.class.getDeclaredField("createdAt");

        assertThat(createdAtField.isAnnotationPresent(
                org.springframework.data.annotation.CreatedDate.class)).isTrue();
    }
}