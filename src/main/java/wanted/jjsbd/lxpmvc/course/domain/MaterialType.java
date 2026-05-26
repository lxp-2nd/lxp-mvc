package wanted.jjsbd.lxpmvc.course.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MaterialType {
	DOCUMENT("문서");

	private final String displayName;
}
