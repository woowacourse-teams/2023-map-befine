package com.mapbefine.mapbefine.entity;

import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Topic extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 20)
	private String name;
	@Lob
	@Column(nullable = false)
	private String description;
	@OneToMany(mappedBy = "topic")
	private List<Pin> pins = new ArrayList<>();
	@Column(nullable = false)
	@ColumnDefault(value = "false")
	private boolean isDeleted = false;

	public Topic(
		String name,
		String description
	) {
		this.name = name;
		this.description = description;
	}

	public void update(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public int countPins() {
		return pins.size();
	}

	public void addPin(Pin pin) {
		pins.add(pin);
	}

}
